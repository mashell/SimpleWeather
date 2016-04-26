package com.example.myvalentine.myweather.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.myvalentine.myweather.Adapter.DailyAdapter;
import com.example.myvalentine.myweather.Adapter.HourlyAdapter;
import com.example.myvalentine.myweather.Adapter.SuggestAdapter;
import com.example.myvalentine.myweather.R;
import com.example.myvalentine.myweather.util.HttpOk;
import com.example.myvalentine.myweather.util.MyLocationListener;
import com.example.myvalentine.myweather.util.OnItemClickListener;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {
    public static MainActivity activity;
    private DrawerLayout mDrawerLayout;
    private HourlyAdapter hourlyAdapter;
    private SuggestAdapter suggestAdapter;
    private DailyAdapter dailyAdapter;
    private SharedPreferences srf;
    private TextView city_qlty;
    private TextView city_tmp;
    private TextView city_pm25;
    private TextView city_dir;
    private TextView city_pcpn;
    private TextView city_aitmp;
    private Bitmap bitmap;
    private ImageView city_icon;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private MyHandler handler;
    public RecyclerView re_suggestion;
    private RecyclerView re_hourly;
    private RecyclerView re_daily;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refresh;
    public LocationClient locationClient = null;
    public BDLocationListener bdListener = new MyLocationListener();
    private Boolean firstUse;

    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateHead();
        initShow();
        selectVersion();

    }

    public void ifFirstUse() {
        firstUse = srf.getBoolean("first_use", true);
        if (firstUse) {
            initBD();
        } else HttpOk.getHttp(srf.getString("city_name", "北京"));
    }


    public void initBD() {
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(bdListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationClient.setLocOption(option);
        locationClient.start();
    }

    public void selectVersion() {
        if (Build.VERSION.SDK_INT >= 23) {
            insertPermission();
        }
    }

    @TargetApi(23)
    private void insertPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0x23);
                return;
            }
            insertPermission();
        } else ifFirstUse();
    }

    public void initShow() {
        activity = this;
        handler = new MyHandler();

        srf = getSharedPreferences("weather_info", MODE_PRIVATE);
        if (!srf.getBoolean("loadCity", false)) {
            HttpOk.getCity();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        final NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(this);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapseTextLight);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedView);
        nestedScrollView.setVisibility(View.GONE);
        refresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        refresh.setColorSchemeResources(R.color.Start, R.color.Middle, R.color.End);
        refresh.setOnRefreshListener(this);

        city_icon = (ImageView) findViewById(R.id.city_icon);
        city_aitmp = (TextView) findViewById(R.id.city_aitmp);
        city_qlty = (TextView) findViewById(R.id.city_qlty);
        city_tmp = (TextView) findViewById(R.id.city_tmp);
        city_dir = (TextView) findViewById(R.id.city_dir);
        city_pm25 = (TextView) findViewById(R.id.city_pm25);
        city_pcpn = (TextView) findViewById(R.id.city_pcpn);

        re_hourly = (RecyclerView) findViewById(R.id.list_hourly);
        re_suggestion = (RecyclerView) findViewById(R.id.list_suggest);
        re_daily = (RecyclerView) findViewById(R.id.list_daily);

        re_hourly.setNestedScrollingEnabled(false);
        re_suggestion.setNestedScrollingEnabled(false);
        re_daily.setNestedScrollingEnabled(false);

        re_hourly.setHasFixedSize(true);
        re_suggestion.setHasFixedSize(true);
        re_daily.setHasFixedSize(true);

        LinearLayoutManager h_layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager s_layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager d_layoutManager = new LinearLayoutManager(this);

        h_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        s_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        d_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        re_daily.setLayoutManager(d_layoutManager);
        re_hourly.setLayoutManager(h_layoutManager);
        re_suggestion.setLayoutManager(s_layoutManager);

        suggestAdapter = new SuggestAdapter();
        hourlyAdapter = new HourlyAdapter();
        dailyAdapter = new DailyAdapter();

        suggestAdapter.setOnItemClickListener(this);
        dailyAdapter.setOnItemClickListener(this);
        hourlyAdapter.setOnItemClickListener(this);

        re_suggestion.setAdapter(suggestAdapter);
        re_hourly.setAdapter(hourlyAdapter);
        re_daily.setAdapter(dailyAdapter);

    }

    public void updateHead() {
        ImageView head_img = (ImageView) findViewById(R.id.top_img);
        SimpleDateFormat sdf = new SimpleDateFormat("H");
        Date curDate = new Date(System.currentTimeMillis());
        int hour = Integer.parseInt(sdf.format(curDate));
        if (6 <= hour && hour <= 18) {
            head_img.setImageResource(R.drawable.light);
        } else head_img.setImageResource(R.drawable.dark);
    }

    public void updateAll() {
        if (bitmap != null) {
            city_icon.setImageBitmap(bitmap);
        } else city_icon.setImageResource(R.drawable.unknow);

        collapsingToolbarLayout.setTitle(srf.getString("city_name", "N/A"));

        city_qlty.setText(srf.getString("city_qlty", "N/A"));
        city_tmp.setText(srf.getString("city_tmp", "N/A"));
        city_pm25.setText(srf.getString("city_pm25", "N/A"));
        city_dir.setText(srf.getString("city_dir", "N/A"));
        city_pcpn.setText(srf.getString("city_pcpn", "N/A"));
        city_aitmp.setText(srf.getString("daily_aitmp", "N/A"));
        suggestAdapter.notifyDataSetChanged();
        hourlyAdapter.notifyDataSetChanged();
        dailyAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.drawer_change: {
                Intent intent = new Intent(MainActivity.this, ChangeActivity.class);
                startActivityForResult(intent, 100);
            }
            break;

            case R.id.drawer_about: {
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
            }
            break;

            default:
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onItemClick(View view, int position, int type) {
        switch (type) {

            case 0x52: {
                Bundle bundle = new Bundle();
                bundle.putInt("sug_position", position);
                SuggestionFragment suggestionFragment = new SuggestionFragment();
                suggestionFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.sug_fragment, suggestionFragment);
                fragmentTransaction.commit();
                re_suggestion.setVisibility(View.GONE);

            }
            break;

            default: {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                WindFragment fragment = new WindFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if (type == 0x51) {
                    bundle.putInt("type",0x51);
                    transaction.replace(R.id.hourly_fragment, fragment);
                    re_hourly.setVisibility(View.GONE);
                }
                if (type == 0x53) {
                    bundle.putInt("type",0x53);
                    transaction.replace(R.id.daily_fragment, fragment);
                    re_daily.setVisibility(View.GONE);
                }
                fragment.setArguments(bundle);
                transaction.commit();
            }
            break;
        }

    }

    @Override
    public void onRefresh() {
        refresh.isRefreshing();
        String city_name = srf.getString("city_name", "北京");
        HttpOk.getHttp(city_name);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x12: {
                    bitmap = BitmapFactory.decodeStream((InputStream) msg.obj);
                    Snackbar snackbar = Snackbar.make(collapsingToolbarLayout, "加载成功! :)", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    break;
                }
                case 0x13: {
                    Snackbar snackbar = Snackbar.make(collapsingToolbarLayout, "获取位置失败,请检查您的设置 :(", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    break;
                }
                case 0x14: {
                    Snackbar snackbar = Snackbar.make(collapsingToolbarLayout, "网络异常,请检查您的网络设置 :(", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    break;
                }
                case 0x23: {
                    Snackbar snackbar = Snackbar.make(collapsingToolbarLayout, "定位中...", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    break;
                }
            }
            if (firstUse) {
                locationClient.stop();
            }
            updateAll();
            refresh.setRefreshing(false);
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public RecyclerView getSuggestView() {
        return re_suggestion;
    }

    public RecyclerView getHourlyView() {
        return re_hourly;
    }

    public RecyclerView getDailyView() { return re_daily; }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            progressBar.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
            String choose_city = data.getStringExtra("city_name");
            HttpOk.getHttp(choose_city);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x23)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                insertPermission();
            } else {
                Message message = new Message();
                message.what = 0x23;
                Handler handler = MainActivity.activity.getHandler();
                handler.sendMessage(message);
            }
    }

    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        //The Refresh must be only active when the offset is zero :
        refresh.setEnabled(i == 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }


    public void onBackPressed() {
        // TODO Auto-generated method stub
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Snackbar snackbar = Snackbar.make(collapsingToolbarLayout, "再次点击返回键退出", Snackbar.LENGTH_SHORT);
            snackbar.show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

}
