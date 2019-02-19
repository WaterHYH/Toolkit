package com.dodoo_tech.gfal.utils;

import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.Layout;
import android.text.TextPaint;
import android.widget.EditText;

/**
 * Created by HUI on 2017/6/27.
 */

public class StringUtil {
	private static final String TAG = "StringUtil";

	/**
	 * Returns true if the string is null or 0-length or " "
	 * @param str the string to be examined
	 * @return true if str is null or zero length
	 */
	public static boolean isEmpty(@Nullable String str) {
		if (str == null || str.trim().length() == 0)
			return true;
		else
			return false;
	}

	public static String bundleToString(@Nullable Bundle bundle){
		if (bundle == null) {
			return "null";
		}
		String str = "{ ";
		try{
			for (String key : bundle.keySet()) {
				str += key + "=" + bundle.get(key) + " ";
			}
			str += "}";
		}catch (Exception e){
			e.printStackTrace();
			LogUtil.logError(TAG,e);
		}
		return str;
	}

	/**
	 * 获取字符串的宽度，既一整行字符串占用多少px
	 * @param text 要获取宽度的字符串
	 * @param size 字体大小，单位：像素(px)
	 * @return 字符串宽度，单位：像素(px)
	 */
	public static float getTextWidth(String text, float size){
		if (isEmpty(text)){
			return 0;
		}
		Paint paint = new Paint();
		paint.setTextSize(size);
		// 得到总体长度
		return paint.measureText(text);
	}

	/**
	 * 获取EditText里文本的总长度（不是个数）
	 * @param editText
	 * @return 字符串总长度，单位：像素(px)
	 */
	public static float getTextWidth(EditText editText){
		if (editText == null || isEmpty(editText.getText().toString())){
			return 0;
		}
		TextPaint paint = editText.getPaint();
		Editable editable = editText.getText();
		// 得到总体长度
		return Layout.getDesiredWidth(editable.toString(),0,editable.length(),paint);
	}
}
