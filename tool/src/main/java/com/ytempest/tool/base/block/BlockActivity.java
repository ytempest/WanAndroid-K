package com.ytempest.tool.base.block;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.ArrayMap;

import com.ytempest.tool.util.SdkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heqidu
 * @since 2020/6/20
 */
public abstract class BlockActivity extends AppCompatActivity {

    private final List<IBlock> mBlocks = new ArrayList<>();
    private Map<Class<?>, IBlock> mBlockCaches;

    @NonNull
    <T> T getBlock(@NonNull Class<T> clazz) {
        if (mBlockCaches != null && mBlockCaches.containsKey(clazz)) {
            return (T) mBlockCaches.get(clazz);
        }
        for (IBlock block : mBlocks) {
            if (clazz.isAssignableFrom(block.getClass())) {
                if (mBlockCaches == null) {
                    mBlockCaches = SdkUtils.OVER_KITKAT ? new ArrayMap<>() : new HashMap<>();
                }
                mBlockCaches.put(clazz, block);
                return (T) block;
            }
        }
        throw new IllegalArgumentException("Block didn't exist, whether you had added this block: " + clazz.getCanonicalName());
    }

    protected abstract void onCreateBlocks(List<IBlock> blocks);

    /*Dispatch*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (IBlock block : mBlocks) {
            block.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateBlocks(mBlocks);
        for (IBlock block : mBlocks) {
            block.onAttach(this);
        }

        for (IBlock block : mBlocks) {
            block.onCreate(savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (IBlock block : mBlocks) {
            block.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (IBlock block : mBlocks) {
            block.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (IBlock block : mBlocks) {
            block.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (IBlock block : mBlocks) {
            block.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (IBlock block : mBlocks) {
            block.onDestroy();
        }
        for (IBlock block : mBlocks) {
            block.onDetach();
        }
    }
}
