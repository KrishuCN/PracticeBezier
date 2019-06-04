package chen.vike.c680.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.JavaScriptInterface
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.views.LoadingDialog
import com.lht.vike.a680_v1.R

/**
 * Created by lht on 2017/3/8.
 */

class WebActivity : BaseStatusBarActivity() {

    private var wb: WebView? = null
    private var url: String? = null
    private var ld: LoadingDialog? = null

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_activity)


        ld = LoadingDialog(this).setMessage("加载中....")
        ld!!.show()

        //我觉得这个写的很麻烦，但是这会没时间改了，只先将就他以前写的用了
        url = intent.getStringExtra("weburl")
        if (url!!.indexOf("?") > 0) {
            url += "&isapp=1"
        } else {
            url += "?isapp=1"
        }

        title.text = intent.getStringExtra("title")
        wb = findViewById(R.id.wb)
        wb!!.requestFocusFromTouch()//支持获取手势焦点，输入用户名、密码或其他

        val webSettings = wb!!.settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webSettings.javaScriptEnabled = true
        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true//将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true// 缩放至屏幕的大小

        webSettings.setSupportZoom(true)//支持缩放，默认为true。是下面那个的前提
        webSettings.builtInZoomControls = true//设置内置的缩放控件
        //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
        //        webSettings.setTextZoom(3);//设置文本的缩放倍数，默认为 100//设置成3你会看到很奇怪的现象

        //设为true会有泄露错误
        webSettings.displayZoomControls = false//隐藏原生的缩放控件

        //        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局

        //        webSettings.setSupportMultipleWindows(true);//多窗口
        //        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        webSettings.allowFileAccess = true//设置可以访问文件
        //        webSettings.setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
        //        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true//支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8"//设置编码格式
        /**
         * “ncp”是js调用的  “callOnJs”webView上跳转回Activity的方法
         * “ ncp.callOnJs("case", t + "," + itemid + "," + cyid);”  js上调用跳转的例句
         * Android容器设置桥连对象
         */
        wb!!.addJavascriptInterface(JavaScriptInterface(this), "ncp") ///  ncp.callOnJs("case", t + "," + itemid + "," + cyid);

        //以下设置可自定义网页字体风格及大小，不过这会用不到
        //        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
        //        webSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
        //        webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8

        wb!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                LhtTool.setCookieManager(url)
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false
                }
                view.settings.cacheMode = WebSettings.LOAD_DEFAULT
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                //在这里让加载动画结束
                if (null != ld) {
                    ld!!.dismiss()
                }
                super.onPageFinished(view, url)
            }
        }

        wb!!.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {

                //                // 增加Javascript异常监控
                //                CrashReport.setJavascriptMonitor(webView, true);
                //                if(newProgress==100){
                //                    if (myDialog != null){
                //                        myDialog.dismiss2();
                //                    }
                //                }
                super.onProgressChanged(view, newProgress)

            }
        }

        LhtTool.setCookieManager(url)
        wb!!.loadUrl(url)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == 1) {
            LhtTool.setCookieManager(url)
            wb!!.loadUrl(url)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
