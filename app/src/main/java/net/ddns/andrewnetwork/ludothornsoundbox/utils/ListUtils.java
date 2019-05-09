package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import java.util.List;
import java.util.Random;

public final class ListUtils {

    public static <T> T selectRandomItem(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static boolean isEmptyOrNull(List list) {
        return list == null || list.isEmpty();
    }
}
