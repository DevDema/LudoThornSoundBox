package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.content.Context;

import net.ddns.andrewnetwork.ludothornsoundbox.utils.view.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpinnerUtils {

    private static List<String> createOrderList() {
        return Arrays.asList( "Nome", "Data");
    }

    public static BaseAdapter<String> createOrderAdapter(Context context) {
        return new BaseAdapter<>(context,
                android.R.layout.simple_list_item_1,
                createOrderList());
    }
}
