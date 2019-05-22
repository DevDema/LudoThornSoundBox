package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.tabs.TabLayout;

public class TabLayoutItem extends TabLayout {
    private int currentTab;
    public TabLayoutItem(Context context) {
        super(context);

        addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                currentTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {
                currentTab = tab.getPosition();
            }
        });
    }

    public TabLayoutItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabLayoutItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected Tab createTabFromPool() {
        Tab tab = super.createTabFromPool();

        if(!(tab instanceof TabItem)) {
            tab = new TabItem<>();
        }

        return tab;
    }

    public int getCurrentTab() {
        return currentTab;
    }
}
