package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras;

import android.os.Bundle;
import android.view.View;

/**
 * @author Daniele Rizzo
 * @version 1.1
 * @date 03/02/2017
 * @edit: Antonio Cornacchia
 */
public interface FragmentLifeCycleListener extends LifeCycleListener{

    void onViewCreated(View view, Bundle savedInstanceState);

    void onActivityCreated(Bundle savedInstanceState);

}
