package com.wills.carl.hotmoviesearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wills.carl.hotmoviesearch.Model.Review;

import java.util.ArrayList;

public class ReviewViewAdapter extends RecyclerView.Adapter<ReviewViewAdapter.ViewHolder>{
    public ArrayList<Review> reviewList;
    private LayoutInflater inflater;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView reviewAuthor;
        private final TextView reviewDetail;
        private ViewHolder(final View v){
            super(v);
            reviewAuthor = v.findViewById(R.id.author_tv);
            reviewDetail = v.findViewById(R.id.review_tv);

        }
    }

    public ReviewViewAdapter(Context c, ArrayList<Review> reviews){
        this.inflater = LayoutInflater.from(c);
        reviewList = new ArrayList<>();
        reviewList = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.review_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Review review = reviewList.get(position);

        holder.reviewDetail.setText("\"" +review.getContent() + "\"");
        holder.reviewAuthor.setText("\n-" +review.getAuthor() + "\n");
        //TODO: Fill the layout
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public void reset(ArrayList<Review> reviews){
        reviewList.clear();
        reviewList = reviews;
        notifyDataSetChanged();
    }

}
