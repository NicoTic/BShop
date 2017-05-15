package example.com.doubandemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import example.com.fragment.BannerDetailFragment;
import example.com.fragmentActivity.SingleFragmentActivity;

/**
 * Created by Administration on 2017/4/17.
 */
public class BannerDetailActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, Uri webViewUri){
        Intent intent = new Intent(context,BannerDetailActivity.class);
        intent.setData(webViewUri);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        return BannerDetailFragment.newInstance(getIntent().getData());
    }
}
