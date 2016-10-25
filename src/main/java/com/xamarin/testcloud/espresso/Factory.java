package com.xamarin.testcloud.espresso;

import android.support.test.InstrumentationRegistry;

import com.xamarin.testcloud.event.StdOutEventReporter;

public class Factory {
    private static RapportHelper rapportHelper;

    static {
        String label = InstrumentationRegistry.getArguments().getString("label");
        if ("true".equals(label)) {
            rapportHelper = new RapportHelper(new LocalSocketEventReporter("junitevent"));
        } else {
            rapportHelper = new RapportHelper(new StdOutEventReporter());
        }
    }

    public static RapportHelper getRapportHelper() {
        return rapportHelper;
    }

    private Factory() {
    }
}
