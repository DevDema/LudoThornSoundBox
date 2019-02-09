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

/**
 * Created by janisharali on 27/01/17.
 */

import android.content.Intent;
import android.os.Bundle;



import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.core.extras.LifeCycleListener;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<V extends MvpView> implements MvpPresenter<V>, LifeCycleListener {

    private static final String TAG = "BasePresenter";

    private final DataManager mDataManager;
    private final SchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mGlobalCompositeDisposable;

    private CompositeDisposable mLocalCompositeDisposable;

    private V mMvpView;

    @Inject
    public BasePresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        this.mDataManager = dataManager;
        this.mSchedulerProvider = schedulerProvider;
        this.mGlobalCompositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
        mLocalCompositeDisposable = new CompositeDisposable();
        mvpView.addLifeCycleListener(this, mvpView.getSavedInstanceState());
    }

    @Override
    public void onDetach() {
        mGlobalCompositeDisposable.dispose();
        mMvpView = null;
    }

    @Override
    public void onDispose() {
        if (this.mLocalCompositeDisposable != null && !this.mLocalCompositeDisposable.isDisposed()) {
            this.mLocalCompositeDisposable.dispose();
        }
    }

    public CompositeDisposable getCompositeDisposable() {
        if (mLocalCompositeDisposable == null || (mLocalCompositeDisposable != null && mLocalCompositeDisposable.isDisposed())) {
            mLocalCompositeDisposable = new CompositeDisposable();
        }
        return mLocalCompositeDisposable;
    }

    public CompositeDisposable getGlobalCompositeDisposable() {
        return mGlobalCompositeDisposable;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        onDispose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
