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
import android.widget.Toast;

import com.example.youtubeapp.R;
import com.example.youtubeapp.Util;
import com.example.youtubeapp.model.itemrecycleview.VideoItem;
import com.example.youtubeapp.activitys.VideoPlayActivity;
import com.example.youtubeapp.adapter.VideoYoutubeAdapter;
import com.example.youtubeapp.api.ApiServicePlayList;
import com.example.youtubeapp.model.listvideohome.Items;
import com.example.youtubeapp.model.listvideohome.ListVideo;
import com.example.youtubeapp.my_interface.IItemOnClickVideoListener;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    public static final String TAG = HomeFragment.class.getName();
    private RecyclerView rvItemVideo;;
    public static VideoYoutubeAdapter adapter;
    public int a = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Util.listVideoItem = new ArrayList<>();
        callApiPlaylist("");
        rvItemVideo = view.findViewById(R.id.rv_item_video);

        // Khi click vào các item video sẽ mở activity play để chạy video
        adapter = new VideoYoutubeAdapter(Util.listVideoItem, new IItemOnClickVideoListener() {
            @Override
            public void OnClickItemVideo(VideoItem item) {
                Intent toPlayVideo = new Intent(getActivity(), VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Util.BUNDLE_EXTRA_OBJECT_ITEM_VIDEO, item);
                toPlayVideo.putExtras(bundle);
                startActivity(toPlayVideo);
            }
        });

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        RecyclerView.ItemDecoration decoration
                = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        rvItemVideo.setLayoutManager(linearLayoutManager);
        rvItemVideo.addItemDecoration(decoration);
        rvItemVideo.setAdapter(adapter);
        rvItemVideo.smoothScrollToPosition(0);
        return view;
    }

    // Get dữ liệu về
    private void callApiPlaylist(String pageToken) {
        ApiServicePlayList.apiServicePlayList.listVideoNext(
                pageToken,
                "snippet",
                "statistics",
                "mostPopular",
                "vn",
                "vn",
                Util.API_KEY,
                "50"
        ).enqueue(new Callback<ListVideo>() {
            @Override
            public void onResponse(Call<ListVideo> call, Response<ListVideo> response) {
//                Toast.makeText(getActivity(), "Call Api Success", Toast.LENGTH_SHORT).show();
                Log.d("abc", response.body().toString());
                String urlThumbnailVideo = "", titleVideo = "", titleChannel = "",
                         timeVideo = "", viewCountVideo = "",
                        idVideo = "", likeCountVideo = "", descVideo = "",
                        pageToken, idChannel = "", urlLogoChannel = "";

                ListVideo listVideo = response.body();
                pageToken = listVideo.getNextPageToken();
                // Nếu json không rỗng thì ta sẽ add vào list
                if (listVideo != null) {
                    ArrayList<Items> listItem = listVideo.getItems();
                    for (int i = 0; i < listItem.size(); i++) {

                        urlThumbnailVideo = listItem.get(i)
                                .getSnippet().getThumbnails()
                                .getHigh().getUrl();

                        titleVideo = listItem.get(i).getSnippet().getTitle();
                        titleChannel = listItem.get(i).getSnippet().getChannelTitle();
                        idChannel = listItem.get(i).getSnippet().getChannelId();
//                        callApiChannel(idChannel , i);

                        urlLogoChannel = listItem.get(i)
                                .getSnippet().getThumbnails()
                                .getHigh().getUrl();
                        timeVideo = listItem.get(i).getSnippet().getPublishedAt();
//                        String dateDayDiff = Util.getTime(timeVideo);

                        viewCountVideo = listItem.get(i).getStatistics().getViewCount();
                        double viewCount = Double.parseDouble(viewCountVideo);
                        viewCountVideo = Util.convertViewCount(viewCount);

                        idVideo = listItem.get(i).getId();
                        likeCountVideo = listItem.get(i).getStatistics().getLikeCount();
                        descVideo = listItem.get(i).getSnippet().getDescription();


                        Util.listVideoItem.add(new VideoItem(urlThumbnailVideo,
                                urlLogoChannel, titleVideo, timeVideo,
                                titleChannel, viewCountVideo, idVideo,
                                likeCountVideo, descVideo, idChannel));
//                        adapter.notifyItemInserted(i);
                    }
                    Collections.shuffle(Util.listVideoItem);
                    adapter.notifyDataSetChanged();
                }
                // Gọi lại 4 lần cho đủ 200 video
                a++;
                if (a > 4) {
                    return;
                }
                callApiPlaylist(pageToken);
            }

            // Nếu lỗi sẽ thông báo lỗi
            @Override
            public void onFailure(Call<ListVideo> call, Throwable t) {
                Toast.makeText(getActivity(), "Call Api Error", Toast.LENGTH_SHORT).show();
                Log.d("ab", t.toString());
            }
        });
    }

    // Lên đầu trang home
    public void topRecycleView() {
//        rvItemVideo.scrollToPosition(0);
        rvItemVideo.smoothScrollToPosition(0);
    }
}