package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

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
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.constraintlayout.widget.ConstraintLayout;

public class AudioPlayer extends ConstraintLayout implements MediaPlayerObserver {

    private LudoAudio audio;
    private Context mContext;
    private TextView audioText;
    private ImageButton playButton;
    private ImageButton stopButton;
    private boolean isPlaying;
    private MediaPlayer.OnCompletionListener onCustomCompletionListener;
    private ProgressBar audioProgress;
    private boolean triggerCustomListener = true;
    private MediaPlayer.OnCompletionListener defaultcompletionListener = mp -> {
        playButton.setImageResource(R.drawable.ic_play_white);
        isPlaying = false;

        playButton.setOnClickListener(v -> play(audio));

        setAudioProgressToZero();

        if (triggerCustomListener) {
            if (onCustomCompletionListener != null) {
                onCustomCompletionListener.onCompletion(mp);
            }
        } else {
            triggerCustomListener = true;
        }
    };



    public AudioPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        inflate();
        bindViews();

        DataSingleTon.getInstance().registerObserver(this);
    }

    private void bindViews() {
        audioText = findViewById(R.id.audio_played_label);
        playButton = findViewById(R.id.play);
        stopButton = findViewById(R.id.stop);
        audioProgress = findViewById(R.id.audio_progress);
        audioText.setMovementMethod(new ScrollingMovementMethod());

        audioProgress.getProgressDrawable().setColorFilter(
                Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);

        playButton.setOnClickListener(v -> play(audio));

        stopButton.setOnClickListener(v -> stop());
    }

    private void inflate() {

        LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.audio_player, this, true);
    }

    public void play(LudoAudio audio) {
        if (audio != null) {
            this.audio = audio;
            AudioUtils.playTrack(mContext, audio);
        }
    }

    @Override
    public void notifyPlaying(LudoAudio audio) {
        isPlaying = true;

        setAudio(audio);

        playButton.setImageResource(R.drawable.ic_pause_white);

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

    public void pause() {
        DataSingleTon.getInstance().getMediaPlayer().pause();
    }

    @Override
    public void notifyPaused() {

        playButton.setImageResource(R.drawable.ic_play_white);

        isPlaying = false;
    }

    public void resume() {
        AudioUtils.resumeTrack();
    }

    @Override
    public void notifyResumed() {

        playButton.setImageResource(R.drawable.ic_pause_white);

        isPlaying = true;
    }

    @Override
    public void notifyFinished() {
        this.triggerCustomListener = false;
        defaultcompletionListener.onCompletion(DataSingleTon.getInstance().getMediaPlayer());
    }

    public void stop() {
        if (audio != null) {
            AudioUtils.stopTrack(onCustomCompletionListener);
        }
    }

    @Override
    public void notifyStopped() {
        setAudio(null);
        setAudioProgressToZero();
        isPlaying = false;
    }

    private void setAudioProgressToZero() {
        audioProgress.setProgress(0);
        audioProgress.setMax(Integer.MAX_VALUE);
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
        this.onCustomCompletionListener = onCompletionListener;
    }
}
