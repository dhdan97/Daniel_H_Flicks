package com.example.daniel_h_flicks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.daniel_h_flicks.adapters.MovieAdapter;
import com.example.daniel_h_flicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"; // set our api call url as a constant
    public static final String TAG = "MainActivity"; // tag used to log data when using api

    List<Movie> movies; // List of movie objects from the movie class we created at Movie.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Associate MainActivity.java(this file) with activity_main.xml
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        // Create adapter instance
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);
        // Set a Layout Manager
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient(); // boilerplate for GET api network request
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() { // notice how we use JsonHttpResponseHandler because we know the api url returns a JSON
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject; // Get the json object from the api call which has the data we need
                try {
                    JSONArray results = jsonObject.getJSONArray("results"); // If we looked at the json data, we would have seen that the data is stored in a array, so we do getJSONArray (make sure you handle the exception)
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG,"Movies " + movies.size());
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