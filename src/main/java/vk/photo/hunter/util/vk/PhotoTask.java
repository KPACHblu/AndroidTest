package vk.photo.hunter.util.vk;

import android.location.Location;
import android.util.Log;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import vk.photo.hunter.provider.Images;
import vk.photo.hunter.provider.model.Photo;
import vk.photo.hunter.util.AsyncTask;

public class PhotoTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "PhotoTask";
    private static boolean inProgress;
    private Location location;
    private BaseAdapter adapter;

    public PhotoTask(BaseAdapter adapter, Location location) {
        this.adapter = adapter;
        this.location = location;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        Log.d(TAG, "Try to run new PhotoTask");
        if (!isInProgress()) {
            setInProgress(true);
            Log.d(TAG, "Start PhotoTask; Image size:" + Images.getPhotoList().size());
            try {
                String requestUrl = "https://api.vk.com/method/photos.search?lat=LAT_PARAM&long=LONG_PARAM&offset=OFFSET_PARAM&count=250&v=5.44";
                requestUrl = requestUrl.replace("OFFSET_PARAM", String.valueOf(Images.getPhotoList().size()));
                requestUrl = requestUrl.replace("LAT_PARAM", String.valueOf(location.getLatitude()));
                requestUrl = requestUrl.replace("LONG_PARAM", String.valueOf(location.getLongitude()));
                result = downloadUrl(requestUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "PhotoTask: wasn't run");
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            return;
        }
        try {
            JSONObject json = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
            JSONArray jsonArray = json.getJSONObject("response").getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String smallPhoto = jsonObject.optString("photo_130");
                String bigPhoto = jsonObject.optString("photo_1280");
                String vkUrl = "https://vk.com/photo" + jsonObject.optString("owner_id") + "_" + jsonObject.optString("id");
                Images.getPhotoList().add(new Photo(bigPhoto, vkUrl, smallPhoto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Finished. Image size:" + Images.getPhotoList().size());
        setInProgress(false);
        adapter.notifyDataSetChanged();
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


