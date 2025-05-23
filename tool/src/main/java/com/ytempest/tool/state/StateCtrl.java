package com.ytempest.tool.state;

import androidx.annotation.Nullable;

/**
 * @author heqidu
 * @since 2020/10/13
 */
@SuppressWarnings("unchecked")
public class StateCtrl<S extends StateCtrl.State> {

    private final InstanceCache<S> mCache;
    private StateChangedListener<S> mListener;
    private S mCur;

    public StateCtrl() {
        this(null);
    }

    public StateCtrl(@Nullable InstanceCache.Creator<S> creator) {
        mCache = new InstanceCache<>();
        mCache.setCreator(creator);
        mCache.setInitListener(state -> {
            state.setup(StateCtrl.this);
            state.onCreate();
        });
    }

    public void setOnStateChangedListener(StateChangedListener<S> listener) {
        mListener = listener;
    }

    public S getCurrent() {
        return mCur;
    }

    public <E extends S> void start(Class<E> clazz) {
        start(clazz, null);
    }

    public <E extends S> void start(Class<E> clazz, Object params) {
        if (mCur != null) {
            throw new IllegalStateException("Cant't start the state after is had working");
        }
        mCur = mCache.get(clazz);
        mCur.isStarted = true;
        mCur.onStart(params);

        if (mListener != null) {
            mListener.onStateChanged(null, mCur);
        }
    }

    public <E extends S> E moveTo(Class<E> nextClz) {
        return moveTo(nextClz, null);
    }

    public <E extends S> E moveTo(Class<E> nextClz, Object params) {
        if (mCur == null) {
            throw new IllegalStateException("Please start the state using method StateCtrl#start() before move state");
        }

        mCur.isStarted = false;
        mCur.nextState = nextClz;
        mCur.onStop();

        Class<S> preStateClz = (Class<S>) mCur.getClass();
        S preState = mCur;

        mCur = mCache.get(nextClz);
        mCur.isStarted = true;
        mCur.preState = preStateClz;
        mCur.nextState = null;
        mCur.onStart(params);

        if (mListener != null) {
            mListener.onStateChanged(preState, mCur);
        }

        return (E) mCur;
    }

    public void setFinish() {
        if (mCur != null) {
            mCur.isStarted = false;
            mCur.preState = null;
            mCur.onStop();
            if (mListener != null) {
                mListener.onStateChanged(mCur, null);
            }
            mCur = null;
        }
    }

    public static class State {

        protected StateCtrl mCtrl;
        Class<? extends State> preState;
        Class<? extends State> nextState;
        boolean isStarted;

        void setup(StateCtrl<? extends State> ctrl) {
            mCtrl = ctrl;
        }

        protected void onCreate() {
        }

        protected void onStart(Object params) {
        }

        protected void onStop() {
        }

        public Class<? extends State> getPreState() {
            return preState;
        }

        public Class<? extends State> getNextState() {
            return nextState;
        }

        public boolean isStarted() {
            return isStarted;
        }

        public <E extends State> E moveTo(Class<E> clazz) {
            return (E) mCtrl.moveTo(clazz);
        }

        public <E extends State> E moveTo(Class<E> clazz, Object params) {
            return (E) mCtrl.moveTo(clazz, params);
        }
    }

    public interface StateChangedListener<T> {
        void onStateChanged(@Nullable T perState, @Nullable T curState);
    }

}
