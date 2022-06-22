package com.example.youtubeapp.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.fragment.BottomSheetDialogCommentFragment;
import com.example.youtubeapp.fragment.VideoContainDataFragment;
import com.example.youtubeapp.fragment.VideoContainYoutubePlayFragment;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class VideoPlayActivity extends AppCompatActivity {
    YouTubePlayerView ypvVideo;
    String idVideo;
    VideoItem itemVideo;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        addFragmentMain();

        VideoContainYoutubePlayFragment youtubePlayFragment =
                (VideoContainYoutubePlayFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fm_youtube_play_view);

        youtubePlayFragment.playVideo(idVideo);
    }

    private void addFragmentMain() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        VideoContainDataFragment videoContainDataFragment = new VideoContainDataFragment();
        Intent getVideoInfo = getIntent();
        Bundle bundle = getVideoInfo.getExtras();
        if (bundle != null) {
            itemVideo =
                    (VideoItem) bundle.getSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO);
            idVideo = itemVideo.getIdVideo();
        }
        videoContainDataFragment.setArguments(bundle);
        transaction.replace(R.id.fl_content_data, videoContainDataFragment);
        transaction.commit();
    }
}