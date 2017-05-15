package cniao5.com.admin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.SparseArray;
import android.webkit.JsPromptResult;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cniao5.com.admin.bean.OrderBean;
import cniao5.com.admin.bean.ShoppingCartWare;
import cniao5.com.admin.bean.Ware;

/**
 * Created by Administration on 2017/4/20.
 * Descrip：数据存储类，存储数据；存储数据--把集合转成String类型存储（Gson）；取数据--把String转换成列表数据(Gson)
 */
public class CartProvider {
    public static final String CART_JSON="cart_json";

    /**
     * SparseArray替换HashMap,性能好于HashMap
     */
    private SparseArray<Ware> datas = null;
    private Context mContext;

    public CartProvider(Context mContext) {
        this.mContext = mContext;
        datas = new SparseArray<>(10);
        listToSpare();
}

    private void listToSpare() {
        List<Ware> carts = getAllData();
        if(carts!=null&&carts.size()>0){
            for(Ware shoppingCart : carts){
                datas.put(shoppingCart.getId(),shoppingCart);
            }
        }

    }

    /**
     * 得到所有数据
     *
     * @return
     */
    public List<Ware> getAllData() {
        return getDataFromLocal();
    }

    /**
     * 从本地获取json数据，并且通过Gson解析成List列表数据
     * @return
     */
    public List<Ware> getDataFromLocal() {
        List<Ware> shoppingCarts = null;
        //从本地获取缓存的数据
        String saveJson = MyPreferencesUtils.getString(mContext,CART_JSON);
        if(!TextUtils.isEmpty(saveJson)){
            //通过Gson把数据转换成List列表
            shoppingCarts = new Gson().fromJson(saveJson,new TypeToken<List<Ware>>(){}.getType());
        }
        return shoppingCarts;
    }

    /**
     * 增加数据
     * @param cartWare
     */

    public void addData(Ware cartWare){
        //添加数据
        Ware  tempCart = datas.get(cartWare.getId());
        if(tempCart!=null){
            //在列表中已经存在该条数据
            tempCart.setCount(tempCart.getCount()+1);
        }else{
            tempCart = cartWare;
            tempCart.setCount(1);
        }
        datas.put(tempCart.getId(),tempCart);
        //保存数据
        commit();
    }

//    public void addData(Ware ware){
//        ShoppingCartWare cart = conversion(ware);
//        addData(cart);
//    }

    /**
     * 保存数据
     * @param
     */

    private void commit() {
        //把SparseArray转换成List
        List<Ware> carts = parseToList();
        //用Gson把List转换成String
        String json = new Gson().toJson(carts);
        //保存数据
        MyPreferencesUtils.putString(mContext,CART_JSON,JsonUtil.toJSON(carts));
    }

    /**
     * 从parses的数据转换成List列表数据
     * @return
     */
    private List<Ware> parseToList() {
        List<Ware> carts = new ArrayList<>();
        if(datas!= null&&datas.size()>=0){
                for(int i = 0;i<datas.size();i++){
                    Ware cart = datas.valueAt(i);
                    carts.add(cart);
            }
        }
        return carts;
    }

    /**
     * 删除数据
     * @return cart
     */
    public void deleteData(Ware cart) {
        //删除数据
        datas.delete(cart.getId());
        //保存数据
        commit();
    }

    /**
     * 修改数据
     * @return cart
     */
    public void updataData(Ware cart) {
        //修改-count
        datas.put(cart.getId(), cart);
        //保存数据
        commit();
    }

    /**
     *  把商品Ware转换成ShoppingCartWare
     */

    public List<OrderBean> conversion(List<Ware> wares){
        List<OrderBean> orderBeenList = new ArrayList<>();

        for(int i=0;i<wares.size();i++){
            OrderBean orderBean = new OrderBean();
            Ware ware = wares.get(i);
            orderBean.setImgUrl(ware.getImgUrl());
            orderBean.setCount(ware.getCount());
            orderBean.setPrice(ware.getPrice());
            orderBean.setId(ware.getId());
            orderBeenList.add(orderBean);
        }

        return orderBeenList;
    }
}
