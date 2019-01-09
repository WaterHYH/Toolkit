package com.dodoo_tech.gfal.ui.dialog;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dodoo_tech.gfal.R;

public class LoadingDialog extends BaseDialog {
	private static final String TAG ="LoadingDialog";

    private TextView dialog_waiting_content;
    private ImageView dialog_waiting_iv;
    /**
     * 等待指定毫秒后隐藏,默认10秒超时,单位：毫秒
     */
    private long duration = 10000;
    private CharSequence content;
    private LoadingListener loadingListener;
    private boolean isShowPic = true;

    private LoadingDialog(Context context) {
        super(context,R.layout.dialog_loading, R.style.dialog_base,null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_waiting_content= (TextView) findViewById(R.id.dialog_waiting_content);
        dialog_waiting_iv= (ImageView) findViewById(R.id.dialog_waiting_iv);

        Glide.with(context).asGif().load(R.mipmap.loading).into(dialog_waiting_iv);

    }

    public static LoadingDialog newInstance(Context context){
        return new LoadingDialog(context);
    }

    @Override
    public void show() {
        super.show();
        //显示文字内容
        showContent();
        if (isShowPic) {
            dialog_waiting_iv.setVisibility(View.VISIBLE);
        }else {
            dialog_waiting_iv.setVisibility(View.GONE);
        }

        //设置超时隐藏
        new Handler().postDelayed(new LoadingDurationThread(this,loadingListener),duration);

    }

    private void showContent() {
        if (content == null || content.toString().trim().length() == 0) {
            dialog_waiting_content.setVisibility(View.GONE);
        }else {
            dialog_waiting_content.setVisibility(View.VISIBLE);
            dialog_waiting_content.setText(content);
        }
    }

    public boolean isShowPic() {
        return isShowPic;
    }

    public LoadingDialog setShowPic(boolean showPic) {
        isShowPic = showPic;
        return this;
    }

    public LoadingDialog setContent(CharSequence content) {
        this.content = content;
        return this;
    }

    /**
     * 必须大于0，否则取默认值（10秒）
     * @param duration
     * @return
     */
    public LoadingDialog setDuration(long duration) {
        if (duration > 0) {
            this.duration = duration;
        }
        return this;
    }

    public LoadingDialog setLoadingListener(LoadingListener loadingListener) {
        this.loadingListener = loadingListener;
        return this;
    }

    public interface LoadingListener{
        /**
         * 显示超时自动隐藏时回调
         */
        void onTimeout();
    }

    private static final class LoadingDurationThread implements Runnable {
        private LoadingDialog dialog;
        private LoadingListener loadingListener;

        public LoadingDurationThread(LoadingDialog loadingDialog, LoadingListener loadingListener) {
            dialog = loadingDialog;
            this.loadingListener = loadingListener;
        }

        @Override
        public void run() {
            if (dialog!=null&&dialog.isShowing()) {
                dialog.cancel();
                dialog = null;
                if (loadingListener!=null) {
                    loadingListener.onTimeout();
                }
            }
        }
    }

}

