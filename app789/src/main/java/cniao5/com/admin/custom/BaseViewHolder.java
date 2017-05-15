package cniao5.com.admin.custom;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Description: Viewholder 基类
 * Author: Luolin
 * Date: 2016/8/17
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private View.OnClickListener  mClickCallback = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onItemClick(view, getAdapterPosition());
        }
    };

    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(mClickCallback);
    }

    /**
     * item点击回调
     * @param view
     * @param adapterPosition
     * 提示: adapterPosition在增加了头布局的情况position显示不正确。
     * 解决方法:　
     * ①　在创建viewholder的页面 用adapter的检测是否有头部局的方法去判断，有的话adapterPosition-1,没的话则正常，
     * ②  在此BaseViewHolder类中传入adapter，BaseViewHolder通过拿到adapter自己去判断。
     */
    protected abstract void onItemClick(View view, int adapterPosition);
    protected abstract void onBindViewHolder(int position);

    /**
     * 重置点击回调 解决有时候itemview里有动态view新增进来的情况
     */
    protected void resetClickCallback(){
        itemView.setOnClickListener(mClickCallback);
    }
}
