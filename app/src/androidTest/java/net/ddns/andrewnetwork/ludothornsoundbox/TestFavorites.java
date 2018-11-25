package net.ddns.andrewnetwork.ludothornsoundbox;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Toast;

import net.ddns.andrewnetwork.ludothornsoundbox.controller.FavoriteController;
import net.ddns.andrewnetwork.ludothornsoundbox.model.FavoriteAudio;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestFavorites {
    @Test
    public void useAppContext() throws JSONException {
        Context instrumentationCtx = InstrumentationRegistry.getContext();
        List<FavoriteAudio> audios = FavoriteController.loadFavorite(instrumentationCtx);

        for(FavoriteAudio audio : audios) {
            System.out.println(audio.getTitle());
        }
    }
}
