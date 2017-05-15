package example.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import example.com.bean.RecentMovieItem;
import example.com.bean.TodayArticleItem;
import example.com.doubandemo.R;

/**
 * Created by Administration on 2017/4/22.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {
    private List<RecentMovieItem> recentMovieItemList;//数据源
    private String TAG = "FragmentThree";
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

    public void addDatas(ArrayList<RecentMovieItem> datas) {
        recentMovieItemList.addAll(datas);
        notifyDataSetChanged();
    }

    public MovieRecyclerAdapter(Context mContext, List<RecentMovieItem> recentMovieItemList) {
        this.mContext = mContext;
        this.recentMovieItemList = recentMovieItemList;
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
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(headerView!=null&&viewType==TYPE_HEADER){
            return new MovieViewHolder(headerView);
        }
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.hot_movie_item_layout,parent,false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_HEADER)
            return;
        RecentMovieItem recentMovieItem = recentMovieItemList.get(position);
        if(holder instanceof MovieViewHolder){
            holder.bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        return recentMovieItemList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        private TextView movieTitle,movieDirector,movieActors,movieType,movieCountry,howMuchSaw;
        private ImageView movieImg;
        private Button buyTicketBtn;


        public MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_ctitle);
            movieDirector = (TextView) itemView.findViewById(R.id.movie_director);
            movieActors = (TextView) itemView.findViewById(R.id.movie_actor);
            movieType = (TextView) itemView.findViewById(R.id.movie_type);
            movieCountry = (TextView) itemView.findViewById(R.id.movie_country);
            howMuchSaw = (TextView) itemView.findViewById(R.id.how_much_saw);
            movieImg = (ImageView) itemView.findViewById(R.id.movie_thumbnail);
            buyTicketBtn = (Button) itemView.findViewById(R.id.buy_ticket);
        }


        //为UI控件填充数据
        private void bindView(final int position) {
            final RecentMovieItem recentMovieItem = recentMovieItemList.get(position);
            movieTitle.setText(recentMovieItem.getMovieTitle());
            movieDirector.setText(recentMovieItem.getMovieDirector());
            movieActors.setText(recentMovieItem.getMovieActors());
            movieType.setText(recentMovieItem.getMovieType());
            movieCountry.setText(recentMovieItem.getMovieCountry());
            howMuchSaw.setText(recentMovieItem.getHowMuchSaw());
            Glide.with(mContext)
                    .load(recentMovieItem.getMovieImageUrl())
                    .placeholder(R.drawable.longmao)
                    .centerCrop()
                    .into(movieImg);
            itemView.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(position, recentMovieItem);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, RecentMovieItem data);
    }
}
