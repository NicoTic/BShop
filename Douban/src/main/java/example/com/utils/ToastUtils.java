package example.com.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import example.com.doubandemo.R;

/**
 * Created by Administration on 2017/4/28.
 */
public class ToastUtils {
    public static boolean isShow = true;
    private static Toast toast;

    private static Handler sMainThreadHandler;

    public static Handler getMainThreadHandler() {
        if (sMainThreadHandler == null) {
            synchronized (ToastUtils.class){
                if(sMainThreadHandler==null){
                    sMainThreadHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return sMainThreadHandler;
    }

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(final AppCompatActivity activity, final CharSequence message) {
        getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                if(toast == null){
                    toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
                }else{
                    toast.setText(message);
                }
                toast.show();
            }
        });

    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(AppCompatActivity activity,int message) {
        //if (isShow)
        showShort(activity,activity.getResources().getString(message));
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(final AppCompatActivity activity, final CharSequence message) {
        getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                if(toast == null){
                    toast = Toast.makeText(activity, message, Toast.LENGTH_LONG);
                }else{
                    toast.setText(message);
                }
                toast.show();
            }
        });

    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(AppCompatActivity activity,int message) {
        showLong(activity,activity.getResources().getString(message));
    }


    /**
     * 服务器连接失败提醒
     */
    public static void showNetErrorToast(AppCompatActivity activity) {
        ToastUtils.showShort(activity,"网络连接失败");
    }

    /**
     * 数据为空的提示
     */
    public static void showDataEmpty(AppCompatActivity activity) {
        showShort(activity,"数据为空！");
    }

    /**
     * 服务器没有更多数据
     */
    public static void showNoMoreListData(AppCompatActivity activity) {
        showShort(activity,"没有更多数据了");
    }
}
