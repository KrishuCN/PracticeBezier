package chen.vike.c680.webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
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
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import chen.vike.c680.main.BaseNewActivity
import chen.vike.c680.tools.JavaScriptInterface
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.views.LodaWindow
import com.lht.vike.a680_v1.R
import kotterknife.bindView
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2018/6/26.
 */

class WebNoTitleActivity : BaseNewActivity() {

    internal val linearBar: LinearLayout by bindView(R.id.linear_bar)
    private val webView: WebView by bindView(R.id.web_view)
    private val chongxinJiazai: Button by bindView(R.id.chongxin_jiazai)
    internal val zanwuWangluo: LinearLayout by bindView(R.id.zanwu_wangluo)
    internal val wangluoTitle: TextView by bindView(R.id.wangluo_title)
    internal var wb: WebView? = null
    private var url: String? = ""
    private val ld: LoadingDialog? = null
    internal var lod: LodaWindow? = null
    private var isload = true
    private var timer: Timer? = null//计时器
    private val timeout: Long = 6000//超时时间
    private val mHandler = MHandler(this)
    private var tt:TimerTask?=null

    /**
     * 设置状态栏
     */
    private var linear_bar: LinearLayout? = null

    private class MHandler(webNoTitleActivity: WebNoTitleActivity):Handler(){
        private var weakReference:WeakReference<WebNoTitleActivity> = WeakReference(webNoTitleActivity)
        private val mActivity = weakReference.get()

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            mActivity?.run {
                when (msg?.what) {
                    LOAD -> {
                        lod = LodaWindow(this, this)
                        lod!!.setMessage("加载中....")
                    }
                    LOADTIMEOUT -> {
                        lod?.dis()
                        Log.e("webload", "chaoshi222")
                        wb!!.visibility = View.GONE
                        zanwuWangluo.visibility = View.VISIBLE
                        wangluoTitle.text = "请求超时，请检查网络!"
                    }
                }
            }
        }
    }

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_page)
        setZhuangTaiLan()
        url = intent.getStringExtra("weburl")
        if (url != null) {
            url = if (url!!.indexOf("?") > 0) {
                url!! + "&isapp=1"
            } else {
                url!! + "?isapp=1"
            }
        }

        wb = findViewById(R.id.web_view)
        wb!!.requestFocusFromTouch()//支持获取手势焦点，输入用户名、密码或其他

        val webSettings = wb!!.settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true//将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true// 缩放至屏幕的大小

        webSettings.setSupportZoom(true)//支持缩放，默认为true。是下面那个的前提
        webSettings.builtInZoomControls = true//设置内置的缩放控件
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT//不适用缓存

        //设为true会有泄露错误
        webSettings.displayZoomControls = false//隐藏原生的缩放控件

        webSettings.allowFileAccess = true//设置可以访问文件
        webSettings.loadsImagesAutomatically = true//支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8"//设置编码格式
        /**
         * “ncp”是js调用的  “callOnJs”webView上跳转回Activity的方法
         * “ ncp.callOnJs("case", t + "," + itemid + "," + cyid);”  js上调用跳转的例句
         * Android容器设置桥连对象
         */
        wb!!.addJavascriptInterface(JavaScriptInterface(this), "ncp") ///  ncp.callOnJs("case", t + "," + itemid + "," + cyid);


        wb!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                LhtTool.setCookieManager(url)
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false
                }
                if (view != null) {
                    view.settings.cacheMode = WebSettings.LOAD_DEFAULT
                }
                if (url.contains("tel:")) {

                    if (ActivityCompat.checkSelfPermission(this@WebNoTitleActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return true
                    }
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse(url))
                    startActivity(intent)
                    //这个超连接,java已经处理了，webview不要处理了
                    return true
                }

                //                if (getIntent().getStringExtra("title").equals("商标注册服务") || getIntent().getStringExtra("title").equals("版权登记服务")) {
                //                    Log.e("notitle",url+"");
                //                    view.loadUrl(url + "?dover=2");
                //                }

                return true
            }


            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                if (isload) {
                    mHandler.sendEmptyMessage(LOAD)
                    webTimeOut()
                }
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageCommitVisible(view: WebView, url: String) {
                lod?.dis()
                isload = false

                super.onPageCommitVisible(view, url)
            }

            override fun onPageFinished(view: WebView, url: String) {
                lod?.dis()
                isload = false
                timer?.cancel()
                timer?.purge()
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                wb!!.visibility = View.GONE
                zanwuWangluo.visibility = View.VISIBLE
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }


        wb!!.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }
        }

        chongxinJiazai.setOnClickListener {
            LhtTool.setCookieManager(url)
            wb!!.loadUrl(url)
            wb!!.visibility = View.VISIBLE
            zanwuWangluo.visibility = View.GONE
        }
        LhtTool.setCookieManager(url)
        wb!!.loadUrl(url)

    }

    private fun webTimeOut() {
        timer = Timer()
        tt = object : TimerTask() {
            override fun run() {
                /* * 超时后,首先判断页面加载是否小于100,就执行超时后的动作 */
                mHandler.sendEmptyMessage(LOADTIMEOUT)
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
        } else if (resultCode == 2) {
            wb!!.reload()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setZhuangTaiLan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏0
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            linear_bar = findViewById(R.id.linear_bar)
            linear_bar!!.visibility = View.VISIBLE
            linear_bar!!.setBackgroundColor(Color.parseColor("#ffffff"))
            val statusHeight = LhtTool.getStatusBarHeight(this)
            val params = linear_bar!!.layoutParams as LinearLayout.LayoutParams
            params.height = statusHeight
            linear_bar!!.layoutParams = params
        }


        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT >= 23) {
            val REQUEST_CODE_CONTACT = 101
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            //验证是否许可权限
            for (str in permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT)
                    return
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lod?.dis()
        timer?.cancel()
        timer?.purge()
        tt?.cancel()
        mHandler.removeCallbacksAndMessages(null)
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
        private val LOAD = 0x123

        /**
         * 网页访问判断超时问题
         */
        private val LOADTIMEOUT = 0x101
    }

}
