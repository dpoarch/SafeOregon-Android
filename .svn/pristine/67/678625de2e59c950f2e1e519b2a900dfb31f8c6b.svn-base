package com.safeoregon.app;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.EmailIntentSender;
import org.acra.sender.HttpSender;

@ReportsCrashes(formKey = "",

        formUri = "", mode = ReportingInteractionMode.DIALOG, mailTo = "developer.etechmavens@gmail.com", customReportContent = {
        ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
        ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL,
        ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT,
        ReportField.USER_COMMENT}, resDialogText = R.string.crash_dialog_text, resDialogTitle = R.string.crash_dialog_title
)
public class SafeOregonApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ACRA.init(this);
        ACRA.getErrorReporter()
                .addReportSender(new EmailIntentSender(this));
        ACRA.getErrorReporter()
                .addReportSender(
                        new HttpSender(
                                HttpSender.Method.POST,
                                HttpSender.Type.FORM,
                                "",
                                null));





    }
}
