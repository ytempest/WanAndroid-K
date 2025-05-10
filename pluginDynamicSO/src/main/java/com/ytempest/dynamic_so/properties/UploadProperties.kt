package com.ytempest.dynamic_so.properties

import java.io.File
import java.io.FileInputStream
import java.util.Properties

/**
 * @author qiduhe
 * @since 2023/9/6
 */
object UploadProperties {

    private var properties = Properties()

    val isInnerNet: Boolean
        get() = properties.getProperty("isInnerNet", "true") == "true"

    val userId: String
        get() = properties.getProperty("userId")

    val userToken: String
        get() = properties.getProperty("userToken")

    val mid: String
        get() = properties.getProperty("mid")

    val uuid: String
        get() = properties.getProperty("uuid")

    val appId: String
        get() = properties.getProperty("appId")

    val appKey: String
        get() = properties.getProperty("appKey")

    val appVer: String
        get() = properties.getProperty("appVer")


    fun load(configDir: File) {
        val uploadProFile = File(configDir, "so_upload.properties")
        if (!uploadProFile.exists()) {
            return
        }

        properties = Properties()
        properties.load(FileInputStream(uploadProFile))
    }


}