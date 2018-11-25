package net.ddns.andrewnetwork.ludothornsoundbox.dao.service.api;


import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;

import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.YoutubeAPIService;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.model.ChannelResponse;

import java.io.IOException;
import java.util.List;

public class YoutubeChannelAPI extends YoutubeAPI {
    ChannelResponse channelResponse;
    YoutubeAPIService youtubeAPIService;
    public YoutubeChannelAPI(YoutubeAPIService service, Channel channel) {
        channelResponse = new ChannelResponse(channel.getChannelUsername());
        this.youtubeAPIService=service;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        if(!isCancelled()) {
            YouTube.Channels.List channels;
            try {
                channels=tubeService.channels().list("statistics");
                channels.setKey(key);
                channels.setForUsername(channelResponse.getUsername());
                final com.google.api.services.youtube.model.Channel channelListResponse = channels.execute().getItems().get(0);
                channelResponse.setId(channelListResponse.getId());
                channelResponse.setTotalnumberOfVideos(channelListResponse.getStatistics().getVideoCount());
                return channelResponse;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Object result) {
        youtubeAPIService.onChannelsLoaded(result);
    }

}
