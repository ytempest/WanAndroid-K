package com.ytempest.dynamic_so.entity

import com.google.gson.annotations.SerializedName

/**
 * @author qiduhe
 * @since 2023/9/7
 */
class SOItem {
    @SerializedName("version")
    var version: Int = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("arm64Hash")
    var arm64Hash: String = ""

    @SerializedName("armHash")
    var armHash: String = ""

    @SerializedName("x86Hash")
    var x86Hash: String = ""

    @Transient
    var historyVersion = 0

    fun backupSOInfo() {
        historyVersion = version
    }

}