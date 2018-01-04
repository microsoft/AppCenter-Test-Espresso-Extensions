package com.microsoft.appcenter.espresso;

import android.os.Bundle;
import android.support.test.InstrumentationRegistry;

import com.microsoft.appcenter.event.EventReporter;
import com.microsoft.appcenter.event.StdOutEventReporter;

public class Factory {
    private static EventReporter eventReporter;

    static {
        Bundle arguments = InstrumentationRegistry.getArguments();
        String label = arguments.getString("label");
        if ("true".equals(label)) {
            int timeoutInSec = 1;
            String timeout = arguments.getString("timeoutInSec");
            if(timeout != null) {
                timeoutInSec = Integer.valueOf(timeout);
            }
            eventReporter = new LocalSocketEventReporter("junitevent", timeoutInSec);
        } else {
            eventReporter = new StdOutEventReporter();
        }
    }

    public static ReportHelper getReportHelper() {
        return new ReportHelper(eventReporter);
    }

    private Factory() {
    }
}
