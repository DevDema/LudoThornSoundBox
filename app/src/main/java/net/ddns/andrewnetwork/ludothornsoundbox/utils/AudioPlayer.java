package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.utils.DataSingleTon;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.constraintlayout.widget.ConstraintLayout;

public class AudioPlayer extends ConstraintLayout {

    private LudoAudio audio;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private TextView audioText;
    private ImageButton playButton;
    private ImageButton stopButton;
    private boolean isPlaying;
    private MediaPlayer.OnCompletionListener onCompletionListener;
    private ProgressBar audioProgress;
    private MediaPlayer.OnCompletionListener defaultcompletionListener = mp -> {
        playButton.setImageResource(R.drawable.ic_play_white);
        isPlaying = false;

        playButton.setOnClickListener(v -> play());


        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(mp);
        }
    };


    public AudioPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        inflate();
        bindViews();
    }

    private void bindViews() {
        audioText = findViewById(R.id.audio_played_label);
        playButton = findViewById(R.id.play);
        stopButton = findViewById(R.id.stop);
        audioProgress = findViewById(R.id.audio_progress);
        audioText.setMovementMethod(new ScrollingMovementMethod());

        audioProgress.getProgressDrawable().setColorFilter(
                Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

        playButton.setOnClickListener(v -> play());

        stopButton.setOnClickListener(v -> stop());
    }

    private void inflate() {

        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.audio_player, this, true);
    }

    public void play() {
        if (audio != null) {
            isPlaying = true;
            playButton.setImageResource(R.drawable.ic_pause_white);
            AudioUtils.playTrack(mContext, audio, defaultcompletionListener);

            playButton.setOnClickListener(v -> {
                try {
                    if (isPlaying) {
                        pause();
                    } else {
                        resume();
                    }
                } catch (IllegalStateException e) {
                    Log.v("MediaPlayer", "MediaPlayer detached, skipping...");
                }
            });

            ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
            audioProgress.setMax(DataSingleTon.getInstance().getMediaPlayer().getDuration());
            service.scheduleWithFixedDelay(() -> audioProgress.setProgress(DataSingleTon.getInstance().getMediaPlayer().getCurrentPosition()), 1, 1, TimeUnit.MICROSECONDS);
        }
    }

    public void pause() {
        DataSingleTon.getInstance().getMediaPlayer().pause();
        playButton.setImageResource(R.drawable.ic_play_white);


        isPlaying = false;
    }

    public void resume() {
        DataSingleTon.getInstance().getMediaPlayer().start();
        playButton.setImageResource(R.drawable.ic_pause_white);


        isPlaying = true;
    }

    public void stop() {
        if (audio != null) {
            AudioUtils.stopTrack();
            setAudio(null);

            isPlaying = false;
        }
    }

    public void setAudio(LudoAudio audio) {
        this.audio = audio;

        if (audio != null) {
            audioText.setText(audio.getTitle());
        } else {
            audioText.setText(mContext.getResources().getString(R.string.nessun_audio_in_riproduzione_label));
            playButton.setImageResource(R.drawable.ic_play_white);
        }

    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }
}
