package com.ytempest.wanandroid.interactor.configs;

import com.ytempest.wanandroid.di.module.InteractorModule;
import com.ytempest.wanandroid.interactor.Configs;

import javax.inject.Inject;

/**
 * @author heqidu
 * @since 2020/6/28
 * 在{@link InteractorModule}中注入，该类实例化为单例
 */
public class ConfigsImpl implements Configs {
    @Inject
    public ConfigsImpl() {
    }

    @Override
    public UserConfig getUser() {
        return new UserConfig();
    }
}
