package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ItemAudioPreferitiBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.IAudioVideoAdaptersBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;

import java.util.HashMap;
import java.util.List;

import static net.ddns.andrewnetwork.ludothornsoundbox.utils.ViewUtils.compareDrawables;


public class PreferitiAudioListAdapter extends PreferitiListAdapter<LudoAudio> {

    private static HashMap<String, Bitmap> drawables = new HashMap<>();
    private static HashMap<String, Boolean> isPlaying = new HashMap<>();
    private static HashMap<String, Boolean> videoAvailable = new HashMap<>();

    PreferitiAudioListAdapter(IFragmentAudioPreferitiAdapterBinder binder, IAudioVideoAdaptersBinder audioVideoBinder, Context context, List<LudoAudio> list) {
        super(binder, audioVideoBinder, context, list);

        initVideoAvailable();
    }

    private void initVideoAvailable() {
        for (LudoAudio audio : list) {
            videoAvailable.put(audio.getTitle(), audio.getVideo() != null && audio.getVideo().getId() != null);
        }
    }

    interface VideoLoadedListener {

        void onVideoLoaded(LudoVideo video);
    }

    @Override
    protected ViewHolder onCreateCustomViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new PreferitiAudioListAdapter.AudioViewHolder(
                inflater.inflate(R.layout.item_audio_preferiti, parent, false),
                (IFragmentAudioPreferitiAdapterBinder) mBinder);
    }

    public class AudioViewHolder extends ViewHolder<LudoAudio> {

        private final ItemAudioPreferitiBinding mBinding;
        private final IFragmentAudioPreferitiAdapterBinder mBinder;

        private AudioViewHolder(View v, IFragmentAudioPreferitiAdapterBinder binder) {
            super(v);
            mBinding = DataBindingUtil.bind(v);
            mBinder = binder;

        }

        public void set(LudoAudio item, int position) {


            mBinding.thumbnailProgressBar.getIndeterminateDrawable().setColorFilter(
                    mContext.getResources().getColor(R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN);

            mBinding.progressBar.getIndeterminateDrawable().setColorFilter(
                    mContext.getResources().getColor(R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN);


            mBinding.titleLabel.setText(item.getTitle());

            LudoVideo video = item.getVideo();
            Boolean isPlayin = isPlaying.get(item.getTitle());
            if (isPlayin != null && isPlayin) {
                mBinding.playButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_stop_black_24dp));
            } else {
                mBinding.playButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_play_black_24dp));
            }

            setPlayPauseListener(item, position);

            if (!isAnyPlaying()) {
                Boolean videoAvailabl = videoAvailable.get(item.getTitle());
                if (videoAvailabl != null && videoAvailabl) {
                    if (!isAlreadyLoaded(video)) {
                        showLoading();
                        mBinder.loadVideo(item, videoResponse -> {
                            manageVideoResponse(item, videoResponse);
                            hideLoading();
                        });
                    } else {
                        onVideoAvailableAndLoadDrawable(item, video);

                        hideLoading();
                    }
                } else {
                    onVideoUnavailable(item);

                    hideLoading();
                }
            }

            if (isSettingPreferito) {
                mBinding.progressBackground.setVisibility(View.VISIBLE);
            }

            mBinding.preferitoButton.setActivated(!isSettingPreferito);
            mBinding.preferitoButton.setEnabled(!isSettingPreferito);

            setPreferitoListener(item, position);

        }

        private boolean isAlreadyLoaded(LudoVideo video) {
            return video != null && video.getTitle() != null;
        }

        private boolean isAnyPlaying() {
            return isPlaying.containsValue(true);
        }

        private void manageVideoResponse(LudoAudio item, LudoVideo video) {

            if (video != null && video.getTitle() != null) {
                onVideoAvailableAndLoadDrawable(item, video);
            } else {
                onVideoUnavailable(item);
            }
        }

        private void onVideoAvailableAndLoadDrawable(LudoAudio item, LudoVideo video) {

            onVideoAvailable(item, video);
            callFinalizeVideoLoaded(item);
            loadDrawable(item);
        }

        private void onVideoAvailable(LudoAudio item, LudoVideo video) {

            videoAvailable.put(item.getTitle(), true);

            mBinding.videoTitleLabel.setText(video.getTitle());
            mBinding.videoButton.setOnClickListener(v ->
                    CommonUtils.showDialog(mContext, "Aprire il video corrispondente su youtube?",
                            (dialog, which) -> {
                                mBinder.apriVideo(video);
                                dialog.dismiss();
                            },
                            true
                    )
            );

            mBinding.videoTitleLabel.setVisibility(View.VISIBLE);
            mBinding.videoButton.setVisibility(View.VISIBLE);
        }

        private void onVideoUnavailable(LudoAudio item) {

            videoAvailable.put(item.getTitle(), false);

            mBinding.videoButton.setVisibility(View.GONE);

            mBinding.videoTitleLabel.setText(mContext.getString(R.string.video_non_disponibile_label));

            hideThumbnailLoading();

            mBinding.thumbnailImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_error_outline_white_24dp));
        }

        private void loadDrawable(LudoAudio item) {
            if (item.getVideo().getThumbnail() != null && item.getVideo().getThumbnail().getImage() != null) {
                drawables.put(item.getTitle(), item.getVideo().getThumbnail().getImage());
            }

            if (drawables.get(item.getTitle()) == null) {
                showThumbnailLoading();
                mBinder.loadThumbnail(item, thumbnail -> {
                    if (thumbnail != null) {
                        drawables.put(item.getTitle(), thumbnail.getImage());

                        setDrawable(thumbnail.getImage());
                    } else {
                        hideThumbnailLoading();

                        mBinding.thumbnailImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_error_outline_white_24dp));
                    }
                });
            } else {
                setDrawable(drawables.get(item.getTitle()));
            }
        }

        private void callFinalizeVideoLoaded(LudoAudio audio) {
            mBinder.saveInPref(audio);
        }

        private void setDrawable(Bitmap bitmap) {
            mBinding.thumbnailImage.setImageBitmap(bitmap);

            hideThumbnailLoading();
        }

        private void showThumbnailLoading() {
            mBinding.thumbnailProgressBar.setVisibility(View.VISIBLE);
        }

        private void hideThumbnailLoading() {
            mBinding.thumbnailProgressBar.setVisibility(View.INVISIBLE);
        }

        private void showLoading() {
            mBinding.progressBar.setVisibility(View.VISIBLE);
            mBinding.progressBackground.setVisibility(View.VISIBLE);
        }

        private void hideLoading() {
            mBinding.progressBar.setVisibility(View.INVISIBLE);
            mBinding.progressBackground.setVisibility(View.INVISIBLE);
        }


        private void setPlayPauseListener(LudoAudio audio, int position) {
            Drawable playImage = ContextCompat.getDrawable(mContext, R.drawable.ic_play_black_24dp);
            Drawable stopImage = ContextCompat.getDrawable(mContext, R.drawable.ic_stop_black_24dp);
            if (compareDrawables(mBinding.playButton.getDrawable(), playImage)) {
                mBinding.playButton.setOnClickListener(v -> {
                    mBinding.playButton.setImageDrawable(stopImage);

                    setPlaying(audio, position, true);

                    mBinder.playAudio(audio);

                    mBinder.setOnCompletionListener(mp -> setPlaying(audio, position, false));

                    setPlayPauseListener(audio, position);
                });
            } else {
                mBinding.playButton.setOnClickListener(v -> {
                    mBinding.playButton.setImageDrawable(playImage);

                    setPlaying(audio, position, false);

                    mBinder.stopAudio(audio);

                    setPlayPauseListener(audio, position);
                });
            }
        }

        private void setPlaying(LudoAudio audio, int position, boolean isPlayin) {
            if (isPlayin) {
                isPlaying.clear();

                notifyOtherItemsChanged(position);
            }

            isPlaying.put(audio.getTitle(), isPlayin);

            notifyItemChanged(position);
        }

        private void notifyOtherItemsChanged(int position) {
            for (int i = 0; i < list.size(); i++) {
                if (i != position) {
                    notifyItemChanged(i);
                }
            }
        }

        private void setPreferitoListener(LudoAudio audio, int position) {
            if (mBinding.preferitoButton.isActivated()) {
                mBinding.preferitoButton.setOnClickListener(v -> {
                    mBinding.preferitoButton.setActivated(false);

                    setSettingPreferito(position, true);

                    mBinder.onPreferitoIntentDelete(audio, preferitoDeleted -> setSettingPreferito(position, false));

                    setPreferitoListener(audio, position);
                });
            } else {
                mBinding.preferitoButton.setOnClickListener(v -> {
                    mBinding.preferitoButton.setActivated(true);

                    setSettingPreferito(position, false);

                    mBinder.cancelPreferitoIntentDelete();
                    setPreferitoListener(audio, position);
                });
            }
        }


    }

    void setList(List<LudoAudio> list) {
        this.list = list;

        initVideoAvailable();
    }
}
