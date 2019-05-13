package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;
import com.google.gson.reflect.TypeToken;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.VideoInformation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

import static net.ddns.andrewnetwork.ludothornsoundbox.BuildConfig.CHANNELS;

public abstract class VideoUtils {
    /**
     * @param searchResultList search results list
     */
    public static List<LudoVideo> castToLudoVideo(List<SearchResult> searchResultList) {
        List<LudoVideo> videoList = new ArrayList<>();
        for (SearchResult searchResult : searchResultList) {
            SearchResultSnippet searchSnippet = searchResult.getSnippet();
            if (searchResult.getId().getKind().equals("youtube#video")) {
                LudoVideo video = new LudoVideo(LudoVideo.Source.YOUTUBE);
                String videoId = searchResult.getId().getVideoId();
                video.setId(videoId);
                video.setTitle(searchSnippet.getTitle());
                video.setDateTime(convertToDate(searchSnippet.getPublishedAt()));
                video.setDescription(searchSnippet.getDescription());
                video.setThumbnail(new Thumbnail(searchSnippet.getThumbnails().getMedium().getUrl()));
                videoList.add(video);
            }
        }
        return videoList;
    }

    public static LudoVideo castToLudoVideo(Video video) {
        LudoVideo ludoVideo = new LudoVideo(LudoVideo.Source.YOUTUBE);

        ludoVideo.setId(video.getId());

        VideoSnippet videoSnippet =video.getSnippet();

        ludoVideo.setTitle(videoSnippet.getTitle());
        ludoVideo.setDateTime(convertToDate(videoSnippet.getPublishedAt()));
        ludoVideo.setDescription(videoSnippet.getDescription());
        ludoVideo.setChannel(findChannel(videoSnippet.getChannelId(), videoSnippet.getChannelTitle()));
        ludoVideo.setThumbnail(new Thumbnail(videoSnippet.getThumbnails().getMedium().getUrl()));

        VideoStatistics videoStatistics = video.getStatistics();
        VideoInformation videoInformation = new VideoInformation();

        videoInformation.setLikes(videoStatistics.getLikeCount());
        videoInformation.setDislikes(videoStatistics.getDislikeCount());
        videoInformation.setViews(videoStatistics.getViewCount());

        ludoVideo.setVideoInformation(videoInformation);

        return ludoVideo;

    }

    private static Channel findChannel(String channelId, String channelTitle) {
        Channel channel = findChannelById(channelId);

        if(channel == null) {
            channel = findChannelByTitle(channelTitle);
        }

        return channel;
    }

    private static Channel findChannelByTitle(String channelTitle) {
        for(Channel channel : getChannels()) {
            if(channel.getChannelName() != null && channel.getChannelName().equals(channelTitle)) {
                return channel;
            }
        }

        return null;
    }

    /**
     * @param channel   associate to channel
     * @param videoList videolist to be linked
     */
    public static void addVideosToChannels(Channel channel, List<LudoVideo> videoList) {
        for (LudoVideo video : videoList) {
            video.setChannel(channel);
            channel.addToVideoList(video);
        }
    }

    public static Date convertToDate(@NonNull DateTime dateTime) {
        Date date = new Date();
        date.setTime(dateTime.getValue());
        return date;
    }

    public static List<Channel> getChannels() {
        return JsonUtil.getGson().fromJson(CHANNELS, new TypeToken<List<Channel>>() {}.getType());
    }

    public static Channel findChannelById(String id) {
        for(Channel channel : getChannels()) {
            if(channel.getId() != null && channel.getId().equals(id)) {
                return channel;
            }
        }

        return null;
    }


    public static List<LudoVideo> concatVideos(List<List<LudoVideo>> videoList) {
        ArrayList<LudoVideo> allVideos = new ArrayList<>();
        for (List<LudoVideo> aVideoList : videoList) {
            allVideos.addAll(aVideoList);
        }

        return allVideos;
    }

    public static List<LudoVideo> concatVideosInChannel(List<Channel> channelList) {
        ArrayList<LudoVideo> allVideos = new ArrayList<>();
        for (Channel channel : channelList) {
            allVideos.addAll(channel.getVideoList());
        }

        return allVideos;
    }

    public static VideoInformation extractVideoInformation(Video video) {
        VideoInformation videoInformation = new VideoInformation();
        VideoStatistics videoStatistics = video.getStatistics();
        if (videoStatistics != null) {
            videoInformation.setLikes(videoStatistics.getLikeCount());
            videoInformation.setDislikes(videoStatistics.getDislikeCount());
            videoInformation.setViews(videoStatistics.getViewCount());
        }
        return videoInformation;
    }

    public static List<LudoVideo> removeDuplicates(List<LudoVideo> videoList) {
        List<LudoVideo> ludoVideos = new ArrayList<>(videoList);
        if (ludoVideos.size() > 1)
            for (int i = 0; i < ludoVideos.size(); i++)
                for (int j = 0; j < ludoVideos.size(); j++)
                    if (ludoVideos.get(i).equals(ludoVideos.get(j)) && i != j)
                        ludoVideos.remove(j);
        return ludoVideos;
    }

    public static Date getMostRecentDate(List<Channel> channelList) {
        List<Date> dateList = new ArrayList<>();

        for (Channel channel : channelList) {
            if (channel.getVideoList().isEmpty())
                continue;

            dateList.add(channel.getVideoList().get(channel.getVideoList().size() - 1).getDateTime());
        }

        return Collections.max(dateList);
    }

    public static Date getMostRecentDate(Channel channel) {
        List<Date> dateList = new ArrayList<>();

        if (channel.getVideoList().isEmpty())
            return Calendar.getInstance().getTime();

        dateList.add(channel.getVideoList().get(channel.getVideoList().size() - 1).getDateTime());

        return Collections.max(dateList);
    }

    public static List<LudoVideo> filterList(List<LudoVideo> videoList) {
        videoList = removeDuplicates(videoList);

        //SORT BY DATE, DESCENDING
        Collections.sort(videoList);
        Collections.reverse(videoList);

        return videoList;
    }
}
