package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.advpopup;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.DialogAdvBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.web.WebActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.ListUtils;

import java.util.List;

public class AdvertisementFragment extends DialogFragment {

    private DialogAdvBinding mBinding;
    private Drawable drawable;
    private String url;

    public static AdvertisementFragment newInstance() {

        Bundle args = new Bundle();

        AdvertisementFragment fragment = new AdvertisementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getContext() != null) {
            initImagesMap(getContext());
        } else {
            dismiss();
        }
    }

    private void initImagesMap(@NonNull Context context) {
            List<Pair<Integer, String>> list = ListUtils.getAdvertisementImages();

            if(!list.isEmpty()) {
                Pair<Integer, String> item = ListUtils.selectRandomItem(list);
                try {
                    drawable = ContextCompat.getDrawable(context, item.first);
                } catch (Resources.NotFoundException ignored) {
                }

                url = item.second;
            }

            if(drawable == null) {
                dismiss();
            }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_adv, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.buttonConferma.setOnClickListener(v -> dismiss());

        if(drawable != null) {
            mBinding.imageView.setImageDrawable(drawable);
        }

        mBinding.imageView.setOnClickListener(v -> {
            if(getActivity() != null) {
                WebActivity.newInstance(getActivity(), url);
            }
        });
    }
}
