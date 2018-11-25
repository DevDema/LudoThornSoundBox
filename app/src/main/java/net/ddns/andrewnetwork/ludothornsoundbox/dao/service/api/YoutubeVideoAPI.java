package net.ddns.andrewnetwork.ludothornsoundbox.dao.service.api;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.YoutubeAPIService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeVideoAPI extends YoutubeAPI {

    private YoutubeAPIService youtubeAPIService;
    private String videoId;

    public YoutubeVideoAPI(YoutubeAPIService service, String videoId) {
        this.youtubeAPIService = service;
        this.videoId=videoId;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        if(!isCancelled()) {
            List<Video> videos = new ArrayList<>();
            YouTube.Videos.List videoSearch;
            try {
                videoSearch = tubeService.videos().list("id,snippet,statistics");
                videoSearch.setKey(key);
                videoSearch.setId(videoId);
                final VideoListResponse videoListResponse = videoSearch.execute();
                videos = videoListResponse.getItems();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!videos.isEmpty() && videos.size() == 1) return videos.get(0);
            else return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        youtubeAPIService.onVideoInformationLoaded(result);
    }
}
