package example.com.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import example.com.bean.Banner;
import example.com.doubandemo.R;

/**
 * Created by Administration on 2017/4/17.
 */
public class BannerDetailFragment extends Fragment {
    private  static final String ARG_BANNER_URL ="banner_detail_url";

    private WebView mWebView;
    private Uri mUri;

    public  static BannerDetailFragment newInstance(Uri mUri) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_BANNER_URL,mUri);

       BannerDetailFragment bannerDetailFragment = new BannerDetailFragment();
        bannerDetailFragment.setArguments(bundle);
        return bannerDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable(ARG_BANNER_URL);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.banner_detail_page, container, false);
        mWebView = (WebView) v.findViewById(R.id.fragment_banner_page_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        mWebView.loadUrl(mUri.toString());
        return v;
    }
}
