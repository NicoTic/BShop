package example.com.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import example.com.adapter.MovieRecyclerAdapter;
import example.com.bean.MovieHeadBean;
import example.com.bean.RecentMovieItem;
import example.com.bean.TodayArticleItem;
import example.com.custom.GolleryViewpager;
import example.com.custom.MovieHeadAdapter;
import example.com.custom.ScalePageTransformer;
import example.com.custom.ZoomOutPageTransformer;
import example.com.doubandemo.R;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class FragmentThree extends Fragment {
    private GolleryViewpager golleryViewpager;
    private MovieHeadAdapter movieHeadAdapter;//

    private RecyclerView movieRecyclerView;//列表
    private MovieRecyclerAdapter movieAdapter;//列表适配器
    private String TAG = "FragmentThree";

    /**
     * 头布局 图片数据源
     */
    private String[] imgUrls = new String[]{
            "https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p480747492.webp",
            "https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p2456198602.webp",
            "https://img1.doubanio.com/view/movie_poster_cover/lpst/public/p2399174377.webp",
            "https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p2444256500.webp"
    };
    /**
     * 头布局 电影分类数据源
     */
    private int[] movieCates = new int[]{
            R.string.top,
            R.string.weekly_parise,
            R.string.new_movie,
            R.string.ticket
    };
    /**
     * 头布局 电影描述数据源
     */
    private int[] cateDescripe = new int[]{
            R.string.top_descrip,
            R.string.week_parise_descrip,
            R.string.new_movie_descrip,
            R.string.ticket_office_descrip
    };

    private ArrayList<RecentMovieItem> movieList = new ArrayList<>();//recyceler列表 数据源
    private RecyclerView.LayoutManager mLayoutManager;//列表布局管理器

    private String recentMovieUrl = "https://www.douban.com/doulist/35918697/";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // initRecyclerViewData();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_three,container,false);
        initView(view);
        new RecentMovieTask().execute();
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void initView(View view) {
        movieRecyclerView = (RecyclerView) view.findViewById(R.id.movies_Recycler);
        Log.i(TAG,"size :"+movieList.size()+"");
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAdapter(movieList);
    }

    /**
     * 设置Adapter
     */
    private void setupAdapter(ArrayList<RecentMovieItem> movieList) {
        movieAdapter = new MovieRecyclerAdapter(getContext(),movieList);
        Log.d(TAG,"dia:   " +movieList.size());
        movieRecyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
        setHeader(movieRecyclerView);
    }

    /**
     * 为recyclerView设置头布局（viewPager 画廊效果）
     * @param movieRecyclerView
     */
    private void setHeader(RecyclerView movieRecyclerView) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.movie_head_layout, movieRecyclerView, false);
        movieAdapter.setHeaderView(header);

        golleryViewpager = (GolleryViewpager) header.findViewById(R.id.view_pager);
        golleryViewpager.setPageTransformer(true, new ZoomOutPageTransformer());

       header.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return golleryViewpager.dispatchTouchEvent(motionEvent);
            }
        });
        List<View> views = new ArrayList<>();
        views.add(header);

        movieHeadAdapter = new MovieHeadAdapter(getContext(),views);
        golleryViewpager.setAdapter(movieHeadAdapter);
        initData();
        golleryViewpager.setCurrentItem(1);
    }

    /**
     * 为头布局设置数据源/填充数据
     */
    private void initData() {
        List<MovieHeadBean> mDatas = new ArrayList<>();
        for(int i = 0;i<imgUrls.length;i++){
            MovieHeadBean moviebean = new MovieHeadBean();
            moviebean.setImagUrl(imgUrls[i]);
            moviebean.setCategortTitle(movieCates[i]);
            moviebean.setDescription(cateDescripe[i]);
            mDatas.add(moviebean);
        }

        //设置OffscreenPageLimit
       golleryViewpager.setOffscreenPageLimit(mDatas.size());
        movieHeadAdapter.addAll(mDatas);
    }


    public class RecentMovieTask extends AsyncTask<Void,String,List<RecentMovieItem>>{

        @Override
        protected List<RecentMovieItem> doInBackground(Void... voids) {
            List<RecentMovieItem> list = new ArrayList<>();
            try{

                Connection conn = Jsoup.connect(recentMovieUrl);

                Document doc = conn
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                        .timeout(5000).get();

                Element allElement = doc.select("div.article").first();//找出第一个content节点

                Elements itemElements = allElement.select("div.doulist-subject");

                Elements titleElements = itemElements.select("div.doulist-subject div.title");//Title　标题列表
                Log.i("movie_title",titleElements.text());

                Elements contentElements = itemElements.select("div.abstract");// 电影导演、演员等描述列表
                Log.i("movie_description",contentElements.text());

                Elements imgElements = itemElements.select("div.doulist-subject div.post a");//图片及详情链接
                Log.i("movie_imgElement",imgElements.toString());

                for(int i=0;i<imgElements.size();i++){
                    RecentMovieItem recentMovieItem = new RecentMovieItem();

                       /*
                        * 解析图片以及详情链接
                        */
                    Element imgElement = itemElements.select("div.post").get(i);
                    String imgUrl = imgElement.select("a").select("img").attr("src");//图片列表

                    recentMovieItem.setMovieImageUrl(imgUrl);
                    Log.i(TAG,"movie_img "+imgUrl);

                    String hrefUrl = imgElement.select("a").attr("href");//链接列表
                   recentMovieItem.setWebViewUrl(hrefUrl);
                    Log.i(TAG,"movie_hrefUrl: "+ hrefUrl);
                       /*
                        * 解析电影标题
                        */
                    String title = titleElements.get(i).text();
                    recentMovieItem.setMovieTitle(title);
                    Log.i(TAG,"movie_title"+ title);

                       /*
                        * 解析电影的描述
                        */
                        String e1 = contentElements.get(i).text();//获得所有的电影描述（导演、演员等）
                        String finalStr1 = e1.replace(" / ","/");//去掉"/"之间的空格
                        String finalStr2 = finalStr1.replace(": ",":");//再去掉": "之后的空格
                        String finalString = finalStr2.replace(" ","\n");//遇空格换行
                        recentMovieItem.setMovieDirector(finalString);
                        Log.i("text",finalString);

                    list.add(recentMovieItem);

                }

                return list;

            }catch (IOException i){
                i.printStackTrace();
                Log.i("error",i.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<RecentMovieItem> recentMovieItems) {
            super.onPostExecute(recentMovieItems);
            if (movieList != null){
                movieList.clear();
                movieList.addAll(recentMovieItems);
            }
            Log.i(TAG,"movieList size"+movieList.size());

            movieAdapter.notifyDataSetChanged();
        }
    }

}
