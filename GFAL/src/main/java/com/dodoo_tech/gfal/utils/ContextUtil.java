package com.dodoo_tech.gfal.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import android.view.View;
import android.view.Window;

import com.dodoo_tech.gfal.app.GFALApp;

import java.util.List;

public class ContextUtil {

    public static Drawable getDrawable(int drawble) {
        return GFALApp.getContext().getResources().getDrawable(drawble);
    }

    public static String getCurProcessName(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid) {
                return info.processName;
            }
        }
        return null;
    }

    /**
     * 全屏隐藏虚拟机，沉浸模式
     *
     * @param window
     */
    public static void hideSystemUI(Window window){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
