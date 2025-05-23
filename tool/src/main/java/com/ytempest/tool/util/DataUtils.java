package com.ytempest.tool.util;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author ytempest
 * @since 2020/2/27
 */
public class DataUtils {

    public static boolean isNull(Object... objects) {
        for (int i = 0, size = getSize(objects); i < size; i++) {
            if (objects[i] == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(Object[]... array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /*Get*/

    public static int getSize(Object[] objects) {
        return objects != null ? objects.length : 0;
    }

    public static int getSize(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }

    public static int getSize(int[] array) {
        return array != null ? array.length : 0;
    }

    public static int getSize(float[] array) {
        return array != null ? array.length : 0;
    }

    public static int getSize(long[] array) {
        return array != null ? array.length : 0;
    }

    @Nullable
    public static <T> T getFirst(List<T> list) {
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    @Nullable
    public static <T> T getFirst(@Nullable T[] array) {
        return array != null && array.length > 0 ? array[0] : null;
    }

    @Nullable
    public static <T> T getLast(List<T> list) {
        return (list != null && !list.isEmpty()) ? list.get(list.size() - 1) : null;
    }

    @Nullable
    public static <T> T getLast(@Nullable T[] array) {
        return array != null && array.length > 0 ? array[array.length - 1] : null;
    }

    @Nullable
    public static <T> T get(@Nullable List<T> list, int index) {
        if (0 <= index && index < getSize(list)) {
            return list.get(index);
        }
        return null;
    }

    @Nullable
    public static <T> T get(@Nullable T[] array, int index) {
        if (0 <= index && index < getSize(array)) {
            return array[index];
        }
        return null;
    }

    /*Filter*/

    @Nullable
    public static <Item> List<Item> filter(@Nullable List<Item> list, Filter<Item> filter) {
        List<Item> result = null;
        if (list != null) {
            for (Item item : list) {
                boolean accept = filter.accept(item);
                if (accept) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(item);
                }
            }
        }
        return result;
    }

    public static <Item> void filterIn(@Nullable List<Item> list, Filter<Item> filter) {
        if (list != null) {
            Iterator<Item> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!filter.accept(iterator.next())) {
                    iterator.remove();
                }
            }
        }
    }

    public interface Filter<Source> {
        boolean accept(Source source);
    }
}
