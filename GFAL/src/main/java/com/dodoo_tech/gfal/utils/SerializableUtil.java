package com.dodoo_tech.gfal.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by HUI on 2017/6/28.
 */

public class SerializableUtil {

	private static final String TAG = "SerializableUtil";

	/**
	 * 序列化对象到本地文件
	 * @param path
	 * @param saveObject
	 * @return true=成功，false=失败
	 */
	public static final boolean saveObject(String path, Object saveObject){
		if (StringUtil.isEmpty(path)||saveObject==null){
			return false;
		}
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		File file = new File(path);
		boolean isSuccess = false;
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(saveObject);
			isSuccess = true;
		}catch (Exception e){
			e.printStackTrace();
			LogUtil.logError(TAG,e);
		}finally {
			try{
				if (oos!=null){
					oos.close();
				}
				if (fos!=null){
					fos.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return isSuccess;
	}

	/**
	 * 从本地文件中反序列化对象
	 * @param path
	 * @return
	 */
	public static final Object restoreObject(String path){
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Object object = null;
		File file = new File(path);
		if (!file.exists()){
			return null;
		}
		try{
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			object = ois.readObject();
		}catch (Exception e){
			e.printStackTrace();
			LogUtil.logError(TAG,e);
		}finally {
			try{
				if (ois!=null){
					ois.close();
				}
				if (fis!=null){
					fis.close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return object;
	}
}
