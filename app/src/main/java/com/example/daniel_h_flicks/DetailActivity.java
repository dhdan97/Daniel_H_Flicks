package com.example.daniel_h_flicks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.Target;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.daniel_h_flicks.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    public static final String TAG = "DetailActivity";
    public static final String YT_API_KEY = BuildConfig.YT_API_KEY;
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String SIMILAR_MOVIES_URL = "https://api.themoviedb.org/3/movie/%d/similar?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US&page=1"; //To show similar movies

    List<Movie> similarMovies;
    Context context = this.context;

    TextView tvDetailTitle;
    TextView tvDetailOverview;
    TextView tvReleaseDate;
    RatingBar ratingBar;
    YouTubePlayerView playerView;
    ImageView ivSimilarMovie1;
    ImageView ivSimilarMovie2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        similarMovies = new ArrayList<>();

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailOverview = findViewById(R.id.tvDetailOverview);
        ratingBar = findViewById(R.id.ratingBar);
        playerView = findViewById(R.id.player);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        ivSimilarMovie1 = findViewById(R.id.ivSimilarMovie1);
        ivSimilarMovie2 = findViewById(R.id.ivSimilarMovie2);


        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        tvDetailTitle.setText(movie.getTitle());
        tvDetailOverview.setText(movie.getOverview());
        tvReleaseDate.setText(String.format("Release Date: %s", movie.getReleaseDate()));
        ratingBar.setRating((float) movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient(); // boilerplate for GET api network request
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject; // Get the json object from the api call which has the data we need
                try {
                    JSONArray results  = jsonObject.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    Log.d(TAG, youtubeKey);
                    initalizeYoutube(youtubeKey, movie);
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse Json." );
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        client.get(String.format(SIMILAR_MOVIES_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results  = jsonObject.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }
                    // Add movie posters here; use posterPath
                    Log.i(TAG, "Results: " + results.toString());
                    similarMovies.addAll(Movie.fromJsonArray(results)); //Works
                    Log.i(TAG, "First Similar Movie: " + similarMovies.get(0).getTitle());
                    Log.i(TAG, "First Similar Movie imageUrl: " + similarMovies.get(0).getPosterPath());

                    String imageUrl1 = similarMovies.get(0).getPosterPath();
                    String imageUrl2 = similarMovies.get(1).getPosterPath();
                    int radius = 100;

                    //Glide.with(context)
                    //        .load(imageUrl1)
                    //        .into(ivSimilarMovie1);
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse Json." );
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });


    }

    private void initalizeYoutube(String youtubeKey, Movie movie) {
        playerView.initialize(YT_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                Log.d(TAG, "Success initializing YouTube player");
                if(movie.getRating() > 5.0){
                    youTubePlayer.loadVideo(youtubeKey);
                } else { youTubePlayer.cueVideo(youtubeKey); }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.d(TAG, "Error initializing YouTube player");
            }
        });
    }
}