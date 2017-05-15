package cniao5.com.admin.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cniao5.com.admin.bean.Ware;
import cniao5.com.cniao5shop.R;


public class WaresAdapter extends SimpleAdapter<Ware>{

    private Context mContext;
    private  OnCampaignClickListener mListener;

    public void setOnCampaignClickListener(OnCampaignClickListener listener){

        this.mListener = listener;
    }

    public WaresAdapter(Context context, List<Ware> datas) {
        super(context, R.layout.ware_item_layout, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder viewHoder, final Ware item){

        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
//        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
//        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
        Glide.with(mContext).load(item.getImgUrl()).into(viewHoder.getImageView(R.id.ware_image_view));
        viewHoder.getImageView(R.id.ware_image_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mListener !=null){

                    mListener.onClick(item);

                }
            }
        });
    }

    public  interface OnCampaignClickListener{


        void onClick(Ware ware);

    }

}
