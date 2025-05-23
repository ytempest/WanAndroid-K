package com.ytempest.tool.base.mvp.inject;

import com.ytempest.tool.base.mvp.IModel;
import com.ytempest.tool.base.mvp.IPresenter;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public class InjectUtil {

    public static <M extends IModel> M findModel(Class<?> target) {
        InjectModel injectModel = target.getAnnotation(InjectModel.class);
        if (injectModel != null) {
            Class<? extends IModel> modelClz = injectModel.value();
            if (modelClz.isInterface()) {
                throw new IllegalArgumentException("Inject model can't be interface");
            }

            try {
                return (M) modelClz.newInstance();
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Inject Model " + target.getCanonicalName() + " must not be private");

            } catch (InstantiationException e) {
                throw new IllegalStateException("Please provide a non params constructor for " + target.getCanonicalName());
            }
        }
        return null;
    }

    public static <P extends IPresenter> P findPresenter(Class<?> target) {
        InjectPresenter presenter = target.getAnnotation(InjectPresenter.class);
        if (presenter != null) {
            Class<? extends IPresenter> presenterClz = presenter.value();
            if (presenterClz.isInterface()) {
                throw new IllegalArgumentException("Inject presenter can't be interface");
            }

            try {
                return (P) presenterClz.newInstance();
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Inject presenter " + target.getCanonicalName() + " must not be private");

            } catch (InstantiationException e) {
                throw new IllegalStateException("Please provide a non params constructor for " + target.getCanonicalName());
            }
        }
        return null;
    }
}
