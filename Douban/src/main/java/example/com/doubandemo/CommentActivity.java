package example.com.doubandemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

import example.com.adapter.CommentAdapter;
import example.com.bean.CommentBean;
import example.com.custom.DetailToolbar;
import example.com.custom.DividerItemDecoration;
import example.com.http.OkHttpHelper;
import example.com.http.SpotsCallBack;
import example.com.other.Constant;
import example.com.utils.EmptyEdit;

/**
 * Created by Administration on 2017/5/5.
 */
public class CommentActivity extends AppCompatActivity{
    private DetailToolbar commentToolbar;
    private RecyclerView recyclerView;
    private List<CommentBean.CommentsBean> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;

    private AppCompatEditText addCommentEdit;
    private AppCompatImageView submitButton;


    private String id = "611996284";

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_comment_layout);
        initView();
        initToolbar();
        initRecycler();
        initListener();
    }

    private void initListener() {

    }

    private void initRecycler() {
        String commentsUrl = Constant.API.DOUBAN_COMMENT+id+"/comments";

        okHttpHelper.get(commentsUrl,new SpotsCallBack<CommentBean>(CommentActivity.this){
            @Override
            public void onSuccess(Response response, CommentBean commentBean) {
                super.onSuccess(response, commentBean);
                commentList.addAll(commentBean.getComments());

                showRecyclerData();
                commentAdapter.notifyDataSetChanged();
                Log.i("sucessa",commentBean.getComments().get(0).getAuthor().getName());
            }

            @Override
            public void onResponse(Response response) {
                super.onResponse(response);
                Log.i("response","onResponse");
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                Log.i("failure","onFailure");
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                super.onError(response, code, e);
                Log.i("error","onError");
            }
        });
       }

    private void showRecyclerData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(commentList,this);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

    }

    private void initToolbar() {
        commentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commentToolbar.setTitle(R.string.comment);
    }

    private void initView() {
        commentToolbar = (DetailToolbar) findViewById(R.id.comment_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.comment_recycler);
        addCommentEdit = (AppCompatEditText) findViewById(R.id.add_coment_editText);
        submitButton = (AppCompatImageView) findViewById(R.id.submit_comment);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });
    }


    private void addComment() {

        CommentBean.CommentsBean comments = new CommentBean.CommentsBean();
        CommentBean.CommentsBean.AuthorBean author = new CommentBean.CommentsBean.AuthorBean();
        author.setAvatar("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg");
        author.setName("Unknown Somebody");
        comments.setAuthor(author);
        //      String comment = addCommentEdit.getText().toString().trim();
        comments.setContent(addCommentEdit.getText().toString());
        commentList.add(0,comments);
        commentAdapter.notifyDataSetChanged();

        EmptyEdit.emptyEditText(addCommentEdit);
    }
}
