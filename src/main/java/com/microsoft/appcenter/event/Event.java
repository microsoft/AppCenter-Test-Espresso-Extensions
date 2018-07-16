package com.microsoft.appcenter.event;

public class Event {

    public static final int MAX_LABEL_SIZE = 256;

    private Event(String id, EventType type, String testName, String className, String parameters) {
        if (null == id || id.length() < 1) {
            throw new IllegalArgumentException("id must be a non-empty string");
        }
        if (null == testName || testName.length() < 1) {
            throw new IllegalArgumentException("testName must be a non-empty string");
        }
        if (null == className || className.length() < 1) {
            throw new IllegalArgumentException("className must be a non-empty string");
        }
        this.id = id;
        this.type = type;
        this.testName = testName;
        this.className = className;
        this.parameters = parameters;
        this.timestamp = System.currentTimeMillis() / 1000.0;
    }

    public static Event createLabel(String id, String testName, String className, String run, String label) {
        if (null == label || label.length() < 1 || label.length() > MAX_LABEL_SIZE) {
            throw new IllegalArgumentException("labels must be non-empty strings of length <= " + MAX_LABEL_SIZE);
        }
        Event event = new Event(id, EventType.label, testName, className, run);
        event.label = label;
        return event;
    }

    public static Event createScreenshot(String id, String testName, String className, String run) {
        Event event = new Event(id, EventType.screenshot, testName, className, run);
        return event;
    }

    public static Event createWithException(String id, EventType type, String testName, String className, String run, String exception) {
        Event event = new Event(id, type, testName, className, run);
        event.exception = exception;
        return event;
    }

    public static Event create(String id, EventType type, String testName, String className, String run) {
        return new Event(id, type, testName, className, run);
    }

    private final String id;
    private final EventType type;
    private final String testName;
    private final String className;
    private final double timestamp;
    private final String parameters;

    private String label;
    private String exception;

    public String getId() {
        return id;
    }

    public EventType getType() {
        return type;
    }

    public String getTestName() {
        return testName;
    }

    public String getClassName() {
        return className;
    }

    public String getParameters() {
        return parameters;
    }

    public String getLabel() {
        return label;
    }

    public String getException() {
        return exception;
    }

    public double getTimestamp() {
        return timestamp;
    }

}

