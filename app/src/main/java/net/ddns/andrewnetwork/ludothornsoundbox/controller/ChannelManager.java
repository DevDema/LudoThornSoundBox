package net.ddns.andrewnetwork.ludothornsoundbox.controller;

import android.content.Context;

import net.ddns.andrewnetwork.ludothornsoundbox.model.Channel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ChannelManager {
    List<Channel> channelList;
    Context context;

    public ChannelManager(Context context, List<Channel> channelList) {
        this.context=context;
        this.channelList=channelList;
    }

    public Channel findChannelbyName(String name) {
        for (Channel channel : channelList)
            if(channel.getChannelName().equals(name))
                return channel;
        return null;
    }

    public Channel findChannelsById(String ID) {
        List<Channel> singleChannel = new ArrayList<>();
        for(Channel channel : channelList)
            if(channel.getId().equals(ID))
                singleChannel.add(channel);
        if(singleChannel.size()>1) return null;
        else return singleChannel.get(0);
    }

    public Channel findChannelsByUsername(String Username) {
        List<Channel> singleChannel = new ArrayList<>();
        for(Channel channel : channelList)
            if(channel.getChannelUsername().equals(Username))
                singleChannel.add(channel);
        if(singleChannel.size()>1) return null;
        else return singleChannel.get(0);
    }


    public boolean areAllIdsSet() {
        int counter=0;
        for(Channel channel : channelList) if(channel.getId()!=null) counter++;
        if(counter>=channelList.size()) return true;
        return false;
    }

    public boolean areAllTotalNumberOfVideosSet() {
        int counter=0;
        for(Channel channel : channelList) if(channel.getTotalNumberOfVideos()!=null) counter++;
        if(counter>=channelList.size()) return true;
        return false;
    }

    public boolean areAllVideosLoaded() {
        int counter=0;
        for (Channel channel : channelList) if(channel.areAllVideosLoaded()) counter++;
        if(counter>=channelList.size()) return true;
        return false;
    }

    public boolean loadThumbnailsFromMemory() throws JSONException {
        int counter=0;
        for(Channel channel : channelList)
            if(VideoManager.loadThumbnailsFromMemory(context,channel.getVideoList()))
                counter++;
        if(counter>=channelList.size()) return true;
        return false;
    }

    public void setChannelList(ArrayList<Channel> channelList) {
        this.channelList=channelList;
    }
}
