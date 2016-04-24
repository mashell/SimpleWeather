package com.example.myvalentine.myweather.util;

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
 * Created by myValentine on 16/4/21.
 */
public class NetWork {
    public static void sendRequest(String send_url, final int type, final NetWorkResponse netWorkResponse) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(send_url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (type == 0x70) {
                    InputStream in = response.body().byteStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuffer sb = new StringBuffer();
                    String strRead;
                    while ((strRead = reader.readLine()) != null) {
                        sb.append(strRead);
                    }
                    netWorkResponse.onResponse(sb.toString(),null);
                }

                if (type == 0x71) {
                    InputStream is = response.body().byteStream();
                    netWorkResponse.onResponse(null,is);
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                netWorkResponse.onFailure();
            }
        });
    }
}
