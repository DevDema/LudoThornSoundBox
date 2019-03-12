package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.controller;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.VideoInformation;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.ParentFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppUtils;

import java.math.BigInteger;
import java.util.Objects;

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
        if (value.toString().length() > 4 && !AppUtils.isTablet(Objects.requireNonNull(fragment.getActivity())))
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
