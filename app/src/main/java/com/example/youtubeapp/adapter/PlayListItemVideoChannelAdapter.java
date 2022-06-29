package com.example.youtubeapp.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.model.itemrecycleview.ItemVideoInPlayList;
import com.example.youtubeapp.model.itemrecycleview.VideoChannelItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayListItemVideoChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int VIEW_TYPE_ITEM = 0,
            VIEW_TYPE_LOADING = 1;
    private ArrayList<ItemVideoInPlayList> listItems;
    private boolean isLoadingAdd;

    public void setData(ArrayList<ItemVideoInPlayList> listItems) {
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
                    .inflate(R.layout.item_video_in_playlist, parent, false);
            return new ItemPlayViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingPagePlayViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            ItemVideoInPlayList item = listItems.get(position);
            if (item == null) {
                return;
            }
            ItemPlayViewHolder viewHolder = (ItemPlayViewHolder) holder;
            Picasso.get().load(item.getUrlImageItemVideo()).into(viewHolder.ivThumbnails);
            viewHolder.tvTitleChannel.setText(item.getTvTitleChannel());
            viewHolder.tvTitleVideo.setText(item.getTitleVideo());
            if (item.getViewCountVideo().equals("")) {
                viewHolder.tvViewCount.setText("0");
            } else {
                viewHolder.tvViewCount.setText(
                        Util.convertViewCount(Double.parseDouble(item.getViewCountVideo())));
            }

            viewHolder.tvTimeDiff.setText(Util.getTime(item.getPublishAt()));
        }
    }

    @Override
    public int getItemCount() {
        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    class ItemPlayViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnails;
        TextView tvTitleVideo, tvViewCount, tvTimeDiff, tvTitleChannel;
        public ItemPlayViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnails = itemView.findViewById(R.id.iv_thumbnail_video_in_playlist);
            tvTitleVideo = itemView.findViewById(R.id.tv_title_video_in_playlist);
            tvViewCount = itemView.findViewById(R.id.tv_view_count_video_in_playlist);
            tvTimeDiff = itemView.findViewById(R.id.tv_time_diff_date_detail);
            tvTitleChannel = itemView.findViewById(R.id.tv_title_Channel_in_playlist_detail);
        }
    }
    class LoadingPagePlayViewHolder extends RecyclerView.ViewHolder {
        ProgressBar pbLoading;
        public LoadingPagePlayViewHolder(@NonNull View itemView) {
            super(itemView);
            pbLoading = itemView.findViewById(R.id.pb_loading);
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        listItems.add(new ItemVideoInPlayList("", "",
                "", "", "", ""));
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int pos = listItems.size() - 1;
        ItemVideoInPlayList item = listItems.get(pos);
        if (item != null) {
            listItems.remove(pos);
            notifyItemRemoved(pos);
        }

    }

}
