package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public abstract class ReducedDialogFragment extends LoadingDialog {

    private static Double SCREEN_WIDTH_PERCENTAGE = 0.9;

    @Override
    public void onResume() {
        super.onResume();

        Point size = new Point();
        Window window = getDialog().getWindow();
        if(window != null) {
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);

            int width = size.x;
            int height = size.y;

            window.setLayout((int) (width * SCREEN_WIDTH_PERCENTAGE), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);

        }
    }
}
