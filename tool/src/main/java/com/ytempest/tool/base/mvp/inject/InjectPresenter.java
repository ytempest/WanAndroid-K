package com.ytempest.tool.base.mvp.inject;

import com.ytempest.tool.base.mvp.IPresenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author heqidu
 * @since 2020/6/28
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectPresenter {
    Class<? extends IPresenter> value();
}
