package com.ytempest.wanandroid.activity.main.navigation;

import androidx.annotation.NonNull;

import com.ytempest.wanandroid.base.presenter.BasePresenter;
import com.ytempest.wanandroid.http.bean.NavigationListBean;
import com.ytempest.wanandroid.http.bean.OutsideArticleCollectBean;
import com.ytempest.wanandroid.http.observer.HandlerObserver;
import com.ytempest.wanandroid.interactor.impl.BaseInteractor;
import com.ytempest.wanandroid.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * @author heqidu
 * @since 2020/6/30
 */
public class NavaigationPresenter extends BasePresenter<INavigationContract.View> implements INavigationContract.Presenter {

    private static final String TAG = NavaigationPresenter.class.getSimpleName();

    @Inject
    public NavaigationPresenter(BaseInteractor interactor) {
        super(interactor);
    }

    @Override
    public void getNavigationList() {
        mInteractor.getHttpHelper().getNavigationList()
                .compose(RxUtils.switchScheduler())
                .subscribe(new HandlerObserver<List<NavigationListBean>>(mView) {
                    @Override
                    protected void onSuccess(@NonNull List<NavigationListBean> list) {
                        mView.displayNavigationList(list);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        super.onFail(code, e);
                        mView.onNavigationListFail(code);
                    }
                });
    }

    @Override
    public void addCollectOutsideArticle(NavigationListBean.ArticlesBean article) {
        mInteractor.getHttpHelper().addCollectOutsideArticle(article.getTitle(), article.getAuthor(), article.getLink())
                .compose(RxUtils.switchScheduler())
                .subscribe(new HandlerObserver<OutsideArticleCollectBean>(mView, HandlerObserver.SHOW_LOADING) {
                    @Override
                    protected void onSuccess(@NonNull OutsideArticleCollectBean outsideArticleCollectBean) {
                        mView.onNavigationArticleCollectSuccess(article);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        super.onFail(code, e);
                        // 在这个接口服务器返回-1表示已经收藏过了
                        boolean onceCollected = code == -1;
                        mView.onNavigationArticleCollectFail(code, onceCollected, article);
                    }
                });
    }
}
