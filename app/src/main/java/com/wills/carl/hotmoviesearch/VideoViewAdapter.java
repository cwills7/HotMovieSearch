package com.wills.carl.hotmoviesearch;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wills.carl.hotmoviesearch.Model.Video;

import java.util.ArrayList;

public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.ViewHolder> {
    private static ArrayList<Video> videoList;
    private final LayoutInflater inflater;


    public VideoViewAdapter(Context c, ArrayList<Video> videos){
        this.inflater = LayoutInflater.from(c);
        videoList = new ArrayList<>();
        videoList = videos;
    }

    @Override
    public VideoViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type){
        View v = inflater.inflate(R.layout.trailer_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Video v = videoList.get(position);
       holder.playButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startYoutubeIntent(view.getContext(), v.getId(), v.getKey());
               Toast.makeText(view.getContext(), "CLICKED PLAY ON " + videoList.get(position).getName(), Toast.LENGTH_SHORT).show();
           }
       });
       holder.videoTitle.setText(videoList.get(position).getName());
    }

    public void startYoutubeIntent(Context context, String videoId, String videoKey){
        Intent youtubeApp = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+ videoKey));
        Intent webApp = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));

        try {
            context.startActivity(youtubeApp);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webApp);
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void reset(ArrayList<Video> videos){
        if (videoList == null || videoList.size() > 0){
            videoList = new ArrayList<Video>();
        }
        videoList = videos;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageButton playButton;
        private final TextView videoTitle;
        private ViewHolder(final View v){
            super(v);
            playButton = v.findViewById(R.id.play_button);
            videoTitle = v.findViewById(R.id.video_name);
        }
    }
}
