package com.example.android.displayingbitmaps.provider.model;

/**
 * Created by slitvinov on 1/26/16.
 */
public class Photo {

    public Photo(String url, String thumbUrl) {
        this.url = url;
        this.thumbUrl = thumbUrl;
    }

    private String url;
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
}
