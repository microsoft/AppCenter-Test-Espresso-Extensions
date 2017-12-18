package com.microsoft.appcenter.event;

import org.junit.runner.Description;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class EventReporter {

    private Set<String> usedIds = new HashSet<String>();
    private Description lastDescription = null;
    private int labelCounter = 0;
    private int screenshotCounter = 0;

    // Parameters will always be in "[]": https://github.com/junit-team/junit4/blob/7111b9621997f6c660687b8ac04003398343ee3a/src/main/java/org/junit/runners/Parameterized.java#L484
    private final Pattern paramsFinder = Pattern.compile("\\[(.+?)\\]");

    public void reportJunit(EventType eventType, Description description, Throwable throwable) {
        String parameters = getParameters(description);
        lastDescription = description;

        try {
            if (throwable != null) {
                StringWriter errors = new StringWriter();
                throwable.printStackTrace(new PrintWriter(errors));
                Event event = Event.createWithException(toId(description, eventType, parameters), eventType, description.getMethodName(), description.getClassName(), parameters, errors.toString());
                commit(event);
            } else {
                Event event = Event.create(toId(description, eventType, parameters), eventType, description.getMethodName(), description.getClassName(), parameters);
                commit(event);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (EventType.started.equals(eventType)) {
            labelCounter = 0;
            screenshotCounter = 0;
        }
    }

    private String getParameters(Description description) {
        if (description == null) {
            throw new RuntimeException("Unable to get test information. Make sure the test class includes a @Rule:\n" +
                    "  @Rule\n" +
                    "  public TestWatcher watcher = Factory.createWatcher();");
        }

        Matcher m = paramsFinder.matcher(description.getMethodName());
        return m.find() ? m.group(1) : "";
    }

    protected abstract void commit(Event event) throws IOException;

    public void reportLabel(String label, String screenshotPath, int screenshotOrientation, boolean screenshotRotated) {
        try {
            String parameters = getParameters(lastDescription);
            Event event = Event.createLabel(labelId(lastDescription, parameters), lastDescription.getMethodName(), lastDescription.getClassName(), parameters, label);
            commit(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reportScreenshot(String screenshotPath, int screenshotOrientation, boolean screenshotRotated) {
        try {
            String parameters = getParameters(lastDescription);
            Event event = Event.createScreenshot(screenshotId(lastDescription, parameters), lastDescription.getMethodName(), lastDescription.getClassName(), parameters);
            commit(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String toId(Description description, EventType eventType, String parameters) {
        String id = new ShortIdentifier(String.format(Locale.US, "j%s:%s:%s", eventType.name().substring(0, 3), description.getDisplayName(), parameters)).value();
        usedIds.add(id);
        return id;
    }

    private String labelId(Description lastDescription, String parameters) {
        String id = new ShortIdentifier(String.format(Locale.US, "jlab:%s:%d:%s", lastDescription.getDisplayName(), labelCounter++, parameters)).value();
        usedIds.add(id);
        return id;
    }

    private String screenshotId(Description lastDescription, String parameters) {
        String id = new ShortIdentifier(String.format(Locale.US, "jscr:%s:%d:%s", lastDescription.getDisplayName(), screenshotCounter++, parameters)).value();
        usedIds.add(id);
        return id;
    }
}
