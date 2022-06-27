package com.example.youtubeapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.adapter.ViewPagerChannelAdapter;
import com.example.youtubeapp.fragment.ChannelAboutFragment;
import com.example.youtubeapp.fragment.ChannelChannelsFragment;
import com.example.youtubeapp.fragment.ChannelCommunityFragment;
import com.example.youtubeapp.fragment.ChannelHomeFragment;
import com.example.youtubeapp.fragment.ChannelPlayListFragment;
import com.example.youtubeapp.fragment.ChannelVideoFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChannelActivity extends AppCompatActivity {
    TabLayout tlChannel;
    ViewPager2 vp2Content;
    ViewPagerChannelAdapter adapter;
    String idChannel = "", titleChannel = "";
    TextView tvTitleChannel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        initView();
        setTabLayout();
        getIdChannelAndTransHomeChannel();
    }

    private void initView() {
        tlChannel = findViewById(R.id.tl_channel);
        vp2Content = findViewById(R.id.vp2_content);
        tvTitleChannel = findViewById(R.id.tv_title_channel_nav);
    }

    private void setTabLayout() {
        adapter = new ViewPagerChannelAdapter(this);
        vp2Content.setAdapter(adapter);
        new TabLayoutMediator(tlChannel, vp2Content, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0 :
                        tab.setText("HOME");
                        break;
                    case 1 :
                        tab.setText("VIDEOS");
                        break;
                    case 2 :
                        tab.setText("PLAYLISTS");
                        break;
                    case 3 :
                        tab.setText("COMMUNITY");
                        break;
                    case 4 :
                        tab.setText("CHANNELS");
                        break;
                    case 5 :
                        tab.setText("ABOUT");
                        break;
                }
            }
        }).attach();
    }
    private void getIdChannelAndTransHomeChannel() {
        Intent getData = getIntent();
        idChannel = getData.getStringExtra(Util.EXTRA_ID_CHANNEL_TO_CHANNEL);
        titleChannel = getData.getStringExtra(Util.EXTRA_TITLE_CHANNEL_TO_CHANNEL);
        tvTitleChannel.setText(titleChannel);
        adapter.setData(idChannel);
    }
}