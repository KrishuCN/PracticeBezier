package chen.vike.c680.activity


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import chen.vike.c680.tools.ACache
import chen.vike.c680.tools.ConfigTools
import chen.vike.c680.tools.ShardTools
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import chen.vike.c680.bean.UserInfoBean
import chen.vike.c680.bean.VersionGson
import chen.vike.c680.bean.YanZhengZTGson
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import com.lht.vike.a680_v1.R
import kotterknife.bindView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*


/**
 * Created by lht on 2017/3/6.
 * 设置页面
 */

class SheZhiActivity : BaseStatusBarActivity() {

    val lv: ListView by bindView(R.id.lv)
    val bt: Button by bindView(R.id.bt)
    val gguuLoginPws: EditText by bindView(R.id.gguu_login_pws)
    val gguuLoginId: EditText by bindView(R.id.gguu_login_id)
    val gguuQueding: Button by bindView(R.id.gguu_queding)
    val gguuLayout: LinearLayout by bindView(R.id.gguu_layout)
    val btnGguuShow: Button by bindView(R.id.btn_gguu_show)
    private val name = arrayOf("实名认证", "邮箱认证", "绑定手机", "绑定银行卡", "修改密码", "检查更新")
    private val zhuang = arrayOf("未认证", "未认证", "已绑定", "未绑定", "", "")
    private val map = HashMap<String, Any>()
    private val map1 = HashMap<String, Any>()
    private val PERSONAL_EXIT = 0x123
    private val NETWORK_EXCEPTION = 0X111
    private val VERSION_UPDATE = 0X121
    private val GET_YANZHENGZHUANGTAI = 0X112
    private var `in`: Intent? = null
    private var versionGson: VersionGson? = null
    private var yanZhengZTGson: YanZhengZTGson? = null
    private var adapter: BaseAdapter? = null
    private var ld: LoadingDialog? = null
    private var appVersion: Int = 0
    private var appName: String? = null
    private var isGetYanzhengInfo: Boolean = false
    private var context: Context? = null
    lateinit var sp: SharedPreferences
    lateinit var aCache: ACache

    /**
     * gguu切换用户
     */
    private val guMap = HashMap<String, Any>()
    private var gguups = ""
    private var gguuid = ""
    private val LOGIN_MESSAGE = 0x145

    @SuppressLint("HandlerLeak")
    private val hd = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            ld?.dismiss()
            if (msg.what == LOGIN_MESSAGE) {
                val gguumsg = msg.data.getString("s")
                when (gguumsg) {
                    "no" -> CustomToast.showToast(this@SheZhiActivity, "密码不能为空", Toast.LENGTH_SHORT)
                    "nocfg" -> CustomToast.showToast(this@SheZhiActivity, "无效登录密码", Toast.LENGTH_SHORT)
                    "pwderr" -> CustomToast.showToast(this@SheZhiActivity, "密码错误", Toast.LENGTH_SHORT)
                    "nouid" -> CustomToast.showToast(this@SheZhiActivity, "无效用户ID", Toast.LENGTH_SHORT)
                    "uiderr" -> CustomToast.showToast(this@SheZhiActivity, "登录失败", Toast.LENGTH_SHORT)
                    else -> {
                        try {
                            MyApplication.userInfo = Gson().fromJson(gguumsg, UserInfoBean::class.java)
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
                        editor.putString("USERID", MyApplication.userInfo!!.userID)
                        editor.putString("VKUSERIP", MyApplication.userInfo!!.cookieLoginIpt)
                        editor.putString("VKTOKEN", MyApplication.userInfo!!.cookieLoginToken)
                        //                           ShardTools.getInstance(UserLoginActivity.this).tempSaveSharedata("userid", jsonObject.getString("UserID"));
                        //                           ShardTools.getInstance(UserLoginActivity.this).tempSaveSharedata("loginip", jsonObject.getString("CookieLoginIpt"));
                        //                           ShardTools.getInstance(UserLoginActivity.this).tempSaveSharedata("logintoken", jsonObject.getString("CookieLoginToken"));
                        ShardTools.getInstance(this@SheZhiActivity).tempSaveSharedata("vip等级", MyApplication.userInfo!!.viptype)
                        if (MyApplication.userInfo!!.isgguu == "1") {
                            editor.putString("GGUU", MyApplication.userInfo!!.isgguu)
                        }
                        editor.putBoolean("ISLOGIN", true)
                        editor.commit()
                        LhtTool.isLogin = true
                        LhtTool.isLoginPage = true
                        setResult(2)
                        finish()
                    }
                }
            } else if (msg.what == PERSONAL_EXIT) {

                //此处添加三方框架退出
                LhtTool.isLogin = false
                //                EMClient.getInstance().logout(true);
                MyApplication.userInfo = null
                val editor = sp.edit()
                editor.putBoolean("ISLOGIN", false)
                aCache.remove("userMessage")
                editor.commit()
                setResult(2)
                ShardTools.getInstance(context).tempSaveSharedata("vip等级", "0")
                ConfigTools.VIPMARK = 0
                LhtTool.isLoginPage = true
                finish()

            } else if (msg.what == NETWORK_EXCEPTION) {
                CustomToast.showToast(this@SheZhiActivity, "网络异常，请重试", Toast.LENGTH_SHORT)
                LhtTool.showNetworkException(this@SheZhiActivity, msg)
                this@SheZhiActivity.finish()
            } else if (msg.what == VERSION_UPDATE) {
                //本来是大于才会显示，但这会开发时每次都会弹出很麻烦，所以直接隐藏了，发布时会改回来的
                Log.e("version", "" + versionGson!!.version + "++++" + appVersion)
                if (Integer.valueOf(versionGson!!.version) > appVersion) {
                    LhtTool.createUPdateDilog(this@SheZhiActivity, LhtTool.getClientVersionName(this@SheZhiActivity), versionGson!!.versionName, versionGson!!.versionDes, versionGson!!.url)
                            .show()
                } else {
                    CustomToast.showToast(this@SheZhiActivity, "已经是最新版本了~", Toast.LENGTH_SHORT)
                }
            } else if (msg.what == GET_YANZHENGZHUANGTAI) {

                if (null != yanZhengZTGson) {
                    isGetYanzhengInfo = true
                    when {
                        yanZhengZTGson!!.v_fullname == "1" -> zhuang[0] = "已认证"
                        yanZhengZTGson!!.v_fullname == "2" -> zhuang[0] = "正在审核"
                        else -> zhuang[0] = "未认证"
                    }
                    if (yanZhengZTGson!!.v_email == "1") {
                        zhuang[1] = "已认证"
                    } else {
                        zhuang[1] = "未认证"
                    }
                    if (yanZhengZTGson!!.v_bankinfo == "1") {
                        zhuang[3] = "已绑定"
                    } else {
                        zhuang[3] = "未绑定"
                    }
                    if (yanZhengZTGson!!.v_phone == "1") {
                        zhuang[2] = "已绑定"
                    } else {
                        zhuang[2] = "未绑定"
                    }
                    zhuang[5] = "当前版本" + appName!!
                    adapter!!.notifyDataSetChanged()
//                    adapter!!.notifyDataSetInvalidated()
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shezhi_activity)
        aCache = ACache.get(this)
        title.text = "设置"
        context = this
        sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        val isgguu = sp.getString("GGUU", "")
        if (isgguu == "1") {
            //gguuLayout.setVisibility(View.VISIBLE);
            btnGguuShow!!.visibility = View.VISIBLE
        }
        gguuQueding!!.setOnClickListener { gguuLogin() }

        btnGguuShow!!.setOnClickListener {
            gguuLayout!!.visibility = View.VISIBLE
            btnGguuShow!!.visibility = View.GONE
        }
        appVersion = LhtTool.getAppVersion(this@SheZhiActivity)
        appName = LhtTool.getClientVersionName(this@SheZhiActivity)
        initZhuangtai()
        ld = LoadingDialog(this@SheZhiActivity).setMessage("加载中..")
        ld!!.show()
        adapter = object : BaseAdapter() {

            override fun getCount(): Int {
                return name.size
            }

            override fun getItem(position: Int): Any {
                return name[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, view: View?, parent: ViewGroup): View {
                var v: View? = null
                var mVH: ID? = null
                if (v == null) {
                    v = LayoutInflater.from(this@SheZhiActivity).inflate(R.layout.shezhi_listview_item, null)
                    mVH = ID()
                    mVH.tv = v!!.findViewById(R.id.mingzi)
                    mVH.lin = v.findViewById(R.id.shezhi_fenge)
                    mVH.tvZhuang = v.findViewById(R.id.shezhi_zhuangtai)
                    v.tag = mVH
                } else {
                    v = view
                    mVH = v!!.tag as ID
                }

                mVH.tv!!.text = name[position]
                mVH.tvZhuang!!.text = zhuang[position]
                if (position % 2 == 0) {
                    mVH.lin!!.visibility = View.VISIBLE
                } else {
                    mVH.lin!!.visibility = View.GONE
                }
                return v
            }

            inner class ID {
                var tv: TextView? = null
                var lin: TextView? = null
                var tvZhuang: TextView? = null
            }

        }
        lv.adapter = adapter

        bt.setOnClickListener {
            //                退出登录
            map["userid"] = MyApplication.userInfo!!.userID
            OkhttpTool.getOkhttpTool().post(UrlConfig.PERSONAL_EXIT, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {

                    val s = response.body()!!.string()
                    LogUtils.d("================Response:$s")
                    hd.sendEmptyMessage(PERSONAL_EXIT)

                }
            })
            ld = LoadingDialog(this@SheZhiActivity).setMessage("退出中")
            ld!!.show()
        }


        lv.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {

                0 -> when {
                    yanZhengZTGson!!.v_fullname == "1" -> CustomToast.showToast(this@SheZhiActivity, "已认证", Toast.LENGTH_SHORT)
                    yanZhengZTGson!!.v_fullname == "2" -> CustomToast.showToast(this@SheZhiActivity, "正在审核中", Toast.LENGTH_LONG)
                    else -> {
                        if (ActivityUtils.isActivityExists(AppUtils.getAppPackageName(), SheZhiActivity::class.java.name)) {
                            `in` = Intent(this@SheZhiActivity, ShiMingRenZhengActivity::class.java)
                            startActivity(`in`)
                        }
                    }
                }

                1 ->

                    if (yanZhengZTGson!!.v_email == "1") {
                        Toast.makeText(this@SheZhiActivity, "已认证", Toast.LENGTH_LONG).show()
                    } else {
                        if (ActivityUtils.isActivityExists(AppUtils.getAppPackageName(), SheZhiActivity::class.java.name)) {
                            `in` = Intent(this@SheZhiActivity, EmailActivity::class.java)
                            startActivity(`in`)
                        }
                    }

                2 ->

                    if (yanZhengZTGson!!.v_phone == "1") {
                        Toast.makeText(this@SheZhiActivity, "已绑定", Toast.LENGTH_LONG).show()
                    } else {
                        if (ActivityUtils.isActivityExists(AppUtils.getAppPackageName(), SheZhiActivity::class.java.name)) {
                            `in` = Intent(this@SheZhiActivity, MobileYanZhengActivity::class.java)
                            startActivity(`in`)
                        }
                    }

                3 ->

                    if (yanZhengZTGson!!.v_bankinfo == "1") {
                        Toast.makeText(this@SheZhiActivity, "已绑定", Toast.LENGTH_LONG).show()
                    } else {
                        if (yanZhengZTGson!!.v_email == "1") {
                            if (yanZhengZTGson!!.v_fullname == "1") {

                                if (ActivityUtils.isActivityExists(AppUtils.getAppPackageName(), SheZhiActivity::class.java.name)) {
                                    `in` = Intent(this@SheZhiActivity, BankActivity::class.java)
                                    startActivity(`in`)
                                }

                            } else if (yanZhengZTGson!!.v_fullname == "2") {
                                CustomToast.showToast(this@SheZhiActivity, "请先等待审核，在绑定银行卡", Toast.LENGTH_LONG)
                            } else {
                                CustomToast.showToast(this@SheZhiActivity, "请先进行实名认证", Toast.LENGTH_LONG)
                            }
                        } else {
                            CustomToast.showToast(this@SheZhiActivity, "请先验证邮箱", Toast.LENGTH_LONG)
                        }
                    }
                4 -> {

                    if (ActivityUtils.isActivityExists(AppUtils.getAppPackageName(), SheZhiActivity::class.java.name)) {
                        `in` = Intent(this@SheZhiActivity, ChangePasswordActivity::class.java)
                        startActivity(`in`)
                    }
                }

                5 -> {
                    ld = LoadingDialog(this@SheZhiActivity).setMessage("最新版本获取中...")
                    ld!!.show()
                    init_is_update()
                }
            }
        }


    }


    private fun init_is_update() {
        OkhttpTool.getOkhttpTool()[UrlConfig.GET_VERSION, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }


            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d("==================response:$s")
                    versionGson = Gson().fromJson(s, VersionGson::class.java)
                    hd.sendEmptyMessage(VERSION_UPDATE)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }]
    }

    private fun initZhuangtai() {
        map1["userid"] = MyApplication.userInfo!!.userID
        OkhttpTool.getOkhttpTool().post(UrlConfig.USERINFO_ZHUANGTAI, map1, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }


            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    Log.e("zhuangtai", s)
                    yanZhengZTGson = Gson().fromJson(s, YanZhengZTGson::class.java)
                    if (yanZhengZTGson != null) {
                        hd.sendEmptyMessage(GET_YANZHENGZHUANGTAI)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }



            }
        })
    }

    private fun gguuLogin() {
        gguups = gguuLoginPws!!.text.toString() + ""
        gguuid = gguuLoginId!!.text.toString() + ""
        if (gguups == "") {
            CustomToast.showToast(this@SheZhiActivity, "请输入密码~", Toast.LENGTH_SHORT)
        } else if (gguuid == "") {
            CustomToast.showToast(this@SheZhiActivity, "ID不能为空", Toast.LENGTH_SHORT)
        } else {
            guMap["pwd"] = gguups
            guMap["uid"] = gguuid
            OkhttpTool.getOkhttpTool().post(GGUUURL, guMap, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val s = response.body()!!.string()
                    Log.e("qiehuan", s)
                    val ms = Message()
                    ms.what = LOGIN_MESSAGE
                    val b = Bundle()
                    b.putString("s", s)
                    ms.data = b
                    hd.sendMessage(ms)

                }
            })
        }
    }

    companion object {
        private const val GGUUURL = "http://app.680.com/api/v5/gguulogin.ashx"
    }


}
