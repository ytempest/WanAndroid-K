package com.ytempest.wanandroid.http.bean

/**
 * @author ytempest
 * @since 2021/8/10
 */
sealed class Entity<T>

class PositiveEntity<T>(
        val data: T,
        val extra: Any? = null
) : Entity<T>()

class NegativeEntity<T>(
        val code: Int,
        val throwable: Throwable?
) : Entity<T>()
