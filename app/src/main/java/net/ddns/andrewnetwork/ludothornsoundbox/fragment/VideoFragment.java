package net.ddns.andrewnetwork.ludothornsoundbox.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.adapter.VideoAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.controller.VideoManager;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.model.ChannelResponse;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.model.VideoInformation;
import net.ddns.andrewnetwork.ludothornsoundbox.view.InteractiveScrollView;
import net.ddns.andrewnetwork.ludothornsoundbox.view.MainActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.view.model.VideoFragmentViewModel;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;

public class VideoFragment extends ParentFragment implements IVideoLoader {

    VideoFragmentViewModel videoFragmentViewModel;
    TextView noVideo;
    boolean done = false;
    ListView listview;
    InteractiveScrollView scrollView;
    ProgressBar mProgressBar;
    ProgressBar moreProgressBar;
    LinearLayout connectivityBlock;
    int executionNumber = 1;
    Button retryButton;
    Spinner dropdown;
    ImageButton refreshButton;
    InteractiveScrollView.OnBottomReachedListener mbottomListener= new InteractiveScrollView.OnBottomReachedListener() {
        @Override
        public void onBottomReached() {
            if(!videoFragmentViewModel.areAllVideosLoaded()) executionNumber++;
            moreProgressBar.setVisibility(View.VISIBLE);
            retryConnection(videoFragmentViewModel.findChannelByPosition(dropdown.getSelectedItemPosition()));
            scrollView.setOnBottomReachedListener(null);
            mProgressBar.setVisibility(View.GONE);
        }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        return inflater.inflate(R.layout.content_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoFragmentViewModel = new VideoFragmentViewModel(this);
        if(savedInstanceState!=null) {
            /*channelListFromActivity = (ArrayList<Channel>) savedInstanceState.getSerializable("channelList");
            if (channelListFromActivity != null)
                videoFragmentViewModel = new VideoFragmentViewModel(this, channelListFromActivity);
            else videoFragmentViewModel = new VideoFragmentViewModel(this);*/
            int executionNumber = savedInstanceState.getInt("executionNumber");
            if(executionNumber!=0) this.executionNumber =executionNumber;
        }
        refreshButton = view.findViewById(R.id.refreshButton);
        listview = view.findViewById(R.id.listview);
        noVideo = view.findViewById(R.id.noVideo);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        moreProgressBar = view.findViewById(R.id.more_progress_bar);
        connectivityBlock = view.findViewById(R.id.connectivityblock);
        scrollView = view.findViewById(R.id.videoScroll);
        dropdown = view.findViewById(R.id.selectChannel);
        retryButton = view.findViewById(R.id.retryConnection);
        createChannelList();
        if(!videoFragmentViewModel.areAllVideosLoaded()) scrollView.setOnBottomReachedListener(mbottomListener);

        if(videoFragmentViewModel.isVideoListEmpty()) {
            mProgressBar.setIndeterminate(true);
            mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
            moreProgressBar.setIndeterminate(true);
            moreProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
            retryButton.setOnClickListener((v) -> reset());
            ArrayList<Channel> savedList = null;
            if(savedInstanceState!=null) savedList = (ArrayList<Channel>) savedInstanceState.getSerializable("channelList");
            if(savedList == null) savedList = (ArrayList<Channel>) bundle.getSerializable("channelList");
            //videoFragmentViewModel.areAllVideosLoaded(savedInstanceState.getBoolean("areAllVideosLoaded"));
            if (savedList != null) {
                videoFragmentViewModel.setChannelList(savedList);
                try {
                    if(videoFragmentViewModel.loadThumbnailsFromMemory()) {
                        //videoFragmentViewModel.setCu(savedList);
                        setAdapter(false, null);
                        mProgressBar.setVisibility(View.GONE);
                        connectivityBlock.setVisibility(View.GONE);
                        if (videoFragmentViewModel.areAllVideosLoaded())
                            scrollView.setOnBottomReachedListener(null);
                        else scrollView.setOnBottomReachedListener(mbottomListener);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else retryConnection(null);
        }
       /*else if (channelListFromActivity!=null) {
            try {
            if(videoFragmentViewModel.loadThumbnailsFromMemory()) {
                connectivityBlock.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //videoFragmentViewModel.setVideoList(videoListfromActivity);
            setAdapter(false,null);
            mProgressBar.setVisibility(View.GONE);
            connectivityBlock.setVisibility(View.GONE);
            if (videoFragmentViewModel.areAllVideosLoaded())
                scrollView.setOnBottomReachedListener(null);
        }
        else retryConnection(null);*/
    }

    @Override
    public void onVideosLoaded()  {
        if (videoFragmentViewModel.getCompleteVideoList() != null && !videoFragmentViewModel.areAllVideosLoaded()) {
            try {
                showProgressBar();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onVideosLoaded(Channel channel) {
        if (channel.getVideoList() != null && !channel.areAllVideosLoaded()) {
            try {
                showProgressBar(channel);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showProgressBar(Channel channel) throws MalformedURLException {
        if (executionNumber != 1) moreProgressBar.setVisibility(View.VISIBLE);
        videoFragmentViewModel.loadVideoInformation(channel);
    }

    private void showProgressBar() throws MalformedURLException {
        if (executionNumber != 1) moreProgressBar.setVisibility(View.VISIBLE);
        videoFragmentViewModel.loadVideoInformation();
    }

    @Override
    public void onVideoInformationLoaded(VideoInformation videoInformation,LudoVideo video) {
        LudoVideo videoFound = VideoManager.findVideoById(videoFragmentViewModel.getCompleteVideoList(), video.getId());
        if(videoFound!=null) videoFound.setVideoInformation(videoInformation);
        //videoList=VideoManager.replaceVideo(videoList,video,videoFound);

    }

    @Override
    public void onChannelsLoaded(ChannelResponse channelResponse) {
        videoFragmentViewModel.onChannelsLoaded(channelResponse);
        //videoList=VideoManager.replaceVideo(videoList,video,videoFound);
    }


    public void setListViewHeightBasedOnChildren() {
        ListAdapter listAdapter = listview.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        View firstElement = listAdapter.getView(0, null, listview);
        firstElement.measure(0, 0);
        int totalHeight = firstElement.getMeasuredHeight();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listview);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) listview.getLayoutParams();
        params.height = totalHeight + (listview.getDividerHeight() * (listAdapter.getCount()))+margins.topMargin + firstElement.getMeasuredHeight();
        listview.setLayoutParams(params);
        listview.requestLayout();
    }

    @Override
    public void onThumbnailLoaded(Thumbnail thumbnail) {
        if(videoFragmentViewModel.setThumbnails(thumbnail,executionNumber) && !done){
            onAllThumbnailsLoaded();
        }
    }

    private void onAllThumbnailsLoaded() {
            setAdapter(true,null);
            mProgressBar.setVisibility(View.GONE);
            moreProgressBar.setVisibility(View.GONE);
            if(!videoFragmentViewModel.areAllVideosLoaded())
                scrollView.setOnBottomReachedListener(mbottomListener);
            done = true;
    }


    public void retryConnection(Channel channel) {
        boolean connect = videoFragmentViewModel.retryConnection(channel);

        if(connect) {
            mProgressBar.setVisibility(View.VISIBLE);
            connectivityBlock.setVisibility(View.GONE);
        }
        else if(executionNumber==1) {
            connectivityBlock.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
        if(executionNumber==1) scrollView.setVisibility(View.GONE);
    }

    private void setAdapter(boolean saveToMemory, ArrayList<LudoVideo> videoList) {
        //for(LudoVideo video : filteredVideoList) Log.v("Video", video.toString());
        VideoAdapter adapter = null;
        if(videoList==null) videoList = videoFragmentViewModel.getCompleteVideoList();
        if (getActivity() != null)
            adapter = new VideoAdapter(this, videoList, R.layout.object_video);
        if (listview != null && !videoFragmentViewModel.isVideoListEmpty() && adapter != null)
            listview.setAdapter(adapter);
        setListViewHeightBasedOnChildren();
        scrollView.setOnBottomReachedListener(mbottomListener);
        scrollView.setVisibility(View.VISIBLE);
        if (saveToMemory) {
            try {
                videoFragmentViewModel.saveToMemory(getContext());
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
    }

    @Override
    public int getExecutionNumber() {
        return executionNumber;
    }

    @Override
    public void setExecutionNumber(int number) {
        executionNumber = number;
    }

    public void reset() {
        done = false;
        setExecutionNumber(1);
        listview.setAdapter(null);
        videoFragmentViewModel.clearChannels();
        retryConnection(videoFragmentViewModel.findChannelByPosition(dropdown.getSelectedItemPosition()));
    }

    @Override
        public void onStop() {
          super.onStop();
          try {
              videoFragmentViewModel.cancelProcesses();
          }
          catch(ConcurrentModificationException e) {
              e.printStackTrace();
          }
    }

    @Override
    public void onPause() {
        super.onPause();
        videoFragmentViewModel.cancelProcesses();
        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).onVideoListReceived((ArrayList<Channel>) saveChannelList());
        mainActivity.OnExecutionNumberReceived(executionNumber);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(videoFragmentViewModel!=null) {
            try {
                videoFragmentViewModel.loadThumbnailsFromMemory();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("channelList", (ArrayList<Channel>) saveChannelList());
        savedInstanceState.putInt("executionNumber", executionNumber);
    }

    private List<Channel> saveChannelList() {
        List<Channel> channelList = new ArrayList<>();
        int selected = dropdown.getSelectedItemPosition();
        if(selected==0) {
            channelList = videoFragmentViewModel.getChannelList();
            for (Channel channel : channelList) VideoManager.removeAllThumbnails(channel.getVideoList());
        } else channelList.add(videoFragmentViewModel.findChannelByPosition(selected));
        return channelList;
    }

    private void createChannelList() {
        List<String> channelNames = new ArrayList<>();
        channelNames.add("Tutti i canali");
        for(Channel channel : getChannelList())
            channelNames.add(channel.getChannelName());
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_dropdown_item, channelNames);

        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<LudoVideo> videoList = null;
                if(position>0) videoList = videoFragmentViewModel.getVideoListFromChannelPosition(position);
                setAdapter(false, videoList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });
        refreshButton.setOnClickListener((v) -> reset());
    }

    @Override
    public List<Channel> getChannelList() {
        return videoFragmentViewModel.getChannelList();
    }
}
