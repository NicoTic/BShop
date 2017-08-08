package com.example.alipay.myreadingapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alipay.myreadingapplication.R;
import com.example.alipay.myreadingapplication.model.Book;
import com.example.alipay.myreadingapplication.realm.RealmController;

import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by Jxq .
 * Description:“想读图书”列表的适配器
 * Date:on 2017/7/19.
 */
public class BooksAdapter extends RealmBasedRecyclerViewAdapter<Book,BooksAdapter.CardViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;

    public BooksAdapter(Context context, RealmResults<Book> data) {
        super(context, data,true,false);
        this.mContext = context;
    }

    @Override
    public CardViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.want_read_item_layout,viewGroup,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindRealmViewHolder(CardViewHolder cardViewHolder, int i) {
        final Book book = realmResults.get(i);
        if(book==null){
            return;
        }else{
            cardViewHolder.bind(book);
        }
    }



    public class CardViewHolder extends RealmViewHolder{
        private final Context context;
        public CardView card;
        public TextView textTitle;
        public TextView textAuthor;
        public TextView textPages;
        public ImageView imageBackground;

        public CardViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            card = (CardView) itemView.findViewById(R.id.card_books);
            textTitle = (TextView) itemView.findViewById(R.id.text_books_title);
            textAuthor = (TextView) itemView.findViewById(R.id.text_books_author);
            textPages = (TextView) itemView.findViewById(R.id.text_books_pages);
            imageBackground = (ImageView) itemView.findViewById(R.id.image_background);
        }

        public void bind(final Book book){
            final long id = book.getId();
            final String title = book.getTitle();
            textTitle.setText(book.getTitle());
            textAuthor.setText(book.getAuthor());
            textPages.setText(book.getPages());
            if(book.getImageUrl()!=null){
                Glide.with(mContext)
                        .load(book.getImageUrl().replace("https","http"))
                        .asBitmap()
                        .fitCenter()
                        .into(imageBackground);
            }

            card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    RealmController.deleteById(Book.class,id);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, book.getTitle() + " is removed from Realm", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View content = inflater.inflate(R.layout.edit_item,null);
                    final EditText editTitle = (EditText) content.findViewById(R.id.title);
                    final EditText editAuthor = (EditText) content.findViewById(R.id.author);
                    final EditText editImagethum = (EditText) content.findViewById(R.id.thumbnail);
                    final EditText editPages = (EditText) content.findViewById(R.id.pages);

                    editTitle.setText(book.getTitle());
                    editAuthor.setText(book.getAuthor());
                    editImagethum.setText(book.getImageUrl());
                    editPages.setText(book.getPages());
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setView(content)
                            .setTitle("Edit Book")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                        Realm realm = RealmController.getRealm();
                                        realm.executeTransactionAsync(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                Book book = realm.where(Book.class).equalTo("title", title).findFirst();
                                                if(book != null) {
                                                    book.setTitle(editTitle.getText().toString());
                                                    book.setDescription(editAuthor.getText().toString());
                                                    book.setImageUrl(editImagethum.getText().toString());
                                                    book.setPages(editPages.getText().toString());
                                                }
                                            }
                                        });
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

}
