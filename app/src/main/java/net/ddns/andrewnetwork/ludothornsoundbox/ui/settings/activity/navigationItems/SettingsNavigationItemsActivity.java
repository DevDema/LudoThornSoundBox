package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ActivitySettingsNavigationItemsBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingsNavigationItemsActivity extends PreferencesManagerActivity {
    public static final String KEY_CURRENT_POSITION_BOT_NAV_MENU = "KEY_CURRENT_POSITION_BOT_NAV_MENU";
    public static final int REQUEST_NAVIGATION_SELECTED = 4044;
    public static final String KEY_NAVIGATION_ITEMS = "KEY_NAVIGATION_ITEMS";
    public static final String KEY_SAVED_NAVIGATION_ITEMS = "KEY_SAVED_NAVIGATION_ITEMS";
    public static final String KEY_FIRST_POSITION_BOT_NAV_MENU = "KEY_FIRST_POSITION_BOT_NAV_MENU";
    private ActivitySettingsNavigationItemsBinding mBinding;
    private List<LudoNavigationItem> navigationItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }

        int currentNavigationPosition = 0;
        int firstNavigationPosition = 0;

        if(getIntent() != null && getIntent().getExtras() != null) {
            String serializedSavednavigationItemList = getIntent().getExtras().getString(KEY_SAVED_NAVIGATION_ITEMS);
            currentNavigationPosition = getIntent().getExtras().getInt(KEY_CURRENT_POSITION_BOT_NAV_MENU);
            firstNavigationPosition = getIntent().getExtras().getInt(KEY_FIRST_POSITION_BOT_NAV_MENU);

            navigationItemList = createNavigationItemsList(this, serializedSavednavigationItemList);
        }

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBinding.recyclerView.setAdapter(new NavigationItemsAdapter(this, navigationItemList, currentNavigationPosition, firstNavigationPosition));

        mBinding.recyclerView.setVisibility(View.VISIBLE);

        mBinding.button.setOnClickListener(v -> save());
    }

    private void save() {
        Intent intent = new Intent();
        intent.putExtra(KEY_NAVIGATION_ITEMS, JsonUtil.getGson().toJson(navigationItemList));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings_navigation_items);

    }

    @Override
    protected int getFragmentContainerView() {
        return 0;
    }

    private static List<LudoNavigationItem> createNavigationItemsList(Context context, String serializedNavigationItems) {
        List<LudoNavigationItem> navigationItemList;

        List<LudoNavigationItem> navigationItemListFromPref = JsonUtil.getGson().fromJson(serializedNavigationItems, new TypeToken<List<LudoNavigationItem>>(){}.getType());
        navigationItemListFromPref = navigationItemListFromPref != null ? navigationItemListFromPref : new ArrayList<>();
        List<LudoNavigationItem> defaultNavigationItems = CommonUtils.createNavigationItemsList(context);
        if(navigationItemListFromPref.isEmpty()) {
            navigationItemList = defaultNavigationItems;
        } else {
            navigationItemList = navigationItemListFromPref;

            if(navigationItemListFromPref.size() != defaultNavigationItems.size()) {
                if(defaultNavigationItems.size() > navigationItemListFromPref.size()) {
                    List<LudoNavigationItem> addedList = new ArrayList<>(defaultNavigationItems);

                    addedList.removeAll(navigationItemListFromPref);

                    navigationItemList.addAll(addedList);

                } else {
                    List<LudoNavigationItem> removedList = new ArrayList<>(navigationItemListFromPref);

                    removedList.removeAll(defaultNavigationItems);

                    navigationItemList.removeAll(removedList);
                }
            }
        }

        return navigationItemList;
    }

    /*private static boolean changeByVisibilityByPreferences(LudoNavigationItem item, List<LudoNavigationItem> navigationItemListFromPref) {
        for(LudoNavigationItem navigationItemFromPref : navigationItemListFromPref) {

            if(item.getDrawableId() == navigationItemFromPref.getDrawableId()) {
                item.setVisible(navigationItemFromPref.isVisible());
                return true;
            }
        }

        return false;
    }*/
}
