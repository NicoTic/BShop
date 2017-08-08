package com.example.alipay.myreadingapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.alipay.myreadingapplication.service.PullService;
import com.example.alipay.myreadingapplication.util.QueryPrefrences;

/**
 * Created by Jxq .
 * Description:广播接收定时器状态，
 * 用于在设备重启后应用也能够在后台轮询网络数据，并在有所发现时通知给用户
 * Date:on 2017/7/25.
 */
public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast intent: " + intent.getAction());
        boolean isOn = QueryPrefrences.isAlarmOn(context);
        PullService.setServiceAlarm(context,isOn);
    }
}
