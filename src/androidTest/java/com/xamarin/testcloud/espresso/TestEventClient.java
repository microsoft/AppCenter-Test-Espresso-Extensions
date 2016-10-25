package com.xamarin.testcloud.espresso;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestEventClient implements Runnable {
    private final String name;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Future<?> task;

    public TestEventClient(String name) {
        this.name = name;
        this.task = executorService.submit(this);
    }

    public void stop() {
        boolean cancel = task.cancel(true);
        executorService.shutdownNow();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            LocalSocket socket = null;
            try {
                socket = new LocalSocket();
                socket.connect(new LocalSocketAddress(name));

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());

                while (true) {
                    String line = EventServer.readLine(inputStreamReader);
                    bufferedWriter.write("ok\n");
                    bufferedWriter.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
