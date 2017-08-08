package com.example.alipay.myreadingapplication.realm;

import com.example.alipay.myreadingapplication.model.Book;

import io.realm.Realm;

/**
 * Created by Jxq .
 * Description:
 * Date:on 2017/7/21.
 */
public class RealmInitialData implements Realm.Transaction {
    @Override
    public void execute(Realm realm) {
        Book book = new Book();
        book.setId((int) (1 + System.currentTimeMillis()));
        book.setAuthor("Reto Meier");
        book.setTitle("Android 4 Application Development");
        book.setImageUrl("https://api.androidhive.info/images/realm/1.png");
        book.setPages("666页");
        realm.insertOrUpdate(book);

        book = new Book();
        book.setId((int) (2 + System.currentTimeMillis()));
        book.setAuthor("Itzik Ben-Gan");
        book.setTitle("Microsoft SQL Server 2012 T-SQL Fundamentals");
        book.setImageUrl("https://api.androidhive.info/images/realm/2.png");
        book.setPages("111页");
        realm.insertOrUpdate(book);

        book = new Book();
        book.setId((int) (3 + System.currentTimeMillis()));
        book.setAuthor("Magnus Lie Hetland");
        book.setTitle("Beginning Python: From Novice To Professional Paperback");
        book.setImageUrl("https://api.androidhive.info/images/realm/3.png");
        book.setPages("222页");
        realm.insertOrUpdate(book);

        book = new Book();
        book.setId((int) (4 + System.currentTimeMillis()));
        book.setAuthor("Chad Fowler");
        book.setTitle("The Passionate Programmer: Creating a Remarkable Career in Software Development");
        book.setImageUrl("https://api.androidhive.info/images/realm/4.png");
        book.setPages("333页");
        realm.insertOrUpdate(book);

        book = new Book();
        book.setId((int) (5 + System.currentTimeMillis()));
        book.setAuthor("Yashavant Kanetkar");
        book.setTitle("Written Test Questions In C Programming");
        book.setImageUrl("https://api.androidhive.info/images/realm/5.png");
        book.setPages("444页");
        realm.insertOrUpdate(book);
    }
    public int hashCode(){
        return RealmInitialData.class.hashCode();
    }

    public boolean equals(Object obj){
        return obj!=null&&obj instanceof RealmInitialData;
    }
}
