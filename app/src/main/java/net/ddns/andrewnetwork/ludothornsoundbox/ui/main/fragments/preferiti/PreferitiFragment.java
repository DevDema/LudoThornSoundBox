package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.FragmentFavoriteBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiViewPresenterBinder.IPreferitiPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.preferiti.PreferitiViewPresenterBinder.IPreferitiView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

public class PreferitiFragment extends BaseFragment implements IPreferitiView {

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

        createList();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            createList();
        }
    }

    private void createList() {
        mBinding.linear.removeAllViews();

        List<LudoAudio> preferitiList = mPresenter.getPreferitiList();
        if (!preferitiList.isEmpty()) {
            mBinding.nofavorite.setVisibility(View.GONE);
            for (LudoAudio audio : preferitiList) {
                Button button = new Button(getActivity());
                button.setText(audio.getTitle());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ViewGroup.MarginLayoutParams params2 = new ViewGroup.MarginLayoutParams(params);
                params2.setMargins(50, 10, 50, 10);
                button.setLayoutParams(params2);
                button.setOnClickListener(v -> AudioUtils.playTrack(getContext(), audio, null));
                button.setOnLongClickListener(view2 -> {
                    mPresenter.rimuoviPreferito(audio);
                    return true;
                });

                button.setTypeface(ResourcesCompat.getFont(getContext(), R.font.knewave));
                button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_white));
                button.setMaxLines(2);
                button.setHeight((int) getResources().getDimension(R.dimen.input_size_xxs));
                button.setVisibility(View.VISIBLE);
                button.setGravity(Gravity.CENTER_VERTICAL);
                mBinding.linear.addView(button);
            }
        } else {
            mBinding.deletefavorite.setVisibility(View.GONE);
            mBinding.nofavorite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPreferitoNonEsistente(LudoAudio audio) {
        CommonUtils.showDialog(getContext(), getString(R.string.audio_non_rimosso_preferiti_label));

    }

    @Override
    public void onPreferitoRimossoSuccess() {
        CommonUtils.showDialog(getContext(), getString(R.string.audio_rimosso_preferiti_label));
        createList();
    }

    @Override
    public void onPreferitoRimossoFailed(String message) {
        CommonUtils.showDialog(getContext(), getString(R.string.audio_non_rimosso_preferiti_label));

    }
}
