package com.wills.carl.hotmoviesearch.Utils;


import android.util.Log;


import com.wills.carl.hotmoviesearch.Model.Movie;
import com.wills.carl.hotmoviesearch.Model.Review;
import com.wills.carl.hotmoviesearch.Model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl on 5/2/2018.
 */

public class JsonUtils {

    public static List<Movie> parseMovies(String json){
        ArrayList<Movie> parsedMovies = new ArrayList<>();

        try{
            JSONObject baseObject = new JSONObject(json);
            JSONArray movies = baseObject.getJSONArray("results");
            for (int i = 0; i < 20; i++){
                //Get the first 20 movies
                JSONObject thisMovie = movies.getJSONObject(i);
                int id = thisMovie.getInt("id");
                double voteAverage = thisMovie.getDouble("vote_average");
                String title = thisMovie.getString("title");
                double popularity = thisMovie.getDouble("popularity");
                String posterPath = thisMovie.getString("poster_path");
                String overview = thisMovie.getString("overview");
                String releaseDate = thisMovie.getString("release_date");

                //Add movies to a list
                parsedMovies.add(new Movie(id, voteAverage, title, popularity, posterPath, overview, releaseDate));
            }


        } catch (JSONException e){
            Log.e("PARSING ERROR", e.getMessage());
        }

        //Return the list of movie objects to display
        return parsedMovies;
    }

    public static ArrayList<Video> parseVideos(String json){
        ArrayList<Video> videoKeys = new ArrayList<>();

        try{
            JSONObject baseObject = new JSONObject(json);
            JSONArray videos = baseObject.getJSONArray("results");
            for (int i = 0; i < videos.length(); i++){
                //Get the first 20 movies
                JSONObject thisVideo = videos.getJSONObject(i);
                String id = thisVideo.getString("id");
                String iso639 = thisVideo.getString("iso_639_1");
                String iso3166 = thisVideo.getString("iso_3166_1");
                String key = thisVideo.getString("key");
                String name = thisVideo.getString("name");
                String site = thisVideo.getString("site");
                int size = thisVideo.getInt("size");
                String type = thisVideo.getString("type");
                //Add video to a list
                videoKeys.add(new Video(id, iso639, iso3166, key, name, site, size, type));
            }
        } catch (JSONException e){
            Log.e("PARSING ERROR", e.getMessage());
        }
        return videoKeys;
    }

    public static ArrayList<Review> parseReviews(String json){
        ArrayList<Review> reviewList = new ArrayList<>();

        try{
            JSONObject baseObject = new JSONObject(json);
            JSONArray reviews = baseObject.getJSONArray("results");
            for (int i = 0; i < reviews.length(); i++){
                //Get the first 20 movies
                JSONObject thisReview = reviews.getJSONObject(i);
                String id = thisReview.getString("id");
                String author = thisReview.getString("author");
                String content = thisReview.getString("content");
                String url = thisReview.getString("url");
                //Add review to a list
                reviewList.add(new Review(author, content, id, url));
            }
        } catch (JSONException e){
            Log.e("PARSING ERROR", e.getMessage());
        }
        return reviewList;
    }
    private static int[] extractIntArray(JSONArray data){
        int[] results = new int[data.length()];
        for(int i = 0; i <data.length(); i++){
            results[i] = data.optInt(i);
        }
        return results;
    }

}
