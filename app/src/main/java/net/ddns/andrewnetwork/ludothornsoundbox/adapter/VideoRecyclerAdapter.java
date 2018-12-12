package net.ddns.andrewnetwork.ludothornsoundbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.controller.VideoInformationManager;
import net.ddns.andrewnetwork.ludothornsoundbox.controller.VideoManager;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.ParentFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Thumbnail;

import java.util.List;
import java.util.Objects;

public class VideoRecyclerAdapter extends RecyclerView.Adapter {

    ParentFragment fragment;
    List<LudoVideo> videoList;

    private static final int PROGRESS = 1;
    private static final int VIDEO = 0;

    public VideoRecyclerAdapter(ParentFragment fragment, List<LudoVideo> videoList) {
        this.fragment = fragment;
        this.videoList = videoList;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if(position==getItemCount()-1)
            return PROGRESS;
        else return VIDEO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(fragment.getContext()).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.object_video, parent, false);
        if(i == PROGRESS) {
            v = inflater.inflate(R.layout.progress_bar_item, parent, false);
            return new ProgressViewHolder(v);
        }
        else return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder.getItemViewType() == 0) {
            LudoVideo p = videoList.get(position);
            if (p != null) {
                TextView tt1 = viewHolder.itemView.findViewById(R.id.videotitle);
                TextView tt2 = viewHolder.itemView.findViewById(R.id.videodesc);
                TextView channel = viewHolder.itemView.findViewById(R.id.videochannel);
                TextView likes = viewHolder.itemView.findViewById(R.id.likes);
                TextView dislikes = viewHolder.itemView.findViewById(R.id.dislikes);
                TextView views = viewHolder.itemView.findViewById(R.id.views);
                TextView updated = viewHolder.itemView.findViewById(R.id.Updated);


                ImageView tt3 = viewHolder.itemView.findViewById(R.id.icon);

                if (tt1 != null) {
                    tt1.setText(p.getTitle());
                }

                if (tt2 != null) {
                    tt2.setText(p.getDescription());
                }

                if (channel != null) {
                    channel.setText(p.getChannel().getChannelName());
                }
                VideoInformationManager videoInformationManager = new VideoInformationManager(fragment, p);
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
                    if (thumbnail != null) image = thumbnail.getImage();
                    if (image != null) tt3.setImageBitmap(image);

                }
                if (updated != null)
                    if (fragment.isTablet()) updated.setText(p.getDateTime().toExtendedString());
                    else updated.setText(p.getDateTime().toString());
            }
            viewHolder.itemView.setOnClickListener(v -> {
                Uri uri = null;
                if (p != null) {
                    uri = Uri.parse(VideoManager.buildVideoUrl(p.getId()));
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                viewHolder.itemView.getRootView().getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private VideoViewHolder(View v) {
            super(v);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressViewHolder(View v) {
            super(v);
        }
    }

    public void setVideoList(List<LudoVideo> videoList) {
        this.videoList = videoList;
    }
}
