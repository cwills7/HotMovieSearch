package com.wills.carl.hotmoviesearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.wills.carl.hotmoviesearch.Model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.ViewHolder>{

    private ArrayList<Movie> movieList;
    private LayoutInflater inflater;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView thumbnail;
        public ViewHolder(View v){
            super(v);
            thumbnail = v.findViewById(R.id.list_movie_icon);
        }

    }

    public MovieViewAdapter(Context c, ArrayList<Movie> movies){
        this.inflater = LayoutInflater.from(c);
        movieList = new ArrayList<>();
        movieList = movies;
    }

    @Override
    public MovieViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type){
        View v = inflater.inflate(R.layout.movie_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos){
        //setImage
        Movie m = movieList.get(pos);

        ///////DEBUG PURPOSES ONLY TODO: Remove before submitting
        Picasso.with(holder.thumbnail.getContext())
                .setIndicatorsEnabled(true);


        Picasso.with(holder.thumbnail.getContext())
                .load("http://image.tmdb.org/t/p/w500/" + m.getPosterPath())
                .resize(R.dimen.movie_poster_width, R.dimen.movie_poster_height)
                .placeholder(R.drawable.loading_animator)
                .onlyScaleDown()
                .into(holder.thumbnail);

        Log.d("ImageURL", "http://image.tmdb.org/t/p/w500/" + m.getPosterPath());

    }

    @Override
    public int getItemCount(){
        return movieList.size();
    }

}
