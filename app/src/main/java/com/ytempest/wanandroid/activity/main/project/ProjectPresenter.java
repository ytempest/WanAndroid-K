package com.ytempest.wanandroid.activity.main.project;

import androidx.annotation.NonNull;

import com.ytempest.wanandroid.base.presenter.BasePresenter;
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean;
import com.ytempest.wanandroid.http.observer.HandlerObserver;
import com.ytempest.wanandroid.interactor.impl.BaseInteractor;
import com.ytempest.wanandroid.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * @author heqidu
 * @since 2020/6/30
 */
public class ProjectPresenter extends BasePresenter<IProjectContract.View> implements IProjectContract.Presenter {

    @Inject
    public ProjectPresenter(BaseInteractor interactor) {
        super(interactor);
    }

    @Override
    public void getProjectClassify() {
        mInteractor.getHttpHelper().getProjectClassify()
                .compose(RxUtils.switchScheduler())
                .subscribe(new HandlerObserver<List<ProjectClassifyBean>>(mView) {
                    @Override
                    protected void onSuccess(@NonNull List<ProjectClassifyBean> list) {
                        mView.onProjectClassifyReceived(list);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        super.onFail(code, e);
                        mView.onProjectClassifyFail(code);
                    }
                });
    }
}
