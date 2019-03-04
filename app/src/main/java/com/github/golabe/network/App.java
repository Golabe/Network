package com.github.golabe.network;

import android.app.Application;

import com.github.golabe.networklib.NetworkManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.getDefault().init(this);
    }
}
