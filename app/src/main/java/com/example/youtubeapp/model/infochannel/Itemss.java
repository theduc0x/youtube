package com.example.youtubeapp.model.infochannel;

public class Itemss {
    private String kind;
    private String etag;
    private String id;
    private SnippetChannel snippet;
    private ContentDetailsChannel contentDetails;
    private StatisticsChannel statistics;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SnippetChannel getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetChannel snippet) {
        this.snippet = snippet;
    }

    public ContentDetailsChannel getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetailsChannel contentDetails) {
        this.contentDetails = contentDetails;
    }

    public StatisticsChannel getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticsChannel statistics) {
        this.statistics = statistics;
    }
}
