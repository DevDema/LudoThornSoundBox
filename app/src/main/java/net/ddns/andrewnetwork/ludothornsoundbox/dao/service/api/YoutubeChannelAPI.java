package net.ddns.andrewnetwork.ludothornsoundbox.dao.service.api;

import com.google.api.services.youtube.YouTube;

import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.YoutubeAPIService;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.model.ChannelResponse;

import java.io.IOException;

public class YoutubeChannelAPI extends YoutubeAPI {
    private ChannelResponse channelResponse;
    private YoutubeAPIService youtubeAPIService;

    public YoutubeChannelAPI(YoutubeAPIService service, Channel channel) {
        channelResponse = new ChannelResponse(channel.getChannelUsername(), channel.getId());
        this.youtubeAPIService=service;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        if(!isCancelled()) {
            YouTube.Channels.List channels;
            try {
                channels=tubeService.channels().list("statistics");
                channels.setKey(key);
                if(channelResponse.getUsername()!=null && !channelResponse.getUsername().isEmpty())
                    channels.setForUsername(channelResponse.getUsername());
                else if(channelResponse.getId()!=null && !channelResponse.getId().isEmpty())
                    channels.setId(channelResponse.getId());
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
