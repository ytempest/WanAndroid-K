package com.ytempest.tool.base.mvp;



import com.ytempest.tool.base.mvp.inject.InjectModel;
import com.ytempest.tool.base.mvp.inject.InjectUtil;

import java.lang.reflect.Proxy;

/**
 * @author heqidu
 * @since 2020/6/28
 */
public class BasePresenter<View extends IView, Model extends IModel> implements IPresenter {

    private IContract mContract;
    private View mSrcView;
    protected View mView;
    protected Model mModel;

    public BasePresenter() {
        mModel = InjectUtil.findModel(getClass());
        if (mModel == null) {
            throw new IllegalArgumentException("Please inject model by " + InjectModel.class.getCanonicalName());
        }
    }

    @Override
    public <T extends IContract> void setContract(T contract) {
        mContract = contract;
        mModel.setContract(mContract);
    }

    @Override
    public <T extends IContract> T getContract() {
        return (T) mContract;
    }

    @Override
    public <V extends IView> void attachView(V view) {
        mSrcView = (View) view;
        mView = (View) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    if (mSrcView != null) {
                        return method.invoke(mSrcView, args);
                    }
                    return null;
                });
    }

    @Override
    public void detach() {
        mModel.detach();
        mModel = null;
        mSrcView = null;
    }
}
