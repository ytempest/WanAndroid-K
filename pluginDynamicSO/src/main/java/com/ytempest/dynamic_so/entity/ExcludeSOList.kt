package com.ytempest.dynamic_so.entity

import com.google.gson.annotations.SerializedName

/**
 * @author qiduhe
 * @since 2023/9/6
 */
class ExcludeSOList {
    @JvmField
    @SerializedName("armeabi")
    var armeabiList: List<String>? = null

    @JvmField
    @SerializedName("arm64-v8a")
    var arm64List: List<String>? = null

    @JvmField
    @SerializedName("x86")
    var x86List: List<String>? = null

    override fun toString(): String {
        return "ExcludeSOList{" +
                "armeabiList=" + armeabiList +
                ", arm64List=" + arm64List +
                ", x86List=" + x86List +
                '}'
    }
}