package com.ssc.www.xfinity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ssc.www.xfinity.config.IWebPageView;
import com.ssc.www.xfinity.config.MyWebViewClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Boy
 *         created at 2017/12/4 15:09
 */
public class MainActivity extends AppCompatActivity implements IWebPageView
{

    // 进度条
    @BindView(R.id.pb_progress)
    ProgressBar mProgressBar;
    //webView
    @BindView(R.id.webview_detail)
    WebView webView;
    @BindView(R.id.layout_empty)
    LinearLayout layoutEmpty;
    // 进度条是否加载到90%
    public boolean mProgress90;
    // 网页是否加载完成
    public boolean mPageFinish;
    // 网页链接
    private String mUrl = "http://192.168.120.219:8080/";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        //去掉title
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView( R.layout.activity_main );

        ButterKnife.bind( this );
        //setTitle( "标题" );
        initWebView();
        webView.loadUrl( mUrl );
    }

    private void initWebView()
    {
        mProgressBar.setVisibility( View.VISIBLE );
        //WebView的子类，对WebView进行配置和管理
        WebSettings ws = webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode( false );
        // 保存表单数据
        ws.setSaveFormData( true );
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom( true );
        ws.setBuiltInZoomControls( true );
        ws.setDisplayZoomControls( false );
        // 启动应用缓存
        ws.setAppCacheEnabled( true );
        // 设置缓存模式
        ws.setCacheMode( WebSettings.LOAD_DEFAULT );
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort( true );
        // 缩放比例
        //webView.setInitialScale(1);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled( true );
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage( false );
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled( true );
        // 排版适应屏幕
        ws.setLayoutAlgorithm( WebSettings.LayoutAlgorithm.NARROW_COLUMNS );
        // WebView是否支持多个窗口。
        ws.setSupportMultipleWindows( true );
        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ws.setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }
        // 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)
        ws.setTextZoom( 100 );

        webView.setWebViewClient( new MyWebViewClient( this,layoutEmpty ) );

        //加载标题
        webView.setWebChromeClient( new WebChromeClient()
        {
            @Override
            public void onReceivedTitle( WebView view,String title )
            {
                super.onReceivedTitle( view,title );
                System.out.println( "title:" + title );
                if(title.contains( "无法打开" ) || isHaveNumber( title ))
                {
                    System.out.println( "网页无法打开" );
                    System.out.println( "isHaveNumber(title)=" + isHaveNumber( title ) );
                    //setTitle( "网页无法打开" );
                    layoutEmpty.setVisibility( View.VISIBLE );
                }
                else
                {
                    System.out.println( "网页正常打开" );
                    //setTitle( title );
                    layoutEmpty.setVisibility( View.GONE );
                }
            }

        } );

        //error事件
        layoutEmpty.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                layoutEmpty.setVisibility( View.VISIBLE );
                webView.reload();
                //webView.loadUrl( mUrl );
                layoutEmpty.setVisibility( View.VISIBLE );
            }
        } );
    }


    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown( int keyCode,KeyEvent event )
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack())
        {
            webView.goBack();
            return true;
        }
        return super.onKeyDown( keyCode,event );
    }

    @Override
    public void hindProgressBar()
    {
        mProgressBar.setVisibility( View.GONE );
    }

    @Override
    public void startProgress()
    {
        startProgress90();
    }

    //进度条 假装加载到90%
    public void startProgress90()
    {
        for(int i = 0; i < 900; i++)
        {
            final int progress = i + 1;
            mProgressBar.postDelayed( new Runnable()
            {
                @Override
                public void run()
                {
                    mProgressBar.setProgress( progress );
                    if(progress == 900)
                    {
                        mProgress90 = true;
                        if(mPageFinish)
                        {
                            startProgress90to100();
                        }
                    }
                }
            },( i + 1 ) * 2 );
        }
    }

    //进度条 加载到100%
    public void startProgress90to100()
    {
        for(int i = 900; i <= 1000; i++)
        {
            final int progress = i + 1;
            mProgressBar.postDelayed( new Runnable()
            {
                @Override
                public void run()
                {
                    mProgressBar.setProgress( progress );
                    if(progress == 1000)
                    {
                        mProgressBar.setVisibility( View.GONE );
                    }
                }
            },( i + 1 ) * 2 );
        }
    }

    @Override
    public void progressChanged( int newProgress )
    {
        if(mProgress90)
        {
            int progress = newProgress * 100;
            if(progress > 900)
            {
                mProgressBar.setProgress( progress );
                if(progress == 1000)
                {
                    mProgressBar.setVisibility( View.GONE );
                }
            }
        }
    }

    @Override
    public void showWebView()
    {
        webView.setVisibility( View.VISIBLE );
    }

    @Override
    public void hindWebView()
    {
        webView.setVisibility( View.INVISIBLE );
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(webView != null)
        {
            ViewGroup parent = (ViewGroup)webView.getParent();
            if(parent != null)
            {
                parent.removeView( webView );
            }
            webView.removeAllViews();
            webView.loadUrl( "about:blank" );
            webView.stopLoading();
            webView.setWebChromeClient( null );
            webView.setWebViewClient( null );
            webView.destroy();
            webView = null;
        }
    }


    // 判断一个字符串是否含有数字
    protected boolean isHaveNumber( String content )
    {
        boolean flag = false;
        Pattern p = Pattern.compile( ".*\\d+.*" );
        Matcher m = p.matcher( content );
        if(m.matches())
        {
            flag = true;
        }
        return flag;
    }
}
