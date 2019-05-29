package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.AltriSocial;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Social;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;

public class SocialItem extends RelativeLayout {

    private final Context mContext;
    private ImageView icon;
    private TextView title;

    public SocialItem(Context context) {
        super(context);

        this.mContext = context;

        inflate();
        bindViews();

    }

    public SocialItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SocialItem, 0, 0);

        Drawable drawable = ta.getDrawable(R.styleable.SocialItem_image);
        CharSequence text = ta.getString(R.styleable.SocialItem_android_text);

        setImageDrawable(drawable);
        setText(text);

        ta.recycle();
        inflate();
        bindViews();
    }

    private void bindViews() {
        icon = findViewById(R.id.page_icon);
        title = findViewById(R.id.page_title);
    }

    private void inflate() {

        LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.item_social, this, true);
    }

    public void setText(CharSequence s) {
        title.setText(s);
    }

    public void setTextPagina(CharSequence s) {
        title.setText(mContext.getString(R.string.pagina_label, s));
    }

    public void setImageResource(@DrawableRes int drawable) {
        icon.setImageResource(drawable);
    }

    public void setImageDrawable(Drawable drawable) {
        icon.setImageDrawable(drawable);
    }

    public void setSocial(Social social) {
        if(social.getSocialImage() != null) {
            setImageResource(social.getSocialImage().getImage());
            if(social instanceof AltriSocial) {
                setText(((AltriSocial) social).getTitle());
            } else {
                setText(social.getSocialImage().getString());
            }
        }

        setOnClickListener(v -> CommonUtils.openLink(mContext, social.getUrl()));
    }
}
