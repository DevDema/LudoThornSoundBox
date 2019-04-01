package net.ddns.andrewnetwork.ludothornsoundbox.ui.main;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainViewPresenterBinder.IMainPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainViewPresenterBinder.IMainView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MainPresenter<V extends IMainView> extends BasePresenter<V> implements IMainPresenter<V> {

    @Inject
    public MainPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void incrementUsageCounter() {
        getDataManager().incrementUsageCounter(getDataManager().getUsageCounter()+1);
    }

    @Override
    public int getUsageCounter() {
        return getDataManager().getUsageCounter();
    }

    @Override
    public long getUsageThreshold() {
        return getDataManager().getUsageThreshold();
    }

    @Override
    public void saveUsageThreshold(long threshold) {
        getDataManager().setUsageThreshold(threshold);
    }
}
