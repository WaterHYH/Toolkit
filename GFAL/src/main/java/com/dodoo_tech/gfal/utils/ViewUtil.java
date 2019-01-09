package com.dodoo_tech.gfal.utils;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by HUI on 2017/8/23.
 */

public class ViewUtil {

	/**
	 * 设置跑马灯效果
	 * @param textView
	 */
	public static void setMarquee(TextView textView){
		textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		textView.setSingleLine();
		textView.setSelected(true);
		textView.setFocusable(true);
		textView.setFocusableInTouchMode(true);
	}
}
