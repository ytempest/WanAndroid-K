package com.ytempest.dynamic_so.protocol

import com.ytempest.dynamic_so.Config
import com.ytempest.dynamic_so.properties.UploadProperties
import com.ytempest.dynamic_so.util.MD5Util
import com.ytempest.dynamic_so.util.Utils
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.io.IOException

class BssAuthorizationProtocol {

    internal interface IBssAuthorizationService {
        @GET("upload/auth")
        fun getAuthorizationSync(@QueryMap queryMap: Map<String, String>): Call<ResponseBody>
    }

    fun getAuthorizationSync(
        bucket: String,
        bucketSlat: String,
        filename: String,
        userId: String,
        token: String,
    ): BssAuthorizationEntity? {
        val simpleRequest = Retrofit.Builder()
            .baseUrl(Config.authBaseUrl)
            .build()

        val params: MutableMap<String, String> = HashMap()
        params["bucket"] = bucket
        params["buVerifyCode"] = MD5Util.getMd5(UploadProperties.appId + bucket + bucketSlat)
        params["filename"] = filename
        params["method"] = "POST"
        params["loginType"] = "0"
        params["userid"] = userId
        params["token"] = token
        params["appid"] = UploadProperties.appId
        params["version"] = UploadProperties.appVer
        params["extranet"] = if (UploadProperties.isInnerNet) "0" else "1"
        Utils.addCommonSignature(params, "")

        try {
            val response = simpleRequest
                .create(IBssAuthorizationService::class.java)
                .getAuthorizationSync(params)
                .execute()
            return body2AuthResult(response.body())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun body2AuthResult(responseBody: ResponseBody?): BssAuthorizationEntity {
        val entity = BssAuthorizationEntity()
        try {
            val respStr = responseBody?.string()
            if (respStr.isNullOrEmpty()) {
                return entity
            }
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
                val authorization = dataObject.optString("authorization")
                entity.status = 1
                entity.authorization = authorization
            }
        } catch (e: Exception) {
            entity.status = 0
            entity.errorCode = 10
            e.printStackTrace()
        }
        return entity
    }

}