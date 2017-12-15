package com.microsoft.appcenter.espresso;

import com.microsoft.appcenter.event.Event;
import com.microsoft.appcenter.event.TestableEventReporter;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by john7doe on 12/12/2017.
 */

@RunWith(Parameterized.class)
public class ParameterizedNamedUniqIdsTest {
    static TestableEventReporter testableEventReporter = new TestableEventReporter();

    @Rule
    public ReportHelper reportHelper = new ReportHelper(testableEventReporter);

    @Parameterized.Parameters(name = "{index}: foo {0}")
    public static Iterable<Integer> data() {
        return Arrays.asList(7, 9, 13);
    }

    private int input;

    public ParameterizedNamedUniqIdsTest(int input) {
        this.input = input;
    }

    @Test
    public void dummyTest() {
    }

    @AfterClass
    public static void testThatIdsAreUniq() {
        Set<String> ids = new HashSet<>();

        for(Event event : testableEventReporter.getCommitedEvents()) {
            ids.add(event.getId());
        }

        assertThat(testableEventReporter.getCommitedEvents().size(), is(ids.size()));
    }
}
