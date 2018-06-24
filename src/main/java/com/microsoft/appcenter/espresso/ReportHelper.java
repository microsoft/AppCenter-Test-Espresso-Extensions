package com.microsoft.appcenter.espresso;

import com.microsoft.appcenter.event.EventReporter;
import com.microsoft.appcenter.event.EventType;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.IdentityHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ReportHelper extends TestWatcher {
    private static AtomicInteger helperIdCounter = new AtomicInteger(0);

    private final EventReporter eventReporter;
    private volatile int helperId = -1;

    ReportHelper(EventReporter eventReporter) {
        super();
        this.eventReporter = eventReporter;
    }

    private void ensureOnlyOneReporter() {
        if(helperId == -1) {
            helperId = helperIdCounter.incrementAndGet();
        } else if(helperId != helperIdCounter.get()) {
            throw new IllegalStateException("More than one ReportHelper is active, this will cause report generation to fail. One common cause for this @Rules in test class and in its super class");
        }
    }

    public void label(String label) {
        if (null == label || label.length() < 1 || label.length() > 128) {
            throw new IllegalArgumentException("Labels must be a non-empty string of length <= 128");
        }
        this.eventReporter.reportLabel(label, "NA", -1, false);
    }

    @Override
    protected void succeeded(Description description) {
        super.succeeded(description);
        ensureOnlyOneReporter();
        this.eventReporter.reportJunit(EventType.succeeded, description, null);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        super.failed(e, description);
        ensureOnlyOneReporter();
        this.eventReporter.reportJunit(EventType.failed, description, e);
    }

    @Override
    protected void skipped(org.junit.internal.AssumptionViolatedException e, Description description) {
        this.eventReporter.reportJunit(EventType.skipped, description, e);
        ensureOnlyOneReporter();
        super.skipped(e, description);
    }

    @Override
    protected void starting(Description description) {
        super.starting(description);
        ensureOnlyOneReporter();
        this.eventReporter.reportJunit(EventType.started, description, null);
    }

    @Override
    protected void finished(Description description) {
        super.finished(description);
        ensureOnlyOneReporter();
        this.eventReporter.reportJunit(EventType.finished, description, null);
    }
}
