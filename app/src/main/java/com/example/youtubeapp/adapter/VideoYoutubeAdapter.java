package com.example.youtubeapp.adapter;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoYoutubeAdapter extends RecyclerView.Adapter<VideoYoutubeAdapter.VideoViewHolder> {
    private ArrayList<VideoItem> listItemVideo;
    private IItemOnClickVideoListener itemOnClickVideoListener;

    public VideoYoutubeAdapter(ArrayList<VideoItem> listItemVideo,
                               IItemOnClickVideoListener itemOnClickVideoListener) {
        this.listItemVideo = listItemVideo;
        this.itemOnClickVideoListener = itemOnClickVideoListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_video_home, parent, false);
        return new VideoViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem video = listItemVideo.get(position);
        if (video == null) {
            return ;
        }
        String urlThumbnailVideo = video.getUrlImageItemVideo();
        String titleVideo = video.getTvTitleVideo();
        String titleChannel = video.getTvTitleChannel();

        String timeVideo = video.getTvTimeVideo();
        // tính khoảng cách từ ngày public video đến nay
        String dateDayDiff = Util.getTime(timeVideo);
        String viewCountVideo = video.getViewCountVideo();
        String likeCount = video.getLikeCountVideo();
        String descVideo = video.getDescVideo();
        String idChannel = video.getIdChannel();

        callApiChannel(idChannel, video, position);
//        video.setUrlLogoChannel(urlLogooo);
        String urlLogoChannel = video.getUrlLogoChannel();

        Picasso.get().load(urlThumbnailVideo).into(holder.ivItemVideo);
        holder.tvTitleVideo.setText(titleVideo);
        holder.tvTitleChannel.setText(titleChannel);
        Picasso.get().load(urlLogoChannel).into(holder.civLogoChannel);

        holder.tvTimeVideo.setText(dateDayDiff);
        holder.tvViewCountVideo.setText( "• "+ viewCountVideo + " views •");
        String idVideo = video.getIdVideo();
        holder.clItemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClickVideoListener.OnClickItemVideo(video);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listItemVideo != null) {
            return listItemVideo.size();
        }
        return 0;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemVideo, ivSettingVideo;
        CircleImageView civLogoChannel;
        TextView tvTitleVideo, tvTitleChannel, tvViewCountVideo, tvTimeVideo;
        ConstraintLayout clItemClick;


        public VideoViewHolder(@NonNull View itemView) {
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

    private void callApiChannel(String id, VideoItem video, int pos) {
        ApiServicePlayList.apiServicePlayList.infoChannel(
                "snippet",
                "contentDetails",
                "statistics",
                id,
                Util.API_KEY
        ).enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                ArrayList<Itemss> listItem = new ArrayList<>();
                    Channel channel = response.body();
                    if (channel != null) {
                        listItem = channel.getItems();
                        String urlLogooo = listItem.get(0).getSnippet().getThumbnails().getMedium().getUrl();
                        video.setUrlLogoChannel(urlLogooo);
                    }


            }
            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.d("ab", t.toString());
            }
        });
    }
}
