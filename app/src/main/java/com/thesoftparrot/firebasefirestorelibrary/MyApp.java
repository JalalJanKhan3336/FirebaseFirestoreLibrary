package com.thesoftparrot.firebasefirestorelibrary;

import android.app.Application;

import androidx.multidex.MultiDex;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
