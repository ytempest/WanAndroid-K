package com.ytempest.wanandroid.di.module.http.event

import com.ytempest.tool.util.LogUtils
import okhttp3.*
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.atomic.AtomicLong

/**
 * @author heqidu
 * @since 21-2-7
 */
class NetEventListener(
        val callId: Long,
        val url: String
) : EventListener() {

    private val TAG = NetEventListener::class.java.simpleName

    companion object FACTORY : Factory {
        private val nextCallId = AtomicLong(1L)

        override fun create(call: Call): NetEventListener {
            val callId = nextCallId.getAndIncrement()
            val url = call.request().url().toString()
            return NetEventListener(callId, url)
        }
    }

    private val mNetEvent: NetEvent = NetEvent()

    init {
        mNetEvent.callId = callId
        mNetEvent.url = url
    }

    override fun callStart(call: Call) {
        super.callStart(call)
        mNetEvent.callStartTime = System.currentTimeMillis()
    }

    override fun dnsStart(call: Call?, domainName: String?) {
        super.dnsStart(call, domainName)
        mNetEvent.dnsStartTime = System.currentTimeMillis()
    }

    override fun dnsEnd(call: Call?, domainName: String?, inetAddressList: List<InetAddress?>?) {
        super.dnsEnd(call, domainName, inetAddressList)
        mNetEvent.dnsEndTime = System.currentTimeMillis()
    }

    override fun connectStart(call: Call?, inetSocketAddress: InetSocketAddress?, proxy: Proxy?) {
        super.connectStart(call, inetSocketAddress, proxy)
        mNetEvent.connectStartTime = System.currentTimeMillis()
    }

    override fun secureConnectStart(call: Call?) {
        super.secureConnectStart(call)
        mNetEvent.secureConnectStart = System.currentTimeMillis()
    }

    override fun secureConnectEnd(call: Call?, handshake: Handshake?) {
        super.secureConnectEnd(call, handshake)
        mNetEvent.secureConnectEnd = System.currentTimeMillis()
    }

    override fun connectEnd(call: Call?, inetSocketAddress: InetSocketAddress?, proxy: Proxy?, protocol: Protocol?) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        mNetEvent.connectEndTime = System.currentTimeMillis()
    }

    override fun connectFailed(call: Call?, inetSocketAddress: InetSocketAddress?, proxy: Proxy?, protocol: Protocol?, ioe: IOException?) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
        mNetEvent.connectEndTime = System.currentTimeMillis()
    }

    override fun connectionAcquired(call: Call?, connection: Connection?) {
        super.connectionAcquired(call, connection)
    }

    override fun connectionReleased(call: Call?, connection: Connection?) {
        super.connectionReleased(call, connection)
    }

    override fun requestHeadersStart(call: Call?) {
        super.requestHeadersStart(call)
        mNetEvent.requestHeadersStart = System.currentTimeMillis()
    }

    override fun requestHeadersEnd(call: Call?, request: Request?) {
        super.requestHeadersEnd(call, request)
        mNetEvent.requestHeadersEnd = System.currentTimeMillis()
    }

    override fun requestBodyStart(call: Call?) {
        super.requestBodyStart(call)
        mNetEvent.requestBodyStart = System.currentTimeMillis()
    }

    override fun requestBodyEnd(call: Call?, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
        mNetEvent.requestBodyEnd = System.currentTimeMillis()
    }

    override fun responseHeadersStart(call: Call?) {
        super.responseHeadersStart(call)
        mNetEvent.responseHeadersStart = System.currentTimeMillis()
    }

    override fun responseHeadersEnd(call: Call?, response: Response?) {
        super.responseHeadersEnd(call, response)
        mNetEvent.responseHeadersEnd = System.currentTimeMillis()
    }

    override fun responseBodyStart(call: Call?) {
        super.responseBodyStart(call)
        mNetEvent.responseBodyStart = System.currentTimeMillis()
    }

    override fun responseBodyEnd(call: Call?, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        mNetEvent.responseBodyEnd = System.currentTimeMillis()
        mNetEvent.responseBodySize = byteCount
    }

    override fun callEnd(call: Call?) {
        super.callEnd(call)
        mNetEvent.callEndTime = System.currentTimeMillis()
        mNetEvent.requestSuccess = true
        LogUtils.d(TAG, mNetEvent.toString())
    }

    override fun callFailed(call: Call?, ioe: IOException) {
        super.callFailed(call, ioe)
        mNetEvent.callEndTime = System.currentTimeMillis()
        mNetEvent.errorReason = ioe.message
        LogUtils.d(TAG, mNetEvent.toString())
    }
}