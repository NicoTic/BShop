package cniao5.com.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cniao5.com.admin.adapter.MyOrderAdapter;
import cniao5.com.admin.bean.OrderBean;
import cniao5.com.admin.bean.Ware;
import cniao5.com.admin.util.CartProvider;
import cniao5.com.admin.widget.CnToolbar;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/5/4.
 */
public class MyOrderActivity extends AppCompatActivity{

    private RecyclerView ordersRecycler;
    private CnToolbar orderToolbar;
    private List<OrderBean> ordersList = new ArrayList<>();

    private CartProvider cartProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_layout);
        initView();
        initToolbar();
        initRecycler();
    }

    private void initRecycler() {
        ordersRecycler.setLayoutManager(new LinearLayoutManager(this));
        ordersRecycler.setAdapter(new MyOrderAdapter(this,ordersList));
    }

    private void initToolbar() {
//        setSupportActionBar(orderToolbar);
       orderToolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               MyOrderActivity.this.finish();
           }
       });
    }

    private void initView() {
        ordersRecycler = (RecyclerView) findViewById(R.id.my_order_recycler);
        orderToolbar = (CnToolbar) findViewById(R.id.my_order_tool_bar);
        cartProvider = new CartProvider(this);
        List<Ware> wareList = cartProvider.getAllData();
        ordersList = cartProvider.conversion(wareList);

    }

}
