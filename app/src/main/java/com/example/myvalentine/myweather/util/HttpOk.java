package com.example.myvalentine.myweather.util;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.myvalentine.myweather.Activity.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by myValentine on 16/3/17.
 */

public class HttpOk {
    public static final int GET_INPUTSTREEAM = 0X70;
    public static final int GET_STRING = 0X71;
    private static SharedPreferences http_srf;
    private static String city_url = "https://api.heweather.com/x3/citylist?search=allchina&key=6012692e102d49e2835c45fc64427826";
    private static String data_url_head = "https://api.heweather.com/x3/weather?city=";
    private static String data_url_end = "&key=6012692e102d49e2835c45fc64427826";
    private static String image_url = "http://files.heweather.com/cond_icon/";
    private static String update_app = "http://api.fir.im/apps/latest/57178664e75e2d7c76000000?api_token=70cc1fa3551026462afd605134af00bc";

    public static void getHttp(final String city_name) {
        String weather_url = data_url_head + city_name + data_url_end;


//        NetWork.sendRequest(weather_url, GET_STRING, new NetWorkResponse() {
//            @Override
//            public void onResponse(String sb, InputStream inputStream) {
//                HandleJson.getWeather(sb);
//            }
//
//            @Override
//            public void onFailure() {
//                Handler handler = MainActivity.activity.getHandler();
//                Message message = new Message();
//                message.obj = 0x14;
//                handler.sendMessage(message);
//            }
//        });
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(weather_url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Handler handler = MainActivity.activity.getHandler();
                Message message = new Message();
                message.obj = 0x14;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream in = response.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sbf.append(strRead);
                }
                HandleJson.getWeather(sbf.toString());
            }
        });
    }

    public static void getImage() {
        final Handler handler = MainActivity.activity.getHandler();
        http_srf = MyApplication.getContext().getSharedPreferences("weather_info", MyApplication.getContext().MODE_PRIVATE);
        String img_url = image_url + http_srf.getString("city_code", "999") + ".png";

//        NetWork.sendRequest(img_url, 0x71, new NetWorkResponse() {
//            @Override
//            public void onResponse(String sb, InputStream inputStream) {
//                Message message = new Message();
//                InputStream is = inputStream;
//                message.obj = is;
//                message.what = 0x12;
//                handler.sendMessage(message);
//            }
//
//            @Override
//            public void onFailure() {
//            }
//        });

        OkHttpClient okHttpClient = new OkHttpClient();
        final  Request request = new Request.Builder()
                .url(img_url).build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {e.printStackTrace();}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                InputStream is = response.body().byteStream();
                message.obj = is;
                message.what = 0x12;
                handler.sendMessage(message);
            }
        });
    }

    public static void getCity() {
//        NetWork.sendRequest(city_url, GET_STRING, new NetWorkResponse() {
//            public void onResponse(String sb, InputStream inputStream) {
//                HandleJson.parseCity(sb);
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(city_url).build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream in = response.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                StringBuffer sb = new StringBuffer();
                String strRead ;
                while((strRead=reader.readLine())!=null){
                    sb.append(strRead);
                }
                HandleJson.parseCity(sb.toString());
            }
        });
    }

    public static void updateApp() {
        Log.i("updateapp","解析更新app1");
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(update_app).build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream in = response.body().byteStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                StringBuffer sb = new StringBuffer();
                String strRead ;
                while((strRead=reader.readLine())!=null){
                    sb.append(strRead);
                }
                HandleJson.getUpdate(sb.toString());
            }
        });
    }



}
