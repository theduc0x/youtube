package com.example.youtubeapp.api;

import com.example.youtubeapp.model.listvideohome.ListVideo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServiceVideo {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiServicePlayList apiService = new Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServicePlayList.class);
//https://youtube.googleapis.com/youtube/v3/channels?part=snippet&part=contentDetails&part=statistics&id=UCu3DXfXYgygIYXN8TduNcNQ&key=AIzaSyDkEdU_hnItFhVO0yDBS758w4FFDIWDuzg
    @GET("youtube/v3/videos")
    Call<ListVideo> listVideo(@Query("part=snippet&part=statistics&id") String part ,
                              @Query("key") String key
    );
}
