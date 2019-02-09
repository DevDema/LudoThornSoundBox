package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras;

import android.content.Intent;
import android.os.Bundle;

/**
 * @author Daniele Rizzo
 * @version 1.0
 * @date 29/10/2018
 * @edit: Antonio Cornacchia
 **/
public interface LifeCycleListener {

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onStop();

    void onPause();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

}
