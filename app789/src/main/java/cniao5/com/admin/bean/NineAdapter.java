package cniao5.com.admin.bean;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.w4lle.library.NineGridAdapter;

import java.util.List;

/**
 * Created by Administration on 2017/5/5.
 */
public class NineAdapter extends NineGridAdapter {
    List<OrderBean> orderList;

    public NineAdapter(Context context, List list) {
        super(context, list);
        this.orderList = list;
    }

    @Override
    public int getCount() {
        return (orderList==null) ? 0 : orderList.size();
    }

    @Override
    public String getUrl(int positopn) {
        OrderBean orderBean = orderList.get(positopn);
        return orderBean.getImgUrl();
    }

    @Override
    public Object getItem(int position) {
        return (orderList == null) ? null : orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        OrderBean orderBean = orderList.get(position);
        return orderBean.getId();
    }

    @Override
    public View getView(int i, View view) {
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        Picasso.with(context).load(getUrl(i)).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(iv);
        return iv;
    }
}
