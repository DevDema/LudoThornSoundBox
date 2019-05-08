package net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.LifeCycleListener;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.fragments.CoreFragment;

import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;

/**
 * @author Antonio Cornacchia
 * @version 1.3
 * @edit: Antonio Cornacchia
 * @date: 29/10/2018
 */
public abstract class CoreActivity extends AppCompatActivity implements ICoreActivity {

    public static final String TAG = "CoreActivity";

    /**
     * Intent to launch new fragment in new mActivity
     **/
    public static final String INTENT_FRAGMENT_NAME = "FRAGMENT_NAME";

    /**
     * Intent used to store the launched request code (for a back result in onActivityResult())
     **/
    public static final String INTENT_REQUEST_CODE = "REQUEST_CODE";

    /**
     * Intent to pass data to next mActivity
     **/
    public static final String NAVIGATION_INTENT = "NAVIGATION_INTENT";

    /**
     * Fragment manager to handle fragments in activities
     **/
    protected FragmentManager mFragmentManager = null;

    /**
     * The toolbar container, it can be null
     **/
    protected AppBarLayout mAppBarLayout = null;

    /**
     * The main toolbar of the mActivity, it can be null
     **/
    protected Toolbar mToolbar = null;

    /**
     * The parent view
     **/
    protected View mView = null;

    /**
     * The number of fragments in the mActivity
     **/
    protected int mFragmentsNumber = 0;

    /**
     * The state of home button icon (toolbar hamburger)
     **/
    protected boolean mIsHomeButtonEnabled = false;

    /**
     * The state of home button icon (toolbar arrow)
     **/
    protected boolean mIsDisplayHomeAsUpEnabled = false;

    /**
     * Return true if mActivity is in foreground, false otherwise
     **/
    protected boolean mIsRunning = false;

    /**
     * The Activity's lifecycle listeners added
     **/
    protected Stack<LifeCycleListener> mActivityLifeCycleListeners;

    /**
     * The Activity's permissions result listeners added
     **/
    protected Stack<OnRequestPermissionsResultCallback> mRequestResults;

    /**
     * The Activity's saved state
     **/
    protected Bundle mSavedState;

    /**
     * Contains the class implementation
     **/
    private Delegate mDelegate = new Delegate(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLifeCycleListeners = new Stack<>();
        mRequestResults = new Stack<>();
        setContentView();
        onCreateView(savedInstanceState);
    }

    @Override
    public void addLifeCycleListener(LifeCycleListener alcl, Bundle savedInstanceState) {
        mDelegate.addLifeCycleListener(alcl, savedInstanceState);
    }

    @Override
    public void addRequestPermissionsResultListner(OnRequestPermissionsResultCallback listener) {
        mDelegate.addRequestPermissionsResultListner(listener);
    }

    @Override
    public View onCreateView(Bundle savedInstanceState) {
        return mDelegate.onCreateView(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    public void onPause() {
        mDelegate.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mDelegate.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Set content view only require to call the setContentView(int resId) method.
     * It is possible to add some code below, that will be called before onCreate()
     **/
    protected abstract void setContentView();

    /**
     * Override this method to get the exact id of the layout resource in which you want to put
     * the fragments (example: R.id.fragmnet_container)
     **/
    protected abstract int getFragmentContainerView();

    @Override
    public View getView() {
        return mDelegate.getView();
    }

    @Override
    public AppBarLayout getAppBarLayout() {
        return mDelegate.getAppBarLayout();
    }

    @Override
    public Toolbar getToolbar() {
        return mDelegate.getToolbar();
    }

    @Override
    public Activity getLastInstance() {
        return mDelegate.getLastInstance();
    }

    @Override
    public Fragment getLastFragment() {
        return mDelegate.getLastFragment();
    }

    @Override
    public boolean isRunning() {
        return mDelegate.isRunning();
    }

    @Override
    public boolean isHomeButtonEnabled() {
        return mDelegate.isHomeButtonEnabled();
    }

    @Override
    public void setHomeButtonEnabled(boolean isHomeButtonEnabled) {
        mDelegate.setHomeButtonEnabled(isHomeButtonEnabled);
    }

    @Override
    public boolean isDisplayHomeAsUpEnabled() {
        return mDelegate.isDisplayHomeAsUpEnabled();
    }

    @Override
    public void setDisplayHomeAsUpEnabled(boolean isDisplayHomeAsUpEnabled) {
        mDelegate.setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled);
    }

    private void parseNavigationIntent(Intent intent) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(NAVIGATION_INTENT)) {
            intent.putExtra(NAVIGATION_INTENT, bundle.getInt(NAVIGATION_INTENT));
            bundle.remove(NAVIGATION_INTENT);
        }
    }

    /**
     * Create back stack when starting the mActivity
     *
     * @reference: https://developer.android.com/training/implementing-navigation/temporal.html
     **/
    @Override
    public void startActivity(Intent intent) {
        parseNavigationIntent(intent);
        PendingIntent pendingIntent = TaskStackBuilder.create(this)
                // add all of DetailsActivity's parents to the stack,
                // followed by DetailsActivity itself
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setContentIntent(pendingIntent);
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Class<? extends Activity> cls) {
        mDelegate.startActivity(cls);
    }

    @Override
    public void startFragment(Fragment fragment, Class<? extends SecondActivity> cls) {
        mDelegate.startFragment(fragment, cls);
    }

    @Override
    public void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                       int requestCode) {
        mDelegate.startFragmentForResult(fragment, cls, requestCode);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startFragment(Fragment fragment, Class<? extends SecondActivity> cls,
                              ActivityOptionsCompat animation) {
        mDelegate.startFragment(fragment, cls, animation);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startFragment(Fragment fragment, Class<? extends SecondActivity> cls, View view,
                              @StringRes int animation) {
        mDelegate.startFragment(fragment, cls, view, animation);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startFragmentForResult(Fragment fragment, Class<? extends SecondActivity> cls,
                                       int requestCode, ActivityOptionsCompat animation) {
        mDelegate.startFragmentForResult(fragment, cls, requestCode, animation);
    }

    @Override
    public int getRequestCode() {
        return mDelegate.getRequestCode();
    }

    @Override
    public void addFragment(Fragment fragment) {
        mDelegate.addFragment(fragment);
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        mDelegate.replaceFragment(fragment);
    }

    @Override
    public int getBackStackCount() {
        return mDelegate.getBackStackCount();
    }

    @Override
    public int getFragmentsNumber() {
        return mDelegate.getFragmentsNumber();
    }

    @Override
    public void showToastLong(@StringRes int resString) {
        mDelegate.showToastLong(resString);
    }

    @Override
    public void showToastShort(@StringRes int resString) {
        mDelegate.showToastShort(resString);
    }

    @Override
    public void showDialog(Dialog dialog) {
        mDelegate.showDialog(dialog);
    }

    @Override
    public void showAlertDialog(@StringRes int resTitle, @StringRes int resMessage) {
        mDelegate.showAlertDialog(resTitle, resMessage);
    }

    @Override
    public AlertDialog.Builder buildAlertDialog(@StringRes int resTitle, @StringRes int resMessage) {
        return mDelegate.buildAlertDialog(resTitle, resMessage);
    }

    @Override
    public void showSnackbar(@StringRes int resTitle) {
        mDelegate.showSnackbar(resTitle);
    }

    @Override
    public Snackbar buildSnackbar(@StringRes int resTitle) {
        return mDelegate.buildSnackbar(resTitle);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        mDelegate.onSaveInstanceState(savedState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState, PersistableBundle persistableBundle) {
        super.onSaveInstanceState(savedState, persistableBundle);
        mDelegate.onSaveInstanceState(savedState);
    }

    @Override
    public Bundle getSavedInstanceState() {
        return mDelegate.getSavedInstanceState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean onOptionItemSelected;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent mActivity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                onOptionItemSelected = true;
                try {
                    Intent upIntent = NavUtils.getParentActivityIntent(this);
                    if (upIntent != null) {
                        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                            // This mActivity is NOT part of this app's task, so create a new task
                            // when navigating up, with a synthesized back stack.
                            TaskStackBuilder.create(this)
                                    // Add all of this mActivity's parents to the back stack
                                    .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                                    .startActivities();
                        } else {
                            // This mActivity is part of this app's task, so simply
                            // navigate up to the logical parent mActivity.
                            NavUtils.navigateUpTo(this, upIntent);
                        }
                    } else {
                        NavUtils.navigateUpFromSameTask(this);
                    }
                } catch (Exception e) {
                    onBackPressed();
                }
                break;
            default:
                onOptionItemSelected = super.onOptionsItemSelected(item);
                break;
        }
        return onOptionItemSelected;
    }

    @Override
    public void onBackPressed() {
        boolean hasOnBackPressed = false;
        // Check if fragment has actions to do before performing mActivity's onBackPressed()
        Fragment fragment = mFragmentManager.findFragmentById(getFragmentContainerView());
        if (fragment instanceof CoreFragment) {
            CoreFragment baseFragment = (CoreFragment) fragment;
            if (baseFragment.hasOnBackPressed()) {
                hasOnBackPressed = true;
                baseFragment.onBackPressed();
            }
        }
        // If fragment does not implement onBackPressed
        if (!hasOnBackPressed) {
            if (getBackStackCount() > 0) {
                mFragmentManager.popBackStack();
                Log.d(TAG, ".onBackPressed() -> popping backstack");
            } else {
                super.onBackPressed();
                Log.d(TAG, ".onBackPressed() -> nothing on backstack, calling super");
            }
        }
    }

    public View getFragmentContainer() {
        return findViewById(getFragmentContainerView());
    }
}