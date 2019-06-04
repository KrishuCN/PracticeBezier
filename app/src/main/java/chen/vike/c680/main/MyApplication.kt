package chen.vike.c680.main

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.blankj.utilcode.util.ProcessUtils

import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import chen.vike.c680.bean.UserInfoBean
import chen.vike.c680.tools.LhtLogUtils
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.squareup.leakcanary.LeakCanary

//import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport

import org.litepal.LitePalApplication


/**
 * Created by lht on 2017/1/16.
 */

class MyApplication : LitePalApplication() {

    private lateinit var options: EMOptions

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        //环信初始化
        options = EMOptions()
        //        MobSDK.init(this);

        //环信
        EMClient.getInstance().init(this, options)

        setReleaseMode(false)

        // Bugly {context,appid,isdebug,strategy}
        CrashReport.initCrashReport(applicationContext, "b24fb1b4e4", true, setBuglyMainStrategy())

    }


    /**
     *  打包模式控制
     */
    private fun setReleaseMode(release: Boolean) {
        if (!release) {
            com.blankj.utilcode.util.LogUtils.getConfig().setLogSwitch(true)
                    .setLogHeadSwitch(true)
                    .setBorderSwitch(true)
                    .setSingleTagSwitch(true)
                    .setGlobalTag("DEBUG")
                    .setConsoleSwitch(true)

            LhtLogUtils.showLog = true
            options.acceptInvitationAlways = false
            EMClient.getInstance().setDebugMode(true)
            //LeakCanary
            initLeakCanary()
            //环信自动登录开关
            options.autoLogin = true

        } else {

            com.blankj.utilcode.util.LogUtils.getConfig().setLogSwitch(false)
                    .setLogHeadSwitch(true)
                    .setBorderSwitch(true)
                    .setSingleTagSwitch(true)
                    .setGlobalTag("RELEASE")
                    .setConsoleSwitch(true)

            LhtLogUtils.showLog = false //log显示开关
            // 默认添加好友时，是不需要验证的，改成需要验证
            options.acceptInvitationAlways = false
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(false)
            //LeakCanary
//            initLeakCanary()
            //环信自动登录开关
            options.autoLogin = true

        }
    }


    /**
     * 设置Bugly上报进程为主进程
     */
    private fun setBuglyMainStrategy(): CrashReport.UserStrategy {
        val context = applicationContext
        val packageName = context.packageName
        val processName = ProcessUtils.getCurrentProcessName()
        val strategy = CrashReport.UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        return strategy
    }

    /**
     * 初始化LeakCanary，检测内存泄露
     */
    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    /**
     *  程序终止的时候执行
     */
    override fun onTerminate() {
        //清除Glide缓存
        Glide.get(this).clearDiskCache()
        LogUtils.d("程序退出了")
        super.onTerminate()
    }

    /**
     * 程序在内存清理的时候执行（回收内存）
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

    }

    /**
     * 低内存的时候执行
     */
    override fun onLowMemory() {
        super.onLowMemory()
    }

    companion object {
        @JvmField
        var userInfo: UserInfoBean? = null
        lateinit var INSTANCE: Application
    }

}
