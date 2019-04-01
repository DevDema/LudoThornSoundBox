package net.ddns.andrewnetwork.ludothornsoundbox.utils;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public abstract class PermissionManager implements OnRequestPermissionsResultCallback {

    public static final int REQUEST_PERMISSION_SETTING = 9833;

    private OnPermissionResultListener mResultListener;

    private String mPermission;

    private int mRequestCode;

    public static class Builder {

        PermissionManager mPermissionManager;

        public PermissionManager.Builder setResultListener(OnPermissionResultListener listener) {
            mPermissionManager.mResultListener = listener;
            return this;
        }


        public PermissionManager.Builder with(BaseFragment fragment) {
            mPermissionManager = new FragmentPermissionManager(fragment);
            return this;
        }

        public PermissionManager.Builder with(BaseActivity activity) {
            mPermissionManager = new ActivityPermissionManager(activity);
            return this;
        }

        public PermissionManager build() {
            return mPermissionManager;
        }
    }

    public static void openAppPermissionsSettingsForResult(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    public static void openAppPermissionsSettingsForResult(Fragment fragment) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", fragment.getActivity().getPackageName(), null);
        intent.setData(uri);
        fragment.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    abstract void requestPermissions(String permission, int requestCode);

    abstract Activity getActivityContext();

    public void checkPermission(String permission, int requestCode) {
        mPermission = permission;
        mRequestCode = requestCode;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(getActivityContext(), permission) != PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(getActivityContext(), permission)) {
                    if (mResultListener != null) {
                        mResultListener.onPermissionRepeated();
                    }
                }
                requestPermissions(permission, requestCode);
            } else {
                if (mResultListener != null) {
                    mResultListener.onPermissionGranted();
                }
            }
        } else {
            if (mResultListener != null) {
                mResultListener.onPermissionGranted();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == mRequestCode && permissions.length > 0 && mPermission.equals(permissions[0])) {
            if (mResultListener != null) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mResultListener.onPermissionGranted();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(getActivityContext(), permissions[0]);
                    if (!showRationale) {
                        mResultListener.onBlockedRequest();
                    }
                } else {
                    mResultListener.onPermissionDenied();
                }
            }
        }
    }

    private static class FragmentPermissionManager extends PermissionManager {
        BaseFragment mFragment;

        FragmentPermissionManager(BaseFragment fragment) {
            this.mFragment = fragment;
            mFragment.addOnRequestPermissionsResultListener(this);
        }

        @Override
        void requestPermissions(String permission, int requestCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mFragment.requestPermissions(new String[]{permission}, requestCode);
            }
        }

        @Override
        Activity getActivityContext() {
            return mFragment.getActivity();
        }


    }

    private static class ActivityPermissionManager extends PermissionManager {
        BaseActivity mActivity;

        ActivityPermissionManager(BaseActivity activity) {
            this.mActivity = activity;
            mActivity.addRequestPermissionsResultListner(this);
        }

        @Override
        void requestPermissions(String permission, int requestCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.requestPermissions(new String[]{permission}, requestCode);
            }
        }

        @Override
        Activity getActivityContext() {
            return mActivity;
        }

    }

    public interface OnPermissionResultListener {

        void onPermissionGranted();

        void onPermissionDenied();

        void onBlockedRequest();

        void onPermissionRepeated();

    }

}
