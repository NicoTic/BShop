package cniao5.com.admin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cniao5.com.admin.bean.Ware;
import cniao5.com.admin.util.CartProvider;
import cniao5.com.cniao5shop.R;


/**
 * Created by Administration on 2017/4/25.
 */
public class WareListAdapter extends RecyclerView.Adapter<WareListAdapter.WareListViewHolder> {
    private Context mContext;
    private List<Ware> wares = new ArrayList<>();//数据源

    public WareListAdapter(Context context, List<Ware> wareListItemBeanList) {
        this.mContext = context;
        this.wares = wareListItemBeanList;
    }

    public void addDatas(ArrayList<Ware> datas) {
        wares.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public WareListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ware_list_item_layout,parent,false);
        return new WareListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WareListViewHolder holder, int position) {
       Ware wareItem = wares.get(position);
        if(holder instanceof WareListViewHolder){
            holder.bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        return wares.size();
    }

    public class WareListViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTextView;
        private TextView mPriceTextView;
        private ImageView mImageView;
        private Button button_buy;

        public WareListViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.text_title);
            mPriceTextView = (TextView) itemView.findViewById(R.id.text_price);
            mImageView = (ImageView) itemView.findViewById(R.id.ware_item_thumile);
            button_buy = (Button) itemView.findViewById(R.id.btn_add);
        }


        //为UI控件填充数据
        private void bindView(final int position) {
            final Ware wareItem = wares.get(position);
            Log.i("wareList",wares.size()+"");
            mTitleTextView.setText(wareItem.getName());
            mPriceTextView.setText(String.valueOf(wareItem.getPrice()));
            Glide.with(mContext).load(wareItem.getImgUrl()).into(mImageView);

            button_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartProvider cartProvider = new CartProvider(mContext);
                    cartProvider.addData(wareItem);
                    Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
