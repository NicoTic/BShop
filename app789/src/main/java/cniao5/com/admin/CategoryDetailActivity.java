package cniao5.com.admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.support.v4.app.Fragment;

import cniao5.com.admin.fragment.CateGoryDetailFragment;


/**
 * Created by Administration on 2017/4/26.
 */
public class CategoryDetailActivity extends SingleFragmentActivity {
    public static final String EXTRA_URL= "uri";

    public static Intent newIntent(Context context, Uri webViewUri){
        Intent intent = new Intent(context,CategoryDetailActivity.class);
        intent.setData(webViewUri);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        return CateGoryDetailFragment.newInstance(getIntent().getData());
    }
}
