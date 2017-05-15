package cniao5.com.admin.fragment;

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

import cniao5.com.admin.widget.CnToolbar;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/4/26.
 */
public class CateGoryDetailFragment extends Fragment {
    private static final String ARG_URL= "diary_page_uel";

    private Uri mUri;
    private WebView mWebView;

    private ProgressBar mProgressBar;
    private CnToolbar cnToolbar;

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

    public  static CateGoryDetailFragment newInstance(Uri mUri) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_URL,mUri);

        CateGoryDetailFragment cateFragment = new CateGoryDetailFragment();
        cateFragment.setArguments(bundle);
        return cateFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable(ARG_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cate_gory_detail_layout,container,false);
        mWebView = (WebView) v.findViewById(R.id.cate_gory_page_web_view);

        mProgressBar = (ProgressBar) v.findViewById(R.id.cate_detail_page_progress_bar);
        mProgressBar.setMax(100);

        cnToolbar = (CnToolbar) v.findViewById(R.id.cate_gory_detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(cnToolbar);
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

        cnToolbar.setTitle("商品详情");

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

}
