package cniao5.com.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cniao5.com.admin.bean.Tab;
import cniao5.com.admin.fragment.CartFragment;
import cniao5.com.admin.fragment.CategoryFragment;
import cniao5.com.admin.fragment.HomeFragment;
import cniao5.com.admin.fragment.HotFragment;
import cniao5.com.admin.fragment.MineFragment;
import cniao5.com.admin.widget.CnToolbar;
import cniao5.com.admin.widget.FragmentTabHost;
import cniao5.com.cniao5shop.R;

/**
 * description: 主类—为五个Fragment提供容器
 */

public class MainActivity extends AppCompatActivity {

    private LayoutInflater mInflater;

    private FragmentTabHost mTabhost;

    private CnToolbar mToolbar;

    private List<Tab> mTabs = new ArrayList<>(5);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initTab();

    }

    /**
     * 初始化Toolbar
     */
    private void initToolBar() {

        mToolbar = (CnToolbar) findViewById(R.id.toolbar);
        mToolbar.showSearchView();
    }

    /**
     * 初始化Tab，即为FragmenTabHost的页签填充Fragment和名称以及图标
     */
    private void initTab() {

        Tab tab_home = new Tab(HomeFragment.class,R.string.home,R.drawable.selector_icon_home);
        Tab tab_hot = new Tab(HotFragment.class,R.string.hot,R.drawable.selector_icon_hot);
        Tab tab_category = new Tab(CategoryFragment.class,R.string.catagory,R.drawable.selector_icon_category);
        Tab tab_cart = new Tab(CartFragment.class,R.string.cart,R.drawable.selector_icon_cart);
        Tab tab_mine = new Tab(MineFragment.class,R.string.mine,R.drawable.selector_icon_mine);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);



        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        mTabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);

        for (Tab tab : mTabs){

            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));


            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec,tab.getFragment(),null);

        }

        //取消分割线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        //mTabhost.setCurrentTab(0);


    }

    /**
     * 返回 页签视图
     * @param tab
     * @return
     */
    private  View buildIndicator(Tab tab){

        View view =mInflater.inflate(R.layout.tab_indicator,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return  view;
    }


}
