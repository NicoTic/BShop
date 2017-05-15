package example.com.fragment;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.com.bean.FavoriteBean;
import example.com.bean.TodayArticleItem;
import example.com.custom.DetailToolbar;
import example.com.doubandemo.CommentActivity;
import example.com.doubandemo.R;
import example.com.utils.ACache;
import example.com.utils.DatabaseHelper;
import example.com.utils.GsonUtil;

/**
 * Created by Administration on 2017/4/13.
 */
public class DiaryFragment extends Fragment {
    private static final String ARG_URL= "diary_page_uel";
    public static final String EXTRA_IMAGE_URL= "DIARY_IMAGE";
    private static final String EXTRA_DIARY_TITLE = "DIARY_TITLE";
    private static final String EXTRA_DIARY_CONTENT = "DIARY_CONTENT";

    private Uri mUri;
    private WebView mWebView;

    private ProgressBar mProgressBar;
    private DetailToolbar detailToolbar;

    //用于缓存
    public ACache mCache;
    public static final String FAVORITE = "MY_FAVORITE";
    private String imageUrl;
    private String diaryTitle;
    private String diaryContent;
    private List<FavoriteBean> favoriteBeanList = new ArrayList<>();

    //用于数据库保存
    private DatabaseHelper databaseHelper;

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
        mWebView.goBack();
    }

    public  static DiaryFragment newInstance(Uri mUri,String imageUrl,String diaryTitle,String diaryContent) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_URL,mUri);
        bundle.putString(EXTRA_IMAGE_URL,imageUrl);
        bundle.putString(EXTRA_DIARY_TITLE,diaryTitle);
        bundle.putString(EXTRA_DIARY_CONTENT,diaryContent);

        DiaryFragment diaryFragment = new DiaryFragment();
        diaryFragment.setArguments(bundle);
        return diaryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mUri = getArguments().getParcelable(ARG_URL);
        Bundle bundle = getArguments();
        mUri = bundle.getParcelable(ARG_URL);
        imageUrl = bundle.getString(EXTRA_IMAGE_URL);
        diaryTitle = bundle.getString(EXTRA_DIARY_TITLE);
        diaryContent = bundle.getString(EXTRA_DIARY_CONTENT);

        //缓存初始化
        mCache = ACache.get(getContext());
        databaseHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.today_diary_detail_page,container,false);
        mWebView = (WebView) v.findViewById(R.id.today_diary_page_web_view);

        mProgressBar = (ProgressBar) v.findViewById(R.id.diary_detail_page_progress_bar);
        mProgressBar.setMax(100);

        detailToolbar = (DetailToolbar) v.findViewById(R.id.diary_detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(detailToolbar);
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().finish();
                // TODO Auto-generated method stub
                if (mWebView.canGoBack()) {
                    mWebView.goBack();

                } else {
                   getActivity().finish();
                }
            }
        });

        detailToolbar.setShareButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"hehe",Toast.LENGTH_SHORT).show();
            }
        });

        detailToolbar.setFavoriteButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"收藏成功！",Toast.LENGTH_SHORT).show();

                saveDataToSQLite();

//                //Person person = new Person("无缘", 25, new Date(), true);
//
////                favoriteBeanList.add(favoriteBean);
////
////                String personArray = GsonUtil.getGson().toJson(persons);
//                mCache.put(FAVORITE,favoriteBean);

            }
        });

        detailToolbar.setCommentButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent();
                commentIntent.setClass(getContext(), CommentActivity.class);
                startActivity(commentIntent);
                Toast.makeText(getContext(),"hehe",Toast.LENGTH_SHORT).show();
            }
        });

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setSupportZoom(true);

        mWebView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
            public void onReceivedTitle(WebView webView, String title) {
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        mWebView.loadUrl(mUri.toString());

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
        return v;
    }

    private void saveDataToSQLite() {
        FavoriteBean favoriteBean = new FavoriteBean();
        favoriteBean.setFavoriteImgUrl(imageUrl);
        favoriteBean.setFavoriteTitle(diaryTitle);
        favoriteBean.setFavoriteContent(diaryContent);
        if(!databaseHelper.checkUser(diaryTitle)){
            databaseHelper.addFavorite(favoriteBean);
        }
    }

}
