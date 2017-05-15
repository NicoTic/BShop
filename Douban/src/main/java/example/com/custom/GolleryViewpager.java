package example.com.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Administration on 2017/4/21.
 */
public class GolleryViewpager extends ViewPager{


    public GolleryViewpager(Context context) {
        super(context);
    }

    public GolleryViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_UP){
            View view  = viewOfClickOnScreen(ev);
            if(view!=null){
                setCurrentItem(indexOfChild(view));
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     *  v.getLocationOnScreen(location);得到当前View在屏幕内的绝对坐标
     * 从屏幕左上角为原点坐标
     * minX:
     * @param ev
     * @return
     */
    private View viewOfClickOnScreen(MotionEvent ev) {
        int childCount = getChildCount();
        int[] location = new int[2];
        for(int i = 0;i<childCount;i++){
            View v = getChildAt(i);
            v.getLocationOnScreen(location);
            int minX = location[0];
            int minY = getTop();

            int maxX = location[0]+v.getWidth();
            int maxY = getBottom();

            float x = ev.getX();
            float y = ev.getY();

            if((x>minX&&x<maxX)&&(y>minY&&y<maxY)){
                return v;
            }

        }
        return null;
    }

}
