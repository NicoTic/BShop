package example.com.doubandemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import example.com.adapter.FavoriteAdapter;
import example.com.bean.FavoriteBean;
import example.com.fragment.FragmentFive;
import example.com.fragment.FragmentFour;
import example.com.fragment.FragmentOne;

import example.com.fragment.FragmentThree;
import example.com.fragment.FragmentTwo;
import example.com.fragmentActivity.MyApplication;
import example.com.other.CircleTransform;
import example.com.utils.DatabaseHelper;
import example.com.utils.DayNight;
import example.com.utils.DayNightHelper;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigation;
    private View navHeader;//头文件
    private ImageView imgNavHeaderBg, imgProfile;//头文件 背景图片、头像图片
    private TextView txtName, txtWebsite;//姓名及介绍
    private FloatingActionButton fab;//
    
    private DrawerLayout mContainer;//fragmentOne LinearLayout

    //背景图及头像地址
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "http://img3.duitang.com/uploads/item/201604/24/20160424132044_ZzhuX.jpeg";

    //当前默认的navagation menu
    private static int navItemIndex = 0;
    //用于附加tag的fragment
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    //item 被选择时，toolbar上的文字变化
    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    //夜间模式切换
    private DayNightHelper mDayNightHelper;
    private SwitchCompat item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDayNightHelper = new DayNightHelper(this);
        if (mDayNightHelper.isDay()) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }

        setContentView(R.layout.activity_main);

        initView();
//        initData();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        /**
         * 初始化的时候也要切换模式
         */

    }

    private void initView() {
        mContainer = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        mHandler = new Handler();

        //底布局
        //footLinearLayout = (FootLinearLayout) findViewById(R.id.foot_layout);
        //设置ToolBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        //设置抽屉DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //初始化导航菜单，设置导航栏NavagationView的点击事件
        mNavigation = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(mNavigation,mToolbar);

        //初始化fab按钮
        fab = (FloatingActionButton) findViewById(R.id.fab);
        // 获得navigationView的头header视图及其控件
        navHeader = mNavigation.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        //从资源文件加载toolbar标题
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        //fab按钮的点击事件
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //加载navagation头布局
        loadNavHeader();
        //头布局控件的点击事件
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,1);
            }
        });

        /**
         * 夜间模式
         */
        switchMode();
    }

    private void switchMode() {
        TypedValue bgContainer = new TypedValue();
        TypedValue bgSwitch = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.text_color, bgSwitch, true);
        theme.resolveAttribute(R.attr.backgroundCardColor, bgContainer, true);
        //item.setBackgroundResource(bgSwitch.resourceId);
        mContainer.setBackgroundResource(bgContainer.resourceId);
        /**
         * 不可忘记
         */
        switchStatusBar();
    }

    private void switchStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue primaryDark = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimaryDark, primaryDark, true);
            getWindow().setStatusBarColor(getResources().getColor(primaryDark.resourceId));
        }
    }


    //返回用户从导航菜单中选中的fragment
    private void loadHomeFragment() {
        //选择相应的导航菜单栏
        selectNavMenu();
        //设置toolbar标题
        setToolbarTitle();

        //如果用户再次选择当前的导航菜单，那么关闭导航栏
        if(getSupportFragmentManager().findFragmentByTag(CURRENT_TAG)!=null){
            mDrawerLayout.closeDrawers();

            //show or hide fab button
             toggleFab();
             return;
        }

        //为Fragment添加淡入淡出的效果
        Runnable mPenddingRunnable = new Runnable() {
            @Override
            public void run() {
                //通过替换fragment来更新主要内容
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame,fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        //如果PenddingRunnable不为空，添加到消息队列
        if(mPenddingRunnable!=null){
            mHandler.post(mPenddingRunnable);
        }
        //show or hide fab button
        toggleFab();
        //关闭抽屉
        mDrawerLayout.closeDrawers();
        // 更新toolbar
        invalidateOptionsMenu();

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex){
            case 0:
                //home
                FragmentOne homeFragment = new FragmentOne();
                return homeFragment;
            case 1:
                FragmentTwo photosFragment = new FragmentTwo();
                return photosFragment;
            case 2:
                FragmentThree  moviesFragment = new FragmentThree();
                return moviesFragment;
            case 3:
                FragmentFour notificationFragment = new FragmentFour();
                return notificationFragment;
            case 4:
                FragmentFive settingFragment = new FragmentFive();
                return settingFragment;
            default:
                return new FragmentOne();
        }
    }

    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        mNavigation.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void loadNavHeader() {
        //加载姓名和其他文字
        txtName.setText("Ravi Tamada");
        txtWebsite.setText("www.androidhive.info");

        // 加载头布局背景图片
        Glide.with(this)
                .load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);
        //加载头像
        Glide.with(this)
                .load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)//以原图的1/2作为缩略图
                .bitmapTransform(new CircleTransform(this))///圆形图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存策略
                .into(imgProfile);


        //显示通知标签旁边的圆点
        mNavigation.getMenu().getItem(3).setActionView(R.layout.menu_dot);
        //为圆点设置监听事件
        View actionView = MenuItemCompat.getActionView(mNavigation.getMenu().findItem(R.id.nav_notifications));
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"hehe",Toast.LENGTH_SHORT).show();
            }
        });

        //为夜间模式 设置后面的选择滑动控件
        mNavigation.getMenu().findItem(R.id.night_model).setActionView(R.layout.action_view_switch);
        item = (SwitchCompat) mNavigation.getMenu().findItem(R.id.night_model).getActionView();
        item.setChecked(false);
        item.setOnCheckedChangeListener(this);

    }

    //初始化NavigationView
    private void setupDrawerContent(NavigationView mNavigation,final Toolbar mToolbar) {
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
 //                       getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new FragmentTwo()).commit();
                        break;
                    case R.id.nav_movies:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
 //                       getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new FragmentThree()).commit();
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
 //                       getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new FragmentFour()).commit();
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
 //                       getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new FragmentFive()).commit();
                        break;
                    case R.id.nav_about_us:
                        startActivity(new Intent(MainActivity.this,AboutUsActivity.class));
                        mDrawerLayout.closeDrawers();
//                        break;
                        return true;
                    case R.id.night_model:
                        break;
                    default:
  //                      getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new FragmentOne()).commit();
                        navItemIndex = 0;

                }
                //检查item状态，是否被选中
                if(item.isChecked()){
                    item.setChecked(false);
                }else{
                    item.setChecked(true);//点击了把它设为选中状态
                }
                item.setChecked(true);
                //setTitle(item.getTitle());
                loadHomeFragment();
                //mDrawerLayout.closeDrawers();//关闭抽屉
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    //在侧边栏弹出时，点击返回时关闭侧边栏
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }

        // 当用户按下返回时，如果当前页面不是FragmentOne，
        // 那么回到FragmentOne页面
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.action_clear_notifications) {
            FragmentFour four = new FragmentFour();
            four.setOnRecyclerItemClick(new FragmentFour.ClearCallBack() {
                @Override
                public void clearAll(List<FavoriteBean> favoriteBeanList) {
                    favoriteBeanList.clear();
                }
            });

            Toast.makeText(getApplicationContext(), "Clear all!", Toast.LENGTH_LONG).show();
        }

        if(id == R.id.action_mark_all_read){
            Toast.makeText(getApplicationContext(),"mark_all_read!",Toast.LENGTH_LONG).show();
        }

        if(id==R.id.action_search){
            item.setIntent(new Intent(MainActivity.this,SearchActivity.class));
        }

        if(id==R.id.action_notification){
            item.setIntent(new Intent(MainActivity.this,NotificationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 填充菜单，如果itemt存在，则把item添加到标题栏.

        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        //当fragment是notifications,则加载专门为其创建的menu
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==0){
                    Bundle bundle = data.getExtras();
                    String name = bundle.getString("username");
                    Toast.makeText(MainActivity.this,"Sucess!",Toast.LENGTH_SHORT).show();
                    showUser(name);
                }
        }
    }

    private void showUser(String name) {
        txtName.setText(name);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            mDayNightHelper.setMode(DayNight.DAY);
            setTheme(R.style.DayTheme);
            changeTheme();
            switchMode();
            item.setChecked(true);
            Toast.makeText(MainActivity.this,"hehe",Toast.LENGTH_SHORT).show();
        }else{
            mDayNightHelper.setMode(DayNight.NIGHT);
            setTheme(R.style.NightTheme);
            changeTheme();
            switchMode();
            item.setChecked(false);
        }
    }

    private void changeTheme() {
        if (mDayNightHelper.isDay()) {
            mDayNightHelper.setMode(DayNight.NIGHT);
            setTheme(R.style.NightTheme);
        } else {
            mDayNightHelper.setMode(DayNight.DAY);
            setTheme(R.style.DayTheme);
        }
    }

}
