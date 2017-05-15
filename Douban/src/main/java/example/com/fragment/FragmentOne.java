package example.com.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import example.com.adapter.TodayDiaryAdapter;
import example.com.bean.TodayArticleItem;
import example.com.custom.DividerItemDecoration;
import example.com.doubandemo.DiaryDetailActivity;
import example.com.doubandemo.R;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class FragmentOne extends Fragment {
    private Context mContext;
    private RecyclerView todayRecyclerView;//列表
    private TodayDiaryAdapter myAdapter;//适配器
    private List<TodayArticleItem> diaryList = new ArrayList<>();//数据源
    private String TAG = "FragmentOne";
    private  String todayDiaryUrl = "https://www.douban.com/explore/";//链接

    private String diaryUrl = "https://www.douban.com/search?cat=1015&q=日记";//日记链接 用于爬取日记id获得评论

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View todayView = inflater.inflate(R.layout.fragment_one,container,false);
        initView(todayView);
        new TutoAndroidFranceTask().execute();

        return todayView;
    }


    /**
     * 初始化控件
     */

    private void initView(View todayView) {
        todayRecyclerView = (RecyclerView) todayView.findViewById(R.id.today_articleRecycler);
        Log.i(TAG,"size :"+diaryList.size()+"");
        todayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setupAdapter(diaryList);

        // 监听事件
        myAdapter.setOnItemClickListener(new TodayDiaryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TodayArticleItem data) {
                Log.i(TAG,"onclick!!");
                Intent i = DiaryDetailActivity
                        .newIntent(getActivity(), Uri.parse(data.getWebViewUrl()),data.getArticleImageUrl(),data.getArticleTitle(),data.getArticleContent());
                startActivity(i);
            }
        });
    }

    /**
     * 设置Adapter
     */
    public void setupAdapter(List<TodayArticleItem> diaryList){
        myAdapter = new TodayDiaryAdapter(getContext(),diaryList);
        Log.d(TAG,"dia:   " +diaryList.size());
        todayRecyclerView.setAdapter(myAdapter);
        todayRecyclerView.setItemAnimator(new DefaultItemAnimator());
        todayRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        myAdapter.notifyDataSetChanged();
        setHeader(todayRecyclerView);
    }

    /**
     * 为列表设置头布局
     * @param todayRecyclerView
     */
    private void setHeader(RecyclerView todayRecyclerView) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_layout, todayRecyclerView, false);
        myAdapter.setHeaderView(header);
        TextView dateText = (TextView) header.findViewById(R.id.date);
        dateText.setText(setDate());
    }

    /**
     * 为头布局控件设置数据
     * @return
     */
    private String setDate() {
        Date date = new Date();
        return (String) DateFormat.format("MMM dd日",date);
    }

    /**
     * 使用AsyncTask后台线程解析网页获取数据
     */
    public class TutoAndroidFranceTask extends AsyncTask<Void,String,List<TodayArticleItem>> {
        @Override
        protected List<TodayArticleItem> doInBackground(Void... voids) {
            List<TodayArticleItem> list = new ArrayList<>();
            try{

                Connection conn = Jsoup.connect(todayDiaryUrl);

                Document doc = conn
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                        .timeout(5000).get();

                Element allElement = doc.select("div.article").first();//找出第一个article节点

                Elements itemElements = allElement.select("div.bd");//找出article节点下所有的bd节点

                Elements titleElements = itemElements.select("div.bd div.title");//获得 Title　标题列表

                Elements contentElements = itemElements.select("div.bd  p");// 获得摘要列表

                Elements imgElements = itemElements.select("div.bd div.pic a");//获得所有 图片及链接 数据

                for(int i=0;i<imgElements.size();i++){
                    TodayArticleItem diaryuItem = new TodayArticleItem();

                       /*
                        * 解析图片以及文章链接
                        */
                    Element imgElement = imgElements.get(i);
                    String imgStyle = imgElement.attr("style");
                    if(imgStyle.indexOf("background-image:url")!=-1){
                        String imgUrl = imgStyle.substring(21,imgStyle.length()-1);//图片列表
                        diaryuItem.setArticleImageUrl(imgUrl);
                        Log.i(TAG,"img: "+imgUrl);
                    }
                    String hrefUrl = imgElement.attr("href");//链接列表
                    diaryuItem.setWebViewUrl(hrefUrl);
                    Log.i(TAG,"href: "+ hrefUrl);
                       /*
                        * 解析文章标题
                        */
                    String title = titleElements.get(i).text();
                    diaryuItem.setArticleTitle(title);
                    Log.i(TAG,"title: "+ title);

                       /*
                        * 解析文章摘要
                        */
                    String content = contentElements.get(i).text();
                    diaryuItem.setArticleContent(content);
                    Log.i(TAG,"content: "+ content);

                    String id = "153039845";
                    diaryuItem.setId(id);

                    list.add(diaryuItem);

                }
                Log.i("hihi","list size"+ list.size());
                return list;

            }catch (IOException i){
                i.printStackTrace();
                Log.i("error",i.toString());
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<TodayArticleItem> todayArticleItems) {
            super.onPostExecute(todayArticleItems);
                if (diaryList != null){
                    diaryList.clear();
                    diaryList.addAll(todayArticleItems);
                }
                Log.i(TAG,"diaryList size"+diaryList.size());

              myAdapter.notifyDataSetChanged();
        }
    }


}
