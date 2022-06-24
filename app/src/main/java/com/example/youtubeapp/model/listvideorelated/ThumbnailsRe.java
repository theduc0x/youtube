package com.example.youtubeapp.model.listvideorelated;

import com.google.gson.annotations.SerializedName;

public class ThumbnailsRe {
    @SerializedName("default")
    public DefaultRe mydefault;
    public MediumRe medium;
    public HighRe high;
    public StandardRe standard;
    public MaxresRe maxres;
}
