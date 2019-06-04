package chen.vike.c680.main

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import chen.vike.c680.WeiKe.WeiKeFragment
import chen.vike.c680.activity.*
import chen.vike.c680.bean.UserInfoBean
import chen.vike.c680.bean.VersionGson
import chen.vike.c680.broadcast.NetReceiver
import chen.vike.c680.fragment.ZuoPinFragment
import chen.vike.c680.fragment.GuZhuNewFragment
import chen.vike.c680.fragment.PersonalFragment
import chen.vike.c680.service.UploadService
import chen.vike.c680.tools.*
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.NoScrollViewPager
import chen.vike.c680.webview.WebViewActivity
import com.baidu.mobstat.StatService
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.lht.vike.a680_v1.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*


class MainActivity : BaseActivity() {

    private var noScrollViewPager: NoScrollViewPager? = null
    private var radioGroup: RadioGroup? = null
    private val fraList = ArrayList<Fragment>()
    private var exitTime: Long = 0
    private val VERSION_UPDATE = 0X123
    private val NETWORK_EXCEPTION = 0X111
    private val AUTO_LOGIN = 0X121
    private val ALL_UNREADNUM = 0X122
    private var versionGson: VersionGson? = null
    private var appVersion: Int = 0
    private val map = HashMap<String, Any>()
    private val map1 = HashMap<String, Any>()
    private val metrics = DisplayMetrics()
    private var sp: SharedPreferences? = null
    private var driveId: String? = null
    private var fabu_bt: TextView? = null
    private val mPopupWindow: PopupWindow? = null
    private var ll: LinearLayout? = null
    private lateinit var mIntent: Intent
    private var context: Context? = null
    private var dataStatisticsBean = DataStatisticsBean()
    private val dataList = ArrayList<DataStatisticsBean>()
    private var netReceiver: NetReceiver? = null

    /**
     * 存储页面数据
     */
    private var userid = ""
    private var pagename = ""
    private var pageindex = ""
    private var pageinittime = ""


    /**
     * handler 更改主线程
     */
    @SuppressLint("HandlerLeak")
    private val hd = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == VERSION_UPDATE) {

                if (Integer.valueOf(versionGson?.version!!) > appVersion) {
                    LhtTool.createUPdateDilog(this@MainActivity, LhtTool.getClientVersionName(this@MainActivity),
                            versionGson?.versionName, versionGson?.versionDes, versionGson?.url).show()
                }
            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(this@MainActivity, msg)
            } else if (msg.what == AUTO_LOGIN) {

                val s = msg.data.getString("s")
                LogUtils.d("userLogin : \n $s")
                when (s) {
                    "null_userid" -> CustomToast.showToast(this@MainActivity, "自动登录失败", Toast.LENGTH_SHORT)
                    "null_vkuserip" -> CustomToast.showToast(this@MainActivity, "自动登录失败", Toast.LENGTH_SHORT)
                    "null_vktoken" -> CustomToast.showToast(this@MainActivity, "自动登录失败", Toast.LENGTH_SHORT)
                    "fail" -> CustomToast.showToast(this@MainActivity, "自动登录失败", Toast.LENGTH_SHORT)
                    "nodata" -> CustomToast.showToast(this@MainActivity, "自动登录失败", Toast.LENGTH_SHORT)
                    else -> {
                        try {
                            MyApplication.userInfo = Gson().fromJson(s, UserInfoBean::class.java)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }


                        MyApplication.userInfo?.let {
                            LhtTool.isLoginPage = true
                            LogUtils.d("userLogin : \n $s")
                            // 环信登录
                            //                        try{
                            //                            EMClient.getInstance().login(MyApplication.userInfo.getUserID(), MyApplication.userInfo.getHuanxin_api_password(), new EMCallBack() {
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
                            //                        });}
                            //                        catch (Exception e){
                            //                            Log.e("huanxin",e.getMessage()+"");
                            //                        }
                            val editor = sp!!.edit()
                            editor.putString("USER_NAME", it.nickame)
                            editor.putString("USERID", it.userID)
                            editor.putString("VKUSERIP", it.cookieLoginIpt)
                            editor.putString("VKTOKEN", it.cookieLoginToken)
                            //                           ShardTools.getInstance(UserLoginActivity.this).tempSaveSharedata("userid", jsonObject.getString("UserID"));
                            //                           ShardTools.getInstance(UserLoginActivity.this).tempSaveSharedata("loginip", jsonObject.getString("CookieLoginIpt"));
                            //                           ShardTools.getInstance(UserLoginActivity.this).tempSaveSharedata("logintoken", jsonObject.getString("CookieLoginToken"));
                            editor.putBoolean("ISLOGIN", true)
                            editor.apply()
                            LhtTool.isLogin = true
                            getAllUnreadNumber()
                        }
                    }
                }

            } else if (msg.what == ALL_UNREADNUM) {
                val s = msg.data.getString("s")
                Log.e("unread", s)
                if (Integer.valueOf(s!!) > 0) {
                    //tv_yuandian.setVisibility(View.VISIBLE);
                    tv_yuandian.text = s
                    LhtTool.unReadMessage = Integer.valueOf(s)
                } else {
                    tv_yuandian.visibility = View.GONE
                }
            }
        }
    }


    private var msgListener: EMMessageListener = object : EMMessageListener {

        override fun onMessageReceived(messages: List<EMMessage>) {
            //收到消息
            if (!LhtTool.isChatActivity) {
                LogUtils.d("===================收到消息进来了2")


                for (i in messages.indices) {
                    if (messages[i].from == "admin") {

                        if (messages[i].getStringAttribute("isreloginok", null) == "1") {
                            LogUtils.d("==================isreloginok进来了")
                            LogUtils.d("==================targetval:" + messages[i].getStringAttribute("targetval", null))
                            LogUtils.d("==================getHouse_CID:" + LhtTool.getHouse_CID(this@MainActivity)!!)

                            if (messages[i].getStringAttribute("targetval", null) != "0" && messages[i].getStringAttribute("targetval", null) == LhtTool.getHouse_CID(this@MainActivity)) {
                                LhtTool.isLogin = false
                                EMClient.getInstance().logout(true)
                                MyApplication.userInfo = null
                                val `in` = Intent(this@MainActivity, MainActivity::class.java)
                                val pi = PendingIntent.getActivity(this@MainActivity, 0, `in`, PendingIntent.FLAG_UPDATE_CURRENT)
                                val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                val nf = Notification.Builder(this@MainActivity)
                                        .setTicker("时间财富发来一条消息")
                                        .setSmallIcon(R.mipmap.app_logo)
                                        .setWhen(System.currentTimeMillis())
                                        .setAutoCancel(true)
                                        .setContentTitle("登录异常")
                                        .setContentText("你的账号已在另一台设备上登录")
                                        .setContentIntent(pi)
                                        .setDefaults(Notification.DEFAULT_ALL)
                                val nof = nf.build()
                                nm.notify(0, nof)
                            }

                        } else if (messages[i].getStringAttribute("ischat", null) == "0") {
                            // 系统消息
                            var mIntent: Intent? = null
                            if (messages[i].getStringAttribute("tui_con_type", null) != "no") {

                                if (messages[i].getStringAttribute("tui_con_type", null) == "fuwu" && Integer.valueOf(messages[i].getStringAttribute("tui_con_id", null)) > 0) {

                                    mIntent = Intent(this@MainActivity, ServiceDetailsActivity::class.java)
                                    mIntent.putExtra("ID", messages[i].getStringAttribute("tui_con_id", null))

                                } else if (messages[i].getStringAttribute("tui_con_type", null) == "item" && Integer.valueOf(messages[i].getStringAttribute("tui_con_id", null)) > 0) {

                                    mIntent = Intent(this@MainActivity, TaskDetailsActivity::class.java)
                                    mIntent.putExtra("ID", messages[i].getStringAttribute("tui_con_id", null))

                                } else if (messages[i].getStringAttribute("tui_con_type", null) == "shop" && Integer.valueOf(messages[i].getStringAttribute("tui_con_id", null)) > 0) {

                                    mIntent = Intent(this@MainActivity, ShopDetailsActivity::class.java)
                                    mIntent.putExtra("ID", messages[i].getStringAttribute("tui_con_id", null))

                                } else if (messages[i].getStringAttribute("tui_con_type", null) == "weburl" && messages[i].getStringAttribute("gourl", null) != "") {

                                    mIntent = Intent(this@MainActivity, WebViewActivity::class.java)
                                    mIntent.putExtra("weburl", messages[i].getStringAttribute("gourl", null))
                                    mIntent.putExtra("title", messages[i].getStringAttribute("title", null))

                                }
                            } else {
                                mIntent = Intent(this@MainActivity, MessageSystemListActivity::class.java)
                            }
                            var content: String? = null
                            val pi = PendingIntent.getActivity(this@MainActivity, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                            val s = messages[i].body.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            content = s[1].substring(1, s[1].length - 1)
                            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            val nf = Notification.Builder(this@MainActivity)
                                    .setTicker("时间财富发来一条消息")
                                    .setSmallIcon(R.mipmap.app_logo)
                                    .setWhen(System.currentTimeMillis())
                                    .setAutoCancel(true)
                                    .setContentTitle("系统通知")
                                    .setContentText(content)
                                    .setContentIntent(pi)
                                    .setDefaults(Notification.DEFAULT_ALL)
                            val nof = nf.build()
                            nm.notify(0, nof)
                            LogUtils.d("============tui_con_type:" + messages[0].getStringAttribute("tui_con_type", null))
                            LogUtils.d("============tui_con_id:" + messages[0].getStringAttribute("tui_con_id", null))
                            LogUtils.d("============tui_con_class:" + messages[0].getStringAttribute("tui_con_class", null))
                            LogUtils.d("============gourl:" + messages[0].getStringAttribute("gourl", null))
                            LogUtils.d("============title:" + messages[0].getStringAttribute("title", null))
                            getAllUnreadNumber()
                            val mIn = Intent("lht.islht.refresh")
                            sendBroadcast(mIn)

                        } else {
                            //站內消息
                            val `in` = Intent(this@MainActivity, MessageChatActivity::class.java)
                            var content: String? = null
                            `in`.putExtra("ID", messages[i].getStringAttribute("from_uid", null))
                            `in`.putExtra("name", messages[i].getStringAttribute("tui_con_class", null))
                            val pi = PendingIntent.getActivity(this@MainActivity, 0, `in`, PendingIntent.FLAG_UPDATE_CURRENT)
                            LogUtils.d("============from_uid :" + messages[i].getStringAttribute("from_uid", null))
                            LogUtils.d("============tui_con_class:" + messages[i].getStringAttribute("tui_con_class", null))
                            LogUtils.d("============gourl:" + messages[i].getStringAttribute("gourl", null))
                            LogUtils.d("===============messages:$messages")
                            LogUtils.d("===============getBody:" + messages[i].body.toString())
                            LogUtils.d("===============getFrom:" + messages[i].from)
                            LogUtils.d("===============getStringAttribute(\"name\"):" + messages[i].getStringAttribute("name", null))
                            LogUtils.d("===============getStringAttribute(\"imageurl\"):" + messages[i].getStringAttribute("imageurl", null))
                            val s = messages[i].body.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            content = s[1].substring(1, s[1].length - 1)
                            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            val nf = Notification.Builder(this@MainActivity)
                                    .setTicker(messages[i].getStringAttribute("tui_con_class", null) + "PC端发来一条消息")
                                    .setSmallIcon(R.mipmap.app_logo)
                                    .setWhen(System.currentTimeMillis())
                                    .setAutoCancel(true)
                                    .setContentTitle(messages[i].getStringAttribute("tui_con_class", null) + "PC端")
                                    .setContentText(content)
                                    .setContentIntent(pi)
                                    .setDefaults(Notification.DEFAULT_ALL)
                            val nof = nf.build()
                            nm.notify(0, nof)
                            getAllUnreadNumber()
                            val mIn = Intent("lht.islht.refresh")
                            sendBroadcast(mIn)
                        }
                    } else {
                        //聊天消息
                        val mIntent = Intent(this@MainActivity, MessageChatActivity::class.java)
                        var content: String? = null
                        mIntent.putExtra("ID", messages[i].getStringAttribute("from_uid", null))
                        mIntent.putExtra("name", messages[i].getStringAttribute("tui_con_class", null))
                        val pi = PendingIntent.getActivity(this@MainActivity, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                        LogUtils.d("===============messages:$messages")
                        LogUtils.d("===============getBody:" + messages[i].body.toString())
                        LogUtils.d("===============getFrom:" + messages[i].from)
                        LogUtils.d("===============getStringAttribute(\"name\"):" + messages[i].getStringAttribute("name", null))
                        LogUtils.d("===============getStringAttribute(\"imageurl\"):" + messages[i].getStringAttribute("imageurl", null))
                        val s = messages[i].body.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        content = s[1].substring(1, s[1].length - 1)
                        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        val nf = Notification.Builder(this@MainActivity)
                                .setTicker(messages[i].getStringAttribute("tui_con_class", null) + "发来一条消息")
                                .setSmallIcon(R.mipmap.app_logo)
                                .setWhen(System.currentTimeMillis())
                                .setAutoCancel(true)
                                .setContentTitle(messages[i].getStringAttribute("tui_con_class", null))
                                .setContentText(content)
                                .setContentIntent(pi)
                                .setDefaults(Notification.DEFAULT_ALL)
                        val nof = nf.build()
                        nm.notify(0, nof)
                        getAllUnreadNumber()
                        val mIn = Intent("lht.islht.refresh")
                        sendBroadcast(mIn)

                    }
                }
            }
        }

        override fun onCmdMessageReceived(messages: List<EMMessage>) {
            //收到透传消息
            LogUtils.d("===================messages:$messages")
        }

        override fun onMessageRead(messages: List<EMMessage>) {
            //收到已读回执
            LogUtils.d("===================messages:$messages")
        }

        override fun onMessageDelivered(message: List<EMMessage>) {
            //收到已送达回执
            LogUtils.d("===================messages:$message")
        }

        override fun onMessageRecalled(messages: List<EMMessage>) {

        }

        override fun onMessageChanged(message: EMMessage, change: Any) {
            //消息状态变动
            LogUtils.d("===================messages:$change")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowManager.defaultDisplay.getMetrics(metrics)
        setContentView(R.layout.activity_main)
        context = this
        registeNetReciver()
        getTaoCanListData()
        EMClient.getInstance().chatManager().addMessageListener(msgListener)
        StatService.start(this)
        appVersion = LhtTool.getAppVersion(this@MainActivity)
//        init_is_update()//检查更新
        autoLogin()//自动登录
        iniView()//初始化
        fragmentList()//首页4个fragment
        setView()
        viewListener()//view监听事件
        if (AppUtils.isAppForeground()) {
            mIntent = Intent(this, UploadService::class.java)
            startService(mIntent)
            storage("首页")
            requestPermissions()
        }

    }

    override fun onResume() {
        LhtTool.isMainActivity = true
        super.onResume()
    }

    override fun onStart() {
        //storage("首页");
        super.onStart()
    }

    /**
     * 注册网络监听广播
     */
    private fun registeNetReciver() {
        netReceiver = NetReceiver()
        val inf = IntentFilter()
        inf.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(netReceiver, inf)
    }

    private fun storage(pagenameStr: String) {
        dataStatisticsBean = DataStatisticsBean()
        userid = if (LhtTool.isLogin) {
            MyApplication.userInfo!!.userID
        } else {
            "未登录"
        }
        pagename = pagenameStr
        pageindex = pagenameStr
        pageinittime = System.currentTimeMillis().toString() + ""
        DataTools.saveData(dataStatisticsBean, userid, pagename, pageindex, pageinittime)

    }

    fun stopStorage() {
        dataStatisticsBean.pagedowntime = System.currentTimeMillis().toString() + ""

        if (dataStatisticsBean.save()) {
            Log.e("jsonlist2", "保存成功")
        } else {
            Log.e("jsonlist2", "保存失败")
        }
    }

    /**
     * 初始化
     */
    private fun iniView() {
        noScrollViewPager = findViewById(R.id.main_vp)
        radioGroup = findViewById(R.id.guzhu_rg)
        tv_yuandian = findViewById(R.id.tv_yuandian)
        fabu_bt = findViewById(R.id.fabu_rb)
        ll = findViewById(R.id.main_ll)
    }

    /**
     * 提前获取套餐发布列表数据
     */
    private fun getTaoCanListData() {
        val mapList = HashMap<String, Any>()
        //   map.put("userid","0");
        OkhttpTool.getOkhttpTool().post(UrlConfig.TAOCANLIST, mapList, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
                TAOCANDATA = s
            }
        })
    }


    /**
     * 操作view
     */
    private fun setView() {
        noScrollViewPager!!.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): android.support.v4.app.Fragment {
                return fraList[position]
            }

            override fun getCount(): Int {
                return fraList.size
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                // 因为默认PagerAdapter里Fragment只保存两个Fragment的状态，当切换到第三个Fragment时第一个则会销毁
                // 会重新创建视图执行onCreatView方法，频繁切换造成内存抖动，消耗资源
                // 保证切换Fragment时其他Fragment状态不会销毁
                // 也可以使用viewPager.setOffscreenPageLimit(2)方法,参数代表能保存几个Fragment
                // 这种做法是把所有页面都加载到内存中不销毁，适合Fragment页面较少的情况
//                //                super.destroyItem(container, position, object);
            }
        }
        val params = FrameLayout.LayoutParams(tv_yuandian.layoutParams)
        params.setMargins(metrics.widthPixels / 8 * 5 + 15, 0, 0, 0)
        tv_yuandian.layoutParams = params

        if (null != intent.extras && intent.extras.equals("3")) {
            noScrollViewPager!!.currentItem = 2
        }
    }

    /**
     * 权限申请
     */
    private fun requestPermissions() {
        XXPermissions.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_PHONE_STATE)
                .request(object : OnPermission {
                    override fun hasPermission(granted: List<String>, isAll: Boolean) {}
                    override fun noPermission(denied: List<String>, quick: Boolean) {}
                })
    }


    /**
     * view监听
     */
    private fun viewListener() {
        noScrollViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                if (position < 2) {
                    val rb = radioGroup!!.getChildAt(position) as RadioButton
                    rb.isChecked = true
                } else {
                    val rb = radioGroup!!.getChildAt(position + 1) as RadioButton
                    rb.isChecked = true
                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        fabu_bt!!.setOnClickListener {
            dataStatisticsBean.pagenext = "发布页面"
            mIntent = Intent(context, FaBuDemandActivity::class.java)
            startActivity(mIntent)
        }
        radioGroup!!.setOnCheckedChangeListener { group, checkedId ->
            var x = 0
            when (checkedId) {
                R.id.guzhu_rb -> {
                    x = 0
                    storage("雇主页面")
                }
                R.id.fuwushang_rb -> {
                    x = 1
                    storage("服务商页面")
                }
                R.id.xiaoxi_rb -> {
                    x = 2
                    storage("作品页面")
                }
                R.id.wode_rb -> {
                    x = 3
                    storage("个人中心")
                }
            }
            noScrollViewPager!!.currentItem = x
        }
    }

    /**
     * 主页fragment
     */
    private fun fragmentList() {
        fraList.add(GuZhuNewFragment())
        fraList.add(WeiKeFragment())
        fraList.add(ZuoPinFragment())
        fraList.add(PersonalFragment())
    }

    /**
     * back键点击两次弹出是否退出整个程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                CustomToast.showToast(applicationContext, "再按一次退出程序",
                        Toast.LENGTH_SHORT)
                exitTime = System.currentTimeMillis()
            } else {
                ActivityController.finishAll()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)

    }


    /**
     * 更新app
     */
    private fun init_is_update() {
        OkhttpTool.getOkhttpTool()[UrlConfig.GET_VERSION, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }


            override fun onResponse(call: Call, response: Response) {

                val s = response.body()!!.string()
                try {
                    versionGson = Gson().fromJson(s, VersionGson::class.java)
                    LogUtils.d("升级数据： \n $versionGson")
                    hd.sendEmptyMessage(VERSION_UPDATE)
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                }
            }
        }]
    }

    /**
     * 自动登录
     */
    private fun autoLogin() {
        sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        val userName = sp!!.getString("USER_NAME", "no")
        val passWord = sp!!.getString("PASSWORD", "no")
        val userid = sp!!.getString("USERID", "no")
        val vkuserip = sp!!.getString("VKUSERIP", "no")
        val vktoken = sp!!.getString("VKTOKEN", "no")
        val islogin = sp!!.getBoolean("ISLOGIN", true)
        if (islogin) {
            if (userName != "no" && passWord != "no") {
                driveId = LhtTool.getHouse_CID(this)
                map["userid"] = userid!!
                map["vkuserip"] = vkuserip!!
                map["mobile_id"] = driveId!!
                map["vktoken"] = vktoken!!
                OkhttpTool.getOkhttpTool().post(UrlConfig.ZIDONG_LOGIN, map, object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {

                        val s = response.body()!!.string()
                        LogUtils.d("userlogin : \n $s")
                        var jsonObject: JSONObject? = null
                        try {
                            jsonObject = JSONObject(s)
                            val UserId = jsonObject.getString("UserID")
                            ShardTools.getInstance(MyApplication.INSTANCE.applicationContext).tempSaveSharedata("userid", UserId)
                            ShardTools.getInstance(MyApplication.INSTANCE.applicationContext).tempSaveSharedata("vip等级", jsonObject.getString("viptype"))
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        val ms = Message()
                        val b = Bundle()
                        b.putString("s", s)
                        ms.data = b
                        ms.what = AUTO_LOGIN
                        hd.sendMessage(ms)
                    }
                })
            }
        }
    }


    private fun getAllUnreadNumber() {

        if (LhtTool.isLogin) {
            map1["userid"] = MyApplication.userInfo!!.userID
            OkhttpTool.getOkhttpTool().post(UrlConfig.ALL_UNREAD, map1, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {

                    val s = response.body()!!.string()
                    LogUtils.d("============Numberresponse:$s")
                    val ms = Message()
                    val b = Bundle()
                    b.putString("s", s)
                    ms.data = b
                    ms.what = ALL_UNREADNUM
                    hd.sendMessage(ms)

                }
            })
        }
    }


    override fun onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(msgListener)
        LhtTool.isMainActivity = false
        unregisterReceiver(netReceiver)
        //   Glide.with(MainActivity.this).pauseRequests();
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0x123 && resultCode == 2) {
            val mIn = Intent("lht.islht.loginInit")
            sendBroadcast(mIn)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var tv_yuandian: TextView
        @JvmField
        var TAOCANDATA = ""
    }
}
