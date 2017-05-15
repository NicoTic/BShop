package cniao5.com.admin.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

import cniao5.com.admin.Contants;
import cniao5.com.admin.CreateOrderActivity;
import cniao5.com.admin.MainActivity;
import cniao5.com.admin.adapter.ShoppingCartAdapter;
import cniao5.com.admin.adapter.ShoppingCartAdapterone;
import cniao5.com.admin.bean.Page;
import cniao5.com.admin.bean.ShoppingCartWare;
import cniao5.com.admin.bean.Ware;
import cniao5.com.admin.custom.DividerItemDecoration;
import cniao5.com.admin.http.BaseCallback;
import cniao5.com.admin.http.OkHttpHelper;
import cniao5.com.admin.util.CartProvider;
import cniao5.com.admin.widget.CnToolbar;
import cniao5.com.cniao5shop.R;

/**
 * 购物车界面
 * Created by Administration
 */
public class CartFragment extends Fragment implements View.OnClickListener{
    public static final int ACTION_EDIT=0;//编辑状态
    public static final int ACTION_CAMPLATE=1;//完成状态

    private RecyclerView cartRecyclerView;
    private CheckBox mCheckBox;
    private TextView mTextTotal;

    private Button mBtnOrder;

    private Button mBtnDel;

    private CnToolbar mToolbar;

    private ShoppingCartAdapterone mAdapter;
    private CartProvider cartProvider;

    private OkHttpHelper ookHttpHelper = OkHttpHelper.getInstance();
    public List<Ware> wareList;
    private List<ShoppingCartWare> wares;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =   inflater.inflate(R.layout.fragment_cart,container,false);

        initView(view);
        initListener();
        init();

        return view;
    }

    private void initListener() {
    }

    private void init() {
        cartProvider = new CartProvider(getContext());
        changeToolbar();
        showData();
    }

    /**
     * 初始化控件
     * @param view
     */
    private void initView(View view) {
        cartRecyclerView = (RecyclerView) view.findViewById(R.id.cart_recycler_view);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_all);
        mTextTotal = (TextView) view.findViewById(R.id.txt_total);
        mBtnOrder = (Button) view.findViewById(R.id.btn_order);
        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayOrder();
            }
        });
        mToolbar = (CnToolbar) view.findViewById(R.id.toolbar);

        mBtnDel = (Button) view.findViewById(R.id.btn_del);
        //设置点击删除
        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.deleteCart();
            }
        });
    }

    private void showData() {
        wareList = cartProvider.getAllData();

        mAdapter = new ShoppingCartAdapterone(getContext(),wareList,mCheckBox,mTextTotal);

        cartRecyclerView.setAdapter(mAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }


    private void changeToolbar() {
        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getmRightImageButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");

        mToolbar.getmRightImageButton().setOnClickListener(this);

        mToolbar.getmRightImageButton().setTag(ACTION_EDIT);

    }

    @Override
    public void onClick(View view) {
       int action = (int) view.getTag();
        if(action == ACTION_EDIT){
            //变成完成状态
            showDelControl();
        }
        else if(action==ACTION_CAMPLATE){
            //变成编辑状态
            hideDelControl();
        }

    }

    private void PayOrder() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CreateOrderActivity.class);
        intent.putExtra("total",mAdapter.getTotalPrice());
        startActivity(intent);
    }

    private void hideDelControl() {
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);

        mBtnDel.setVisibility(View.GONE);
        mToolbar.getmRightImageButton().setText("编辑");
        mToolbar.getmRightImageButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

        mCheckBox.setChecked(true);
    }

    private void showDelControl() {
        mToolbar.getmRightImageButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getmRightImageButton().setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);
    }

    public void refData(){
        mAdapter.clear();
        List<Ware> carts = cartProvider.getAllData();
       carts.clear();
    }
}
