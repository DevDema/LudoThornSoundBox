package net.ddns.andrewnetwork.ludothornsoundbox.model;

public class LudoVideoResponse {
    String id;
    String thumbnailPath;
    String url;

    public LudoVideoResponse(String id, String url, String thumbnailPath) {
        this.id = id;
        this.url = url;
        this.thumbnailPath = thumbnailPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
