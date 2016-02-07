package com.example.android.displayingbitmaps.provider;

import com.example.android.displayingbitmaps.provider.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the Photos
 */
public class Images {
    private static List<Photo> photoList;

    public synchronized static List<Photo> getPhotoList() {
        if (photoList == null) {
            photoList = new ArrayList<>();
        }
        return photoList;
    }
}
