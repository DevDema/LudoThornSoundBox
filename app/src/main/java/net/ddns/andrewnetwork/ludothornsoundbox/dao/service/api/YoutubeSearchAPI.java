package net.ddns.andrewnetwork.ludothornsoundbox.dao.service.api;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.YoutubeAPIService;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Channel;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeSearchAPI extends YoutubeAPI {
    private YoutubeAPIService youtubeAPIService;
    private final long NUMBER_OF_VIDEOS_RETURNED = 10;
    private List<Channel> channelList;
    private boolean selectiveSearch;
    public YoutubeSearchAPI(YoutubeAPIService service) {
        super();
        this.youtubeAPIService = service;
        channelList =getChannelsWithUnfinishedVideos(service.getChannelList());
        selectiveSearch=false;
    }

    public YoutubeSearchAPI(YoutubeAPIService service, List<Channel> channelList) {
        super();
        this.youtubeAPIService = service;
        this.channelList=channelList;
        selectiveSearch=true;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        if(!isCancelled()) {
            boolean maxReached = false;
            Long maxresults;
            maxresults=setMaxResults();
            if(maxresults>50) {
                youtubeAPIService.setExecutionNumber(1);
                maxresults=setMaxResults();
                maxReached=true;
            }
            List<SearchResult> searchResultList = new ArrayList<>();
            YouTube.Search.List search;
            for (Channel channel : channelList)
                try {
                    if(!channel.areAllVideosLoaded()) {
                        String nextPageToken = channel.getNextPageToken();
                        search = tubeService.search().list("id,snippet");
                        search.setKey(key);
                        search.setChannelId(channel.getId());
                        search.setType("video");
                        if (nextPageToken != null) search.setPageToken(nextPageToken);
                        search.setOrder("date");
                        search.setMaxResults((long) 50);
                        final SearchListResponse searchResponse = search.execute();
                        //youtubeAPIService.setNumberOfVideos(searchResponse.getPageInfo().getTotalResults());
                        if (maxReached)channel.setNextPageToken(searchResponse.getNextPageToken());
                        else searchResultList.addAll(searchResponse.getItems());
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(!maxReached) {
                int start = (int) (NUMBER_OF_VIDEOS_RETURNED * (youtubeAPIService.getExecutionNumber() - 1));
                int end = maxresults.intValue();
                if (start < end && searchResultList.size() >= end)
                    searchResultList = searchResultList.subList(start, end);
                return searchResultList;
            }
        }
        return null;
    }

    private int getCountofChannelsWithUnfinishedVideos() {
        return getChannelsWithUnfinishedVideos(channelList).size();
    }

    private List<Channel> getChannelsWithUnfinishedVideos(List<Channel> channelList) {
        ArrayList<Channel> channels = new ArrayList<>();
        for(Channel channel : channelList) if(!channel.areAllVideosLoaded()) channels.add(channel);
        return channels;
    }
    @Override
    protected void onPostExecute(Object result) {
        try {
            if (selectiveSearch) {
                Channel channel = channelList.get(0);
                youtubeAPIService.onVideosLoaded(result, channel);
            } else youtubeAPIService.onVideosLoaded(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

        private Long setMaxResults() {
            Long maxresults;
            if(channelList.size()==1) maxresults = NUMBER_OF_VIDEOS_RETURNED * (youtubeAPIService.getExecutionNumber());
            else maxresults = (NUMBER_OF_VIDEOS_RETURNED * (youtubeAPIService.getExecutionNumber())/getCountofChannelsWithUnfinishedVideos());
            return maxresults;
        }
}

