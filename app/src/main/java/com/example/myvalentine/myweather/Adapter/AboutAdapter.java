package com.example.myvalentine.myweather.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myvalentine.myweather.R;
import com.example.myvalentine.myweather.util.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by myValentine on 16/4/19.
 */
public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.MyViewHolder> {



    private List<Integer> listImage = new ArrayList<Integer>(Arrays.asList
            (R.drawable.update, R.drawable.github, R.drawable.email, R.drawable.tick));

    private List<String> listText = new ArrayList<String>(Arrays.asList("检查更新\n点击检查更新",
            "Github\nhttps://github.com/myValentine/\nSimpleWeather",
            "e-mail\nmyValentine1995@163.com", "反馈\n欢迎向作者提出建议以及反馈BUG"));

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.about_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.imageView.setImageResource(listImage.get(position));
        holder.textView.setText(listText.get(position));
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position, 0x60);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.about_img);
            textView = (TextView) itemView.findViewById(R.id.about_txt);
        }
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

