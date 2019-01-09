package com.dodoo_tech.gfal.utils;

import android.os.Handler;
import android.widget.Toast;

import com.dodoo_tech.gfal.app.GFALApp;
import com.dodoo_tech.gfal.thread.LooperThread;

/**
 * author hui on 2016/1/26.
 */
public class ToastUtils extends LooperThread{
    private Toast mToast;
    private Handler myHandler;
    private static class ToastUtilsHolder{
        private static ToastUtils toastUtils = new ToastUtils(Thread.MAX_PRIORITY);
    }

    public ToastUtils(int priority) {
        super(priority);
        myHandler = new Handler(getMyLooper());
    }

    private static ToastUtils getInstance(){
        return ToastUtilsHolder.toastUtils;
    }

    /**
     * 显示吐司
     * @param message
     * @param duration
     */
    private void showToast(final String message, final int duration){
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    if (GFALApp.getContext() == null) {
                        return;
                    }else {
                        mToast = Toast.makeText(GFALApp.getContext(), message, duration);
                    }
                }
                mToast.setText(message);
                mToast.setDuration(duration);
                mToast.show();
            }
        });

    }

    /**
     * 显示吐司
     * @param resId
     * @param duration
     */
    private void showToast(final int resId, final int duration){
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    if (GFALApp.getContext() == null) {
                        return;
                    }else {
                        mToast = Toast.makeText(GFALApp.getContext(), resId, duration);
                    }
                }
                mToast.setText(resId);
                mToast.setDuration(duration);
                mToast.show();
            }
        });

    }

    /**
     * 显示吐司
     * @param message
     */
    public static void showToast(String message){
        ToastUtils.getInstance().showToast(message,Toast.LENGTH_SHORT);
    }

    /**
     * 显示吐司
     * @param messageResId
     */
    public static void showToast(int messageResId){
        ToastUtils.getInstance().showToast(messageResId,Toast.LENGTH_SHORT);
    }
    
    /**
     * 显示吐司
     * @param message
     */
    public static void showLongToast(String message){
        ToastUtils.getInstance().showToast(message,Toast.LENGTH_LONG);
    }

    /**
     * 显示吐司
     * @param messageResId
     */
    public static void showLongToast(int messageResId){
        ToastUtils.getInstance().showToast(messageResId,Toast.LENGTH_LONG);
    }
}
