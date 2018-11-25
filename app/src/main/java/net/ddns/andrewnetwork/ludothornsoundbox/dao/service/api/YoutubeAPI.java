package net.ddns.andrewnetwork.ludothornsoundbox.dao.service.api;

import android.os.AsyncTask;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

import java.io.IOException;

public abstract class YoutubeAPI extends AsyncTask<Void, Void, Object> {
    protected YouTube tubeService;
    private String LUDOTHORN="Ludo Thorn";
    String key="AIzaSyAulVzUGq5Ef0A9bfqGHJ6OQw2bDQg2Hx0";

    protected YoutubeAPI() {
        tubeService = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(LUDOTHORN).build();
    }
}
