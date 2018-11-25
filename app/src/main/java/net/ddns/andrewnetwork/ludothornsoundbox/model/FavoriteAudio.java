package net.ddns.andrewnetwork.ludothornsoundbox.model;

import java.io.Serializable;
import java.util.Comparator;

public class FavoriteAudio implements Serializable {

    private String title;
    private int audio;
    private int order;

    public static Comparator<FavoriteAudio> COMPARE_BY_DATE = (one, other) -> Integer.compare(one.order,other.order);

    public static Comparator<FavoriteAudio> COMPARE_BY_NAME = (one, other) -> one.title.compareTo(other.title);

    public FavoriteAudio(String filename, int audio) {
        if(filename.contains("_")) {
            filename = filename.substring(1);
            int underscore = filename.indexOf("_");
            order = Integer.parseInt(filename.substring(0, underscore));
            title = filename.substring(underscore, filename.length()).replace("_", " ");
        }
        else
        {
            order = 0;
            title = filename;
        }
        this.audio = audio;
    }

    public FavoriteAudio(String title, int audio, int order) {
        this.title = title;
        this.audio = audio;
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public int getAudio() {
        return audio;
    }

    public int getOrder() {
        return order;
    }


}
