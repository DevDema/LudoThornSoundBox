/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package net.ddns.andrewnetwork.ludothornsoundbox.data.network;

import android.util.Log;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.VideoInformation;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.JsonUtil;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.VideoUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

import static net.ddns.andrewnetwork.ludothornsoundbox.utils.AppConstants.LOOKUP_TYPE_VIDEO;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.AppConstants.LUDO_THORN_KEY;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.AppConstants.LUDO_THORN_NAME;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.AppConstants.ORDER_TYPE_VIDEO;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils.nonEmptyNonNull;
import static net.ddns.andrewnetwork.ludothornsoundbox.utils.VideoUtils.castToLudoVideo;


@Singleton
public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;

    @Inject
    AppApiHelper(ApiHeader apiHeader) {
        mApiHeader = apiHeader;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }

    @Override
    public Observable<List<LudoVideo>> getVideoList(Channel channel) {
        return Observable.create(emitter -> {
            Log.v("ChannelREST", "getting Videos.");

            YouTube.Search.List search;
            search = createTubeService().search().list("id,snippet");
            search.setKey(LUDO_THORN_KEY);
            search.setChannelId(channel.getId());
            search.setType(LOOKUP_TYPE_VIDEO);
            search.setOrder(ORDER_TYPE_VIDEO);
            search.setMaxResults(10L);
            final SearchListResponse searchResponse = search.execute();

            List<SearchResult> searchResultList = new ArrayList<>(searchResponse.getItems());
            List<LudoVideo> videoList = castToLudoVideo(searchResultList);
            VideoUtils.addVideosToChannels(channel, videoList);
            emitter.onNext(videoList);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Channel> getChannel(Channel channel) {
        return Observable.create(emitter -> {
            Log.v("ChannelREST", "getting Channel.");

            YouTube.Channels.List channels;
            channels = createTubeService().channels().list("statistics");
            channels.setKey(LUDO_THORN_KEY);
            if (nonEmptyNonNull(channel.getChannelUsername()))
                channels.setForUsername(channel.getChannelUsername());
            else if (nonEmptyNonNull(channel.getId()))
                channels.setId(channel.getId());
            final com.google.api.services.youtube.model.Channel channelListResponse = channels.execute().getItems().get(0);
            channel.setId(channelListResponse.getId());
            channel.setTotalNumberOfVideos(channelListResponse.getStatistics().getVideoCount().longValue());
            emitter.onNext(channel);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Thumbnail> getThumbnail(LudoVideo video) {
        return Observable.create(emitter -> {
            Log.v("ChannelREST", "getting Thumbnails.");

            Thumbnail thumbnail = video.getThumbnail();
            if (thumbnail != null) {
                InputStream input = null;
                URL url = new URL(video.getThumbnail().getUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK)
                    input = connection.getInputStream();
                if (input == null) {
                    thumbnail = new Thumbnail(video.getThumbnail().getUrl());

                } else thumbnail = new Thumbnail(input, video.getThumbnail().getUrl());
            }

            video.setThumbnail(thumbnail);
            emitter.onNext(thumbnail);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<VideoInformation> getVideoInformation(LudoVideo video) {
        return Observable.create(emitter -> {
            Log.v("VideoInfoREST", "getting VideoInformation.");
            List<Video> videos;

            YouTube.Videos.List videoSearch;

            videoSearch = createTubeService().videos().list("id,snippet,statistics");
            videoSearch.setKey(LUDO_THORN_KEY);
            videoSearch.setId(video.getId());
            final VideoListResponse videoListResponse = videoSearch.execute();
            videos = videoListResponse.getItems();

            if (!videos.isEmpty() && videos.size() == 1) {
                VideoInformation videoInformation = VideoUtils.extractVideoInformation(videos.get(0));
                video.setVideoInformation(videoInformation);

                emitter.onNext(videoInformation);
            } else emitter.onError(new IllegalArgumentException("Video results are not 1."));

            emitter.onComplete();
        });
    }

    @Override
    public Observable<List<LudoVideo>> getMoreVideos(Channel channel, Date beforeDate) {
        return Observable.create(emitter -> {
            Log.v("ChannelREST", "getting More Videos.");

            YouTube.Search.List search;
            search = createTubeService().search().list("id,snippet");
            search.setKey(LUDO_THORN_KEY);
            if (channel != null) {
                search.setChannelId(channel.getId());
            }
            search.setPublishedBefore(new DateTime(beforeDate));
            search.setType(LOOKUP_TYPE_VIDEO);
            search.setOrder(ORDER_TYPE_VIDEO);
            search.setMaxResults(10L);
            final SearchListResponse searchResponse = search.execute();

            List<SearchResult> searchResultList = new ArrayList<>(searchResponse.getItems());
            List<LudoVideo> videoList = castToLudoVideo(searchResultList);
            VideoUtils.addVideosToChannels(channel, videoList);
            emitter.onNext(videoList);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<LudoAudio> getVideoById(LudoAudio audio) {
        return Observable.create(emitter -> {
            Log.v("VideoFromAudioREST", "getting Video for audio: " + audio.getTitle());

            YouTube.Videos.List search;
            search = createTubeService().videos().list("id,snippet,statistics");
            search.setKey(LUDO_THORN_KEY);
            search.setId(audio.getVideo().getId());
            search.setMaxResults(1L);
            final VideoListResponse videoListResponse = search.execute();


            List<Video> searchResultList = new ArrayList<>(videoListResponse.getItems());
            if(searchResultList.isEmpty()) {
                Throwable throwable = new IllegalArgumentException("No Videos were found for audio " + JsonUtil.getGson().toJson(audio));
                throwable.printStackTrace();
                emitter.onNext(audio);
            } else {
                LudoVideo video = castToLudoVideo(searchResultList.get(0));

                audio.setVideo(video);

                emitter.onNext(audio);

            }

            emitter.onComplete();
        });
    }

    private YouTube createTubeService() {
        return new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
        }).setApplicationName(LUDO_THORN_NAME).build();
    }
}

