package com.ytempest.wanandroid.activity.main.knowledge;

import androidx.annotation.NonNull;

import com.ytempest.wanandroid.base.presenter.BasePresenter;
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean;
import com.ytempest.wanandroid.http.observer.HandlerObserver;
import com.ytempest.wanandroid.interactor.impl.BaseInteractor;
import com.ytempest.wanandroid.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * @author heqidu
 * @since 2020/6/30
 */
public class KnowledgePresenter extends BasePresenter<IKnowledgeContract.View> implements IKnowledgeContract.Presenter {

    @Inject
    public KnowledgePresenter(BaseInteractor interactor) {
        super(interactor);
    }

    @Override
    public void getKnowledgeArchitecture() {
        mInteractor.getHttpHelper().getKnowledgeArchitecture()
                .compose(RxUtils.switchScheduler())
                .subscribe(new HandlerObserver<List<KnowledgeArchitectureBean>>(mView) {
                    @Override
                    protected void onSuccess(@NonNull List<KnowledgeArchitectureBean> list) {
                        mView.onKnowledgeArchitectureReceived(list);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        super.onFail(code, e);
                        mView.onKnowledgeArchitectureFail(code);
                    }
                });
    }
}
