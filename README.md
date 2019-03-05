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