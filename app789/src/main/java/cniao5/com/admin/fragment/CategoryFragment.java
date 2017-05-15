package cniao5.com.admin.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

import cniao5.com.admin.CategoryDetailActivity;
import cniao5.com.admin.Contants;
import cniao5.com.admin.WareListActivity;
import cniao5.com.admin.adapter.BaseAdapter;
import cniao5.com.admin.adapter.CategoryAdapter;
import cniao5.com.admin.adapter.WaresAdapter;
import cniao5.com.admin.bean.Category;
import cniao5.com.admin.bean.Page;
import cniao5.com.admin.bean.Ware;
import cniao5.com.admin.custom.DividerItemDecoration;
import cniao5.com.admin.http.BaseCallback;
import cniao5.com.admin.http.OkHttpHelper;
import cniao5.com.admin.http.SpotsCallBack;
import cniao5.com.cniao5shop.R;

public class CategoryFragment extends Fragment {
    private RecyclerView categoryRecycler,wareRecycler;
    private MaterialRefreshLayout materialRefreshLayout;

    private CategoryAdapter categoryAdapter;
    private WaresAdapter waresAdapter;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    private int currentPage =1;
    private int totalPage =1;
    private int pageSize = 10;
    private long category_id = 0;

    //刷新状态
    private static final int REFRESH_PULL = 0;
    private static final int REFRESH_NORMAL = 1;
    private static final int REFRESH_LOAD = 2;

    private int currentState = REFRESH_NORMAL;

    private  String wareListUrl = "https://market.douban.com/book/?platform=web&channel=book_nav";//详情数据链接

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category,container,false);
        categoryRecycler = (RecyclerView) view.findViewById(R.id.category_recyclerview);
        wareRecycler = (RecyclerView) view.findViewById(R.id.wares_recyclerview);
        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_layout);

        requestCategoryData();

        initRefreshLayout();
        return  view;
    }





    private  void initRefreshLayout(){

        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                refreshData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if(currentPage <=totalPage)
                    loadMoreData();
                else{
//                    Toast.makeText()
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }


    private  void refreshData(){

        currentPage =1;

        currentState=REFRESH_PULL;
        requestWares(category_id);

    }

    private void loadMoreData(){

        currentPage = ++currentPage;
        currentState = REFRESH_LOAD;
        requestWares(category_id);

    }


    private  void requestCategoryData(){

        httpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {

                showCategoryData(categories);

                if(categories !=null && categories.size()>0)
                    category_id = categories.get(0).getId();
                requestWares(category_id);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private  void showCategoryData(List<Category> categories){

        categoryAdapter = new CategoryAdapter(getContext(),categories);

       categoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Category category = categoryAdapter.getItem(position);

                category_id = category.getId();
                currentPage=1;
                currentState=REFRESH_NORMAL;

                requestWares(category_id);

            }
        });

        categoryRecycler.setAdapter(categoryAdapter);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryRecycler.setItemAnimator(new DefaultItemAnimator());
        categoryRecycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

    }



    private void requestWares(long categoryId){

        String url = Contants.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+currentPage+"&pageSize="+pageSize;

        httpHelper.get(url, new BaseCallback<Page<Ware>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, Page<Ware> waresPage) {

                currentPage = waresPage.getCurrentPage();
                totalPage =waresPage.getTotalPage();

                showWaresData(waresPage.getList());

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }


    private  void showWaresData(List<Ware> wares){

        switch (currentState){

            case  REFRESH_NORMAL:

                if(waresAdapter ==null) {



                    waresAdapter = new WaresAdapter(getContext(), wares);


                    wareRecycler.setAdapter(waresAdapter);

                    wareRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    wareRecycler.setItemAnimator(new DefaultItemAnimator());
                    wareRecycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

                    waresAdapter.setOnCampaignClickListener(new WaresAdapter.OnCampaignClickListener() {
                        @Override
                        public void onClick(Ware ware) {
                            String url = "https://market.douban.com/book/talese?region=zhishu_collect_books_list&location=1";
                            ware.setDetailHrefUrl(url);
                            Intent i = CategoryDetailActivity
                                    .newIntent(getActivity(), Uri.parse(ware.getDetailHrefUrl()));
                            startActivity(i);
                        }

                    });
                }
                else{
                    waresAdapter.clear();
                    waresAdapter.addData(wares);
                }

                break;

            case REFRESH_PULL:
                waresAdapter.clear();
                waresAdapter.addData(wares);

                wareRecycler.scrollToPosition(0);
                materialRefreshLayout.finishRefresh();
                break;

            case REFRESH_LOAD:
                waresAdapter.addData(waresAdapter.getDatas().size(),wares);
                wareRecycler.scrollToPosition(waresAdapter.getDatas().size());
                materialRefreshLayout.finishRefreshLoadMore();
                break;
        }

    }

}



