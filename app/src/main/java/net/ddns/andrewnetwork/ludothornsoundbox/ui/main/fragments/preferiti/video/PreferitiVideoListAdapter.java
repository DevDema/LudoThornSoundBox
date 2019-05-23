package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.IFragmentPreferitiAdapterBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.IFragmentAudioPreferitiAdapterBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoRecyclerAdapter;

import java.util.List;

public class PreferitiVideoListAdapter extends PreferitiListAdapter<LudoVideo> {

    public PreferitiVideoListAdapter(IFragmentVideoPreferitiAdapterBinder binder, Context context, List<LudoVideo> list) {
        super(binder, context, list);
    }

    @Override
    protected ViewHolder onCreateCustomViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new PreferitiVideoListAdapter.VideoViewHolder(
                inflater.inflate(R.layout.item_video, parent, false), (IFragmentVideoPreferitiAdapterBinder) mBinder);
    }

    private class VideoViewHolder extends VideoRecyclerAdapter.VideoViewHolder {

        private final IFragmentVideoPreferitiAdapterBinder mBinder;

        private VideoViewHolder(@NonNull View itemView, IFragmentVideoPreferitiAdapterBinder binder) {
            super(mContext, itemView);

            this.mBinder = binder;
        }

        @Override
        protected void set(LudoVideo item, int position) {
            super.set(item, position);

            ImageView imageView = itemView.findViewById(R.id.icon);

            imageView.setImageDrawable(null);

            if (item.getThumbnail() == null || item.getThumbnail().getImage() == null) {
                showLoading();
                mBinder.loadThumbnail(item, thumbnail -> {
                    if (thumbnail.getImage() != null) {

                        imageView.setImageBitmap(thumbnail.getImage());
                    } else {
                        imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_error_outline_white_24dp));
                    }
                    hideLoading();
                });
            } else {
                imageView.setImageBitmap(item.getThumbnail().getImage());
                hideLoading();
            }

            if (item.getChannel() == null) {
                mBinder.loadChannel(item, channel -> {
                    if (channel != null) {
                        TextView textView = itemView.findViewById(R.id.videochannel);
                        textView.setText(channel.getChannelName());
                    }
                    hideLoading();
                });
            }
        }

        private void hideLoading() {
            ProgressBar progressBar = itemView.findViewById(R.id.loading_icon);
            ImageView imageView = itemView.findViewById(R.id.icon);

            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        private void showLoading() {
            ProgressBar progressBar = itemView.findViewById(R.id.loading_icon);
            ImageView imageView = itemView.findViewById(R.id.icon);

            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

        }
    }
}
