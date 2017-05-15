package cniao5.com.admin;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cniao5.com.admin.adapter.WareListAdapter;
import cniao5.com.admin.bean.Ware;
import cniao5.com.admin.bean.WareListItemBean;
import cniao5.com.admin.widget.CnToolbar;
import cniao5.com.cniao5shop.R;

/**
 * Created by Administration on 2017/4/25.
 */
public class WareListActivity extends AppCompatActivity{
    private static final String TAG = "WareListActivity";
    private CnToolbar cnToolbar;
    private RecyclerView recyclerView;
    private Context mContext;
    private WareListAdapter wareListAdapter;
    private List<Ware> wareList = new ArrayList<>();//数据源
    private  String wareListUrl = "https://market.douban.com/book/?platform=web&channel=book_nav&page=2&page_num=20";//链接
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ware_list_layout);
        initView();
        initToolbar();
        new TutoAndroidFranceTask().execute();
    }

    private void initToolbar() {
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.ware_list_recycler_view);
       cnToolbar = (CnToolbar) findViewById(R.id.toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter(wareList);

    }

    /**
     * 设置Adapter
     */
    private void setupAdapter(List<Ware> wareList) {
        wareListAdapter = new WareListAdapter(this,wareList);
        Log.d(TAG,"dia: " + wareList.size());
        recyclerView.setAdapter(wareListAdapter);
        wareListAdapter.notifyDataSetChanged();
    }

    /**
     * 使用AsyncTask后台线程解析网页获取数据
     */
    public class TutoAndroidFranceTask extends AsyncTask<Void,String,List<Ware>> {
        @Override
        protected List<Ware> doInBackground(Void... voids) {
            List<Ware> list = new ArrayList<>();
            try{

                Connection conn = Jsoup.connect(wareListUrl);

                Document doc = conn
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
                        .timeout(5000).get();

                Element allElement = doc.select("div.books-wrap").first();//找出第一个article节点
                Elements itemElements = allElement.select("div.book-brief");
                Elements titleElements = itemElements.select("h3");
                Elements priceElements = allElement.select("span.book-price");
                Elements imgElements = allElement.select("div.bookface-img").select("img");
                Elements hrefElements = allElement.select("li.book-item").select("a");

                for(int i = 0;i<imgElements.size();i++){
                    Ware ware = new Ware();
                    Element titleElement = titleElements.get(i);
                    ware.setName(titleElement.text());
                    Element priceElement = priceElements.get(i);
                    String price = priceElement.text();
                    String realPrice = price.replace("¥","");

                    ware.setPrice(Float.valueOf(realPrice));
                    Log.i("price",realPrice);

                    Element imgElement  = imgElements.get(i);
                    String imgUrls = imgElement.attr("src");
                    ware.setImgUrl(imgUrls);

                    Element hrefElement = hrefElements.get(i);
                    String hrefUrls = hrefElement.attr("href");
                    ware.setDetailHrefUrl(hrefUrls);

                    list.add(ware);

                    Log.i("yy",hrefUrls);
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
        protected void onPostExecute(List<Ware> wareListbeanItems) {
            super.onPostExecute(wareListbeanItems);
            if (wareList != null){
                wareList.clear();
                wareList.addAll(wareListbeanItems);
            }
            Log.i(TAG,"diaryList size"+ wareList.size());

            wareListAdapter.notifyDataSetChanged();
        }
    }

}
