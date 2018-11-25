package net.ddns.andrewnetwork.ludothornsoundbox.controller;

import net.ddns.andrewnetwork.ludothornsoundbox.fragment.ParentFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.fragment.VideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.VideoInformation;

import java.math.BigInteger;

public class VideoInformationManager {

    ParentFragment fragment;
    LudoVideo video;

    public String getCompactedLikes() {
        VideoInformation videoInformation = video.getVideoInformation();
        if(videoInformation!=null) {
            BigInteger likes = videoInformation.getLikes();
            return compactNumber(likes);
        }
        return null;
    }

    public String getCompactedDislikes() {
        VideoInformation videoInformation = video.getVideoInformation();
        if (videoInformation != null) {
            BigInteger dislikes = videoInformation.getDislikes();
            return compactNumber(dislikes);
        }
        return null;
    }

    public String getCompactedViews() {
        VideoInformation videoInformation = video.getVideoInformation();
        if (videoInformation != null) {
            BigInteger views = videoInformation.getViews();
            return compactNumber(views);
        }
        return null;
    }

    private String compactNumber(BigInteger value) {
        if (value.toString().length() > 4 && !fragment.isTablet())
            return value.toString().substring(0,value.toString().length()-3)+"K";
        else if (value.toString().length() > 6)
            return value.toString().substring(0,value.toString().length()-6)+"M";
        else return value.toString();
    }

    public VideoInformationManager(ParentFragment fragment, LudoVideo video) {
        this.fragment=fragment;
        this.video= video;
    }
}
