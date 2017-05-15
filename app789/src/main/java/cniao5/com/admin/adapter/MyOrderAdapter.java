package cniao5.com.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
import java.util.List;

import cniao5.com.admin.MainActivity;
import cniao5.com.admin.MyOrderActivity;
import cniao5.com.admin.NewOrderActivity;
import cniao5.com.admin.bean.NineAdapter;
import cniao5.com.admin.bean.OrderBean;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/5/4.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder> {
    private LayoutInflater mInflater;
    private List<OrderBean> list=new ArrayList<>();
    private Context mContext;

    public MyOrderAdapter(Context context, List<OrderBean> ordersList) {
        this.mContext = context;
        this.list = ordersList;
    }


    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.my_order_item_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.textTotal.setText(list.get(position).getPrice()+"");
        String billNumber = getBillnumber();
        holder.textNumber.setText("订单编号："+ billNumber);
        holder.nineGridlayout.setAdapter(new NineAdapter(mContext,list));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, NewOrderActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    private String getBillnumber() {
            Time time = new Time("GMT+8");
            time.setToNow();
            int year = time.year;
            int month = time.month;
            int day = time.monthDay;
            int minute = time.minute;
            int hour = time.hour;
            int sec = time.second;
          String billNumber = ""+year+month+day+hour+minute+ sec;
          return billNumber;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView textNumber, textTotal;
        private NineGridlayout nineGridlayout;
        private Button button;

        public OrderViewHolder(View itemView) {
            super(itemView);
            textNumber = (TextView) itemView.findViewById(R.id.number_text);
            textTotal = (TextView) itemView.findViewById(R.id.ntotal_text);
            nineGridlayout = (NineGridlayout) itemView.findViewById(R.id.iv_ngrid_layout);
            button = (Button) itemView.findViewById(R.id.buy_btn);

        }
    }
}
