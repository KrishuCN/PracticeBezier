package chen.vike.c680.webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.JavaScriptInterface
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.views.LodaWindow
import com.blankj.utilcode.util.LogUtils
import com.lht.vike.a680_v1.R
import kotlinx.android.synthetic.main.webview_activity.*
import kotterknife.bindView
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2017/3/8.
 */

class WebViewActivity : BaseStatusBarActivity() {


    internal val webView: WebView by bindView(R.id.wb)
    internal val chongxinJiazai: Button by bindView(R.id.chongxin_jiazai)
    internal val zanwuWangluo: LinearLayout by bindView(R.id.zanwu_wangluo)
    internal val wangluoTitle: TextView by bindView(R.id.wangluo_title)
    internal val webViewProgress: ProgressBar by bindView(R.id.web_view_progress)
    private var url: String? = ""
    private val ld: LoadingDialog? = null
    private var lod: LodaWindow? = null
    private var timer: Timer? = null//计时器
    private val timeout: Long = 6000//超时时间
    private var barTitle = ""
    private var intProgress = 0
    private var tt: TimerTask? = null
    private val handler: MHandler = MHandler(this)


    private class MHandler(activity: WebViewActivity) : Handler() {
        private val weakReference: WeakReference<WebViewActivity> = WeakReference(activity)
        private var activity = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            activity?.run {
                when (msg?.what) {
                    LOAD -> {
                        lod = LodaWindow(this, this)
                        lod!!.setMessage("加载中....")
                        webViewProgress.visibility = View.VISIBLE
                        webViewProgress.progress = 0
                    }
                    LOADTIMEOUT -> {
                        lod?.dismiss()
                        Log.e("webload", "chaoshi222")
                        Log.e("webload", "chaoshi11")
                        webView.visibility = View.GONE
                        zanwuWangluo.visibility = View.VISIBLE
                        wangluoTitle.text = "请求超时，请检查网络!"
                    }
                    SETTILE -> title.text = barTitle
                    PROGRESS -> webViewProgress.progress = intProgress
                }
            }
        }
    }

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_activity)

        url = intent.getStringExtra("weburl")
        if (url != null) {
            if (url!!.indexOf("?") > 0) {
                url = url!! + "&isapp=1"
            } else {
                url = url!! + "?isapp=1"
            }
        }

        title.text = intent.getStringExtra("title")
        wb.requestFocusFromTouch()//支持获取手势焦点，输入用户名、密码或其他

        val webSettings = wb.settings
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
//        webSettings.textZoom = 3//设置文本的缩放倍数，默认为 100//设置成3你会看到很奇怪的现象


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
        wb.addJavascriptInterface(JavaScriptInterface(this), "ncp") ///  ncp.callOnJs("case", t + "," + itemid + "," + cyid);

        //以下设置可自定义网页字体风格及大小，不过这会用不到
        //        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
        //        webSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
        //        webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8

        wb.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {


                LhtTool.setCookieManager(url)
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false
                }
                view.settings.cacheMode = WebSettings.LOAD_DEFAULT
                if (url.contains("tel:")) {

                    if (ActivityCompat.checkSelfPermission(this@WebViewActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return true
                    }
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse(url))
                    startActivity(intent)
                    //这个超连接,java已经处理了，webview不要处理了
                    return true
                }

                if (intent.getStringExtra("title") == "商标注册服务" || intent.getStringExtra("title") == "版权登记服务") {
                    LogUtils.d("==================url:$url")
                    view.loadUrl("$url?dover=2")
                }

                return true
            }

            override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
                handler.sendEmptyMessage(LOAD)
                webTimeOut()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (null != lod) {
                    lod!!.dis()
                }
                timer!!.cancel()
                timer!!.purge()
                webViewProgress.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onPageCommitVisible(view: WebView, url: String) {
                //在这里让加载动画结束
                if (null != lod) {
                    lod!!.dis()
                }
                timer!!.cancel()
                timer!!.purge()
                webViewProgress.visibility = View.GONE
                super.onPageCommitVisible(view, url)
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                if (errorCode == WebViewClient.ERROR_HOST_LOOKUP || errorCode == WebViewClient.ERROR_CONNECT || errorCode == WebViewClient.ERROR_TIMEOUT) {
                    wb.visibility = View.GONE
                    zanwuWangluo.visibility = View.VISIBLE
                }
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }
        wb.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                barTitle = title
                handler.sendEmptyMessage(SETTILE)
                super.onReceivedTitle(view, title)
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                Log.e("webpro", newProgress.toString() + "")
                intProgress = newProgress
                handler.sendEmptyMessage(PROGRESS)
                super.onProgressChanged(view, newProgress)
            }
        }

        chongxinJiazai.setOnClickListener {
            wb.visibility = View.VISIBLE
            zanwuWangluo.visibility = View.GONE
            LhtTool.setCookieManager(url)
            wb.loadUrl(url)
            webViewProgress.visibility = View.VISIBLE
            webViewProgress.progress = 0
        }
        LhtTool.setCookieManager(url)
        wb.loadUrl(url)

    }

    /**
     * 网页访问判断超时问题
     */
    private fun webTimeOut() {
        timer = Timer()
        tt = object : TimerTask() {
            override fun run() {
                /* * 超时后,首先判断页面加载是否小于100,就执行超时后的动作 */
                Log.e("webload", "chaoshi")
                handler.sendEmptyMessage(LOADTIMEOUT)
            }
        }
        timer!!.schedule(tt, timeout, timeout)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wb!!.canGoBack()) {
                wb!!.goBack()
                return true
            } else {
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == 1) {
            LhtTool.setCookieManager(url)
            wb!!.loadUrl(url)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        lod?.dismiss()
        timer?.cancel()
        timer?.purge()
        tt?.cancel()
        handler.removeCallbacksAndMessages(null)
        // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
        // destory()
        val parent = webView.parent as? ViewGroup
        parent?.removeView(webView)
        webView.stopLoading()
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        webView.settings.javaScriptEnabled = false
        webView.clearHistory()
        webView.clearView()
        webView.removeAllViews()
        webView.destroy()
    }

    companion object {
        private val LOAD = 0X121
        private val LOADTIMEOUT = 0x101
        private val SETTILE = 0x123
        private val PROGRESS = 0x124
    }
}
