package com.amyhuyen.instagram;

import android.app.Application;

import com.amyhuyen.instagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.applicationId))
            .clientKey(getString(R.string.masterKey))
            .server(getString(R.string.serverUrl))
            .build();

        Parse.initialize(configuration);
    }

}
