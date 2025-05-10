package com.ytempest.dynamic_so.entity

import com.google.gson.annotations.SerializedName

/**
 * @author qiduhe
 * @since 2023/9/7
 */
class SOVersionList {
    @SerializedName("list")
    var list: MutableList<SOItem> = mutableListOf()

    @SerializedName("lastUploadTime")
    var lastUploadTime: Long = 0
}