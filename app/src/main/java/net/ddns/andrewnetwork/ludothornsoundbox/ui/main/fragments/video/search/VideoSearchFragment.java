package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.search;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.MainActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.child.ChildVideoFragment;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.child.VideoRecyclerAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.VideoUtils;

import java.util.ArrayList;
import java.util.List;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment.isLoadingMoreVideos;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.video.VideoFragment.setLoadingMoreVideos;

public class VideoSearchFragment extends ChildVideoFragment implements IVideoChildSearchView {

    private String searchString;
    private static final String KEY_SEARCH = "KEY_SEARCH";
    private List<LudoVideo> videoList = new ArrayList<>();

    public static VideoSearchFragment newInstance(String text) {

        Bundle args = new Bundle();

        VideoSearchFragment fragment = new VideoSearchFragment();
        args.putString(KEY_SEARCH, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(mActivity instanceof MainActivity) {
            this.parent = ((MainActivity) mActivity).getFragmentByClass(VideoFragment.class);

            mActivity.invalidateOptionsMenu();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            searchString = getArguments().getString(KEY_SEARCH);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(parent == null) {
            throw new IllegalArgumentException("onActivityCreated() ParentFragment is null.");
        }

        parent.refreshSearch(searchString);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mBinding.getRoot().setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));

        mBinding.masterLayout.setOnRefreshListener(() -> {
            initVideos();
            setRecyclerViewRefreshing(true);
            parent.refreshSearch(searchString);
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        initVideos();

        mBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingMoreVideos()) {
                        VideoFragment.MoreVideosLoadedListener moreChannelsLoadedListener = null;
                        VideoRecyclerAdapter adapter = (VideoRecyclerAdapter) recyclerView.getAdapter();
                        if (adapter != null) {
                            adapter.showLoading();
                            moreChannelsLoadedListener = videoList -> mActivity.runOnUiThread(adapter::hideLoading);
                        }


                        parent.getMoreVideos(searchString, VideoUtils.getMostRecentDate(videoList), moreChannelsLoadedListener);

                        setLoadingMoreVideos(true);
                    }
                }
            }
        });
    }

    public void initVideos() {
        this.videoList = new ArrayList<>();
        mBinding.recyclerView.setAdapter(new VideoRecyclerAdapter(mContext, getParent()));
    }

    @Override
    public void onSearchMoreVideosLoaded(List<LudoVideo> videoList) {
        this.videoList.addAll(videoList);

        if(mBinding.recyclerView.getAdapter() != null) {
            ((VideoRecyclerAdapter) mBinding.recyclerView.getAdapter()).addItems(videoList);
        }

        setRecyclerViewRefreshing(false);
    }
}
