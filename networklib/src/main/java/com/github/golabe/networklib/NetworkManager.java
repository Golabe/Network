package com.github.golabe.networklib;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.github.golabe.networklib.receiver.NetworkStateReceiver;

public class NetworkManager {

    private Application application;
    private NetworkStateReceiver receiver;


    private static class SingletonHelper {
        private static final NetworkManager INSTANCE = new NetworkManager();
    }

    private NetworkManager() {
    }

    public static NetworkManager getDefault() {
        return SingletonHelper.INSTANCE;
    }

    public void init(Application application) {
        this.application = application;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl();
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager connManager = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager != null) connManager.registerNetworkCallback(request, networkCallback);
        } else {
            receiver = new NetworkStateReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constans.ANDROID_NET_CHANGE_ACTIOB);
            application.registerReceiver(receiver, filter);
        }
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("Application not null");
        }
        return this.application;
    }


    public void register(Object obj) {
        if (receiver != null) {
            receiver.register(obj);
        }

    }

    public void unregister(Object obj) {
        if (receiver != null) {
            receiver.unregister(obj);
        }
    }


    public void unregisterAll() {
        if (receiver != null) {
            receiver.unregisterAll();
        }
    }
}
