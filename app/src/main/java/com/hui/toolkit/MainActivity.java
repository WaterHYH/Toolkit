package com.hui.toolkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dodoo_tech.gfal.utils.ActivityUtils;
import com.dodoo_tech.gfal.utils.LogUtil;
import com.dodoo_tech.gfal.utils.ToastUtils;
import com.dodoo_tech.gfal.utils.Utils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(this);
        LogUtil.setPackageName(getPackageName());
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

        boolean hasActivity = ActivityUtils.isActivityExists("com.lmiot.autotool","com.lmiot.autotool.Activity.MainActivit");
        LogUtil.logInfo("aaaa "+hasActivity);

        ToastUtils.showLong("bbbbbbbbbbbbb");
    }


}
