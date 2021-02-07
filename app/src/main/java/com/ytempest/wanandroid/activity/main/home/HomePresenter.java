package com.ytempest.wanandroid.activity.main.home;

import androidx.annotation.NonNull;

import com.ytempest.tool.util.DataUtils;
import com.ytempest.wanandroid.base.presenter.BasePresenter;
import com.ytempest.wanandroid.http.bean.ArticleCollectBean;
import com.ytempest.wanandroid.http.bean.BannerBean;
import com.ytempest.wanandroid.http.bean.BaseResp;
import com.ytempest.wanandroid.http.bean.HomeArticleBean;
import com.ytempest.wanandroid.http.observer.HandlerObserver;
import com.ytempest.wanandroid.interactor.impl.BaseInteractor;
import com.ytempest.wanandroid.utils.PageCtrl;
import com.ytempest.wanandroid.utils.PageCtrl.State;
import com.ytempest.wanandroid.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author heqidu
 * @since 2020/6/30
 */
public class HomePresenter extends BasePresenter<IHomeContract.View> implements IHomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();
    private final PageCtrl mPageCtrl = new PageCtrl();

    @Inject
    public HomePresenter(BaseInteractor interactor) {
        super(interactor);
    }

    @Override
    public boolean isUserLogin() {
        return mInteractor.getConfigs().getUser().isUserLogin();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadHomeData() {
        if (mPageCtrl.isRequesting()) return;
        mPageCtrl.moveTo(State.REFRESH);
        Observable.merge(
                mInteractor.getHttpHelper().getBannerList(),
                mInteractor.getHttpHelper().getHomeArticleList(mPageCtrl.getNextPage())
        ).compose(RxUtils.switchScheduler())
                .subscribe(new HandlerObserver(mView) {
                    private List<BannerBean> mBannerList;
                    private HomeArticleBean mArticleBean;

                    @Override
                    protected void onSuccess(@NonNull Object data) {
                        if (data instanceof List) {
                            mBannerList = (List<BannerBean>) data;
                        } else if (data instanceof HomeArticleBean) {
                            mArticleBean = (HomeArticleBean) data;
                        }

                        if (!DataUtils.isNull(mBannerList, mArticleBean)) {
                            mPageCtrl.moveTo(State.SUCCESS);
                            mView.onHomeDataSuccess(mBannerList, mArticleBean);
                        }
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        mPageCtrl.moveTo(State.FAIL);
                        mView.onHomeDataFail(code);
                    }
                });
    }

    @Override
    public void refreshHomeArticle() {
        mPageCtrl.moveTo(State.REFRESH);
        mInteractor.getHttpHelper().getHomeArticleList(mPageCtrl.getNextPage())
                .compose(RxUtils.switchScheduler())
                .filter(mPageCtrl.filterDirtyData())
                .subscribe(new HandlerObserver<HomeArticleBean>(mView) {
                    @Override
                    protected void onSuccess(@NonNull HomeArticleBean homeArticleBean) {
                        mView.displayHomeArticle(true, homeArticleBean);
                        mPageCtrl.moveTo(PageCtrl.State.SUCCESS);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        super.onFail(code, e);
                        mPageCtrl.moveTo(PageCtrl.State.FAIL);
                    }
                });
    }

    @Override
    public void loadMoreHomeArticle() {
        if (mPageCtrl.isRequesting()) return;
        mPageCtrl.moveTo(PageCtrl.State.LOAD_MORE);
        mInteractor.getHttpHelper().getHomeArticleList(mPageCtrl.getNextPage())
                .compose(RxUtils.switchScheduler())
                .filter(mPageCtrl.filterDirtyData())
                .subscribe(new HandlerObserver<HomeArticleBean>(mView, HandlerObserver.SHOW_ERR_MSG) {
                    @Override
                    protected void onSuccess(@NonNull HomeArticleBean homeArticleBean) {
                        mView.displayHomeArticle(false, homeArticleBean);
                        mPageCtrl.moveTo(PageCtrl.State.SUCCESS);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        super.onFail(code, e);
                        mPageCtrl.moveTo(PageCtrl.State.FAIL);
                    }
                });
    }

    @Override
    public void updateArticleCollectStatus(HomeArticleBean.DatasBean data) {
        Observable<BaseResp<ArticleCollectBean>> collectObservable;
        boolean isCollect = !data.isCollect();
        if (isCollect) { // 收藏文章
            collectObservable = mInteractor.getHttpHelper().addCollectArticle(data.getId());
        } else { // 取消收藏
            collectObservable = mInteractor.getHttpHelper().cancelCollectArticle(data.getId());
        }

        collectObservable.compose(RxUtils.switchScheduler())
                .map(RxUtils.checkArticleCollectData())
                .subscribe(new HandlerObserver<ArticleCollectBean>(mView, HandlerObserver.SHOW_LOADING) {
                    @Override
                    protected void onSuccess(@NonNull ArticleCollectBean bean) {
                        data.setCollect(isCollect);
                        mView.onArticleCollectSuccess(data);
                    }

                    @Override
                    protected void onFail(int code, Throwable e) {
                        data.setCollect(isCollect);
                        mView.onArticleCollectFail(data, code);
                    }
                });
    }

}
