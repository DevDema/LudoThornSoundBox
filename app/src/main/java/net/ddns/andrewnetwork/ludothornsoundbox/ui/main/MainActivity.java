package net.ddns.andrewnetwork.ludothornsoundbox.ui.main;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.ddns.andrewnetwork.ludothornsoundbox.BuildConfig;
import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ActivityMainBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainViewPresenterBinder.IMainPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainViewPresenterBinder.IMainView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random.RandomFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.SettingsActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.credits.CreditsActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems.LudoNavigationItem;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.social.SocialActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.web.WebActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.JsonUtil;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.ListUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.BlankFragmentActivity.KEY_EXTRA_FRAGMENT_ACTION;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.BlankFragmentActivity.REQUEST_BLANK_ACTIVITY;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeFragment.AUDIO_DATA;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeFragment.AUDIO_NOME;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems.SettingsNavigationItemsActivity.KEY_CURRENT_POSITION_BOT_NAV_MENU;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.AppUtils.DAYS_LATER_ASKING_FEEDBACK;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.AppUtils.LINK_ASKING_FEEDBACK;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils.nonEmptyNonNull;


public class MainActivity extends AdsActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, IMainView, SharedPreferences.OnSharedPreferenceChangeListener, SearchView.OnQueryTextListener {


    private ActivityMainBinding mBinding;
    @Inject
    IMainPresenter<IMainView> mPresenter;

    @IdRes
    int fragmentFirstSelection;
    private Fragment currentFragment;
    private List<LudoNavigationItem> orderedNavigationMenu;

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = menuItem -> {
        int id = menuItem.getItemId();

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);

        switch (id) {
            case R.id.action_home:
                selectOnBottomNavigationOrInstantiate(R.id.action_home);
                return true;
            case R.id.action_random:
                selectOnBottomNavigationOrInstantiate(R.id.action_random);
                return true;
            case R.id.action_video:
                selectOnBottomNavigationOrInstantiate(R.id.action_video);
                return true;
            case R.id.action_favorites:
                selectOnBottomNavigationOrInstantiate(R.id.action_favorites);
                return true;
            case R.id.action_credits:
                Intent creditsIntent = new Intent(this, CreditsActivity.class);
                startActivityForResult(creditsIntent, CreditsActivity.REQUEST_CREDITS);
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                settingsIntent.putExtra(KEY_CURRENT_POSITION_BOT_NAV_MENU, mBinding.appBarMain.navigation.getSelectedItemId());
                startActivityForResult(settingsIntent, SettingsActivity.REQUEST_SETTINGS);
                return true;
            case R.id.action_patreon:
                WebActivity.newInstance(this, AppUtils.PATREON_LINK);
                return true;
            case R.id.action_merchandise:
                WebActivity.newInstance(this, AppUtils.MERCHANDISE_LINK);
                return true;
            case R.id.action_social:
                Intent socialIntent = new Intent(this, SocialActivity.class);
                startActivityForResult(socialIntent, SocialActivity.REQUEST_SOCIAL);
                return true;
        }

        return false;
    };
    private String searchText;

    @Override
    public void onResume() {
        super.onResume();

        setRandomTextInNavigation();
    }

    private void setRandomTextInNavigation() {
        TextView randomLabel = mBinding.navView.getHeaderView(0).findViewById(R.id.random_text_label);
        List<String> stringList = StringUtils.buildRandomStringList(this);

        randomLabel.setText(ListUtils.selectRandomItem(stringList));
    }

    private void selectOnBottomNavigationOrInstantiate(@IdRes int actionId) {
        if (ViewUtils.hasItemId(mBinding.appBarMain.navigation, actionId)) {
            mBinding.appBarMain.navigation.setSelectedItemId(actionId);
        } else {
            Intent intent = new Intent(this, BlankFragmentActivity.class);
            intent.putExtra(KEY_EXTRA_FRAGMENT_ACTION, actionId);
            startActivityForResult(intent, REQUEST_BLANK_ACTIVITY);
        }
    }

    @Override
    protected void setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    protected int getFragmentContainerView() {
        return R.id.content_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(BuildConfig.DEBUG) {
            logCurrentFirebaseInstanceId();
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ActivityComponent activityComponent = getActivityComponent();
        if (activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        createBottomNavigationMenu();

        mBinding.navView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        mBinding.appBarMain.navigation.setOnNavigationItemSelectedListener(this);


        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(this);

        mBinding.appBarMain.navigation.setSelectedItemId(fragmentFirstSelection);

        handleIntents(getIntent()!= null ? getIntent() : new Intent());
    }

    @Override
    protected LinearLayout getAdRootView() {
        return (LinearLayout) mBinding.appBarMain.getRoot();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntents(intent);

    }

    private void handleIntents(Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals("OPEN_VIDEOS")) {
            selectOnBottomNavigationOrInstantiate(R.id.action_video);
            Log.d("IntentAction", intent.getAction() +" Received");
        }
    }

    private void logCurrentFirebaseInstanceId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    if (task.getResult() != null) {
                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "Token Firebase: " + token;
                        Log.d(TAG, msg);
                    } else {
                        Log.d(TAG, "Nessun Token");
                    }
                });
    }

    private @IdRes
    int getIdByCurrentFragment() {
        if (currentFragment != null) {
            if (currentFragment.getClass() == HomeFragment.class) {
                return R.id.action_home;
            } else if (currentFragment.getClass() == RandomFragment.class) {
                return R.id.action_random;
            } else if (currentFragment.getClass() == PreferitiAudioFragment.class) {
                return R.id.action_favorites;
            } else if (currentFragment.getClass() == VideoFragment.class) {
                return R.id.action_video;
            }
        }

        return -1;
    }

    private void createBottomNavigationMenu(List<LudoNavigationItem> navigationItemList) {
        Menu menu = mBinding.appBarMain.navigation.getMenu();

        menu.clear();

        for (LudoNavigationItem navigationItem : navigationItemList) {
            if (navigationItem.isVisible()) {
                menu.add(Menu.NONE, navigationItem.getId(), Menu.NONE, navigationItem.getName()).setIcon(navigationItem.getDrawableRes());
            }
        }
    }

    private void createBottomNavigationMenu() {
        createBottomNavigationMenu(orderedNavigationMenu);
    }

    private void manageFeedbackAlert() {
        mPresenter.incrementUsageCounter();
        if (mPresenter.getUsageCounter() > mPresenter.getUsageThreshold()) {
            showFeedBackDialog();
        }
    }

    @Override
    protected void managePreferences(SharedPreferences settings) {
        super.managePreferences(settings);


        this.fragmentFirstSelection = getFirstFragment(settings);
        this.orderedNavigationMenu = getOrderedNavigationMenu(settings);
    }

    private List<LudoNavigationItem> getOrderedNavigationMenu(SharedPreferences settings) {
        String serializedNavigationMenu = settings.getString(getString(R.string.cambia_ordine_key), "");
        return nonEmptyNonNull(serializedNavigationMenu) ?
                JsonUtil.getGson().fromJson(serializedNavigationMenu, new TypeToken<List<LudoNavigationItem>>() {
                }.getType()) :
                CommonUtils.createNavigationItemsList(this);
    }

    private @IdRes
    int getFirstFragment(SharedPreferences settings) {
        String fragment = settings.getString(getString(R.string.pag_iniziale_key), "Home");

        return StringUtils.getActionIdByString(fragment);
    }


    private void showFeedBackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.do_you_like_label))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    CommonUtils.openLink(this, LINK_ASKING_FEEDBACK);
                    mPresenter.saveUsageThreshold(Long.MAX_VALUE);
                    dialog.dismiss();
                })
                .setNeutralButton(getString(R.string.giammai_label), (dialog, which) -> {
                    mPresenter.saveUsageThreshold(Long.MAX_VALUE);
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.dopo_label), ((dialog, which) -> {
                    mPresenter.saveUsageThreshold(mPresenter.getUsageThreshold() + DAYS_LATER_ASKING_FEEDBACK);
                    dialog.dismiss();
                }));
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        Fragment fragment;

        switch (id) {
            //BOTTOM NAVIGATION MENU
            default:
            case R.id.action_home:
                fragment = HomeFragment.newInstance(loadAtOnce);
                break;
            case R.id.action_random:
                fragment = RandomFragment.newInstance();
                break;
            case R.id.action_video:
                fragment = VideoFragment.newInstance();
                break;
            case R.id.action_favorites:
                fragment = PreferitiFragment.newInstance();
                break;
        }

        if (fragment != null) {
            currentFragment = fragment;
            ViewUtils.selectByItemId(mBinding.navView, id);
            replaceFragmentIfNotExists(fragment);
        }
        return true;
    }

    public void replaceFragmentIfNotExists(Fragment newFragment) {
        boolean found = false;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (newFragment.getClass().isInstance(fragment)) {
                fragmentTransaction.show(fragment);

                found = true;
            }
        }

        if (found) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (!newFragment.getClass().isInstance(fragment)) {
                    fragmentTransaction.hide(fragment);
                }
            }

            fragmentTransaction.commit();
        } else addFragment(newFragment);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mAdView.setVisibility(View.VISIBLE);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (getFragmentContainer().getHeight() < 600) {
                        mAdView.setVisibility(View.GONE);
                        getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }

        invalidateOptionsMenu();


        /*Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(fragment)
                    .commit();
        }*/
    }

    public void onHomeFragmentReady() {
        manageFeedbackAlert();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.audio_nascosti_key))
                || key.equals(getString(R.string.dimensione_pulsanti_key))
                || key.equals(getString(R.string.mostra_info_in_audio_key))
        ) {
            HomeFragment fragment = getFragmentByClass(HomeFragment.class);

            if (fragment != null) {
                fragment.onAudioListChanged();
            }
        } else if (key.equals(getString(R.string.cambia_ordine_key))) {
            onBottomNavigationMenuChanged(JsonUtil.getGson().fromJson(sharedPreferences.getString(key, ""), new TypeToken<List<LudoNavigationItem>>() {
            }.getType()));
        }
    }

    private void onBottomNavigationMenuChanged(List<LudoNavigationItem> navigationItemList) {

        int id = mBinding.appBarMain.navigation.getSelectedItemId();

        createBottomNavigationMenu(navigationItemList);

        mBinding.appBarMain.navigation.setOnNavigationItemSelectedListener(null);

        mBinding.appBarMain.navigation.setSelectedItemId(id);

        mBinding.appBarMain.navigation.setOnNavigationItemSelectedListener(this);
    }

    private <T extends Fragment> T getFragmentByClass(Class<T> fragmentClass) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.getClass().equals(fragmentClass)) {
                return (T) fragment;
            }
        }

        return null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ViewUtils.selectByItemId(mBinding.navView, getIdByCurrentFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        if (currentFragment != null && currentFragment.getClass().equals(HomeFragment.class)) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.options_home_menu, menu);

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(this);
            return true;
        } else {
            menu.clear();
        }

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return getFragmentByClass(HomeFragment.class) == null;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.searchText = newText;

        HomeFragment homeFragment = getFragmentByClass(HomeFragment.class);
        if (homeFragment != null) {
            homeFragment.searchAudioByTitle(newText);
            return true;
        }
        return false;
    }

    public boolean forceSearchViewListener() {
        return onQueryTextChange(searchText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        HomeFragment homeFragment = getFragmentByClass(HomeFragment.class);

        if (homeFragment != null) {
            switch (item.getItemId()) {
                case R.id.action_shuffle:
                    return homeFragment.invertOrder();
                case R.id.action_nome:
                    return homeFragment.orderBy(AUDIO_NOME);
                case R.id.action_data:
                    return homeFragment.orderBy(AUDIO_DATA);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
