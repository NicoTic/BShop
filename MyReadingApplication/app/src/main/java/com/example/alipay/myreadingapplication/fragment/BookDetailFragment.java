package com.example.alipay.myreadingapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alipay.myreadingapplication.R;
import com.example.alipay.myreadingapplication.model.Book;
import com.example.alipay.myreadingapplication.realm.RealmController;


/**
 * Created by Jxq .
 * Description:Book详情界面
 * Date:on 2017/7/11.
 */
public class BookDetailFragment extends Fragment {
    private static final String TAG = "BookDetailFragment";

    private static final String ARG_BOOK_URL ="book_detail_url";
    private static final String EXTRA_BOOK_PAGES= "BOOK_PAGE";
    private static final String EXTRA_BOOK_TITLE = "BOOK_TITLE";
    private static final String EXTRA_BOOK_AUTHOR = "BOOK_AUTHOR";
    private static final String EXTRA_BOOK_POSITION = "BOOK_POSITION";

    private Uri mUri;
    private String bookPages;
    private String bookTitle;
    private String bookAuthor;
    private int position;
    private WebView mWebView;
    private ProgressBar progressBar;
    private Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };

    private void webViewGoBack() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            getActivity().finish();
        }
    }

    public  static BookDetailFragment newInstance(Uri mUri,String bookPages,String bookTitle,String bookAuthor,int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_BOOK_URL,mUri);
        bundle.putString(EXTRA_BOOK_PAGES,bookPages);
        bundle.putString(EXTRA_BOOK_TITLE,bookTitle);
        bundle.putString(EXTRA_BOOK_AUTHOR,bookAuthor);
        bundle.putInt(EXTRA_BOOK_POSITION,position);

        BookDetailFragment bookDetailFragment = new BookDetailFragment();
        bookDetailFragment.setArguments(bundle);
        return bookDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mUri = bundle.getParcelable(ARG_BOOK_URL);
        bookPages = bundle.getString(EXTRA_BOOK_PAGES);
        bookTitle = bundle.getString(EXTRA_BOOK_TITLE);
        bookAuthor = bundle.getString(EXTRA_BOOK_AUTHOR);
        position = bundle.getInt(EXTRA_BOOK_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_detail_layout,container,false);
        progressBar = (ProgressBar) view.findViewById(R.id.book_detail_page_progress_bar);
        progressBar.setMax(100);
        mWebView = (WebView) view.findViewById(R.id.fragment_book_page_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setSupportZoom(true);

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });


//        Log.d("uri",mUri.toString());
        mWebView.loadUrl(mUri.toString());
        mWebView.addJavascriptInterface(new WebAppInterface(getContext()),"MYOBJECT");
        mWebView.setWebViewClient(new MyWebViewClient());
        //控制网页返回
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK
                        && keyEvent.getAction() == MotionEvent.ACTION_UP
                        && mWebView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    private class MyWebViewClient extends WebViewClient {


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            Uri parse = Uri.parse(url);
            String scheme = parse.getScheme();
            if(scheme.equals("http")||scheme.equals("https")){
                return false;
            }else{
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                boolean isInstall = getActivity().getPackageManager().queryIntentActivities(in, PackageManager.MATCH_DEFAULT_ONLY).size()>0;
                Log.d(TAG,"是否安装要跳转的app:"+isInstall);
                if(isInstall)
                {
                    startActivity(in);
                    getActivity().finish();
                }
                return true;
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            StringBuilder sb = new StringBuilder();
            sb.append("document.getElementsByTagName('form')[0].submit = function(){");
            sb.append("window.MYOBJECT.processHTML();");
            sb.append("return true;");
            sb.append("};");
            mWebView.loadUrl("javascript:" + sb.toString());
        }
    }



    private class WebAppInterface {
        private Context mContext;
        public WebAppInterface(Context context) {
            this.mContext = context;
        }
        @JavascriptInterface
        public void processHTML(){
            Toast.makeText(mContext,"添加成功！",Toast.LENGTH_SHORT).show();
            final Book book = new Book();
            book.setTitle(bookTitle);
            book.setAuthor(bookAuthor);
            book.setImageUrl("https://img3.doubanio.com/lpic/s1800355.jpg");
            book.setPages(bookPages);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RealmController.add(book);
                }
            });

        }
    }
}
