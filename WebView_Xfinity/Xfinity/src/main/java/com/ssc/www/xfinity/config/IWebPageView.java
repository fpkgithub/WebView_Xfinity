package com.ssc.www.xfinity.config;

/**
 * @author Boy
 *         created at 2017/12/4 15:09
 */
public interface IWebPageView
{

    // 隐藏进度条
    void hindProgressBar();

    // 显示webview
    void showWebView();

    // 隐藏webview
    void hindWebView();

    //  进度条先加载到90%,然后再加载到100%
    void startProgress();


    /**
     * 进度条变化时调用
     */
    void progressChanged( int newProgress );


}
