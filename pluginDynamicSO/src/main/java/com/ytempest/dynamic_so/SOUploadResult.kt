package com.ytempest.dynamic_so

import com.ytempest.dynamic_so.protocol.BssUploadResult
import java.io.File

/**
 * @author qiduhe
 * @since 2023/9/6
 */
class SOUploadResult(
    var soFile: File,
    var cpuType: String,
) {

    val fileName = soFile.name
    val soName = FileUtil.getSOSimpleName(soFile)

    var uploadResult: BssUploadResult? = null

    fun isSuccess(): Boolean {
        val result = uploadResult
        return result != null && result.status == 1 && !result.hash.isNullOrEmpty()
    }

    fun getSODownloadHash(): String {
        return uploadResult?.hash ?: ""
    }
}