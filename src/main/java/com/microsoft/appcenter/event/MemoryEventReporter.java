package com.microsoft.appcenter.event;

import java.io.IOException;
import java.util.ArrayList;

public class MemoryEventReporter extends EventReporter {

    ArrayList<Event> events = new ArrayList<Event>();

    public ArrayList<Event> getReports() {
        return events;
    }

    public String[] getIds() {
        String[] ids = new String[events.size()];
        for (int i = 0; i < events.size(); i++) {
            ids[i] = events.get(i).getId();
        }
        return ids;
    }

    public boolean hasId(String id) {
        for (Event event : events) {
            if (event.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        events.clear();
    }

    @Override
    protected void commit(Event event) throws IOException {
        events.add(event);
    }
}
