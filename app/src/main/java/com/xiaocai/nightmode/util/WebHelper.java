package com.xiaocai.nightmode.util;

import android.annotation.SuppressLint;
import android.webkit.WebView;

/**
 * Created by 肖坤 on 2017/9/15.
 * company：exsun
 * email：838494268@qq.com
 * <p>
 * 一个webview的帮助类
 */

public class WebHelper
{
    private WebView webView;

    public WebHelper(WebView webView)
    {
        this.webView = webView;
    }

    /**
     * 检测webView是否为空
     */
    private void checkNull()
    {
        if (webView == null)
        {
            throw new RuntimeException("必须首先调用WebHelper(WebView webView)构造方法");
        }
    }

    /**
     * 设置是否支持js
     *
     * @param b
     * @return
     */
    public WebHelper setJavaScriptEnabled(boolean b)
    {
        checkNull();
        webView.getSettings().setJavaScriptEnabled(b);
        return this;
    }

    /**
     * 设置webview是否从网络加载图片资源
     *
     * @param b
     * @return
     */
    public WebHelper setBlockNetworkImage(boolean b)
    {
        checkNull();
        webView.getSettings().setBlockNetworkImage(b);
        return this;
    }

    /**
     * 设置是否webview开启缓存
     *
     * @param b
     * @return
     */
    public WebHelper setAppCacheEnabled(boolean b)
    {
        checkNull();
        webView.getSettings().setAppCacheEnabled(b);
        return this;
    }

    @SuppressLint("JavascriptInterface")
    public WebHelper addJavascriptInterface(Object object, String className)
    {
        checkNull();
        webView.addJavascriptInterface(object, className);
        return this;
    }

    public WebView build()
    {
        checkNull();
        return this.webView;
    }
}
