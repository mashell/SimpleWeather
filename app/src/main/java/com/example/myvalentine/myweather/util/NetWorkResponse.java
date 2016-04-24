package com.example.myvalentine.myweather.util;

import java.io.InputStream;

/**
 * Created by myValentine on 16/4/21.
 */
public interface NetWorkResponse {
    void onResponse(String sb, InputStream inputStream);
    void onFailure();
}
