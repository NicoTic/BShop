package example.com.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import example.com.bean.FavoriteBean;

/**
 * Created by Administration on 2017/5/6.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
   //Database version
    private static final int DATABASE_VERSION = 1;
    //Database name
    private static final String DATABASE_NAME= "FavoriteManager.db";
    //Favorite table name
    private static final String TABLE_FAVORITE = "favorite";
    //Favorite table columns name
    private static final String COLUMN_FAVORITE_ID = "favorite_id";
    private static final String COLUMN_FAVORITE_IMAGE = "favorite_image";
    private static final String COLUMN_FAVORITE_TITLE = "favorite_title";
    private static final String COLUMN_FAVORITE_CONTENT = "favorite_content";
    //create table sql query
    private String CREATE_FAVORITE_TABLE = "CREATE TABLE "+TABLE_FAVORITE+"("
            + COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_FAVORITE_IMAGE + " TEXT,"
            + COLUMN_FAVORITE_TITLE + " TEXT,"
            + COLUMN_FAVORITE_CONTENT + " TEXT"+")";

    //drop table sql query
    private String DROP_FAVORITE_TABLE = "DROP TABLE　IF EXISTS" + TABLE_FAVORITE;

    public DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

 @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       sqLiteDatabase.execSQL(DROP_FAVORITE_TABLE);
       onCreate(sqLiteDatabase);
    }


 /**
  * 创建用户记录 add user to database
  */
 public void addFavorite(FavoriteBean favoriteBean){
  SQLiteDatabase db = this.getWritableDatabase();

  ContentValues values = new ContentValues();
  values.put(COLUMN_FAVORITE_IMAGE,favoriteBean.getFavoriteImgUrl());
  values.put(COLUMN_FAVORITE_TITLE,favoriteBean.getFavoriteTitle());
  values.put(COLUMN_FAVORITE_CONTENT,favoriteBean.getFavoriteContent());
  //插入和更新数据
  db.insert(TABLE_FAVORITE,null,values);
  db.close();
 }

 /**
  * 取得用户记录 fetch user from database
  */
 public List<FavoriteBean> getAllUser(){
  String[] columns = {
          COLUMN_FAVORITE_ID,
          COLUMN_FAVORITE_IMAGE,
          COLUMN_FAVORITE_TITLE,
          COLUMN_FAVORITE_CONTENT
  };
  //正序排序
  String sortOrder = COLUMN_FAVORITE_TITLE + " ASC";
  List<FavoriteBean> favoriteList = new ArrayList<FavoriteBean>();
  SQLiteDatabase db = this.getReadableDatabase();
  /**
   * 读取数据库
   */
  Cursor cursor = db.query(TABLE_FAVORITE,
          columns,
          null,
          null,
          null,
          null,
          sortOrder);

  if(cursor.moveToFirst()){
   do{
    FavoriteBean favorite = new FavoriteBean();
    favorite.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_ID))));
    favorite.setFavoriteImgUrl(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_IMAGE)));
    favorite.setFavoriteTitle(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_TITLE)));
    favorite.setFavoriteContent(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_CONTENT)));
    favoriteList.add(favorite);
   }while (cursor.moveToNext());
  }
  cursor.close();
  db.close();
  //返回用户列表
  return favoriteList;
 }

 /**
  * 更新用户记录 update user in database by the basis of user id
  */
 public void updateFavorite(FavoriteBean favoriteBean){
  SQLiteDatabase db = this.getWritableDatabase();
  ContentValues contentValues = new ContentValues();
  contentValues.put(COLUMN_FAVORITE_IMAGE,favoriteBean.getFavoriteImgUrl());
  contentValues.put(COLUMN_FAVORITE_TITLE,favoriteBean.getFavoriteTitle());
  contentValues.put(COLUMN_FAVORITE_CONTENT,favoriteBean.getFavoriteContent());

  db.update(TABLE_FAVORITE,contentValues, COLUMN_FAVORITE_ID+" = ?",
          new String[]{String.valueOf(favoriteBean.getId())});
  db.close();
 }

 /**
  * 删除用户记录
  */
 public void deleteFavorite(FavoriteBean favoriteBean){
  SQLiteDatabase db = this.getWritableDatabase();
  db.delete(TABLE_FAVORITE, COLUMN_FAVORITE_ID + " = ?",
          new String[]{String.valueOf(favoriteBean.getId())});
  db.close();
 }

 /**
  * 检查用户是否已经存在数据库
  */
 public boolean checkUser(String title){
  //array of column to fetch
  String[] columns = {
          COLUMN_FAVORITE_ID
  };
  SQLiteDatabase db =this.getReadableDatabase();
  //selection criteria
  String selection = COLUMN_FAVORITE_TITLE+" = ?";
  //selection argument
  String[] selectionArgs = {title};

  Cursor cursor = db.query(TABLE_FAVORITE,
          columns,
          selection,
          selectionArgs,
          null,
          null,
          null);
  int cursorCount = cursor.getCount();
  cursor.close();
  db.close();

  if(cursorCount>0){
   return true;
  }
  return false;
 }

 public boolean checkUser(String phoneNumber,String passWord){
  //array of column to fetch
  String[] columns = {
          COLUMN_FAVORITE_ID
  };
  SQLiteDatabase db =this.getReadableDatabase();
  //selection criteria
  String selection = COLUMN_FAVORITE_TITLE+" = ?" +
          " AND " + COLUMN_FAVORITE_CONTENT + " = ?";
  //selection argument
  String[] selectionArgs = {phoneNumber,passWord};

  Cursor cursor = db.query(TABLE_FAVORITE,
          columns,
          selection,
          selectionArgs,
          null,
          null,
          null);
  int cursorCount = cursor.getCount();
  cursor.close();
  db.close();

  if(cursorCount>0){
   return true;
  }
  return false;
 }

 /**
  * 获得单个User对象
  */
 public FavoriteBean getFavorite(String title,String content){
  String[] columns = {
         COLUMN_FAVORITE_ID,
         COLUMN_FAVORITE_IMAGE,
         COLUMN_FAVORITE_TITLE,
         COLUMN_FAVORITE_CONTENT
  };
  SQLiteDatabase db = this.getReadableDatabase();

  String selection = COLUMN_FAVORITE_TITLE+" = ?" +
          " AND " + COLUMN_FAVORITE_CONTENT + " = ?";

  String[] selectionArgs = {title,content};

  Cursor cursor = db.query(TABLE_FAVORITE,
          columns,
          selection,
          selectionArgs,
          null,
          null,
          null);
  if(cursor.moveToFirst()){
   FavoriteBean favoriteBean = new FavoriteBean();
   favoriteBean.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_ID))));
   favoriteBean.setFavoriteImgUrl(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_IMAGE)));
   favoriteBean.setFavoriteTitle(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_TITLE)));
   favoriteBean.setFavoriteContent(cursor.getString(cursor.getColumnIndex(COLUMN_FAVORITE_CONTENT)));

   cursor.close();
   db.close();
   return favoriteBean;
  }

  cursor.close();
  db.close();
  return null;
 }
}
