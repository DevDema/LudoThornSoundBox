package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsViewPresenterBinder.ISettingsPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsViewPresenterBinder.ISettingsView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

public class SettingsPresenter<V extends ISettingsView> extends BasePresenter<V> implements ISettingsPresenter<V> {

    @Inject
    public SettingsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

}
