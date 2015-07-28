package com.w3effects.groupmess.groupmess;

import android.app.Application;

import com.parse.Parse;
import com.parse.PushService;

import ui.MyActivity;

/**
 * Created by pankaj on 03/10/14.
 */
public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "k7JBljlIjBD5c0hMM8uGl9rjRuEUo4zPDDMzEm2D", "1DkFH9pjr7QJD0AwEFh2DevD7lKFXmlcTvAGctUe");
        // Also in this method, specify a default Activity to handle push notifications
        PushService.setDefaultPushCallback(ApplicationClass.this, MyActivity.class);
    }

}
