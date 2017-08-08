package com.example.alipay.myreadingapplication.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Jxq .
 * Description:
 * Date:on 2017/7/24.
 */
public class QueryPrefrences{
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";
    private static final String PREF_IS_ALARM_ON = "isAlarmOn";

    public static String getStoresQuery(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY,null);
    }

    public static void setStoreQuery(Context context,String query){
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_SEARCH_QUERY,query)
                    .apply();
    }

    /**
     * 获取上一次结果ID
     * @param context
     */
    public static String getLastResultId(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_RESULT_ID,null);
    }

    /**
     * 存储上一次结果ID
     * @param context
     * @param lastResultId
     */
    public static void setLastResultId(Context context,String lastResultId){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_RESULT_ID,lastResultId)
                .apply();
    }

    /**
     * 获取定时器的开启状态
     * @param context
     * @return
     */
    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }

    /**
     * 存储定时器的开启状态
     * @param context
     * @param isOn
     */
    public static void setAlarmOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .apply();
    }
}
