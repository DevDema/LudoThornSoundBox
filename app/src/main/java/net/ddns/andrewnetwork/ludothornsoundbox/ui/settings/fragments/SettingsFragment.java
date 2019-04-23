package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePrefencesFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.ParentActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.SettingsActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsViewPresenterBinder.ISettingsPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsViewPresenterBinder.ISettingsView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;

public class SettingsFragment extends BasePrefencesFragment implements ISettingsView, SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener, MvpView, BaseFragment.Callback {

    @Inject
    ISettingsPresenter<ISettingsView> mPresenter;

    private @StringRes
    int[] mandatoryPreferences = {R.string.usa_font_app_key};

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);


        bindPreferenceSummaryToValue(findPreference(getString(R.string.usa_font_app_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.carica_audio_insieme_key)));
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

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        /*onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), true));*/
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


    }

    private void onMandatoryPreferenceChanged(String key) {
        CommonUtils.showDialog(mActivity,
                R.style.PreferenceActivityTheme_DialogTheme,
                getString(R.string.mandatory_setting_label),
                getString(R.string.mandatory_setting_summary_label), (dialog, which) -> ((SettingsActivity) mActivity).restartApp(),
                false);
    }

    private boolean isMandatory(String key) {
        for (@StringRes int mandatoryPreference : mandatoryPreferences) {
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


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Log.d("PrefListener", "Selezionata preferenza " + newValue + " per: " + preference);

        String key = preference.getKey();

        if (isMandatory(key)) {
            onMandatoryPreferenceChanged(key);
        }

        return true;
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }
}