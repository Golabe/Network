package com.github.golabe.network;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.golabe.networklib.NetworkManager;
import com.github.golabe.networklib.annotaion.Network;
import com.github.golabe.networklib.type.NetType;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkManager.getDefault().register(this);

    }

    @Network(netType = NetType.AUTO)
    public void network(NetType netType) {
        switch (netType) {
            case AUTO:
                Log.d(TAG, "network: 有网络");
                toast(" 有网络");
                break;
            case WIFI:
                Log.d(TAG, "network: WIFI网络");
                toast(" WIFI网络");
                break;
            case MOBILE:
                Log.d(TAG, "network: MOBILE网络");
                toast(" WIFI网络");
                break;
            case NONE:
                Log.d(TAG, "network: 没网络");
                toast(" 没网络");
                break;
        }
    }

    public void toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getDefault().unregister(this);
    }
}
