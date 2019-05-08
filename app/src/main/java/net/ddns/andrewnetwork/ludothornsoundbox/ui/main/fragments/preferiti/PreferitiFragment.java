package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.FragmentFavoriteBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiListAdapter.ThumbnailLoadedListener;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiViewPresenterBinder.IPreferitiPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiViewPresenterBinder.IPreferitiView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class PreferitiFragment extends BaseFragment implements IPreferitiView, IFragmentAdapterBinder {

    private FragmentFavoriteBinding mBinding;
    @Inject
    IPreferitiPresenter<IPreferitiView> mPresenter;

    public static PreferitiFragment newInstance() {

        Bundle args = new Bundle();

        PreferitiFragment fragment = new PreferitiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);

        ActivityComponent activityComponent = getActivityComponent();

        if (activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.getPreferitiList();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            mPresenter.getPreferitiList();
        }
    }

    @Override
    public void onPreferitoNonEsistente(LudoAudio audio) {
        CommonUtils.showDialog(getContext(), getString(R.string.audio_non_rimosso_preferiti_label));

    }

    @Override
    public void onPreferitoRimossoSuccess() {
        CommonUtils.showDialog(getContext(), getString(R.string.audio_rimosso_preferiti_label));

    }

    @Override
    public void onPreferitoRimossoFailed(String message) {
        CommonUtils.showDialog(getContext(), getString(R.string.audio_non_rimosso_preferiti_label));

    }

    @Override
    public void onPreferitiListLoaded(List<LudoAudio> audioList) {

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        if(mBinding.recyclerView.getItemAnimator() != null) {
            ((SimpleItemAnimator) mBinding.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }


        mBinding.recyclerView.setAdapter(new PreferitiListAdapter(this, mContext, audioList));
    }

    @Override
    public void onPreferitiListError(List<LudoAudio> audioListWithoutThumbnail) {

        CommonUtils.showDialog(mContext, "Impossibile caricare le informazioni dei video.");
        onPreferitiListLoaded(audioListWithoutThumbnail);
    }

    @Override
    public void onPreferitiListEmpty() {
        mBinding.globalLinear.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void playAudio(LudoAudio audio) {
        mBinding.audioPlayer.setAudio(audio);
        mBinding.audioPlayer.play();
    }

    @Override
    public void stopAudio(LudoAudio audio) {
        mBinding.audioPlayer.stop();
    }

    @Override
    public void loadThumbnail(LudoAudio audio, ThumbnailLoadedListener thumbnailLoadedListener) {
        LudoVideo video = audio.getVideo();
        if(video != null) {
            mPresenter.loadThumbnail(video, thumbnailLoadedListener);
        } else {
            thumbnailLoadedListener.onThumbnailLoaded(null);
        }
    }

    @Override
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        mBinding.audioPlayer.setOnCompletionListener(onCompletionListener);
    }
}
