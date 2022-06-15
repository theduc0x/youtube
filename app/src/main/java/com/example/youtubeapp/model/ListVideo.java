package com.example.youtubeapp.model;

import java.util.ArrayList;

public class ListVideo {
        private String kind;
        private String etag;
        private String nextPageToken;
        private ArrayList<Items> items;

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

        public String getNextPageToken() {
                return nextPageToken;
        }

        public void setNextPageToken(String nextPageToken) {
                this.nextPageToken = nextPageToken;
        }

        public ArrayList<Items> getItems() {
                return items;
        }

        public void setItems(ArrayList<Items> items) {
                this.items = items;
        }

        public PageInfos getPageInfo() {
                return pageInfo;
        }

        public void setPageInfo(PageInfos pageInfo) {
                this.pageInfo = pageInfo;
        }

        private PageInfos pageInfo;
    }


