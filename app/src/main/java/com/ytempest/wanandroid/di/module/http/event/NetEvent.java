package com.ytempest.wanandroid.di.module.http.event;

import androidx.annotation.NonNull;

/**
 * @author heqidu
 * @since 2020/7/6
 */
public class NetEvent {
    public long callId;
    public String url;
    public long callStartTime;
    public long callEndTime;
    public long dnsStartTime;
    public long dnsEndTime;
    public long connectStartTime;
    public long connectEndTime;
    public long secureConnectStart;
    public long secureConnectEnd;

    public long requestHeadersStart;
    public long requestHeadersEnd;
    public long requestBodyStart;
    public long requestBodyEnd;

    public long responseBodyStart;
    public long responseBodyEnd;
    public long responseHeadersStart;
    public long responseHeadersEnd;
    public long responseBodySize;

    public boolean requestSuccess;
    public String errorReason;

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() +
                ":[" + "\n" +
                "callId: " + callId + ",\n" +
                "url: " + url + ",\n" +
                "callTime: " + (callEndTime - callStartTime) + ",\n" +
                "dnsTime: " + (dnsEndTime - dnsStartTime) + ",\n" +
                "connectTime: " + (connectEndTime - connectStartTime) + ",\n" +
                "secureTime: " + (secureConnectEnd - secureConnectStart) + ",\n" +
                "requestHeadersTime: " + (requestHeadersEnd - requestHeadersStart) + ",\t" +
                "requestBodyTime: " + (requestBodyEnd - requestBodyStart) + ",\n" +
                "responseHeadersTime: " + (responseHeadersEnd - responseHeadersStart) + ",\t" +
                "responseBodyTime: " + (responseBodyEnd - responseBodyStart) + ",\n" +
                "responseBodySize: " + responseBodySize + ",\n" +
                "requestResult: " + (requestSuccess ? "success" : "fail") + ",\n" +
                "errorReason: " + errorReason + ",];";
    }
}
