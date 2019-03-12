package net.ddns.andrewnetwork.ludothornsoundbox.data.model;

import java.io.Serializable;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LudoVideo implements Comparable<LudoVideo>, Serializable {
    private String id, title, description;
    private Date dateTime;
    private Thumbnail thumbnail;

    public enum Source {YOUTUBE, PERSONAL}

    ;
    private VideoInformation videoInformation;
    private Channel channel;
    private Source source;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public LudoVideo() {

    }

    public LudoVideo(String id, String title, String description, Date dateTime, Thumbnail thumbnail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.thumbnail = thumbnail;
    }

    public LudoVideo(String id, String title, String description, Date dateTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
    }

    public LudoVideo(Source source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }


    public VideoInformation getVideoInformation() {
        return videoInformation;
    }

    public void setVideoInformation(VideoInformation videoInformation) {
        this.videoInformation = videoInformation;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        String string = "Video:" +
                "{  id =" + getId() + "\n" +
                "   titolo=" + getTitle() + "\n" +
                "   descrizione=" + getDescription() + "\n" +
                "   canale=" + getChannel() + "\n" +
                "   data-ora=" + getDateTime().toString() + "\n" +
                "   thumbnail-url=" + getThumbnail().getUrl();
        if (getVideoInformation() != null)
            return string +
                    "   likes=" + getVideoInformation().getLikes() + "\n" +
                    "   dislikes=" + getVideoInformation().getDislikes() + "\n" +
                    "   views" + getVideoInformation().getViews() + "\n";
        else return string;
    }

    @Override
    public int compareTo(@NonNull LudoVideo o) {
        return getDateTime().compareTo(o.getDateTime());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;

        if (obj.getClass() == this.getClass()) {
            LudoVideo video2 = (LudoVideo) obj;
            return this.getId().equals(video2.getId());
        }
        return false;
    }

    public boolean hasThumbnail() {
        if (thumbnail != null && thumbnail.getImage() != null) return true;
        return false;
    }

    public boolean hasInformation() {
        if (videoInformation != null && videoInformation.getViews() != null) return true;
        return false;
    }
}
