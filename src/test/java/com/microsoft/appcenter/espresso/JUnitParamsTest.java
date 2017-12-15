package com.microsoft.appcenter.espresso;

import com.microsoft.appcenter.event.Event;
import com.microsoft.appcenter.event.TestableEventReporter;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.NamedParameters;
import junitparams.Parameters;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import junitparams.custom.combined.CombinedParameters;
import junitparams.naming.TestCaseName;

import static junitparams.JUnitParamsRunner.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class JUnitParamsTest {
    static TestableEventReporter testableEventReporter = new TestableEventReporter();

    @Rule
    public ReportHelper reportHelper = new ReportHelper(testableEventReporter);

    // tests taken from https://github.com/Pragmatists/JUnitParams/blob/master/src/test/java/junitparams/usage/SamplesOfUsageTest.java

    static enum  PersonType {
        SOME_VALUE,
        OTHER_VALUE
    }

    @Test
    @Parameters({"AAA,1", "BBB,2"})
    public void paramsInAnnotation(String p1, Integer p2) {
    }

    @Test
    @Parameters({"AAA|1", "BBB|2"})
    public void paramsInAnnotationPipeSeparated(String p1, Integer p2) {
    }

    @Test
    @Parameters
    public void paramsInDefaultMethod(String p1, Integer p2) {
    }

    private Object parametersForParamsInDefaultMethod() {
        return new Object[]{new Object[]{"AAA", 1}, new Object[]{"BBB", 2}};
    }

    @Test
    @Parameters(method = "named1")
    public void paramsInNamedMethod(String p1, Integer p2) {
    }

    private Object named1() {
        return new Object[]{"AAA", 1};
    }

    @Test
    @Parameters(named = "return 1")
    public void paramsInNamedParameters(int number) {
    }

    @NamedParameters("return 1")
    private Integer[] customNamedParameters() {
        return new Integer[]{1};
    }

    @Test
    @Parameters(method = "named2,named3")
    public void paramsInMultipleMethods(String p1, Integer p2) {
    }

    private Object named2() {
        return new Object[]{"AAA", 1};
    }

    private Object named3() {
        return new Object[]{"BBB", 2};
    }

    @Test
    @Parameters(method = "named4")
    public void paramsWithVarargs(String... args) {
    }

    private Object named4() {
        return new Object[]{new String[]{"AAA", "BBB"}};
    }


    @Test
    @Parameters
    public void paramsInCollection(String p1) {
    }

    private List<String> parametersForParamsInCollection() {
        return Arrays.asList("a");
    }

    @Test
    @Parameters
    public void paramsInIterator(String p1) {
    }

    private Iterator<String> parametersForParamsInIterator() {
        return Arrays.asList("a").iterator();
    }

    @Test
    @Parameters
    public void paramsInIterableOfIterables(String p1, String p2) {
    }

    private List<List<String>> parametersForParamsInIterableOfIterables() {
        return Arrays.asList(
                Arrays.asList("s01e01", "s01e02"),
                Arrays.asList("s02e01", "s02e02")
        );
    }

    @Test
    @Parameters({"SOME_VALUE", "OTHER_VALUE"})
    public void enumsAsParamInAnnotation(PersonType person) {
    }

    @Test
    @Parameters
    public void enumsAsParamsInMethod(PersonType person) {
    }

    private PersonType[] parametersForEnumsAsParamsInMethod() {
        return (PersonType[]) new PersonType[]{PersonType.SOME_VALUE};
    }

    @Test
    @Parameters(source = PersonType.class)
    public void enumAsSource(PersonType personType) {
    }


    @Test
    @Parameters("please\\, escape commas if you use it here and don't want your parameters to be splitted")
    public void commasInParametersUsage(String phrase) {
    }

    @Test
    @Parameters({"1,1", "2,2", "3,6"})
    @TestCaseName("factorial({0}) = {1}")
    public void customNamesForTestCase(int argument, int result) {
    }

    @Test
    @Parameters({"value1, value2", "value3, value4"})
    @TestCaseName("[{index}] {method}: {params}")
    public void predefinedMacroForTestCaseNames(String param1, String param2) {
    }

    public Object mixedParameters() {
        boolean booleanValue = true;
        int[] primitiveArray = {1, 2, 3};
        String stringValue = "Test";
        String[] stringArray = {"one", "two", null};
        return $(
                $(booleanValue, primitiveArray, stringValue, stringArray)
        );
    }

    @Test
    @Parameters(method = "mixedParameters")
    @TestCaseName("{0}, {1}, {2}, {3}")
    public void usageOfMultipleTypesOfParameters(
            boolean booleanValue, int[] primitiveArray, String stringValue, String[] stringArray) {
    }

    @Test
    @CombinedParameters({"AAA,BBB", "1|2"})
    public void combineParamsInAnnotation(String p1, Integer p2) {
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

