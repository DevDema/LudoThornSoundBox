package net.ddns.andrewnetwork.ludothornsoundbox.dao.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;

import net.ddns.andrewnetwork.ludothornsoundbox.controller.VideoManager;
import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.api.YoutubeChannelAPI;
import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.api.YoutubeSearchAPI;
import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.api.YoutubeVideoAPI;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.IVideoLoader;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.VideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.model.ChannelResponse;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Date;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.model.VideoInformation;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class YoutubeAPIService {

    IVideoLoader caller;
    YoutubeSearchAPI youtubeSearchAPI;
    YoutubeVideoAPI youtubeVideoAPI;

    public void onVideosLoaded(Object result) {
        if(result!=null) {
            addVideosToChannels((List<SearchResult>) result);
            for(Channel channel : caller.getChannelList())
                VideoManager.removeDuplicatesifThereAreAny(channel.getVideoList());
            caller.onVideosLoaded();
        }
            else loadVideos();
    }

    public void onVideosLoaded(Object result, Channel channel) {
        if(result!=null) {
            addVideosToChannel((List<SearchResult>) result, channel);
            VideoManager.removeDuplicatesifThereAreAny(channel.getVideoList());
            caller.onVideosLoaded(channel);
        }
        else loadVideos(channel);

    }

    public void onVideoInformationLoaded(Object result) {
        if(result!=null)
        caller.onVideoInformationLoaded(extractVideoInformation((Video) result), castToLudoVideo((Video) result));
    }


    public void onChannelsLoaded(Object result) {
        if(result!=null)
            caller.onChannelsLoaded((ChannelResponse) result);
    }


    /**
     *
      * @param searchResultList search results list
     */
    public void addVideosToChannels(List<SearchResult> searchResultList) {
        if (searchResultList != null) {
            for (SearchResult searchResult : searchResultList) {
                SearchResultSnippet searchSnippet = searchResult.getSnippet();
                for (Channel channel : caller.getChannelList())
                    if (searchResult.getId().getKind().equals("youtube#video") && searchSnippet.getChannelId().equals(channel.getId())) {
                        LudoVideo video = new LudoVideo(LudoVideo.Source.YOUTUBE);
                        String videoId = searchResult.getId().getVideoId();
                        video.setId(videoId);
                        video.setTitle(searchSnippet.getTitle());
                        video.setDateTime(new Date(searchSnippet.getPublishedAt()));
                        video.setDescription(searchSnippet.getDescription());
                        video.setChannel(channel);
                        video.setThumbnail(new Thumbnail(searchSnippet.getThumbnails().getMedium().getUrl()));
                        channel.addToVideoList(video);
                    }
            }
        }
    }

    public void addVideosToChannel(List<SearchResult> searchResultList, Channel channel) {
        if (searchResultList != null) {
            for (SearchResult searchResult : searchResultList) {
                SearchResultSnippet searchSnippet = searchResult.getSnippet();
                if (searchResult.getId().getKind().equals("youtube#video") && searchSnippet.getChannelId().equals(channel.getId())) {
                    LudoVideo video = new LudoVideo(LudoVideo.Source.YOUTUBE);
                    String videoId = searchResult.getId().getVideoId();
                    video.setId(videoId);
                    video.setTitle(searchSnippet.getTitle());
                    video.setDateTime(new Date(searchSnippet.getPublishedAt()));
                    video.setDescription(searchSnippet.getDescription());
                    video.setChannel(channel);
                    video.setThumbnail(new Thumbnail(searchSnippet.getThumbnails().getMedium().getUrl()));
                    channel.addToVideoList(video);
                }
            }
        }
    }

    /*public List<Video> getVideoList() throws IOException {
        return convertToVideos(getResultList());
    }*/


    public YoutubeAPIService(IVideoLoader videoFragment) {
        this.caller = videoFragment;

         }

    public void loadVideos() {
            new YoutubeSearchAPI(this).execute();
    }

    public void loadVideos(Channel channel) {
        List<Channel> channelList = new ArrayList<>();
                channelList.add(channel);
        new YoutubeSearchAPI(this, channelList).execute();
    }

    public void loadChannels(List<Channel> channelList) {
        for(Channel channel : channelList)
            new YoutubeChannelAPI(this, channel).execute();
    }

    public void loadVideoInformation(String videoId) {
        youtubeVideoAPI = new YoutubeVideoAPI(this, videoId);
        youtubeVideoAPI.execute();
    }

    public VideoInformation extractVideoInformation(Video video) {
        VideoInformation videoInformation = new VideoInformation();
        VideoStatistics videoStatistics = video.getStatistics();
        if(videoStatistics!=null) {
        videoInformation.setLikes(videoStatistics.getLikeCount());
        videoInformation.setDislikes(videoStatistics.getDislikeCount());
        videoInformation.setViews(videoStatistics.getViewCount());
        }
        return videoInformation;
    }
    /**
     *
     * @param video video in google format
     * @return video in application format
     */
    public LudoVideo castToLudoVideo(Video video) {
        LudoVideo newvideo = new LudoVideo(LudoVideo.Source.YOUTUBE);
        VideoSnippet videoSnippet = video.getSnippet();
        if(videoSnippet!=null) {
        newvideo.setId(video.getId());
        newvideo.setTitle(videoSnippet.getTitle());
        newvideo.setDateTime(new Date(videoSnippet.getPublishedAt()));
        newvideo.setDescription(videoSnippet.getDescription());
        newvideo.setChannel(new Channel(videoSnippet.getChannelTitle(),videoSnippet.getChannelId()));
        newvideo.setThumbnail(new Thumbnail(videoSnippet.getThumbnails().getMedium().getUrl()));
        }
        return newvideo;
    }

    public int getExecutionNumber(){
        return caller.getExecutionNumber();
    }

    public void setExecutionNumber(int number){
        caller.setExecutionNumber(number);
    }

    public void removeCallBacks() {
        if(youtubeSearchAPI!=null)
            youtubeSearchAPI.cancel(true);
        if(youtubeVideoAPI!=null)
            youtubeVideoAPI.cancel(true);
    }

    public List<Channel> getChannelList() {
        return caller.getChannelList();
    }

}
