package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

import android.text.Editable;
import android.view.View;

public interface OnUserProgramStoppedListener extends OnUserStoppedListener {

    /**
     * OVERRIDE THIS TO IMPLEMENTED BEHAVIOUR WHEN PROGRAM STOPS WRITING.
     *
     * CALLED BEFORE ONUSERLISTENER.
     * @param editText view
     * @param editable text
     */
    void OnProgramStoppedWriting(View editText, Editable editable);
}
