package com.example.alipay.myreadingapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alipay.myreadingapplication.R;
import com.example.alipay.myreadingapplication.model.BookItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jxq .
 * Description:图书展示类的适配器
 * Date:on 2017/7/11.
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    private List<BookItemModel.BooksBean> booksList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li)
    {
        mListener = li;
    }

    public BookListAdapter(List<BookItemModel.BooksBean> booksList, Context mContext) {
        this.booksList = booksList;
        this.mContext = mContext;
        notifyDataSetChanged();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==-1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.error_layout,parent,false);

            return new BookViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item_layout,parent,false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        if(booksList.size()>0){
            final BookItemModel.BooksBean booksBean = booksList.get(position);
            BookItemModel.BooksBean.ImagesBean imagesBean = booksBean.getImages();

            Glide.with(mContext).load(booksBean.getImages().getLarge()).into(holder.bookImg);
            holder.bookTitle.setText(booksBean.getTitle());
            if(booksBean.getAuthor().size()>0){
                holder.bookAuthor.setText(booksBean.getAuthor().get(0));
            }
            else{
                holder.bookAuthor.setText("佚名");
            }
            holder.bookPages.setText(booksBean.getPages());
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
        return booksList.size()>0?booksList.size():1;
    }

    public int getItemViewType(int position) {
        if(booksList.size()<=0){
            return -1;
        }
        return super.getItemViewType(position);
    }
    public  class BookViewHolder extends RecyclerView.ViewHolder{
        public ImageView bookImg;
        private TextView bookTitle;
        private TextView bookAuthor;
        private TextView bookPages;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookImg = (ImageView) itemView.findViewById(R.id.book_img);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthor = (TextView) itemView.findViewById(R.id.book_author);
            bookPages = (TextView) itemView.findViewById(R.id.book_pages);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position, BookItemModel.BooksBean item);
    }

    public void clearData(){
        booksList.clear();
        notifyItemMoved(0,booksList.size());
    }

    public void addData(List<BookItemModel.BooksBean> datas){
        addData(0,datas);
    }

    public void addData(int startPosition,List<BookItemModel.BooksBean> books) {
        if (books != null && books.size() > 0) {
            booksList.addAll(books);
            notifyItemRangeChanged(startPosition, booksList.size());
        }

    }

    public List<BookItemModel.BooksBean> getDatas(){

        return  booksList;
    }
}
