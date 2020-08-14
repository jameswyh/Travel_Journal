package com.example.james_000.traveljournal.application;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.model.LatLng;

public class MyApplication extends Application {

    private static Context context;
    private LatLng MyLatLng;
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public LatLng getMyLatLng() {
        return MyLatLng;
    }

    public void setMyLatLng (LatLng MyLatLng) {
        this.MyLatLng = MyLatLng;
    }
}
