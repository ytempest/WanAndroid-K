package com.ytempest.tool.util.web;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ytempest.tool.util.SdkUtils;

/**
 * @author ytempest
 * @since 2020/12/21
 */
public class WebViewClientWrapper extends WebViewClient {

    private boolean isLoadErr;

    public boolean isLoadErr() {
        return isLoadErr;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        isLoadErr = false;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        isLoadErr = true;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        isLoadErr = true;
    }


    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        // 解决Url打开空白，即url未私密链接，或者由于SSL证书问题
        if (SdkUtils.OVER_LOLLIPOP) {
            handler.proceed();
        }
    }


}
