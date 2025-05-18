package com.ytempest.dynamic_so

import com.google.gson.Gson
import com.ytempest.dynamic_so.FileUtil.isExist
import com.ytempest.dynamic_so.FileUtil.readJson
import com.ytempest.dynamic_so.SOUploadUtil.upload
import com.ytempest.dynamic_so.entity.ExcludeSOList
import com.ytempest.dynamic_so.properties.SOConfigProperties
import com.ytempest.dynamic_so.properties.UploadProperties
import com.ytempest.dynamic_so.util.SLog
import org.gradle.api.Project
import java.io.File

/**
 * @author qiduhe
 * @since 2023/9/6
 */
class SOCollectTask(
    private val mProject: Project,
    private val mSoLibDir: File,
    private val mPluginModuleDir: File,
    private val mConfigDir: File,
) {

    companion object {
        private const val TAG = "SOCollectTask"
        private const val EXCLUDE_SO_FILE = "excludeSOList.json"
    }

    private val mCacheDir: File = File(mPluginModuleDir, "cache")
    private val mExcludeSOList = parseExcludeSOList(File(mConfigDir, EXCLUDE_SO_FILE))

    init {
        SOVersionHelper.load(mConfigDir)

        SLog.d(TAG, "excludeSOList:$mExcludeSOList")
    }


    private fun parseExcludeSOList(file: File): ExcludeSOList {
        val json = readJson(file)
        return Gson().fromJson(json, ExcludeSOList::class.java)
    }

    fun start() {
        val soLibDir = mSoLibDir
        if (!isExist(soLibDir)) {
            SLog.d(TAG, "not exist so list")
            return
        }
        val cpuDirList = soLibDir.listFiles() ?: return
        val srcSOList: MutableList<UploadSO> = ArrayList()
        for (cpuDir in cpuDirList) {
            val dirName = cpuDir.name
            if (Constants.ARMEABI == dirName) {
                srcSOList.addAll(
                    pickSOFromLib(
                        cpuDir,
                        Constants.ARMEABI,
                        mExcludeSOList.armeabiList
                    )
                )
            } else if (Constants.ARMEABI_V7A == dirName) {
                srcSOList.addAll(
                    pickSOFromLib(
                        cpuDir,
                        Constants.ARMEABI_V7A,
                        mExcludeSOList.armeabiList
                    )
                )
            } else if (Constants.ARM64 == dirName) {
                srcSOList.addAll(pickSOFromLib(cpuDir, Constants.ARM64, mExcludeSOList.arm64List))
            } else if (Constants.X86 == dirName) {
                srcSOList.addAll(pickSOFromLib(cpuDir, Constants.X86, mExcludeSOList.x86List))
            }
        }
        val iterator = srcSOList.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (!SOVersionHelper.isSOChanged(next)) {
                SLog.d(TAG, "remove no change so:" + next.soName)
//                iterator.remove()
            }
        }
        SLog.d(TAG, "need upload so count: " + srcSOList.size)
        if (srcSOList.isEmpty()) {
            return
        }
        // 加载配置
        UploadProperties.load(mConfigDir)
        SOConfigProperties.load(mConfigDir)

        val failList: MutableList<SOUploadResult> = ArrayList()
        val successList: MutableList<SOUploadResult> = ArrayList()
        for (so in srcSOList) {
            val result = upload(so.soFile, so.cpuType)
            if (result.isSuccess()) {
                successList.add(result)
            } else {
                failList.add(result)
            }
            for (uploadResult in successList) {
                SOVersionHelper.updateSOInfo(uploadResult)
                deleteSOFromApk(uploadResult.soFile)
            }
            SLog.d(TAG, "upload success: " + so.soName)
        }
        SOVersionHelper.saveToLocal()

        SOClassGenerator(mProject.projectDir)
            .generateClass(SOVersionHelper.getEntity())
    }

    private fun deleteSOFromApk(soFile: File) {
        if (SLog.uploadDebug) {
            // ignore delete
        } else {
            FileUtil.delete(soFile)
        }
    }

    private fun pickSOFromLib(
        dir: File,
        cpuType: String,
        excludeList: List<String>?,
    ): List<UploadSO> {
        val result: MutableList<UploadSO> = ArrayList()
        val soList = dir.listFiles()
        if (soList != null && excludeList != null) {
            for (soFile in soList) {
                if (soFile != null && excludeList.contains(soFile.name)) {
                    result.add(UploadSO(cpuType, soFile))
                }
            }
        }
        return result
    }


}