package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.FragmentHomeBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.ParentActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.MainFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomeView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view.ButtonViewPagerAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view.OnButtonSelectedListener;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.ListUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils.nonEmptyNonNull;

@SuppressWarnings("unchecked")
public class HomeFragment extends MainFragment implements OnButtonSelectedListener<LudoAudio>, IHomeView {

    public static final String KEY_LOAD_AT_ONCE = "KEY_LOAD_AT_ONCE";
    private FragmentHomeBinding mBinding;
    private List<LudoAudio> audioList;
    public static final int AUDIO_NOME = 1;
    public static final int AUDIO_DATA = 2;
    @Inject
    IHomePresenter<IHomeView> mPresenter;
    private boolean loadAtOnce;
    private static int pageSelected;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public static HomeFragment newInstance(boolean loadAtOnce) {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        args.putBoolean(KEY_LOAD_AT_ONCE, loadAtOnce);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        ActivityComponent activityComponent = getActivityComponent();

        if (activityComponent != null) {
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        audioList = AudioUtils.createAudioList(mActivity);

        if (loadAtOnce) {
            mPresenter.getVideoInformationForAudios(audioList);
        } else {
            AudioUtils.attachSameVideoToAudios(audioList);
        }

        return mBinding.getRoot();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            loadAtOnce = getArguments().getBoolean(KEY_LOAD_AT_ONCE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!loadAtOnce) {
            showLoading();
            new Handler().postDelayed(() -> onAudioListReceived(audioList), 1000);
        }

        mBinding.buttonsAudioProgressBar.getIndeterminateDrawable().setColorFilter(
                mContext.getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

        if(mActivity.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            configurePagerInitialHeight();
        }
    }

    @Override
    public void showLoading() {
        mBinding.buttonsAudioPager.setVisibility(View.INVISIBLE);
        mBinding.buttonsAudioProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mBinding.buttonsAudioProgressBar.setVisibility(View.INVISIBLE);
        mBinding.buttonsAudioPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onButtonSelected(LudoAudio audio, int position, View button) {
        if (mActivity instanceof ParentActivity && ListUtils.getRandomBoolean()) {
            ((ParentActivity) mActivity).showInterstitialAd();
        } else {
            mBinding.audioPlayer.play(audio);
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onButtonLongSelected(LudoAudio audio, int position, View button) {
        MenuBuilder menuBuilder = new MenuBuilder(mActivity);
        MenuInflater inflater = new MenuInflater(mActivity);
        MenuPopupHelper optionsMenu = new MenuPopupHelper(mActivity, menuBuilder, button);
        optionsMenu.setForceShowIcon(true);
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.preferiti:
                        mPresenter.salvaPreferito(audio);
                        break;
                    case R.id.video_collegato:
                        if (mActivity != null && audio.getVideo() != null && nonEmptyNonNull(audio.getVideo().getId())) {
                            mActivity.newDialogFragment(VideoInformationFragment.newInstance(loadAtOnce, audio, audio.getVideo().getConnectedAudioList()));
                        } else {
                            CommonUtils.showDialog(getContext(), "Video non disponibile per questo audio!");
                            Log.e("AudioMissing", "Missing id for " + audio.getTitle() + ": Video:" + audio.getVideo());
                        }
                        break;
                    case R.id.condividi_audio:
                        shareAudio(audio);
                        break;
                    case R.id.nascondi_audio:
                        audio.setHidden(true);
                        mPresenter.salvaAudio(audio);
                        if (mActivity instanceof MainActivity) {
                            if (mBinding.buttonsAudioPager.getAdapter() != null
                                    && ((MainActivity) mActivity).forceSearchViewListener()) {
                                setCounter(
                                        (ButtonViewPagerAdapter) mBinding.buttonsAudioPager.getAdapter(),
                                        mBinding.buttonsAudioPager.getCurrentItem()
                                );
                            }


                        }
                        break;
                    case R.id.suoneria_audio:
                        checkAndChangeRingtone(audio, RingtoneManager.TYPE_RINGTONE, R.string.suoneria_cambiata_label);
                        break;
                    case R.id.suoneria_notifica_audio:
                        checkAndChangeRingtone(audio, RingtoneManager.TYPE_ALARM, R.string.suoneria_notifiche_cambiata_label);
                        break;

                }
                return true;
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {

            }
        });

        inflater.inflate(R.menu.popup_menu_audio, menuBuilder);

        optionsMenu.show();

        return true;
    }

    private void checkAndChangeRingtone(LudoAudio audio, int typeNotification, @StringRes int stringResource) {
        PermissionListener permissionListener = () -> changeRingtone(audio, typeNotification, stringResource);

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            CommonUtils.showDialog(getContext(), getResources().getString(R.string.ask_permission_suoneria_label), (dialog, which) -> {
                        CommonUtils.askForStoragePermission(HomeFragment.this,
                                permissionListener
                        );
                        dialog.dismiss();
                    }
                    , true);
        } else {
            permissionListener.onPermissionGranted();
        }

    }

    private void changeRingtone(LudoAudio audio, int typeNotification, @StringRes int stringResource) {

        String messageNotification = getString(R.string.generic_error_suoneria_label);

        if (AppUtils.canWriteSettings(mActivity) && AudioUtils.setAsRingtone(mActivity, audio, typeNotification)) {
            messageNotification = getString(stringResource);
        }

        if (AppUtils.canWriteSettings(mActivity)) {
            CommonUtils.showDialog(mActivity, messageNotification);
        }
    }

    private void shareAudio(LudoAudio audio) {
        PermissionListener permissionListener = () -> AudioUtils.shareAudio(HomeFragment.this, audio);

        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            CommonUtils.showDialog(getContext(), getResources().getString(R.string.ask_permission_label), (dialog, which) ->
                            CommonUtils.askForStoragePermission(HomeFragment.this,
                                    permissionListener
                            )
                    , true);
        } else {
            permissionListener.onPermissionGranted();
        }
    }

    /*private void expandToolbar() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mBinding.appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.setTopAndBottomOffset(0);
            behavior.onNestedPreScroll(mBinding.coordinatorRoot, mBinding.appBarLayout, null, 0, 1, new int[2]);
        }
    }*/

    @Override
    public void onAudioListReceived(List<LudoAudio> audioList) {
        configAudioList(audioList);
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).onHomeFragmentReady();
        }
    }

    private void configAudioList(List<LudoAudio> audioList) {

        configureAdapter(audioList);

        mBinding.buttonRight.setOnClickListener(v -> mBinding.buttonsAudioPager.arrowScroll(View.FOCUS_RIGHT));

        mBinding.buttonLeft.setOnClickListener(v -> mBinding.buttonsAudioPager.arrowScroll(View.FOCUS_LEFT));


    }

    private ButtonViewPagerAdapter<LudoAudio> configureAdapter(List<LudoAudio> audioList) {
        ButtonViewPagerAdapter<LudoAudio> adapter = new ButtonViewPagerAdapter<>(mContext, audioList, LudoAudio::getTitle, mBinding.buttonsAudioPager, ludoaudio -> !ludoaudio.isHidden());
        onPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pageSelected = position;
                setCounter(adapter, position);

                changePageSwitchers(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mBinding.buttonsAudioPager.addOnPageChangeListener(onPageChangeListener);
        adapter.setOnButtonSelectedListener(this);
        adapter.setOnFilteredResults(list -> {
            changePageSwitchers(mBinding.buttonsAudioPager.getCurrentItem());
            setCounter(adapter, mBinding.buttonsAudioPager.getCurrentItem());
        });
        adapter.getFilter().filter("");
        mBinding.buttonsAudioPager.setAdapter(adapter);

        onPageChangeListener.onPageSelected(0);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mBinding.buttonsAudioLayout.getLayoutParams();
        layoutParams.matchConstraintMinHeight = mBinding.buttonsAudioLayout.getHeight();

        hideLoading();

        return adapter;
    }

    private void configurePagerInitialHeight() {
        if(mBinding.buttonsAudioLayout.getHeight() <= 0) {
            mBinding.buttonsAudioLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (mBinding.buttonsAudioLayout.getHeight() > 0) {
                        setPagerInitialHeight();
                        mBinding.buttonsAudioLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        } else {
            setPagerInitialHeight();
        }
    }

    private void setPagerInitialHeight() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mBinding.buttonsAudioLayout.getLayoutParams();
        layoutParams.matchConstraintMaxHeight = mBinding.buttonsAudioLayout.getHeight();
    }

    private void changePageSwitchers(int position) {
        ButtonViewPagerAdapter<LudoAudio> adapter = (ButtonViewPagerAdapter<LudoAudio>) mBinding.buttonsAudioPager.getAdapter();

        if (adapter != null) {
            if (adapter.getList().isEmpty() || adapter.getList().size() == 1) {
                onNoPageSelected();
            } else {

                onRegularPageSelected();

                if (position == 0) {
                    onFirstPageSelected();
                } else if (position == adapter.getList().size() - 1) {
                    onLastPageSelected();
                }
            }
        }
    }

    private void onNoPageSelected() {
        mBinding.buttonLeft.setVisibility(View.INVISIBLE);
        mBinding.buttonRight.setVisibility(View.INVISIBLE);
    }

    private void onRegularPageSelected() {
        mBinding.buttonLeft.setVisibility(View.VISIBLE);
        mBinding.buttonRight.setVisibility(View.VISIBLE);
    }

    private void onFirstPageSelected() {
        mBinding.buttonLeft.setVisibility(View.INVISIBLE);
    }

    private void onLastPageSelected() {
        mBinding.buttonRight.setVisibility(View.INVISIBLE);
    }

    private void setCounter(ButtonViewPagerAdapter adapter, int position) {
        int maxSize = adapter.getUngroupedItems().size();
        int currentItem = adapter.getMaxItemsPerPage() * position + 1;

        if (maxSize == 0) {
            currentItem = 0;
        }

        mBinding.counter.setText(String.format(Locale.ITALIAN, "%d/%d", currentItem, maxSize));

    }

    @Override
    public void onPreferitoEsistente(LudoAudio audio) {
        CommonUtils.showDialog(mActivity, getString(R.string.audio_esistente_label));
    }

    @Override
    public void onPreferitoSalvataggioSuccess() {
        CommonUtils.showDialog(mActivity, getString(R.string.salvato_correttamente_audio_label));

    }

    @Override
    public void onPreferitoSalvataggioFailed(String messaggio) {

        if (messaggio == null || messaggio.isEmpty()) {
            messaggio = getString(R.string.salvato_fallito_audio_label);
        }

        CommonUtils.showDialog(mActivity, messaggio);

    }

    @Override
    public void onMaxAudioReached() {
        CommonUtils.showDialog(mActivity, mActivity.getString(R.string.max_audio_reached_label));
    }

    @Override
    public void onVideoInformationNotLoaded() {
        CommonUtils.showDialog(mActivity, "Si è verificato un errore nel caricamento delle informazioni degli audio.");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mBinding.buttonsAudioPager.removeOnPageChangeListener(onPageChangeListener);
        showLoading();

        new Handler().postDelayed(() -> {

            configureAdapter(audioList);

            mBinding.buttonsAudioPager.setCurrentItem(pageSelected);
        }, 1000);
    }


    public void onAudioListChanged() {
        this.audioList = mPresenter.getAudioListFromPreferences();

        onAudioListReceived(audioList);
    }

    public void searchAudioByTitle(String title) {
        ButtonViewPagerAdapter<LudoAudio> adapter = (ButtonViewPagerAdapter<LudoAudio>) mBinding.buttonsAudioPager.getAdapter();

        if (adapter != null) {
            adapter.getFilter().filter(title);
        }
    }

    public boolean invertOrder() {

        ButtonViewPagerAdapter<LudoAudio> adapter = (ButtonViewPagerAdapter<LudoAudio>) mBinding.buttonsAudioPager.getAdapter();

        if (adapter != null) {
            adapter.changeItemList(AudioUtils::reverse);
            adapter.changeItemsAll(AudioUtils::reverse);

            adapter.notifyDataSetChanged();

            return true;
        }

        return false;
    }

    public boolean orderBy(int orderType) {
        ButtonViewPagerAdapter<LudoAudio> adapter = (ButtonViewPagerAdapter<LudoAudio>) mBinding.buttonsAudioPager.getAdapter();

        if (adapter != null) {
            adapter.changeItemList(list -> AudioUtils.sortBy(list,
                    orderType));
            adapter.changeItemsAll(list -> AudioUtils.sortBy(list,
                    orderType));

            adapter.notifyDataSetChanged();

            return true;
        }

        return false;
    }
}
