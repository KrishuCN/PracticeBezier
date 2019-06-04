package chen.vike.c680.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Message
import chen.vike.c680.tools.ACache
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.gyf.barlibrary.BarHide
import com.gyf.barlibrary.ImmersionBar
import chen.vike.c680.bean.GuZhuBean
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.tools.UrlConfig
import com.lht.vike.a680_v1.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2017/3/1.
 * 初始化图片页面
 */

class Splash : BaseActivity() {

    private val SPLASH_DELAY_MILLONS: Long = 3000
    private val TO_GUIDE = 0X111
    private val TO_MAIN = 0X123
    private var sp: SharedPreferences? = null
    var mIntent: Intent? = null
    private val mHandler: MHandler = MHandler(this)
    /**
     * 雇主页面网络数据
     */
    private val guzhuMap = HashMap<String, Any>()
    private var guZhuBean = GuZhuBean()
    private val NETWORK_EXCEPTION = 0X111
    private var aCache: ACache? = null//轻量级缓存


    class MHandler constructor(activity:Splash): Handler(){
        private var weakReference: WeakReference<Splash> = WeakReference(activity)
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val splash = weakReference.get()
            when (msg!!.what) {
                splash?.TO_GUIDE-> splash.mIntent = Intent(splash, GuidePage::class.java)
                splash?.TO_MAIN -> splash.mIntent = Intent(splash, MainActivity::class.java)
                GUZHUDATASUCCES -> {
                }
            }
            if (splash?.mIntent != null) {
                splash.startActivity(splash.mIntent)
                splash.finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        /**
         * 隐藏状态栏
         */
        ImmersionBar.with(this)
                .fullScreen(true)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init()

        OkhttpTool.getOkhttpTool()[UrlConfig.SPLASH_NEWS, object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val s = response.body()!!.string()
//                Log.d("NOBUG","============response:$s")
            }
        }]

        guzhuData()
        sp = getSharedPreferences("splash", Context.MODE_PRIVATE)
        if (sp!!.getBoolean("FirstIn", true)) {
            mHandler.sendEmptyMessageDelayed(TO_GUIDE, SPLASH_DELAY_MILLONS)
            val et = sp!!.edit()
            et.putBoolean("FirstIn", false)
            et.apply()
        } else {
            mHandler.sendEmptyMessageDelayed(TO_MAIN, SPLASH_DELAY_MILLONS)
        }
    }

    private fun guzhuData() {
        aCache = ACache.get(this)
        if (LhtTool.isLogin) {
            guzhuMap["userid"] = MyApplication.userInfo!!.userID + ""
        } else {
            guzhuMap["userid"] = "0"
        }
        OkhttpTool.getOkhttpTool().post(UrlConfig.GUZHUPAGE, guzhuMap, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(mHandler, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
                try{
                    LogUtils.d("雇主页面: \n $s")
                    guZhuBean = Gson().fromJson(s, GuZhuBean::class.java)
                    aCache?.put("guZhuBean", s, 5 * ACache.TIME_DAY)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    companion object {
        private val GUZHUDATASUCCES = 0X124
        private val GUZHUDATAERROR = 0X120
    }
}
