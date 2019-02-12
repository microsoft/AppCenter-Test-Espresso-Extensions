package com.microsoft.appcenter.espresso;

import android.os.Bundle;
import android.util.Log;

import com.microsoft.appcenter.event.EventReporter;
import com.microsoft.appcenter.event.StdOutEventReporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Factory {
    private static String TAG = Factory.class.getSimpleName();
    private static String locations[] = {"androidx.test.platform.app.InstrumentationRegistry",
            "androidx.test.InstrumentationRegistry",
            "android.support.test.InstrumentationRegistry"};


    private static EventReporter eventReporter;

    static {
        Bundle arguments = getArguments();
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

    private static Bundle getArguments() {
        for (String location: locations) {
            try {
                Class<?> aClass = Class.forName(location);
                Method getArguments = aClass.getMethod("getArguments", (Class[]) null);
                return (Bundle) getArguments.invoke(null, (Object[]) null);
                // We need to support api level 8 and up, so we cannot combine catches into on
            } catch (IllegalStateException e) {
                String msg = String.format("Unable to find arguments in {0}, trying next \"global\" Registry", location);
                Log.d(TAG, msg);
            } catch (ClassNotFoundException e) {
                // Ignore
            } catch (NoSuchMethodException e) {
                // Ignore
            } catch (IllegalAccessException e) {
                // Ignore
            } catch (InvocationTargetException e) {
                // Ignore
            }
        }
        return new Bundle();
    }

    public static ReportHelper getReportHelper() {
        return new ReportHelper(eventReporter);
    }

    private Factory() {
    }
}
