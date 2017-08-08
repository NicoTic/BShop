package com.example.alipay.myreadingapplication.receiver;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.alipay.myreadingapplication.service.PullService;

/**
 * Created by Jxq .
 * Description:过滤前台通知的广播
 * Date:on 2017/7/25.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"received result: "+getResultCode());
        if(getResultCode()!= Activity.RESULT_OK){
            return;
        }
        int requestCode = intent.getIntExtra(PullService.REQUEST_CODE,0);
        Notification notification = intent.getParcelableExtra(PullService.NOTIFICATION);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(requestCode,notification);
    }
}
