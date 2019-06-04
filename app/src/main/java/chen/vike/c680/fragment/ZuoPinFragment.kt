package chen.vike.c680.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.JavaScriptInterface
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.views.LoadingDialog
import com.gyf.barlibrary.ImmersionBar
import com.gyf.barlibrary.SimpleImmersionFragment
import com.lht.vike.a680_v1.R
import kotlinx.android.synthetic.main.faxian_fragment_page.*
import kotlinx.android.synthetic.main.faxian_fragment_page.view.*

/**
 * Created by lht on 2018/3/28.
 */

class ZuoPinFragment : SimpleImmersionFragment() {

    private var mContext: Context? = null
    private var wSet: WebSettings? = null
    private var FAXIAN = ""
    private val ld: LoadingDialog? = null

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = LayoutInflater.from(mContext).inflate(R.layout.faxian_fragment_page, null)
        FAXIAN = if (MyApplication.userInfo != null) {
            "http://app.680.com/faxian/?isandrod=1&uid=" + MyApplication.userInfo!!.userID
        } else {
            "http://app.680.com/faxian/?isandrod=1&uid=0"
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wSet = view.faxian_web!!.settings
        setwebFaxian()
    }

    /**
     * webview设置
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setwebFaxian() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wSet!!.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        wSet!!.javaScriptEnabled = true
        //设置自适应屏幕，两者合用
        wSet!!.useWideViewPort = true//将图片调整到适合webview的大小
        wSet!!.loadWithOverviewMode = true// 缩放至屏幕的大小

        wSet!!.setSupportZoom(true)//支持缩放，默认为true。是下面那个的前提
        wSet!!.builtInZoomControls = true//设置内置的缩放控件
        //设为true会有泄露错误
        wSet!!.displayZoomControls = false//隐藏原生的缩放控件
        wSet!!.cacheMode = WebSettings.LOAD_NO_CACHE//不适用缓存


        wSet!!.allowFileAccess = true//设置可以访问文件
        //        webSettings.setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
        //        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        wSet!!.loadsImagesAutomatically = true//支持自动加载图片
        wSet!!.defaultTextEncodingName = "utf-8"//设置编码格式
        faxian_web!!.addJavascriptInterface(JavaScriptInterface(activity), "ncp") ///  ncp.callOnJs("case", t + "," + itemid + "," + cyid);
        faxian_web!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                LhtTool.setCookieManager(url)
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false
                }
                view.settings.cacheMode = WebSettings.LOAD_DEFAULT
                if (url.contains("tel:")) {

                    if (ActivityCompat.checkSelfPermission(mContext!!, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return true
                    }
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse(url))
                    startActivity(intent)
                    //这个超连接,java已经处理了，webview不要处理了
                    return true
                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                //在这里让加载动画结束
                ld?.dismiss()
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                faxian_web!!.visibility = View.GONE
                zanwu_wangluo!!.visibility = View.VISIBLE
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }
        chongxin_jiazai!!.setOnClickListener {
            faxian_web!!.loadUrl(FAXIAN)
            faxian_web!!.visibility = View.VISIBLE
            zanwu_wangluo!!.visibility = View.GONE
        }
        faxian_web!!.loadUrl(FAXIAN)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ld?.dismiss()
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                //                .fitsSystemWindows(true)
                .autoDarkModeEnable(true, 0.2f)
                .init()
    }
}
