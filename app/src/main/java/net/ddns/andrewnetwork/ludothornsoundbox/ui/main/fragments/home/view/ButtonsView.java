package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view;

import android.content.Context;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringParse;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;


public class ButtonsView<T> extends LinearLayout {

    public interface OnViewReadyListener {

        void onViewReady();
    }

    private OnViewReadyListener onViewReadyListener;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private static int MAX_COLUMNS;
    private static int MAX_ROWS;
    private static int MARGIN;
    private List<T> list;
    private LinearLayout masterLayout;
    private boolean infoVisible;

    public ButtonsView(Context context) {
        super(context);

        this.mContext = context;
        this.infoVisible = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(mContext.getString(R.string.mostra_info_in_audio_key), false);
        inflate();
        bindViews();
    }

    private void bindViews() {
        masterLayout = findViewById(R.id.master_layout);
    }

    private void inflate() {
        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.buttons_audio, this, true);
    }

    @Override
    public void removeAllViews() {
        masterLayout.removeAllViews();
    }

    public void setAdapter(List<T> list, StringParse<T> parser) {
        setAllInvisible();

        this.list = list;
        int lastIndex = 0;
        for (int i = 0; i < MAX_ROWS; i++) {
            LinearLayout linearLayout = (LinearLayout) masterLayout.getChildAt(i);
            if (linearLayout != null) {
                for (int j = 0; j < MAX_COLUMNS; j++) {
                    View button = linearLayout.getChildAt(j);
                    int index = i * MAX_COLUMNS + j;
                    if (index < list.size()) {
                        T object = list.get(index);
                        if (object != null) {
                            if (button instanceof Button) {
                                ((Button) button).setText(parser.parseToString(object));
                            } else if (button instanceof OptionsMenuButton) {
                                ((OptionsMenuButton) button).setText(parser.parseToString(object));
                            }
                            button.setVisibility(View.VISIBLE);
                        }
                    } else if (lastIndex == 0) {
                        lastIndex = index;
                        break;
                    } else {
                        button.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void setAllInvisible() {
        for (int i = 0; i < MAX_ROWS; i++) {
            LinearLayout linearLayout = (LinearLayout) masterLayout.getChildAt(i);
            if(linearLayout != null) {
                for (int j = 0; j < MAX_COLUMNS; j++) {
                    if (linearLayout.getChildAt(j) != null) {
                        linearLayout.getChildAt(j).setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }


    public void setOnButtonSelectedListener(OnButtonSelectedListener<T> listener) {
        if (list == null) {
            throw new IllegalArgumentException("Trying to set listener on a empty array or list");
        }

        for (int i = 0; i < MAX_ROWS; i++) {
            LinearLayout linearLayout = (LinearLayout) masterLayout.getChildAt(i);
            if (linearLayout != null) {
                for (int j = 0; j < MAX_COLUMNS; j++) {
                    View button = linearLayout.getChildAt(j);
                    int position = i * MAX_COLUMNS + j;
                    if (position < list.size()) {
                        T object = list.get(position);
                        if (object != null) {
                            button.setOnClickListener(view -> listener.onButtonSelected(object, position, view));
                            if (infoVisible) {
                                ((OptionsMenuButton) button).setOnMoreButtonClickListener(v -> listener.onButtonLongSelected(object, position, v));
                            } else {
                                button.setOnLongClickListener(v -> listener.onButtonLongSelected(object, position, v));
                            }
                        }
                    }
                }
            }
        }
    }

    public void inflateButtons(Context context) {
        int buttonWidth = getWidthFromPreferences(context);
        int buttonHeight = (int) context.getResources().getDimension(R.dimen.input_size_xxxs);
        MAX_COLUMNS = (int) Math.floor(getWidth() * 1.0 / (buttonWidth + 2 * MARGIN));
        MAX_ROWS = (int) Math.floor(getHeight() * 1.0 / (buttonHeight + 2 * MARGIN));
        for (int i = 0; i < MAX_ROWS; i++) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setWeightSum(MAX_COLUMNS);
            linearLayout.setOrientation(HORIZONTAL);
            for (int j = 0; j < MAX_COLUMNS; j++) {
                View button;
                if (infoVisible) {
                    button = new OptionsMenuButton(context);
                    ((OptionsMenuButton) button).setTypeface(ResourcesCompat.getFont(mContext, R.font.knewave));
                    ((OptionsMenuButton) button).setMaxLines(1);
                } else {
                    button = new Button(context);
                    ((Button) button).setTypeface(ResourcesCompat.getFont(mContext, R.font.knewave));
                    ((Button) button).setMaxLines(1);
                }
                LayoutParams layoutParams = new LayoutParams(buttonWidth, buttonHeight);
                layoutParams.gravity = Gravity.CENTER;
                layoutParams.weight = 1;
                layoutParams.leftMargin = MARGIN;
                layoutParams.rightMargin = MARGIN;
                layoutParams.bottomMargin = MARGIN;
                layoutParams.topMargin = MARGIN;

                button.setLayoutParams(layoutParams);

                button.setBackground(ContextCompat.getDrawable(mContext, R.drawable.button_white));
                button.setVisibility(View.INVISIBLE);
                linearLayout.addView(button);
            }
            masterLayout.addView(linearLayout);
        }

        if (onViewReadyListener != null) {
            onViewReadyListener.onViewReady();
        }
    }

    public int getMaxItems() {
        return MAX_COLUMNS * MAX_ROWS;
    }

    public void setOnViewReadyListener(OnViewReadyListener onViewReadyListener) {
        this.onViewReadyListener = onViewReadyListener;
    }

    static int getWidthFromPreferences(Context context) {
        final String DEFAULT_VALUE = "Media";
        String widthString = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.dimensione_pulsanti_key), DEFAULT_VALUE);

        widthString = widthString != null ? widthString : DEFAULT_VALUE;
        switch (widthString) {
            case "Piccola":
                return (int) context.getResources().getDimension(R.dimen.input_size_xs2);
            default:
            case "Media":
                return (int) context.getResources().getDimension(R.dimen.input_size_m);
            case "Grande":
                return (int) context.getResources().getDimension(R.dimen.input_size_l);

        }
    }
}
