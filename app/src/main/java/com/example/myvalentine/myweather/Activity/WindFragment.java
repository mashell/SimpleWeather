package com.example.myvalentine.myweather.Activity;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myvalentine.myweather.R;
import com.example.myvalentine.myweather.util.MyApplication;

/**
 * Created by myValentine on 16/4/16.
 */
public class WindFragment extends Fragment implements View.OnClickListener {
    private int type;
    private SharedPreferences srf;
    private LinearLayout linearLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        srf = MyApplication.getContext().getSharedPreferences("weather_info", MyApplication.getContext().MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        int position = getArguments().getInt("position");
        View view = inflater.inflate(R.layout.wind_detail, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.wind_fragment);
        TextView wind_time = (TextView) view.findViewById(R.id.wind_time);
        TextView wind_dir = (TextView) view.findViewById(R.id.wind_dir);
        TextView wind_spd = (TextView) view.findViewById(R.id.wind_spd);
        TextView wind_sc = (TextView) view.findViewById(R.id.wind_sc);
        if (type == 0x51) {
            String win_time = "时间: "+srf.getString("hourly_date"+position,"N/A");
            String win_dir = "风向: " + srf.getString("wind_dir_hourly"+position, "N/A");
            String win_spd = "风速: " + srf.getString("wind_spd_hourly"+position, "N/A");
            String win_sc = "风力等级: " + srf.getString("wind_sc_hourly"+position, "N/A");
            wind_time.setText(win_time);
            wind_dir.setText(win_dir);
            wind_spd.setText(win_spd);
            wind_sc.setText(win_sc);
        } else {
            String win_time = "时间: "+srf.getString("daily_date"+position,"N/A");
            String win_dir = "风向: " + srf.getString("wind_dir_daily"+position, "N/A");
            String win_spd = "风速: " + srf.getString("wind_spd_daily"+position, "N/A");
            String win_sc = "风力等级: " + srf.getString("wind_sc_daily"+position, "N/A");
            wind_time.setText(win_time);
            wind_dir.setText(win_dir);
            wind_spd.setText(win_spd);
            wind_sc.setText(win_sc);
        }

        linearLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (type == 0x51) MainActivity.activity.getHourlyView().setVisibility(View.VISIBLE);
        else MainActivity.activity.getDailyView().setVisibility(View.VISIBLE);
    }
}
