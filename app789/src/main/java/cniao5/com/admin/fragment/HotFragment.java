package cniao5.com.admin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

import cniao5.com.admin.Contants;
import cniao5.com.admin.adapter.HotWareAdapter;
import cniao5.com.admin.bean.Page;
import cniao5.com.admin.bean.Ware;
import cniao5.com.admin.custom.DividerItemDecoration;
import cniao5.com.admin.http.BaseCallback;
import cniao5.com.admin.http.OkHttpHelper;
import cniao5.com.cniao5shop.R;

/**
 * description:热卖商品
 *create by administration
 */
public class HotFragment extends Fragment{
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private int currentPage = 1;
    private int totalPage =1;
    private int pageSize = 10;

    private List<Ware> mWares;
    private MaterialRefreshLayout materialRefreshLayout;
    private RecyclerView recyclerView;
    private HotWareAdapter hotWareAdapter;

    private static final int STATE_PULL = 0;//下拉状态
    private static final int STATE_NORMAL = 1;//空闲状态
    private static final int STATE_LOAD = 2;//上拉状态
    private int currentState = STATE_NORMAL;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_hot,container,false);
        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        initRefreshLayout();
        getData();

        return view ;

    }

    private void getData() {
        String url = Contants.API.HOT_WARE+"?curPage="+currentPage+"&pageSize="+pageSize;
        okHttpHelper.get(url, new BaseCallback<Page<Ware>>() {
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
            public void onSuccess(Response response, Page<Ware> warePage) {
                mWares = warePage.getList();
                currentPage = warePage.getCurrentPage();
                totalPage = warePage.getTotalPage();
                Log.d("totalPage","totoalPage : "+totalPage);

                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showData() {
        switch (currentState){
            case STATE_PULL:
                Toast.makeText(getContext(),"这是第一页哦",Toast.LENGTH_SHORT).show();
                hotWareAdapter.clearData();
                hotWareAdapter.addData(mWares);

                recyclerView.scrollToPosition(0);
                materialRefreshLayout.finishRefresh();
                break;

            case STATE_NORMAL:
                hotWareAdapter = new HotWareAdapter(getContext(),mWares);
                recyclerView.setAdapter(hotWareAdapter);

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
                break;

            case STATE_LOAD:
                hotWareAdapter.addData(hotWareAdapter.getDatas().size(),mWares);
                recyclerView.scrollToPosition(mWares.size());
                materialRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    /*
     * 初始化上拉下拉控件
     */
    private void initRefreshLayout() {
        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                onPullRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(totalPage<=currentPage&&currentPage<10){
                    onLoadMore();
                }else{
                    Toast.makeText(getContext(),"这已经是最后一页啦",Toast.LENGTH_SHORT).show();
                    materialRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    //上拉加载
    private void onLoadMore() {
        ++currentPage;
        currentState = STATE_LOAD;
        getData();
    }

    //下拉刷新
    private void onPullRefresh() {
        currentPage = 1;
        currentState = STATE_PULL;
        getData();
    }


}
