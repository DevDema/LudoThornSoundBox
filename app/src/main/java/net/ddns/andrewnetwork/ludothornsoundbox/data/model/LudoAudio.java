package net.ddns.andrewnetwork.ludothornsoundbox.data.model;

import java.io.Serializable;
import java.util.Comparator;

public class LudoAudio implements Serializable {

    private String title;
    private int audio;
    private int order;

    public static Comparator<LudoAudio> COMPARE_BY_DATE = (one, other) -> Integer.compare(one.order,other.order);

    public static Comparator<LudoAudio> COMPARE_BY_NAME = (one, other) -> one.title.compareTo(other.title);

    public LudoAudio(String filename, int audio) {
        if(filename.contains("_")) {
            filename = filename.substring(1);
            int underscore = filename.indexOf("_");
            order = Integer.parseInt(filename.substring(0, underscore));
            title = filename.substring(underscore+1, filename.length()).replace("_", " ");
        }
        else
        {
            order = 0;
            title = filename;
        }
        this.audio = audio;
    }

    public LudoAudio(String title, int audio, int order) {
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
