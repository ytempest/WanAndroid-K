package com.ytempest.dynamic_so.properties

import java.io.File
import java.io.FileInputStream
import java.util.Properties

/**
 * @author qiduhe
 * @since 2023/9/6
 */
object SOConfigProperties {

    const val PROPERTIES_FILE_NAME = "so_config.properties"
    const val SO_CLASS_NAME = "YSO"
    const val SO_CLASS_FULL_NAME = "$SO_CLASS_NAME.java"

    private var properties = Properties()

    val soClassPath: String
        get() = properties.getProperty("soClassPath")

    val soPackageName: String
        get() = properties.getProperty("soPackageName")

    fun load(configDir: File) {
        val uploadProFile = File(configDir, PROPERTIES_FILE_NAME)
        if (!uploadProFile.exists()) {
            return
        }

        properties = Properties()
        properties.load(FileInputStream(uploadProFile))
    }


}