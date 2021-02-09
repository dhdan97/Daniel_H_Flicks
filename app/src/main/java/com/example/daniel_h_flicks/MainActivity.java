package com.example.daniel_h_flicks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"; // set our api call url as a constant
    public static final String TAG = "MainActivity" // tag used to log data when using api
;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Associate MainActivity.java(this file) with activity_main.xml

        AsyncHttpClient client = new AsyncHttpClient(); // boilerplate for GET api network request
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() { // notice how we use JsonHttpResponseHandler because we know the api url returns a JSON
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject; // Get the json object from the api call which has the data we need
                try {
                    JSONArray results = jsonObject.getJSONArray("Results"); // If we looked at the json data, we would have seen that the data is stored in a array, so we do getJSONArray (make sure you handle the exception)
                    Log.i(TAG, "Results: " + results.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}