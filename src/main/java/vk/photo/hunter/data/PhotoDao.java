package vk.photo.hunter.data;

import vk.photo.hunter.data.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoDao {
    private static List<Photo> photoList;

    public synchronized static List<Photo> getPhotoList() {
        if (photoList == null) {
            photoList = new ArrayList<>();
        }
        return photoList;
    }
}
