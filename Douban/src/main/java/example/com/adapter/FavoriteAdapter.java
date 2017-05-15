package example.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import example.com.bean.FavoriteBean;
import example.com.doubandemo.R;

/**
 * Created by Administration on 2017/5/6.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<FavoriteBean> favoriteBeanList;
    private Context mContext;

    public FavoriteAdapter(Context context, List<FavoriteBean> favoriteBeanList) {
        this.mContext = context;
        this.favoriteBeanList = favoriteBeanList;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_favorite_item_layout,parent,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        FavoriteBean favoriteBean = favoriteBeanList.get(position);
        Glide.with(mContext)
                .load(favoriteBean.getFavoriteImgUrl())
                .dontAnimate()
                .placeholder(R.drawable.longmao)
                .into(holder.dirayImageView);
        holder.diaryTitle.setText(favoriteBean.getFavoriteTitle());
        holder.diaryContent.setText(favoriteBean.getFavoriteContent());
    }

    @Override
    public int getItemCount() {
        return favoriteBeanList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        private ImageView dirayImageView;
        private TextView diaryTitle;
        private TextView diaryContent;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            dirayImageView = (ImageView) itemView.findViewById(R.id.favorite_diary_image);
            diaryTitle = (TextView) itemView.findViewById(R.id.favorite_diary_title);
            diaryContent = (TextView) itemView.findViewById(R.id.favorite_diary_content);
        }
    }
}
