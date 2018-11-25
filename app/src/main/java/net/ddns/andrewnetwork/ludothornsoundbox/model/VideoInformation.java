package net.ddns.andrewnetwork.ludothornsoundbox.model;

import java.io.Serializable;
import java.math.BigInteger;

public class VideoInformation implements Serializable {
    BigInteger likes;
    BigInteger dislikes;
    BigInteger views;

    public BigInteger getLikes() {
        return likes;
    }

    public void setLikes(BigInteger likes) {
        this.likes = likes;
    }

    public BigInteger getDislikes() {
        return dislikes;
    }

    public void setDislikes(BigInteger dislikes) {
        this.dislikes = dislikes;
    }

    public BigInteger getViews() {
        return views;
    }

    public void setViews(BigInteger views) {
        this.views = views;
    }


    public VideoInformation(BigInteger likes, BigInteger dislikes, BigInteger views) {
        this.likes = likes;
        this.dislikes = dislikes;
        this.views = views;
    }

    public VideoInformation() {
    }
}
