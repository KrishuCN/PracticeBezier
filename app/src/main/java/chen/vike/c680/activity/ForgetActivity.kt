package chen.vike.c680.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import com.blankj.utilcode.util.LogUtils
import com.lht.vike.a680_v1.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2017/3/3.
 */

class ForgetActivity : BaseStatusBarActivity() {

    private var mobile: EditText? = null
    private var code: EditText? = null
    private var password: EditText? = null
    private var testPassword: EditText? = null
    private var button: Button? = null
    private var enter: Button? = null
    private var number: TextView? = null
    private var timer: Timer? = null
    private val map = HashMap<String, Any>()
    private var currentTime = (60 * 1000).toLong()
    private val GETYANZHENGMA = 0x123
    private val HUANMIMA = 0x111
    private val NETWORK_EXCEPTION = 0x112
    private val TIME = 0x113
    private val hd = MHandler(this)

    private class MHandler(forgetActivity: ForgetActivity):Handler(){
        private val weakReference:WeakReference<ForgetActivity> = WeakReference(forgetActivity)
        private val forgetActivity = weakReference.get()

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            forgetActivity?.run {

                if (msg?.what == GETYANZHENGMA) {
                    val s = msg.data.getString("s")
                    if (s == "false" || s!!.isEmpty()) {
                        CustomToast.showToast(this, "发送失败", Toast.LENGTH_LONG)
                    } else if (s == "has") {
                        CustomToast.showToast(this, "发送失败", Toast.LENGTH_LONG)
                    } else {
1878
                        map["key"] = s
                        //按钮开始计时
                        timer = Timer()
                        timer!!.schedule(object : TimerTask() {
                            override fun run() {
                                hd.sendEmptyMessage(TIME)
                            }
                        }, 0, 1000)
                        CustomToast.showToast(this, "发送成功", Toast.LENGTH_LONG)
                    }
                } else if (msg?.what == NETWORK_EXCEPTION) {
                    LhtTool.showNetworkException(this, msg)
                    button!!.isEnabled = true
                } else if (msg?.what == HUANMIMA) {

                    val s = msg.data.getString("s")
                    when (s) {
                        "error_yzm" -> CustomToast.showToast(this, "短信验证码错误", Toast.LENGTH_LONG)
                        "newpdlen" -> CustomToast.showToast(this, "新密码长度不对(6-16)", Toast.LENGTH_LONG)
                        "flagcheck" -> CustomToast.showToast(this, "该手机绑定的账号等待审核中", Toast.LENGTH_LONG)
                        "stop" -> CustomToast.showToast(this, "该手机绑定的账号已被锁定", Toast.LENGTH_LONG)
                        "ok" -> {
                            CustomToast.showToast(this, "设置新登录密码成功,请登录", Toast.LENGTH_LONG)
                            finish()
                        }
                        "setnewfail" -> {
                            CustomToast.showToast(this, "设置新密码失败", Toast.LENGTH_LONG)
                            finish()
                        }
                        "unverify" -> CustomToast.showToast(this, "该手机号未认证", Toast.LENGTH_LONG)
                        "nouser" -> CustomToast.showToast(this, "该手机号绑定的账号不存在", Toast.LENGTH_LONG)
                    }
                } else if (msg?.what == TIME) {

                    currentTime -= 1000
                    button!!.text = (currentTime / 1000).toString() + "秒后重新获取"
                    button!!.isEnabled = false
                    if (currentTime <= 0) {
                        currentTime = (60 * 1000).toLong()
                        timer?.cancel()
                        button!!.text = "获取验证码"
                        button!!.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)

        title.text = "设置密码"
        mobile = findViewById<View>(R.id.phone) as EditText
        code = findViewById<View>(R.id.code) as EditText
        password = findViewById<View>(R.id.password) as EditText
        testPassword = findViewById<View>(R.id.testPass) as EditText
        button = findViewById<View>(R.id.button9) as Button
        number = findViewById<View>(R.id.number) as TextView
        enter = findViewById<View>(R.id.enter) as Button
        viewListener()//控件监听事件
        number!!.text = Html.fromHtml("注：如未绑定手机，请拨打客服电话<font color='#df231b'>400-630-6800</font>或到网站操作")

    }

    /**
     * view控件监听事件
     */
    private fun viewListener() {
        button!!.setOnClickListener {
            button!!.isEnabled = false
            if (mobile!!.text.toString().length == 11) {

                map["mobile"] = mobile!!.text
                map["key"] = UrlConfig.MOBILE_VERIFICATION_KEY
                map["type"] = "verify"
                map["check"] = "3"
                map["android"] = "1"
                OkhttpTool.getOkhttpTool().post(UrlConfig.MOBILE_VERIFICATION, map, object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        val s = response.body()!!.string()
                        LogUtils.d("======================GETYANZHENGMA_Response:$s")
                        val ms = Message()
                        ms.what = GETYANZHENGMA
                        val b = Bundle()
                        b.putString("s", s)
                        ms.data = b
                        hd.sendMessage(ms)
                    }
                })
            } else {
                CustomToast.showToast(this@ForgetActivity, "请输入正确的手机号码", Toast.LENGTH_LONG)
                button!!.isEnabled = true
            }
        }
        enter!!.setOnClickListener {
            if (mobile!!.text.toString().isEmpty()) {
                CustomToast.showToast(this@ForgetActivity, "请输入正确的手机号码", Toast.LENGTH_LONG)

            } else {
                if (code!!.text.toString().isEmpty()) {
                    CustomToast.showToast(this@ForgetActivity, "验证码不能为空", Toast.LENGTH_LONG)

                } else {
                    if (password!!.text.toString().isEmpty()) {
                        CustomToast.showToast(this@ForgetActivity, "密码不能为空", Toast.LENGTH_LONG)

                    } else {
                        if (password!!.text.toString() == testPassword!!.text.toString()) {
                            map["crv_newpass"] = password!!.text
                            map["yzm"] = code!!.text
                            OkhttpTool.getOkhttpTool().post(UrlConfig.MOBILE_USER_FIND, map, object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)

                                }

                                @Throws(IOException::class)
                                override fun onResponse(call: Call, response: Response) {

                                    val s = response.body()!!.string()
                                    LogUtils.d("======================GETYANZHENGMA_Response:$s")
                                    val ms = Message()
                                    ms.what = HUANMIMA
                                    val b = Bundle()
                                    b.putString("s", s)
                                    ms.data = b
                                    hd.sendMessage(ms)


                                }
                            })
                        } else {


                            CustomToast.showToast(this@ForgetActivity, "确认密码不一致", Toast.LENGTH_LONG)
                        }
                    }
                }
            }
        }
        number!!.setOnClickListener {
            try {
                LhtTool.Call(Intent.ACTION_DIAL, "400-630-6800", this@ForgetActivity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        hd.removeCallbacksAndMessages(null)
    }
}
