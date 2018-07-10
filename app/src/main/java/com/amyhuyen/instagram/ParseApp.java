package com.amyhuyen.instagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.applicationId))
            .clientKey(getString(R.string.masterKey))
            .server(getString(R.string.serverUrl))
            .build();

        Parse.initialize(configuration);
    }

}
