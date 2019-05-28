package net.ddns.andrewnetwork.ludothornsoundbox.data.model;

public class AltriSocial extends Social {

    private String title;

    public AltriSocial(String title) {
        super();

        this.title = title;
    }

    public AltriSocial(SocialImage socialImage, String url, String title) {
        super(socialImage, url);

        this.title = title;
    }

    public AltriSocial(int socialImageIndex, String url, String title) {
        super(socialImageIndex, url);

        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
