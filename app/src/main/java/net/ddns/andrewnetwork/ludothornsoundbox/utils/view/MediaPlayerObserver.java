package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

public interface MediaPlayerObserver {

    void notifyPlaying();

    void notifyStopped();

    void notifyPaused();

    void notifyResumed();
}
