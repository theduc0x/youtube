package com.example.youtubeapp.api;

import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.listcomment.Comment;
import com.example.youtubeapp.model.listvideohome.ListVideo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiServicePlayList {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiServicePlayList apiServicePlayList = new Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServicePlayList.class);

//    // Nhận list video max = 50;
//    @GET("youtube/v3/videos?part" +
//            "=snippet&part=statistics&chart=mostPopular&" +
//            "locale=vn&regionCode=vn&" +
//            "key=AIzaSyDkEdU_hnItFhVO0yDBS758w4FFDIWDuzg&maxResults=50")
//    Call<ListVideo> listVideo();


    // List video
    @GET("youtube/v3/videos")
    Call<ListVideo> listVideoNext(
            @Query("pageToken") String pageToken ,
            @Query("part") String partSnippet ,
            @Query("part") String partStatic ,
            @Query("chart") String chart ,
            @Query("locale") String locale ,
            @Query("regionCode") String regionCode ,
            @Query("key") String key,
            @Query("maxResults" ) String maxResults);


    @GET("youtube/v3/channels")
    Call<Channel> infoChannel(@Query("part") String partSnippet ,
                              @Query("part") String partContent ,
                              @Query("part") String partStatic ,
                              @Query("id") String idChannel ,
                              @Query("key") String key);

    @GET("youtube/v3/commentThreads")
    Call<Comment> Comment(
            @Query("pageToken") String pageToken ,
            @Query("part") String partSnippet ,
            @Query("part") String partReplies,
            @Query("order") String order ,
            @Query("part") String id ,
            @Query("textFormat") String planText,
            @Query("videoId") String videoId ,
            @Query("key") String key,
            @Query("maxResults" ) String maxResults);
}
