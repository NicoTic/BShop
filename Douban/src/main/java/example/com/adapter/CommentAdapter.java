package example.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import example.com.bean.BookListBean;
import example.com.bean.CommentBean;
import example.com.doubandemo.R;
import example.com.other.CircleTransform;

/**
 * Created by Administration on 2017/5/5.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private boolean isLogin;

    private List<CommentBean.CommentsBean> commentsList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;

    public CommentAdapter(List<CommentBean.CommentsBean> comentList, Context context) {
        this.commentsList = comentList;
        this.mContext = context;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.erroe_layout, parent, false);

            return new CommentViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_comment_item_layout, parent, false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, final int position) {
        if (commentsList.size() > 0) {
            final CommentBean.CommentsBean commentsBean = commentsList.get(position);
            CommentBean.CommentsBean.AuthorBean authorBean = commentsBean.getAuthor();

            Glide.with(mContext)
                    .load(authorBean.getAvatar())
                    .dontAnimate()
                    .bitmapTransform(new CircleTransform(mContext))
                    .into(holder.userHeadIcon);
            holder.userName.setText(authorBean.getName());
            holder.comment.setText(commentsBean.getContent());
            holder.itemView.findViewById(R.id.comment_rl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(position, commentsBean);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return commentsList.size() > 0 ? commentsList.size() : 1;
    }

    public int getItemViewType(int position) {
        if (commentsList.size() <= 0) {
            Log.i("bookListr", commentsList.size() + "");
            return -1;
        }
        return super.getItemViewType(position);
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView userHeadIcon;
        private TextView userName;
        private TextView comment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userHeadIcon = (ImageView) itemView.findViewById(R.id.sentUser_portrait);
            userName = (TextView) itemView.findViewById(R.id.user_name_textview);
            comment = (TextView) itemView.findViewById(R.id.user_comment_textview);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, CommentBean.CommentsBean commentsBean);
    }

    public void clearData() {
        commentsList.clear();
        notifyItemMoved(0, commentsList.size());
    }

    public List<CommentBean.CommentsBean> getDatas() {

        return commentsList;
    }

}
