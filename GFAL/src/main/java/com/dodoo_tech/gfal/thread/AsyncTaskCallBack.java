package com.dodoo_tech.gfal.thread;

import android.os.AsyncTask;

import com.dodoo_tech.gfal.utils.LogUtil;


public class AsyncTaskCallBack<T> extends AsyncTask<String,String,T> {

	private CallBack<T> callBack;
	
	public AsyncTaskCallBack(CallBack<T> callBack)
	{
		this.callBack=callBack;
	}
	
	/**
     * 定义回调接口
     */
    public interface CallBack<T> {
    	T run();
        void onPostExecute(T t);
    }
	
	@Override
	protected T doInBackground(String... params) {
		try
		{
			if(callBack!=null){
				return (T) callBack.run();
			}
		}catch (Exception e) {
            LogUtil.logError(e);
		}
		return null;
	}

	@Override
    protected void onPostExecute(T str) {
		try
		{
			if(callBack!=null){callBack.onPostExecute(str);}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
