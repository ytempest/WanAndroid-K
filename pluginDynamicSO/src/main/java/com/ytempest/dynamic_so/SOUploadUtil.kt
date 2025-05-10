package com.ytempest.dynamic_so

import com.ytempest.dynamic_so.protocol.BssAuthorizationEntity
import com.ytempest.dynamic_so.protocol.BssAuthorizationProtocol
import com.ytempest.dynamic_so.protocol.BssUploadProtocol
import com.ytempest.dynamic_so.protocol.BssUploadResult
import com.ytempest.dynamic_so.util.SLog
import java.io.File

/**
 * @author qiduhe
 * @since 2023/9/6
 */
object SOUploadUtil {

    private const val TAG = "SOUploadUtil"

    fun upload(so: File, cpuType: String): SOUploadResult {

        val soPath = so.absolutePath
        val result: Array<String?> = FileUtil.getMd5AndSuffix(so)
        val fileName = result[0] + result[1]
        val extName: String = soPath.substring(soPath.lastIndexOf(".") + 1)
        val soUploadResult = SOUploadResult(so, cpuType)

        val authorization = if (SLog.uploadDebug) {
            BssAuthorizationEntity().apply {
                status = 1
                authorization = "authorization"
            }
        } else {
            BssAuthorizationProtocol().getAuthorizationSync(
                Config.SO_BUCKET,
                Config.SLAT_SO_BUCKET,
                fileName,
                "",
                ""
            )
        }

        if (authorization == null
            || authorization.status == 0
            || authorization.authorization.isNullOrEmpty()
        ) {
            SLog.d(TAG, "authorization=$authorization")
            return soUploadResult
        }

        if (SLog.uploadDebug) {
            val uploadResult = BssUploadResult()
            uploadResult.status = 1
            uploadResult.hash = result[0]
            soUploadResult.uploadResult = uploadResult
            return soUploadResult
        }

        val byteData = FileUtil.readData(so.absolutePath)
        val uploadResult = BssUploadProtocol().uploadSync(
            byteData,
            Config.SO_BUCKET,
            extName,
            fileName,
            authorization.authorization
        )

        SLog.d(TAG, "uploadResult=$uploadResult")

        soUploadResult.uploadResult = uploadResult

        return soUploadResult
    }

}