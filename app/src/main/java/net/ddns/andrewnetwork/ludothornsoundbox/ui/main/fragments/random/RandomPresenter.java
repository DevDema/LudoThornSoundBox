package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random;

import net.ddns.andrewnetwork.ludothornsoundbox.data.DataManager;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random.RandomViewPresenterBinder.IRandomPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.random.RandomViewPresenterBinder.IRandomView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class RandomPresenter<V extends IRandomView> extends BasePresenter<V> implements IRandomPresenter<V> {

    @Inject
    public RandomPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public List<LudoAudio> getAudioListFromPref() {
        return getDataManager().getAudioSavedList();
    }
}
