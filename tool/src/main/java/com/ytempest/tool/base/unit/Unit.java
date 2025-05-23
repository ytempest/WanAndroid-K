package com.ytempest.tool.base.unit;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

/**
 * @author heqidu
 * @since 2020/6/24
 */
public class Unit implements IUnit {

    private UnitFragment mFrag;

    @Override
    public void onAttach(UnitFragment frag) {
        mFrag = frag;
    }

    /*Get*/

    @NonNull
    protected <U> U getUnit(Class<U> clazz) {
        return mFrag.getUnit(clazz);
    }

    public <Frag extends UnitFragment> Frag getFrag() {
        return (Frag) mFrag;
    }

    public Context getContext() {
        return mFrag.getContext();
    }

    public Resources getResources() {
        return mFrag.getResources();
    }

    /*Impl*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onCreateView(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroyView() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onDetach() {
        mFrag = null;
    }
}
