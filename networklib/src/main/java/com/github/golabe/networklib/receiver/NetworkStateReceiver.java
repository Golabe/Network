package com.github.golabe.networklib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.golabe.networklib.Constans;
import com.github.golabe.networklib.NetworkManager;
import com.github.golabe.networklib.annotaion.MethodManager;
import com.github.golabe.networklib.annotaion.Network;
import com.github.golabe.networklib.type.NetType;
import com.github.golabe.networklib.utils.NetworkUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";
    private NetType netType;
    private Map<Object, List<MethodManager>> networkList;

    public NetworkStateReceiver() {
        this.netType = NetType.NONE;
        networkList = new HashMap<>();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        if (intent.getAction().equalsIgnoreCase(Constans.ANDROID_NET_CHANGE_ACTIOB)) {
            netType = NetworkUtil.getNetType();
//            if (NetworkUtil.isNetworkAvailable()) {
//                Log.d(TAG, "onReceive:网络连接成功");
//            } else {
//                Log.d(TAG, "onReceive: 没有网络连接");
//            }

            post(netType);
        }
    }

    /**
     * 分发
     *
     * @param netType
     */
    private void post(NetType netType) {
        Set<Object> set = networkList.keySet();

        for (final Object getter : set) {

            List<MethodManager> methodList = networkList.get(getter);
            if (methodList != null) {
                for (final MethodManager method : methodList) {
                    if (method.getType().isAssignableFrom(netType.getClass())) {
                        switch (method.getNetType()) {
                            case AUTO:
                                invoke(method, getter, netType);
                                break;
                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;
                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;
                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

    }

    private void invoke(MethodManager method, Object getter, NetType netType) {
        Method execute = method.getMethod();
        try {
            execute.invoke(getter, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void unregisterAll() {
        if (!networkList.isEmpty()) {
            networkList.clear();

        }
        NetworkManager.getDefault().unregister(this);
        networkList = null;
        Log.d(TAG, "unregisterAll: 注销全部成功");
    }

    public void unregister(Object obj) {
        if (!networkList.isEmpty()) {
            networkList.remove(obj);
        }
    }

    public void register(Object obj) {

        List<MethodManager> methodList = networkList.get(obj);
        if (methodList == null) {
            methodList = findAnnotationMethod(obj);
            networkList.put(obj, methodList);
        }
        Log.d(TAG, "register: 注销成功 ：" + obj.getClass().getName());

    }

    private List<MethodManager> findAnnotationMethod(Object obj) {
        ArrayList<MethodManager> methodList = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Network network = method.getAnnotation(Network.class);
            if (network == null) continue;
            Type returnType = method.getGenericReturnType();
            if (!returnType.getTypeName().equals("void")) {
                throw new RuntimeException(method.getName() + "返回必须是void");
            }
            Class<?>[] types = method.getParameterTypes();
            if (types.length != 1) {
                throw new RuntimeException(method.getName() + "方法只有一个返回值");
            }
            methodList.add(new MethodManager(types[0], network.netType(), method));
        }

        return methodList;
    }
}
