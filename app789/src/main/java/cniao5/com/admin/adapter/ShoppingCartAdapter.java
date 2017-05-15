package cniao5.com.admin.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import cniao5.com.admin.bean.ShoppingCartWare;
import cniao5.com.admin.custom.ValueSelector;
import cniao5.com.admin.util.CartProvider;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/4/20.
 */
public class ShoppingCartAdapter extends SimpleAdapter<ShoppingCartWare>implements BaseAdapter.OnItemClickListener{
    private static final String TAG = "ShoppingCartAdapter";
    private CheckBox checkBox;
    private TextView textView;

    private CartProvider cartProvider;

    private Context mContext;
    private List<ShoppingCartWare> cartWaresList;


    public ShoppingCartAdapter(Context context, List<ShoppingCartWare> datas, final CheckBox checkBox, TextView tv) {
        super(context, R.layout.shopping_cart_item_layout,datas);
        this.mContext = context;

        setCheckBox(checkBox);
        setTextView(tv);
        
        cartProvider = new CartProvider(mContext);
        setOnItemClickListener(this);
        
        showTotalPrice();
    }

    /**
     * 显示总商品的价格
     */
    private void showTotalPrice() {
        float total = getTotalPrice();

        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    /**
     * 计算购物车里面所选商品的总价
     * @return
     */
    private float getTotalPrice() {
        float sum=0;
        if(datas==null||datas.size()<=0){
            return sum;
        }

        for (ShoppingCartWare cart: datas) {
            if(cart.isChecked())
                sum += cart.getCount()*cart.getPrice();
        }

        return sum;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder,final ShoppingCartWare item) {
        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText(String.valueOf(item.getPrice()));
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        CheckBox checkBox = (CheckBox) viewHoder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());

        ValueSelector valueSelector = (ValueSelector) viewHoder.getView(R.id.num_control);
        valueSelector.setMinValue(1);
        valueSelector.setMaxValue(1000);
        valueSelector.setValue(item.getCount());

        /**
         * 增加/减少商品时计算总价
         */
        valueSelector.setOnButtonClickListener(new ValueSelector.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                item.setCount(value);
                cartProvider.updataData(item);
                showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                item.setCount(value);
                cartProvider.updataData(item);
                showTotalPrice();
            }
        });


    }

    @Override
    public void onItemClick(View view, int position) {
        //设置点击Item的状态
        ShoppingCartWare cart =  getItem(position);
        cart.setChecked(!cart.isChecked());
        notifyItemChanged(position);
        //设置全选和非全选
        checkListen();
        //显示总价
        showTotalPrice();
        Toast.makeText(context,"选中了"+position,Toast.LENGTH_SHORT).show();
    }

    /**
     * 全选的监听
     */
    private void checkListen() {
        int count = 0;
        int checkNum = 0;
        if (datas != null) {
            count = datas.size();

            for (ShoppingCartWare cart : datas) {
                if (!cart.isChecked()) {
                    checkBox.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }
            if (count == checkNum) {
                checkBox.setChecked(true);
            }

        }
    }

    public void checkAll_None(boolean isChecked){


        if(datas==null||datas.size()<=0){
            return;
        }

        int i=0;
        for (ShoppingCartWare cart :datas){
            cart.setChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }

    }

    public void delCart() {


        if (datas == null || datas.size() <= 0)
            return;

        for (Iterator iterator = datas.iterator(); iterator.hasNext(); ) {

            ShoppingCartWare cart = (ShoppingCartWare) iterator.next();
            if (cart.isChecked()) {
                int position = datas.indexOf(cart);
                cartProvider.deleteData(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }

        }
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setCheckBox(final CheckBox checkBox) {
        this.checkBox = checkBox;

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAll_None(checkBox.isChecked());
                showTotalPrice();

            }
        });
    }

}
