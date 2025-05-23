package com.ytempest.tool.state;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author heqidu
 * @since 2020/10/15
 */
public class LifeStateCtrl<T extends StateCtrl.State> extends StateCtrl<T> {

    private final MutableLiveData<StateUnit<T>> mStateTransform = new MutableLiveData<>();
    private final Observer<StateUnit<T>> mStateUnitObserver = stateUnit -> {
        Class<T> state = (Class<T>) stateUnit.state;
        LifeStateCtrl.super.moveTo(state, stateUnit.params);
    };

    public LifeStateCtrl(@NonNull LifecycleOwner owner) {
        this(owner, null);
    }

    public LifeStateCtrl(@NonNull LifecycleOwner owner, @Nullable InstanceCache.Creator<T> creator) {
        super(creator);
        mStateTransform.observe(owner, mStateUnitObserver);
    }

    @Override
    public <E extends T> E moveTo(Class<E> nextClz, Object params) {
        mStateTransform.postValue(new StateUnit<>(nextClz, params));
        return null;
    }

    @Nullable
    public Class<T> getPendingState() {
        StateUnit stateUnit = mStateTransform.getValue();
        return stateUnit != null ? stateUnit.state : null;
    }

    public void detachLife() {
        mStateTransform.removeObserver(mStateUnitObserver);
    }

    private static final class StateUnit<T> {

        Class<? extends T> state;
        Object params;

        StateUnit(Class<? extends T> state, Object params) {
            this.state = state;
            this.params = params;
        }
    }
}
