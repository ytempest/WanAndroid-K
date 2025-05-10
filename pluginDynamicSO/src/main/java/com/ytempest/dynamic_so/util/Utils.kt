package com.ytempest.dynamic_so.util

import com.ytempest.dynamic_so.properties.UploadProperties
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author qiduhe
 * @since 2023/9/6
 */
object Utils {
    fun addCommonSignature(params: MutableMap<String, String>, srcBodyStr: String?) {
        val bodyStr = srcBodyStr ?: ""
        //通用get参数

        params["dfid"] = "-"
        params["appid"] = UploadProperties.appId
        params["mid"] = UploadProperties.mid
        params["uuid"] = UploadProperties.uuid
        params["clientver"] = UploadProperties.appVer
        params["clienttime"] = (System.currentTimeMillis() / 1000).toString()
        val querySortString: String = map2SortString(params)
        params["signature"] = getSignature(querySortString + bodyStr)
    }


    fun map2SortString(map: Map<String, Any?>?): String {
        if (map == null || map.isEmpty()) {
            return ""
        }
        val keyList = ArrayList(map.keys)
        keyList.sort()
        val stringBuilder = StringBuilder()
        for (key in keyList) {
            if (key.isNullOrEmpty()) {
                continue
            }
            stringBuilder.append(key)
            stringBuilder.append("=")
            stringBuilder.append(map[key])
        }
        return stringBuilder.toString()
    }

    private fun getSignature(rawString: String): String {
        val appKey = UploadProperties.appKey
        return MD5Util.getMd5(appKey + rawString + appKey)
    }


    fun humpToUnderline(str: String?): String {
        //匹配 A-Z
        val compile: Pattern = Pattern.compile("[A-Z]")

        //进行匹配，结果存入 匹配器
        val matcher: Matcher = compile.matcher(str)
        val sb = StringBuffer()

        //如果匹配中存在
        while (matcher.find()) {
            //加入下换线，并且转为 小写。
            //如果是首字符，这里 应该直接转为小写。比如截取 字符串的第一个，判断是不是 [A-Z] 之间的
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase())
        }
        //添加到 sb中
        matcher.appendTail(sb)
        return if (sb.startsWith("_")) {
            sb.substring(1, sb.length).toString()
        } else {
            sb.toString()
        }
    }
}