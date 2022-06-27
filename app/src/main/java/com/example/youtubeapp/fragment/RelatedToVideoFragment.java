package com.example.youtubeapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.activitys.VideoPlayActivity;
import com.example.youtubeapp.adapter.RelatedVideoAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.model.listvideorelated.ItemsRelated;
import com.example.youtubeapp.model.listvideorelated.RelatedVideo;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelatedToVideoFragment extends Fragment {
    ArrayList<VideoItem> listItems;
    RelatedVideoAdapter adapter;
    RecyclerView rvRelatedVideo;
    VideoItem itemVideo;
    String idVideoRe = "";
    String pageTokenTo = "";
    RelatedVideo relatedVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_related_to_video, container, false);
        initView(view);
        listItems = new ArrayList<>();
        Bundle bundleRe = getArguments();
            itemVideo =
                    (VideoItem) bundleRe.getSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO);
            idVideoRe = itemVideo.getIdVideo();
        callApiRelatedVideo("", idVideoRe, "10");


        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        RecyclerView.ItemDecoration decoration =
                new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        rvRelatedVideo.setLayoutManager(linearLayoutManager);
        adapter = new RelatedVideoAdapter(rvRelatedVideo, listItems, new IItemOnClickVideoListener() {
            @Override
            public void OnClickItemVideo(VideoItem item) {
                Intent toPlayVideo = new Intent(getActivity(), VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO, item);
                toPlayVideo.putExtras(bundle);
                startActivity(toPlayVideo);
            }
        });
        rvRelatedVideo.addItemDecoration(decoration);
        rvRelatedVideo.setAdapter(adapter);

//
//        adapter.setLoadMore(new ILoadMore() {
//            @Override
//            public void onLoadMore() {
//                String s = pageTokenTo;
//                int totalR = relatedVideo.getPageInfo().getTotalResults();
//                Log.d("abccc", relatedVideo.getPageInfo().getResultsPerPage()+"");
//                if (listItems.size() <= totalR) {
//                    listItems.add(null);
//                    adapter.notifyItemInserted(listItems.size() - 1);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            listItems.remove(listItems.size() - 1);
//                            adapter.notifyItemRemoved(listItems.size());
//                            callApiRelatedVideo(pageTokenTo, idVideoRe, "5");
////                            int index = listItems.size();
////                            int end = index + 5;
////                            for (int i = index ; i < end; i++) {
////                                adapter.notifyItemInserted(i);
////                            }
//                            adapter.notifyDataSetChanged();
//
//                            Log.d("abcccc", s + "");
//                            adapter.setLoaded();
//                        }
//                    }, 1000);
//                } else {
//                    Log.d("abccc", "Success");
//                }
//            }
//        });

        return view;
    }

    private void initView(View view) {
        rvRelatedVideo = view.findViewById(R.id.rv_list_related_video);
    }


    // Get dữ liệu về
    private void callApiRelatedVideo(String pageToken, String relatedIdVideo, String maxResults) {
        ApiServicePlayList.apiServicePlayList.relatedCall(
                pageToken,
                "snippet",
                relatedIdVideo,
                "video",
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<RelatedVideo>() {
            @Override
            public void onResponse(Call<RelatedVideo> call, Response<RelatedVideo> response) {
                Log.d("ssss", "Success");
                Log.d("ssss", relatedIdVideo);
                String urlThumbnailVideo = "", titleVideo = "", titleChannel = "",
                        timeVideo = "", viewCountVideo = "", commentCount = "",
                        idVideo = "", likeCountVideo = "", descVideo = "",
                        nextPageToken = "", idChannel = "", urlLogoChannel = "";
                relatedVideo = response.body();
                if (relatedVideo != null) {
                    nextPageToken = relatedVideo.getNextPageToken();
                    if (nextPageToken.equals(pageTokenTo)) {
                        return;
                    }
                    pageTokenTo = nextPageToken;

                    ArrayList<ItemsRelated> listItem = relatedVideo.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        if (listItem.get(i).getSnippet() == null) {
                            i++;
                        } else {
                            urlThumbnailVideo = listItem.get(i).getSnippet().getThumbnails().getHigh().getUrl();



                            titleVideo = listItem.get(i).getSnippet().getTitle();
                            titleChannel = listItem.get(i).getSnippet().getChannelTitle();
                            idChannel = listItem.get(i).getSnippet().getChannelId();

                            urlLogoChannel = listItem.get(i).getSnippet()
                                    .getThumbnails().getHigh().getUrl();

                            idVideo = listItem.get(i).getId().getVideoId();

                            timeVideo = listItem.get(i).getSnippet().getPublishedAt();

                            descVideo = listItem.get(i).getSnippet().getDescription();

                            listItems.add(new VideoItem(urlThumbnailVideo,
                                    urlLogoChannel, titleVideo, timeVideo,
                                    titleChannel, viewCountVideo, idVideo,
                                    likeCountVideo, descVideo, idChannel, commentCount));
//                        adapter.notifyItemInserted(i);
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RelatedVideo> call, Throwable t) {

            }
        });
    }

}