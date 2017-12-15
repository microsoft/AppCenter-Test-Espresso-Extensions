package com.microsoft.appcenter.event;

import org.junit.Test;
import org.junit.runner.Description;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EventReporterTest {


    @Test
    public void reportJunitWillCommitEvent() throws Exception {
        TestableEventReporter eventReporter = new TestableEventReporter();
        eventReporter.reportJunit(EventType.failed, Description.createTestDescription(this.getClass(), "AFailure"), null);

        assertThat("event should be committed", eventReporter.getCommitedEvents().size(), is(1));
    }
}