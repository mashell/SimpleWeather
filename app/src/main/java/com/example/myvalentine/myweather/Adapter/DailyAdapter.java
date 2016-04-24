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
 * Created by myValentine on 16/3/14.
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.MyViewHolder>{

    public OnItemClickListener onItemClickListener;

    private SharedPreferences srf = MyApplication.getContext().getSharedPreferences("weather_info", MyApplication.getContext().MODE_PRIVATE);

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_daily_date.setText(srf.getString("daily_date"+position,"未知"));
        holder.tv_daily_d.setText(srf.getString("daily_txt_d"+position,"未知"));
        holder.tv_daily_n.setText(srf.getString("daily_txt_n"+position,"未知"));
        holder.tv_daily_tmp.setText(srf.getString("daily_aitmp"+position,"未知"));
        if(onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView,pos,0x53);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_daily_date;
        public TextView tv_daily_d;
        public TextView tv_daily_n;
        public TextView tv_daily_tmp;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_daily_date = (TextView) itemView.findViewById(R.id.daily_date);
            tv_daily_d = (TextView) itemView.findViewById(R.id.daily_d);
            tv_daily_n = (TextView) itemView.findViewById(R.id.daily_n);
            tv_daily_tmp = (TextView) itemView.findViewById(R.id.daily_tmp);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
