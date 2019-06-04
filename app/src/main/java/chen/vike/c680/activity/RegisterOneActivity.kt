package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.blankj.utilcode.util.LogUtils
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import com.lht.vike.a680_v1.R

import java.io.IOException
import java.util.HashMap

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.lang.ref.WeakReference


/**
 * Created by lht on 2017/3/3.
 */

class RegisterOneActivity : BaseStatusBarActivity() {

    private var next: Button? = null
    private var login: View? = null
    private var phoneNumber: EditText? = null
    private val REGISTER_YANZHENGMA = 0x123
    private val NETWORK_EXCEPTION = 0X111
    private val hd = MHandler(this)

    private class MHandler(registerOneActivity: RegisterOneActivity) : Handler() {
        private val weakReference: WeakReference<RegisterOneActivity> = WeakReference(registerOneActivity)
        private val registerOneActivity = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            registerOneActivity?.run {
                if (msg?.what == REGISTER_YANZHENGMA) {

                    val s = msg.data.getString("s")
                    if (s == "false" || s!!.isEmpty()) {
                        CustomToast.showToast(this, "发送失败", Toast.LENGTH_LONG)

                    } else if (s == "has") {
                        CustomToast.showToast(this, "该手机号已经注册", Toast.LENGTH_LONG)

                    } else {
                        CustomToast.showToast(this, "已发送验证码到" + phoneNumber!!.text.toString() + ",请注意查收", Toast.LENGTH_LONG)
                        val intent = Intent(this, RegisterTwoActivity::class.java)
                        intent.putExtra("key", s.toString())
                        intent.putExtra("phone", phoneNumber!!.text.toString())
                        startActivityForResult(intent, 2)
                        finish()
                    }
                } else if (msg?.what == NETWORK_EXCEPTION) {
                    LhtTool.showNetworkException(this, msg)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_one)

        title.text = "注册"
        next = findViewById(R.id.next)
        login = findViewById(R.id.login)
        phoneNumber = findViewById(R.id.phoneNumber)
        next!!.setOnClickListener {
            if (phoneNumber!!.text.toString().isEmpty()) {

                CustomToast.showToast(this@RegisterOneActivity, "手机号码不能为空", Toast.LENGTH_LONG)

            } else {

                if (LhtTool.isMobile(phoneNumber!!.text.toString())) {
                    val map = HashMap<String, Any>()
                    map["mobile"] = phoneNumber!!.text
                    map["key"] = UrlConfig.MOBILE_VERIFICATION_KEY
                    map["android"] = "1"
                    OkhttpTool.getOkhttpTool().post(UrlConfig.MOBILE_VERIFICATION, map, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)

                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {

                            val s = response.body()!!.string()
                            LogUtils.d("======================Response:$s")
                            val ms = Message()
                            ms.what = REGISTER_YANZHENGMA
                            val b = Bundle()
                            b.putString("s", s)
                            ms.data = b
                            hd.sendMessage(ms)


                        }
                    })
                } else {
                    CustomToast.showToast(this@RegisterOneActivity, "请输入正确的手机号码", Toast.LENGTH_LONG)
                }
            }
        }
        login!!.setOnClickListener { finish() }


    }

}
