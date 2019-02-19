package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringParse;

import java.util.List;


public class ButtonsView<T> extends LinearLayout {

    public interface OnViewReadyListener {

        void onViewReady();
    }

    private OnViewReadyListener onViewReadyListener;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private static int MAX_COLUMNS;
    private static int MAX_ROWS;
    private List<T> list;
    private LinearLayout masterLayout;

    public ButtonsView(Context context) {
        super(context);

        this.mContext = context;
        inflate();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int buttonWidth = (int) context.getResources().getDimension(R.dimen.input_size_m);
                int buttonHeight = (int) context.getResources().getDimension(R.dimen.input_size_xxxs);
                MAX_COLUMNS = (int) Math.floor(getWidth()*1.0 / buttonWidth);
                MAX_ROWS = (int) Math.floor(getHeight()*1.0 / buttonHeight);

                masterLayout = (LinearLayout) getChildAt(0);
                for (int i = 0; i < MAX_ROWS; i++) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setWeightSum(MAX_COLUMNS);
                    linearLayout.setOrientation(HORIZONTAL);
                    for (int j = 0; j < MAX_COLUMNS; j++) {
                        Button button = new Button(context);
                        LayoutParams layoutParams = new LayoutParams(buttonWidth, buttonHeight);
                        layoutParams.gravity = Gravity.CENTER;
                        layoutParams.weight = 1;

                        button.setLayoutParams(layoutParams);

                        button.setVisibility(View.INVISIBLE);
                        button.setMaxLines(1);
                        linearLayout.addView(button);
                    }
                    masterLayout.addView(linearLayout);
                }

                if(onViewReadyListener != null) {
                    onViewReadyListener.onViewReady();
                }
            }
        });
    }

    private void inflate() {
        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.buttons_audio, this, true);
    }

    public void setAdapter(List<T> list, StringParse<T> parser) {
        this.list = list;

        for (int i = 0; i < MAX_ROWS; i++) {
            LinearLayout linearLayout = (LinearLayout) masterLayout.getChildAt(i);
            for (int j = 0; j < MAX_COLUMNS; j++) {
                Button button = (Button) linearLayout.getChildAt(j);
                int index = i * MAX_COLUMNS + j;
                if(index < list.size()) {
                    T object = list.get(index);
                    if (object != null) {
                        button.setText(parser.parseToString(object));
                        button.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    public void setOnButtonSelectedListener(OnButtonSelectedListener<T> listener) {
        if (list == null)
            throw new IllegalArgumentException("Trying to set listener on a empty array or list");

        for (int i = 0; i < MAX_ROWS; i++) {
            LinearLayout linearLayout = (LinearLayout) masterLayout.getChildAt(i);
            for (int j = 0; j < MAX_COLUMNS; j++) {
                Button button = (Button) linearLayout.getChildAt(j);
                int position = i * MAX_COLUMNS + j;
                if(position < list.size()) {
                    T object = list.get(position);
                    if (object != null) {
                        button.setOnClickListener(view -> listener.onButtonSelected(object, position, (Button) view));
                    }
                }
            }
        }
    }

    public int getMaxItems() {
        return MAX_COLUMNS*MAX_ROWS;
    }

    public void setOnViewReadyListener(OnViewReadyListener onViewReadyListener) {
        this.onViewReadyListener = onViewReadyListener;
    }
}
