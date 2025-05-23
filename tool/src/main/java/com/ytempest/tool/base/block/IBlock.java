package com.ytempest.tool.base.block;

import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * @author heqidu
 * @since 2020/6/21
 */
public interface IBlock {
    void onAttach(BlockActivity activity);

    void onDetach();

    void onSaveInstanceState(Bundle outState);

    void onCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
