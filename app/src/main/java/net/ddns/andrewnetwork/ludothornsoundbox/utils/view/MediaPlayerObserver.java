package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;

public interface MediaPlayerObserver {

    void notifyPlaying(LudoAudio audio);

    void notifyStopped();

    void notifyPaused();

    void notifyResumed();

    void notifyFinished();
}
