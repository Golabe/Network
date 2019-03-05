# Network
# 网络状态监听库
## 使用
## gradle 添加
```xml
implementation'com.github.golabe.network:networklib:1.0.0'
```

## application  初始化
```java
 @Override
    public void onCreate() {
        super.onCreate();
        NetworkManager.getDefault().init(this);
    }
```

## 注册
```java
    @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           NetworkManager.getDefault().register(this);

       }
```

## 创建方法 用@Network 标记  netType ：AUTO 有网络，WIFI 有WIFI，MOBILE 移动网络，NONE 没有网络
```java
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
```
## 反注册
```java
  @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getDefault().unregister(this);
    }
```

