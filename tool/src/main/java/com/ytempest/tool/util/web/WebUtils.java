package com.ytempest.tool.util.web;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ytempest.tool.util.SdkUtils;

/**
 * @author ytempest
 * @since 2020/12/21
 */
public class WebUtils {

    public static void initWebView(Context context, WebView webView) {
        webView.clearFocus();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setSupportZoom(true);
        settings.setAllowContentAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);// 隐藏缩放按钮
        settings.setUseWideViewPort(true);// 可任意比例缩放
        settings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        settings.setSavePassword(true);
        settings.setSaveFormData(true);// 保存表单数据
        settings.setJavaScriptEnabled(true);
        settings.setTextZoom(100);
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);
        if (SdkUtils.OVER_LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (SdkUtils.OVER_JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(true);
        }
        if (SdkUtils.OVER_JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(context.getCacheDir().getAbsolutePath());
        settings.setDatabaseEnabled(true);
        settings.setGeolocationDatabasePath(context.getDir("database", 0).getPath());
        settings.setGeolocationEnabled(true);
        //启用cookie
        enabledCookie(context, webView);
    }

    /**
     * 启用cookie
     */
    public static void enabledCookie(Context context, WebView web) {
        CookieManager instance = CookieManager.getInstance();
        instance.setAcceptCookie(true);
        if (SdkUtils.OVER_LOLLIPOP) {
            instance.setAcceptThirdPartyCookies(web, true);
        } else {
            CookieSyncManager.createInstance(context);
        }
    }

    public static void destroyWebView(WebView webView) {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
    }
}
