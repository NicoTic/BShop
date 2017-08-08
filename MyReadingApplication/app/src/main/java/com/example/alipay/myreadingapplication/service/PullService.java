package com.example.alipay.myreadingapplication.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.alipay.myreadingapplication.R;
import com.example.alipay.myreadingapplication.activity.MainActivity;
import com.example.alipay.myreadingapplication.model.MeiZi;
import com.example.alipay.myreadingapplication.util.FlickrFetchr;
import com.example.alipay.myreadingapplication.util.QueryPrefrences;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Jxq .
 * Description:后台服务
 * Date:on 2017/7/24.
 */
public class PullService extends IntentService {
    private static final String TAG = "PullService";
    private static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES; // =

    public static final String ACTION_SHOW_NOTIFICATION =
            "com.example.alipay.myreadingapplication.service.SHOW_NOTIFICATION";

    public static final String PERM_PRIVATE =
            "com.bignerdranch.android.photogallery.PRIVATE";

    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";

    /**
     * 用于启动后台服务的Intent
     */
    public static Intent newIntent(Context context){
        return new Intent(context,PullService.class);
    }

    /**
     * 设置定时器管理后台服务
     * @param context
     * @param isOn
     */
    public static void setServiceAlarm(Context context,boolean isOn){
        Intent i = PullService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,0,i,0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(isOn){
            //启动定时器
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),POLL_INTERVAL,pi);
        }else {
            //取消定时器
            alarmManager.cancel(pi);
            pi.cancel();
        }
        //将定时器的启停状态存储起来
        QueryPrefrences.setAlarmOn(context,isOn);
    }

    /**
     * 判断PenddingIntent是否为空
     * 如果为空，则定时器未启动，返回false
     */
    public static boolean isServiceAlarmOn(Context context){
        Intent i = PullService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context,0,i,PendingIntent.FLAG_NO_CREATE);
        return  pi!=null;
    }

    public PullService() {
        super(TAG);
//        aCache = ACache.get(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
        if(!isNetworkAvailableAndConnected()){
            return;
        }
        //获得上一次结果的第一条数据的ID
        String lastResultId = QueryPrefrences.getLastResultId(this);
        List<MeiZi.ResultsBean> items = new ArrayList<>();
        //获取最新返回集
        items = new FlickrFetchr().fetchRecentPhotos();

        if (items.size() == 0) {
            return;
        }
        //如果有结果，抓取第一条结果
        String resultId = items.get(0).get_id();
        /**
         * 确认是否不同于上一次结果id
         */
        if(resultId.equals(lastResultId)){
            Log.i(TAG, "Got an old result: " + resultId);
        }else{
            Log.i(TAG, "Got a new result: " + resultId);
            /**
             * 发送一条通知
             */
            Resources resources = getResources();
            Intent i = MainActivity.newIntent(this);
            PendingIntent pi = PendingIntent.getActivity(this,0,i,0);

            Notification notification = new NotificationCompat.Builder(this)
                                        .setTicker(resources.getString(R.string.new_pictures_title))
                                        .setSmallIcon(android.R.drawable.ic_menu_report_image)
                                        .setContentTitle(resources.getString(R.string.new_pictures_title))
                                        .setContentText(resources.getString(R.string.new_pictures_text))
                                        .setContentIntent(pi)
                                        .setAutoCancel(true)
                                        .build();

//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            notificationManager.notify(0,notification);

//            sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION),PERM_PRIVATE);
            showBackgroundNotification(0, notification);

        }
        //将第一条结果存入SharedPreference
        QueryPrefrences.setLastResultId(this, resultId);

    }

    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(i, PERM_PRIVATE, null, null,
                Activity.RESULT_OK, null, null);
    }

    /**
     * 判断是否能够获得网络及网络是否打开
     * @return
     */
    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo()!=null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

}
