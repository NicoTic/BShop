package com.example.alipay.myreadingapplication.realm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.alipay.myreadingapplication.MyApplication;
import com.example.alipay.myreadingapplication.model.Book;
import com.example.alipay.myreadingapplication.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Jxq .
 * Description:
 * Date:on 2017/7/19.
 */
public class RealmController {
    private static String TAG = "RealmManager";
    private static RealmConfiguration realmConfiguration;
    private static Realm realm;

    public static Realm getSaveRealm() {
        return getRealm();
    }
    public static Realm getRealm() {
        try {
            if (realm == null) {
                realm = Realm.getDefaultInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Realm.deleteRealm(realmConfiguration);
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    public static void initializeRealmConfig(Context appContext) {
        if(realmConfiguration == null) {
            Log.d(TAG, "Initializing Realm configuration.");
            setRealmConfiguration(new RealmConfiguration.Builder(appContext).initialData(new RealmInitialData())
                    .deleteRealmIfMigrationNeeded()
                    .build());
        }
    }

    public static void setRealmConfiguration(RealmConfiguration realmConfiguration){
        RealmController.realmConfiguration = realmConfiguration;
        realm.setDefaultConfiguration(realmConfiguration);
    }

   private static int activityCount = 0;

   public static void incrementCount(){
        if(activityCount==0){
            if(realm!=null){
                if(!realm.isClosed()){
                    Log.w(TAG, "Unexpected open Realm found.");
                    realm.close();
                }
            }
            Log.d(TAG, "Incrementing Activity Count [0]: opening Realm.");
            realm = Realm.getDefaultInstance();
        }
       activityCount++;
       Log.d(TAG, "Increment: Count [" + activityCount + "]");
   }

    public static void decrementCount() {
        activityCount--;
        Log.d(TAG, "Decrement: Count [" + activityCount + "]");
        if(activityCount <= 0) {
            Log.d(TAG, "Decrementing Activity Count: closing Realm.");
            activityCount = 0;
            realm.close();
            if(Realm.compactRealm(realmConfiguration)) {
                Log.d(TAG, "Realm compacted successfully.");
            }
            realm = null;
        }
    }

    /**
     * 添加数据，当数据有的时候就会更新
     *
     * @param realObject
     * @return
     */
    public static void add(final RealmObject realObject) {
        Realm realm = RealmController.getSaveRealm();

        realm.executeTransactionAsync(new Realm.Transaction() {
            public void execute(Realm realm) {
                if (realObject != null) {
                    realm.insertOrUpdate(realObject);
                }
            }
        });
    }

    /**
     * 添加数据List，当数据有的时候就会更新
     */
    public static void addAll(final List<? extends RealmObject> realObjects) {
        if (!CollectionUtils.isEmpty(realObjects)) {
            Realm realm = RealmController.getSaveRealm();
            realm.executeTransaction(new Realm.Transaction() {
                public void execute(Realm realm) {
                    realm.insertOrUpdate(realObjects);
                }
            });

        }
    }

    /**
     * 得到对应的本地数据的数量
     *
     * @param className
     * @return
     */
    public static long RealmDataCount(Class className) {
        Realm realm = RealmController.getSaveRealm();
        long count = realm.where(className).count();
        return count;
    }

    /**
     * 删除某个表
     */
    public static void deleteDB(final Class className) {
        Realm realm = RealmController.getSaveRealm();
        realm.executeTransactionAsync(
                new Realm.Transaction() {
                    public void execute(Realm realm) {
                        realm.where(className).findAll().clear();
                    }
                }
        );
    }

    /**
     * 据id来删除指定db下的某条数据
     * @param className
     */
    public static void deleteById(final Class className,final long id) {
        Realm realm = RealmController.getSaveRealm();
        realm.executeTransactionAsync(
                new Realm.Transaction() {
                    public void execute(Realm realm) {
                        realm.where(Book.class).equalTo("id",id).findFirst().deleteFromRealm();
                    }
                }
        );
    }

    /**
     * 获取到指定db下的所有数据
     *
     * @return
     */
    public static List<? extends RealmObject> findAll(Class className) {
        Realm realm = RealmController.getRealm();
        RealmResults objects = realm.where(className).findAllAsync();
        return objects;
    }


    /**
     * 根据id获得该条数据
     */
    public static RealmObject findById(Class className,long id) {
        Realm realm = RealmController.getRealm();
        RealmObject object;
        object = (RealmObject) realm.where(className).equalTo("id", id).findFirst();
        return object;
    }

    /**
     * 用于异步线程的realm 关闭
     *
     * @param realm
     */
    public static void closeReadRealm(Realm realm) {
        if (realm != null && !realm.isClosed() ) {
            realm.close();
        }
    }
}
