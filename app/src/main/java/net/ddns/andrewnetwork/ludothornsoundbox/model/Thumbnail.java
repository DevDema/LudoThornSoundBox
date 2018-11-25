package net.ddns.andrewnetwork.ludothornsoundbox.model;

import android.graphics.Bitmap;

import java.io.InputStream;
import java.io.Serializable;

public class Thumbnail implements Serializable {
    SerialBitmap image;
    String url;

    public Thumbnail(InputStream inputStream, String url) {
        this.image = new SerialBitmap(inputStream);
        this.url = url;
    }

    public Thumbnail(String imagePath, String url) {
        this.image = new SerialBitmap(imagePath);
        this.url = url;
    }

    public Thumbnail(String url) {
        this.url = url;
    }

    public Bitmap getImage() {
        if(image!=null) return image.getBitmap();
        return null;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Url: "+getUrl();
    }
}
