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

package net.ddns.andrewnetwork.ludothornsoundbox.di.component;

import net.ddns.andrewnetwork.ludothornsoundbox.di.PerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.di.module.ActivityModule;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random.RandomFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio.SettingsHiddenAudioActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsFragment;

import dagger.Component;

/**
 * Created by janisharali on 27/01/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {


    void inject(VideoFragment fragment);

    void inject(HomeFragment fragment);

    void inject(PreferitiFragment fragment);

    void inject(RandomFragment randomFragment);

    void inject(VideoInformationFragment videoInformationFragment);

    void inject(MainActivity mainActivity);

    void inject(SettingsFragment settingsFragment);

    void inject(SettingsHiddenAudioActivity settingsHiddenAudioActivity);
}
