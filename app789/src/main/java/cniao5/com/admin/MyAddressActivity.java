package cniao5.com.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;

import java.text.SimpleDateFormat;
import java.util.Date;

import cniao5.com.admin.bean.Ware;
import cniao5.com.admin.widget.CnToolbar;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/5/3.
 */
public class MyAddressActivity extends AppCompatActivity implements LocationSource,AMapLocationListener{
    private MapView mapView;
    //声明AMapLocationClient类对象
    public AMapLocationClient mapLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mapLocationOption = null;
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    //标题栏
    private CnToolbar addressTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_address_layout);
        initView(savedInstanceState);

    }

    private void initView(Bundle savedInstanceState) {
        addressTool = (CnToolbar) findViewById(R.id.address_toolbar);
        initToolbar(addressTool);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        AMap aMap = null;
        if (aMap == null) {
            aMap = mapView.getMap();
        }


        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);

        //定位的小图标 默认是蓝点 这里自定义一团火，其实就是一张图片
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_gaode));
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);

        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.BLUE);
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(2);
        aMap.setMyLocationStyle(myLocationStyle);

        //开始定位
        initLocation();
    }

    private void initToolbar(CnToolbar addressTool) {
        addressTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initLocation() {
        //初始化定位
        mapLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mapLocationClient.setLocationListener(this);
        //初始化定位参数
        mapLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mapLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mapLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mapLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mapLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mapLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mapLocationOption.setInterval(2000);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mapLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mapLocationOption.setLocationCacheEnable(false);

        //给定位客户端对象设置定位参数
        mapLocationClient.setLocationOption(mapLocationOption);
        //启动定位
        mapLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation!= null&&mListener!=null){
            if(aMapLocation!=null&&aMapLocation.getErrorCode()==0){
                //定位成功回调信息，设置相关信息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                //获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间

                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
                aMapLocation.getAoiName();//获取当前定位点的AOI信息
                aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                aMapLocation.getFloor();//获取当前室内定位的楼层
                aMapLocation.getGpsAccuracyStatus();//获取GPS的当前状态

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if(isFirstLoc){
                    //设置缩放级别
                    mapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(17));
                    mapView.getMap().getUiSettings().setCompassEnabled(true);// 设置指南针
                    mapView.getMap().getUiSettings().setScaleControlsEnabled(true);// 设置比例尺
                    //mapView.getMap().setMyLocationStyle(AMap.MAP_TYPE_NORMAL);
                    //将地图移动到定位点
                    mapView.getMap().moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //添加图钉
                    mapView.getMap().addMarker(getMarkerOptions(aMapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + "" + aMapLocation.getProvince() + "" + aMapLocation.getCity() + "" + aMapLocation.getProvince() + "" + aMapLocation.getDistrict() + "" + aMapLocation.getStreet() + "" + aMapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }

                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            }else{
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation aMapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_map_icon));
        //位置
        options.position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(aMapLocation.getCountry() + "" + aMapLocation.getProvince() + "" + aMapLocation.getCity() +  "" + aMapLocation.getDistrict() + "" + aMapLocation.getStreet() + "" + aMapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        options.snippet("我在这里");
        //设置多少帧刷新一次图片资源
        options.period(60);

        return options;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if(mListener==null){
            //初始化定位
            mapLocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mapLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mapLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mapLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mapLocationClient.setLocationOption(mapLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mapLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mapLocationClient!= null) {
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient= null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        if(null !=mapLocationClient){
            mapLocationClient.onDestroy();
        }
    }
}
