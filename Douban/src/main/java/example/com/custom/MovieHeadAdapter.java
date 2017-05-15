package example.com.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import example.com.bean.MovieHeadBean;
import example.com.doubandemo.R;

/**
 * Created by Administration on 2017/4/21.
 */
public class MovieHeadAdapter extends RecyclingPagerAdapter{
    private List<MovieHeadBean> movieHeadBeanList = new ArrayList<>();
    private Context mContext;

    private List<View> viewList;

    public MovieHeadAdapter(Context mContext, List<View> views) {
        this.mContext = mContext;
        this.viewList = views;
    }

    public void addAll(List<MovieHeadBean> list){
        movieHeadBeanList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movieHeadBeanList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        ImageView movieImg;
        TextView movieCartTxt;
        TextView movieDescrip;

        if(convertView==null){
            convertView = LayoutInflater.from(container.getContext()).inflate(R.layout.movie_head_item_layout,container,false);
            movieImg = (ImageView) convertView.findViewById(R.id.movie_image);
            movieCartTxt = (TextView) convertView.findViewById(R.id.movie_cart);
            movieDescrip = (TextView) convertView.findViewById(R.id.movie_description);
            convertView.setTag(position);

        }else{
            View v = (View) convertView.getTag();
            return v;
        }

        Glide.with(mContext).load(movieHeadBeanList.get(position).getImagUrl())
                .placeholder(R.mipmap.suolue_normal)
                .into(movieImg);
        movieCartTxt.setText(movieHeadBeanList.get(position).getCategortTitle());
        movieDescrip.setText(movieHeadBeanList.get(position).getDescription());
        return convertView;
    }
}
