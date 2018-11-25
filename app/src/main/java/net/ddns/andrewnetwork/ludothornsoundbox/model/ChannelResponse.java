package net.ddns.andrewnetwork.ludothornsoundbox.model;

import java.math.BigInteger;

public class ChannelResponse {
    String id;
    BigInteger totalnumberOfVideos;

    public ChannelResponse(String username) {
        this.username = username;
    }

    String username;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigInteger getTotalnumberOfVideos() {
        return totalnumberOfVideos;
    }

    public void setTotalnumberOfVideos(BigInteger totalnumberOfVideos) {
        this.totalnumberOfVideos = totalnumberOfVideos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
