package com.example.youtubeapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.activitys.ChannelActivity;
import com.example.youtubeapp.activitys.VideoPlayListActivity;
import com.example.youtubeapp.adapter.PlayListAdapter;
import com.example.youtubeapp.adapter.VideoChannelAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.itemrecycleview.PlayListItem;
import com.example.youtubeapp.model.listplaylistvideochannel.Items;
import com.example.youtubeapp.model.listplaylistvideochannel.PlayList;
import com.example.youtubeapp.my_interface.IItemOnClickPlayListListener;
import com.example.youtubeapp.my_interface.PaginationScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelPlayListFragment extends Fragment {
    RecyclerView rvPlayList;
    PlayListAdapter adapter;
    ArrayList<PlayListItem> listItems;
    ArrayList<PlayListItem> listAdd;
    String idChannel;
    private String pageToken = "";
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 5;
    private int currenPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_play_list, container, false);
        rvPlayList = view.findViewById(R.id.rv_list_playlist_channel);
        listItems = new ArrayList<>();
        getBundle();

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvPlayList.setLayoutManager(linearLayoutManager);
        adapter = new PlayListAdapter(new IItemOnClickPlayListListener() {
            @Override
            public void onCLickItemPlayList(PlayListItem item) {
                Intent openToChannel = new Intent(getActivity(), VideoPlayListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Util.BUNDLE_EXTRA_PLAY_LIST_TO_VIDEO_PLAY_LIST, item);
                openToChannel.putExtras(bundle);
                startActivity(openToChannel);
            }
        });
                rvPlayList.setAdapter(adapter);
        setFirstData();

        rvPlayList.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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
        return view;
    }


    //     Load data page one
    private void setFirstData() {
        listItems = null;
        callApiPlayList(pageToken, idChannel, "10");
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
                Toast.makeText(getContext(), "Load Page" + currenPage, Toast.LENGTH_SHORT).show();
                callApiPlayList(pageToken, idChannel, "10");
                isLoading = false;
            }
        },1000);
    }


    private void callApiPlayList(String nextPageToken, String idChannel, String maxResults) {
        listAdd = new ArrayList<>();
        ApiServicePlayList.apiServicePlayList.playListChannel(
                nextPageToken,
                "contentDetails,id,localizations,player,snippet,status",
                idChannel,
                Util.API_KEY,
                maxResults
        ).enqueue(new Callback<PlayList>() {
            @Override
            public void onResponse(Call<PlayList> call, Response<PlayList> response) {
                String urlThumbnails = "", titleChannel = "", titlePlayList = "",
                        idPlayList = "";
                int videoCount = 0;

                PlayList playList = response.body();

                if (playList != null) {
                    int totalPlayList =  playList.getPageInfo().getTotalResults();
                    if (totalPlayList % 10 != 0) {
                        totalPage = (totalPlayList / 10) + 1;
                    } else {
                        totalPage = (totalPlayList / 10);
                    }
                    Log.d("page", String.valueOf(totalPage));
                    Log.d("page", String.valueOf(playList.getPageInfo().getTotalResults()));

                    pageToken = playList.getNextPageToken();
                    ArrayList<Items> listItem = playList.getItems();
                    for (int i = 0; i < listItem.size(); i++) {
                        idPlayList = listItem.get(i).getId();

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

                        titleChannel = listItem.get(i).getSnippet().getChannelTitle();
                        titlePlayList = listItem.get(i).getSnippet().getTitle();
                        videoCount = listItem.get(i).getContentDetails().getItemCount();

                        listAdd.add(new PlayListItem(urlThumbnails, String.valueOf(videoCount),
                                titlePlayList, titleChannel, idPlayList));
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
            public void onFailure(Call<PlayList> call, Throwable t) {

            }
        });
    }
    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            idChannel = bundle.getString(Util.EXTRA_ID_CHANNEL_TO_CHANNEL_FROM_ADAPTER);
        }
    }

}