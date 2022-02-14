package com.ytempest.wanandroid.activity.main.knowledge


import com.ytempest.framework.base.view.IView
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean

/**
 * @author heqidu
 * @since 21-2-9
 */
interface IKnowledgeView : IView {
    fun onKnowledgeArchitectureReceived(list: List<KnowledgeArchitectureBean>)
    fun onKnowledgeArchitectureFail(code: Int)
}