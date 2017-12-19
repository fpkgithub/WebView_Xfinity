package com.ssc.www.xfinity.config;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ssc.www.xfinity.MainActivity;
import com.ssc.www.xfinity.utils.CheckNetwork;

/**
 * @author Boy
 *         created at 2017/12/4 15:05
 *         监听网页链接
 *         进度条的显示
 *         2017-12-12 pm  is ok
 */

public class MyWebViewClient extends WebViewClient
{

    private IWebPageView mIWebPageView;
    private MainActivity mActivity;
    private LinearLayout layoutEmpty;

    private Boolean flagPreError = false;

    public MyWebViewClient( IWebPageView mIWebPageView,LinearLayout layoutEmpty )
    {
        this.mIWebPageView = mIWebPageView;
        mActivity = (MainActivity)mIWebPageView;
        this.layoutEmpty = layoutEmpty;


    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading( WebView view,String url )
    {
        System.out.println( "shouldOverrideUrlLoading..." );
        if(TextUtils.isEmpty( url ))
        {
            return false;
        }
        view.loadUrl( url );
        return true;
    }


    @Override
    public void onReceivedError( WebView view,WebResourceRequest request,WebResourceError error )
    {
        System.out.println( "onReceivedError1..." );
        super.onReceivedError( view,request,error );
        System.out.println( "boy:error=" + error );
    }


    @Override
    public void onReceivedError( WebView view,int errorCode,String description,String failingUrl )
    {
        System.out.println( "onReceivedError2..." );
        super.onReceivedError( view,errorCode,description,failingUrl );
        System.out.println( "boy:errorCode=" + errorCode );
        if(errorCode == -2)
        {
            layoutEmpty.setVisibility( View.VISIBLE );
            Toast.makeText( mActivity,"网络不可用，请检查网络",Toast.LENGTH_SHORT ).show();
            flagPreError = true;
        }
    }

    //设置网页加载状态
    @Override
    public void onPageFinished( WebView view,String url )
    {
        System.out.println( "onPageFinished..." );
        if(mActivity.mProgress90)
        {
            mIWebPageView.hindProgressBar();
        }
        else
        {
            mActivity.mPageFinish = true;
        }
        if(!CheckNetwork.isNetworkConnected( mActivity ))
        {
            mIWebPageView.hindProgressBar();
        }
        flagPreError = false;
        super.onPageFinished( view,url );
    }

}
