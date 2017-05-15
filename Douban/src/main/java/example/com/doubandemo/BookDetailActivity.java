package example.com.doubandemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import example.com.fragment.BookDetailFragment;
import example.com.fragmentActivity.SingleFragmentActivity;

/**
 * Created by Administrator on 2017/4/16 0016.
 */
public class BookDetailActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, Uri webViewUri){
        Intent intent = new Intent(context,BookDetailActivity.class);
        intent.setData(webViewUri);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return BookDetailFragment.newInstance(getIntent().getData());
    }

}
