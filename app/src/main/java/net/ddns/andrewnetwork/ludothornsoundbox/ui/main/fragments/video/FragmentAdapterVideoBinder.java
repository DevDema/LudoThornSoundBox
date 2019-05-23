package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter;

public interface FragmentAdapterVideoBinder {

    void aggiungiPreferito(LudoVideo video, PreferitiListAdapter.PreferitoDeletedListener<LudoVideo> preferitoDeletedListener);

    void loadThumbnail(LudoVideo item, PreferitiListAdapter.ThumbnailLoadedListener thumbnailLoadedListener);

    void rimuoviPreferito(LudoVideo item, PreferitiListAdapter.PreferitoDeletedListener<LudoVideo> preferitoDeletedListener);
}
