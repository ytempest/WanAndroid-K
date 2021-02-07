package com.ytempest.wanandroid.activity.main.knowledge;

import com.ytempest.wanandroid.base.presenter.IPresenter;
import com.ytempest.wanandroid.base.view.IView;
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean;

import java.util.List;

/**
 * @author heqidu
 * @since 2020/6/30
 */
public interface IKnowledgeContract {
    interface View extends IView {
        void onKnowledgeArchitectureReceived(List<KnowledgeArchitectureBean> list);

        void onKnowledgeArchitectureFail(int code);
    }

    interface Presenter extends IPresenter {
        void getKnowledgeArchitecture();
    }
}
