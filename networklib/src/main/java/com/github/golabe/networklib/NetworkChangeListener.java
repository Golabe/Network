package com.github.golabe.networklib;

import com.github.golabe.networklib.type.NetType;

public interface NetworkChangeListener {

    void onNetworkPost(NetType netType);
}
