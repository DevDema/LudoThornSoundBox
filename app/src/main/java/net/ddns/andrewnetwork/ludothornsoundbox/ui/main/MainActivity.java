package net.ddns.andrewnetwork.ludothornsoundbox.ui.main;

import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ActivityMainBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment;


public class MainActivity extends ParentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;

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

        replaceFragment(new HomeFragment());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);
        mBinding.appBarMain.navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        Fragment fragment;

        switch (id) {
            default:
            case R.id.action_home:
                fragment = new HomeFragment();
                break;
            case R.id.action_random:
                fragment = new HomeFragment();
                break;
            case R.id.action_video:
                fragment = new VideoFragment();
                break;
            case R.id.action_favorites:
                fragment = new HomeFragment();
                break;
            case R.id.action_info:
                fragment = new HomeFragment();
                break;
        }

        replaceFragmentIfNotExists(fragment);
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

        if(found) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (!newFragment.getClass().isInstance(fragment)) {
                    fragmentTransaction.hide(fragment);
                }
            }

            fragmentTransaction.commit();
        }

        else addFragment(newFragment);

    }
}
