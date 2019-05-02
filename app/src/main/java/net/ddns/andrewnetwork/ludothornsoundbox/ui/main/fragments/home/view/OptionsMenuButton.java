package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view;


import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import net.ddns.andrewnetwork.ludothornsoundbox.R;

public class OptionsMenuButton extends ConstraintLayout {

    private TextView defaultButton;
    private ImageButton moreButton;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public OptionsMenuButton(Context context) {
        super(context);

        this.mContext = context;

        inflate();
        bindViews();

        configButton();
    }

    private void configButton() {
    }

    public void setText(CharSequence charSequence) {
        defaultButton.setText(charSequence);
    }

    public void setText(@StringRes int res) {
        defaultButton.setText(res);
    }

    private void bindViews() {
        defaultButton = findViewById(R.id.button_main);
        moreButton = findViewById(R.id.button_options);
    }

    private void inflate() {
        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.buttons_options_menu, this, true);
    }

    public void setTypeface(Typeface font) {
        defaultButton.setTypeface(font);
    }

    public void setOnMoreButtonClickListener(OnClickListener onMoreButtonClickListener) {
        moreButton.setOnClickListener(onMoreButtonClickListener);
    }

    public void setMaxLines(int maxLines) {
        defaultButton.setMaxLines(maxLines);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {

        super.setOnClickListener(l);

        defaultButton.setClickable(false);
    }

    @Override
    public void setLongClickable(boolean longClickable) {
        super.setLongClickable(longClickable);

        defaultButton.setLongClickable(!longClickable);
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        super.setOnLongClickListener(l);

        defaultButton.setLongClickable(false);
    }
}
