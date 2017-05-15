package example.com.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

import example.com.adapter.HotBookAdapter;
import example.com.bean.Banner;
import example.com.bean.BookItem;
import example.com.bean.BookListBean;
import example.com.doubandemo.BannerDetailActivity;
import example.com.doubandemo.BookDetailActivity;
import example.com.doubandemo.DiaryDetailActivity;
import example.com.doubandemo.R;
import example.com.http.OkHttpHelper;
import example.com.http.SpotsCallBack;
import example.com.other.Constant;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class FragmentTwo extends Fragment{
    private int start=0;//开始：第一页
    private int totalCount = 1;//总数
    private int count = 20;//第一页显示的数目

    private static final int STATE_REFRESH_PULL = 0;//下拉刷新
    private static final int STATE_REFRESH_NORMAL = 1;//空闲状态
    private static final int STATE_REFRESH_LOAD = 2;//上拉加载
    private int currentState = STATE_REFRESH_NORMAL;

    private SliderLayout sliderLayout;//轮播控件
    private RecyclerView bookRecyclerView;//列表
    private PagerIndicator indicator;//轮播指示器
    private MaterialRefreshLayout materialRefreshLayout;//刷新控件

    private HotBookAdapter bookAdapter;

    private static final String TAG = "BookFragment";

    private Gson mGson = new Gson();

    private List<Banner> banners = new ArrayList<>();

    private List<BookListBean.BooksBean> booksList = new ArrayList<>();

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private String[] imgUrls = new String[]{
            "https://img3.doubanio.com/view/ark_campaign_pic/large/public/4144.jpg",
            "https://img1.doubanio.com/view/ark_campaign_pic/large/public/4139.jpg",
            "https://img1.doubanio.com/view/ark_campaign_pic/large/public/4138.jpg",
            "https://img3.doubanio.com/view/ark_campaign_pic/large/public/4143.jpg",
            "https://img3.doubanio.com/view/ark_campaign_pic/large/public/4135.jpg",
            "https://img1.doubanio.com/view/ark_attachment/raw/public/b5ae0e5d0fcc484babf12b33a9276feb.jpg"
    };

    private String[] bannerDetailUrls = new String[]{
            "https://read.douban.com/topic/1037/?ici=%E5%A5%97%E8%A3%85%E4%B9%A6&amp;icn=index-banner",
            "https://read.douban.com/topic/988/?ici=%E7%8E%8B%E5%B0%8F%E6%B3%A2&amp;icn=index-banner",
            "https://read.douban.com/ebook/32890883/?ici=%E6%81%8B%E6%83%85%E7%9A%84%E7%BB%88%E7%BB%93&amp;icn=index-banner",
            "https://read.douban.com/topic/1028/?ici=%E5%9B%9B%E6%9C%88%E5%8E%9F%E7%89%88%E7%88%B1%E6%83%85&amp;icn=index-banner",
            "https://read.douban.com/competition/2016/?ici=index-banner&amp;icn=index-banner",
            "https://read.douban.com/event/world_book_day_2017?icn=global-banner"

    };

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_two,container,false);
        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        bookRecyclerView = (RecyclerView) view.findViewById(R.id.books_Recycler);
        indicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);
        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_view);

        initImgData();

        initSlideLayout();
        initRecyclerView();


        initRefeshLayout();

        return view;
    }

    private void initRefeshLayout() {
        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                onPullRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if(start<totalCount){
                    onLoadMore();
                }else{
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });


    }

    /*
     * 上拉加载
     */
    private void onLoadMore() {
        start = start+20;
        currentState = STATE_REFRESH_LOAD;
        initRecyclerView();
    }

    /*
     * 下拉刷新
     */
    private void onPullRefresh() {
        start=0;
        count=20;
        currentState=STATE_REFRESH_PULL;
       initRecyclerView();
    }

    private void initSlideLayout() {
        if(banners!=null&&banners.size()>0){
            for(final Banner banner:banners){

                DefaultSliderView defaultSlideView = new DefaultSliderView(getActivity());
                defaultSlideView.image(banner.getImgUrl());
                defaultSlideView.setScaleType(BaseSliderView.ScaleType.Fit);
               defaultSlideView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                   @Override
                   public void onSliderClick(BaseSliderView slider) {
                      // Toast.makeText(getActivity(), banner.getImgDetailUrl(), Toast.LENGTH_SHORT).show();
                        Intent i = BannerDetailActivity.newIntent(getContext(),Uri.parse(banner.getImgDetailUrl()));
                       startActivity(i);
                   }
               });
                sliderLayout.addSlider(defaultSlideView);
            }
        }

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setDuration(3000);
        sliderLayout.setCustomIndicator(indicator);

    }

    private void initImgData() {
        for(int i =0;i<imgUrls.length;i++){
            Banner  banner = new Banner();
            banner.setImgUrl(imgUrls[i]);
            banner.setImgDetailUrl(bannerDetailUrls[i]);
            banners.add(banner);
        }

    }

    private void initRecyclerView() {
        String doubanBookUrl = Constant.API.DOUBAN_BOOK+"&start="+start+"&count="+count;
        okHttpHelper.get(doubanBookUrl,new SpotsCallBack<BookListBean>(getContext()){
            @Override
            public void onSuccess(Response response, BookListBean bookListBean) {
                super.onSuccess(response, bookListBean);
                totalCount = bookListBean.getTotal();
                start = bookListBean.getStart();

                booksList = bookListBean.getBooks();
                requestRecyclerData();
                Log.i("sucessa",bookListBean.getBooks().get(0).getTitle());
            }

            @Override
            public void onResponse(Response response) {
                super.onResponse(response);
                Log.i("response","onResponse");
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                Log.i("failure","onFailure");
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                super.onError(response, code, e);
                Log.i("error","onError");
            }
        });
    }



    private void requestRecyclerData() {
        switch (currentState){
            case STATE_REFRESH_PULL:
                Toast.makeText(getContext(),"这是第一页哦",Toast.LENGTH_SHORT).show();
                bookAdapter.clearData();
                bookAdapter.addData(booksList);

                bookRecyclerView.scrollToPosition(0);
                materialRefreshLayout.finishRefresh();
                break;
            case STATE_REFRESH_NORMAL:
                bookRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                bookAdapter = new HotBookAdapter(booksList,getContext());

                bookRecyclerView.setAdapter(bookAdapter);
                bookRecyclerView.setItemAnimator(new DefaultItemAnimator());

                bookAdapter.setOnItemClickListener(new HotBookAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, BookListBean.BooksBean item) {
                        //Intent i = new Intent(getContext(), BookDetailActivity.class);
                        Intent i = BookDetailActivity.newIntent(getContext(),Uri.parse(item.getAlt()));
                        startActivity(i);
                    }
                });
                break;
            case STATE_REFRESH_LOAD:
                bookAdapter.addData(bookAdapter.getDatas().size(),booksList);
                bookRecyclerView.scrollToPosition(bookAdapter.getDatas().size());
                materialRefreshLayout.finishRefreshLoadMore();
                break;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sliderLayout.stopAutoCycle();
    }
}
