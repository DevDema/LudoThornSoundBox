package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.content.Context;

import net.ddns.andrewnetwork.ludothornsoundbox.ui.main.utils.model.ChiaveValore;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.view.BaseAdapter;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.view.StringParsingAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeFragment.AUDIO_DATA;
import static net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.HomeFragment.AUDIO_NOME;

public class SpinnerUtils {


    private static List<ChiaveValore<String>> createOrderList(int... keys) {
        List<ChiaveValore<String>> orderList = new ArrayList<>();
        List<String> stringArrayList = Arrays.asList( "Nome", "Data");

        if(keys.length != stringArrayList.size())
            throw new IllegalArgumentException();

        for(int i=0; i<stringArrayList.size();i++) {
            ChiaveValore<String> chiaveValore = new ChiaveValore<>(keys[i], stringArrayList.get(i));
            orderList.add(chiaveValore);
        }
        return orderList;
    }

    public static StringParsingAdapter<ChiaveValore<String>> createOrderAdapter(Context context) {
        return new StringParsingAdapter<>(context,
                android.R.layout.simple_list_item_1,
                createOrderList(AUDIO_NOME, AUDIO_DATA),
                ChiaveValore::getValore);
    }
}
