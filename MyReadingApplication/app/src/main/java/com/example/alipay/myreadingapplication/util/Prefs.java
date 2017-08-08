package com.example.alipay.myreadingapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jxq .
 * Description:
 * Date:on 2017/7/19.
 */
public class Prefs {
    private static final String PRE_LOAD = "preLoad";
    private static final String PREFS_NAME = "prefs";
    private static Prefs instance;
    private final SharedPreferences sharedPreferences;

    public Prefs(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
    }

    public static Prefs with(Context context){
        if(instance==null){
            instance = new Prefs(context);
        }
        return instance;
    }

    public void setPrefLoad(boolean totlaTime){
        sharedPreferences.edit().putBoolean(PRE_LOAD,totlaTime).apply();
    }

    public boolean getPreLoad(){
        return sharedPreferences.getBoolean(PRE_LOAD, false);
    }
}
