package net.ddns.andrewnetwork.ludothornsoundbox.data.model;

import java.io.Serializable;
import java.util.Comparator;

import androidx.annotation.Nullable;

public class LudoAudio implements Serializable, Cloneable {

    private String title;
    private int order;
    private String audioFile;
    private LudoVideo referredVideo;
    private boolean hidden;

    public static Comparator<LudoAudio> COMPARE_BY_DATE = (one, other) -> Integer.compare(one.order, other.order);

    public static Comparator<LudoAudio> COMPARE_BY_NAME = (one, other) -> one.title.compareTo(other.title);

    public LudoAudio() {}

    public LudoAudio(String filename, LudoVideo ludoVideo) {
        this.audioFile = filename;
        this.referredVideo = ludoVideo;

        filename = filename.substring(filename.indexOf("/")+1);

        if(filename.charAt(0) == 'n') {
            filename = filename.substring(1);
        }

        int divider = filename.contains("_") ? filename.indexOf("_") : filename.indexOf(" ");

        try {
            order = Integer.parseInt(filename.substring(0, divider));
            title = filename.substring(divider + 1, filename.lastIndexOf(".")).replace("_", " ");
        } catch (NumberFormatException e) {
            //NO ORDER SPECIFIED
            order = 0;
            title = filename.substring(0, filename.lastIndexOf("."));
        }
    }

    public String getTitle() {
        return title;
    }

    public int getOrder() {
        return order;
    }


    public LudoVideo getVideo() {
        return referredVideo;
    }

    public void setVideo(LudoVideo video) {

        if(video != null) {
            try {
                video.addAudio(clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        this.referredVideo = video;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    @Override
    protected LudoAudio clone() throws CloneNotSupportedException {
        LudoAudio audio = (LudoAudio) super.clone();

        audio.setVideo(null);

        return audio;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) {
            return false;
        }

        if(getClass() != obj.getClass()) {
            return false;
        }

        return ((LudoAudio) obj).getTitle().equals(getTitle());
    }

    public String getAudioFile() {
        return audioFile;
    }
}
