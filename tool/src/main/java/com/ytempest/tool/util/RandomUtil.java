package com.ytempest.tool.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Random;

/**
 * @author heqidu
 * @since 2020/6/20
 */
public class RandomUtil {
    private static final Random random = new Random();
    private static long seed = System.currentTimeMillis();

    static {
        random.setSeed(seed);
    }

    public static void setSeed(long seed) {
        RandomUtil.seed = seed;
        random.setSeed(seed);
    }

    public static long getSeed() {
        return seed;
    }

    public static int nextInt() {
        return random.nextInt();
    }

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }

    /**
     * get random value in [min, max)
     */
    public static int nextInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max, min=" + min + " max=" + max);
        }
        return min + random.nextInt(max - min);
    }

    public static float nextFloat() {
        return random.nextFloat();
    }

    public static boolean nextBoolean() {
        return random.nextBoolean();
    }

    public static double nextDouble() {
        return random.nextDouble();
    }

    public static long nextLong() {
        return random.nextLong();
    }

    public static <Item> Item randomIn(@NonNull List<Item> list) {
        return list.get(nextInt(list.size()));
    }

    public static <Item> Item randomIn(@NonNull Item... items) {
        return items[nextInt(items.length)];
    }

    @Nullable
    public static Integer randomIn(int[] array) {
        return array == null || array.length == 0 ? null : array[nextInt(array.length)];
    }

    @Nullable
    public static Float randomIn(float[] array) {
        return array == null || array.length == 0 ? null : array[nextInt(array.length)];
    }

    @Nullable
    public static Long randomIn(long[] array) {
        return array == null || array.length == 0 ? null : array[nextInt(array.length)];
    }

    public static <Item> void shuffleIn(Item[] array) {
        if (array != null) {
            int len = array.length;
            for (int i = 0; i < len; i++) {
                int swapIdx = nextInt(i, len);
                Item temp = array[i];
                array[i] = array[swapIdx];
                array[swapIdx] = temp;
            }
        }
    }
}
