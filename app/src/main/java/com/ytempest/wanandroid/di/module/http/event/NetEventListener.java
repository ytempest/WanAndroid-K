package com.ytempest.wanandroid.di.module.http.event;

import com.ytempest.tool.util.LogUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author heqidu
 * @since 2020/7/6
 */
public class NetEventListener extends EventListener {

    private static final String TAG = NetEventListener.class.getSimpleName();

    public static final EventListener.Factory FACTORY = new EventListener.Factory() {

        private final AtomicLong nextCallId = new AtomicLong(1L);

        @Override
        public NetEventListener create(Call call) {
            long callId = nextCallId.getAndIncrement();
            String url = call.request().url().toString();
            return new NetEventListener(callId, url);
        }
    };

    private NetEvent mNetEvent;

    public NetEventListener(long callId, String url) {
        super();
        mNetEvent = new NetEvent();
        mNetEvent.callId = callId;
        mNetEvent.url = url;
    }

    @Override
    public void callStart(Call call) {
        super.callStart(call);
        mNetEvent.callStartTime = System.currentTimeMillis();
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        super.dnsStart(call, domainName);
        mNetEvent.dnsStartTime = System.currentTimeMillis();
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        mNetEvent.dnsEndTime = System.currentTimeMillis();
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        mNetEvent.connectStartTime = System.currentTimeMillis();
    }

    @Override
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        mNetEvent.secureConnectStart = System.currentTimeMillis();
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        mNetEvent.secureConnectEnd = System.currentTimeMillis();
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        mNetEvent.connectEndTime = System.currentTimeMillis();
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        mNetEvent.connectEndTime = System.currentTimeMillis();
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
    }

    @Override
    public void connectionReleased(Call call, Connection connection) {
        super.connectionReleased(call, connection);
    }

    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        mNetEvent.requestHeadersStart = System.currentTimeMillis();
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        mNetEvent.requestHeadersEnd = System.currentTimeMillis();
    }

    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        mNetEvent.requestBodyStart = System.currentTimeMillis();
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        mNetEvent.requestBodyEnd = System.currentTimeMillis();
    }

    @Override
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        mNetEvent.responseHeadersStart = System.currentTimeMillis();
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        mNetEvent.responseHeadersEnd = System.currentTimeMillis();
    }

    @Override
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        mNetEvent.responseBodyStart = System.currentTimeMillis();
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        mNetEvent.responseBodyEnd = System.currentTimeMillis();
        mNetEvent.responseBodySize = byteCount;
    }

    @Override
    public void callEnd(Call call) {
        super.callEnd(call);
        mNetEvent.callEndTime = System.currentTimeMillis();
        mNetEvent.requestSuccess = true;
        LogUtils.d(TAG, mNetEvent.toString());
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        super.callFailed(call, ioe);
        mNetEvent.callEndTime = System.currentTimeMillis();
        mNetEvent.errorReason = ioe.getMessage();
        LogUtils.d(TAG, mNetEvent.toString());
    }
}
