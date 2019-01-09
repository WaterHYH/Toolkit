package com.dodoo_tech.gfal.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dodoo_tech.gfal.entity.AuthenticationInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class SystemUtil {

	private static final String TAG = "SystemUtil";
	public static final String DODOO_CONFIG_PATH="/protect_f/";
	public static final String DODOO_CONFIG_FILENAME=DODOO_CONFIG_PATH+"dodoo.config";

	/**
	 * 获取设备sn号
	 * @return
	 */
	public static String getSerialNumber() {
        String serialNumber = getSysParameter("gsm.serial");
        if(!StringUtil.isEmpty(serialNumber)) {
            String[] str = serialNumber.split("\\s+");
            serialNumber = str[0];
        }
        return serialNumber;

	}

	public synchronized static AuthenticationInfo gettDodooAuthentication()
	{
		AuthenticationInfo info=null;
		try {
			String str=readFile(DODOO_CONFIG_FILENAME);
			if(!TextUtils.isEmpty(str))
			{
				info= JSON.parseObject(str,new TypeReference<AuthenticationInfo>(){});
			}
		} catch (Exception e) {
			LogUtil.logError(TAG,e);
		}
		return info;
	}

	//读文件
	public static String readFile(String fileName) throws IOException {

		File file = new File(fileName);
		if(!file.exists())
		{
			return null;
		}
		FileInputStream fis = new FileInputStream(file);

		int length = fis.available();

		byte [] buffer = new byte[length];

		fis.read(buffer);

		String res = new String(buffer, "UTF-8");

		fis.close();

		return res;


	}

	public static String getRomVersionCode() {
		return getSysParameter("ro.custom.build.version");
	}

	private static String getSysParameter(String key) {
		String value = null;
		if (key == null || key.isEmpty()) {
			return value;
		}
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class);
			value = (String) get.invoke(c, key);
			value = value.trim();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	/**
	 * 获取进程号对应的进程名
	 *
	 * @param pid 进程号
	 * @return 进程名
	 */
	public static String getProcessName(int pid) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
			String processName = reader.readLine();
			if (!TextUtils.isEmpty(processName)) {
				processName = processName.trim();
			}
			return processName;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			LogUtil.logError(TAG,throwable);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException exception) {
				exception.printStackTrace();
				LogUtil.logError(TAG,exception);
			}
		}
		return null;
	}

	public static String getMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

}
