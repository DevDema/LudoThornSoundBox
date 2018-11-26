package net.ddns.andrewnetwork.ludothornsoundbox.view.model;

import android.content.Context;

import net.ddns.andrewnetwork.ludothornsoundbox.controller.ApplicationConnectivityManager;
import net.ddns.andrewnetwork.ludothornsoundbox.controller.ChannelManager;
import net.ddns.andrewnetwork.ludothornsoundbox.controller.VideoManager;
import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.ThumbnailAPIService;
import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.YoutubeAPIService;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.VideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.model.ChannelResponse;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Thumbnail;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class VideoFragmentViewModel {

    ApplicationConnectivityManager connectivityManager;
    YoutubeAPIService youtube ;
    ThumbnailAPIService thumbnailAPIService;
    ArrayList<Channel> channelList = new ArrayList<>();
    ChannelManager channelManager;

    public VideoFragmentViewModel(VideoFragment fragment) {
        youtube = new YoutubeAPIService(fragment);
        thumbnailAPIService  = new ThumbnailAPIService(fragment);
        connectivityManager = new ApplicationConnectivityManager(fragment.getActivity());

        channelList.add(new Channel("Ludo Thorn", "LudoThornDoppiaggio"));
        channelList.add(new Channel("Ludo MasterRace", "","UCAmxcLPY_gvUm_XzDIcB0wg"));

        //channelList.add(new Channel("Ludo MasterRace", "UCAmxcLPY_gvUm_XzDIcB0wg"));
        channelManager = new ChannelManager(fragment.getContext(),channelList);
    }

    public VideoFragmentViewModel(VideoFragment fragment, ArrayList<Channel> channelList) {
        youtube = new YoutubeAPIService(fragment);
        thumbnailAPIService  = new ThumbnailAPIService(fragment);
        connectivityManager = new ApplicationConnectivityManager(fragment.getActivity());
        this.channelList=channelList;
        channelManager = new ChannelManager(fragment.getContext(), channelList);
    }

    public ArrayList<LudoVideo> getCompleteVideoList() {
        return VideoManager.createCompleteList(channelList);
    }

    public boolean areAllVideosLoaded() {
        return channelManager.areAllVideosLoaded();
    }

    public ArrayList<LudoVideo> getCompleteVideoListWithNoThumbnails() {
        ArrayList<LudoVideo> ludoVideos = VideoManager.createCompleteList(channelList);
        ArrayList<LudoVideo> newLudoVideos = new ArrayList<>();
        for(LudoVideo video : ludoVideos)
            if(!video.hasThumbnail()) newLudoVideos.add(video);
        return newLudoVideos;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public List<LudoVideo> getFilteredVideoList(Channel channel) {
        return orderByDate(channel.getVideoList());
    }

    public ArrayList<LudoVideo> getVideoListFromChannelPosition(int position) {
        return orderByDate(findChannelByPosition(position).getVideoList());
    }


    public Channel findChannelByPosition(int position){
        if(position==0) return null;
        return channelList.get(position - 1);
    }



    public boolean isVideoListEmpty() {
        return getCompleteVideoList().isEmpty();
    }

    public boolean isVideoListEmpty(Channel channel) {
        if(channel.getVideoList().isEmpty()) return true;
        return false;
    }

   /* public void addListWithoutDuplicates(List<LudoVideo> videoList) {
        VideoManager.addListWithoutDuplicates(youtube.getChannelList(),videoList);
    }*/

    public void loadVideoInformation() throws MalformedURLException {
        int start = getCompleteVideoList().size()-getCompleteVideoListWithNoThumbnails().size();
        int end = getCompleteVideoList().size();
        if(start<end)
            for(LudoVideo video : getCompleteVideoList().subList(start,end)) {
                youtube.loadVideoInformation(video.getId());
                thumbnailAPIService.loadThumbnail(video);
            }
    }
    public void loadVideoInformation(Channel channel) throws MalformedURLException {
        int start = channel.getVideoList().size()-channel.getVideoListWithNoThumbnails().size();
        int end = channel.getVideoList().size();
        if(start<end)
            for(LudoVideo video : channel.getVideoList().subList(start,end)) {
                youtube.loadVideoInformation(video.getId());
                thumbnailAPIService.loadThumbnail(video);
            }
    }


    public boolean areAllThumbnailsLoaded() {
        return VideoManager.areAllThumbnailsLoaded(getCompleteVideoList());
    }

    public boolean setThumbnails(Thumbnail thumbnail, int executionNumber) {
        VideoManager.addThumbnailtoVideo(getCompleteVideoList(), thumbnail);
        if (areAllThumbnailsLoaded()) {
            if (executionNumber > 1) VideoManager.orderByDate(getCompleteVideoList());
            return true;
        }
        return false;
    }

        public boolean getConnectivity() {
            return connectivityManager.isNetworkAvailable();
        }

    public boolean retryConnection() {
        if(getConnectivity()) {
            if(channelManager.areAllTotalNumberOfVideosSet() && channelManager.areAllIdsSet()) youtube.loadVideos();
            else youtube.loadChannels(channelList);
                return true;
        }
        return false;
    }

    public boolean retryConnection(Channel channel) {
        if(channel==null) return retryConnection();
        if(getConnectivity()) {
            youtube.loadVideos(channel);
            return true;
        }
        return false;
    }

    public ArrayList<LudoVideo> orderByDate(ArrayList<LudoVideo> videoList) {
        return VideoManager.orderByDate(videoList);
    }

    public void clearChannels() {
        for(Channel channel : channelList) {
            channel.setVideoList(new ArrayList<>());
            channel.setNextPageToken(null);
        }
    }
    /*public boolean isFilteredListEmpty() {
        return filteredVideoList.isEmpty();
    }*/

    public boolean isCurrentVideoListEmpty() {
        return getCompleteVideoList().isEmpty();
    }

    public void cancelProcesses() {
        youtube.removeCallBacks();
        thumbnailAPIService.removeCallBacks();
        cleanVideoList();
    }

    public void cleanVideoList() {
        VideoManager.cleanVideoList(getCompleteVideoList());
    }


    /*public void loadThumbnailsFromMemory(Context context) throws JSONException {
        if(!isVideoListEmpty() && VideoManager.noVideohasThumbnail(getCompleteVideoList()))
            VideoManager.loadThumbnailsFromMemory(context,getCompleteVideoList());
    }*/

    public void saveToMemory(Context context) throws JSONException {
        VideoManager.saveToMemory(context, getCompleteVideoList());
    }

    public void onChannelsLoaded(ChannelResponse channelResponse) {
        Channel channel = channelManager.findChannelsByUsername(channelResponse.getUsername());
        channel.setTotalNumberOfVideos(channelResponse.getTotalnumberOfVideos().intValue());
        channel.setId(channelResponse.getId());
        retryConnection(channel);
    }

    public boolean loadThumbnailsFromMemory() throws JSONException {
        return channelManager.loadThumbnailsFromMemory();
    }


    public void setChannelList(ArrayList<Channel> channelList) {
        this.channelList = channelList;
        channelManager.setChannelList(channelList);
    }
    /*public void reinitializeVideoListIfNull() {
        if(getCompleteVideoList()==null) videoList = new ArrayList<>();
    }*/

    /*public void reinitializeVideoList() {
        videoList = new ArrayList<>();
    }*/

   /* public Integer getNumberOfVideos() {
        return numberOfVideos;
    }*/

    /*public void setNumberOfVideos(Integer numberOfVideos) {
        this.numberOfVideos = numberOfVideos;
    }*/


    /*public List<LudoVideo> getCurrentVideoList() {
        return currentVideoList;
    }*/

    /*public void resetCurrentVideoList() {
        this.currentVideoList = VideoManager.createCompleteList(youtube.getChannelList());
    }*/
}
