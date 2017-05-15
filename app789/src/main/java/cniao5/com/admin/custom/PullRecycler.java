package cniao5.com.admin.custom;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import cniao5.com.cniao5shop.R;


/**
 * Description: 可下拉的Recycler View
 *
 */
public class PullRecycler extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private BaseListAdapter mAdapter;

    public static final int ACTION_IDLE = 0; // 空闲状态
    public static final int ACTION_PULL = 1; // 下拉刷新
    public static final int ACTION_LOAD_MORE = 2; //加载更多
    private int mCurrentActionState = ACTION_IDLE;

    private boolean mLoadMoreEnabled = true;
    private boolean mPullRefreshEnabled = true;

    private OnRecyclerRefreshListener mListener;
    private RecyclerView.LayoutManager mLayoutManager;

    public PullRecycler(Context context) {
        super(context);
        initView();
    }

    public PullRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PullRecycler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.widget_pull_recycler, null);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pull_recycler_swipe_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.pull_recycler_recyclerview);
        addView(view);
        mSwipeRefresh.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 检测是否达到加载更多的条件
                if (dy > 0 && mLoadMoreEnabled && checkIfLoadMore()) {
                    mCurrentActionState = ACTION_LOAD_MORE;
                    mSwipeRefresh.setEnabled(false);
                    mAdapter.ShowLoadMoreView(true);
                    mListener.onRefresh(ACTION_LOAD_MORE);
                }
            }
        });
    }

    private boolean checkIfLoadMore() {
        int lastVisibleItemPosition = 0;
        if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();//得到可见视图项最底层id
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] positions = null;
            positions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(positions);
            lastVisibleItemPosition = positions[0];
        }
        return mCurrentActionState == ACTION_IDLE && mLayoutManager.getItemCount() - lastVisibleItemPosition < 2;
    }


    @Override
    public void onRefresh() {
        mCurrentActionState = ACTION_PULL;
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
                if (mListener != null){
                    mListener.onRefresh(ACTION_PULL);
                }
            }
        });
     }

    public void resetRefresh() {
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    public void onRefreshCompleted() {
        if (mCurrentActionState == ACTION_PULL|| mCurrentActionState == ACTION_IDLE) {
            mSwipeRefresh.setRefreshing(false);
        } else if (mCurrentActionState == ACTION_LOAD_MORE) {
            mAdapter.ShowLoadMoreView(false);
            mSwipeRefresh.setEnabled(!mPullRefreshEnabled ? false : true);
        }
        mCurrentActionState = ACTION_IDLE;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        mRecyclerView.setLayoutManager(layoutManager);
    }


    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    public void setAdapter(BaseListAdapter adapter) {
        if (adapter != null) {
            mAdapter = adapter;
            mRecyclerView.setAdapter(adapter);
        }
    }

    public void setRecyclerListener(OnRecyclerRefreshListener listener) {
        if (listener != null) {
            this.mListener = listener;
        }
    }

    public interface OnRecyclerRefreshListener {
        void onRefresh(int action);
    }

    public void addHeaderView(View view) {
        if (view != null) {
            mAdapter.addHeaderView(view);
        }
    }

    public void removeHeaderView() {
        mAdapter.removeHeaderView();
    }

    public void enableLoadMore(boolean enable) {
        mLoadMoreEnabled = enable;
        if(!enable){
            mAdapter.ShowLoadMoreView(false);
        }
    }

    public void enablePullToRefresh(boolean enable) {
        mPullRefreshEnabled = enable;
        mSwipeRefresh.setEnabled(enable);
    }

    public void setSwipeRefreshing(final boolean status) {
        mSwipeRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        mSwipeRefresh.setRefreshing(status);
    }
}
