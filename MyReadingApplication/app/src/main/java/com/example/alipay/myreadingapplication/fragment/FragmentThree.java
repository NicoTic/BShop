package com.example.alipay.myreadingapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.alipay.myreadingapplication.R;
import com.example.alipay.myreadingapplication.model.MeiZi;
import com.example.alipay.myreadingapplication.service.PullService;
import com.example.alipay.myreadingapplication.util.FlickrFetchr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jxq .
 * Description:
 * Date:on 2017/7/10.
 */
public class FragmentThree extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    private RecyclerView mPhotoRecyclerView;
    private List<MeiZi.ResultsBean> mItems = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
//        Intent i = PullService.newIntent(getActivity());
//        getActivity().startService(i);
//        PullService.setServiceAlarm(getActivity(), true);
        new FetchItemsTask().execute();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_three, container, false);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.is_reading_recycler);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(container.getContext(),2));
        setupAdapter();
        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,List<MeiZi.ResultsBean>>{

        @Override
        protected List<MeiZi.ResultsBean> doInBackground(Void... params) {

            final String query = null; // Just for testing
            if (query == null) {
                return new FlickrFetchr().fetchRecentPhotos();
            } else {
                return new FlickrFetchr().searchPhotos(query);
            }
        }

        @Override
        protected void onPostExecute(List<MeiZi.ResultsBean> resultsBeen) {
            mItems = resultsBeen;
            setupAdapter();
        }
    }



    private class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {
        private List<MeiZi.ResultsBean> mGalleryItems;

        public PhotoAdapter(List<MeiZi.ResultsBean> galleryItems) {
                mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.fragment_three_item_layout, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            MeiZi.ResultsBean galleryItem = mGalleryItems.get(position);
            holder.bindGalleryItem(galleryItem);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

        class PhotoHolder extends RecyclerView.ViewHolder{
            private ImageView mItemImageView;
            public PhotoHolder(View itemView) {
                super(itemView);
                mItemImageView = (ImageView)itemView.findViewById(R.id.fragment_photo_gallery_image_view);
            }
            public void bindGalleryItem(MeiZi.ResultsBean item) {
                Glide.with(itemView.getContext())
                .load(item.getUrl())
                .asBitmap()
                .placeholder(R.drawable.ic_action_photo)
                .into(mItemImageView);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.is_reading_menu, menu);

        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PullService.isServiceAlarmOn(getActivity())) {
            toggleItem.setTitle(R.string.stop_polling);
        } else {
            toggleItem.setTitle(R.string.start_polling);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PullService.isServiceAlarmOn(getActivity());
                PullService.setServiceAlarm(getActivity(), shouldStartAlarm);
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
