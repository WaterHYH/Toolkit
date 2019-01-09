package com.dodoo_tech.gfal.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;

import com.dodoo_tech.gfal.utils.LogUtil;
import com.dodoo_tech.gfal.utils.SystemUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hui on 19-1-8.
 */
public class GFALApp extends Application {

    private static final String TAG = "GFALApp";
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static Executor getExecuterPool() {
        return ExecuterPoolHolder.THREAD_POOL_EXECUTOR;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //初始化自定义log
        initAppLog();

    }

    private void initAppLog() {
        LogUtil.setDebugMode(true);
        LogUtil.setLogToFile(true);
        LogUtil.setPackageName(getPackageName());
        int processID = Process.myPid();
        String processName = SystemUtil.getProcessName(processID);
        LogUtil.logInfo(TAG, "initAppLog processId=" + processID + " processName=" + processName + " threadId=" + Thread.currentThread().getId());
    }

    protected String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 自定义线程池管理类
     */
    private static class ExecuterPoolHolder {
        private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
        private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        private static final int KEEP_ALIVE = 1;

        private static final ThreadFactory sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
            }
        };

        private static final BlockingQueue<Runnable> sPoolWorkQueue =
                new LinkedBlockingQueue<Runnable>(128);

        /**
         * 自定义线程池
         */
        public static final Executor THREAD_POOL_EXECUTOR
                = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
    }
}
