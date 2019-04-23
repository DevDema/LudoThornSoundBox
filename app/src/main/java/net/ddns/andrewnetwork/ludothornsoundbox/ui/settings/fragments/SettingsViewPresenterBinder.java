package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;


public interface SettingsViewPresenterBinder {

    interface ISettingsView extends MvpView {

    }

    interface ISettingsPresenter<V extends ISettingsView> extends MvpPresenter<V> {

    }
}
