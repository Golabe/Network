package com.github.golabe.networklib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.golabe.networklib.Constants;
import com.github.golabe.networklib.NetworkChangeListener;
import com.github.golabe.networklib.type.NetType;
import com.github.golabe.networklib.utils.NetworkUtil;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";
    private NetType netType;
    //    private Map<Object, List<MethodManager>> networkList;
    private NetworkChangeListener onChangeListener;

    public NetworkStateReceiver(NetworkChangeListener listener) {
        this.netType = NetType.NONE;
        this.onChangeListener = listener;
//        networkList = new HashMap<>();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            netType = NetworkUtil.getNetType();
//            if (NetworkUtil.isNetworkAvailable()) {
//                Log.d(TAG, "onReceive:网络连接成功");
//            } else {
//                Log.d(TAG, "onReceive: 没有网络连接");
//            }
            if (onChangeListener != null) {
                onChangeListener.onNetworkPost(netType);
            }
        }
    }


}
