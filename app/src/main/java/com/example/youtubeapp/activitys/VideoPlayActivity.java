package com.example.youtubeapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.fragment.BottomSheetDialogDescFragment;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.infochannel.Itemss;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.model.listvideohome.ListVideo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayActivity extends AppCompatActivity {
    YouTubePlayerView ypvVideo;
    RelativeLayout rlGroup;
    LinearLayout llDisplayDesc;
    TextView tvTitleVideoPlay, tvViewVideoPlay, tvTimeVideoPlay;
    BottomNavigationView bnvOption;
    CircleImageView civLogoChannel;
    TextView tvTitleChannelVideo, tvSubscription;
    AppCompatButton btSubscribe;
    String idVideo, titleVideo, titleChannel;
    String viewCount;
    String timePublic;
    String likeCount;
    String descVideo, idChannel,dateDayDiff;
    VideoItem itemVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();

        Intent getVideoInfo = getIntent();
        Bundle bundle = getVideoInfo.getExtras();
        // Nhận dữ liệu bundle từ sự kiện click item video và add vào activity
        if (bundle != null) {
            itemVideo =
                    (VideoItem) bundle.getSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO);
            idVideo = itemVideo.getIdVideo();
            titleVideo = itemVideo.getTvTitleVideo();
            titleChannel = itemVideo.getTvTitleChannel();
            viewCount = itemVideo.getViewCountVideo();
            timePublic = itemVideo.getTvTimeVideo();
            dateDayDiff = Util.getTime(timePublic);
            likeCount = itemVideo.getLikeCountVideo();
            descVideo = itemVideo.getDescVideo();
            idChannel = itemVideo.getIdChannel();
            Log.d("abc", descVideo);
        }
        // call api lấy logo channel
        callApiChannel(idChannel);
        // chạy video
        ypvVideo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo(idVideo, 0);
            }
        });
        tvTitleVideoPlay.setText(titleVideo);
        tvTitleChannelVideo.setText(titleChannel);
        tvViewVideoPlay.setText(viewCount + " views • ");
        tvTimeVideoPlay.setText(dateDayDiff);
        if (likeCount == null) {
            likeCount = "";
            bnvOption.getMenu().findItem(R.id.mn_like).setTitle("Like");
        } else {
            likeCount = Util.convertViewCount(Double.parseDouble(likeCount));
            bnvOption.getMenu().findItem(R.id.mn_like).setTitle(likeCount);
        }


        llDisplayDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpenBottomSheetDialogFragment();
            }
        });

    }

//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider,
//                                        YouTubePlayer youTubePlayer, boolean b) {
//        youTubePlayer.loadVideo(idVideo);
//    }
//
//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider,
//                                        YouTubeInitializationResult youTubeInitializationResult) {
//        if (youTubeInitializationResult.isUserRecoverableError()) {
//            youTubeInitializationResult
//                    .getErrorDialog(VideoPlayActivity.this, Util.REQUEST_CODE_VIDEO);
//        } else {
//            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == Util.REQUEST_CODE_VIDEO) {
//            ypvVideo.initialize(Util.API_KEY, VideoPlayActivity.this);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    // api lấy url channel (thumbnail)
    private void callApiChannel(String id) {
        ApiServicePlayList.apiServicePlayList.infoChannel(
                "snippet",
                "contentDetails",
                "statistics",
                id,
                Util.API_KEY
        ).enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                Toast.makeText(VideoPlayActivity.this, "Call api", Toast.LENGTH_SHORT).show();
                Channel channel = response.body();
                String sub = "";
                if (channel != null) {
                    ArrayList<Itemss> listItem = channel.getItems();
                    Picasso.get().load(listItem.get(0).getSnippet().getThumbnails().getMedium().getUrl())
                            .into(civLogoChannel);
                    if (listItem.get(0).getStatistics().getSubscriberCount() == null) {
                        sub = "";
                        tvSubscription.setVisibility(View.INVISIBLE);
                        rlGroup.setVerticalGravity(Gravity.CENTER_VERTICAL);
                    } else {
                        sub = listItem.get(0).getStatistics().getSubscriberCount();
                        double subC = Double.parseDouble(sub);
                        sub = Util.convertViewCount(subC);
                        tvSubscription.setText(sub + " subcribers");
                    }
                }
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Toast.makeText(VideoPlayActivity.this,
                        "Call Api Error", Toast.LENGTH_SHORT).show();
                Log.d("ab", t.toString());
            }
        });
    }
    // Click open desc
    private void clickOpenBottomSheetDialogFragment() {
        BottomSheetDialogDescFragment bottomSheetDialogDescFragment =
                BottomSheetDialogDescFragment.newInstance(itemVideo);
        bottomSheetDialogDescFragment.show(getSupportFragmentManager(),
                bottomSheetDialogDescFragment.getTag());
    }

    private void initView() {
        ypvVideo = findViewById(R.id.ypv_video);
        getLifecycle().addObserver(ypvVideo);
        tvTitleVideoPlay = findViewById(R.id.tv_title_video_play);
        tvViewVideoPlay = findViewById(R.id.tv_view_video_play);
        tvTimeVideoPlay = findViewById(R.id.tv_time_video_play);
        bnvOption = findViewById(R.id.bnv_play_video_select);
        civLogoChannel = findViewById(R.id.civ_image_logo_channel);
        tvTitleChannelVideo = findViewById(R.id.tv_title_channel_video);
        tvSubscription = findViewById(R.id.tv_subscription);
        btSubscribe = findViewById(R.id.bt_subscribe);
        rlGroup = findViewById(R.id.rl_group_title_and_sub);
        llDisplayDesc = findViewById(R.id.ll_display_desc);
    }
}