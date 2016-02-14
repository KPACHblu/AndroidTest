package vk.photo.hunter.provider;

import vk.photo.hunter.provider.model.Photo;

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
