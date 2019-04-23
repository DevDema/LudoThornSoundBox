/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package net.ddns.andrewnetwork.ludothornsoundbox.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import net.ddns.andrewnetwork.ludothornsoundbox.MvpApp;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.FragmentLifeCycleListener;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.LifeCycleListener;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.fragments.CoreFragment;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * Created by janisharali on 27/01/17.
 */

public abstract class BaseFragment extends CoreFragment implements MvpView {

    protected BaseActivity mActivity;
    private ProgressDialog mProgressDialog;
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }

        this.mContext = context;
    }

    @Override
    public void showLoading() {
        getBaseActivity().showLoading();
    }

    @Override
    public void hideLoading() {
        if (getBaseActivity() != null) {
            getBaseActivity().hideLoading();
        }
    }

    @Override
    public void onError(String message) {
        if (mActivity != null) {
            mActivity.onError(message);
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.onError(resId);
        }
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (mActivity != null) {
            mActivity.showMessage(resId);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        if (mActivity != null) {
            return mActivity.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
        if (mActivity != null) {
            mActivity.openActivityOnTokenExpire();
        }
    }

    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    @Override
    public void addLifeCycleListener(LifeCycleListener listener, Bundle savedInstanceState) {
        if (listener instanceof FragmentLifeCycleListener) {
            FragmentLifeCycleListener flcl = (FragmentLifeCycleListener) listener;
            addLifeCycleListener(flcl, savedInstanceState);
        }
    }

    @Override
    public boolean isAppVisible() {
        return MvpApp.isActivityVisible();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}
