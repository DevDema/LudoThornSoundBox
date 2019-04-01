package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;

import javax.inject.Singleton;

import io.reactivex.Single;

public interface VideoInformationViewPresenterBinder {

    interface IVideoInformationView extends MvpView {

        void onThumbnailLoadSuccess(Thumbnail thumbnail);

        void onThumbnailLoadFailed();
    }

    interface IVideoInformationPresenter<V extends IVideoInformationView> extends MvpPresenter<V> {

        void getThumbnail(LudoVideo video);
    }
}
