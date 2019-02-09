package net.ddns.andrewnetwork.ludothornsoundbox.ui.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public abstract class NavigationDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Menu mMenu;
    protected DrawerLayout mDrawerLayout;
    protected LinearLayout mNavigationView;
    protected PreferenceActivity.Header mHeader;

    private int mLastItemSelected = -1;
    private Map<Integer, Integer> mMenuItemsPositions;
    private Map<Integer, Boolean> mMenuItemsExpanded;

    /*protected TextView getName() {
        return mHeader.name;
    }*/

    protected abstract int getResourceMenu();

    public abstract void onMenuItemSelected(MenuItem menuItem);

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void openRightDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            drawer.openDrawer(GravityCompat.END);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        Log.d(TAG, "DrawerLayout:" + mDrawerLayout);
        if (mDrawerLayout != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();

        }

        try {
            Class<?> menuBuilderClass = Class.forName("androidx.appcompat.view.menu.MenuBuilder");
            Constructor<?> constructor = menuBuilderClass.getDeclaredConstructor(Context.class);
            mMenu = (Menu) constructor.newInstance(this);
            onCreateNavigationMenu(mMenu, getResourceMenu());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    private void onCreateNavigationMenu(Menu menu, @MenuRes int resMenuID) {
        new MenuInflater(this).inflate(resMenuID, menu);
        mMenuItemsPositions = new HashMap<>();
        mMenuItemsExpanded = new HashMap<>();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            // Salva la posizione dell'item nella view (+1 perchè è già presente l'header)
            mMenuItemsPositions.put(menuItem.getItemId(), i + 1);
            mMenuItemsExpanded.put(menuItem.getItemId(), false);
            //addView(menuItem);
        }
    }

    /*void addView(MenuItem menuItem) {
        NavDrawerMenuItemBinding binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.nav_drawer_menu_item, mNavigationView, false);
        binding.icon.setImageDrawable(menuItem.getIcon());
        binding.text.setText(menuItem.getTitle());
        binding.getRoot().setOnClickListener(v -> onMenuItemSelected(menuItem, binding));
        mNavigationView.addView(binding.getRoot());
    }

    void onMenuItemSelected(MenuItem menuItem, NavDrawerMenuItemBinding viewBinded) {
        boolean isLastSelectedItem = menuItem.getItemId() == mLastItemSelected;
        boolean isSelectedItemExpanded = mMenuItemsExpanded.get(menuItem.getItemId());
        if (menuItem.hasSubMenu()) {
            if (isSelectedItemExpanded) {
                Drawable drw = ContextCompat.getDrawable(this, R.drawable.ic_chevron_right);
                viewBinded.arrow.setImageDrawable(drw);
                mMenuItemsExpanded.put(menuItem.getItemId(), false);
                onRemoveSubMenu(menuItem);
            } else {
                checkRemove();

                Drawable drw = ContextCompat.getDrawable(this, R.drawable.ic_chevron_down);
                viewBinded.arrow.setImageDrawable(drw);
                mMenuItemsExpanded.put(menuItem.getItemId(), true);
                onCreateSubMenu(menuItem);
            }

        } else {
            if (!isLastSelectedItem) {
                checkRemove();
                onMenuItemSelected(menuItem);
            }
        }
        if (isLastSelectedItem) {
            mLastItemSelected = -1;
        } else {
            mLastItemSelected = menuItem.getItemId();
        }
    }

    private void checkRemove(){
        Boolean isLastItemExpanded = mMenuItemsExpanded.get(mLastItemSelected);
        if (isLastItemExpanded != null && isLastItemExpanded) {
            int position = mMenuItemsPositions.get(mLastItemSelected);
            Drawable drw = ContextCompat.getDrawable(this, R.drawable.ic_chevron_right);
            View lastItemView = mNavigationView.getChildAt(position);
            ImageView lastItemArrow = lastItemView.findViewById(R.id.arrow);
            lastItemArrow.setImageDrawable(drw);
            // Prendo la posizione dell'item nel menù (tolgo 1 perchè qui l'header non c'è)
            int menuPosition = position - 1;
            MenuItem lastMenuItem = mMenu.getItem(menuPosition);
            mMenuItemsExpanded.put(lastMenuItem.getItemId(), false);
            onRemoveSubMenu(lastMenuItem);
        }
    }

    private void onRemoveSubMenu(MenuItem parentMenuItem) {
        // Prendo l'index del primo child della view (padre +1)
        int index = mMenuItemsPositions.get(parentMenuItem.getItemId()) + 1;
        int itemCount = parentMenuItem.getSubMenu().size();
        for (int i = 0; i < itemCount; i++) {
            mNavigationView.removeViewAt(index);
        }
    }

    private void onCreateSubMenu(MenuItem parentMenuItem) {
        SubMenu menu = parentMenuItem.getSubMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            addView(parentMenuItem, menuItem, i);
        }
    }

    void addView(MenuItem parentMenuItem, MenuItem menuItem, int position) {
        NavDrawerSubmenuItemBinding binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.nav_drawer_submenu_item, mNavigationView, false);
        binding.icon.setImageDrawable(menuItem.getIcon());
        binding.text.setText(menuItem.getTitle());
        binding.getRoot().setOnClickListener(v -> onMenuItemSelected(menuItem));
        int index = mMenuItemsPositions.get(parentMenuItem.getItemId()) + 1 + position;
        mNavigationView.addView(binding.getRoot(), index);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    private class Header {
        TextView name;

        Header(View v) {
            name = v.findViewById(R.id.drawer_main_text);
        }
    }*/
}
