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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;
import com.example.youtubeapp.my_interface.ILoadMore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class Loading2ViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;
    public Loading2ViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.pb_loading);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {
    ImageView ivItemVideo, ivSettingVideo;
    CircleImageView civLogoChannel;
    TextView tvTitleVideo, tvTitleChannel, tvViewCountVideo, tvTimeVideo;
    ConstraintLayout clItemClick;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ivItemVideo = itemView.findViewById(R.id.iv_item_video);
        ivSettingVideo = itemView.findViewById(R.id.iv_setting_video);
        civLogoChannel = itemView.findViewById(R.id.civ_logo_channel);
        tvTitleVideo = itemView.findViewById(R.id.tv_title_video);
        tvTitleChannel = itemView.findViewById(R.id.tv_title_channel);
        tvViewCountVideo = itemView.findViewById(R.id.tv_view_video);
        tvTimeVideo = itemView.findViewById(R.id.tv_time_video);
        clItemClick = itemView.findViewById(R.id.cl_item_click);
    }
}

public class RelatedVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadMore;
    boolean isLoading;
    ArrayList<VideoItem>  items;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;
    private IItemOnClickVideoListener itemOnClickVideoListener;

    public RelatedVideoAdapter(RecyclerView recyclerView, ArrayList<VideoItem> items, IItemOnClickVideoListener itemOnClickVideoListener) {
        this.items = items;
        this.itemOnClickVideoListener = itemOnClickVideoListener;

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null) {
                        loadMore.onLoadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_video_home, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_loading, parent, false);
            return new Loading2ViewHolder(view);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            VideoItem item = items.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;

            String urlThumbnailVideo = item.getUrlImageItemVideo();
            String titleVideo = item.getTvTitleVideo();
            String titleChannel = item.getTvTitleChannel();
            String urlLogoChannel = item.getUrlLogoChannel();
            String timeVideo = item.getTvTimeVideo();
            // tính khoảng cách từ ngày public video đến nay
            String dateDayDiff = Util.getTime(timeVideo);
            String viewCountVideo = item.getViewCountVideo();
            String likeCount = item.getLikeCountVideo();
            String descVideo = item.getDescVideo();
            String idChannel = item.getIdChannel();
            String idVideo = item.getIdVideo();

            Picasso.get().load(urlThumbnailVideo).into(viewHolder.ivItemVideo);
            viewHolder.tvTitleVideo.setText(titleVideo);
            viewHolder.tvTitleChannel.setText(titleChannel);
            Picasso.get().load(urlLogoChannel).into(viewHolder.civLogoChannel);
            viewHolder.tvTimeVideo.setText(dateDayDiff);
            viewHolder.tvViewCountVideo.setText( "• "+ viewCountVideo + " views •");
            viewHolder.clItemClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickVideoListener.OnClickItemVideo(item);
                }
            });


        } else if(holder instanceof Loading2ViewHolder) {
            Loading2ViewHolder loading2ViewHolder = (Loading2ViewHolder) holder;
            loading2ViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
}
