package com.movies.movieapp;

import android.app.Application;
import android.content.Context;

public class MyAppliction extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyAppliction.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyAppliction.context;
    }
}
