package com.microsoft.appcenter.espresso;

import android.net.LocalServerSocket;

import com.microsoft.appcenter.event.Event;
import com.microsoft.appcenter.event.EventType;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class EventServerTest {
    @Test
    public void sendSync_will_return_false_if_no_one_listens() throws Exception {
        String socketName = "junitevent";
        LocalServerSocket socket = new LocalServerSocket(socketName);
        EventServer server = new EventServer(socket, 10, TimeUnit.MILLISECONDS);

        boolean b = server.sendSync(Event.create("id", EventType.started, "testName", "className", "1"));
        server.stop();

        assertFalse("sendSync without a listener should return false", b);
    }

    @Test
    public void sendSync_will_return_true_if_some_one_listens() throws Exception {
        String socketName = "junitevent2";
        LocalServerSocket socket = new LocalServerSocket(socketName);
        EventServer server = new EventServer(socket, 10, TimeUnit.SECONDS);
        TestEventClient client = new TestEventClient(socketName);

        boolean b = server.sendSync(Event.create("id", EventType.started, "testName", "className", "1"));
        server.stop();
        client.stop();

        assertTrue("sendSync with a listener should return true", b);
    }
}