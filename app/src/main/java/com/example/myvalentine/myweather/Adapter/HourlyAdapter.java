package com.example.myvalentine.myweather.Adapter;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myvalentine.myweather.R;
import com.example.myvalentine.myweather.util.MyApplication;
import com.example.myvalentine.myweather.util.OnItemClickListener;

/**
 * Created by myValentine on 16/3/13.
 */
public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.MyViewHolder> {

    private OnItemClickListener onItemClickListener;

    private SharedPreferences srf = MyApplication.getContext().getSharedPreferences("weather_info", MyApplication.getContext().MODE_PRIVATE);

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_hourly_date.setText(srf.getString("hourly_date" + position, "未知"));
        holder.tv_hourly_tmp.setText(srf.getString("hourly_tmp" + position, "未知"));
        holder.tv_hourly_pop.setText(srf.getString("hourly_pop" + position, "未知"));
        if(onItemClickListener !=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView,pos,0x51);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return  srf.getInt("hourly_count",0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_hourly_date;
        TextView tv_hourly_tmp;
        TextView tv_hourly_pop;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_hourly_date = (TextView) itemView.findViewById(R.id.hourly_date);
            tv_hourly_tmp = (TextView) itemView.findViewById(R.id.hourly_tmp);
            tv_hourly_pop = (TextView) itemView.findViewById(R.id.hourly_pop);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
