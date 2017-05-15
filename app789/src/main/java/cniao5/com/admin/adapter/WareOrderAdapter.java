package cniao5.com.admin.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cniao5.com.admin.bean.Ware;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/4/28.
 */
public class WareOrderAdapter extends SimpleAdapter<Ware> {

    public WareOrderAdapter(Context context, List<Ware> datas) {
        super(context, R.layout.ware_order_item_layout,datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Ware item) {
        SimpleDraweeView mImageView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        mImageView.setImageURI(Uri.parse(item.getImgUrl()));
    }
    public float getTotalPrice(){
        float sum = 0;
        if(datas!=null&&datas.size()>0){
            for(int i =0;i<datas.size();i++){
                Ware ware = datas.get(i);
                //判断是否选中商品
                if(ware.isChecked()){
                    sum = sum+ware.getCount()*ware.getPrice();
                }
            }
        }
        return sum;

    }

    private boolean isNull(){

        return (datas !=null && datas.size()>0);
    }

}
