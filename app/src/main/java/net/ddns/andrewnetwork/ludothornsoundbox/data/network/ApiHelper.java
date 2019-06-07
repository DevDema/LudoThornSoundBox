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

import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Channel;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Thumbnail;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.VideoInformation;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface ApiHelper {

    ApiHeader getApiHeader();

    Observable<List<LudoVideo>> getVideoList(Channel channel);

    Observable<Channel> getChannel(Channel channel);

    Single<Channel> getChannel(LudoVideo video);

    Observable<Thumbnail> getThumbnail(LudoVideo video);

    Observable<VideoInformation> getVideoInformation(LudoVideo video);

    Observable<List<LudoVideo>> getMoreVideos(Channel channel, Date beforeDate);

    Observable<LudoAudio> getVideoById(LudoAudio audio);

    Single<LudoVideo> getVideoById(String url);
}
