package com.ytempest.wanandroid.activity.architecture.content;

import androidx.annotation.NonNull;

import com.ytempest.wanandroid.base.presenter.BasePresenter;
import com.ytempest.wanandroid.http.bean.ArchitectureContentBean;
import com.ytempest.wanandroid.http.bean.ArticleCollectBean;
import com.ytempest.wanandroid.http.bean.BaseResp;
import com.ytempest.wanandroid.http.observer.HandlerObserver;
import com.ytempest.wanandroid.interactor.impl.BaseInteractor;
import com.ytempest.wanandroid.utils.PageCtrl;
import com.ytempest.wanandroid.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author heqidu
 * @since 2021/1/6
 */
public class ArchArticlePresenter extends BasePresenter<IArchArticleContract.View> implements IArchArticleContract.Presenter {

    private static final String TAG = ArchArticlePresenter.class.getSimpleName();

    private final PageCtrl mPageCtrl = new PageCtrl();

    @Inject
    public ArchArticlePresenter(BaseInteractor interactor) {
        super(interactor);
    }

    @Override
    public boolean isUserLogin() {
        return mInteractor.getConfigs().getUser().isUserLogin();
    }

    public void refreshArchitectureContent(int id) {
        if (mPageCtrl.isRequesting()) return;
        mPageCtrl.moveTo(PageCtrl.State.REFRESH);
        mInteractor.getHttpHelper().getArchitectureContent(id, mPageCtrl.getNextPage())
                .compose(RxUtils.switchScheduler())
                .subscribe(new HandlerObserver<ArchitectureContentBean>(mView) {
                    @Override
                    protected void onSuccess(@NonNull ArchitectureContentBean architectureContentBean) {
                        mPageCtrl.moveTo(PageCtrl.State.SUCCESS);
                        mView.onLoadArchitectureContent(architectureContentBean, true);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        super.onFail(code, e);
                        mPageCtrl.moveTo(PageCtrl.State.FAIL);
                        mView.onRefreshArchitectureFail(code);
                    }
                });
    }

    @Override
    public void loadMoreArchitectureContent(int id) {
        if (mPageCtrl.isRequesting()) return;
        mPageCtrl.moveTo(PageCtrl.State.LOAD_MORE);
        mInteractor.getHttpHelper().getArchitectureContent(id, mPageCtrl.getNextPage())
                .compose(RxUtils.switchScheduler())
                .subscribe(new HandlerObserver<ArchitectureContentBean>(mView) {
                    @Override
                    protected void onSuccess(@NonNull ArchitectureContentBean architectureContentBean) {
                        mPageCtrl.moveTo(PageCtrl.State.SUCCESS);
                        mView.onLoadArchitectureContent(architectureContentBean, false);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        super.onFail(code, e);
                        mPageCtrl.moveTo(PageCtrl.State.FAIL);
                    }
                });
    }

    @Override
    public void updateArticleCollectStatus(boolean isCollect, ArchitectureContentBean.DatasBean article) {
        Observable<BaseResp<ArticleCollectBean>> collectObservable;
        if (isCollect) { // 收藏文章
            collectObservable = mInteractor.getHttpHelper().addCollectArticle(article.getId());
        } else { // 取消收藏
            collectObservable = mInteractor.getHttpHelper().cancelCollectArticle(article.getId());
        }

        collectObservable.compose(RxUtils.switchScheduler())
                .map(RxUtils.checkArticleCollectData())
                .subscribe(new HandlerObserver<ArticleCollectBean>(mView, HandlerObserver.SHOW_LOADING) {
                    @Override
                    protected void onSuccess(@NonNull ArticleCollectBean bean) {
                        mView.onArchArticleCollectSuccess(isCollect, article);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        super.onFail(code, e);
                        mView.onArchArticleCollectFail(isCollect, article, code);
                    }
                });
    }
}
