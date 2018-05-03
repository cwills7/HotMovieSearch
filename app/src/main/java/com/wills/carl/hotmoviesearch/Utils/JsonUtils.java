package com.wills.carl.hotmoviesearch.Utils;


import android.util.Log;


import com.wills.carl.hotmoviesearch.Model.Movie;

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
                int voteCount = thisMovie.getInt("vote_count");
                int id = thisMovie.getInt("id");
                boolean video = thisMovie.getBoolean("video");
                double voteAverage = thisMovie.getDouble("vote_average");
                String title = thisMovie.getString("title");
                double popularity = thisMovie.getDouble("popularity");
                String posterPath = thisMovie.getString("poster_path");
                String origLanguage = thisMovie.getString("original_language");
                String origTitle = thisMovie.getString("original_title");
                int[] genreIds = extractIntArray(thisMovie.getJSONArray("genre_ids"));
                String backdropPath = thisMovie.getString("backdrop_path");
                boolean adult = thisMovie.getBoolean("adult");
                String overview = thisMovie.getString("overview");
                String releaseDate = thisMovie.getString("release_date");

                //Add movies to a list
                parsedMovies.add(new Movie(voteCount, id, video, voteAverage, title, popularity, posterPath,
                        origLanguage, origTitle, genreIds, backdropPath, adult, overview, releaseDate));
            }


        } catch (JSONException e){
            Log.e("PARSING ERROR", e.getMessage());
        }

        //Return the list of movie objects to display
        return parsedMovies;
    }

    private static int[] extractIntArray(JSONArray data){
        int[] results = new int[data.length()];
        for(int i = 0; i <data.length(); i++){
            results[i] = data.optInt(i);
        }
        return results;
    }

}
