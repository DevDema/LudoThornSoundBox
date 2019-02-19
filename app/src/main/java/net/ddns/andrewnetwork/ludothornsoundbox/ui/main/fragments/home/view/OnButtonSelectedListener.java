package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view;

import android.widget.Button;

public interface OnButtonSelectedListener<T> {

    void onButtonSelected(T object, int position, Button button);

}
