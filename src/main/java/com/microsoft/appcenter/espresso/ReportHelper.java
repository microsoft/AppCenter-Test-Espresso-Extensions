package com.microsoft.appcenter.espresso;

import com.microsoft.appcenter.event.EventReporter;
import com.microsoft.appcenter.event.EventType;

import net.jcip.annotations.GuardedBy;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.IdentityHashMap;

public class ReportHelper extends TestWatcher {
    private final static Object currentReportHelperLock = new Object();
    @GuardedBy("currentReportHelperLock")
    private final static IdentityHashMap<ReportHelper, Void> oldReportHelpers = new IdentityHashMap<>();
    @GuardedBy("currentReportHelperLock")
    private static ReportHelper currentReportHelper = null;

    private final EventReporter eventReporter;

    ReportHelper(EventReporter eventReporter) {
        super();
        this.eventReporter = eventReporter;
    }

    private void ensureOnlyOneReporter() {
       synchronized (currentReportHelperLock) {
           if(this != currentReportHelper) {
               if(oldReportHelpers.containsKey(this)) {
                   throw new IllegalStateException("More that on ReportHelper is active, this will cause report generation to fail. One common cause for this @Rules in test class and in its super class");
               } else {
                   oldReportHelpers.put(currentReportHelper, null);
                   currentReportHelper = this;
               }
           }
       }
    }

    public void label(String label) {
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