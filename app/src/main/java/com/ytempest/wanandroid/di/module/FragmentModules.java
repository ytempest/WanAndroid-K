package com.ytempest.wanandroid.di.module;

import dagger.Module;

/**
 * @author heqidu
 * @since 2020/6/30
 */
public class FragmentModules {
    /**
     * 空映射关系表，如果类没有特定要进行注入的实例，那么可以直接使用该Module
     */
    @Module
    static class EmptyModule {
    }

}
