package com.hui.toolkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dodoo_tech.gfal.utils.LogUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.setDebugMode(true);
        setContentView(R.layout.activity_main);

        findViewById(R.id.test_noise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(getApplicationContext(),TestNoiseActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    LogUtil.logError(e);
                }
            }
        });
    }


}
