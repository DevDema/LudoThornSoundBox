package net.ddns.andrewnetwork.ludothornsoundbox.ui.main;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;

public interface MainViewPresenterBinder {

    interface IMainPresenter<V extends IMainView> extends MvpPresenter<V> {

        void incrementUsageCounter();

        int getUsageCounter();

        long getUsageThreshold();

        void saveUsageThreshold(long threshold);
    }

    interface IMainView extends MvpView {

    }
}
