package example.com.http;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Administration on 2017/4/12.
 */
public class MyWorkerThread extends HandlerThread {
    private static final String TAG = MyWorkerThread.class.getSimpleName();

    private boolean mHasQuit = false;

    private Handler mWokerHandler;

    public MyWorkerThread() {
        super(TAG);
    }

    public boolean quit(){
        mHasQuit = true;
        return super.quit();
    }

    public void queueTask(String url, int side, ImageView imageView){
        Log.i(TAG, url + " added to the queue");
    }
}
