package com.dodoo_tech.gfal.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.dodoo_tech.gfal.R;
import com.dodoo_tech.gfal.utils.LogUtil;

public class BaseDialog extends Dialog {
	private static final String TAG = "BaseDialog";
	private View rootView;
	public BaseDialog(Context context, int resource, ViewGroup root) {
		//使用默认的样式
        this(context,resource,R.style.dialog_base,root);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public BaseDialog(Context context, int resource, int theme, ViewGroup root) {
		//使用自定义的样式
		super(context, theme);
		//构造rootView
		rootView = LayoutInflater.from(context).inflate(resource,root);
	}
	
	public void onResume() {
		
		/*LogUtil.logInfo("BaseDialog-onResume");
    	if(isShowing()){
    		dismiss();
    		show();
    	}*/
    	
    }

	@Override
	public void show() {
		show(-1,-1);
	}

	public void show(int width,int height) {
		try {
			super.show();
			if (rootView!=null){
			    if (width > 0 && height > 0){
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
                    setContentView(rootView,params);
                }else {
                    setContentView(rootView);
                }
			}
		} catch (Exception e) {
			LogUtil.logError(TAG,e);
		}

	}

    //@SuppressWarnings("TypeParameterUnusedInFormals")
    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        if(rootView!=null){
            return rootView.findViewById(id);
        }
        return super.findViewById(id);
    }

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		try{
			if(getContext()!=null){
				super.dismiss();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		try{
			if(getContext()!=null){
				super.cancel();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void show(boolean canceledOnTouchOutside, boolean cancelable) {
		// TODO Auto-generated method stub
		setCanceledOnTouchOutside(canceledOnTouchOutside);
		setCancelable(cancelable);
		show();
	}

	public void showInWindow() {
		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		show();
	}

	public void showInWindow(boolean canceledOnTouchOutside, boolean cancelable) {
		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		show(canceledOnTouchOutside,cancelable);
	}

}
