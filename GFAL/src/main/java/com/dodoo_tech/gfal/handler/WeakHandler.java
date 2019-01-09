package com.dodoo_tech.gfal.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by HUI on 2017/6/9.
 */

public abstract class WeakHandler<T> extends Handler implements HandlerInterface {
	private WeakReference<T> weakReference = new WeakReference<T>(null);
	protected abstract void handleWeakMessage(T weak, Message msg);

	public WeakHandler(T weak){
		super();
		weakReference = new WeakReference<T>(weak);
	}

	public WeakHandler(Looper looper, T weak){
		super(looper);
		weakReference = new WeakReference<T>(weak);
	}

	@Override
	public void destory() {
		weakReference = new WeakReference<T>(null);
		removeCallbacksAndMessages(null);
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (getWeak()!=null){
			handleWeakMessage(weakReference.get(),msg);
		}else {
			destory();
		}
	}

	public T getWeak(){
		return weakReference.get();
	}
}
