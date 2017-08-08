package com.example.alipay.myreadingapplication.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jxq .
 * Description:
 * Date:on 2017/7/11.
 */
public class MyOkhttpHelper {
    private static MyOkhttpHelper myOkhttpHelperInstance;
    private static OkHttpClient mClientInstance;
    private Handler handler;
    private Gson mGson;

    public MyOkhttpHelper() {
        mClientInstance = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();

        handler = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    /**
     * 对外公开的GET方法
     * @param url
     * @param params
     * @param baseCallback
     */
    public void get(String url,Map<String,Object> params,BaseCallback baseCallback){
        Request request = buildRequest(url,HttpMethodType.GET,params);
        request(request,baseCallback);
    }

    /**
     * 对外公开的POST方法
     * @param url
     * @param params
     * @param baseCallback
     */
    public void post(String url,Map<String,Object> params,BaseCallback baseCallback){
        Request request = buildRequest(url,HttpMethodType.POST,params);
        request(request,baseCallback);
    }
    /**
     * 获取实例
     * @return
     */
    public static MyOkhttpHelper getInstance() {
        if (myOkhttpHelperInstance == null) {
            synchronized (OkHttpHelper.class) {
                if (myOkhttpHelperInstance == null) {
                    myOkhttpHelperInstance = new MyOkhttpHelper();
                }
            }
        }
        return myOkhttpHelperInstance;
    }


    /**
     *封装一个request方法，不管post或者get方法中都会用到
     * @param request
     * @param baseCallBack
     */
    private void request(final Request request, final BaseCallback baseCallBack){
        baseCallBack.onBeforeRequest(request);
        mClientInstance.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackFailure(baseCallBack,request,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callbackResponse(baseCallBack,response);
                if(response.isSuccessful()&&response!=null){
                    final String resultStr  = response.body().string();
                    Log.d(getClass().getName(), "result=" + resultStr);
                   if(baseCallBack.mType==String.class){
                       callbackSuccess(baseCallBack,response,resultStr);
                   }else{
                       try {
                           Object obj = mGson.fromJson(resultStr, baseCallBack.mType);
                           callbackSuccess(baseCallBack,response,obj);
                       }
                       catch (com.google.gson.JsonParseException e){ // Json解析的错误
                           baseCallBack.onError(response,response.code(),e);
                       }
                   }

                   /*
                    *可以获得字节流,实现文件下载
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    // 储存下载文件的目录
                    String savePath = isExistDir(saveDir);
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File file = new File(savePath, getNameFromUrl(url));
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            // 下载中
                            listener.onDownloading(progress);
                        }
                        fos.flush();
                        // 下载完成
                        listener.onDownloadSuccess();
                    } catch (Exception e) {
                        listener.onDownloadFailed();
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                        }
                    }
                    */

                }
                else{
                    callBackError(baseCallBack,response,null);
                }
            }
        });

    }

    /**
     * 在主线程中执行的解析出错回调
     * @param baseCallBack
     * @param response
     * @param e
     */
    private void callBackError(final BaseCallback baseCallBack, final Response response, final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                baseCallBack.onError(response,response.code(),e);
            }
        });

    }

    /**
     * 在主线程中执行的解析成功回调
     * @param baseCallBack
     * @param response
     * @param resultStr
     */
    private void callbackSuccess(final BaseCallback baseCallBack, final Response response, final Object resultStr) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                baseCallBack.onSuccess(response,resultStr);
            }
        });
    }

    /**
     * 在主线程中执行的请求成功回调
     * @param baseCallBack
     * @param response
     */
    private void callbackResponse(final BaseCallback baseCallBack, final Response response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                baseCallBack.onResponse(response);
            }
        });
    }

    /**
     * 在主线程中执行的请求失败回调
     * @param baseCallBack
     * @param request
     * @param e
     */
    private void callbackFailure(final BaseCallback baseCallBack, final Request request, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                baseCallBack.onFailure(request,e);
            }
        });
    }

    /**
     * 构造请求对象
     * @param url
     * @param httpMethodType
     * @param params
     * @return
     */
    private Request buildRequest(String url, HttpMethodType httpMethodType, Map<String,Object> params){
        Request request = null;
        Request.Builder builder = new Request.Builder().url(url);
        if(httpMethodType==HttpMethodType.GET){
            url = buildUrlParam(url,params);
            request = builder.url(url).get().build();
        }
        if(httpMethodType == HttpMethodType.POST){
            RequestBody requestBody = getRequestBody(params);
            request = builder.post(requestBody).build();
        }
       return request;
    }

    /**
     * 通过Map的键值对构建请求URL
     * @param url
     * @param params
     * @return
     */
    private String buildUrlParam(String url, Map<String, Object> params) {
        if(params==null){
            params = new HashMap<>();
        }
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String,Object> entry:params.entrySet()){
            sb.append(entry.getKey()+"="+(entry.getValue()==null?"":entry.getValue().toString()));
            sb.append("&");
        }
        String s = sb.toString();
        if(s.endsWith("&")){
            s = s.substring(0,s.length()-1);
        }
        if(url.indexOf("?")>0){
            url = url +"&"+s;
        }else{
            url = url +"?"+s;
        }
        return url;
    }

    /**
     * 通过Map的键值对构建请求对象的body
     * @param params
     * @return
     */
    private RequestBody getRequestBody(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if(null!= params){
            for(Map.Entry<String,Object> entry:params.entrySet()){
                builder.add(entry.getKey(),entry.getValue().toString());
            }
        }
        return builder.build();
    }

    private enum HttpMethodType{
        GET,
        POST,
    }
}
