package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view;

import android.view.View;
import android.widget.Button;

public interface OnButtonSelectedListener<T> {

    void onButtonSelected(T object, int position, View button);

    boolean onButtonLongSelected(T object, int position, View button);

}
