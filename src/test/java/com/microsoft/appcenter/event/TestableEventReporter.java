package com.microsoft.appcenter.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestableEventReporter extends EventReporter {
    private final List<Event> commitedEvents = new ArrayList<>();

    public List<Event> getCommitedEvents() {
        return commitedEvents;
    }

    public void clearCommitedEvents() {
        commitedEvents.clear();
    }

    @Override
    protected void commit(Event event) throws IOException {
        commitedEvents.add(event);

    }
}
