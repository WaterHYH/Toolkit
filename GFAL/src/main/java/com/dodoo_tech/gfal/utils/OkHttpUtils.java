package com.dodoo_tech.gfal.utils;

import com.dodoo_tech.gfal.app.GFALApp;
import com.dodoo_tech.gfal.thread.AsyncTaskCallBack;
import com.dodoo_tech.gfal.thread.ExecuterPoolHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by MOU on 2016/9/18.
 */
public class OkHttpUtils {

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    private static final String CLASS_NAME = "OkHttpUtils";

    public static String SendGet(String url)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        //构建一个请求对象
        Request request = new Request.Builder().url(url).build();
        //发送请求
        try {
            Response response = okHttpClient.newCall(request).execute();
            //打印服务端传回的数据
            String result=response.body().string();
            return result;
        } catch (Exception e) {
            LogUtil.logError(CLASS_NAME,e);
        }

        return null;
    }

    public static void SendGet_post(final String url, final sendCallBack callback)
    {
        AsyncTaskCallBack<String> task=new AsyncTaskCallBack<String>(new AsyncTaskCallBack.CallBack<String>() {

            @Override
            public String run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                //构建一个请求对象
                Request request = new Request.Builder().url(url).build();
                //发送请求
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    //打印服务端传回的数据
                    String result=response.body().string();
                    return result;
                } catch (Exception e) {
                    LogUtil.logError(CLASS_NAME,e);
                }

                return null;
            }

            @Override
            public void onPostExecute(String repjson) {
                try {
                    callback.onPostExecute(repjson);
                }catch (Exception e)
                {
                    LogUtil.logError(CLASS_NAME,e);
                }
            }
        });

        task.executeOnExecutor(ExecuterPoolHolder.getExecuterPool());

    }

    public static String SendPost(String url, HashMap<String, Object> paramsMap)
    {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        FormBody.Builder builder = new FormBody.Builder();
        //追加参数
        for (String key : paramsMap.keySet()) {
            Object object = paramsMap.get(key);
            builder.add(key, object.toString());
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
            Response response=okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                String result=response.body().string();
                return result;
            }
        } catch (Exception e) {
            LogUtil.logError(CLASS_NAME,e);
        }

        return null;
    }

    public static String SendJson(final String url, final String json)
    {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response=okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                String result=response.body().string();
                return result;
            }
        } catch (Exception e) {
            LogUtil.logError(CLASS_NAME,e);
           // LogUtil.logInfo(e);
        }

        return null;

    }

    public static String SendPostFile(String url, HashMap<String, Object> paramsMap)
    {
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        //追加参数
        for (String key : paramsMap.keySet()) {
            Object object = paramsMap.get(key);
            if (!(object instanceof File)) {
                builder.addFormDataPart(key, (String) object);
                //LogUtil.logInfo(key+":"+(String) object);
            } else {
                File file = (File) object;
                builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                //LogUtil.logInfo(key+":"+ file.getAbsolutePath());
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
            Response response=okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                String result=response.body().string();
                return result;
            }
        } catch (Exception e) {
            LogUtil.logError(CLASS_NAME,e);
        }

        return null;
    }

    /**
     * 发送post请求到服务器，并返回服务器json串，不能在主线程调用
     * @param url 服务器url
     * @param json 请求参数json串
     * @return 服务器返回的json串，出错则返回null
     */
    public static String sendPostJson(String url, String json){
        LogUtil.logInfo(CLASS_NAME,"sendPostJson","url="+url+" json="+json);
        try {
            //申明给服务端传递一个json串
            //创建一个OkHttpClient对象
            OkHttpClient okHttpClient = new OkHttpClient();
            //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
            RequestBody requestBody = RequestBody.create(JSON, json);
            //创建一个请求对象
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            //发送请求获取响应
            Response response=okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                String resultStr=response.body().string();
                return resultStr;
            }
        } catch (Exception e) {
            LogUtil.logError(CLASS_NAME,e);
        }
        return null;
    }

    public static void SendPostJson(final String url, final String json, final sendCallBack callback)
    {
        AsyncTaskCallBack<String> task=new AsyncTaskCallBack<String>(new AsyncTaskCallBack.CallBack<String>() {

            @Override
            public String run() {
                //申明给服务端传递一个json串
                //创建一个OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                RequestBody requestBody = RequestBody.create(JSON, json);
                //创建一个请求对象
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                //发送请求获取响应
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    //判断请求是否成功
                    if(response.isSuccessful()){
                        //打印服务端返回结果
                        String result=response.body().string();
                        return result;
                    }
                } catch (Exception e) {
                    LogUtil.logError(CLASS_NAME,e);
                    //LogUtil.logInfo(e);
                }

                return null;
            }

            @Override
            public void onPostExecute(String repjson) {
                try {
                    callback.onPostExecute(repjson);
                }catch (Exception e)
                {
                    LogUtil.logError(CLASS_NAME,e);
                }
            }
        });

        task.executeOnExecutor(ExecuterPoolHolder.getExecuterPool());

    }

    public interface sendCallBack {
        void  onPostExecute(String repjson) throws Exception;
    }

    public interface loadUrlCallBack {
        void fail(Call call, Exception e);
        void success(String url, String savePath);
    }

    public static void loadUrl(final String url, final String savePath, final loadUrlCallBack callback) {

        try {

            //构建一个请求对象
            new AsyncTaskCallBack<Long>(new AsyncTaskCallBack.CallBack<Long>() {
                @Override
                public Long run() {
                    try {
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        Request request = new Request.Builder().url(url).head().build();
                        Response response1 = mOkHttpClient.newCall(request).execute();
                        long contentLength = (int) response1.body().contentLength();
                        response1.close();
                        response1 = null;

                        return contentLength;
                    }catch (Exception e)
                    {
                        LogUtil.logError(CLASS_NAME,e);
                    }

                    return new Long(0);
                }

                @Override
                public void onPostExecute(final Long contentLength) {

                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Call call = mOkHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogUtil.logWarn(CLASS_NAME,"loadUrl",e);
                            FileUtil.deleteFile(savePath);
                            callback.fail(call, e);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            try {
                                FileUtil.deleteFile(savePath);
                                //创建文件对象，用来存储新的图像文件
                                File file = new File(savePath);
                                //创建文件
                                file.createNewFile();


                                byte[] buffer = new byte[1024 * 5];
                                InputStream is = response.body().byteStream();

                                long dlLen = 0;
                                int len;
                                FileOutputStream fos = new FileOutputStream(savePath);
                                while ((len = is.read(buffer)) != -1) {
                                    fos.write(buffer, 0, len);
                                    dlLen += len;
                                }
                                fos.flush();
                                if (is != null) {
                                    is.close();
                                }
                                if (fos != null) {
                                    fos.close();
                                }

                                if (dlLen == contentLength) {
                                    callback.success(url, savePath);
                                } else {
                                    callback.fail(call, null);
                                }

                            } catch (Exception e) {
                                callback.fail(call, e);
                            }

                        }

                    });
                }
            }).executeOnExecutor(ExecuterPoolHolder.getExecuterPool());

        }catch (Exception e)
        {
            LogUtil.logError(CLASS_NAME,e);
            //LogUtil.logInfo(e);
            callback.fail(null, e);
        }
    }

    public static void  loadUrlHttps(final String url, final String savePath, final loadUrlCallBack callback) {


        OkHttpClient mOkHttpClient = getUnsafeOkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.logWarn(CLASS_NAME,"loadUrlHttps",e);

                FileUtil.deleteFile(savePath);
                callback.fail(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    //创建文件对象，用来存储新的图像文件
                    File file = new File(savePath);
                    FileUtil.deleteFile(file.getAbsolutePath());
                    //创建文件
                    file.createNewFile();

                    byte[] buffer = new byte[1024 * 5];
                    InputStream is = response.body().byteStream();
                    int len;
                    FileOutputStream fos = new FileOutputStream(savePath);
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }

                    callback.success(url, savePath);
                }catch (Exception e)
                {
                    callback.fail(call,e);
                }


            }

        });
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
