package com.wills.carl.hotmoviesearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wills.carl.hotmoviesearch.Model.Movie;

import java.util.ArrayList;

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.ViewHolder>{

    private ArrayList<Movie> movieList;
    private final LayoutInflater inflater;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView thumbnail;
        private ViewHolder(final View v){
            super(v);
            thumbnail = v.findViewById(R.id.list_movie_icon);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), MovieDetail.class);
                    i.putExtra("movie", movieList.get(getAdapterPosition()));
                    view.getContext().startActivity(i);
                }
            });
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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos){
        //setImage
        Movie m = movieList.get(pos);
        holder.thumbnail.setContentDescription(m.getTitle());

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
