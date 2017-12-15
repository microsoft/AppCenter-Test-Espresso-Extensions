package com.microsoft.appcenter.event;

public class Event {

    private Event(String id, EventType type, String testName, String className, String parameters) {
        this.id = id;
        this.type = type;
        this.testName = testName;
        this.className = className;
        this.parameters = parameters;
        this.timestamp = System.currentTimeMillis() / 1000.0;
    }

    public static Event createLabel(String id, String testName, String className, String run, String label, String screenshotPath, int screenshotOrientation, boolean screenshotRotated) {
        Event event = new Event(id, EventType.label, testName, className, run);
        event.screenshotPath = screenshotPath;
        event.label = label;
        event.screenshotOrientation = screenshotOrientation;
        event.screenshotRotated = screenshotRotated;
        return event;
    }

    public static Event createScreenshot(String id, String testName, String className, String run, String screenshotPath, int screenshotOrientation, boolean screenshotRotated) {
        Event event = new Event(id, EventType.screenshot, testName, className, run);
        event.screenshotPath = screenshotPath;
        event.screenshotOrientation = screenshotOrientation;
        event.screenshotRotated = screenshotRotated;
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

    private String screenshotPath;
    private int screenshotOrientation;
    private boolean screenshotRotated;
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

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public int getScreenshotOrientation() {
        return screenshotOrientation;
    }

    public String getException() {
        return exception;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public boolean isScreenshotRotated() {
        return screenshotRotated;
    }
}

