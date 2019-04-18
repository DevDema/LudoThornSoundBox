package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePrefencesFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.ParentActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.settings.SettingsViewPresenterBinder.ISettingsPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.settings.SettingsViewPresenterBinder.ISettingsView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;

import java.lang.reflect.Array;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

public class SettingsFragment extends BasePrefencesFragment implements ISettingsView, SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    ISettingsPresenter<ISettingsView> mPresenter;

    private @StringRes int[] mandatoryPreferences = { R.string.usa_font_app_key };
    public SettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ActivityComponent activityComponent = getActivityComponent();

        if (activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(isMandatory(key)) {
            onMandatoryPreferenceChanged(sharedPreferences, key);
        }
    }

    private void onMandatoryPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        CommonUtils.showDialog(mActivity,
                "Opzione obbligatoria",
                "Per effettuare la modifica è necessario riavviare l'app. Procedere?",
                (dialog, which) -> ((ParentActivity) mActivity).restartActivity(),
                true);
    }

    private boolean isMandatory(String key) {
        for (int mandatoryPreference : mandatoryPreferences) {
            if (getString(mandatoryPreference).equals(key)) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}