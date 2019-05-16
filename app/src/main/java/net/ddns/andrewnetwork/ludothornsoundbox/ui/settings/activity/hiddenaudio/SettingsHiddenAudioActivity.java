package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ActivitySettingsHiddenAudioBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio.SettingsHiddenAudioViewPresenterBinder.ISettingsHiddenAudioPresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio.SettingsHiddenAudioViewPresenterBinder.ISettingsHiddenAudioView;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SettingsHiddenAudioActivity extends PreferencesManagerActivity implements ISettingsHiddenAudioView {

    public static final int REQUEST_HIDDEN_SELECTED = 2021;
    public static final String RESULT_HIDDEN_AUDIO_LIST = "RESULT_HIDDEN_AUDIO_LIST";
    public static final int RESULT_CODE_HIDDEN_AUDIO = 3033;
    private ActivitySettingsHiddenAudioBinding mBinding;
    private List<LudoAudio> hiddenAudioList;
    @Inject
    ISettingsHiddenAudioPresenter<ISettingsHiddenAudioView> mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }

        ActivityComponent activityComponent = getActivityComponent();
        if (activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        hiddenAudioList = getHiddenAudioList();

        mBinding.button.setOnClickListener(v -> {
            mPresenter.salvaListaAudio(hiddenAudioList);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(RESULT_HIDDEN_AUDIO_LIST, JsonUtil.getGson().toJson(hiddenAudioList));
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        if (!hiddenAudioList.isEmpty()) {
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mBinding.recyclerView.setAdapter(new HiddenAudioAdapter(this, hiddenAudioList));
            mBinding.recyclerView.setVisibility(View.VISIBLE);
            mBinding.noAudioLabel.setVisibility(View.GONE);
        } else {
            mBinding.recyclerView.setVisibility(View.GONE);
            mBinding.noAudioLabel.setVisibility(View.VISIBLE);
        }

        refreshButtonStatus();
    }

    private List<LudoAudio> getHiddenAudioList() {
        List<LudoAudio> hiddenAudioList = new ArrayList<>();
        List<LudoAudio> audioList = mPresenter.getAudioList();

        for (LudoAudio audio : audioList) {
            if (audio.isHidden()) {
                hiddenAudioList.add(audio);
            }
        }

        return hiddenAudioList;
    }

    @Override
    protected void setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings_hidden_audio);
    }

    @Override
    protected int getFragmentContainerView() {
        return 0;
    }

    void refreshButtonStatus() {
        HiddenAudioAdapter audioAdapter = (HiddenAudioAdapter) mBinding.recyclerView.getAdapter();
        if (audioAdapter != null) {
            mBinding.button.setEnabled(isAnyChecked(audioAdapter));
        } else {
            mBinding.button.setEnabled(false);
        }
    }

    private static boolean isAnyChecked(HiddenAudioAdapter audioAdapter) {
        boolean[] selectedArray = audioAdapter.getSelectedArray();

        for (boolean isSelected : selectedArray) {
            if (isSelected) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!hiddenAudioList.isEmpty()) {
            getMenuInflater().inflate(R.menu.menu_action_select_all, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select_all:
                selectAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectAll() {
        ((HiddenAudioAdapter) mBinding.recyclerView.getAdapter()).selectAll();
    }
}
