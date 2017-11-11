# Espresso Extensions

This project provides extensions for producing test reports for Espresso tests in Visual Studio App Center and Xamarin Test Cloud. 

Visit our [docs](https://docs.microsoft.com/en-us/mobile-center/test-cloud/preparing-for-upload/espresso) for instructions on how to convert and run your existing Espresso test suite in Visual Studio App Center.

## Upload to Xamarin Test Cloud

The extension also works with Xamarin Test Cloud. Below are the upload instructions for Xamarin Test Cloud. Upload instructions for Visual Studio App Center can be found using the link above.

If you have not done so already, install our command line interface by following [the installation instructions](XamarinTestCloudUploaderInstall.md/#installation).

*Note: If you are an existing Test Cloud user currently using the command line tools for Calabash, UITest or Appium, you will need to install this new tool.*

If you do not have an existing device key ready, you can generate one by following the *new test run* dialog in [Test Cloud](https://testcloud.xamarin.com). On the final screen, extract only the device key from the generated command.

### Compile application and test apk

Compile your application and tests. E.g. by running `gradle` with the tasks `assembleDebug` and `assembleDebugAndroidTest`

Perform upload by running:

```
xtc test <project>/build/outputs/apk/<apk> <api-key> --devices <selection> --user <email> --workspace <project>/build/outputs/apk/<test-apk>
```

*Note: If you are having trouble targeting the `xtc` command, try executing with the fully qualified path to the package.*


# Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.microsoft.com.

When you submit a pull request, a CLA-bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., label, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.
