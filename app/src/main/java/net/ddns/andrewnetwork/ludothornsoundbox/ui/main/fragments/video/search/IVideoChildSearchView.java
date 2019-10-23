package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.search;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.FragmentVideoChildBinder;

import java.util.List;

public interface IVideoChildSearchView extends FragmentVideoChildBinder.FragmentVideoChild {

    void onSearchMoreVideosLoaded(List<LudoVideo> videoList);
}
