package com.dodoo_tech.gfal.thread;

import android.os.Looper;

/**
 * Created by HUI on 2017/7/25.
 */

public class LooperThread extends Thread {
	private final Object mLock = new Object();
	private Looper myLooper;

	public LooperThread(int priority) {

		setPriority(priority);
		start();
		synchronized (mLock) {
			while (myLooper == null) {
				try {
					mLock.wait();
				} catch (InterruptedException ex) {
				}
			}
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized (mLock) {
			Looper.prepare();
			myLooper = Looper.myLooper();
			mLock.notifyAll();
		}
		Looper.loop();
	}

	public Looper getMyLooper() {
		return myLooper;
	}
}
