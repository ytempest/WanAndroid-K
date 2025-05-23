package com.ytempest.tool.state;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heqidu
 * @since 2020/10/13
 */
public class InstanceCache<T> {

    private final Map<Class<? extends T>, T> mCache = new HashMap<>();
    private Creator<T> mCreator;
    private InitListener<T> mInitListener;

    public void setCreator(Creator<T> creator) {
        this.mCreator = creator;
    }

    public void setInitListener(InitListener<T> listener) {
        this.mInitListener = listener;
    }

    public <E extends T> E get(Class<E> clazz) {
        T instance = mCache.get(clazz);
        if (instance == null) {
            instance = createAndCache(clazz);
        }
        return (E) instance;
    }

    private <E extends T> T createAndCache(Class<E> clazz) {
        T instance;
        if (mCreator != null) {
            instance = mCreator.create(clazz);
        } else {
            instance = createByReflect(clazz);
        }
        if (mInitListener != null) {
            mInitListener.onInit(instance);
        }
        mCache.put(clazz, instance);
        return instance;
    }

    private <E extends T> T createByReflect(Class<E> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public <E extends T> E remove(Class<? extends E> clazz) {
        return (E) mCache.remove(clazz);
    }

    public interface Creator<T> {
        @NonNull
        T create(Class<? extends T> clazz);
    }

    public interface InitListener<T> {
        void onInit(T t);
    }
}
