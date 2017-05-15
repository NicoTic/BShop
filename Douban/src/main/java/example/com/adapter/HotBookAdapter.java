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
import example.com.bean.BookListBean;
import example.com.doubandemo.R;

/**
 * Created by Administration on 2017/4/14.
 */
public class HotBookAdapter extends RecyclerView.Adapter<HotBookAdapter.BookViewHolder> {
    private List<BookListBean.BooksBean> booksList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li)
    {
        mListener = li;
    }

    public HotBookAdapter(List<BookListBean.BooksBean> bookList, Context mContext) {
            this.booksList.addAll(bookList);
            notifyDataSetChanged();
        this.mContext = mContext;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==-1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.erroe_layout,parent,false);

            return new BookViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_book_item_layout,parent,false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {

        if(booksList.size()>0){
            final BookListBean.BooksBean booksBean = booksList.get(position);
            BookListBean.BooksBean.ImagesBean imagesBean = booksBean.getImages();

            Glide.with(mContext).load(booksBean.getImages().getLarge()).into(holder.bookImg);
            holder.bookTitle.setText(booksBean.getTitle());
            if(booksBean.getAuthor().size()>0){
                holder.bookAuthor.setText(booksBean.getAuthor().get(0));
        }
            else{
                holder.bookAuthor.setText("尼采尔.诺拉德");
            }
            holder.itemView.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(position,booksBean);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        Log.i("getItemCount","list size:"+booksList.size());
        return booksList.size()>0?booksList.size():1;
    }

    public int getItemViewType(int position) {
        if(booksList.size()<=0){
            Log.i("bookListr",booksList.size()+"");
            return -1;
        }
        return super.getItemViewType(position);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder{
        public ImageView bookImg;
        private TextView bookTitle;
        private TextView bookAuthor;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookImg = (ImageView) itemView.findViewById(R.id.book_img);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthor = (TextView) itemView.findViewById(R.id.book_author);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, BookListBean.BooksBean item);
    }

    public void clearData(){
        booksList.clear();
        notifyItemMoved(0,booksList.size());
    }

    public void addData(List<BookListBean.BooksBean> datas){
        addData(0,datas);
    }

    public void addData(int startPosition,List<BookListBean.BooksBean> books) {
        if (books != null && books.size() > 0) {
            booksList.addAll(books);
            notifyItemRangeChanged(startPosition, booksList.size());
        }

    }

    public List<BookListBean.BooksBean> getDatas(){

        return  booksList;
    }
}


