package com.example.youtubeapp.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.adapter.PlayListItemVideoChannelAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.detailvideo.DetailVideo;
import com.example.youtubeapp.model.detailvideo.ItemVideo;
import com.example.youtubeapp.model.itemrecycleview.ItemVideoInPlayList;
import com.example.youtubeapp.model.itemrecycleview.PlayListItem;
import com.example.youtubeapp.model.itemrecycleview.VideoChannelItem;
import com.example.youtubeapp.model.playlistitem.Items;
import com.example.youtubeapp.model.playlistitem.PlayListItemVideo;
import com.example.youtubeapp.my_interface.PaginationScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayListActivity extends AppCompatActivity {
    String idPlayList, videoCount, titlePlayList, titleChannel;
    TextView tvTitlePlayListTB, tvTitlePlayList, tvTitleChannel,
            tvVideoCount;
    RecyclerView rvListVideo;
    PlayListItemVideoChannelAdapter adapter;
    ArrayList<ItemVideoInPlayList> listItems;
    ArrayList<ItemVideoInPlayList> listAdd;

    private String pageToken = "";
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;
    private int currenPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_list);
        getData();
        initView();
        setData();
        listItems = new ArrayList<>();

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvListVideo.setLayoutManager(linearLayoutManager);
        adapter = new PlayListItemVideoChannelAdapter();
        rvListVideo.setAdapter(adapter);
        setFirstData();

        rvListVideo.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currenPage += 1;
                loadNextPage();
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
    }

    private void setFirstData() {
        listItems = null;
        callApiListVideoInPlayList(pageToken, idPlayList, "10");
    }
    // Set propress bar load data
    private void setProgressBar() {
        if (currenPage < totalPage) {
            adapter.addFooterLoading();
        } else {
            isLastPage = true;
        }
    }
    // Load dữ liệu của page tiếp theo
    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), "Load Page" + currenPage, Toast.LENGTH_SHORT).show();
                callApiListVideoInPlayList(pageToken, idPlayList, "10");
                isLoading = false;
            }
        },1000);
    }

    private void initView() {
        tvTitlePlayListTB = findViewById(R.id.tv_title_play_list_video);
        tvTitlePlayList = findViewById(R.id.title_playlist_detail);
        tvTitleChannel = findViewById(R.id.tv_title_channel_detail);
        tvVideoCount = findViewById(R.id.tv_view_count_detail);
        rvListVideo = findViewById(R.id.rv_item_video_playList);
    }
    private void setData() {
        tvTitlePlayListTB.setText(titlePlayList);
        tvTitlePlayList.setText(titlePlayList);
        tvVideoCount.setText(videoCount + " video");
        tvTitleChannel.setText(titleChannel);
    }

    private void getData() {
        Intent getDataPlayList = getIntent();
        Bundle bundleRe = getDataPlayList.getExtras();
        if (bundleRe != null) {
            PlayListItem item = (PlayListItem) bundleRe.getSerializable(Util.BUNDLE_EXTRA_PLAY_LIST_TO_VIDEO_PLAY_LIST);
            idPlayList = item.getIdPlayList();
            videoCount = item.getVideoCount();
            titlePlayList = item.getTitleVideo();
            titleChannel = item.getTitleChannel();
        }
    }

    private void callApiListVideoInPlayList(String nextPageToken, String idPlayList, String maxResults) {
        listAdd = new ArrayList<>();
        ApiServicePlayList.apiServicePlayList.listInPlayList(
                nextPageToken,
                "contentDetails,snippet,id,status",
                idPlayList,
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<PlayListItemVideo>() {
            @Override
            public void onResponse(Call<PlayListItemVideo> call,
                                   Response<PlayListItemVideo> response) {
                String urlThumbnails = "", titleChannel = "", titleVideo = "",
                        idVideo = "", viewCount = "", publishAt = "";
                PlayListItemVideo item = response.body();
                if (item != null) {

                    int totalPlayList =  item.getPageInfo().getTotalResults();
                    if (totalPlayList % 10 != 0) {
                        totalPage = (totalPlayList / 10) + 1;
                    } else {
                        totalPage = (totalPlayList / 10);
                    }
                    pageToken = item.getNextPageToken();
                    ArrayList<Items> listItem = item.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        idVideo = listItem.get(i).getSnippet().getResourceId().getVideoId();

                        if (listItem.get(i).getSnippet().getThumbnails().getMaxres() != null) {
                            urlThumbnails = listItem.get(i).getSnippet()
                                    .getThumbnails().getMaxres().getUrl();
                        } else if (listItem.get(i).getSnippet().getThumbnails().getStandard() != null) {
                            urlThumbnails = listItem.get(i).getSnippet()
                                    .getThumbnails().getStandard().getUrl();
                        }else {
                            urlThumbnails = listItem.get(i).getSnippet()
                                    .getThumbnails().getHigh().getUrl();
                        }
                        callApiViewCountVideo(idVideo, listAdd, i);

                        titleChannel = listItem.get(i).getSnippet().getChannelTitle();
                        titleVideo = listItem.get(i).getSnippet().getTitle();
                        publishAt = listItem.get(i).getSnippet().getPublishedAt();

                        listAdd.add(new ItemVideoInPlayList(urlThumbnails, titleVideo,
                                titleChannel, "",publishAt, idVideo));
                    }
                    if (listItems == null) {
                        listItems = listAdd;
                        adapter.setData(listItems);
                        setProgressBar();
                    } else {
                        adapter.removeFooterLoading();
                        listItems.addAll(listAdd);
                        adapter.notifyDataSetChanged();
                        setProgressBar();
                    }
                }
            }

            @Override
            public void onFailure(Call<PlayListItemVideo> call, Throwable t) {

            }
        });
    }

    private void callApiViewCountVideo(String idVideo, ArrayList<ItemVideoInPlayList> listItemV,
                                       int pos) {
        ApiServicePlayList.apiServicePlayList.detailVideo(
                "snippet",
                "statistics",
                idVideo,
                Util.API_KEY
        ).enqueue(new Callback<DetailVideo>() {
            @Override
            public void onResponse(Call<DetailVideo> call, Response<DetailVideo> response) {
                String viewCount = "";
                DetailVideo video = response.body();
                if (video != null) {
                    ArrayList<ItemVideo> listItem = video.getItems();
                    for (int i = 0; i <listItem.size(); i++ ) {
                        viewCount = listItem.get(0).getStatistics().getViewCount();
                        listItemV.get(pos).setViewCountVideo(viewCount);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailVideo> call, Throwable t) {

            }
        });
    }

}