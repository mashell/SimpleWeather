package com.example.myvalentine.myweather.util;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by myValentine on 16/3/14.
 */
public class HandleJson {
    public static SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("weather_info", MyApplication.getContext().MODE_PRIVATE).edit();
    //public static List<String> city_list = new ArrayList<String>();
    public static SQLiteHelper helper = new SQLiteHelper(MyApplication.getContext(), "Weather.db", null, 1);
    private static UpdateListener upListener;

    public static void getWeather(String myJson) {
        // editor = MyApplication.getContext().getSharedPreferences("weath_info", MyApplication.getContext().MODE_PRIVATE).edit();
        try {
            JSONObject jsonObject = new JSONObject(myJson);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject jsonO = jsonArray.getJSONObject(0);
            editor.putInt("hourly_count", jsonO.getJSONArray("hourly_forecast").length());

            if (jsonO.has("aqi")) {
                String city_pm25 = jsonO.getJSONObject("aqi").getJSONObject("city").getString("pm25");
                String city_quality = jsonO.getJSONObject("aqi").getJSONObject("city").getString("qlty");
                editor.putString("city_qlty", city_quality);
                editor.putString("city_pm25", city_pm25);
            } else {
                editor.putString("city_qlty", "N/A");
                editor.putString("city_pm25", "N/A");
            }

            String city_name = jsonO.getJSONObject("basic").getString("city");
            editor.putString("city_name", city_name);
            String city_code = jsonO.getJSONObject("now").getJSONObject("cond").getString("code");
            editor.putString("city_code", city_code);
            String city_tmp = jsonO.getJSONObject("now").getString("tmp") + "°C";
            editor.putString("city_tmp", city_tmp);
            String city_pcpn = jsonO.getJSONObject("now").getString("pcpn");
            editor.putString("city_pcpn", city_pcpn);
            String city_dir = jsonO.getJSONObject("now").getJSONObject("wind").getString("dir");
            editor.putString("city_dir", city_dir);
            //String up_time = jsonO.getJSONObject("basic").getJSONObject("update").getString("loc").substring();
            //Log.d("基础信息",up_time);


            JSONArray hourly_cast = jsonO.getJSONArray("hourly_forecast");
            for (int i = 0; i < hourly_cast.length(); i++) {
                JSONObject hourly_object = hourly_cast.getJSONObject(i);
                String hourly_date = hourly_object.getString("date").substring(11, 16);
                editor.putString("hourly_date" + i, hourly_date);
                String hourly_tmp = hourly_object.getString("tmp") + "°C";
                editor.putString("hourly_tmp" + i, hourly_tmp);
                String hourly_pop = hourly_object.getString("pop") + "%";
                editor.putString("hourly_pop" + i, hourly_pop);

                JSONObject hourly_wind = hourly_object.getJSONObject("wind");
                String wind_dir = hourly_wind.getString("dir");
                editor.putString("wind_dir_hourly" + i, wind_dir);

                String wind_spd = hourly_wind.getString("spd");
                editor.putString("wind_spd_hourly" + i, wind_spd);

                String wind_sc = hourly_wind.getString("sc");
                editor.putString("wind_sc_hourly" + i, wind_sc);


            }


            JSONArray forecast_array = jsonO.getJSONArray("daily_forecast");
            for (int i = 0; i < forecast_array.length(); i++) {
                JSONObject daily_object = (JSONObject) forecast_array.get(i);
                String daily_date = daily_object.getString("date").substring(5, 10);
                editor.putString("daily_date" + i, daily_date);
                String txt_d = daily_object.getJSONObject("cond").getString("txt_d");
                editor.putString("daily_txt_d" + i, txt_d);
                String txt_n = daily_object.getJSONObject("cond").getString("txt_n");
                editor.putString("daily_txt_n" + i, txt_n);
                String max_tmp = daily_object.getJSONObject("tmp").getString("max");
                String min_tmp = daily_object.getJSONObject("tmp").getString("min");
                String daily_aitmp = min_tmp + "~" + max_tmp + "°C";
                editor.putString("daily_aitmp" + i, daily_aitmp);

                String wind_dir = daily_object.getJSONObject("wind").getString("dir");
                editor.putString("wind_dir_daily" + i, wind_dir);
                String wind_spd = daily_object.getJSONObject("wind").getString("spd");
                editor.putString("wind_spd_daily" + i, wind_spd);
                String wind_sc = daily_object.getJSONObject("wind").getString("sc");
                editor.putString("wind_sc_daily" + i, wind_sc);


            }

            int COUNT = 0;
            String com_brf = jsonO.getJSONObject("suggestion").getJSONObject("comf").getString("brf");
            editor.putString("sug_title" + COUNT, com_brf);
            String com_txt = jsonO.getJSONObject("suggestion").getJSONObject("comf").getString("txt");
            editor.putString("sug_txt" + COUNT, com_txt);
            COUNT++;
            String cw_brf = jsonO.getJSONObject("suggestion").getJSONObject("cw").getString("brf");
            editor.putString("sug_title" + COUNT, cw_brf);
            String cw_txt = jsonO.getJSONObject("suggestion").getJSONObject("cw").getString("txt");
            editor.putString("sug_txt" + COUNT, cw_txt);
            COUNT++;
            String drsg_brf = jsonO.getJSONObject("suggestion").getJSONObject("drsg").getString("brf");
            editor.putString("sug_title" + COUNT, drsg_brf);
            String drsg_txt = jsonO.getJSONObject("suggestion").getJSONObject("drsg").getString("txt");
            editor.putString("sug_txt" + COUNT, drsg_txt);
            COUNT++;
            String flu_brf = jsonO.getJSONObject("suggestion").getJSONObject("flu").getString("brf");
            editor.putString("sug_title" + COUNT, flu_brf);
            String flu_txt = jsonO.getJSONObject("suggestion").getJSONObject("flu").getString("txt");
            editor.putString("sug_txt" + COUNT, flu_txt);
            COUNT++;
            String sport_brf = jsonO.getJSONObject("suggestion").getJSONObject("sport").getString("brf");
            editor.putString("sug_title" + COUNT, sport_brf);
            String sport_txt = jsonO.getJSONObject("suggestion").getJSONObject("sport").getString("txt");
            editor.putString("sug_txt" + COUNT, sport_txt);
            COUNT++;
            String trav_brf = jsonO.getJSONObject("suggestion").getJSONObject("trav").getString("brf");
            editor.putString("sug_title" + COUNT, trav_brf);
            String trav_txt = jsonO.getJSONObject("suggestion").getJSONObject("trav").getString("txt");
            editor.putString("sug_txt" + COUNT, trav_txt);
            COUNT++;
            String uv_brf = jsonO.getJSONObject("suggestion").getJSONObject("uv").getString("brf");
            editor.putString("sug_title" + COUNT, uv_brf);
            String uv_txt = jsonO.getJSONObject("suggestion").getJSONObject("uv").getString("txt");
            editor.putString("sug_txt" + COUNT, uv_txt);

            editor.putBoolean("first_use", false);
            editor.apply();
            HttpOk.getImage();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void parseCity(String myJson) {
        try {
            JSONObject jsonObject = new JSONObject(myJson);
            JSONArray jsonArray = jsonObject.getJSONArray("city_info");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json_city = jsonArray.getJSONObject(i);
                String city_name = json_city.getString("city");
                //city_list.add(city_name);
                initList(city_name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            editor.putBoolean("loadCity", true);
            editor.apply();
        }
    }

    public static void initList(String name) {
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("city_name", name);
        database.insert("City", null, contentValues);
        contentValues.clear();
    }

    public static void setClickListener(UpdateListener upListener) {
        HandleJson.upListener = upListener;
    }

    public static void getUpdate(String myjson) {
        try {
            JSONObject jsonObject = new JSONObject(myjson);
            int version = Integer.parseInt(jsonObject.getString("version"));
            String update_log = jsonObject.getString("changelog");
            String update_url = jsonObject.getString("update_url");
            String versionShort = "发现新版本:" + jsonObject.getString("versionShort");
            int fsize = jsonObject.getJSONObject("binary").getInt("fsize") / (1024 * 1024);
            String change_log = "安装包大小:" + fsize + "MB" + "\n更新日志:" + update_log;
            if (version >getVersionCode()){
            upListener.getNew(versionShort,change_log,update_url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getVersionCode() {
        Log.i("updateapp", "计算版本");
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = MyApplication.getContext().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
            String version = packInfo.versionName;
            Log.i("updateapp", "版本号为" + version);
            return Integer.parseInt(version);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static boolean ifContain(String city_name) {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("City", new String[]{"city_name"}, "city_name = ?", new String[]{city_name}, null, null, null);
        return !cursor.moveToFirst();
    }
}
