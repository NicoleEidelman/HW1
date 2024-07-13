package com.example.hw1.Utilities;

import android.app.Application;

import com.example.hw1.SupportingClasses.MSPV3;


public class App extends Application {
    @Override

    public void onCreate() {
        super.onCreate();
        MSPV3.init(this);
    }
}
