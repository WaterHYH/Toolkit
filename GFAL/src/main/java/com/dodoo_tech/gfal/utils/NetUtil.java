package com.dodoo_tech.gfal.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jim on 16-6-21.
 */
public class NetUtil {

    private static final String TAG = "NetUtil";

    private static class OkHttpClientHolder{
        private static OkHttpClient instance = new OkHttpClient();
    }
    private static OkHttpClient getInstance(){
        return OkHttpClientHolder.instance;
    }

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    private static Request.Builder makeReqBuilder(String url, RequestBody body){
        if (body==null){
            return makeReqBuilder(url);
        }else {
            return new Request.Builder().url(url).post(body);
        }
    }
    private static Request.Builder makeReqBuilder(String url){
        return new Request.Builder().url(url);
    }

    private static String makeResult(Request.Builder builder) throws IOException {
        Response response = getInstance().newCall(builder.build()).execute();
        if (response!=null&&response.isSuccessful()) {
            return response.body().string();
        } else {
            return "";
        }
    }

    /**
     * 获取HTTP的Get请求返回值(阻塞)
     * @param url
     * @return
     * @throws IOException
     */
    public static String HttpGet(String url) throws IOException {
        Request.Builder builder = makeReqBuilder(url);
        return makeResult(builder);
    }

    /**
     * 获取HTTP的Post请求返回值(阻塞)
     * @param url
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public static String HttpPost(String url, JSONObject jsonObject) throws IOException {
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request.Builder builder = makeReqBuilder(url,body);
        return makeResult(builder);
    }

    public static String HttpPostFile(String url, HashMap<String, Object> paramsMap)
    {
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        //追加参数
        for (String key : paramsMap.keySet()) {
            Object object = paramsMap.get(key);
            if (!(object instanceof File)) {
                builder.addFormDataPart(key, (String) object);
                LogUtil.logInfo(TAG,"HttpPostFile "+key+":"+(String) object);
            } else {
                File file = (File) object;
                builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                LogUtil.logInfo(TAG,"HttpPostFile "+key+":"+ file.getAbsolutePath());
            }
        }
        //创建RequestBody
        RequestBody body = builder.build();

        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        //发送请求获取响应
        try {
            Response response=getInstance().newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                String result=response.body().string();
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    //是否连接WIFI
    public synchronized static boolean isWifiConnected(Context context) {
        if (context == null) {
            return false;
        }

        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }

        }catch(Exception e){
            e.printStackTrace();
            LogUtil.logError(TAG,e);
        }

        return false;
    }

}
