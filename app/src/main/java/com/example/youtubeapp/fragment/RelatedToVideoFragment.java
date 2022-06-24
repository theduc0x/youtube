package com.example.youtubeapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.adapter.RelatedVideoAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.model.listvideohome.Items;
import com.example.youtubeapp.model.listvideohome.ListVideo;
import com.example.youtubeapp.model.listvideorelated.RelatedVideo;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelatedToVideoFragment extends Fragment {
    ArrayList<VideoItem> listItems;
    RelatedVideoAdapter adapter;
    RecyclerView rvRelatedVideo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_related_to_video, container, false);
        initView(view);
        listItems = new ArrayList<>();



        return view;
    }

    private void initView(View view) {
        rvRelatedVideo = view.findViewById(R.id.rv_list_related_video);
    }


    // Get dữ liệu về
    private void callApiRelatedVideo(String pageToken, String relatedIdVideo, String maxResults) {
        ApiServicePlayList.apiServicePlayList.Related_call(
                pageToken,
                "snippet",
                relatedIdVideo,
                "video",
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<RelatedVideo>() {
            @Override
            public void onResponse(Call<RelatedVideo> call, Response<RelatedVideo> response) {

            }

            @Override
            public void onFailure(Call<RelatedVideo> call, Throwable t) {

            }
        });
    }

}