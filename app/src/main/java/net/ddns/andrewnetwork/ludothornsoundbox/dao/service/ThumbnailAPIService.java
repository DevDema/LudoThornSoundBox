package net.ddns.andrewnetwork.ludothornsoundbox.dao.service;

import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.api.ThumbnailAPI;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.VideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Thumbnail;

import java.net.MalformedURLException;

public class ThumbnailAPIService {

    VideoFragment videoFragment;
    ThumbnailAPI process;
    public ThumbnailAPIService(VideoFragment videoFragment) {
        this.videoFragment = videoFragment;
    }

    /**
     *
     * @param result risultato
     */
    public void onThumbnailLoaded(Object result) {
        videoFragment.onThumbnailLoaded((Thumbnail) result);
    }

    /**
     *
     * @param video video
     * @throws MalformedURLException eccezione
     */
    public void loadThumbnail(LudoVideo video) throws MalformedURLException {
        process = new ThumbnailAPI(this, video);
        process.execute();
    }

    public void removeCallBacks() {
        if(process!=null)
        process.cancel(true);
    }
}
