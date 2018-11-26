package net.ddns.andrewnetwork.ludothornsoundbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.youtube.model.Video;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.controller.VideoInformationManager;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.VideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Thumbnail;

import java.util.ArrayList;


public class VideoAdapter extends ArrayAdapter<Video> {

    ArrayList<LudoVideo> videoList;
    ImageView tt3;
    VideoFragment videoFragment;
    public VideoAdapter(VideoFragment videoFragment, ArrayList<LudoVideo> videoList, int resource) {
        super(videoFragment.getContext(), resource);
        this.videoFragment=videoFragment;
        this.videoList=videoList;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        if (convertView == null) {
            Context context = videoFragment.getContext();
            if(context!=null) inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null && getCount()>0) {

            View row = inflater.inflate(R.layout.object_video, parent, false);

            LudoVideo p = videoList.get(position);
            if (p != null) {
                TextView tt1 = row.findViewById(R.id.videotitle);
                TextView tt2 = row.findViewById(R.id.videodesc);
                TextView channel = row.findViewById(R.id.videochannel);
                TextView likes = row.findViewById(R.id.likes);
                TextView dislikes = row.findViewById(R.id.dislikes);
                TextView views = row.findViewById(R.id.views);
                TextView updated = row.findViewById(R.id.Updated);


                tt3 = row.findViewById(R.id.icon);

                if (tt1 != null) {
                    tt1.setText(p.getTitle());
                }

                if (tt2 != null) {
                    tt2.setText(p.getDescription());
                }

                if (channel != null) {
                    channel.setText(p.getChannel().getChannelName());
                }
                VideoInformationManager videoInformationManager = new VideoInformationManager(videoFragment, p);
                    if (likes != null) {
                        likes.setText(videoInformationManager.getCompactedLikes());
                    }

                    if (dislikes != null) {
                        dislikes.setText(videoInformationManager.getCompactedDislikes());
                    }

                    if (views != null) {
                        views.setText(videoInformationManager.getCompactedViews());

                    }

                if (tt3 != null) {
                    Thumbnail thumbnail = p.getThumbnail();
                    Bitmap image = null;
                    if(thumbnail!=null) image=thumbnail.getImage();
                    if(image!=null) tt3.setImageBitmap(image);

                }
                if(updated!=null)
                    if(videoFragment.isTablet()) updated.setText(p.getDateTime().toExtendedString());
                    else updated.setText(p.getDateTime().toString());
            }
            row.setOnClickListener(v -> {
                Uri uri = Uri.parse(buildVideoUrl(p.getId()));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                row.getRootView().getContext().startActivity(intent);
            });
            convertView = row;
       }
    }
        return convertView;
    }



    /**
     *
     * @param id videoId
     * @return complete video string
     */
    public String buildVideoUrl(String id) {
        return "https://www.youtube.com/watch?v="+id;
    }
}
