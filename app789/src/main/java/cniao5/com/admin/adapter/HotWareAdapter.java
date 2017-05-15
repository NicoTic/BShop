package cniao5.com.admin.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cniao5.com.admin.bean.Ware;
import cniao5.com.admin.util.CartProvider;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/4/7.
 */
public class HotWareAdapter extends RecyclerView.Adapter<HotWareAdapter.HotWareViewHolder> {
    private List<Ware> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;

    public HotWareAdapter(Context context, List<Ware> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
    }

    @Override
    public HotWareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.template_hot_ware_item,null);
        return new HotWareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotWareViewHolder holder, int position) {
           holder.bind(position);

    }

    @Override
    public int getItemCount() {
        if(mDatas!=null&&mDatas.size()>0){
            return mDatas.size();
        }
        return 0;
    }

    public List<Ware> getDatas(){

        return  mDatas;
    }

    public void clearData(){
        mDatas.clear();
        notifyItemMoved(0,mDatas.size());
    }

    public void addData(List<Ware> datas){
        addData(0,datas);
    }

    public void addData(int startPosition,List<Ware> wares){
        if(wares!=null&&wares.size()>0){
            mDatas.addAll(wares);
            notifyItemRangeChanged(startPosition,mDatas.size());
        }


    }

    protected class HotWareViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView draweeView;
        private TextView textTitle;
        private TextView textPrice;
        private Button buy_btn;

        public HotWareViewHolder(View itemView) {
            super(itemView);

            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            textPrice = (TextView) itemView.findViewById(R.id.text_price);
            buy_btn = (Button) itemView.findViewById(R.id.button_buy);

        }

        public void bind(int position) {
           final Ware ware = mDatas.get(position);

           draweeView.setImageURI(Uri.parse(ware.getImgUrl()));
            textTitle.setText(ware.getName());
            textPrice.setText("$"+ware.getPrice());

            buy_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartProvider cartProvider = new CartProvider(mContext);
                    cartProvider.addData(ware);
                    Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
