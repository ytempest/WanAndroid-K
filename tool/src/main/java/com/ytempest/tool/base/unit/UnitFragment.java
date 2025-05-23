package com.ytempest.tool.base.unit;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytempest.tool.util.SdkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heqidu
 * @since 2020/6/24
 */
public abstract class UnitFragment extends Fragment {

    private final List<IUnit> mUnits = new ArrayList<>();
    private Map<Class<?>, IUnit> mUnitsCaches;

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void onCreateUnits(List<IUnit> units);

    public UnitFragment() {
        onCreateUnits(mUnits);
    }

    @NonNull
    <U> U getUnit(@NonNull Class<U> clazz) {
        if (mUnitsCaches != null && mUnitsCaches.containsKey(clazz)) {
            return (U) mUnitsCaches.get(clazz);
        }
        for (IUnit unit : mUnits) {
            if (clazz.isAssignableFrom(unit.getClass())) {
                if (mUnitsCaches == null) {
                    mUnitsCaches = SdkUtils.OVER_KITKAT ? new ArrayMap<>() : new HashMap<>();
                }
                mUnitsCaches.put(clazz, unit);
                return (U) unit;
            }
        }
        throw new IllegalArgumentException("Unit didn't exist, whether you had added this unit: " + clazz.getCanonicalName());
    }

    /*Dispatch*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        for (IUnit unit : mUnits) {
            unit.onAttach(this);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (IUnit unit : mUnits) {
            unit.onCreate(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutId(), container, false);
        for (IUnit unit : mUnits) {
            unit.onCreateView(view, savedInstanceState);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (IUnit unit : mUnits) {
            unit.onViewCreated(view, savedInstanceState);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        for (IUnit unit : mUnits) {
            unit.onHiddenChanged(hidden);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (IUnit unit : mUnits) {
            unit.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (IUnit unit : mUnits) {
            unit.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (IUnit unit : mUnits) {
            unit.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (IUnit unit : mUnits) {
            unit.onDestroy();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for (IUnit unit : mUnits) {
            unit.onDetach();
        }
    }

}
