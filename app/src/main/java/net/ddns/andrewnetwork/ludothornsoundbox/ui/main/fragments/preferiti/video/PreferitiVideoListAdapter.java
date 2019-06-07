package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ItemVideoBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ItemVideoPreferitoBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.IAudioVideoAdaptersBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.child.VideoRecyclerAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.VideoUtils;

import java.util.List;

public class PreferitiVideoListAdapter extends PreferitiListAdapter<LudoVideo> {


    PreferitiVideoListAdapter(IFragmentVideoPreferitiAdapterBinder adapterBinder, IAudioVideoAdaptersBinder audioVideoBinder, Context context, List<LudoVideo> list) {
        super(adapterBinder, audioVideoBinder, context, list);
    }

    @Override
    protected ViewHolder onCreateCustomViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new PreferitiVideoListAdapter.VideoViewHolder(
                inflater.inflate(R.layout.item_video_preferito, parent, false), (IFragmentVideoPreferitiAdapterBinder) mBinder);
    }

    private class VideoViewHolder extends VideoRecyclerAdapter.VideoAbstractViewHolder {

        private final IFragmentVideoPreferitiAdapterBinder mAdapterBinder;
        private final ItemVideoPreferitoBinding mBinding;

        private VideoViewHolder(@NonNull View itemView, IFragmentVideoPreferitiAdapterBinder adapterBinder) {
            super(mContext, itemView);

            this.mAdapterBinder = adapterBinder;
            this.mBinding = ItemVideoPreferitoBinding.bind(itemView);
        }

        @Override
        protected void set(LudoVideo item, int position) {
            super.set(item, position);

            mBinding.icon.setImageDrawable(null);

            if (item.getThumbnail() == null || item.getThumbnail().getImage() == null) {
                showLoading();
                mAdapterBinder.loadThumbnail(item, thumbnail -> {
                    if (thumbnail.getImage() != null) {

                        mBinding.icon.setImageBitmap(thumbnail.getImage());
                    } else {
                        mBinding.icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_error_outline_white_24dp));
                    }
                    hideLoading();
                });
            } else {
                mBinding.icon.setImageBitmap(item.getThumbnail().getImage());
                hideLoading();
            }

            if (item.getChannel() == null) {
                mAdapterBinder.loadChannel(item, channel -> {
                    if (channel != null) {
                        TextView textView = itemView.findViewById(R.id.videochannel);
                        textView.setText(channel.getChannelName());
                    }
                    hideLoading();
                });
            }

            mBinding.preferitoButton.setOnClickListener(v -> {

                setSettingPreferito(position, true);

                mAdapterBinder.onPreferitoIntentDelete(item, preferitoDeleted -> {
                    setSettingPreferito(position, false);
                });
            });


            if(isSettingPreferito) {
                mBinding.progressBackground.setVisibility(View.VISIBLE);
            }


            mBinding.preferitoButton.setActivated(!isSettingPreferito);
            mBinding.preferitoButton.setEnabled(!isSettingPreferito);

            setPreferitoListener(item, position);

            mBinding.getRoot().setOnClickListener(v -> mAdapterBinder.apriVideo(item));
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

        private void setPreferitoListener(LudoVideo video, int position) {
            if (mBinding.preferitoButton.isActivated()) {
                mBinding.preferitoButton.setOnClickListener(v -> {
                    mBinding.preferitoButton.setActivated(false);

                    setSettingPreferito(position, true);

                    mBinder.onPreferitoIntentDelete(video, preferitoDeleted -> setSettingPreferito(position, false));

                    setPreferitoListener(video, position);
                });
            } else {
                mBinding.preferitoButton.setOnClickListener(v -> {
                    mBinding.preferitoButton.setActivated(true);

                    setSettingPreferito(position, false);

                    mBinder.cancelPreferitoIntentDelete();
                    setPreferitoListener(video, position);
                });
            }
        }
    }


}
