package com.hui.toolkit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dodoo_tech.gfal.adapter.CommonAdapter;
import com.dodoo_tech.gfal.utils.LogUtil;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Locale;

import dodoo.water.qmui.widget.QMUIAnimationListView;

public class MainActivity extends Activity {
    QMUIAnimationListView listView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
