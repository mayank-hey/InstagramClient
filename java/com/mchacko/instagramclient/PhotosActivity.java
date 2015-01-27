package com.mchacko.instagramclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {
    public static final String CLIENT_ID = "b8f079b506e547399035924b1be1f386";
    private ArrayList<InstagramPhoto> photos = null;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhoto>(); // initialize photos
        // Create adapter and bind it to the data in arraylist
        aPhotos = new InstagramPhotosAdapter(this, photos);
        // Populate the data in the list view
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // Set the adapter to the listView (population of items)
        lvPhotos.setAdapter(aPhotos);
        // https://api.instagram.com/v1/media/popular?client_id=b8f079b506e547399035924b1be1f386
        // SETUP POPULAR URL ENDPOINT
        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // CREATE THE NETWORK CLIENT
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(popularUrl, new JsonHttpResponseHandler() {
            // define success and failure callbacks

            // Handle the successful response (popular photos JSON)


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // fired once the successful response is back
                // response is == popular photos json
                // { “data” -> [x] -> “images" -> “standard_resolution” -> “url” }
                // { “data” -> [x] -> “images" -> “standard_resolution” -> “height” }
                // { “data” -> [x] -> “user" -> “standard_resolution” -> “username” }
                // { “data” -> [x] -> “caption" -> “standard_resolution” -> “text” }
                Log.i("INFO", response.toString());
                JSONArray photosJSON = null;
                try {
                    photos.clear();
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        if(!photoJSON.isNull("caption")) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photos.add(photo);
                    }
                    // Notified the adapter that it should populate new changes into the listView
                    aPhotos.notifyDataSetChanged();
                } catch (JSONException e) {
                    // Fire if things fail, json parsing is invalid
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        // TRIGGER THE NETWORK REQUEST

        // HANDLE THE SUCCESSFUL RESPONSE

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
