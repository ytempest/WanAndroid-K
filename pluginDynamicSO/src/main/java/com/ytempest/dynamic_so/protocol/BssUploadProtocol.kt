package com.ytempest.dynamic_so.protocol

import com.ytempest.dynamic_so.Config
import com.ytempest.dynamic_so.properties.UploadProperties
import com.ytempest.dynamic_so.util.Utils
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.QueryMap
import java.io.IOException

class BssUploadProtocol {
    private val TAG = "BssUploadProtocol"

    internal interface IBssUploadService {
        @POST("upload")
        fun upload(
            @QueryMap queryMap: Map<String, String>,
            @Body requestBody: RequestBody,
        ): Call<ResponseBody?>
    }

    fun uploadSync(
        data: ByteArray,
        bucket: String,
        extName: String?,
        filename: String?,
        authorization: String?,
    ): BssUploadResult {
        val simpleRequest = Retrofit.Builder()
            .baseUrl(Config.uploadBaseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val params: MutableMap<String, String> = HashMap()
        params["bucket"] = bucket
        params["extendname"] = extName ?: ""
        params["use_ext"] = "1"
        params["filename"] = ""
        params["type"] = ""
        params["authorization"] = authorization ?: ""
        params["userid"] = UploadProperties.userId
        params["token"] = UploadProperties.userToken
        params["body_empty"] = "1"
        Utils.addCommonSignature(params, "")
//        SLog.d(TAG, "params=$params")

        try {
            val response = simpleRequest.create(IBssUploadService::class.java)
                .upload(
                    params,
                    RequestBody.create(MediaType.parse("application/octet-stream"), data)
                )
                .execute()
            return body2Result(response.body())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return BssUploadResult()
    }

    private fun body2Result(responseBody: ResponseBody?): BssUploadResult {
        val entity = BssUploadResult()
        val respStr = responseBody?.string()
//        SLog.d(TAG, "body2Result=$respStr")
        if (respStr.isNullOrEmpty()) {
            return entity
        }
        try {
            val jsonObject = JSONObject(respStr)
            val status = jsonObject.optInt("status")
            val errorCode = jsonObject.optInt("error_code")
            if (status == 0) {
                entity.status = status
                entity.errorCode = errorCode
                return entity
            }
            val dataObject = jsonObject.optJSONObject("data")
            if (dataObject != null) {
                entity.bucket = dataObject.optString("x-bss-bucket")
                entity.filename = dataObject.optString("x-bss-filename")
                entity.hash = dataObject.optString("x-bss-hash")
                entity.tag = dataObject.optString("x-bss-Etag")
                entity.status = 1
            }
        } catch (e: Exception) {
            entity.status = 0
            entity.errorCode = 10
            e.printStackTrace()
        }
        return entity
    }


}