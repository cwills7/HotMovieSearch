package com.wills.carl.hotmoviesearch.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Carl on 5/2/2018.
 */

public class Movie implements Serializable{

    private int id;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private ArrayList<Video> videoList;
    private ArrayList<Review> reviewList;


    //id, title, overview, release date, vote avg, video, populatiry, poster path
    public Movie(int id, double voteAverage, String title, double popularity,
                 String posterPath, String overview, String releaseDate) {
        this.id= id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        videoList= new ArrayList<>();
        reviewList = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<Video> getVideoList() {
        return videoList;
    }
    public void setVideoList(ArrayList<Video> videoList){
        this.videoList = videoList;
    }
    public ArrayList<Review> getReviewList() {
        return reviewList;
    }
    public void setReviewList(ArrayList<Review> reviewList){
        this.reviewList = reviewList;
    }
}