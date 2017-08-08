package com.example.alipay.myreadingapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.alipay.myreadingapplication.R;
import com.example.alipay.myreadingapplication.activity.BookDetailActivity;
import com.example.alipay.myreadingapplication.adapter.BookListAdapter;
import com.example.alipay.myreadingapplication.model.BookItemModel;
import com.example.alipay.myreadingapplication.other.Constant;
import com.example.alipay.myreadingapplication.service.PullService;
import com.example.alipay.myreadingapplication.util.ACache;
import com.example.alipay.myreadingapplication.util.BaseCallback;
import com.example.alipay.myreadingapplication.util.MyOkhttpHelper;
import com.example.alipay.myreadingapplication.util.PromptManager;
import com.example.alipay.myreadingapplication.util.QueryPrefrences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;



/**
 * Created by Jxq .
 * Description:图书展示界面
 * Date:on 2017/7/10.
 */
public class FragmentOne extends VisibleFragment {

    private String TAG = "FragmentOne";
    public static String ACACHE_SEARCH_QUERY = "searchQuery";
    private ACache aCache;//存储器

    private MyOkhttpHelper myOkhttpHelper = MyOkhttpHelper.getInstance();
    private int start=0;//开始：第一页
    private int totalCount = 1;//总数
    private int count = 20;//第一页显示的数目
    private String tag = "World";//标签

    private static final int STATE_REFRESH_PULL = 0;//下拉刷新
    private static final int STATE_REFRESH_NORMAL = 1;//空闲状态
    private static final int STATE_REFRESH_LOAD = 2;//上拉加载
    private int currentState = STATE_REFRESH_NORMAL;

    private Context mContext;
    private RecyclerView bookRecyclerView;//列表
    private MaterialRefreshLayout materialRefreshLayout;//刷新控件

   private BookListAdapter bookAdapter;//适配器
    private List<BookItemModel.BooksBean> booksList = new ArrayList<>();//数据源

    private MaterialDialog materialDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        //aCache = ACache.get(getActivity());
//        Intent i = PullService.newIntent(getActivity());
//        getActivity().startService(i);
//        PullService.setServiceAlarm(getContext(),true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View bookView = inflater.inflate(R.layout.fragment_one, container, false);
        initView(bookView);
        initRefreshLayout();
        initRecyclerView(tag);
        return bookView;
    }

    private void initRefreshLayout() {
        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                onPullRecycler();
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

    /**
     * 上拉加载
     */
    private void onLoadMore() {
        start = start+20;
        currentState = STATE_REFRESH_LOAD;
        initRecyclerView(tag);
    }
    /**
     * 下拉刷新
     */
    private void onPullRecycler() {
        start=0;
        count=20;
        currentState=STATE_REFRESH_PULL;
        initRecyclerView(tag);
    }

    public void initRecyclerView(String tag) {
        String doubanBookUrl = Constant.API.DOUBAN_BOOK;
        myOkhttpHelper.get(doubanBookUrl, getParams(tag), new BaseCallback<BookItemModel>() {
            @Override
            public void onBeforeRequest(Request request) {
                materialDialog = PromptManager.showIndeterminateProgressDialog(getActivity(), getString(R.string.loading_tip));
            }

            @Override
            public void onFailure(Request request, Exception e) {
                PromptManager.hideMaterialLoadView(materialDialog);
                Toast.makeText(getActivity(),"it doesn't work!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Response response) {
                PromptManager.hideMaterialLoadView(materialDialog);
            }

            @Override
            public void onSuccess(okhttp3.Response response, BookItemModel bookItemModel) {
                PromptManager.hideMaterialLoadView(materialDialog);
                totalCount = bookItemModel.getTotal();
                start = bookItemModel.getStart();
                count = bookItemModel.getCount();
                booksList = bookItemModel.getBooks();
                requestRecyclerData();
            }

            @Override
            public void onError(okhttp3.Response response, int code, Exception e) {

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
                bookRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                bookAdapter = new BookListAdapter(booksList,getContext());

                bookRecyclerView.setAdapter(bookAdapter);
                bookRecyclerView.setItemAnimator(new DefaultItemAnimator());
                bookAdapter.setOnItemClickListener(new BookListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, BookItemModel.BooksBean item) {
                        Intent i = BookDetailActivity.newIntent(getContext(), Uri.parse(item.getAlt()),
                                item.getPages(),item.getTitle(),item.getAuthor().get(0),position);
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

    private Map<String,Object> getParams(String tagText) {
        Map<String,Object> params = new HashMap<>();
        params.put("tag",tagText);
        params.put("start",start);
        params.put("count",count);
        return params;
    }

    private void initView(View bookView) {
        bookRecyclerView = (RecyclerView) bookView.findViewById(R.id.book_recyclerView);
        materialRefreshLayout = (MaterialRefreshLayout) bookView.findViewById(R.id.refresh_view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("tag",query);
              //  aCache.put(ACACHE_SEARCH_QUERY,query);
                QueryPrefrences.setStoreQuery(getContext(),query);
                tag = query;
                searchView.onActionViewCollapsed();
                currentState = STATE_REFRESH_NORMAL;
                initRecyclerView(tag);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("tag_changed",newText);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tag = aCache.getAsString(ACACHE_SEARCH_QUERY);
                tag = QueryPrefrences.getStoresQuery(getContext());
                searchView.setQuery(tag,false);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_clear:
              //  aCache.put(ACACHE_SEARCH_QUERY,"");
                QueryPrefrences.setStoreQuery(getContext(),"");
                initRecyclerView(tag);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void hideInputManager(Activity mActivity) {
        try {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = mActivity.getCurrentFocus();
            if (null != view) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
        }
    }
}
