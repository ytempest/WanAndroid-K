package com.ytempest.tool.base.block;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;

import com.ytempest.tool.helper.ActivityLauncher;

/**
 * @author heqidu
 * @since 2020/6/21
 */
public class Block implements IBlock {

    private BlockActivity mHost;

    @Override
    public void onAttach(BlockActivity host) {
        this.mHost = host;
    }

    @Override
    public void onDetach() {
        mHost = null;
    }

    protected <T extends BlockActivity> T getHost() {
        return (T) mHost;
    }

    @NonNull
    protected <T> T getBlock(Class<T> clazz) {
        return (T) mHost.getBlock(clazz);
    }

    /*Proxy*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
    }

    /*Delegate*/

    protected void startActivity(Intent intent) {
        ActivityLauncher.startActivity(getHost(), intent);
    }

    protected void setContentView(@LayoutRes int layoutResID) {
        getHost().setContentView(layoutResID);
    }

    protected <V extends View> V findViewById(@IdRes int id) {
        return getHost().findViewById(id);
    }

    protected Window getWindow() {
        return mHost.getWindow();
    }

    protected Intent getIntent() {
        return mHost.getIntent();
    }

    protected Resources getResources() {
        return mHost.getResources();
    }
}
