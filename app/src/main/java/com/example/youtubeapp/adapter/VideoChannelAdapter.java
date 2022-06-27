package com.example.youtubeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;

import java.util.ArrayList;

public class VideoChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int VIEW_TYPE_ITEM = 0,
                            VIEW_TYPE_LOADING = 1;
    private ArrayList<VideoItem> listItems;
    private boolean isLoadingAdd;

    public void setData(ArrayList<VideoItem> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (listItems != null && position == listItems.size() - 1 && isLoadingAdd) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (VIEW_TYPE_ITEM == viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video_in_channel, parent, false);
            return new ItemVideoViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingPageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    class ItemVideoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnails;
        TextView tvTitleVideo, tvViewCount, tvTimeDiff;
        public ItemVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnails = itemView.findViewById(R.id.iv_thumbnail_video);
            tvTitleVideo = itemView.findViewById(R.id.tv_title_video_channel);
            tvViewCount = itemView.findViewById(R.id.tv_view_count_video_channel);
            tvTimeDiff = itemView.findViewById(R.id.tv_time_diff_date);
        }
    }
    class LoadingPageViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pbLoading;
        public LoadingPageViewHolder(@NonNull View itemView) {
            super(itemView);
            pbLoading = itemView.findViewById(R.id.pb_loading);
        }
    }
}
