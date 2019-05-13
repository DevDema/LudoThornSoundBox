package net.ddns.andrewnetwork.ludothornsoundbox.data.model;

import android.util.Pair;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.ColorUtils;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public class Channel implements Comparable, Serializable {

    private final String backGroundColor;
    private String ChannelName;
    private String id;
    private long totalNumberOfVideos;
    private String channelUsername;
    private ArrayList<LudoVideo> videoList;

    private String nextPageToken;


    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public Channel(String id) {
        this.id = id;
        backGroundColor = ColorUtils.DEFAULT_COLOR;
    }

    public Channel(String channelName, String username, String color) {
        ChannelName = channelName;
        this.channelUsername = username;
        videoList = new ArrayList<>();
        this.backGroundColor = color;
    }

    public Channel(String channelName, String username, String id, String color) {
        ChannelName = channelName;
        this.channelUsername = username;
        this.id=id;
        videoList = new ArrayList<>();
        this.backGroundColor = color;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTotalNumberOfVideos() {
        return totalNumberOfVideos;
    }

    public void setTotalNumberOfVideos(long totalNumberOfVideos) {
        this.totalNumberOfVideos = totalNumberOfVideos;
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

    public String getBackGroundColor() {
        return backGroundColor;
    }


}
