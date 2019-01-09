package com.dodoo_tech.gfal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by HUI on 2017/8/22.
 */

public class SharedPrefsUtil {

    private static final String CLASS_NAME = "SharedPrefsUtil";

    public static boolean putValue(Context context, String key, long value) {
		SharedPreferences.Editor sp =  PreferenceManager.getDefaultSharedPreferences(context).edit();
		sp.putLong(key,value);
		return sp.commit();
	}
	public static boolean putValue(Context context, String key, int value) {
		SharedPreferences.Editor sp =  PreferenceManager.getDefaultSharedPreferences(context).edit();
		sp.putInt(key, value);
		return sp.commit();
	}
	public static boolean putValue(Context context, String key, boolean value) {
		SharedPreferences.Editor sp =  PreferenceManager.getDefaultSharedPreferences(context).edit();
		sp.putBoolean(key, value);
		return sp.commit();
	}
	public static boolean putValue(Context context, String key, String value) {
		SharedPreferences.Editor sp =  PreferenceManager.getDefaultSharedPreferences(context).edit();
		sp.putString(key, value);
		return sp.commit();
	}
	public static int getValue(Context context, String key, int defValue) {
        try{
            SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
            int value = sp.getInt(key, defValue);
            return value;
        }catch (Exception e){
            LogUtil.logError(CLASS_NAME,e);
        }
		return defValue;
	}
	public static long getValue(Context context, String key, long defValue) {
        try{
            SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
            long value = sp.getLong(key, defValue);
            return value;
        }catch (Exception e){
            LogUtil.logError(CLASS_NAME,e);
        }
		return defValue;
	}
	public static boolean getValue(Context context, String key, boolean defValue) {
        try{
            SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
            boolean value = sp.getBoolean(key, defValue);
            return value;
        }catch (Exception e){
            LogUtil.logError(CLASS_NAME,e);
        }
		return defValue;
	}
	public static String getValue(Context context, String key, String defValue) {
        try{
            SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
            String value = sp.getString(key, defValue);
            return value;
        }catch (Exception e){
            LogUtil.logError(CLASS_NAME,e);
        }
		return defValue;
	}
}
