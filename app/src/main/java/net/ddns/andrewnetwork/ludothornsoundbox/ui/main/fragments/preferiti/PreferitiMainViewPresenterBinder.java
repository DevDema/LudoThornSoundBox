package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import android.content.SharedPreferences;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;

public interface PreferitiMainViewPresenterBinder {

    interface IPreferitiMainPresenter<V extends MvpView> extends MvpPresenter<V> {

        void registerOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener);

        void unregisterOnSharedPreferencesChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener);
    }
}
