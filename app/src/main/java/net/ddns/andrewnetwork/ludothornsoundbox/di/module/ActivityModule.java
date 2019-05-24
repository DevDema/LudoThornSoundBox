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

package net.ddns.andrewnetwork.ludothornsoundbox.di.module;

import android.app.Activity;
import android.content.Context;

import net.ddns.andrewnetwork.ludothornsoundbox.di.ActivityContext;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainViewPresenterBinder.IMainPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainViewPresenterBinder.IMainView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomeView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationViewPresenterBinder.IVideoInformationPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationViewPresenterBinder.IVideoInformationView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiMainPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiMainViewPresenterBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiViewPresenterBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.audio.PreferitiAudioViewPresenterBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video.PreferitiVideoPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.video.PreferitiVideoViewPresenterBinder;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random.RandomPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random.RandomViewPresenterBinder.IRandomPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random.RandomViewPresenterBinder.IRandomView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio.SettingsHiddenAudioPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio.SettingsHiddenAudioViewPresenterBinder.ISettingsHiddenAudioPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio.SettingsHiddenAudioViewPresenterBinder.ISettingsHiddenAudioView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsViewPresenterBinder.ISettingsPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsViewPresenterBinder.ISettingsView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoViewPresenterBinder.IVideoView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.AppSchedulerProvider;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by janisharali on 27/01/17.
 */

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {
        return new LinearLayoutManager(activity);
    }

    //      EXAMPLE CALL
    @Provides
    IMainPresenter<IMainView> provideMainPresenter(MainPresenter<IMainView> presenter) {
        return presenter;
    }

    @Provides
    IVideoPresenter<IVideoView> provideVideoPresenter(VideoPresenter<IVideoView> presenter) {
        return presenter;
    }

    @Provides
    IHomePresenter<IHomeView> provideHomePresenter(HomePresenter<IHomeView> presenter) {
        return presenter;
    }

    @Provides
    PreferitiAudioViewPresenterBinder.IPreferitiPresenter<PreferitiAudioViewPresenterBinder.IPreferitiView> provideAudioPreferitiPresenter(PreferitiAudioPresenter<PreferitiAudioViewPresenterBinder.IPreferitiView> presenter) {
        return presenter;
    }

    @Provides
    IRandomPresenter<IRandomView> provideRandomPresenter(RandomPresenter<IRandomView> presenter) {
        return presenter;
    }

    @Provides
    IVideoInformationPresenter<IVideoInformationView> provideVideoInformationPresenter(VideoInformationPresenter<IVideoInformationView> presenter) {
        return presenter;
    }

    @Provides
    ISettingsPresenter<ISettingsView> provideSettingsPresenter(SettingsPresenter<ISettingsView> presenter) {
        return presenter;
    }

    @Provides
    ISettingsHiddenAudioPresenter<ISettingsHiddenAudioView> provideSettingsHiddenAudioPresenter(SettingsHiddenAudioPresenter<ISettingsHiddenAudioView> presenter) {
        return presenter;
    }

    @Provides
    PreferitiVideoViewPresenterBinder.IPreferitiPresenter<PreferitiVideoViewPresenterBinder.IPreferitiView> provideVideoPreferitiPresenter(PreferitiVideoPresenter<PreferitiVideoViewPresenterBinder.IPreferitiView> presenter) {
        return presenter;
    }

    @Provides
    PreferitiMainViewPresenterBinder.IPreferitiMainPresenter<MvpView> provideMainPreferitiPresenter(PreferitiMainPresenter<MvpView> presenter) {
        return presenter;
    }
}
