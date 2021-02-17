package com.example.daniel_h_flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie { // making a class Movie that so we can put the data from the JsonObject into a java object we can use. We obtained the jsoObject back over at MainActivity.java

    String posterPath;
    String title;
    String overview;
    String backdropPath;
    double rating;
    int movieId;


    public String getPosterPath() { // get methods so we can get info from movie objects we make in MainActivity.java
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath); // Note that size(w342) is hard coded in this instance, might have to change for landscape mode
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath); //for landscape mode
    }

    public Movie(JSONObject jsonObject) throws JSONException { // Movies will have posters, titles, and an overview
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        rating = jsonObject.getDouble("vote_average");
        movieId = jsonObject.getInt("id");
    }

    // Empty constructor in order to wrap movie objects in parcel
    public Movie() {

    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException { // Making a list of Movie objects from JSON array which containsJSON objects, which are the movies
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }
}
