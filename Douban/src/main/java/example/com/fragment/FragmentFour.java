package example.com.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import example.com.adapter.FavoriteAdapter;
import example.com.bean.FavoriteBean;
import example.com.custom.DetailToolbar;
import example.com.custom.DividerItemDecoration;
import example.com.doubandemo.DiaryDetailActivity;
import example.com.doubandemo.R;
import example.com.utils.ACache;
import example.com.utils.DatabaseHelper;
import example.com.utils.ToastUtils;

/**
 * Created by Administrator on 2017/4/9 0009.
 */
public class FragmentFour extends Fragment {
    private RecyclerView favoriteRecycler;
    private FavoriteAdapter favoriteAdapter;
    public List<FavoriteBean> favoriteBeanList = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    //接口
    public ClearCallBack mListener;//接口
    public void setOnRecyclerItemClick(ClearCallBack onRecyclerItemClick) {
        this.mListener = onRecyclerItemClick;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // 确保关联fragment的activity实现该接口，如果没有实现则抛出异常
            mListener = (ClearCallBack) activity;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_four,container,false);
        initData();
        initView(view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        favoriteAdapter = new FavoriteAdapter(getContext(),favoriteBeanList);
       // favoriteAdapter.notifyDataSetChanged();
        favoriteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteRecycler.setItemAnimator(new DefaultItemAnimator());
        favoriteRecycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        favoriteAdapter.notifyDataSetChanged();
        favoriteRecycler.setAdapter(favoriteAdapter);
        getDataFromSQLite();
        if(mListener!=null){
            if(favoriteBeanList!=null&&favoriteBeanList.size()>0){
                mListener.clearAll(favoriteBeanList);
            }
            else{
                Toast.makeText(getContext(),"Null Pointer",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initView(View view) {
        favoriteRecycler = (RecyclerView) view.findViewById(R.id.favorite_recycler);
    }

    private void initData() {
        databaseHelper = new DatabaseHelper(getContext());

//        JSONArray result = ACache.get(getContext()).getAsJSONArray(DiaryFragment.FAVORITE);
//
//        Type mType = new TypeToken<List<FavoriteBean>>(){}.getType();
//
//        List<> persons = GsonUtil.getGson().fromJson(result.toString(), mType);

//         FavoriteBean favorite = (FavoriteBean) ACache.get(getContext()).getAsObject(DiaryFragment.FAVORITE);
//       // System.out.print(favorite.toString());
//        favoriteBeanList.add(favorite);
//        Log.i("favoriteSize",favoriteBeanList.size()+"");
    }

    private void getDataFromSQLite() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                favoriteBeanList.clear();
                favoriteBeanList.addAll(databaseHelper.getAllUser());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                favoriteAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public interface ClearCallBack{
        void clearAll(List<FavoriteBean> favoriteBeanList);
    }
}
