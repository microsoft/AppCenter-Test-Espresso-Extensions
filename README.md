# Test-Cloud Espresso Extensions

This project provides extensions for producing test reports for Espresso tests in Test Cloud. Test Cloud offers access to a very large and diverse set of __Real__ Android (and iOS) devices.

In this guide, you’ll learn how to make the changes necessary to run your existing Espresso test suite in Test Cloud.

## 1. Changes to the build system
Add the following dependency in your `build.gradle` file:

```gradle
androidTestCompile('com.xamarin.testcloud:espresso-support:1.0')
```

This will ensure the reportHelper is available at compile time. The reportHelper enable the `label` feature. See Step 4 for more detail on the `label` feature.

## 2. Changes to the tests

### Step 1 - Add imports

Import these packages into your test classes:

```java
import android.support.test.runner.AndroidJUnit4;
import com.xamarin.testcloud.espresso.Factory;
import com.xamarin.testcloud.espresso.ReportHelper;
```

### Step 2 - Instantiate the ReportHelper

Insert this declaration in each of your test classes:

```java
    @Rule
    public ReportHelper reportHelper = Factory.getReportHelper();
```

### Step 3 - Update your test cases

Using the helper will still allow you to run your tests locally without additional modifications, but enables you to "label" test steps in your test execution using `reportHelper.label("text")`. The text and a screenshot from the device will be visible in test report in  Test Cloud.

A recommended practice is to have a call to label in the `@After` method, this will include a screenshot of the app final state in the test report. The screenshot will be taken, even if a test is failing, and often provides valuable information as to why it does so. An example `@After` method for a test could look like this:

```java
    @After
    public void TearDown(){
        reportHelper.label("Stopping App");
    }
```

## 3. Upload to Test Cloud

If you have not done so already, install our command line interface by following [the installation instructions](UploaderInstall.md/#installation).

*Note: If you are an existing Test Cloud user currently using the command line tools for Calabash, UITest or Appium, you will need to install this new tool.*

If you do not have an existing device key ready, you can generate one by following the *new test run* dialog in [Test Cloud](https://testcloud.xamarin.com). On the final screen, extract only the device key from the generated command.

### Compile application and test apk

Compile your application and tests. E.g. by running `gradle` with the tasks `assembleDebug` and `assembleDebugAndroidTest`

Perform upload by running:

TODO: change uploader to know what apk is the espresso test?

```
xtc test <project>/build/outputs/apk/<apk> <api-key> --devices <selection> --user <email> --workspace <project>/build/outputs/apk/<test-apk>
```

*Note: If you are having trouble targeting the `xtc` command, try executing with the fully qualified path to the package.*