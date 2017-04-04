package com.xamarin.testcloud.espresso;

import android.os.Bundle;
import android.support.test.InstrumentationRegistry;

import com.xamarin.testcloud.event.StdOutEventReporter;

public class Factory {
    private static ReportHelper reportHelper;

    static {
        Bundle arguments = InstrumentationRegistry.getArguments();
        String label = arguments.getString("label");
        if ("true".equals(label)) {
            int timeoutInSec = Integer.valueOf(arguments.getString("timeoutInSec", "1"));
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
