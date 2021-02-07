package com.ytempest.wanandroid.activity.article;

import androidx.annotation.NonNull;

import com.ytempest.wanandroid.base.presenter.BasePresenter;
import com.ytempest.wanandroid.http.bean.ArticleCollectBean;
import com.ytempest.wanandroid.http.bean.BaseResp;
import com.ytempest.wanandroid.http.observer.HandlerObserver;
import com.ytempest.wanandroid.interactor.impl.BaseInteractor;
import com.ytempest.wanandroid.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author heqidu
 * @since 2020/12/15
 */
public class ArticleDetailPresenter extends BasePresenter<IArticleDetailContract.View> implements IArticleDetailContract.Presenter {

    private static final String TAG = "ArticleDetailPresenter";

    @Inject
    public ArticleDetailPresenter(BaseInteractor interactor) {
        super(interactor);
    }


    @Override
    public boolean isUserLogin() {
        return mInteractor.getConfigs().getUser().isUserLogin();
    }

    @Override
    public void updateArticleCollectStatus(boolean isCollect, long articleId) {
        Observable<BaseResp<ArticleCollectBean>> collectObservable;
        if (isCollect) { // 收藏文章
            collectObservable = mInteractor.getHttpHelper().addCollectArticle(articleId);
        } else { // 取消收藏
            collectObservable = mInteractor.getHttpHelper().cancelCollectArticle(articleId);
        }

        collectObservable.compose(RxUtils.switchScheduler())
                .map(RxUtils.checkArticleCollectData())
                .subscribe(new HandlerObserver<ArticleCollectBean>(mView, HandlerObserver.SHOW_LOADING) {
                    @Override
                    protected void onSuccess(@NonNull ArticleCollectBean bean) {
                        mView.onArticleCollectSuccess(isCollect, articleId);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        super.onFail(code, e);
                        mView.onArticleCollectFail(isCollect, articleId, code);
                    }
                });
    }
}
