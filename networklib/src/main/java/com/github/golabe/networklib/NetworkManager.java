package com.github.golabe.networklib;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import com.github.golabe.networklib.annotaion.MethodManager;
import com.github.golabe.networklib.annotaion.Network;
import com.github.golabe.networklib.receiver.NetworkCallbackImpl;
import com.github.golabe.networklib.receiver.NetworkStateReceiver;
import com.github.golabe.networklib.type.NetType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetworkManager implements NetworkChangeListener {
    private static final String TAG = "NetworkManager";
    private Application application;
    private Map<Object, List<MethodManager>> networkList;

    @Override
    public void onNetworkPost(NetType netType) {
        post(netType);
    }


    private static class SingletonHelper {
        private static final NetworkManager INSTANCE = new NetworkManager();
    }

    private NetworkManager() {
        networkList = new HashMap<>();
    }

    public static NetworkManager getDefault() {
        return SingletonHelper.INSTANCE;
    }

    public void init(Application application) {
        this.application = application;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl(this);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager connManager = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager != null) connManager.registerNetworkCallback(request, networkCallback);
        } else {
            NetworkStateReceiver receiver = new NetworkStateReceiver(this);
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
            application.registerReceiver(receiver, filter);
        }
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("Application not null");
        }
        return this.application;
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
                            case MOBILE:
                                if (netType == NetType.MOBILE || netType == NetType.NONE) {
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

    }

    private List<MethodManager> findAnnotationMethod(Object obj) {
        ArrayList<MethodManager> methodList = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Network network = method.getAnnotation(Network.class);
            if (network == null) continue;
            Class<?> returnType = method.getReturnType();
            if (!returnType.getName().equals("void")) {
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
