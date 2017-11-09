package com.microsoft.appcenter.espresso;

import com.microsoft.appcenter.event.EventReporter;
import com.microsoft.appcenter.event.EventType;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class ReportHelper extends TestWatcher {
    private final EventReporter eventReporter;

    ReportHelper(EventReporter eventReporter) {
        super();
        this.eventReporter = eventReporter;
    }

    public void label(String label) {
        this.eventReporter.reportLabel(label, "NA", -1, false);
    }

    @Override
    protected void succeeded(Description description) {
        super.succeeded(description);
        this.eventReporter.reportJunit(EventType.succeeded, description, null);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        this.eventReporter.reportJunit(EventType.failed, description, e);
        super.failed(e, description);
    }

    @Override
    protected void skipped(org.junit.internal.AssumptionViolatedException e, Description description) {
        this.eventReporter.reportJunit(EventType.skipped, description, e);
        super.skipped(e, description);
    }

    @Override
    protected void starting(Description description) {
        super.starting(description);
        this.eventReporter.reportJunit(EventType.started, description, null);
    }

    @Override
    protected void finished(Description description) {
        super.finished(description);
        this.eventReporter.reportJunit(EventType.finished, description, null);
    }
}