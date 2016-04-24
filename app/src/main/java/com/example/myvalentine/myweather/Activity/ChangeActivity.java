package com.example.myvalentine.myweather.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myvalentine.myweather.R;
import com.example.myvalentine.myweather.util.HandleJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myValentine on 16/3/20.
 */
public class ChangeActivity extends AppCompatActivity {
    private SearchView searchView;
    private ListView listView;
    private List<String> list_data = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_layout);
        initData();
        initWidget();

    }

    public void initWidget(){
        listView = (ListView) findViewById(R.id.city_list);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_data));
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "选中的是" + ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeActivity.this, MainActivity.class);
                intent.putExtra("city_name", ((TextView) view).getText());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        listView.setVisibility(View.GONE);
        // 设置该SearchView显示搜索按钮
        searchView.setSubmitButtonEnabled(true);
        // 设置该SearchView内默认显示的提示文本
        searchView.setQueryHint("查找");
        // 为该SearchView组件设置事件监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                // 实际应用中应该在该方法内执行实际查询
                listView.setFilterText(query);
                return false;
            }

            public boolean onQueryTextChange(String newText) {

                if (TextUtils.isEmpty(newText)) {
                    // 清除ListView的过滤
                    listView.setVisibility(View.GONE);
                    listView.clearTextFilter();
                } else {
                    // 使用用户输入的内容对ListView的列表项进行过滤
                    listView.setFilterText(newText);
                    listView.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });
    }


    public void initData() {
        SQLiteDatabase database = HandleJson.helper.getWritableDatabase();
        Cursor cursor = database.query("City", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("city_name"));
                list_data.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}
