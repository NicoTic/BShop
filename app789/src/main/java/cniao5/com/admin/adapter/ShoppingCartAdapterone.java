package cniao5.com.admin.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import cniao5.com.admin.bean.ShoppingCartWare;
import cniao5.com.admin.bean.Ware;
import cniao5.com.admin.custom.ValueSelector;
import cniao5.com.admin.util.CartProvider;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/4/21.
 */
public class ShoppingCartAdapterone extends RecyclerView.Adapter<ShoppingCartAdapterone.ShopCartViewHolder> {
    private static final String TAG = "ShoppingCartAdapter";
    //监听器接口回调
    private OnItemClickListener onItemClickListener;
    /**
     * 设置某条的监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private Context context;
    private List<Ware> wares;

    private CheckBox checkBox;
    private TextView textView;

    private CartProvider cartProvider;

    public ShoppingCartAdapterone(final Context context, final List<Ware> datas, CheckBox mCheckBox, TextView mTextTotal) {
        this.context = context;
        this.wares = datas;

        setCheckBox(mCheckBox);
        setTextView(mTextTotal);

        cartProvider = new CartProvider(context);

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //1.设置点击item的状态
                Ware shopingCart = wares.get(position);
                shopingCart.setChecked(!shopingCart.isChecked());
                notifyItemChanged(position);
                //2.设置全选和非全选
                checkListener();
                //3.显示总价格
                showTotalPrice();
                Toast.makeText(context,"选中了"+position,Toast.LENGTH_SHORT).show();
            }
        });

        showTotalPrice();

    }

    /**
     * 全选的监听
     */
    private void checkListener() {
        int num = 0;
        if(wares != null && wares.size()>0){
            for(int i=0;i<wares.size();i++){
                Ware cart = wares.get(i);
                //只要有一个没有被选中就把全选设置为未勾选
                if(!cart.isChecked()){
                    checkBox.setChecked(false);
                }else{
                    num += 1;
                }
            }
            if(wares.size()==num){
                checkBox.setChecked(true);
            }
        }
    }

    /**
     * 显示总商品的价格
     */
    public void showTotalPrice() {
        float total = getTotalPrice();

        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    /**
     * 计算购物车里面所选商品的总价
     * @return
     */
    public float getTotalPrice() {
        float sum = 0;
        if(wares!=null&&wares.size()>0){
            for(int i =0;i<wares.size();i++){
                Ware ware = wares.get(i);
                //判断是否选中商品
                if(ware.isChecked()){
                    sum = sum+ware.getCount()*ware.getPrice();
                }
            }
        }
        return sum;
    }

    private boolean isNull(){

        return (wares !=null && wares.size()>0);
    }

    private void setTextView(TextView mTextTotal) {
        this.textView = mTextTotal;
    }

    private void setCheckBox(CheckBox mCheckBox) {
        this.checkBox = mCheckBox;

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAll_None(checkBox.isChecked());
                showTotalPrice();

            }
        });
    }

    /**
     * 全选和反选
     * @param checked
     */
    public void checkAll_None(boolean checked) {
        if(wares!=null&&wares.size()>0){
            for(int i=0;i<wares.size();i++){
                Ware ware = wares.get(i);
                ware.setChecked(checked);
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public ShopCartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.shopping_cart_item_layout, null);
        return new ShopCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopCartViewHolder holder, int position) {
        final Ware ware = wares.get(position);
        holder.iv_icon.setImageURI(Uri.parse(ware.getImgUrl()));
        holder.checkbox.setChecked(ware.isChecked());
        holder.tv_name.setText(ware.getName());
        holder.tv_price.setText("￥ "+ware.getPrice());
        holder.valueSelector.setValue(ware.getCount());

        holder.valueSelector.setOnButtonClickListener(new ValueSelector.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                ware.setCount(value);
                cartProvider.updataData(ware);
                showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                ware.setCount(value);
                cartProvider.updataData(ware);
                showTotalPrice();
            }
        });

    }

    @Override
    public int getItemCount() {
        return wares.size();
    }
    //设置点击删除选中的数据
    public void deleteCart() {
        if(wares!=null&&wares.size()>0){
            for(Iterator iterator = wares.iterator();iterator.hasNext();){
                    Ware ware = (Ware) iterator.next();
                    if(ware.isChecked()){
                        //这行代码放在前面
                        int position = wares.indexOf(ware);
                        //删除本地缓存的
                        cartProvider.deleteData(ware);
                        //删除当前内存的
                        iterator.remove();
                        //刷新数据
                        notifyItemRemoved(position);
                    }
            }
        }
    }


    public class ShopCartViewHolder extends RecyclerView.ViewHolder{
        private CheckBox checkbox;
        private SimpleDraweeView iv_icon;
        private TextView tv_name;
        private TextView tv_price;
        private ValueSelector valueSelector;

        public ShopCartViewHolder(final View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            iv_icon = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
            tv_name = (TextView) itemView.findViewById(R.id.text_title);
            tv_price = (TextView) itemView.findViewById(R.id.text_price);
            valueSelector = (ValueSelector) itemView.findViewById(R.id.num_control);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(view,getLayoutPosition());
                    }
                }
            });
        }
    }

    //设置点击某个item的监听
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void clear(){
        for (int i=0;i<wares.size();i++){

            wares.removeAll(wares);
            notifyItemRemoved(i);
        }
    }

}
