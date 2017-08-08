package com.example.alipay.myreadingapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.alipay.myreadingapplication.fragment.BookDetailFragment;

/**
 * Created by Jxq .
 * Description:图书详情Activity
 * Date:on 2017/7/11.
 */
public class BookDetailActivity extends SingleFragmentActivity{
    public static final String EXTRA_BOOK_PAGES= "BOOK_PAGES";
    private static final String EXTRA_BOOK_TITLE = "BOOK_TITLE";
    private static final String EXTRA_BOOK_AUTHOR = "BOOK_Author";
    private static final String EXTRA_BOOK_POSITION = "BOOK_POSITION";
    public static Intent newIntent(Context context, Uri webViewUri, String bookPages, String bookTitle, String bookAuthor, int position){
        Intent intent = new Intent(context,BookDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_BOOK_PAGES,bookPages);
        bundle.putString(EXTRA_BOOK_TITLE,bookTitle);
        bundle.putString(EXTRA_BOOK_AUTHOR,bookAuthor);
        bundle.putInt(EXTRA_BOOK_POSITION,position);
        intent.putExtras(bundle);
        intent.setData(webViewUri);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Bundle bundle = getIntent().getExtras();
        Uri uri = getIntent().getData();
        String bookPages = bundle.getString(EXTRA_BOOK_PAGES);
        String bookTitle = bundle.getString(EXTRA_BOOK_TITLE);
        String bookAuthor = bundle.getString(EXTRA_BOOK_AUTHOR);
        int position = bundle.getInt(EXTRA_BOOK_POSITION);
        return BookDetailFragment.newInstance(uri,bookPages,bookTitle,bookAuthor,position);
    }
}
