package com.example.alipay.myreadingapplication.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
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
 * Date:on 2017/7/10.
 */
public class OkHttpHelper {
    private static OkHttpHelper mOkhttpHelperInstance;
    private static OkHttpClient mOkHttpClientInstance;
    private Handler handler;
    private Gson gson;

    public OkHttpHelper() {
        mOkHttpClientInstance = new OkHttpClient.Builder()
        .connectTimeout(10,TimeUnit.SECONDS)
        .readTimeout(10,TimeUnit.SECONDS)
        .writeTimeout(10,TimeUnit.SECONDS)
        .build();

        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getmOkhttpHelperInstance() {
        if(mOkhttpHelperInstance==null){
            synchronized (OkHttpHelper.class){
                if(mOkhttpHelperInstance==null){
                    mOkhttpHelperInstance = new OkHttpHelper();
                }
            }
        }
        return mOkhttpHelperInstance;
    }
    /**
     * 对外公开的get方法
     * @param url
     * @param callback
     */
    public void get(String url,BaseCallback callback){
        Request request = buildGetRequest(url);
        request(request,callback);
    }

    private Request buildGetRequest(String url) {
        return buildRequest(url, HttpMethodType.GET,null);
    }

    private Request buildPostRequest(String url, Map<String, Object> param) {
        return buildRequest(url,HttpMethodType.POST,param);
    }

    private Request buildRequest(String url, HttpMethodType methodType, Map<String, Object> param) {
        Request.Builder builder = new Request.Builder().url(url);
        if(methodType==HttpMethodType.GET){
            builder.get();
        }
        if(methodType==HttpMethodType.POST){
            RequestBody body = builderFormData(param);
            builder.post(body);
        }
        return  builder.build();
    }

    private RequestBody builderFormData(Map<String, Object> param) {
        FormBody.Builder builder = new FormBody.Builder();
        if (param != null) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                builder.add(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
            }
        }
        return  builder.build();
    }

    /**
     * 对外公开的post方法
     * @param url
     * @param callback
     */
    public void post(String url,Map<String,Object> param, BaseCallback callback){

        Request request = buildPostRequest(url,param);
        request(request,callback);
    }


    private void request(final Request request, final BaseCallback callback) {
        callback.onBeforeRequest(request);
        mOkHttpClientInstance.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(request,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(response);
                if(response.isSuccessful()){
                    String resultStr = response.body().string();
                    Log.d(String.valueOf(getClass()), "result=" + resultStr);
                    if(callback.mType==String.class){
                        callbackSuccess(callback,response,resultStr);
                    }
                    else{
                        try{
                            Object obj = gson.fromJson(resultStr,callback.mType);
                            callbackSuccess(callback,response,obj);
                        }catch (JsonParseException e){
                            e.printStackTrace();
                            callback.onError(response,response.code(),e);
                        }
                    }
                }else{
                        callbackError(callback,response,null);
                }
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response,response.code(),e);     
            }
        });
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response,obj);     
            }
        });
    }

    enum  HttpMethodType{
        GET,
        POST,
    }
}
