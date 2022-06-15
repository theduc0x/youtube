package com.example.youtubeapp.model;

import com.google.gson.annotations.SerializedName;

public class Thumbnail {
    @SerializedName("default")
    private Defaultss defaults;

    private Medium medium;
    private High high;

    public Defaultss getDefaults() {
        return defaults;
    }

    public void setDefaults(Defaultss defaults) {
        this.defaults = defaults;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public High getHigh() {
        return high;
    }

    public void setHigh(High high) {
        this.high = high;
    }

    public Standards getStandard() {
        return standard;
    }

    public void setStandard(Standards standard) {
        this.standard = standard;
    }

    public Maxres getMaxres() {
        return maxres;
    }

    public void setMaxres(Maxres maxres) {
        this.maxres = maxres;
    }

    private Standards standard;
    private Maxres maxres;
}
