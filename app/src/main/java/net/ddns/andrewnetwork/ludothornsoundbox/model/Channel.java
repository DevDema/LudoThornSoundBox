package net.ddns.andrewnetwork.ludothornsoundbox.model;

import android.support.annotation.NonNull;

import net.ddns.andrewnetwork.ludothornsoundbox.controller.VideoManager;

import java.io.Serializable;
import java.util.ArrayList;

public class Channel implements Comparable, Serializable {

    private String ChannelName;
    private String id;
    private Integer totalNumberOfVideos;
    private String channelUsername;
    private ArrayList<LudoVideo> videoList;

    private String nextPageToken;


    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public Channel(String channelName, String username) {
        ChannelName = channelName;
        this.channelUsername = username;
        videoList = new ArrayList<>();
    }

    public Channel(String channelName, String username, String id) {
        ChannelName = channelName;
        this.channelUsername = username;
        this.id=id;
        videoList = new ArrayList<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTotalNumberOfVideos() {
        return totalNumberOfVideos;
    }

    public void setTotalNumberOfVideos(Integer totalNumberOfVideos) {
        this.totalNumberOfVideos = totalNumberOfVideos;
    }

    public boolean areAllVideosLoaded() {
        if(totalNumberOfVideos!=null) return videoList.size()>=totalNumberOfVideos && VideoManager.areAllThumbnailsLoaded(videoList);
        return false;
    }
    public ArrayList<LudoVideo> getVideoList() {
        return videoList;
    }

    public ArrayList<LudoVideo> getVideoListWithNoThumbnails() {
        ArrayList<LudoVideo> newLudoVideos = new ArrayList<>();
        for(LudoVideo video : videoList)
            if(!video.hasThumbnail()) newLudoVideos.add(video);
        return newLudoVideos;
    }

    public void setVideoList(ArrayList<LudoVideo> videoList) {
        this.videoList = videoList;
    }

    public void addToVideoList(LudoVideo video) {
        this.videoList.add(video);
    }

    public String getChannelUsername() {
        return channelUsername;
    }

    public void setChannelUsername(String channelUsername) {
        this.channelUsername = channelUsername;
    }

    @Override
    public String toString() {
        return "Canale: "+getChannelName()+" ID: " +getId();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if(o.getClass()==this.getClass()) {
            Channel channel = (Channel) o;
            if(this.getId().equals(channel.getId())) return 0;
        }
        return -1;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
