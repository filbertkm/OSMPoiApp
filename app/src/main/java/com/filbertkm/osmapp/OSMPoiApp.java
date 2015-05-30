package com.filbertkm.osmapp;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(
        mode = ReportingInteractionMode.DIALOG,
        resDialogTitle = R.string.acra_report_dialog_title,
        resDialogText = R.string.acra_report_dialog_text,
        resDialogCommentPrompt = R.string.acra_report_dialog_comment,
        mailTo = "aude.wiki@gmail.com"
)

public class OSMPoiApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
    }

}
