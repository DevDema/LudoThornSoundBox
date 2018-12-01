package net.ddns.andrewnetwork.ludothornsoundbox.fragment;

import net.ddns.andrewnetwork.ludothornsoundbox.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.model.ChannelResponse;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.model.VideoInformation;

import java.util.List;

public interface IVideoLoader {

    void onVideosLoaded(Channel channel);

    void onVideosLoaded();

    void onThumbnailLoaded(Thumbnail thumbnail);

    void onVideoInformationLoaded(VideoInformation videoInformation, LudoVideo ludoVideo);

    void onChannelsLoaded(ChannelResponse result);

    int getExecutionNumber();

    void setExecutionNumber(int number);

    List<Channel> getChannelList();
}
