package com.microsoft.appcenter.espresso;

import android.net.LocalServerSocket;
import android.net.LocalSocket;

import com.google.gson.Gson;
import com.microsoft.appcenter.event.Event;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class EventServer implements Runnable {
    private final LocalServerSocket socket;
    private final SynchronousQueue<String> queue = new SynchronousQueue<String>();

    private final Lock sendStatusLock = new ReentrantLock();
    private final Condition sendStatusSet = sendStatusLock.newCondition();
    private final int timeout;
    private final TimeUnit unit;
    private Boolean successfulAck = null; // Guarded by sendStatusLock

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Future<?> serverTask;

    private final Gson gson = new Gson();

    EventServer(LocalServerSocket socket, int timeout, TimeUnit unit) {
        this.socket = socket;
        serverTask = executorService.submit(this);
        this.timeout = timeout;
        this.unit = unit;
    }

    static String readLine(Reader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        do {
            int c = reader.read();
            if(c == -1 || c == '\n') {
                break;
            }
            builder.append((char)c);
        } while (true);
        return builder.toString();
    }

    public boolean sendSync(Event event) {
        String asjson = gson.toJson(event);
        try {
            boolean wasAccepted = queue.offer(asjson, timeout, unit);
            if (!wasAccepted) {
                return false;
            }

            if (sendStatusLock.tryLock(timeout, unit)) {
                try {
                    while (successfulAck == null) {
                        boolean statusInTime = sendStatusSet.await(timeout, unit);
                        if (!statusInTime) {
                            return false;
                        }
                    }
                    return successfulAck;
                } finally {
                    successfulAck = null;
                    sendStatusLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            // Ignored
        }
        return false;
    }

    public void stop() throws InterruptedException {
        boolean cancel = serverTask.cancel(true);
        executorService.shutdownNow();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            LocalSocket accept = null;
            try {
                accept = socket.accept();

                InputStreamReader reader = new InputStreamReader(accept.getInputStream());
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));

                boolean ackOK = false;
                do {
                    String string = queue.take();
                    try {
                        bufferedWriter.write(string);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        String status = readLine(reader);
                        ackOK = "ok".equals(status);
                    } finally {
                        sendStatusLock.lock();
                        try {
                            successfulAck = ackOK;
                            sendStatusSet.signal();
                        } finally {
                            sendStatusLock.unlock();
                        }
                    }
                } while (ackOK);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (accept != null) {
                    try {
                        accept.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
