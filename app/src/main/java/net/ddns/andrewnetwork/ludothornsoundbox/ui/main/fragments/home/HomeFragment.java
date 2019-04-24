package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.FragmentHomeBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.di.component.ActivityComponent;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.GifFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomePresenter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeViewPresenterBinder.IHomeView;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.videoinfo.VideoInformationFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view.ButtonViewPagerAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view.OnButtonSelectedListener;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.utils.model.ChiaveValore;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AudioUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.PermissionListener;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.SpinnerUtils;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import static net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils.nonEmptyNonNull;

@SuppressWarnings("unchecked")
public class HomeFragment extends GifFragment implements OnButtonSelectedListener<LudoAudio>, IHomeView {

    public static final String KEY_LOAD_AT_ONCE = "KEY_LOAD_AT_ONCE";
    private FragmentHomeBinding mBinding;
    private List<LudoAudio> audioList;
    public static final int AUDIO_NOME = 1;
    public static final int AUDIO_DATA = 2;
    @Inject
    IHomePresenter<IHomeView> mPresenter;
    private boolean loadAtOnce;

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

        if (loadAtOnce) {
            mPresenter.getVideoInformationForAudios(audioList);
        }


        return mBinding.getRoot();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioList = AudioUtils.createAudioList(Objects.requireNonNull(mActivity));
        if (getArguments() != null) {
            loadAtOnce = getArguments().getBoolean(KEY_LOAD_AT_ONCE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!loadAtOnce) {
            new Handler().postDelayed(() -> onAudioListReceived(audioList), 1000);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onButtonSelected(LudoAudio audio, int position, Button button) {

        mBinding.audioPlayer.setAudio(audio);
        mBinding.audioPlayer.play();
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onButtonLongSelected(LudoAudio audio, int position, Button button) {
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
                            mActivity.newDialogFragment(VideoInformationFragment.newInstance(loadAtOnce, audio));
                        } else {
                            CommonUtils.showDialog(getContext(), "Video non disponibile per questo audio!");

                        }
                        break;
                    case R.id.condividi_audio:
                        shareAudio(audio);
                        break;
                    case R.id.nascondi_audio:
                        audio.setHidden(true);
                        mPresenter.salvaAudio(audio);
                        if (mBinding.buttonsAudioPager.getAdapter() != null) {
                            if (mBinding.searchstring.getText() != null) {
                                ((ButtonViewPagerAdapter) mBinding.buttonsAudioPager.getAdapter()).getFilter().filter(mBinding.searchstring.getText().toString());
                            }
                        }
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

    private void shareAudio(LudoAudio audio) {
        PermissionListener permissionListener = () -> AudioUtils.shareAudio(HomeFragment.this, audio);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
        mBinding.spinner.setAdapter(SpinnerUtils.createOrderAdapter(mContext));

        mBinding.reverse.setOnClickListener(view2 -> {
                    ButtonViewPagerAdapter<LudoAudio> adapter = (ButtonViewPagerAdapter<LudoAudio>) mBinding.buttonsAudioPager.getAdapter();

                    if (adapter == null)
                        return;

                    adapter.changeItemList(AudioUtils::reverse);
                    adapter.changeItemsAll(AudioUtils::reverse);

                    adapter.notifyDataSetChanged();
                }
        );

        ButtonViewPagerAdapter<LudoAudio> adapter = new ButtonViewPagerAdapter<>(mContext, audioList, LudoAudio::getTitle, mBinding.buttonsAudioPager, ludoaudio -> !ludoaudio.isHidden());

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCounter(adapter, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mBinding.buttonsAudioPager.addOnPageChangeListener(onPageChangeListener);

        adapter.setOnButtonSelectedListener(this);

        mBinding.buttonsAudioPager.setAdapter(adapter);

        mBinding.buttonRight.setOnClickListener(v -> mBinding.buttonsAudioPager.arrowScroll(View.FOCUS_RIGHT));

        mBinding.buttonLeft.setOnClickListener(v -> mBinding.buttonsAudioPager.arrowScroll(View.FOCUS_LEFT));

        mBinding.searchstring.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });

        mBinding.buttonsAudioPager.setOffscreenPageLimit(2);

        mBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ButtonViewPagerAdapter<LudoAudio> adapter = (ButtonViewPagerAdapter<LudoAudio>) mBinding.buttonsAudioPager.getAdapter();

                if (adapter != null) {
                    adapter.changeItemList(list -> AudioUtils.sortBy(list,
                            ((ChiaveValore<String>) parent.getSelectedItem()).getChiave()));
                    adapter.changeItemsAll(list -> AudioUtils.sortBy(list,
                            ((ChiaveValore<String>) parent.getSelectedItem()).getChiave()));

                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setCounter(adapter, 0);

    }

    private void setCounter(ButtonViewPagerAdapter adapter, int position) {
        mBinding.counter.setText(String.format(Locale.ITALIAN, "%d/%d", adapter.getMaxItemsPerPage() * position + 1, adapter.getUngroupedItems().size()));

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

        if (mBinding.buttonsAudioPager.getAdapter() != null) {
            mBinding.buttonsAudioPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ((ButtonViewPagerAdapter) mBinding.buttonsAudioPager.getAdapter()).notifyLayoutChanged();
                    mBinding.buttonsAudioPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }
}
