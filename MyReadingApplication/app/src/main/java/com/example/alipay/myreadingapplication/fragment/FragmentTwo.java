package com.example.alipay.myreadingapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.alipay.myreadingapplication.R;
import com.example.alipay.myreadingapplication.activity.SDTxtListActivity;
import com.example.alipay.myreadingapplication.adapter.BooksAdapter;
import com.example.alipay.myreadingapplication.model.Book;
import com.example.alipay.myreadingapplication.model.FileMsgModel;
import com.example.alipay.myreadingapplication.realm.RealmController;
import com.example.alipay.myreadingapplication.util.Prefs;
import com.example.alipay.myreadingapplication.util.PromptManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by Jxq .
 * Description:“想读图书”的界面
 * Date:on 2017/7/10.
 */
public class FragmentTwo extends Fragment {
    private AlertDialog dialog;;
    private BooksAdapter adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private RealmRecyclerView recycler;
    private RealmResults<Book> bookList;
    private String mExternalStoragePath;
    private ArrayList<FileMsgModel> lists = new ArrayList<FileMsgModel>();
    private MaterialDialog materialDialog;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2:
                    PromptManager.hideMaterialLoadView(materialDialog);
                    Log.i("file",lists.size()+"");
                    Intent txtIntent = new Intent();
                    txtIntent.putExtra("data", lists);
                    txtIntent.setClass(getActivity(), SDTxtListActivity.class);
                    startActivityForResult(txtIntent,2);
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            List<FileMsgModel> txtFiles = (List<FileMsgModel>) data.getSerializableExtra("MESSAGE");
            List<Book> books = new ArrayList<>();

            for(int i = 0;i<txtFiles.size();i++){
                Book book = new Book();
                FileMsgModel fsm = txtFiles.get(i);
                book.setTitle(fsm.getFileName());
                book.setPages(String.valueOf(Integer.parseInt(fsm.getFileSize())/4*4096/1000));
                book.setAuthor("佚名");
                book.setImageUrl("https://img1.doubanio.com/lpic/s1790028.jpg");
                books.add(book);
            }
            RealmController.addAll(books);
            setupRecycler();
        }
    }
    public File getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取根目录
        }
        return sdDir;

    }

    public void scanFile(File rootPath, final String[] filterNames) {
        for (int i = 0; i < filterNames.length; i++) {
            scanFile(rootPath, filterNames[i]);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RealmController.initializeRealmConfig(getActivity().getApplicationContext());
        setHasOptionsMenu(true);
        setRetainInstance(true);
        realm = RealmController.getRealm();
        if(savedInstanceState == null) {
            Toast.makeText(getContext(), "Press card item for edit, long press to remove item", Toast.LENGTH_LONG).show();
        }
        Log.i("oncreate","onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View wantReadView = inflater.inflate(R.layout.fragment_wanted_read_layout, container, false);
        initView(wantReadView);
        Log.i("onCreateView","onCreateView");
        return wantReadView;
    }

    private void initView(View wantReadView) {
        recycler = (RealmRecyclerView) wantReadView.findViewById(R.id.want_read_recycler);
        recycler.setHasTransientState(true);
        setupRecycler();
    }

    private void setupRecycler() {
        RealmResults<Book> notes = realm.where(Book.class).findAll();
        adapter = new BooksAdapter(getContext(), notes);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.want_read_add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mExternalStoragePath = Environment.getExternalStorageDirectory().toString();
        File file = new File(mExternalStoragePath);
        switch (item.getItemId()) {
            case R.id.action_mark_all_read:
                materialDialog = PromptManager.showIndeterminateProgressDialog(getActivity(), "正在查找中");
                new Thread() {
                    @Override
                    public void run() {
                        // listFileTxt(file);
                        final String[] filterNames = {".pdf", ".txt"};
                        scanFile(getSDPath(), filterNames);
                        mHandler.sendEmptyMessage(2);
                    }
                }.start();
                return true;
            case R.id.action_clear_notifications:
               Toast.makeText(getContext(),"clear all",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void scanFile(File rootPath, final String filterName) {
        File[] files = rootPath.listFiles();
        try {
            for(File f:files){
                if(!f.isDirectory()){
                    if(f.getName().endsWith(filterName)){
                        long size = f.length();
                        if(size>50*1024){
                            FileMsgModel fileModel = new FileMsgModel();
                            fileModel.setFileName(f.getName());
                            fileModel.setFileSize(String.valueOf(size/1024));
                            fileModel.setFilePath(f.getPath());
                            lists.add(fileModel);
                        }
                    }
                }else{
                    scanFile(f,filterName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog!=null){
            dialog.dismiss();
        }
    }
}
