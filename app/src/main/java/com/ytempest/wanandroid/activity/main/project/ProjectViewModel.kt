package com.ytempest.wanandroid.activity.main.project

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ytempest.wanandroid.base.vm.BaseViewModel
import com.ytempest.wanandroid.http.bean.Entity
import com.ytempest.wanandroid.http.bean.NegativeEntity
import com.ytempest.wanandroid.http.bean.PositiveEntity
import com.ytempest.wanandroid.http.bean.ProjectClassifyBean

/**
 * @author heqidu
 * @since 21-2-10
 */
class ProjectViewModel(application: Application) : BaseViewModel(application) {

    val projectClassifyResult = MutableLiveData<Entity<List<ProjectClassifyBean>>>()

    fun loadProjectClassify() {
        request(
            mInteractor.httpHelper.getProjectClassify(),
            onSuccess = { list ->
                projectClassifyResult.value = PositiveEntity(list)
            },
            onFail = { code, throwable ->
                projectClassifyResult.value = NegativeEntity(code, throwable)
            }
        )
    }
}