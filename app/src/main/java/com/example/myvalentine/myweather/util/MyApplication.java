package com.example.myvalentine.myweather.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by myValentine on 16/3/14.
 */
public class MyApplication extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
