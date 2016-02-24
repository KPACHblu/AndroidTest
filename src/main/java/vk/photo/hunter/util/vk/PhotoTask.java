package vk.photo.hunter.util.vk;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import vk.photo.hunter.data.PhotoDao;
import vk.photo.hunter.data.model.Photo;
import vk.photo.hunter.ui.ImageGridFragment;
import vk.photo.hunter.util.android.AsyncTask;

public class PhotoTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "PhotoTask";
    private static boolean inProgress;
    private ImageGridFragment.ImageAdapter adapter;
    private double latitude;
    private double longitude;

    public PhotoTask(ImageGridFragment.ImageAdapter adapter, Location location) {
        this.adapter = adapter;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        Log.d(TAG, "Try to run new PhotoTask with lat:" + latitude + ";long:" + longitude);
        if (!isInProgress()) {
            setInProgress(true);
            Log.d(TAG, "Start PhotoList size:" + PhotoDao.getPhotoList().size() + " lat:" + latitude + "; long:" + longitude);
            try {
                String requestUrl = "http://api.vk.com/method/photos.search?lat=LAT_PARAM&long=LONG_PARAM&offset=OFFSET_PARAM&count=250&v=5.45&sort=0&radius=6000";
                requestUrl = requestUrl.replace("OFFSET_PARAM", String.valueOf(PhotoDao.getPhotoList().size()));
                requestUrl = requestUrl.replace("LAT_PARAM", String.valueOf(latitude));
                requestUrl = requestUrl.replace("LONG_PARAM", String.valueOf(longitude));
                result = downloadUrl(requestUrl);
            } catch (IOException e) {
                //TODO we need something to show to user
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "PhotoTask wasn't run");
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            return;
        }
        int initialPhotoListSize = PhotoDao.getPhotoList().size();
        try {
            JSONObject json = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
            JSONArray jsonArray = json.getJSONObject("response").getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String smallPhoto = jsonObject.optString("photo_130");
                String bigPhoto = jsonObject.optString("photo_1280");
                String vkUrl = "https://vk.com/photo" + jsonObject.optString("owner_id") + "_" + jsonObject.optString("id");
                PhotoDao.getPhotoList().add(new Photo(bigPhoto, vkUrl, smallPhoto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setInProgress(false);
        int currentPhotoListSize = PhotoDao.getPhotoList().size();
        if (currentPhotoListSize == initialPhotoListSize) {
            adapter.notifyNoDataPulled();
        } else {
            adapter.notifyDataSetChanged();
        }
        Log.d(TAG, "Finished. PhotoList size:" + currentPhotoListSize);
    }

    private String downloadUrl(String urlString) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();

            return convertStreamToString(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private synchronized static boolean isInProgress() {
        return inProgress;
    }

    private synchronized static void setInProgress(boolean inProgress) {
        PhotoTask.inProgress = inProgress;
    }
}


