package com.hui.toolkit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.dodoo_tech.gfal.adapter.CommonAdapter;

import java.util.ArrayList;

import dodoo.water.qmui.widget.QMUIAnimationListView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QMUIAnimationListView listView = findViewById(R.id.animation_list_view);

        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add(String.valueOf(i));
        }
        listView.setAdapter(new CommonAdapter<String>(this, R.layout.list_item,datas) {
            @Override
            public void getView(int postion, ViewHolder holder, String data) {
                TextView textView = holder.getView(R.id.text);
                textView.setText(data);
            }
        });
    }
}
