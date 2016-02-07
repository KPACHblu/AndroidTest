package com.example.android.displayingbitmaps.provider.model;

import java.io.Serializable;

public class Photo implements Serializable{

    public Photo(String url, String vkUrl, String thumbUrl) {
        this.url = url;
        this.vkUrl = vkUrl;
        this.thumbUrl = thumbUrl;
    }

    private String url;
    private String vkUrl;
    private String thumbUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getVkUrl() {
        return vkUrl;
    }

    public void setVkUrl(String vkUrl) {
        this.vkUrl = vkUrl;
    }
}
