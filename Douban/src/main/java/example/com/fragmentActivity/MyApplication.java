package example.com.fragmentActivity;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by Administration on 2017/5/8.
 */
public class MyApplication extends Application {
    public static final String TAG = "MyApplication";
    private boolean isNightModeEnabled = false;
    @Override
    public void onCreate() {
        super.onCreate();
        // 默认设置为日间模式
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.isNightModeEnabled = sharedPreferences.getBoolean("NIGHT_MODE",false);
    }

    public boolean isNightModeEnabled(){
        return isNightModeEnabled;
    }

    public void setNightModeEnabled(boolean nightModeEnabled) {
        this.isNightModeEnabled = nightModeEnabled;
    }
}
