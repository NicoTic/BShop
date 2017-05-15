package cniao5.com.admin.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cniao5.com.admin.bean.User;


/**
 * Created by Administration on 2017/4/26.
 */
public class InfoBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "infoBase.db";

    private String DROP_USER_TABLE = "DROP TABLE IF EXIST"+ InfoDbScheme.InfoTable.NAME;
    public InfoBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + InfoDbScheme.InfoTable.NAME + "(" +
                InfoDbScheme.InfoTable.Col.USER_ID+ " integer primary key autoincrement," +
                InfoDbScheme.InfoTable.Col.USER_NAME + " Text," +
                InfoDbScheme.InfoTable.Col.PHONE_NUMBER + " Text," +
                InfoDbScheme.InfoTable.Col.PASS_WORD + " Text," +
                InfoDbScheme.InfoTable.Col.USER_THUMBNAIL + " Text" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    /**
     * 创建用户记录 add user to database
     */
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InfoDbScheme.InfoTable.Col.USER_NAME,user.getName());
        values.put(InfoDbScheme.InfoTable.Col.PHONE_NUMBER,user.getPhoneNumber());
        values.put(InfoDbScheme.InfoTable.Col.PASS_WORD,user.getPassWord());
        values.put(InfoDbScheme.InfoTable.Col.USER_THUMBNAIL,user.getHeadImg());
        //插入和更新数据
        db.insert(InfoDbScheme.InfoTable.NAME,null,values);
        db.close();
    }

    /**
     * 取得用户记录 fetch user from database
     */
    public List<User> getAllUser(){
        String[] columns = {
                InfoDbScheme.InfoTable.Col.USER_ID,
                InfoDbScheme.InfoTable.Col.USER_NAME,
                InfoDbScheme.InfoTable.Col.PHONE_NUMBER,
                InfoDbScheme.InfoTable.Col.PASS_WORD,
                InfoDbScheme.InfoTable.Col.USER_THUMBNAIL
        };
        //正序排序
        String sortOrder = InfoDbScheme.InfoTable.Col.USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        /**
         * 读取数据库
         */
        Cursor cursor = db.query(InfoDbScheme.InfoTable.NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(InfoDbScheme.InfoTable.Col.USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(InfoDbScheme.InfoTable.Col.USER_NAME)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(InfoDbScheme.InfoTable.Col.PHONE_NUMBER)));
                user.setPassWord(cursor.getString(cursor.getColumnIndex(InfoDbScheme.InfoTable.Col.PASS_WORD)));
                user.setHeadImg(cursor.getString(cursor.getColumnIndex(InfoDbScheme.InfoTable.Col.USER_THUMBNAIL)));
                userList.add(user);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        //返回用户列表
        return userList;
    }

    /**
     * 更新用户记录 update user in database by the basis of user id
     */
    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(InfoDbScheme.InfoTable.Col.USER_NAME,user.getName());
        contentValues.put(InfoDbScheme.InfoTable.Col.PHONE_NUMBER,user.getPhoneNumber());
        contentValues.put(InfoDbScheme.InfoTable.Col.PASS_WORD,user.getPassWord());
        contentValues.put(InfoDbScheme.InfoTable.Col.USER_THUMBNAIL,user.getHeadImg());

        db.update(InfoDbScheme.InfoTable.NAME,contentValues, InfoDbScheme.InfoTable.Col.USER_ID+" = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * 删除用户记录
     */
    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(InfoDbScheme.InfoTable.NAME, InfoDbScheme.InfoTable.Col.USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * 检查用户是否已经存在数据库
     */
    public boolean checkUser(String phoneNumber){
        //array of column to fetch
        String[] columns = {
                InfoDbScheme.InfoTable.Col.USER_ID
        };
        SQLiteDatabase db =this.getReadableDatabase();
        //selection criteria
        String selection = InfoDbScheme.InfoTable.Col.PHONE_NUMBER+" = ?";
        //selection argument
        String[] selectionArgs = {phoneNumber};

        Cursor cursor = db.query(InfoDbScheme.InfoTable.NAME,
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
                InfoDbScheme.InfoTable.Col.USER_ID
        };
        SQLiteDatabase db =this.getReadableDatabase();
        //selection criteria
        String selection = InfoDbScheme.InfoTable.Col.PHONE_NUMBER+" = ?" +
                " AND " + InfoDbScheme.InfoTable.Col.PASS_WORD + " = ?";
        //selection argument
        String[] selectionArgs = {phoneNumber,passWord};

        Cursor cursor = db.query(InfoDbScheme.InfoTable.NAME,
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
    public User getUser(String phone,String password){
        String[] columns = {
                InfoDbScheme.InfoTable.Col.USER_ID,
                InfoDbScheme.InfoTable.Col.USER_NAME,
                InfoDbScheme.InfoTable.Col.PHONE_NUMBER,
                InfoDbScheme.InfoTable.Col.PASS_WORD,
                InfoDbScheme.InfoTable.Col.USER_THUMBNAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = InfoDbScheme.InfoTable.Col.PHONE_NUMBER+" = ?" +
                " AND " + InfoDbScheme.InfoTable.Col.PASS_WORD + " = ?";

        String[] selectionArgs = {phone,password};

        Cursor cursor = db.query(InfoDbScheme.InfoTable.NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if(cursor.moveToFirst()){
            User user = new User();
            user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(InfoDbScheme.InfoTable.Col.USER_ID))));
            user.setName(cursor.getString(cursor.getColumnIndex(InfoDbScheme.InfoTable.Col.USER_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(InfoDbScheme.InfoTable.Col.PHONE_NUMBER)));
            user.setPassWord(cursor.getString(cursor.getColumnIndex(InfoDbScheme.InfoTable.Col.PASS_WORD)));
            user.setHeadImg(cursor.getString(cursor.getColumnIndex(InfoDbScheme.InfoTable.Col.USER_THUMBNAIL)));
            cursor.close();
            db.close();
            return user;
        }

        cursor.close();
        db.close();
        return null;
    }
}
