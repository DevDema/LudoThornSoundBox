package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video;


import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.IFragmentPreferitiAdapterBinder;

public interface IFragmentVideoPreferitiAdapterBinder extends IFragmentPreferitiAdapterBinder<LudoVideo> {

    interface OnChannelLoadedListener {

        void onChannelLoaded(Channel channel);
    }

    void apriVideo(LudoVideo item);

    void loadChannel(LudoVideo video, OnChannelLoadedListener onChannelLoadedListener);
}
