package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

public class LudoNavigationItem {

    private @IdRes int id;
    private @DrawableRes int drawableRes;
    private String name;
    private boolean visible;

    LudoNavigationItem(@IdRes int idRes, @DrawableRes int drawableRes){
        this.drawableRes = drawableRes;

        this.id = idRes;
    }


    public LudoNavigationItem(@IdRes int idRes, @DrawableRes int drawableRes, String title){
        this(idRes, drawableRes);

        this.name = title;

        this.visible = true;
    }

    LudoNavigationItem(@IdRes int idRes, @DrawableRes int drawableRes, String title, boolean b) {
        this(idRes, drawableRes, title);

        this.visible = b;
    }

    @DrawableRes int getDrawableId() {
        return drawableRes;
    }

    public void setDrawable(int drawableRes) {
        this.drawableRes = drawableRes;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisible() {
        return visible;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof LudoNavigationItem)) {
            return false;
        }

        return this.getDrawableId() == ((LudoNavigationItem) obj).getDrawableId();
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public @IdRes int getId() {
        return id;
    }

    public void setId(@IdRes int id) {
        this.id = id;
    }
}
