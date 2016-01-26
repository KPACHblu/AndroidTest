package com.example.android.displayingbitmaps.util.vk;

import com.example.android.common.logger.Log;
import com.example.android.displayingbitmaps.provider.Images;
import com.example.android.displayingbitmaps.provider.model.Photo;
import com.example.android.displayingbitmaps.util.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by slitvinov on 1/26/16.
 */
public class PhotoTask extends AsyncTask<String, Void, String>

    {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl("https://api.vk.com/method/photos.search?lat=50.0218205&long=36.2245285&v=5.44");
            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
//            textView.setText(result);
            Log.d("Debug tag HTTP result:", result);
            JSONObject json = null;
            try {
                json = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
                JSONArray jsonArray = json.getJSONObject("response").getJSONArray("items");
                for (int i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String smallPhoto = jsonObject.optString("photo_130");
                    String bigPhoto = jsonObject.optString("photo_1280");

                    Images.getPhotoList().add(new Photo(bigPhoto, smallPhoto));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Debug tag", json!=null?json.toString():"empty json((");
        }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Debug tag", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = convertStreamToString(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
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
}


