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

import android.app.Application;
import android.content.Context;

import net.ddns.andrewnetwork.ludothornsoundbox.BuildConfig;
import net.ddns.andrewnetwork.ludothornsoundbox.data.AppDataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.network.ApiHeader;
import net.ddns.andrewnetwork.ludothornsoundbox.data.network.ApiHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.data.network.AppApiHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.data.persistence.AppDatabaseHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.data.persistence.DatabaseHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.data.prefs.AppPreferencesHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.data.prefs.PreferencesHelper;
import net.ddns.andrewnetwork.ludothornsoundbox.di.ApiInfo;
import net.ddns.andrewnetwork.ludothornsoundbox.di.ApplicationContext;
import net.ddns.andrewnetwork.ludothornsoundbox.di.DatabaseInfo;
import net.ddns.andrewnetwork.ludothornsoundbox.di.PreferenceInfo;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by janisharali on 27/01/17.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
        return BuildConfig.API_KEY;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    DatabaseHelper provideDatabaseHelper(AppDatabaseHelper databaseHelper) {
        return databaseHelper;
    }

    @Provides
    @Singleton
    ApiHeader.ProtectedApiHeader provideProtectedApiHeader(@ApiInfo String apiKey,
                                                           PreferencesHelper preferencesHelper) {
        return new ApiHeader.ProtectedApiHeader(
                apiKey,
                preferencesHelper.getCurrentUserId(),
                preferencesHelper.getAccessToken());
    }

}
