package net.ddns.andrewnetwork.ludothornsoundbox.data.model;

import androidx.annotation.DrawableRes;

import net.ddns.andrewnetwork.ludothornsoundbox.R;


public class Social {

    public enum SocialImage {
        SOCIAL_FACEBOOK(0, R.drawable.ic_facebook, "Facebook"),
        SOCIAL_INSTAGRAM(1, R.drawable.ic_instagram, "Instagram"),
        SOCIAL_TELEGRAM(2, R.drawable.ic_telegram, "Telegram");

        final int index;
        final @DrawableRes int socialImage;
        final CharSequence string;
        SocialImage(int index,  @DrawableRes int socialImage, CharSequence string) {
            this.index = index;
            this.socialImage = socialImage;
            this.string = string;
        }

        public @DrawableRes int getImage() {
            return socialImage;
        }

        public CharSequence getString() {
            return string;
        }

        public int getIndex() {
            return index;
        }

        public static SocialImage valueOf(int value) {
            for(SocialImage socialImage : SocialImage.values()) {
                if(socialImage.getIndex() == value) {
                    return socialImage;
                }
            }

            return null;
        }
    }

    private int socialImageIndex;
    private String url;
    public Social() {

    }

    public Social(SocialImage socialImage, String url) {
        this(socialImage.getIndex(), url);
    }

    public Social(int socialImageIndex, String url) {
        this();

        this.socialImageIndex = socialImageIndex;
        this.url = url;
    }

    public SocialImage getSocialImage() {
        return SocialImage.valueOf(socialImageIndex);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
