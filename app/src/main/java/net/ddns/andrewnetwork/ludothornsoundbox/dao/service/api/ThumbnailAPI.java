package net.ddns.andrewnetwork.ludothornsoundbox.dao.service.api;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import net.ddns.andrewnetwork.ludothornsoundbox.dao.service.ThumbnailAPIService;
import net.ddns.andrewnetwork.ludothornsoundbox.model.LudoVideo;
import net.ddns.andrewnetwork.ludothornsoundbox.model.Thumbnail;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ThumbnailAPI extends AsyncTask<Void, Void, Object>  {

    private ThumbnailAPIService caller;
    Bitmap result;
    LudoVideo video;

    public ThumbnailAPI(ThumbnailAPIService caller, LudoVideo video) throws MalformedURLException {
        this.caller = caller;
        this.video = video;
    }

    @Override
    protected Thumbnail doInBackground(Void... voids) {
        if(!isCancelled()) {
            Thumbnail thumbnail = video.getThumbnail();
            if (thumbnail != null) {
                InputStream input;
                try {
                    input = getConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                    return new Thumbnail(video.getThumbnail().getUrl());
                }
                if (input == null) return new Thumbnail(video.getThumbnail().getUrl());
                else return new Thumbnail(input, video.getThumbnail().getUrl());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        caller.onThumbnailLoaded(result);
    }

    private InputStream getConnection() throws IOException {

            URL url = new URL(video.getThumbnail().getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            InputStream input = null;
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK)
             input = connection.getInputStream();
            return input;
    }
}
