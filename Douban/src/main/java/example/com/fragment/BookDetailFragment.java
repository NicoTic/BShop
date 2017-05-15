package example.com.fragment;

import android.net.Uri;
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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import example.com.custom.DetailToolbar;
import example.com.doubandemo.R;

/**
 * Created by Administration on 2017/4/17.
 * description:Book详情界面
 */
public class BookDetailFragment extends Fragment {
    private  static final String ARG_BOOK_URL ="book_detail_url";

    private Uri mUri;

    private WebView mWebView;
    private ProgressBar progressBar;
    private DetailToolbar detailToolbar;

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
        mWebView.canGoBack();
    }

    public  static BookDetailFragment newInstance(Uri mUri) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_BOOK_URL,mUri);

        BookDetailFragment bookDetailFragment = new BookDetailFragment();
        bookDetailFragment.setArguments(bundle);
        return bookDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUri = getArguments().getParcelable(ARG_BOOK_URL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_detail_page,container,false);
        progressBar = (ProgressBar) view.findViewById(R.id.book_detail_page_progress_bar);
        progressBar.setMax(100);

        detailToolbar = (DetailToolbar) view.findViewById(R.id.book_detail_toolbar);
        initToolbar();

        mWebView = (WebView) view.findViewById(R.id.fragment_book_page_web_view);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setSupportZoom(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
            public void onReceivedTitle(WebView webView, String title) {
//                AppCompatActivity activity = (AppCompatActivity) getActivity();
//                activity.getSupportActionBar().setSubtitle(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){
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
        return view;
    }

    private void initToolbar() {
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

    }
}
