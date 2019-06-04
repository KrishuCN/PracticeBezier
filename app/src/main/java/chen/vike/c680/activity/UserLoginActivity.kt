package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.*
import butterknife.ButterKnife
import chen.vike.c680.tools.ShardTools
import chen.vike.c680.bean.VipUserMessageBean
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.wechat.friends.Wechat
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PhoneUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import chen.vike.c680.bean.UserInfoBean
import chen.vike.c680.Interface.TimeOutListener
import chen.vike.c680.main.BaseActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import com.lht.vike.a680_v1.R
import com.mob.MobSDK
import kotterknife.bindView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2018/3/2.
 * 登陆Activity
 */

class UserLoginActivity : BaseActivity(), View.OnClickListener {


    private val LOGIN_MESSAGE = 0x123
    private val NETWORK_EXCEPTION = 0X111
    private val REGISTER = 0x122
    private val QQ_LOGIN = 0x132
    private val WECHAT_LOGIN = 0x133
    private val GETYANZHENGMA = 0x124
    private val TIME = 0x113
    private val PHONELOGIN_MESSAGE = 0x125
    private val WECHAT_ERRO = 0X156
    val linearBar: LinearLayout by bindView(R.id.linear_bar)
    val fh: ImageView by bindView(R.id.fh)
    val loginTextZhuce: TextView by bindView(R.id.login_text_zhuce)
    val loginIv: ImageView by bindView(R.id.login_iv)
    val inputEditName: EditText by bindView(R.id.input_edit_name)
    val inputEditPsw: EditText by bindView(R.id.input_edit_psw)
    val zhanghaoLoginShow: LinearLayout by bindView(R.id.zhanghao_login_show)
    val inputEditPhone: EditText by bindView(R.id.input_edit_phone)
    val huoquYanzhengm: TextView by bindView(R.id.huoqu_yanzhengm)
    val inputEditYanzhengm: EditText by bindView(R.id.input_edit_yanzhengm)
    val phoneLoginShow: LinearLayout by bindView(R.id.phone_login_show)
    val loginWjmm: TextView by bindView(R.id.login_wjmm)
    val loginQiehuanYan: TextView by bindView(R.id.login_qiehuan_yan)
    val loginQiehuanZhang: TextView by bindView(R.id.login_qiehuan_zhang)
    val loginButton: Button by bindView(R.id.login_button)
    val loginQQ: Button by bindView(R.id.qq)
    val loginWechat: Button by bindView(R.id.wechat)
    private var isLoginFs = true
    private var mIntent: Intent? = null
    private var ld: LoadingDialog? = null
    private val map = HashMap<String, Any>()
    private var driveId: String? = null
    private var userMessageBean = VipUserMessageBean()//个人信息  登录信息
    lateinit var sp: SharedPreferences
    private var timer: Timer? = null
    private var currentTime = (60 * 1000).toLong()
    private val userPhone = ""
    var mHandler: MHandler = MHandler(this)

    class MHandler constructor(mActivity: UserLoginActivity) : Handler() {
        private var weakReference: WeakReference<UserLoginActivity> = WeakReference(mActivity)

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val userLoginActivity = weakReference.get()
            when (msg?.what) {
                userLoginActivity?.PHONELOGIN_MESSAGE -> {
                    val phones = msg?.data?.getString("s")
                    phones?.let {
                        when (it) {
                            "null_phone " -> {
                                CustomToast.showToast(userLoginActivity, "手机号不能为空", Toast.LENGTH_LONG)
                                userLoginActivity?.ld?.dismiss()
                            }
                            "null_yzm" -> {
                                CustomToast.showToast(userLoginActivity, "验证码不能为空", Toast.LENGTH_LONG)
                                userLoginActivity?.ld?.dismiss()
                            }
                            "stop" -> {
                                CustomToast.showToast(userLoginActivity, "账号被禁用", Toast.LENGTH_LONG)
                                userLoginActivity?.ld?.dismiss()
                            }
                            "fail" -> {
                                CustomToast.showToast(userLoginActivity, "登录失败（请检查账号或密码是否正确）", Toast.LENGTH_LONG)
                                userLoginActivity?.ld?.dismiss()
                            }
                            "nodata" -> {
                                CustomToast.showToast(userLoginActivity, "登录失败，账号无效", Toast.LENGTH_LONG)
                                userLoginActivity?.ld?.dismiss()
                            }
                            "erryzm" -> {
                                CustomToast.showToast(userLoginActivity, "验证码错误", Toast.LENGTH_LONG)
                                userLoginActivity?.ld?.dismiss()
                            }
                            "err_phone" -> {
                                CustomToast.showToast(userLoginActivity, "手机号无效", Toast.LENGTH_LONG)
                                userLoginActivity?.ld?.dismiss()
                            }
                            else -> {
                                LogUtils.e("phonelogin", phones)
                                try {
                                    MyApplication.userInfo = Gson().fromJson(phones, UserInfoBean::class.java)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }


                                // 环信登录
                                //                            EMClient.getInstance().login(MyApplication.userInfo.getUserID(), MyApplication.userInfo.getHuanxin_api_password(), new EMCallBack() {
                                //                                @Override
                                //                                public void onSuccess() {
                                //                                    //为了保证进入主页面后本地会话和群组都 load 完毕
                                //                                    EMClient.getInstance().chatManager().loadAllConversations();
                                //                                    EMClient.getInstance().groupManager().loadAllGroups();
                                //                                    LogUtils.d("=================登录成功!");
                                //                                }
                                //
                                //                                @Override
                                //                                public void onError(int code, String error) {
                                //                                    LogUtils.d("===================error:" + error);
                                //                                }
                                //
                                //                                @Override
                                //                                public void onProgress(int progress, String status) {
                                //                                    LogUtils.d("===================progress:" + progress + ",status：" + status);
                                //                                }
                                //                            });
                                // 保证每次SharedPreferences里的东西都是最新的，
                                // 上一次登录时填写的覆盖上上次写的数据
                                val editor = userLoginActivity?.sp?.edit()
                                editor?.putString("USER_NAME", MyApplication.userInfo!!.nickame)
                                editor?.putString("USERPHONE", userLoginActivity.inputEditPhone.text.toString())
                                editor?.putString("USERID", MyApplication.userInfo!!.userID)
                                editor?.putString("VKUSERIP", MyApplication.userInfo!!.cookieLoginIpt)
                                editor?.putString("VKTOKEN", MyApplication.userInfo!!.cookieLoginToken)
                                editor?.putString("GGUU", MyApplication.userInfo!!.isgguu)
                                editor?.putBoolean("ISLOGIN", true)
                                editor?.apply()
                                LhtTool.isLogin = true
                                LhtTool.isLoginPage = true
                                LhtTool.isFaLogin = "1"
                                userLoginActivity?.setResult(2)
                                userLoginActivity?.finish()
                            }
                        }

                    }

                }
                userLoginActivity?.GETYANZHENGMA -> {
                    val s = msg?.data?.getString("s")
                    when (s) {
                        "false" -> {
                            CustomToast.showToast(userLoginActivity, "发送失败", Toast.LENGTH_LONG)
                            userLoginActivity?.huoquYanzhengm?.isEnabled = true
                            userLoginActivity?.timer?.cancel()
                            userLoginActivity?.huoquYanzhengm?.text = "获取验证码"
                        }
                        "has" -> {
                            CustomToast.showToast(userLoginActivity, "发送失败", Toast.LENGTH_LONG)
                            userLoginActivity?.huoquYanzhengm?.isEnabled = true
                            userLoginActivity?.timer?.cancel()
                            userLoginActivity?.huoquYanzhengm?.text = "获取验证码"
                        }
                        "no_reg" -> {
                            CustomToast.showToast(userLoginActivity, "还未注册", Toast.LENGTH_LONG)
                            userLoginActivity?.huoquYanzhengm?.isEnabled = true
                            userLoginActivity?.timer?.cancel()
                            userLoginActivity?.huoquYanzhengm?.text = "获取验证码"
                        }
                        "stopcache" -> {
                            CustomToast.showToast(userLoginActivity, "发送失败", Toast.LENGTH_LONG)
                            userLoginActivity?.huoquYanzhengm?.isEnabled = true
                            userLoginActivity?.timer?.cancel()
                            userLoginActivity?.huoquYanzhengm?.text = "获取验证码"
                        }
                        else -> {
                            userLoginActivity?.map!!["yzmkey"] = s!!
                            CustomToast.showToast(userLoginActivity, "发送成功", Toast.LENGTH_LONG)
                        }
                    }
                }
                userLoginActivity?.TIME -> {
                    userLoginActivity!!.currentTime -= 1000
                    userLoginActivity?.huoquYanzhengm?.text = (userLoginActivity!!.currentTime / 1000).toString() + "秒后重新获取"
                    userLoginActivity.huoquYanzhengm.isEnabled = false
                    if (userLoginActivity.currentTime <= 0) {
                        userLoginActivity.currentTime = (60 * 1000).toLong()
                        userLoginActivity.timer!!.cancel()
                        userLoginActivity.huoquYanzhengm.text = "获取验证码"
                        userLoginActivity.huoquYanzhengm.isEnabled = true
                    }
                }
                userLoginActivity?.QQ_LOGIN -> {
                    val qqs = msg?.data?.getString("s")
                    when (qqs) {
                        "null_name" -> {
                            CustomToast.showToast(userLoginActivity, "账号不能为空", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "null_pwd" -> {
                            CustomToast.showToast(userLoginActivity, "密码不能为空", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "stop" -> {
                            CustomToast.showToast(userLoginActivity, "账号被禁用", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "fail" -> {
                            CustomToast.showToast(userLoginActivity, "登录失败（请检查账号或密码是否正确）", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "nodata" -> {
                            CustomToast.showToast(userLoginActivity, "登录失败，账号无效", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_nouserinfo" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录绑定的本网站的会员账号已无效", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_nobinduser" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录未绑定本网站的会员账号", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_nodata" -> {
                            CustomToast.showToast(userLoginActivity, "未传递QQ登录所需的openid", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_openid_err2" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录所需的openid错误", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_openid_err1" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录所需的openid无效", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_newfail" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录openid验证成功，系统处理异常，创建账号失败", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        else -> {
                            try {
                                MyApplication.userInfo = Gson().fromJson(qqs, UserInfoBean::class.java)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }


                            // 环信登录
                            //                            EMClient.getInstance().login(MyApplication.userInfo.getUserID(), MyApplication.userInfo.getHuanxin_api_password(), new EMCallBack() {
                            //                                @Override
                            //                                public void onSuccess() {
                            //                                    //为了保证进入主页面后本地会话和群组都 load 完毕
                            //                                    EMClient.getInstance().chatManager().loadAllConversations();
                            //                                    EMClient.getInstance().groupManager().loadAllGroups();
                            //                                    LogUtils.d("=================登录成功!");
                            //                                }
                            //
                            //                                @Override
                            //                                public void onError(int code, String error) {
                            //                                    LogUtils.d("===================error:" + error);
                            //                                }
                            //
                            //                                @Override
                            //                                public void onProgress(int progress, String status) {
                            //                                    LogUtils.d("===================progress:" + progress + ",status：" + status);
                            //                                }
                            //                            });
                            val editor = userLoginActivity?.sp?.edit()
                            editor?.putString("USER_NAME", MyApplication.userInfo!!.nickame)
                            editor?.putString("PASSWORD", userLoginActivity.inputEditPsw.text.toString())
                            editor?.putString("USER", userLoginActivity.inputEditName.text.toString())
                            editor?.putString("USERID", MyApplication.userInfo!!.userID)
                            editor?.putString("VKUSERIP", MyApplication.userInfo!!.cookieLoginIpt)
                            editor?.putString("VKTOKEN", MyApplication.userInfo!!.cookieLoginToken)
                            editor?.putString("GGUU", MyApplication.userInfo!!.isgguu)
                            editor?.putBoolean("ISLOGIN", true)
                            editor?.apply()
                            LhtTool.isLogin = true
                            LhtTool.isLoginPage = true
                            userLoginActivity?.setResult(2)
                            userLoginActivity?.finish()
                        }
                    }
                }
                userLoginActivity?.WECHAT_LOGIN -> {
                    val wechats = msg?.data?.getString("s")
                    when (wechats) {
                        "weixin_nouserinfo" -> {
                            CustomToast.showToast(userLoginActivity, "微信登录绑定的本网站的会员账号已无效", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "weixin_openid_err2" -> {
                            CustomToast.showToast(userLoginActivity, "微信登录所需的openid错误", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "weixin_openid_err1" -> {
                            CustomToast.showToast(userLoginActivity, "微信登录所需的openid无效", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "weixin_nodata" -> {
                            CustomToast.showToast(userLoginActivity, "未传递微信登录所需的openid", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "weixin_newfail" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录openid验证成功，系统处理异常，创建账号失败", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        else -> {
                            try {
                                MyApplication.userInfo = Gson().fromJson(wechats, UserInfoBean::class.java)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            // 环信登录

                            val editor = userLoginActivity?.sp?.edit()
                            editor?.putString("USER_NAME", MyApplication.userInfo!!.nickame)
                            editor?.putString("PASSWORD", userLoginActivity.inputEditPsw.text.toString())
                            editor?.putString("USER", userLoginActivity.inputEditName.text.toString())
                            editor?.putString("USERID", MyApplication.userInfo!!.userID)
                            editor?.putString("VKUSERIP", MyApplication.userInfo!!.cookieLoginIpt)
                            editor?.putString("VKTOKEN", MyApplication.userInfo!!.cookieLoginToken)
                            editor?.putString("GGUU", MyApplication.userInfo!!.isgguu)
                            editor?.putBoolean("ISLOGIN", true)
                            editor?.apply()
                            LhtTool.isLogin = true
                            LhtTool.isLoginPage = true
                            userLoginActivity?.setResult(2)
                            userLoginActivity?.finish()
                        }
                    }
                }
                userLoginActivity?.WECHAT_ERRO -> {
                    CustomToast.showToast(userLoginActivity, "请安装微信客户端", Toast.LENGTH_LONG)
                    userLoginActivity?.ld?.dismiss()
                }
                userLoginActivity?.LOGIN_MESSAGE -> {
                    val zhs = msg?.data?.getString("s")
                    when (zhs) {
                        "null_name" -> {
                            CustomToast.showToast(userLoginActivity, "账号不能为空", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "null_pwd" -> {
                            CustomToast.showToast(userLoginActivity, "密码不能为空", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "stop" -> {
                            CustomToast.showToast(userLoginActivity, "账号被禁用", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "fail" -> {
                            CustomToast.showToast(userLoginActivity, "登录失败（请检查账号或密码是否正确）", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "nodata" -> {
                            CustomToast.showToast(userLoginActivity, "登录失败，账号无效", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_nouserinfo" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录绑定的本网站的会员账号已无效", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_nobinduser" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录未绑定本网站的会员账号", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_nodata" -> {
                            CustomToast.showToast(userLoginActivity, "未传递QQ登录所需的openid", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_openid_err2" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录所需的openid错误", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_openid_err1" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录所需的openid无效", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        "qq_newfail" -> {
                            CustomToast.showToast(userLoginActivity, "QQ登录openid验证成功，系统处理异常，创建账号失败", Toast.LENGTH_LONG)
                            userLoginActivity?.ld?.dismiss()
                        }
                        else -> {
                            try {
                                MyApplication.userInfo = Gson().fromJson(zhs, UserInfoBean::class.java)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }


                            // 环信登录
                            //                            EMClient.getInstance().login(MyApplication.userInfo.getUserID(), MyApplication.userInfo.getHuanxin_api_password(), new EMCallBack() {
                            //                                @Override
                            //                                public void onSuccess() {
                            //                                    //为了保证进入主页面后本地会话和群组都 load 完毕
                            //                                    EMClient.getInstance().chatManager().loadAllConversations();
                            //                                    EMClient.getInstance().groupManager().loadAllGroups();
                            //                                    LogUtils.d("=================登录成功!");
                            //                                }
                            //
                            //                                @Override
                            //                                public void onError(int code, String error) {
                            //                                    LogUtils.d("===================error:" + error);
                            //                                }
                            //
                            //                                @Override
                            //                                public void onProgress(int progress, String status) {
                            //                                    LogUtils.d("===================progress:" + progress + ",status：" + status);
                            //                                }
                            //                            });
                            // 保证每次SharedPreferences里的东西都是最新的，
                            // 上一次登录时填写的覆盖上上次写的数据
                            val editor = userLoginActivity?.sp?.edit()
                            editor?.putString("USER_NAME", MyApplication.userInfo!!.nickame)
                            editor?.putString("PASSWORD", userLoginActivity.inputEditPsw.text.toString())
                            editor?.putString("USER", userLoginActivity.inputEditName.text.toString())
                            editor?.putString("USERID", MyApplication.userInfo!!.userID)
                            editor?.putString("VKUSERIP", MyApplication.userInfo!!.cookieLoginIpt)
                            editor?.putString("VKTOKEN", MyApplication.userInfo!!.cookieLoginToken)
                            editor?.putString("GGUU", MyApplication.userInfo!!.isgguu)
                            editor?.putBoolean("ISLOGIN", true)
                            editor?.apply()
                            LhtTool.isLogin = true
                            LhtTool.isLoginPage = true
                            userLoginActivity?.setResult(2)
                            userLoginActivity?.finish()
                        }
                    }
                }
                userLoginActivity?.NETWORK_EXCEPTION -> LhtTool.showNetworkException(userLoginActivity, msg)
                else -> {
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)
        ButterKnife.bind(this)
        driveId = if (XXPermissions.isHasPermission(this@UserLoginActivity,Permission.READ_PHONE_STATE)){
            PhoneUtils.getDeviceId()
        }else{
            "00"
        }
        sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        val name = sp.getString("USER_NAME", "")
        val pass = sp.getString("PASSWORD", "")
        val phoneNumber = sp.getString("USERPHONE", "")
        inputEditName.setText(name)
        inputEditName.setSelection(inputEditName.text.length)
        inputEditPhone.setText(phoneNumber)
        initView()
    }

    override fun onResume() {
        if (!LhtTool.isLogin) {
            if (!LhtTool.isLoginFa) {
                zhanghaoLoginShow.visibility = View.GONE
                phoneLoginShow.visibility = View.VISIBLE
                loginQiehuanYan.visibility = View.VISIBLE
                loginQiehuanZhang.visibility = View.GONE
                isLoginFs = false
                LhtTool.isLoginFa = true
            }
        }
        super.onResume()
    }

    fun initView(): Unit {
        fh.setOnClickListener(this)
        loginTextZhuce.setOnClickListener(this)
        loginQiehuanYan.setOnClickListener(this)
        loginQiehuanZhang.setOnClickListener(this)
        loginIv.setOnClickListener(this)
        inputEditName.setOnClickListener(this)
        inputEditPsw.setOnClickListener(this)
        inputEditPhone.setOnClickListener(this)
        huoquYanzhengm.setOnClickListener(this)
        inputEditYanzhengm.setOnClickListener(this)
        loginWjmm.setOnClickListener(this)
        loginButton.setOnClickListener(this)
        loginQQ.setOnClickListener(this)
        loginWechat.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fh -> finish()
            R.id.login_text_zhuce -> {
                startActivityForResult(Intent(this, RegisterOneActivity::class.java), REGISTER)
                finish()
            }
            R.id.login_iv -> {
            }
            R.id.login_qiehuan_yan -> {
                zhanghaoLoginShow.visibility = View.VISIBLE
                phoneLoginShow.visibility = View.GONE
                loginQiehuanYan.visibility = View.GONE
                loginQiehuanZhang.visibility = View.VISIBLE
                isLoginFs = true
            }
            R.id.login_qiehuan_zhang -> {
                zhanghaoLoginShow.visibility = View.GONE
                phoneLoginShow.visibility = View.VISIBLE
                loginQiehuanYan.visibility = View.VISIBLE
                loginQiehuanZhang.visibility = View.GONE
                isLoginFs = false
            }
            R.id.input_edit_name -> {
            }
            R.id.input_edit_psw -> {
            }
            R.id.input_edit_phone -> {
            }
            R.id.huoqu_yanzhengm -> huoquYan()
            R.id.input_edit_yanzhengm -> {
            }
            R.id.login_wjmm -> {
                mIntent = Intent(this, ForgetActivity::class.java)
                startActivity(mIntent)
            }
            R.id.login_button -> if (isLoginFs) {
                zhangPswLogin()
            } else {
                phoneYanLogin()
            }
            R.id.qq -> qqLogin()
            R.id.wechat -> wechat()
        }
    }

    /**
     * 获取验证码
     */
    private fun huoquYan() {
        huoquYanzhengm.isEnabled = false

        if (inputEditPhone.text.toString().length == 11) {
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    mHandler.sendEmptyMessage(TIME)
                }
            }, 0, 1000)
            map["mobile"] = inputEditPhone!!.text
            map["key"] = UrlConfig.MOBILE_VERIFICATION_KEY
            map["type"] = "verify"
            map["check"] = "2"
            map["android"] = "1"
            OkhttpTool.getOkhttpTool().post(UrlConfig.MOBILE_VERIFICATION, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LhtTool.sendMessage(mHandler, e, NETWORK_EXCEPTION)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val s = response.body()!!.string()
                        Log.e("yanzhengma", s)
                        val ms = Message()
                        ms.what = GETYANZHENGMA
                        val b = Bundle()
                        b.putString("s", s)
                        ms.data = b
                        mHandler.sendMessage(ms)
                    }catch (e:Exception){
                        LhtTool.sendMessage(mHandler,e,NETWORK_EXCEPTION)
                    }
                }
            })
        } else {
            CustomToast.showToast(this, "请输入正确的手机号码", Toast.LENGTH_LONG)
            huoquYanzhengm.isEnabled = true
        }
    }

    /**
     * 手机验证码登录
     */
    private fun phoneYanLogin() {
        if (inputEditPhone.text.toString().isEmpty()) {
            CustomToast.showToast(this, "请输入正确的手机号码", Toast.LENGTH_LONG)

        } else {
            if (inputEditYanzhengm.text.toString().isEmpty()) {
                CustomToast.showToast(this, "验证码不能为空", Toast.LENGTH_LONG)

            } else {
                map["phone"] = inputEditPhone.text
                map["yzm"] = inputEditYanzhengm.text
                map["mobile_id"] = driveId?:"00"

                showLoginWaitingDialog()

                OkhttpTool.getOkhttpTool().post(UrlConfig.LOGIN_PHONE, map, object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        LhtTool.sendMessage(mHandler, e, NETWORK_EXCEPTION)

                    }

                    override fun onResponse(call: Call, response: Response) {
                     try {
                         val s = response.body()?.string()
                         if (!StringUtils.isEmpty(s)) {
                             Log.e("phone", s)
                             val ms = Message()
                             ms.what = PHONELOGIN_MESSAGE
                             val b = Bundle()
                             b.putString("s", s)
                             ms.data = b
                             mHandler.sendMessage(ms)
                         }
                     }catch (e:Exception){
                         LhtTool.sendMessage(mHandler,e,NETWORK_EXCEPTION)
                     }
                    }
                })
            }
        }
    }



    /**
     * 账号密码登陆
     */
    private fun zhangPswLogin() {
        run {

            if (inputEditName.text.toString().isEmpty()) {
                CustomToast.showToast(this, "账号不能为空~", Toast.LENGTH_LONG)
            } else {
                if (inputEditPsw.text.toString().isEmpty()) {
                    CustomToast.showToast(this, "密码不能为空~", Toast.LENGTH_LONG)
                } else {
                    map["UserId"] = inputEditName.text
                    map["Password"] = inputEditPsw.text
                    map["mobile_id"] = driveId?:"00"
                    map["hx"] = "1"

                    showLoginWaitingDialog()

                    OkhttpTool.getOkhttpTool().post(UrlConfig.GET_USER_INFO, map, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            LhtTool.sendMessage(mHandler, e, NETWORK_EXCEPTION)
                        }


                        override fun onResponse(call: Call, response: Response) {

                            var s: String? = response.body()!!.string()
                            Log.e("loginmessage", s!! + "")
                            if (s == "stop" || s == "fail" || s == "nodata") {

                            } else {
                                try {
                                    userMessageBean = Gson().fromJson(s, VipUserMessageBean::class.java)
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }
                            try {
                                if (s.startsWith("\ufeff")) {
                                    s = s.substring(1)
                                }
                                val jsonObject = JSONObject(s)
                                ShardTools.getInstance(this@UserLoginActivity).tempSaveSharedata("userid", jsonObject.getString("UserID"))
                                ShardTools.getInstance(this@UserLoginActivity).tempSaveSharedata("loginip", jsonObject.getString("CookieLoginIpt"))
                                ShardTools.getInstance(this@UserLoginActivity).tempSaveSharedata("logintoken", jsonObject.getString("CookieLoginToken"))
                                ShardTools.getInstance(this@UserLoginActivity).tempSaveSharedata("vip等级", jsonObject.getString("viptype"))

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            val ms = Message()
                            val b = Bundle()
                            b.putString("s", s)
                            ms.data = b
                            ms.what = LOGIN_MESSAGE
                            mHandler.sendMessage(ms)
                        }
                    })
                }
            }

        }

    }

    /**
     * 微信登录
     */
    private fun wechat() {

        val weChat = ShareSDK.getPlatform(Wechat.NAME)
        if (weChat.isAuthValid) {   //判断是否已经存在授权
            weChat.removeAccount(true)  //移除授权状态和本地缓存，下次会重新授权
        }
        if (weChat.isClientValid) {
            //判断是否存在授权凭条的客户端
        }
        weChat.SSOSetting(false)
        weChat.platformActionListener = object : PlatformActionListener {
            override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
                runOnUiThread {
                    platform.db.exportData()
                    val openId = weChat.db.userId
                    val accessToken = weChat.db.token
                    Log.e("wechat", openId + "")
                    Log.e("wechat", accessToken + "")
                    map["openid"] = openId
                    map["access_token"] = accessToken
                    map["mobile_id"] = LhtTool.getHouse_CID(this@UserLoginActivity)!!
                    map["hx"] = "1"

                    showLoginWaitingDialog()

                    OkhttpTool.getOkhttpTool().post(UrlConfig.WECHAT_LOGIN, map, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            LhtTool.sendMessage(mHandler, e, NETWORK_EXCEPTION)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            try {
                                val s = response.body()!!.string()
                                LogUtils.d("wechat", s)
                                val ms = Message()
                                ms.what = WECHAT_LOGIN
                                val b = Bundle()
                                b.putString("s", s)
                                ms.data = b
                                mHandler.sendMessage(ms)
                            }catch (e:Exception){
                                LhtTool.sendMessage(mHandler,e,NETWORK_EXCEPTION)
                            }
                        }
                    })
                }
            }

            override fun onError(platform: Platform, i: Int, throwable: Throwable) {
                LogUtils.e("wechat", throwable.toString() + "")
                weChat.removeAccount(true)
                mHandler.sendEmptyMessage(WECHAT_ERRO)
            }

            override fun onCancel(platform: Platform, i: Int) {

            }
        }
        weChat.showUser(null)//要数据不要功能，主要体现在不会重复出现授权界面
        // wechat.authorize(); //要功能不要数据、

    }

    /**
     * qq快速登录
     */
    private fun qqLogin() {
        ld = LoadingDialog(this@UserLoginActivity).setMessage("跳转中")
        ld?.show()
        MobSDK.init(this@UserLoginActivity)
        val qq = ShareSDK.getPlatform("QQ")
        qq.platformActionListener = object : PlatformActionListener {
            override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {

                runOnUiThread {
                    LogUtils.d("=================platform:$platform")
                    LogUtils.d("=================hashMap:$hashMap")
                    platform.db.exportData()
                    val openId = qq.db.userId
                    val accessToken = qq.db.token
                    LogUtils.d("==============openId:$openId")
                    LogUtils.d("==============accessToken:$accessToken")
                    LogUtils.d("==============hashMap:$hashMap")
                    map["openid"] = openId
                    map["access_token"] = accessToken
                    map["mobile_id"] = LhtTool.getHouse_CID(this@UserLoginActivity)!!
                    map["hx"] = "1"

                    showLoginWaitingDialog()

                    OkhttpTool.getOkhttpTool().post(UrlConfig.QQ_LOGIN, map, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            LhtTool.sendMessage(mHandler, e, NETWORK_EXCEPTION)
                        }


                        override fun onResponse(call: Call, response: Response) {
                            try {
                                val s = response.body()!!.string()
                                LogUtils.d("================response:$s")
                                val ms = Message()
                                ms.what = QQ_LOGIN
                                val b = Bundle()
                                b.putString("s", s)
                                ms.data = b
                                mHandler.sendMessage(ms)
                            }catch (e:Exception){
                                LhtTool.sendMessage(mHandler,e,NETWORK_EXCEPTION)
                            }
                        }
                    })
                }
            }

            override fun onError(platform: Platform, i: Int, throwable: Throwable) {

                LogUtils.d("============throwable:$throwable")

            }

            override fun onCancel(platform: Platform, i: Int) {

            }
        }
        qq.showUser(null)
    }

    private fun showLoginWaitingDialog() {
        ld = LoadingDialog(this).setMessage("登录中...")
        ld?.setOnTimeOut(10, object : TimeOutListener {
            override fun timeOut() {
                ld?.dismiss()
                ToastUtils.showShort("网络不佳请稍后重试")
            }
        })
        ld?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        mHandler.removeCallbacksAndMessages(null)
        ld?.dismiss()
    }
}
