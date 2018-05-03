package com.wills.carl.hotmoviesearch.Utils;

/**
 * Created by Carl on 5/1/2018.
 */


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;



/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    private final static String MOVIE_BASE_URL =
            "https://api.themoviedb.org/3/movie/";

    private final static String API_KEY =
            //TODO: ADD API KEY HERE
            "";


    private final static String API_PARAM_QUERY = "api_key";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    private final static String PARAM_LANGUAGE = "language";
    private final static String DEFAULT_LANGUAGE = "en-US";
    private final static String PARAM_PAGE = "page";
    private final static String DEFAULT_PAGE = "1";

    public static URL buildPopularUrl(){
        return buildUrl("popular");
    }

    public static URL buildTopRatedUrl(){
        return buildUrl("top_rated");
    }

    /**
     * Builds the URL used to query GitHub.
     *
     * @param endpoint either "popular" or "top_rated"
     * @return The URL to use to query the GitHub.
     */
    private static URL buildUrl(String endpoint) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + endpoint).buildUpon()
                .appendQueryParameter(API_PARAM_QUERY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, DEFAULT_LANGUAGE)
                .appendQueryParameter(PARAM_PAGE, DEFAULT_PAGE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e){
            Log.d("ERROR", e.getMessage());
            return null;

        } finally{
            urlConnection.disconnect();
        }
    }
}