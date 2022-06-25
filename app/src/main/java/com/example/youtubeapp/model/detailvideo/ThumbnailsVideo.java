package com.example.youtubeapp.model.detailvideo;

import com.google.gson.annotations.SerializedName;

public class ThumbnailsVideo {
    @SerializedName("default")
    public DefaultVideo mydefault;
    public MediumV medium;
    public High high;
}
