package chen.vike.c680.activity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import chen.vike.c680.main.MyApplication
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
 * Created by lht on 2017/3/8.
 * 银行卡绑定
 */

class BankActivity : BaseStatusBarActivity() {

    private var bank_one: EditText? = null
    private var bank_tw: EditText? = null
    private var bank_th: EditText? = null
    private var bank_fo: EditText? = null
    private var code: EditText? = null
    private var button9: Button? = null
    private var buttona: Button? = null
    private val map = HashMap<String, Any>()
    private var key: String? = null
    private var exitTime: Long = 0
    private var exitTime1: Long = 0
    private var exitTime2: Long = 0
    private var exitTime3: Long = 0
    private var exitTime4: Long = 0
    private var exitTime5: Long = 0
    private val GETYANZHENGMA = 0x123
    private val TIJIAOXINXI = 0x111
    private val NETWORK_EXCEPTION = 0x112
    private val TIME = 0x113
    private var currentTime = (60 * 1000).toLong()
    private var timer: Timer? = null
    private val hd: MHandler = MHandler(this)


    private class MHandler(activity: BankActivity) : Handler() {
        private val weakReference: WeakReference<BankActivity> = WeakReference(activity)
        private var activity = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            activity?.run {
                if (msg?.what == TIJIAOXINXI) {

                    val s = msg.data.getString("s")
                    when (s) {
                        "unlogin" -> CustomToast.showToast(this, "请登录", Toast.LENGTH_SHORT)
                        "erryzm" -> CustomToast.showToast(this, "验证码错误", Toast.LENGTH_SHORT)
                        "notname" -> CustomToast.showToast(this, "无开户行户名", Toast.LENGTH_SHORT)
                        "nobank" -> CustomToast.showToast(this, "未指定银行卡名称", Toast.LENGTH_SHORT)
                        "noopenbank" -> CustomToast.showToast(this, "未指定具体开户行", Toast.LENGTH_SHORT)
                        "nobanknmb" -> CustomToast.showToast(this, "未指定银行卡账号或卡号", Toast.LENGTH_SHORT)
                        "nouser" -> CustomToast.showToast(this, "无效用户", Toast.LENGTH_SHORT)
                        "fail" -> CustomToast.showToast(this, "绑定失败", Toast.LENGTH_SHORT)
                        "nopayedit" -> CustomToast.showToast(this, "银行卡已绑定", Toast.LENGTH_SHORT)
                        "ok" -> {
                            CustomToast.showToast(this, "绑定成功", Toast.LENGTH_SHORT)
                            MyApplication.userInfo!!.is_bind_bankinfo = "1"
                            MyApplication.userInfo!!.bankname = bank_one!!.text.toString()
                            MyApplication.userInfo!!.bankcardno = bank_fo!!.text.toString()
                            finish()
                        }
                    }


                } else if (msg?.what == GETYANZHENGMA) {

                    val s = msg.data.getString("s")
                    when (s) {
                        "senderr" -> CustomToast.showToast(this, "发送失败", Toast.LENGTH_SHORT)
                        "nouser" -> CustomToast.showToast(this, "无效用户", Toast.LENGTH_SHORT)
                        "noemail" -> CustomToast.showToast(this, "无效邮箱", Toast.LENGTH_SHORT)
                        else -> {
                            val k = s.toString().split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (k[0] == "yeskey") {
                                timer = Timer()
                                timer!!.schedule(object : TimerTask() {
                                    override fun run() {
                                        hd.sendEmptyMessage(TIME)
                                    }
                                }, 0, 1000)
                                key = k[1]
                                CustomToast.showToast(this, "发送成功，请注意查收", Toast.LENGTH_SHORT)
                            }
                        }
                    }


                } else if (msg?.what == NETWORK_EXCEPTION) {
                    LhtTool.showNetworkException(this, msg)
                    button9!!.isEnabled = true
                } else if (msg?.what == TIME) {
                    currentTime -= 1000
                    button9!!.text = (currentTime / 1000).toString() + "秒后重新获取"
                    if (currentTime <= 0) {
                        currentTime = (60 * 1000).toLong()
                        timer!!.cancel()
                        button9!!.text = "获取验证码"
                        button9!!.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bank_activity)

        title.text = "银行卡绑定"
        iniView()//初始化
        viewListener()//控件监听事件
        if (LhtTool.isLogin) {
            bank_th!!.setText(MyApplication.userInfo!!.realName)
            bank_th!!.isFocusable = false
        }
    }

    /**
     * 初始化
     */
    private fun iniView() {
        bank_one = findViewById(R.id.bank_one)
        bank_tw = findViewById(R.id.bank_tw)
        bank_th = findViewById(R.id.bank_th)
        bank_fo = findViewById(R.id.bank_fo)
        code = findViewById(R.id.code)
        button9 = findViewById(R.id.button9)
        buttona = findViewById(R.id.buttona)
    }

    /**
     * view控件监听事件
     */
    private fun viewListener() {
        button9!!.setOnClickListener {
            button9!!.isEnabled = false
            if (MyApplication.userInfo!!.is_verify_email == "1") {
                map["userid"] = MyApplication.userInfo!!.userID
                map["crv_email"] = MyApplication.userInfo!!.email
                OkhttpTool.getOkhttpTool().post(UrlConfig.EMAIL_VERIFICATION, map, object : Callback {
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
                if (System.currentTimeMillis() - exitTime > 4000) {
                    CustomToast.showToast(this@BankActivity, "请先进行邮箱验证", Toast.LENGTH_SHORT)
                    exitTime = System.currentTimeMillis()
                    button9!!.isEnabled = true
                }

            }
        }

        buttona!!.setOnClickListener {
            if (!bank_one!!.text.toString().isEmpty()) {
                if (!bank_tw!!.text.toString().isEmpty()) {
                    if (!bank_th!!.text.toString().isEmpty()) {
                        if (!bank_fo!!.text.toString().isEmpty()) {
                            if (!code!!.text.toString().isEmpty()) {
                                Bank()
                            } else {
                                if (System.currentTimeMillis() - exitTime5 > 4000) {
                                    CustomToast.showToast(this@BankActivity, "请填写输入验证码", Toast.LENGTH_SHORT)
                                    exitTime5 = System.currentTimeMillis()
                                }
                            }
                        } else {
                            if (System.currentTimeMillis() - exitTime4 > 4000) {
                                CustomToast.showToast(this@BankActivity, "请填写银行卡号或账号", Toast.LENGTH_SHORT)
                                exitTime4 = System.currentTimeMillis()
                            }
                        }
                    } else {
                        if (System.currentTimeMillis() - exitTime3 > 4000) {
                            CustomToast.showToast(this@BankActivity, "请填写开户行姓名", Toast.LENGTH_SHORT)
                            exitTime3 = System.currentTimeMillis()
                        }
                    }
                } else {
                    if (System.currentTimeMillis() - exitTime2 > 4000) {
                        CustomToast.showToast(this@BankActivity, "请填写银行具体开户行", Toast.LENGTH_SHORT)
                        exitTime2 = System.currentTimeMillis()
                    }
                }
            } else {
                if (System.currentTimeMillis() - exitTime1 > 4000) {
                    CustomToast.showToast(this@BankActivity, "请填写银行名称", Toast.LENGTH_SHORT)
                    exitTime1 = System.currentTimeMillis()
                }

            }
        }
    }

    /**
     * 银行卡信息
     */
    private fun Bank() {
        map["userid"] = MyApplication.userInfo!!.userID
        map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
        map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
        map["yzm"] = code!!.text.toString()
        map["email"] = MyApplication.userInfo!!.email
        map["key"] = key!!
        map["crv_bankuser"] = bank_th!!.text.toString()
        map["crv_selbank"] = bank_one!!.text.toString()//银行名称
        map["crv_openbankname"] = bank_tw!!.text.toString()
        map["crv_bankcardno"] = bank_fo!!.text.toString()
        OkhttpTool.getOkhttpTool().post(UrlConfig.BANK_BIND, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
                LogUtils.d("======================GETYANZHENGMA_Response:$s")
                val ms = Message()
                ms.what = TIJIAOXINXI
                val b = Bundle()
                b.putString("s", s)
                ms.data = b
                hd.sendMessage(ms)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer?.purge()
        hd.removeCallbacksAndMessages(null)
    }
}
