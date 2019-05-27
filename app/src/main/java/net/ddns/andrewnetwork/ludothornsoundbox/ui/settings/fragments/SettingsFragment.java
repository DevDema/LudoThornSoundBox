package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BasePrefencesFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.MvpView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.SettingsActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.credits.CreditsActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio.SettingsHiddenAudioActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.icons.SettingsIconActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems.LudoNavigationItem;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems.SettingsNavigationItemsActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.fragments.SettingsViewPresenterBinder.ISettingsView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.JsonUtil;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio.SettingsHiddenAudioActivity.REQUEST_HIDDEN_SELECTED;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio.SettingsHiddenAudioActivity.RESULT_HIDDEN_AUDIO_LIST;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.icons.SettingsIconActivity.CURRENT_POSITION;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.icons.SettingsIconActivity.EXTRA_ICON_SELECTED;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.icons.SettingsIconActivity.REQUEST_ICON_SELECTED;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems.SettingsNavigationItemsActivity.KEY_CURRENT_POSITION_BOT_NAV_MENU;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems.SettingsNavigationItemsActivity.KEY_FIRST_POSITION_BOT_NAV_MENU;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems.SettingsNavigationItemsActivity.KEY_NAVIGATION_ITEMS;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems.SettingsNavigationItemsActivity.KEY_SAVED_NAVIGATION_ITEMS;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems.SettingsNavigationItemsActivity.REQUEST_NAVIGATION_SELECTED;

public class SettingsFragment extends BasePrefencesFragment implements ISettingsView, Preference.OnPreferenceChangeListener, MvpView, BaseFragment.Callback, Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private @StringRes
    int[] mandatoryPreferences = {R.string.usa_font_app_key, R.string.reset_app_key};
    private int currentNavigationItemPosition;
    private int firstNavigationItemPosition;

    public static SettingsFragment newInstance(int currentPosition) {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();

        args.putInt(KEY_CURRENT_POSITION_BOT_NAV_MENU, currentPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentNavigationItemPosition = getArguments().getInt(KEY_CURRENT_POSITION_BOT_NAV_MENU);
            firstNavigationItemPosition = getFirstNavigationItemPosition();
        }

        bindPreferenceSummaryToValue(findPreference(getString(R.string.usa_font_app_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.carica_audio_insieme_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.dimensione_pulsanti_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.cambia_icona_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.cambia_ordine_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.audio_nascosti_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.reset_app_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.credits_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pag_iniziale_key)));

        setInitialFragmentData();

        android.preference.PreferenceManager.getDefaultSharedPreferences(mActivity).registerOnSharedPreferenceChangeListener(this);

    }

    private void setInitialFragmentData() {
        Preference preference = findPreference(getString(R.string.pag_iniziale_key));
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            List<CharSequence> newEntryValues = new ArrayList<>();
            List<LudoNavigationItem> navigationItems = JsonUtil.getGson().fromJson(
                    getPreferenceManager().getSharedPreferences().getString(getString(R.string.cambia_ordine_key), ""),
                    new TypeToken<List<LudoNavigationItem>>() {
                    }.getType()
            );

            if (navigationItems != null) {
                for (LudoNavigationItem navigationItem : navigationItems) {
                    if (navigationItem.getVisible()) {
                        newEntryValues.add(navigationItem.getName());

                    }
                }
            }

            if (!newEntryValues.isEmpty()) {
                listPreference.setEntryValues(newEntryValues.toArray(new CharSequence[0]));
                listPreference.setEntries(newEntryValues.toArray(new CharSequence[0]));
            }
        }
    }

    private int getFirstNavigationItemPosition() {
        return StringUtils.getActionIdByString(PreferenceManager.getDefaultSharedPreferences(mActivity)
                .getString(mActivity.getString(R.string.pag_iniziale_key), "Home"));
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        if (preference != null) {
            preference.setOnPreferenceChangeListener(this);
            preference.setOnPreferenceClickListener(this);
        }
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

    private void onMandatoryPreferenceChanged(String key) {
        CommonUtils.showDialog(mActivity,
                R.style.PreferenceActivityTheme_DialogTheme,
                getString(R.string.mandatory_setting_label),
                getString(R.string.mandatory_setting_summary_label), (dialog, which) -> ((SettingsActivity) mActivity).restartApp(),
                getString(R.string.no_label),
                true);
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

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        final String iconKey = getString(R.string.cambia_icona_key);
        final String ordineKey = getString(R.string.cambia_ordine_key);
        final String hiddenKey = getString(R.string.audio_nascosti_key);

        if (key.equals(iconKey)) {
            int currentPosition = getPreferenceManager().getSharedPreferences().getInt(iconKey, 0);

            Intent iconIntent = new Intent(mActivity, SettingsIconActivity.class);
            iconIntent.putExtra(CURRENT_POSITION, currentPosition);
            startActivityForResult(iconIntent, REQUEST_ICON_SELECTED);

            return true;
        } else if (key.equals(ordineKey)) {

            Intent ordineIntent = new Intent(mActivity, SettingsNavigationItemsActivity.class);
            ordineIntent.putExtra(KEY_SAVED_NAVIGATION_ITEMS, getPreferenceManager().getSharedPreferences().getString(ordineKey, ""));
            ordineIntent.putExtra(KEY_CURRENT_POSITION_BOT_NAV_MENU, currentNavigationItemPosition);
            ordineIntent.putExtra(KEY_FIRST_POSITION_BOT_NAV_MENU, firstNavigationItemPosition);
            startActivityForResult(ordineIntent, REQUEST_NAVIGATION_SELECTED);

            return true;
        } else if (key.equals(hiddenKey)) {
            Intent hiddenIntent = new Intent(mActivity, SettingsHiddenAudioActivity.class);

            startActivityForResult(hiddenIntent, REQUEST_HIDDEN_SELECTED);

            return true;
        } else if (key.equals(getString(R.string.reset_app_key))) {

            onPreferenceChange(preference, key);
            PreferenceManager.getDefaultSharedPreferences(mActivity).edit().clear().apply();

            return true;
        } else if (key.equals(getString(R.string.credits_key))) {
            onPreferenceChange(preference, key);
            Intent creditsIntent = new Intent(mActivity, CreditsActivity.class);

            startActivity(creditsIntent);
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            switch (requestCode) {
                case REQUEST_ICON_SELECTED:
                    Preference iconPreference = findPreference(getString(R.string.cambia_icona_key));
                    int position = (int) data.getExtras().get(EXTRA_ICON_SELECTED);
                    //save to preferences
                    getPreferenceManager().getSharedPreferences().edit().putInt(iconPreference.getKey(), position).apply();
                    AppUtils.changeIcon(mActivity, position);
                    break;
                case REQUEST_HIDDEN_SELECTED:
                    Preference hiddenPreference = findPreference(getString(R.string.audio_nascosti_key));

                    getPreferenceManager().getSharedPreferences().edit().putString(hiddenPreference.getKey(),
                            (String) data.getExtras().get(RESULT_HIDDEN_AUDIO_LIST)
                    ).apply();
                    break;
                case REQUEST_NAVIGATION_SELECTED:
                    Preference navigationPreference = findPreference(getString(R.string.cambia_ordine_key));

                    getPreferenceManager().getSharedPreferences().edit().putString(navigationPreference.getKey(),
                            (String) data.getExtras().get(KEY_NAVIGATION_ITEMS)
                    ).apply();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pag_iniziale_key))) {
            firstNavigationItemPosition = getFirstNavigationItemPosition();
        } else if (key.equals(getString(R.string.cambia_ordine_key))) {
            setInitialFragmentData();
        }
    }
}