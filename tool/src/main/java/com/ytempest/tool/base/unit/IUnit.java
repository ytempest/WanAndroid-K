package com.ytempest.tool.base.unit;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

/**
 * @author heqidu
 * @since 2020/6/24
 */
public interface IUnit {
    void onAttach(UnitFragment frag);

    void onCreate(@Nullable Bundle savedInstanceState);

    void onCreateView(@NonNull View view, @Nullable Bundle savedInstanceState);

    void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState);

    void onHiddenChanged(boolean hidden);

    void onResume();

    void onPause();

    void onDestroyView();

    void onDestroy();

    void onDetach();
}
