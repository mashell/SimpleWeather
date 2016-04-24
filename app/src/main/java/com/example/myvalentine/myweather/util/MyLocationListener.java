package com.example.myvalentine.myweather.util;

import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.myvalentine.myweather.Activity.MainActivity;

/**
 * Created by myValentine on 16/4/4.
 */
public class MyLocationListener implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        Handler handler = MainActivity.activity.getHandler();
        //Receive Location
        if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
            String city_name = location.getCity().substring(0, location.getCity().length() - 1);
            HttpOk.getHttp(city_name);
        }
        if (location.getLocType() != BDLocation.TypeNetWorkLocation) {
            Message message = new Message();
            message.what = 0x13;
            handler.sendMessage(message);
        }
    }
}
