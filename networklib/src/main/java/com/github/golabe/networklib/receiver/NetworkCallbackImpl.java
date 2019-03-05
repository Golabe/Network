package com.github.golabe.networklib.receiver;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import com.github.golabe.networklib.listener.NetworkChangeListener;
import com.github.golabe.networklib.type.NetType;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "NetworkCallbackImpl";
    private NetType netType;
    private NetworkChangeListener onChangeListener;

    public NetworkCallbackImpl(NetworkChangeListener listener) {
        this.onChangeListener = listener;
        netType = NetType.NONE;

    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        netType = NetType.AUTO;
        if (onChangeListener != null) {
            onChangeListener.onNetworkPost(netType);
        }
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        netType = NetType.NONE;
        if (onChangeListener != null) {
            onChangeListener.onNetworkPost(netType);
        }
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                netType = NetType.WIFI;
                if (onChangeListener != null) {
                    onChangeListener.onNetworkPost(netType);
                    Log.d(TAG, "onCapabilitiesChanged: WIFI");
                }
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                netType = NetType.MOBILE;
                if (onChangeListener != null) {
                    Log.d(TAG, "onCapabilitiesChanged: MOBILE");
                    onChangeListener.onNetworkPost(netType);
                }
            }
        }
    }

}
