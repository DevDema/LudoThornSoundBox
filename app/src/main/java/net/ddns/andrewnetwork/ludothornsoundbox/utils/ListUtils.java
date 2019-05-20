package net.ddns.andrewnetwork.ludothornsoundbox.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class ListUtils {

    private static Set<Integer> alreadyIndexesArrays = new HashSet<>();
    private static final float RANDOM_PERCENTAGE_ADS = 0.08F;
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

    public static boolean isEmptyOrNull(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean getRandomBoolean() {
        return Math.random() < RANDOM_PERCENTAGE_ADS;
    }
}
