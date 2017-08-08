package com.example.alipay.myreadingapplication.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.example.alipay.myreadingapplication.DividerItemDecoration;
import com.example.alipay.myreadingapplication.R;
import com.example.alipay.myreadingapplication.model.FileMsgModel;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jxq .
 * Description:
 * Date:on 2017/7/20.
 */
public class SDTxtListActivity extends AppCompatActivity {
    RecyclerView  recycler;
    ArrayList<FileMsgModel> txtFiles;
    private MultiSelector multiSelector = new MultiSelector();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sd_txt_layout);
        txtFiles = (ArrayList<FileMsgModel>)getIntent().getSerializableExtra("data");
        initView(txtFiles);

    }

    private void initView(ArrayList<FileMsgModel> txtFiles) {
        recycler = (RecyclerView) findViewById(R.id.txt_file_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new TxtFileAdapter(txtFiles));
        recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }

    private ModalMultiSelectorCallback mActionModeCallback = new ModalMultiSelectorCallback(multiSelector) {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.sd_card_txt_menu,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_delete:
                    mode.finish();
                    List<FileMsgModel> files =  new ArrayList<>();
                    for(int i = txtFiles.size()-1;i>=0;i--){
                        if(multiSelector.isSelected(i,0)){
                            FileMsgModel fileModel = txtFiles.get(i);
                            files.add(fileModel);
                        }
                    }
                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE", (Serializable) files);
                    setResult(2,intent);
                    finish();
                    multiSelector.clearSelections();
                    return true;
                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            new TxtFileAdapter(txtFiles).clearSelections();
        }
    };
    private class TxtFileAdapter extends RecyclerView.Adapter<TxtFileHodler> {
        private ArrayList<FileMsgModel> files;
        public TxtFileAdapter(ArrayList<FileMsgModel> txtFiles) {
            this.files = txtFiles;
        }

        @Override
        public TxtFileHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(SDTxtListActivity.this);
            View view = inflater.inflate(R.layout.item,parent,false);
            return new TxtFileHodler(view);
        }

        @Override
        public void onBindViewHolder(TxtFileHodler holder, int position) {
            FileMsgModel txt = files.get(position);
            holder.setSelectionModeBackgroundDrawable(getResources().getDrawable(R.drawable.sd_txt_back_drawable));
            holder.bindView(txt);
        }

        @Override
        public int getItemCount() {
            return files.size();
        }

        public void clearSelections() {
            multiSelector.setSelectable(false);
            notifyDataSetChanged();
        }
    }

    public class TxtFileHodler extends SwappingHolder implements View.OnClickListener,View.OnLongClickListener{
        private FileMsgModel dm;
        private TextView textView;
        public TxtFileHodler(View itemView) {
            super(itemView,multiSelector);
            textView = (TextView) itemView.findViewById(R.id.txtName);
            itemView.setLongClickable(true);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        public void bindView(FileMsgModel fm){
            this.dm = fm;
            String txtName = dm.getFileName();
            textView.setText(txtName);
        }

        @Override
        public void onClick(View view) {
            if(!multiSelector.tapSelection(TxtFileHodler.this)){
                selectTxt(dm);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(!multiSelector.isSelectable()){
                SDTxtListActivity.this.startSupportActionMode(mActionModeCallback);
                multiSelector.setSelectable(true);
                multiSelector.setSelected(TxtFileHodler.this,true);
                return true;
            }
            return false;
        }
    }

    /**
     * 选择txt文件
     * @param dm
     */
    private void selectTxt(FileMsgModel dm) {
        File f = new File(dm.getFilePath());
        if (f.exists()) {
            Uri path = Uri.fromFile(f);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(SDTxtListActivity.this,
                        "No Application Available to View PDF",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
