package com.dodoo_tech.gfal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * Created by HUI on 2017/8/22.
 */

public class SharedPrefsUtil {

    private static final String CLASS_NAME = "SharedPrefsUtil";

    public static boolean putValue(Context context,String preferencesName, String key, long value) {
    	try {
			SharedPreferences.Editor editor =  null;
			if (TextUtils.isEmpty(preferencesName)) {
				editor =  PreferenceManager.getDefaultSharedPreferences(context).edit();
			}else {
				editor =  context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE).edit();
			}
			editor.putLong(key,value);
			return editor.commit();
    	}catch (Exception e){
    		LogUtil.logError(CLASS_NAME,e);
    	}
		return false;
	}
	public static boolean putValue(Context context, String preferencesName, String key, int value) {
		try {
			SharedPreferences.Editor editor =  null;
			if (TextUtils.isEmpty(preferencesName)) {
				editor =  PreferenceManager.getDefaultSharedPreferences(context).edit();
			}else {
				editor =  context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE).edit();
			}
			editor.putInt(key,value);
			return editor.commit();
		}catch (Exception e){
			LogUtil.logError(CLASS_NAME,e);
		}
		return false;
	}
	public static boolean putValue(Context context, String preferencesName, String key, boolean value) {
		try {
			SharedPreferences.Editor editor =  null;
			if (TextUtils.isEmpty(preferencesName)) {
				editor =  PreferenceManager.getDefaultSharedPreferences(context).edit();
			}else {
				editor =  context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE).edit();
			}
			editor.putBoolean(key,value);
			return editor.commit();
		}catch (Exception e){
			LogUtil.logError(CLASS_NAME,e);
		}
		return false;
	}
	public static boolean putValue(Context context, String preferencesName, String key, String value) {
		try {
			SharedPreferences.Editor editor =  null;
			if (TextUtils.isEmpty(preferencesName)) {
				editor =  PreferenceManager.getDefaultSharedPreferences(context).edit();
			}else {
				editor =  context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE).edit();
			}
			editor.putString(key,value);
			return editor.commit();
		}catch (Exception e){
			LogUtil.logError(CLASS_NAME,e);
		}
		return false;
	}
	public static int getValue(Context context, String preferencesName, String key, int defValue) {
        try{
            SharedPreferences sp = null;
			if (TextUtils.isEmpty(preferencesName)) {
				sp =  PreferenceManager.getDefaultSharedPreferences(context);
			}else {
				sp =  context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE);
			}
            int value = sp.getInt(key, defValue);
            return value;
        }catch (Exception e){
            LogUtil.logError(CLASS_NAME,e);
        }
		return defValue;
	}
	public static long getValue(Context context, String preferencesName, String key, long defValue) {
        try{
			SharedPreferences sp = null;
			if (TextUtils.isEmpty(preferencesName)) {
				sp =  PreferenceManager.getDefaultSharedPreferences(context);
			}else {
				sp =  context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE);
			}
            long value = sp.getLong(key, defValue);
            return value;
        }catch (Exception e){
            LogUtil.logError(CLASS_NAME,e);
        }
		return defValue;
	}
	public static boolean getValue(Context context, String preferencesName, String key, boolean defValue) {
        try{
			SharedPreferences sp = null;
			if (TextUtils.isEmpty(preferencesName)) {
				sp =  PreferenceManager.getDefaultSharedPreferences(context);
			}else {
				sp =  context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE);
			}
            boolean value = sp.getBoolean(key, defValue);
            return value;
        }catch (Exception e){
            LogUtil.logError(CLASS_NAME,e);
        }
		return defValue;
	}
	public static String getValue(Context context, String preferencesName, String key, String defValue) {
        try{
			SharedPreferences sp = null;
			if (TextUtils.isEmpty(preferencesName)) {
				sp =  PreferenceManager.getDefaultSharedPreferences(context);
			}else {
				sp =  context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE);
			}
            String value = sp.getString(key, defValue);
            return value;
        }catch (Exception e){
            LogUtil.logError(CLASS_NAME,e);
        }
		return defValue;
	}

    public static boolean putValue(Context context, String key, long value) {
    	return putValue(context,null,key,value);
	}
	public static boolean putValue(Context context, String key, int value) {
		return putValue(context,null,key,value);
	}
	public static boolean putValue(Context context, String key, boolean value) {
		return putValue(context,null,key,value);
	}
	public static boolean putValue(Context context, String key, String value) {
		return putValue(context,null,key,value);
	}
	public static int getValue(Context context, String key, int defValue) {
    	return getValue(context,null,key,defValue);
	}
	public static long getValue(Context context, String key, long defValue) {
		return getValue(context,null,key,defValue);
	}
	public static boolean getValue(Context context, String key, boolean defValue) {
		return getValue(context,null,key,defValue);
	}
	public static String getValue(Context context, String key, String defValue) {
		return getValue(context,null,key,defValue);
	}
}
