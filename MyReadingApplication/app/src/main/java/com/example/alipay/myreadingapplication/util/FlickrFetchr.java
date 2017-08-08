package com.example.alipay.myreadingapplication.util;

import android.util.Log;

import com.example.alipay.myreadingapplication.model.MeiZi;
import com.google.gson.Gson;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jxq .
 * Description:网络操作帮助类
 * Date:on 2017/7/25.
 */
public class FlickrFetchr {
    private static final String TAG = "FlickrFetchr";
    private Gson gson;
    /**
     * 获得网络数据（Json数据）
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage()+": with"+ urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead=in.read(buffer))>0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }

    }


    public String getUrlString(String urlSpec)throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    /**
     * 轮询网络地址获得网页数据
     * @return
     */
    public List<MeiZi.ResultsBean> fetchRecentPhotos() {
        String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/1";
        return downloadGalleryItems(url);
    }

    public List<MeiZi.ResultsBean> searchPhotos(String query) {
        String url = "http://gank.io/api/data/"+query+"/20/1";
        return downloadGalleryItems(url);
    }

    /**
     * 获得解析Json之后的List集合
     * @param url
     * @return
     */
    public List<MeiZi.ResultsBean> downloadGalleryItems(String url) {
        List<MeiZi.ResultsBean> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            parseItems(items, jsonString);
            Log.i(TAG, "Received JSON: " + jsonString);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON", e);
        }
        return items;
    }

    /**
     * 解析Json数据
     * @param items
     * @param data
     * @throws IOException
     * @throws JSONException
     */
    private void parseItems(List<MeiZi.ResultsBean> items, String data) throws IOException,JSONException{
        try {
            Gson gson = new Gson();
            MeiZi meizi = gson.fromJson(data,MeiZi.class);
            items.addAll(meizi.getResults());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
