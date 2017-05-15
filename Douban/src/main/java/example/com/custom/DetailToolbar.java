package example.com.custom;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.com.doubandemo.R;

/**
 * Created by Administration on 2017/4/14.
 */
public class DetailToolbar extends Toolbar {
    private View mView;

    private LayoutInflater layoutInflater;
    private ImageButton shareImgBtn,likeimgBtn,commentImgBtn;
    private TextView textTitle;

    public DetailToolbar(Context context) {
        this(context,null);
    }

    public DetailToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DetailToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        //为Toolbar添加间距
        setContentInsetsRelative(10,10);

        if(attrs!=null){
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(),attrs, R.styleable.DetailToolbar,defStyleAttr,0);

            /*
             * 读取在自定义属性值
             */
            final Drawable shareIcon = a.getDrawable(R.styleable.DetailToolbar_shareButtonIcon);
            final Drawable likeIcon = a.getDrawable(R.styleable.DetailToolbar_likeButtonIcon);
            final Drawable commentIcon = a.getDrawable(R.styleable.DetailToolbar_commentButtonIcon);

            if(shareIcon!=null){
                setShareButtonIcon(shareIcon);
            }

            if(likeIcon!=null){
                setLikeButtonIcon(likeIcon);
            }

            if(commentIcon!=null){
                setCommentico(commentIcon);
            }

            a.recycle();
        }

    }

    private void setCommentico(Drawable commentIcon) {
        if(commentImgBtn!=null){
            commentImgBtn.setBackground(commentIcon);
            commentImgBtn.setVisibility(VISIBLE);
        }
    }

    private void setLikeButtonIcon(Drawable likeIcon) {
        if(likeimgBtn!=null){
            likeimgBtn.setBackground(likeIcon);
            likeimgBtn.setVisibility(VISIBLE);
        }

    }

    private void setShareButtonIcon(Drawable shareIcon) {
        if(shareImgBtn!=null){
            shareImgBtn.setBackground(shareIcon);
            shareImgBtn.setVisibility(VISIBLE);
        }

    }

    @Override
    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        initView();
        if(textTitle !=null) {
            textTitle.setText(title);
            showTextTitle();
        }

    }

    private void showTextTitle() {
        if(textTitle !=null)
            textTitle.setVisibility(VISIBLE);
    }

    public  void setShareButtonOnClickListener(OnClickListener li){

        shareImgBtn.setOnClickListener(li);
    }
    public  void setFavoriteButtonOnClickListener(OnClickListener li){

        likeimgBtn.setOnClickListener(li);
    }

    public  void setCommentButtonOnClickListener(OnClickListener li){

        commentImgBtn.setOnClickListener(li);
    }

    private void initView() {
        if(mView==null){
            layoutInflater = LayoutInflater.from(getContext());
            mView = layoutInflater.inflate(R.layout.detail_toolbar,null);

            shareImgBtn = (ImageButton) mView.findViewById(R.id.toolbar_share);
            likeimgBtn = (ImageButton) mView.findViewById(R.id.toolbar_like);
            commentImgBtn = (ImageButton) mView.findViewById(R.id.toolbar_comment);
            textTitle = (TextView) findViewById(R.id.tolbar_title);

            //将控件组合起来
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView,lp);
        }
    }

}
