package com.ytempest.dynamic_so

import com.google.gson.Gson
import com.ytempest.dynamic_so.FileUtil.readJson
import com.ytempest.dynamic_so.entity.SOItem
import com.ytempest.dynamic_so.entity.SOVersionList
import com.ytempest.dynamic_so.util.SLog
import java.io.File

/**
 * @author qiduhe
 * @since 2023/9/7
 */
object SOVersionHelper {
    private const val TAG = "SOVersionHelper"
    private const val FILE_NAME = "so_version_list.json"
    private var soVersionList = SOVersionList()
    private var configDir: File? = null

    fun load(configDir: File) {
        this.configDir = configDir;
        val soVerListFile = File(configDir, FILE_NAME)
        if (!FileUtil.isExist(soVerListFile)) {
            return
        }
        val json = readJson(soVerListFile)
        if (json.isEmpty()) {
            return
        }

        soVersionList = Gson().fromJson(json, SOVersionList::class.java)
        soVersionList.list?.forEach { it.backupSOInfo() }
    }

    fun saveToLocal() {
        val dir = configDir ?: return
        if (!FileUtil.isExist(dir)) {
            dir.mkdir()
        }

        val soVerListFile = File(configDir, FILE_NAME)
        if (soVersionList.list.isEmpty()) {
            return
        }

        val json = Gson().toJson(soVersionList)
        SLog.d(TAG, "saveToLocal json=$json")
        FileUtil.writeJson(json, soVerListFile)
    }

    fun isSOChanged(so: UploadSO?): Boolean {
        if (so == null) {
            return false
        }
        val soItem = getSOByName(so.soName)
        if (soItem == null) {
            return true
        }
        return when (so.cpuType) {
            Constants.ARM64 -> {
                soItem.arm64Hash != so.soHash
            }

            Constants.ARMEABI, Constants.ARMEABI_V7A -> {
                soItem.armHash != so.soHash
            }

            Constants.X86 -> {
                soItem.x86Hash != so.soHash
            }

            else -> {
                false
            }
        }
    }

    fun getEntity(): SOVersionList {
        return soVersionList
    }

    fun getSOByName(soName: String): SOItem? {
        return soVersionList.list.find { soName == it.name }
    }

    fun updateSOInfo(uploadResult: SOUploadResult) {
        var soItem = getSOByName(uploadResult.soName)
        if (soItem == null) {
            soItem = SOItem()
            soItem.version = 0
            soItem.name = uploadResult.soName
            soItem.backupSOInfo()
            soVersionList.list.add(soItem)
        }

        soItem.version = soItem.historyVersion + 1
        when (uploadResult.cpuType) {
            Constants.ARM64 -> {
                soItem.arm64Hash = uploadResult.getSODownloadHash()
            }

            Constants.ARMEABI, Constants.ARMEABI_V7A -> {
                soItem.armHash = uploadResult.getSODownloadHash()
            }

            Constants.X86 -> {
                soItem.x86Hash = uploadResult.getSODownloadHash()
            }

            else -> {
            }
        }

        soVersionList.lastUploadTime = System.currentTimeMillis()
    }
}