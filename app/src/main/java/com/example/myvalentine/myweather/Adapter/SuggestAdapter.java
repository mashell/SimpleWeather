package com.example.myvalentine.myweather.Adapter;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myvalentine.myweather.R;
import com.example.myvalentine.myweather.util.MyApplication;
import com.example.myvalentine.myweather.util.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by myValentine on 16/3/14.
 */
public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.MyViewHolder> {
    public OnItemClickListener onItemClickListener;

    private SharedPreferences srf = MyApplication.getContext().getSharedPreferences("weather_info", MyApplication.getContext().MODE_PRIVATE);


    private List<Integer> sug_icon = new ArrayList<Integer>(Arrays.asList
            (R.drawable.shushi,R.drawable.xiche,R.drawable.chuanzhuo,
            R.drawable.ganmao,R.drawable.yundong,R.drawable.lvyou,R.drawable.ziwaixian));

    private List<String> sug_title = new ArrayList<String>(Arrays.asList("舒适指数","洗车指数","穿衣指数","感冒指数",
            "运动指数","旅游指数","紫外线指数"));

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggestion_item,parent,false));
        return  myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.sug_icon.setImageResource(sug_icon.get(position));
        holder.sug_title.setText(sug_title.get(position));
        holder.sug_title_value.setText(srf.getString("sug_title"+position,"未知"));
        if(onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView,pos,0x52);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView sug_title;
        TextView sug_title_value;
        ImageView sug_icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            sug_title = (TextView)itemView.findViewById(R.id.sug_title);
            sug_title_value = (TextView)itemView.findViewById(R.id.sug_title_value);
            sug_icon = (ImageView) itemView.findViewById(R.id.sug_icon);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
