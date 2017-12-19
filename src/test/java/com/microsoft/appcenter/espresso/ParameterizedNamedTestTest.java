package com.microsoft.appcenter.espresso;

import com.microsoft.appcenter.event.TestableEventReporter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by john7doe on 12/12/2017.
 */

@RunWith(Parameterized.class)
public class ParameterizedNamedTestTest {
    TestableEventReporter testableEventReporter = new TestableEventReporter();

    @Rule
    public ReportHelper reportHelper = new ReportHelper(testableEventReporter);

    @Parameterized.Parameters(name = "{index}: fib({0})={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, 0 }
        });
    }

    private int input;
    private int expected;

    public ParameterizedNamedTestTest(int input, int expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void testThatNameWork() {
        assertEquals(expected, input);
        assertThat(testableEventReporter.getCommitedEvents().size(), is(1));
        assertThat(testableEventReporter.getCommitedEvents().get(0).getParameters(), is("0: fib(0)=0"));
    }
}
