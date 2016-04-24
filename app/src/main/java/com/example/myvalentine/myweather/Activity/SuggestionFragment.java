package com.example.myvalentine.myweather.Activity;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myvalentine.myweather.R;
import com.example.myvalentine.myweather.util.MyApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by myValentine on 16/3/21.
 */
public class SuggestionFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences srf;

    private List<Integer> sug_icon = new ArrayList<Integer>(Arrays.asList
            (R.drawable.shushi, R.drawable.xiche, R.drawable.chuanzhuo,
                    R.drawable.ganmao, R.drawable.yundong, R.drawable.lvyou, R.drawable.ziwaixian));

    private List<String> sug_title = new ArrayList<String>(Arrays.asList("舒适指数","洗车指数","穿衣指数","感冒指数",
            "运动指数","旅游指数","紫外线指数"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        srf = MyApplication.getContext().getSharedPreferences("weather_info",MyApplication.getContext(). MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.suggest_detial,container,false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.sug_fragment);
        ImageView sug_img = (ImageView) view.findViewById(R.id.sug_img);
        TextView sug_txt_title = (TextView) view.findViewById(R.id.sug_txt_title);
        TextView sug_txt_detail= (TextView) view.findViewById(R.id.sug_txt_detail);
        linearLayout.setOnClickListener(this);

        int position = getArguments().getInt("sug_position");
        sug_img.setImageResource(sug_icon.get(position));
        String txt_title = sug_title.get(position)+"--"+srf.getString("sug_title" + position, "未知");
        sug_txt_title.setText(txt_title);
        sug_txt_detail.setText(srf.getString("sug_txt"+position,"未知"));
        return view;
    }

    @Override
    public void onClick(View v) {
        MainActivity.activity.getSuggestView().setVisibility(View.VISIBLE);
    }
}
