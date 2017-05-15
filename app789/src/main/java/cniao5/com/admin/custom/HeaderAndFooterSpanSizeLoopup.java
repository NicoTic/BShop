package cniao5.com.admin.custom;

import android.support.v7.widget.GridLayoutManager;

/**
 * Description:
 * Author: Luolin
 * Date: 2016/8/17
 */
public class HeaderAndFooterSpanSizeLoopup extends GridLayoutManager.SpanSizeLookup{
    private BaseListAdapter mAdapter;
    private int spanCount;

    public HeaderAndFooterSpanSizeLoopup(BaseListAdapter mAdapter, int spanCount) {
        this.mAdapter = mAdapter;
        this.spanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {
        if(mAdapter.checkIsHeaderView(position) || mAdapter.checkIsFootView(position)){
            return spanCount;
        }
        return 1;
    }
}
