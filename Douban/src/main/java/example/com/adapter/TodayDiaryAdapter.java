package example.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import example.com.bean.TodayArticleItem;
import example.com.doubandemo.R;


/**
 * Created by Administration on 2017/4/12.
 */
public class TodayDiaryAdapter extends RecyclerView.Adapter<TodayDiaryAdapter.TodayDiaryViewHolder>{
    private List<TodayArticleItem> todayDiaries;//数据源
    private String TAG = "FragmentOne";
    private Context mContext;

    public static final int TYPE_HEADER = 0;//有头布局的类型
    public static final int TYPE_NORMAL = 1;//正常的类型

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    private View headerView;//头布局

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return headerView;
    }

    public void addDatas(ArrayList<TodayArticleItem> datas) {
        todayDiaries.addAll(datas);
        notifyDataSetChanged();
    }

    public TodayDiaryAdapter(Context context, List<TodayArticleItem> todayDiary) {
        this.mContext = context;
        this.todayDiaries = todayDiary;
    }

    @Override
    public int getItemViewType(int position) {
        if(headerView==null){
            return TYPE_NORMAL;
        }
        if(position==0){
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public TodayDiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(headerView!=null&&viewType==TYPE_HEADER){
            return new TodayDiaryViewHolder(headerView);
        }
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.today_diary_item,parent,false);
        Log.i("xixi","xixi");
        return new TodayDiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodayDiaryViewHolder holder, final int position) {
        if(getItemViewType(position)==TYPE_HEADER)
            return;
        TodayArticleItem todayArticleItem = todayDiaries.get(position);
        if(holder instanceof TodayDiaryViewHolder){
            holder.bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        return headerView==null?todayDiaries.size():todayDiaries.size()-1;
    }

    public class TodayDiaryViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTextView;
        private TextView mContentTextView;
        private ImageView mImageView;

        public TodayDiaryViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.today_diary_title);
            mContentTextView = (TextView) itemView.findViewById(R.id.today_diary_commary);
            mImageView = (ImageView) itemView.findViewById(R.id.today_diary_image);
        }

        //为UI控件填充数据
        private void bindView(final int position){
            final TodayArticleItem todayArticleItem = todayDiaries.get(position);
            mTitleTextView.setText(todayArticleItem.getArticleTitle());
            mContentTextView.setText(todayArticleItem.getArticleContent());
            Glide.with(mContext).load(todayArticleItem.getArticleImageUrl()).into(mImageView);
            itemView.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(position,todayArticleItem);
                    }
                }
            });
        }
    }

   public interface OnItemClickListener {
        void onItemClick(int position, TodayArticleItem data);
    }
}
