package com.ytempest.wanandroid.di.module.http.event

/**
 * @author heqidu
 * @since 21-2-7
 */
class NetEvent {
    var callId: Long = 0
    var url: String? = null
    var callStartTime: Long = 0
    var callEndTime: Long = 0
    var dnsStartTime: Long = 0
    var dnsEndTime: Long = 0
    var connectStartTime: Long = 0
    var connectEndTime: Long = 0
    var secureConnectStart: Long = 0
    var secureConnectEnd: Long = 0

    var requestHeadersStart: Long = 0
    var requestHeadersEnd: Long = 0
    var requestBodyStart: Long = 0
    var requestBodyEnd: Long = 0

    var responseBodyStart: Long = 0
    var responseBodyEnd: Long = 0
    var responseHeadersStart: Long = 0
    var responseHeadersEnd: Long = 0
    var responseBodySize: Long = 0

    var requestSuccess = false
    var errorReason: String? = null

    override fun toString(): String {
        return """
            ${javaClass.simpleName}:[
            callId: $callId,
            url: $url,
            callTime: ${callEndTime - callStartTime},
            dnsTime: ${dnsEndTime - dnsStartTime},
            connectTime: ${connectEndTime - connectStartTime},
            secureTime: ${secureConnectEnd - secureConnectStart},
            requestHeadersTime: ${requestHeadersEnd - requestHeadersStart},	requestBodyTime: ${requestBodyEnd - requestBodyStart},
            responseHeadersTime: ${responseHeadersEnd - responseHeadersStart},	responseBodyTime: ${responseBodyEnd - responseBodyStart},
            responseBodySize: $responseBodySize,
            requestResult: ${if (requestSuccess) "success" else "fail"},
            errorReason: $errorReason,];
            """.trimIndent()
    }
}