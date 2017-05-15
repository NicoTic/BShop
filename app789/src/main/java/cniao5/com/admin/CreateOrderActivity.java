package cniao5.com.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cniao5.com.admin.adapter.WareOrderAdapter;
import cniao5.com.admin.bean.ProvinceBean;
import cniao5.com.admin.bean.Ware;
import cniao5.com.admin.util.CartProvider;
import cniao5.com.admin.util.JsonFileReader;
import cniao5.com.admin.widget.CnToolbar;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/4/28.
 */
public class CreateOrderActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    private TextView txtOrder;
    //付款控件
    private RelativeLayout mLayoutAlipay;
    private RelativeLayout mLayoutWeixinpay;

    //我的地址
    private RelativeLayout addressRelative;
    private TextView addressText;
    private OptionsPickerView pvPickerView;
    //  省份
    ArrayList<ProvinceBean> provinceBeanList = new ArrayList<>();
    //  城市
    ArrayList<String> cities;
    ArrayList<List<String>> cityList = new ArrayList<>();
    //  区/县
    ArrayList<String> district;
    ArrayList<List<String>> districts;
    ArrayList<List<List<String>>> districtList = new ArrayList<>();

    //我的订单
    private RelativeLayout orderRelative;
    private TextView orderNumberText;

    private RadioButton mRbAlipay;
    private RadioButton mRbWeixinpay;

    private Button mBtnCreateOrder;
    private TextView mTextTotal;

    private RecyclerView mRecyclerView;

    private CartProvider cartProvider;

    private WareOrderAdapter mAdapter;

    private CnToolbar cnToolbar;

    private String orderNum;
    private String payChannel = CHANNEL_ALIPAY;
    private float amount;

    private HashMap<String,RadioButton> channels = new HashMap<>(3);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order_layout);
        initView();
        showData();
        initListener();
        initToolbar();

    }

    private void showCity() {
        //  获取json数据
        String province_data_json = JsonFileReader.getJson(CreateOrderActivity.this, "province_data.json");
        //  解析json数据
        parseJson(province_data_json);

        //  设置三级联动效果
        pvPickerView.setPicker(provinceBeanList, cityList, districtList,true);

        //  设置选择的三级单位
        //pvOptions.setLabels("省", "市", "区");
        //pvOptions.setTitle("选择城市");

        //  设置是否循环滚动
        pvPickerView.setCyclic(false, false, false);
        // 设置默认选中的三级项目
        pvPickerView.setSelectOptions(0, 0, 0);
        //  监听确定选择按钮
        pvPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String city = provinceBeanList.get(options1).getPickerViewText();
                String address;
                //  如果是直辖市或者特别行政区只设置市和区/县
                if ("北京市".equals(city) || "上海市".equals(city) || "天津市".equals(city) || "重庆市".equals(city) || "澳门".equals(city) || "香港".equals(city)) {
                    address = provinceBeanList.get(options1).getPickerViewText()
                            + " " + districtList.get(options1).get(option2).get(options3);
                } else {
                    address = provinceBeanList.get(options1).getPickerViewText()
                            + " " + cityList.get(options1).get(option2)
                            + " " + districtList.get(options1).get(option2).get(options3);
                }
                addressText.setText(address);
                Ware ware = new Ware();
                ware.setAddress(address);
            }
        });

    }

    private void parseJson(String province_data_json) {
        try {
            //  获取json中的数组
            JSONArray jsonArray = new JSONArray(province_data_json);
            //  遍历数据组
            for (int i = 0; i < jsonArray.length(); i++) {
                //  获取省份的对象
                JSONObject provinceObject = jsonArray.optJSONObject(i);
                //  获取省份名称放入集合
                String provinceName = provinceObject.getString("name");
                provinceBeanList.add(new ProvinceBean(provinceName));
                //  获取城市数组
                JSONArray cityArray = provinceObject.optJSONArray("city");
                cities = new ArrayList<>();//   声明存放城市的集合
                districts = new ArrayList<>();//声明存放区县集合的集合
                //  遍历城市数组
                for (int j = 0; j < cityArray.length(); j++) {
                    //  获取城市对象
                    JSONObject cityObject = cityArray.optJSONObject(j);
                    //  将城市放入集合
                    String cityName = cityObject.optString("name");
                    cities.add(cityName);
                    district = new ArrayList<>();// 声明存放区县的集合
                    //  获取区县的数组
                    JSONArray areaArray = cityObject.optJSONArray("area");
                    //  遍历区县数组，获取到区县名称并放入集合
                    for (int k = 0; k < areaArray.length(); k++) {
                        String areaName = areaArray.getString(k);
                        district.add(areaName);
                    }
                    //  将区县的集合放入集合
                    districts.add(district);
                }
                //  将存放区县集合的集合放入集合
                districtList.add(districts);
                //  将存放城市的集合放入集合
                cityList.add(cities);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initToolbar() {
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showData() {
        cartProvider = new CartProvider(this);
        mAdapter = new WareOrderAdapter(this,cartProvider.getAllData());

        LinearLayoutManager linearlayouManager = new LinearLayoutManager(this);
        linearlayouManager.setOrientation(LinearLayoutManager.HORIZONTAL);
       // mRecyclerView.setAdapter(mAdapter);
    }
    
    private void initListener() {
        mLayoutAlipay.setOnClickListener(this);
        mLayoutWeixinpay.setOnClickListener(this);

        addressRelative.setOnClickListener(this);
        addressRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPickerView.show();
            }
        });
        orderRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CreateOrderActivity.this,MyOrderActivity.class);
                startActivity(intent);
            }
        });

        mBtnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CreateOrderActivity.this,NewOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        addressRelative = (RelativeLayout) findViewById(R.id.address_rl);
        addressText = (TextView) findViewById(R.id.address);
        pvPickerView = new OptionsPickerView(CreateOrderActivity.this);

        orderRelative = (RelativeLayout) findViewById(R.id.order_rl);
        orderNumberText = (TextView) findViewById(R.id.order_number);
        showOrderNumber();

        txtOrder = (TextView) findViewById(R.id.txt_order);
        mLayoutAlipay= (RelativeLayout) findViewById(R.id.rl_alipay);
        mLayoutWeixinpay = (RelativeLayout) findViewById(R.id.rl_weixinpay);
        mRbAlipay = (RadioButton) findViewById(R.id.rb_alipay);
        mRbWeixinpay = (RadioButton) findViewById(R.id.rb_weixinpay);

        mBtnCreateOrder = (Button) findViewById(R.id.create_order_btn);
        mTextTotal = (TextView) findViewById(R.id.txt_total);
        cnToolbar = (CnToolbar) findViewById(R.id.order_toolbar);


        channels.put(CHANNEL_ALIPAY,mRbAlipay);
        channels.put(CHANNEL_WECHAT,mRbWeixinpay);
        Intent intent = getIntent();
        float money =intent.getFloatExtra("total",0);
        mTextTotal.setText("应付款： ￥"+ money);

        showCity();
    }

    private void showOrderNumber() {
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int minute = time.minute;
        int hour = time.hour;
        int sec = time.second;
        orderNumberText.setText(""+year+month+day+hour+minute+ sec);
    }

    @Override
    public void onClick(View view) {
        selectPayChannle(view.getTag().toString());
    }

    private void selectPayChannle(String s) {
        for (Map.Entry<String,RadioButton> entry:channels.entrySet()){

            payChannel = s;
            RadioButton rb = entry.getValue();
            if(entry.getKey().equals(s)){

                boolean isCheck = rb.isChecked();
                rb.setChecked(!isCheck);
            }
            else
                rb.setChecked(false);
        }

    }
}
