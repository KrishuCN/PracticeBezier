package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import chen.vike.c680.bean.UserInfoBean
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import com.lht.vike.a680_v1.R

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.util.HashMap

import chen.vike.c680.tools.ShardTools
import chen.vike.c680.bean.VipUserMessageBean
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

//import com.hyphenate.EMCallBack;
//import com.hyphenate.chat.EMClient;

/**
 * Created by lht on 2017/3/3.
 */

class RegisterTwoActivity : BaseStatusBarActivity() {

    private var next: Button? = null

    private var mYzm: EditText? = null
    private var mPass: EditText? = null
    private var mPass2: EditText? = null
    private var `in`: Intent? = null
    private val REGISTER_MESSAGE = 0x123
    private val NETWORK_EXCEPTION = 0X111
    private val LOGIN_MESSAGE = 0x124
    private var userMessageBean = VipUserMessageBean()//个人信息  登录信息
    lateinit var sp: SharedPreferences
    private var ld: LoadingDialog? = null

    private val map = HashMap<String, Any>()
    private var b: Bundle? = null
    private var driveId: String? = null


    @SuppressLint("HandlerLeak")
    private val hd = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(this@RegisterTwoActivity, msg)
            } else if (msg.what == REGISTER_MESSAGE) {
                val s = msg.data.getString("s")
                when (s) {
                    "mobile_err" -> CustomToast.showToast(this@RegisterTwoActivity, "手机号无效", Toast.LENGTH_LONG)
                    "yzm_null" -> CustomToast.showToast(this@RegisterTwoActivity, "验证码不能为空", Toast.LENGTH_LONG)
                    "yzm_err" -> CustomToast.showToast(this@RegisterTwoActivity, "验证码错误", Toast.LENGTH_LONG)
                    "pwd_err" -> CustomToast.showToast(this@RegisterTwoActivity, "请输入6-16位字母、数字组合密码", Toast.LENGTH_LONG)
                    "reg_err" -> CustomToast.showToast(this@RegisterTwoActivity, "注册失败", Toast.LENGTH_LONG)
                    "hasmobile" -> CustomToast.showToast(this@RegisterTwoActivity, "手机号已被注册使用了", Toast.LENGTH_LONG)
                    "fail" -> CustomToast.showToast(this@RegisterTwoActivity, "注册失败", Toast.LENGTH_LONG)
                    else -> {
                        //这儿会有相应第三单方注册调用
                        CustomToast.showToast(this@RegisterTwoActivity, "注册成功", Toast.LENGTH_LONG)
                        zhangPswLogin()
                    }
                }
            } else if (msg.what == LOGIN_MESSAGE) {
                val zhs = msg.data.getString("s")
                when (zhs) {
                    "null_name" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "账号不能为空", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    "null_pwd" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "密码不能为空", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    "stop" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "账号被禁用", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    "fail" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "登录失败（请检查账号或密码是否正确）", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    "nodata" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "登录失败，账号无效", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    "qq_nouserinfo" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "QQ登录绑定的本网站的会员账号已无效", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    "qq_nobinduser" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "QQ登录未绑定本网站的会员账号", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    "qq_nodata" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "未传递QQ登录所需的openid", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    "qq_openid_err2" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "QQ登录所需的openid错误", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    "qq_openid_err1" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "QQ登录所需的openid无效", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    "qq_newfail" -> {
                        CustomToast.showToast(this@RegisterTwoActivity, "QQ登录openid验证成功，系统处理异常，创建账号失败", Toast.LENGTH_LONG)
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                    }
                    else -> {
                        try {
                            MyApplication.userInfo = Gson().fromJson(zhs, UserInfoBean::class.java)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        // 环信登录
                        //                        EMClient.getInstance().login(MyApplication.userInfo.getUserID(), MyApplication.userInfo.getHuanxin_api_password(), new EMCallBack() {
                        //                            @Override
                        //                            public void onSuccess() {
                        //                                //为了保证进入主页面后本地会话和群组都 load 完毕
                        //                                EMClient.getInstance().chatManager().loadAllConversations();
                        //                                EMClient.getInstance().groupManager().loadAllGroups();
                        //                                LogUtils.d("=================登录成功!");
                        //                            }
                        //
                        //                            @Override
                        //                            public void onError(int code, String error) {
                        //                                LogUtils.d("===================error:" + error);
                        //                            }
                        //
                        //                            @Override
                        //                            public void onProgress(int progress, String status) {
                        //                                LogUtils.d("===================progress:" + progress + ",status：" + status);
                        //                            }
                        //                        });
                        // 保证每次SharedPreferences里的东西都是最新的，
                        // 上一次登录时填写的覆盖上上次写的数据
                        val editor = sp.edit()
                        editor.putString("USER_NAME", MyApplication.userInfo!!.nickame)
                        editor.putString("PASSWORD", mPass!!.text.toString())
                        //  editor.putString("USER", inputEditName.getText().toString());
                        editor.putString("USERID", MyApplication.userInfo!!.userID)
                        editor.putString("VKUSERIP", MyApplication.userInfo!!.cookieLoginIpt)
                        editor.putString("VKTOKEN", MyApplication.userInfo!!.cookieLoginToken)
                        editor.commit()
                        LhtTool.isLogin = true
                        LhtTool.isLoginPage = true
                        setResult(2)
                        //                        startActivity(new Intent(RegisterTwoActivity.this, MainActivity.class));
                        finish()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiyt_sign_up_info)

        title.text = "设置登录密码"
        sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        mYzm = findViewById(R.id.yanzhengma)
        mPass = findViewById(R.id.password)
        mPass2 = findViewById(R.id.testPass)
        next = findViewById(R.id.next)
        `in` = intent
        b = `in`!!.extras
        driveId = LhtTool.getHouse_CID(this)
        if (null != b) {
            map["key"] = b!!.getString("key")!!
            map["mobile"] = b!!.getString("phone")!!
        }
        next!!.setOnClickListener {
            if (mYzm!!.text.toString().isEmpty()) {
                CustomToast.showToast(this@RegisterTwoActivity, "验证码不能为空", Toast.LENGTH_LONG)
            } else {
                map["yzm"] = mYzm!!.text
                if (mPass!!.text.toString().isEmpty()) {
                    CustomToast.showToast(this@RegisterTwoActivity, "密码不能为空", Toast.LENGTH_LONG)
                } else {
                    if (mPass!!.text.toString().contentEquals(mPass2!!.text.toString())) {
                        map["crv_pass"] = mPass!!.text
                        OkhttpTool.getOkhttpTool().post(UrlConfig.REGISTER_MESSAGE, map, object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)

                            }

                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {

                                val s = response.body()!!.string()
                                LogUtils.d("======================Response:$s")
                                val ms = Message()
                                ms.what = REGISTER_MESSAGE
                                val b = Bundle()
                                b.putString("s", s)
                                ms.data = b
                                hd.sendMessage(ms)
                            }
                        })


                    } else {

                        CustomToast.showToast(this@RegisterTwoActivity, "上下密码不一致", Toast.LENGTH_LONG)
                    }

                }
            }
        }


    }

    /**
     * 注册成功后自动登录
     */
    private fun zhangPswLogin() {
        map["UserId"] = b!!.getString("phone")!!
        map["Password"] = mPass!!.text
        map["mobile_id"] = driveId!!
        map["hx"] = "1"
        OkhttpTool.getOkhttpTool().post(UrlConfig.GET_USER_INFO, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val s = response.body()!!.string()
                if (s == "stop" || s == "fail" || s == "nodata") {

                } else {
                    try {
                        userMessageBean = Gson().fromJson(s, VipUserMessageBean::class.java)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
                try {
                    val jsonObject = JSONObject(s)
                    ShardTools.getInstance(this@RegisterTwoActivity).tempSaveSharedata("userid", jsonObject.getString("UserID"))
                    ShardTools.getInstance(this@RegisterTwoActivity).tempSaveSharedata("loginip", jsonObject.getString("CookieLoginIpt"))
                    ShardTools.getInstance(this@RegisterTwoActivity).tempSaveSharedata("logintoken", jsonObject.getString("CookieLoginToken"))
                    ShardTools.getInstance(this@RegisterTwoActivity).tempSaveSharedata("vip等级", jsonObject.getString("viptype"))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                val ms = Message()
                val b = Bundle()
                b.putString("s", s)
                ms.data = b
                ms.what = LOGIN_MESSAGE
                hd.sendMessage(ms)
            }
        })
        ld = LoadingDialog(this@RegisterTwoActivity).setMessage("登录中...")
        ld!!.show()
    }


}
