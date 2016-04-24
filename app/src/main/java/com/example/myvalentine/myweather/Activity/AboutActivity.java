package com.example.myvalentine.myweather.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.myvalentine.myweather.Adapter.AboutAdapter;
import com.example.myvalentine.myweather.R;
import com.example.myvalentine.myweather.util.HandleJson;
import com.example.myvalentine.myweather.util.HttpOk;
import com.example.myvalentine.myweather.util.OnItemClickListener;
import com.example.myvalentine.myweather.util.UpdateListener;

/**
 * Created by myValentine on 16/4/18.
 */
public class AboutActivity extends AppCompatActivity implements OnItemClickListener ,UpdateListener{
    public static AboutActivity aboutActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        aboutActivity = this;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.about_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AboutAdapter adapter = new AboutAdapter();
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position, int type) {
        switch (position) {
            case 0: {
                Log.i("updateapp","解析更新app");
                HandleJson.setClickListener(this);
                HttpOk.updateApp();
            }
            break;

            case 1: {
                showUpdateDialog("跳转网页","即将跳转作者的Github地址,是否继续?","https://github.com/myValentine/SimpleWeather");
            }
            break;
        }
    }

    public void showUpdateDialog(String title, String log, final String update_url){
        AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this)
                .setTitle(title).setMessage(log);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intentUrl(update_url);
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    public void intentUrl(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
    @Override
    public void getNew(final String versionShort,final String change_log,final String update_url) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showUpdateDialog(versionShort,change_log,update_url);
            }
        });

    }
}
