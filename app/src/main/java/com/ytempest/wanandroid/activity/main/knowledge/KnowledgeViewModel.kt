package com.ytempest.wanandroid.activity.main.knowledge

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ytempest.wanandroid.base.vm.BaseViewModel
import com.ytempest.wanandroid.http.bean.Entity
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean
import com.ytempest.wanandroid.http.bean.NegativeEntity
import com.ytempest.wanandroid.http.bean.PositiveEntity

/**
 * @author qiduhe
 * @since 2022/2/10
 */
class KnowledgeViewModel(application: Application) : BaseViewModel(application) {

    val knowledgeListResult = MutableLiveData<Entity<List<KnowledgeArchitectureBean>>>()

    fun loadKnowledgeArchitecture() {
        request(mInteractor.httpHelper.getKnowledgeArchitecture(),
            onSuccess = { list ->
                knowledgeListResult.value = PositiveEntity(list)
            },
            onFail = { code: Int, throwable: Throwable? ->
                knowledgeListResult.value = NegativeEntity(code, throwable)
            }
        )
    }


}