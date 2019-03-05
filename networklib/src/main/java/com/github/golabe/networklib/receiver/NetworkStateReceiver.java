package com.github.golabe.networklib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.golabe.networklib.Constants;
import com.github.golabe.networklib.listener.NetworkChangeListener;
import com.github.golabe.networklib.type.NetType;
import com.github.golabe.networklib.utils.NetworkUtil;

public class NetworkStateReceiver extends BroadcastReceiver {
    private NetType netType;
    private NetworkChangeListener onChangeListener;

    public NetworkStateReceiver(NetworkChangeListener listener) {
        this.netType = NetType.NONE;
        this.onChangeListener = listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            netType = NetworkUtil.getNetType();

            if (onChangeListener != null) {
                onChangeListener.onNetworkPost(netType);
            }
        }
    }


}
