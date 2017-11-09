package com.microsoft.appcenter.espresso;

import android.os.Bundle;
import android.support.test.InstrumentationRegistry;

import com.microsoft.appcenter.event.StdOutEventReporter;

public class Factory {
    private static ReportHelper reportHelper;

    static {
        Bundle arguments = InstrumentationRegistry.getArguments();
        String label = arguments.getString("label");
        if ("true".equals(label)) {
            int timeoutInSec = 1;
            String timeout = arguments.getString("timeoutInSec");
            if(timeout != null) {
                timeoutInSec = Integer.valueOf(timeout);
            }
            reportHelper = new ReportHelper(new LocalSocketEventReporter("junitevent", timeoutInSec));
        } else {
            reportHelper = new ReportHelper(new StdOutEventReporter());
        }
    }

    public static ReportHelper getReportHelper() {
        return reportHelper;
    }

    private Factory() {
    }
}
