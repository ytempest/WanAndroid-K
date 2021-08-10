package com.ytempest.wanandroid.base.vm

import androidx.lifecycle.Observer
import com.ytempest.wanandroid.http.bean.Entity
import com.ytempest.wanandroid.http.bean.NegativeEntity
import com.ytempest.wanandroid.http.bean.PositiveEntity

/**
 * @author ytempest
 * @since 2021/8/10
 */
class EntityObserver<T>(
        private val onSuccess: (PositiveEntity<T>) -> Unit,
        private val onFail: ((NegativeEntity<T>) -> Unit)? = null,
        private val onComplete: (() -> Unit)? = null
) : Observer<Entity<T>> {

    override fun onChanged(entity: Entity<T>?) {
        if (entity == null) {
            return
        }

        if (entity is PositiveEntity<T>) {
            onSuccess.invoke(entity)
        }

        if (entity is NegativeEntity) {
            onFail?.invoke(entity)
        }

        onComplete?.invoke()
    }

}