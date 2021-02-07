package com.ytempest.wanandroid.utils;

import android.annotation.SuppressLint;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;

/**
 * @author heqidu
 * @since 2020/12/18
 */
public class Utils {

    /**
     * 设置菜单栏显示ICON
     */
    @SuppressLint("RestrictedApi")
    public static void enableMenuShowIcon(Menu menu) {
        if (menu instanceof MenuBuilder) {
            try {
                MenuBuilder builder = (MenuBuilder) menu;
                builder.setOptionalIconsVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isArriveTop(RecyclerView recyclerView) {
        // RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        // RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        return !recyclerView.canScrollVertically(-1);
    }

    public static boolean isArriveBottom(RecyclerView recyclerView) {
        // RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        // RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        return !recyclerView.canScrollVertically(1);
    }
}
