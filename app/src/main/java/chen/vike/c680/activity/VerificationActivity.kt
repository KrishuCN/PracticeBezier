package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import chen.vike.c680.bean.UserYanZhengInfoBean
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.lht.vike.a680_v1.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * Created by lht on 2017/3/10.
 *
 * 提现页面
 */

class VerificationActivity : BaseStatusBarActivity() {

    private var username: TextView? = null
    private var blance_moneny: TextView? = null
    private var userID: TextView? = null
    private var icon: ImageView? = null
    private var textview_one: TextView? = null
    private var textview_tw: TextView? = null
    private var textview_th: TextView? = null
    private val map = HashMap<String, Any>()
    private val TIXIANQUEREN = 0x123
    private val NETWORKEXCEPTION = 0x122
    private var email: String? = null
    private var bankname: String? = null
    private var fullname: String? = null
    private var userYanZhengInfo: UserYanZhengInfoBean? = null

    @SuppressLint("HandlerLeak")
    private val hd = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == TIXIANQUEREN) {

                if (userYanZhengInfo!!.v_fullname == "1") {

                    textview_one!!.text = userYanZhengInfo!!.truename
                    MyApplication.userInfo!!.is_verify_fullname = "1"
                    textview_one!!.setTextColor(this@VerificationActivity.resources.getColor(R.color.text_color_13))

                } else if (userYanZhengInfo!!.v_fullname == "2") {

                    textview_one!!.text = "等待审核中"
                    MyApplication.userInfo!!.is_verify_fullname = "2"
                    textview_one!!.setTextColor(this@VerificationActivity.resources.getColor(R.color.text_color_13))

                } else {

                    textview_one!!.text = "请点击验证"
                    textview_one!!.setTextColor(this@VerificationActivity.resources.getColor(R.color.text_color_2))

                }
                if (userYanZhengInfo!!.v_email == "1") {

                    textview_tw!!.setTextColor(this@VerificationActivity.resources.getColor(R.color.text_color_13))
                    textview_tw!!.text = userYanZhengInfo!!.email

                } else {

                    textview_tw!!.text = "请点击验证"
                    textview_tw!!.setTextColor(this@VerificationActivity.resources.getColor(R.color.text_color_2))

                }
                if (userYanZhengInfo!!.v_bankinfo == "1") {

                    textview_th!!.text = userYanZhengInfo!!.bankcardno
                    textview_th!!.setTextColor(this@VerificationActivity.resources.getColor(R.color.text_color_13))

                } else {

                    textview_th!!.text = "请点击绑定银行卡"
                    textview_th!!.setTextColor(this@VerificationActivity.resources.getColor(R.color.text_color_2))

                }


            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(this@VerificationActivity, msg)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.verification_activity)

        title.text = "提现"
        username = findViewById(R.id.userName)
        userID = findViewById(R.id.userId)
        blance_moneny = findViewById(R.id.mony_tx)
        icon = findViewById(R.id.imag_head)//用户头像
        if (LhtTool.isLogin) {
            username!!.text = "余额"
            userID!!.text = MyApplication.userInfo!!.nickame
            blance_moneny!!.text = MyApplication.userInfo!!.balance + "元"

            val options = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(this@VerificationActivity).load(MyApplication.userInfo!!.icon).apply(options).into(icon!!)
        }
        textview_one = findViewById(R.id.textview_one)
        textview_tw = findViewById(R.id.textview_tw)
        textview_th = findViewById(R.id.textview_th)
        textview_one!!.setOnClickListener {
            if (fullname!!.isEmpty()) {

                val `in` = Intent(this@VerificationActivity, ShiMingRenZhengActivity::class.java)
                startActivityForResult(`in`, 1)

            } else if (fullname!!.toString() == "2") {
                CustomToast.showToast(this@VerificationActivity, "审核中...", Toast.LENGTH_SHORT)

            } else {
                CustomToast.showToast(this@VerificationActivity, "实名已验证", Toast.LENGTH_SHORT)
            }
        }

        textview_tw!!.setOnClickListener {
            //邮箱验证界面跳转
            if (email!!.isEmpty()) {
                val `in` = Intent(this@VerificationActivity, EmailActivity::class.java)
                startActivityForResult(`in`, 2)
            } else {
                CustomToast.showToast(this@VerificationActivity, "邮箱已验证", Toast.LENGTH_SHORT)
            }
        }

        textview_th!!.setOnClickListener {
            //银行卡绑定界面
            if (userYanZhengInfo!!.v_bankinfo == "1") {
                CustomToast.showToast(this@VerificationActivity, "银行卡已绑定", Toast.LENGTH_SHORT)
            } else {
                if (userYanZhengInfo!!.v_fullname == "2") {
                    CustomToast.showToast(this@VerificationActivity, "请等待审核", Toast.LENGTH_SHORT)
                } else {
                    if (userYanZhengInfo!!.v_fullname == "1") {
                        if (userYanZhengInfo!!.v_email == "1") {
                            val `in` = Intent(this@VerificationActivity, BankActivity::class.java)
                            startActivityForResult(`in`, 3)
                        } else {
                            CustomToast.showToast(this@VerificationActivity, "请先进行邮箱和实名验证", Toast.LENGTH_SHORT)
                        }
                    } else {
                        CustomToast.showToast(this@VerificationActivity, "请先进行邮箱和实名验证", Toast.LENGTH_SHORT)
                    }
                }

            }
        }

        email = intent.getStringExtra("email")
        bankname = intent.getStringExtra("bankname")
        fullname = intent.getStringExtra("fullname")

        personalVerificationInfo()

    }


    private fun personalVerificationInfo() {

        val map = HashMap<String, Any>()
        if (MyApplication.userInfo != null && MyApplication.userInfo!!.userID != null) {
            map["userid"] = MyApplication.userInfo!!.userID
        }
        OkhttpTool.getOkhttpTool().post(UrlConfig.USER_VERIFICATION_INFO, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d("============Response:$s")
                    userYanZhengInfo = Gson().fromJson(s, UserYanZhengInfoBean::class.java)
                    hd.sendEmptyMessage(TIXIANQUEREN)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        personalVerificationInfo()
        if (MyApplication.userInfo != null) {
            if (MyApplication.userInfo!!.is_verify_fullname != null) {
                if (requestCode == 1) {
                    if (MyApplication.userInfo!!.is_verify_fullname == "2") {
                        textview_one!!.text = "等待审核中..."
                        textview_one!!.setTextColor(this@VerificationActivity.resources.getColor(R.color.text_color_13))
                        fullname = "等待审核中"
                    }
                } else if (requestCode == 2) {
                    if (MyApplication.userInfo!!.is_verify_email == "1") {
                        textview_tw!!.setTextColor(this@VerificationActivity.resources.getColor(R.color.text_color_13))
                        textview_tw!!.text = MyApplication.userInfo!!.email
                        email = MyApplication.userInfo!!.email
                    }
                } else if (requestCode == 3) {
                    if (MyApplication.userInfo!!.is_bind_bankinfo == "1") {
                        textview_th!!.text = MyApplication.userInfo!!.bankname
                        textview_th!!.setTextColor(this@VerificationActivity.resources.getColor(R.color.text_color_13))
                        bankname = MyApplication.userInfo!!.bankname
                    }
                }
            }
        }
    }

}
