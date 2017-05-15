package cniao5.com.admin.custom;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import cniao5.com.cniao5shop.R;


/**
 * Description: 列表adapter 基类
 * Author: Luolin
 * Date: 2016/8/17
 */
public abstract class BaseListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int ITEM_TYPE_ITEM = 0;
    public static final int ITEM_TYPE_HEADER = 1;
    public static final int ITEM_TYPE_FOOTER = 2;
    private View mHeaderView;
    private boolean hasHeadView;
    private boolean isShowLoadMore;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (hasHeadView && viewType == ITEM_TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent);
        }
        if (isShowLoadMore && viewType == ITEM_TYPE_FOOTER) {
            return onCreateLoadMoreHeaderView(parent);
        }
        return onCreateItemView(parent);
    }

    protected abstract int getItemTypeItem(int position);

    protected abstract BaseViewHolder onCreateItemView(ViewGroup parent);

    private BaseViewHolder onCreateLoadMoreHeaderView(ViewGroup parent) {
        View loadMoreView = View.inflate(parent.getContext(), R.layout.widget_item_load_more, null);
        return new LoadMoreViewHolder(loadMoreView);
    }

    private BaseViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(mHeaderView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if((checkIsFootView(position) && isShowLoadMore) || (hasHeadView && checkIsHeaderView(position))){
            setStaggerdSpanSizeLoop(holder);
        }
        holder.onBindViewHolder(position - getHasHeaderViewNum());
    }

    private void setStaggerdSpanSizeLoop(BaseViewHolder holder) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if(params instanceof StaggeredGridLayoutManager.LayoutParams){
            ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        return getDataCount() + (isShowLoadMore ? 1 : 0) + getHasHeaderViewNum();
    }


    protected abstract int getDataCount();

    /**
     * 是否显示加载更多footer布局
     *
     * @param isShow
     */
    public void ShowLoadMoreView(boolean isShow) {
        isShowLoadMore = isShow;
        if (isShow) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (checkIsHeaderView(position)) {
            return ITEM_TYPE_HEADER;
        } else if (checkIsFootView(position)) {
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_ITEM;
    }

    private int getHasHeaderViewNum() {
        return hasHeadView ? 1 : 0;
    }

    public void addHeaderView(View view) {
        if (view != null) {
            this.mHeaderView = view;
            this.hasHeadView = true;
        }
    }

    public void removeHeaderView() {
        if (mHeaderView != null) {
            notifyItemRemoved(0);
            this.mHeaderView = null;
            this.hasHeadView = false;
            notifyDataSetChanged();
        }
    }

    public boolean checkIsHeaderView(int position) {
        return hasHeadView && position == 0;
    }

    public boolean checkIsFootView(int position) {
        return isShowLoadMore && position == getItemCount() - 1;
    }


    class HeaderViewHolder extends BaseViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(null);
        }

        @Override
        protected void onItemClick(View view, int adapterPosition) {

        }

        @Override
        protected void onBindViewHolder(int position) {

        }
    }

    class LoadMoreViewHolder extends BaseViewHolder {
        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,40));
        }

        @Override
        protected void onItemClick(View view, int adapterPosition) {

        }

        @Override
        protected void onBindViewHolder(int position) {

        }
    }
}

