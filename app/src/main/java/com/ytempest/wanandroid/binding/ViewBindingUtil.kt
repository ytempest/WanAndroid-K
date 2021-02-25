package com.ytempest.wanandroid.binding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * @author ytempest
 */

fun <VB : ViewBinding> Any.inflateViewBindingGeneric(inflater: LayoutInflater): VB =
    findViewBindingClass<VB>(this)
        .getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, inflater) as VB

fun <VB : ViewBinding> Any.inflateViewBindingGeneric(
    inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
): VB =
    findViewBindingClass<VB>(this)
        .getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        )
        .invoke(inflater, container, attachToRoot) as VB

private fun <VB : ViewBinding> findViewBindingClass(any: Any): Class<VB> {
    any.allParameterizedTypes.forEach { type ->
        type.actualTypeArguments.forEach {
            // check the generic type
            if (it is Class<*> && ViewBinding::class.java.isAssignableFrom(it)) {
                return it as Class<VB>
            }
        }
    }
    throw IllegalArgumentException("not fount Binding in $any")
}

private val Any.allParameterizedTypes: List<ParameterizedType>
    get() {
        val parameterizedTypes = mutableListOf<ParameterizedType>()
        var genericSuperclass = javaClass.genericSuperclass
        var superclass = javaClass.superclass
        while (superclass != null) {
            if (genericSuperclass is ParameterizedType) {
                parameterizedTypes.add(genericSuperclass)
            }
            genericSuperclass = superclass.genericSuperclass
            superclass = superclass.superclass
        }
        return parameterizedTypes
    }