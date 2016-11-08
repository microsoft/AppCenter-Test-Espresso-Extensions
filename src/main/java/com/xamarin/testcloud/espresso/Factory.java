package com.xamarin.testcloud.espresso;

import android.support.test.InstrumentationRegistry;

import com.xamarin.testcloud.event.StdOutEventReporter;

public class Factory {
    private static ReportHelper reportHelper;

    static {
        String label = InstrumentationRegistry.getArguments().getString("label");
        if ("true".equals(label)) {
            reportHelper = new ReportHelper(new LocalSocketEventReporter("junitevent"));
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
