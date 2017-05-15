package example.com.doubandemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;

import example.com.fragment.DiaryFragment;
import example.com.fragmentActivity.SingleFragmentActivity;

/**
 * Created by Administration on 2017/4/13.
 */
public class DiaryDetailActivity extends SingleFragmentActivity {
    public static final String EXTRA_URL= "uri";
    public static final String EXTRA_IMAGE_URL= "DIARY_IMAGE";
    private static final String EXTRA_DIARY_TITLE = "DIARY_TITLE";
    private static final String EXTRA_DIARY_CONTENT = "DIARY_CONTENT";

    public static Intent newIntent(Context context,Uri webViewUri,String diaryImage,String diaryTitle,String diaryContent){
        Intent intent = new Intent(context,DiaryDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_IMAGE_URL,diaryImage);
        bundle.putString(EXTRA_DIARY_TITLE,diaryTitle);
        bundle.putString(EXTRA_DIARY_CONTENT,diaryContent);
        intent.putExtras(bundle);
        intent.setData(webViewUri);

        return intent;
    }
    @Override
    protected Fragment createFragment() {
        Bundle bundle = getIntent().getExtras();
        Uri uri = getIntent().getData();
        String imageUrl = bundle.getString(EXTRA_IMAGE_URL);
        String diaryTitle = bundle.getString(EXTRA_DIARY_TITLE);
        String diaryContent = bundle.getString(EXTRA_DIARY_CONTENT);
        return DiaryFragment.newInstance(uri,imageUrl,diaryTitle,diaryContent);
    }

}
