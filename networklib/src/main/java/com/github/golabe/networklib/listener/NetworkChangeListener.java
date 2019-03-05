package com.github.golabe.networklib.listener;

import com.github.golabe.networklib.type.NetType;

public interface NetworkChangeListener {

    void onNetworkPost(NetType netType);
}
