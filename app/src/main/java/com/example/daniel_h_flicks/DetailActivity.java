package com.example.daniel_h_flicks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.daniel_h_flicks.models.Movie;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {
    TextView tvDetailTitle;
    TextView tvDetailOverview;
    RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailOverview = findViewById(R.id.tvDetailOverview);
        ratingBar = findViewById(R.id.ratingBar);


        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvDetailTitle.setText(movie.getTitle());
        tvDetailOverview.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getRating());
    }
}