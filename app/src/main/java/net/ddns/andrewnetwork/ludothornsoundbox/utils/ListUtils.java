package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import android.util.Pair;

import net.ddns.andrewnetwork.ludothornsoundbox.BuildConfig;
import net.ddns.andrewnetwork.ludothornsoundbox.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class ListUtils {

    private static Set<Integer> alreadyIndexesArrays = new HashSet<>();
    private static final float RANDOM_PERCENTAGE_ADS = 0.08F;
    private static final float RANDOM_PERCENTAGE_POPUP_ADS = 0.25F;
    public static final String imageAssetpath = "advImages";

    public static <T> T selectRandomItem(List<T> list) {
        int randomIndex = new Random().nextInt(list.size());

        if(list.size() > alreadyIndexesArrays.size()) {
            if (alreadyIndexesArrays.contains(randomIndex)) {
                return selectRandomItem(list);
            }
        } else {
            alreadyIndexesArrays = new HashSet<>();
            return selectRandomItem(list);
        }

        alreadyIndexesArrays.add(randomIndex);

        return list.get(randomIndex);
    }

    public static <T> T selectRandomItem(T[] list) {
        int randomIndex = new Random().nextInt(list.length);

        if(list.length > alreadyIndexesArrays.size()) {
            if (alreadyIndexesArrays.contains(randomIndex)) {
                return selectRandomItem(list);
            }
        } else {
            alreadyIndexesArrays = new HashSet<>();
            return selectRandomItem(list);
        }

        alreadyIndexesArrays.add(randomIndex);

        return list[randomIndex];
    }


    public static boolean isEmptyOrNull(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean getRandomBoolean() {
        return Math.random() < RANDOM_PERCENTAGE_ADS;
    }

    public static boolean getRandomPopUpBoolean() {
        return Math.random() < RANDOM_PERCENTAGE_POPUP_ADS;
    }

    public static List<Pair<Integer, String>> getAdvertisementImages() {
        List<Pair<Integer, String>> drawableList = new ArrayList<>();

        if(BuildConfig.FLAVOR.equals("ludo")) {
            drawableList.add(new Pair<>(R.drawable.ic_adv_1, AppUtils.MERCHANDISE_LINK));
            drawableList.add(new Pair<>(R.drawable.ic_adv_2, AppUtils.PATREON_LINK));
        }

        return drawableList;
    }
}
