package com.microsoft.appcenter.espresso;

import com.microsoft.appcenter.event.TestableEventReporter;

import org.junit.Test;
import org.junit.runner.Description;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ReportHelperTest {

    @Test
    public void reportHelperWillComplainIfMoreThanOneIsActive() {
        TestableEventReporter eventReporter = new TestableEventReporter();
        Description description = Description.createTestDescription(this.getClass().getCanonicalName(), "name");
        ReportHelper firstHelper = new ReportHelper(eventReporter);
        ReportHelper nextHelper = new ReportHelper(eventReporter);

        firstHelper.starting(description);
        // Its ok to stop using a helper and use another one
        nextHelper.starting(description);
        // but we should fail if we ever use it again
        try {
            firstHelper.finished(description);
            fail("Expected an IllegalArgumentException because there is more than one reporter active");
        } catch (IllegalStateException e) {
            assertThat(e.getMessage(), containsString("@Rules"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLabelsNotAllowed() {
        ReportHelper helper = createHelper();
        helper.label("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLabelsNotAllowed() {
        ReportHelper helper = createHelper();
        helper.label(null);
    }

    @Test
    public void testVeryLongLabelsAreNotAllowed() {
        //128 chars
        ReportHelper helper = createHelper();
        String longLabel = "longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglonglong";
        helper.label(longLabel);
        //expected 128 to be allowed
        try {
            helper.label(longLabel + "1");
            fail("Expected IllegalArgumentException to be raised on 129 length strings");
        } catch (IllegalArgumentException e) {
            //expected raise. All good
        }
    }


    private ReportHelper createHelper() {
        TestableEventReporter eventReporter = new TestableEventReporter();
        Description description = Description.createTestDescription(this.getClass().getCanonicalName(), "name");
        ReportHelper helper = new ReportHelper(eventReporter);
        helper.starting(description);
        return helper;
    }

}