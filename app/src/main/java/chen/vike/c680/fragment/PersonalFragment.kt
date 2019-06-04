package chen.vike.c680.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.AdapterView
import android.widget.Toast
import chen.vike.c680.activity.*
import chen.vike.c680.adapter.SelfGvadapter
import chen.vike.c680.adapter.TuiJianGVadapter
import chen.vike.c680.adapter.TuiSongXiangMuAdapter
import chen.vike.c680.bean.*
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.*
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.views.MyListView
import chen.vike.c680.webview.WebViewActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.gyf.barlibrary.ImmersionBar
import com.gyf.barlibrary.SimpleImmersionFragment
import com.lht.vike.a680_v1.R
import kotlinx.android.synthetic.main.fragment_weike_person_gridlist.view.*
import kotlinx.android.synthetic.main.fragment_weike_personal_head.view.*
import kotlinx.android.synthetic.main.fuwu_person_tusong.view.*
import kotlinx.android.synthetic.main.guzhu_person_tusong.view.*
import kotlinx.android.synthetic.main.person_head_guzhu_item.view.*
import kotlinx.android.synthetic.main.person_head_no_login_item.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2017/11/27.
 * 个人中心
 */

class PersonalFragment : SimpleImmersionFragment(), MyListView.OnLoadListener, View.OnClickListener {


    private val NETWORK_EXCEPTION = 0X111
    private val TX_YANZHENG = 0X212
    private val TO_OTHER = 0X121

    private var mView: View? = null
    private var v: View? = null
    private var mContext: Context? = null
    private var nameAdapter: SelfGvadapter? = null
    //服务商个人中心
    private val fuwu_person_Name = arrayOf("参与项目", "雇佣项目", "消息", "余额提现", "进入店铺", "店铺信息", "发布服务", "我的服务")
    private val fuwu_person_Image = intArrayOf(R.mipmap.fuwu_person_canyu, R.mipmap.fuwu_person_guyong, R.mipmap.person_message_img, R.mipmap.fuwu_person_tixian, R.mipmap.person_fuwu_jinru_img, R.mipmap.person_fuwu_dianpu_img, R.mipmap.person_fuwu_fabu_img, R.mipmap.person_fuwu_myisfuwu_img)
    //雇主个人中心
    private val guzhu_person_Name = arrayOf("我的商标", "商标注册", "发票管理", "道具商城", "优惠券", "消息", "上传作品", "我的作品")
    private val guzhu_person_Image = intArrayOf(R.mipmap.guzhu_person_wdsb, R.mipmap.guzhu_person_sbzc, R.mipmap.guzhu_person_fapiao, R.mipmap.guzhu_person_daoju, R.mipmap.person_youhui_img, R.mipmap.person_message_img, R.mipmap.update_zuopin_img, R.mipmap.my_is_zuopin)
    private val namesList = ArrayList<GridViewInfoBean>()
    private val tuijianList = ArrayList<XuQiuList.ListBean>()
    private var mIntent: Intent? = null
    private var myList: MyListView? = null
    private var personRefresh: SwipeRefreshLayout? = null
    internal var isPress = true
    private val vipImgs = intArrayOf(R.mipmap.vipa0, R.mipmap.vipa1, R.mipmap.vipa2, R.mipmap.vipa3, R.mipmap.vipa4, R.mipmap.vipa5)
    private val weikeload = ArrayList<WeiKePageGson.ItemListBean>()
    private var aCache: ACache? = null//轻量级缓存
    private var userMeassBean: UserMeassBean? = UserMeassBean()
    private var isListener = true
    private var tuiJianGVadapter: TuiJianGVadapter? = null
    private var dataStatisticsBean = DataStatisticsBean()
    private var isPrepared = false //fragment切换标志位
    private val hd = MHandler(this)
    private lateinit var mainView: View


    /**
     * 存储页面数据
     */
    private var userid = ""
    private var pagename = ""
    private var pageindex = ""
    private var pageinittime = ""

    /**
     * 初始化
     */
    private var myadapter: TuiSongXiangMuAdapter? = null
    private val list_xiangmu = ArrayList<WeiKePageGson.ItemListBean>()

    /**
     * 推送项目数据
     */
    private var tuiSongBean: TuiSongBean? = TuiSongBean()
    private val fuwusList = ArrayList<TuiSongFuwuSBean.ListBean>()

    /**
     * 签到按钮的动画
     */
    private var set: AnimationSet? = null

    /**
     * 判断登录  加载数据
     */
    private val map = HashMap<String, Any>()

    /**
     * 刷新数据
     */
    private var xuQiuList: XuQiuList? = null

    /**
     * 提现
     */
    private val mapTi = HashMap<String, Any>()
    private var ld: LoadingDialog? = null
    private var tiXianSureGson: TiXianSureGson? = null

    private var exitTime: Long = 0

    private class MHandler(personalFragment: PersonalFragment) : Handler() {
        private val weakReference: WeakReference<PersonalFragment> = WeakReference(personalFragment)
        private val personalFragment = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            personalFragment?.run {
                if (msg?.what == TX_YANZHENG) {
                    val s = tiXianSureGson!!.user_data.account_money
                    val ss = s.substring(0, s.indexOf("."))
                    val a = Integer.valueOf(ss)
                    if (tiXianSureGson!!.user_data.email != "" && tiXianSureGson!!.user_data.bankname != "" && tiXianSureGson!!.user_data.fullname != "" && a >= 50) {
                        //所有条件都符合
                        //                    Intent mIntent = new Intent(mContext, ReffectActivity.class);
                        //                    startActivityForResult(mIntent, 1);
                        mIntent = Intent(mContext, WebViewActivity::class.java)
                        mIntent!!.putExtra("weburl", UrlConfig.TIXIANRENZHENG + "?vkuserip=" + MyApplication.userInfo!!.cookieLoginIpt
                                + "&vktoken=" + MyApplication.userInfo!!.cookieLoginToken + "&userid=" + MyApplication.userInfo!!.userID)
                        mIntent!!.putExtra("title", "提现")
                        startActivityForResult(mIntent, 1)

                    } else if (tiXianSureGson!!.user_data.fullname == "" || tiXianSureGson!!.user_data.email == "" || tiXianSureGson!!.user_data.bankname == "") {
                        //实名未验证

                        val mIntent = Intent(mContext, GRZXWeiTiXianActivity::class.java)
                        mIntent.putExtra("email", tiXianSureGson!!.user_data.email)
                        mIntent.putExtra("bankname", tiXianSureGson!!.user_data.bankname)
                        mIntent.putExtra("fullname", tiXianSureGson!!.user_data.fullname)
                        dataStatisticsBean.pagenext = "提现未认证页面"
                        saveData()
                        startActivity(mIntent)

                    } else if (a < 50) {
                        if (System.currentTimeMillis() - exitTime > 4000) {
                            CustomToast.showToast(activity, "你的余额少于50元,不能进行提现", Toast.LENGTH_SHORT)
                            exitTime = System.currentTimeMillis()
                        }
                    }
                } else if (msg?.what == 3) {
                    personRefresh!!.isRefreshing = false
                } else if (msg?.what == QIANDAO) {
                    mainView.person_qiandao_btn.setBackgroundResource(R.mipmap.person_weiqiandao)
                    mainView.person_qiandao_text.startAnimation(set)
                    mainView.person_qiandao_text.visibility = View.GONE
                } else if (msg?.what == USERMESSAGE) {
                    if (userMeassBean?.vkdata?.sp3total != null) {
                        mainView.person_text_cjl.text = userMeassBean!!.vkdata.sp3total + ""
                        mainView.person_name_show.text = userMeassBean!!.vkdata.username + ""
                        mainView.person_name.text = userMeassBean!!.vkdata.username + ""
                        mainView.person_shouru_show.text = userMeassBean!!.vkdata.vk_money + ""
                        mainView.person_yue_show.text = userMeassBean!!.vkdata.vk_mon_pay + ""
                        mainView.person_zuji_show.text = userMeassBean!!.vkdata.dingzhi_num + ""
                        val vipType = userMeassBean!!.vkdata.viptype
                        when (vipType) {
                            "4" -> {
                                mainView.person_dengji_img.setBackgroundResource(vipImgs[0])
                                mainView.person_dengji_img_show.setBackgroundResource(vipImgs[0])
                            }
                            "5" -> {
                                mainView.person_dengji_img.setBackgroundResource(vipImgs[1])
                                mainView.person_dengji_img_show.setBackgroundResource(vipImgs[1])
                            }
                            "6" -> {
                                mainView.person_dengji_img.setBackgroundResource(vipImgs[2])
                                mainView.person_dengji_img_show.setBackgroundResource(vipImgs[2])
                            }
                            "7" -> {
                                mainView.person_dengji_img.setBackgroundResource(vipImgs[3])
                                mainView.person_dengji_img_show.setBackgroundResource(vipImgs[3])
                            }
                            "8" -> {
                                mainView.person_dengji_img.setBackgroundResource(vipImgs[4])
                                mainView.person_dengji_img_show.setBackgroundResource(vipImgs[4])
                            }
                            "9" -> {
                                mainView.person_dengji_img.setBackgroundResource(vipImgs[5])
                                mainView.person_dengji_img_show.setBackgroundResource(vipImgs[5])
                            }
                        }
                        if (vipType != null) {
                            if (Integer.valueOf(vipType) < 4) {
                                mainView.fuwu_more.visibility = View.GONE
                            }
                        }
                        val qiandao = userMeassBean!!.vkdata.vk_sign
                        if (qiandao == "0") {
                            mainView.person_qiandao_btn.setBackgroundResource(R.mipmap.person_qiandao)
                        } else {
                            mainView.person_qiandao_btn.setBackgroundResource(R.mipmap.person_weiqiandao)
                            mainView.person_qiandao_btn.isEnabled = false
                        }
                        mainView.person_guanzhu_show.text = userMeassBean!!.vkdata.fav_item_count
                    }
                    if (MyApplication.userInfo != null && MyApplication.userInfo!!.isGuzhu != null) {
                        // if (MyApplication.userInfo.getIsGuzhu().equals("1")) {
                        val options = RequestOptions()
                                .placeholder(R.mipmap.image_loading)
                                .error(R.mipmap.image_erroe)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                        Glide.with(mContext!!).load(MyApplication.userInfo!!.icon).apply(options).into(mainView.iv_login)
                        try {
                            Glide.with(mContext!!).load(MyApplication.userInfo!!.icon).apply(options).into(mainView.iv_login)

                        } catch (e: Exception) {
                            ToastUtils.showShort(MyApplication.INSTANCE.getString(R.string.net_error), Toast.LENGTH_SHORT)
                        }

                        mainView.head_guzhu_name.text = MyApplication.userInfo!!.nickame
                        mainView.head_guzhu_yue.text = "余额：￥" + userMeassBean!!.vkdata.vk_mon_pay


                        // }
                    }
                    personRefresh!!.isRefreshing = false
                } else if (msg?.what == TUISONGXIANGMU) {
                    isPress = false
                    if (myadapter != null) {
                        myadapter!!.type = "项目"
                        myadapter!!.refresh()
                        myadapter!!.addlist(weikeload)
                        myadapter!!.notifyDataSetChanged()
                    }

                } else if (msg?.what == GETINFO) {
                    mainView.person_wei_fabu_xuqiu.visibility = View.GONE
                    mainView.tujian_grid.visibility = View.VISIBLE
                    if (tuijianList.size > 0) {
                        tuijianList.clear()
                    }
                    tuijianList.addAll(xuQiuList!!.list)
                    tuiJianGVadapter!!.notifyDataSetChanged()
                } else if (msg?.what == GUZHULISTERROR) {
                    mainView.person_wei_fabu_xuqiu.visibility = View.VISIBLE
                    mainView.tujian_grid.visibility = View.GONE
                } else if (msg?.what == NETWORK_EXCEPTION) {

                    LhtTool.showNetworkException(mContext, msg)

                }
                ld?.dismiss()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        //setUserVisVleHint（）方法在 Fragment 1 第一次加载的时候不走，只有在切换的时候走该方法
        userVisibleHint = true
        super.onActivityCreated(savedInstanceState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        //可见的并且是初始化之后才加载
        if (isPrepared && isVisibleToUser) {

            getLoginInfo()

        }
    }

    override fun onResume() {
        super.onResume()
        getLoginInfo()
    }

    /**
     * 登录状态
     */
    private fun getLoginInfo() {
        isPress = true
        //        Toast.makeText(getActivity(),"OnResume",Toast.LENGTH_SHORT).show();

        if (LhtTool.isLoginPage) {
            if (LhtTool.isLogin) {
                if (MyApplication.userInfo != null) {
                    if (MyApplication.userInfo!!.isGuzhu == "1") {
                        mainView.person_head.visibility = View.GONE
                        mainView.person_head_guzhu.visibility = View.VISIBLE
                        mainView.person_head_no_login.visibility = View.GONE
                        LhtTool.isLoginPage = false
                        isListener = true
                        if (namesList.size > 0) {
                            namesList.clear()
                        }
                        for (i in guzhu_person_Name.indices) {
                            val info = GridViewInfoBean()
                            info.image = guzhu_person_Image[i]
                            info.name = guzhu_person_Name[i]
                            namesList.add(info)
                        }
                        nameAdapter!!.notifyDataSetChanged()
                        myadapter!!.type = "服务"
                        isLoginFresh()

                    } else if (MyApplication.userInfo!!.isGuzhu == "0") {
                        mainView.person_head.visibility = View.VISIBLE
                        mainView.person_head_guzhu.visibility = View.GONE
                        mainView.person_head_no_login.visibility = View.GONE
                        //2018.3.22
                        mainView.guzhu_tuisong_show.visibility = View.GONE
                        mainView.fuwu_tuisong_show.visibility = View.VISIBLE
                        LhtTool.isLoginPage = false
                        isListener = false
                        if (namesList.size > 0) {
                            namesList.clear()
                        }
                        for (i in fuwu_person_Name.indices) {
                            val info = GridViewInfoBean()
                            info.image = fuwu_person_Image[i]
                            info.name = fuwu_person_Name[i]
                            namesList.add(info)
                        }
                        nameAdapter!!.notifyDataSetChanged()
                        tuiSongData()
                    }
                }
            } else {
                mainView.person_head.visibility = View.GONE
                mainView.person_head_guzhu.visibility = View.GONE
                mainView.person_head_no_login.visibility = View.VISIBLE
                mainView.person_wei_fabu_xuqiu.visibility = View.VISIBLE
                mainView.tujian_grid.visibility = View.GONE
                mainView.guzhu_tuisong_show.visibility = View.VISIBLE
                mainView.fuwu_tuisong_show.visibility = View.GONE
                LhtTool.isLoginPage = false
                isListener = false
                if (namesList.size > 0) {
                    namesList.clear()
                }
                for (i in guzhu_person_Name.indices) {
                    val info = GridViewInfoBean()
                    info.image = guzhu_person_Image[i]
                    info.name = guzhu_person_Name[i]
                    namesList.add(info)
                }
                myadapter!!.type = "服务"
                // isLoginFresh();
                nameAdapter!!.notifyDataSetChanged()
            }
        }
        tuiSongListener()
        if (LhtTool.isLogin) {
            userMessage()//用户信息
            isLoginFresh()
        } else {
            mainView.fuwu_more.visibility = View.GONE
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = LayoutInflater.from(mContext).inflate(R.layout.fragment_weike_person_all, null)
        mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_weike_personal, null)
        aCache = ACache.get(mContext)
        if (aCache!!.getAsString("userMessage") != null) {
            val userMessage = aCache!!.getAsString("userMessage")
            try {
                userMeassBean = Gson().fromJson(userMessage, UserMeassBean::class.java)
                hd.sendEmptyMessage(USERMESSAGE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        iniview()
        isLoginYno()
        isPrepared = true
    }

    override fun onStart() {
        super.onStart()
        setSaveData("个人中心页面")
    }

    private fun setSaveData(pagenameStr: String) {
        dataStatisticsBean = DataStatisticsBean()
        userid = if (LhtTool.isLogin) {
            MyApplication.userInfo!!.userID
        } else {
            "未登录"
        }
        pagename = pagenameStr
        pageindex = pagenameStr
        pageinittime = System.currentTimeMillis().toString()
        DataTools.saveData(dataStatisticsBean, userid, pagename, pageindex, pageinittime)


    }

    /**
     * 保存数据
     */
    fun saveData() {
        dataStatisticsBean.pagedowntime = System.currentTimeMillis().toString() + ""

        if (dataStatisticsBean.save()) {
            LogUtils.d("jsonlist2", "保存成功")
        } else {
            LogUtils.d("jsonlist2", "保存失败")
        }
    }

    private fun iniview() {

        mView!!.person_signed_setting.setOnClickListener(this)
        mView!!.person_chakan_cjl.setOnClickListener(this)
        mView!!.person_qiandao_btn.setOnClickListener(this)
        mView!!.shouru_show.setOnClickListener(this)
        mView!!.person_yue_tixian_btn.setOnClickListener(this)
        mView!!.yue_show.setOnClickListener(this)
        mView!!.guanzhu_show.setOnClickListener(this)
        mView!!.dingzhi_show.setOnClickListener(this)
        mView!!.person_head_btn_login.setOnClickListener(this)
        myList = v!!.findViewById(R.id.person_list)

        nameAdapter = SelfGvadapter(mContext, namesList)
        mView!!.person_gv.adapter = nameAdapter
        tuiJianGVadapter = TuiJianGVadapter(mContext, tuijianList, map)
        mView!!.tujian_grid.adapter = tuiJianGVadapter
        myadapter = TuiSongXiangMuAdapter(mContext!!)
        if (LhtTool.isLogin) {
            if (MyApplication.userInfo != null && MyApplication.userInfo!!.isGuzhu != null) {
                if (MyApplication.userInfo!!.isGuzhu == "0") {
                    myadapter!!.type = "项目"
                    tuiSongData()
                } else {
                    myadapter!!.type = "服务"
                    isLoginFresh()
                }
            }
        } else {
            myadapter!!.type = "服务"
            isLoginFresh()
        }
        personRefresh = v!!.findViewById(R.id.person_refresh)
        setView()
        isShowView()
        gvListener()


    }

    private fun setView() {
        myList!!.adapter = myadapter
        myList!!.addHeaderView(mView)
        personRefresh!!.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"))
        qianDaoDongHua()
        otherListener()
    }

    /**
     * 从未登录
     */
    private fun isLoginYno() {
        if (LhtTool.isLogin) {

        } else {
            mainView.person_head.visibility = View.GONE
            mainView.person_head_guzhu.visibility = View.GONE
            mainView.person_head_no_login.visibility = View.VISIBLE
            mainView.guzhu_tuisong_show.visibility = View.VISIBLE
            mainView.fuwu_tuisong_show.visibility = View.GONE
            mainView.person_wei_fabu_xuqiu.visibility = View.VISIBLE
            mainView.tujian_grid.visibility = View.GONE
            LhtTool.isLoginPage = false
            isListener = false
            if (namesList.size > 0) {
                namesList.clear()
            }
            for (i in guzhu_person_Name.indices) {
                val info = GridViewInfoBean()
                info.image = guzhu_person_Image[i]
                info.name = guzhu_person_Name[i]
                namesList.add(info)
            }
            myadapter!!.type = "服务"
            isLoginFresh()
            nameAdapter!!.notifyDataSetChanged()
        }
    }

    /**
     * 雇主监听事件
     */
    private fun otherListener() {
        mainView.person_fuwu_setting.setOnClickListener {
            if (LhtTool.isLogin) {
                dataStatisticsBean.pagenext = "设置页面"
                saveData()
                startActivityForResult(Intent(mContext, SheZhiActivity::class.java), TO_OTHER)
            } else {
                CustomToast.showToast(mContext, "亲，你还没有登录哦~", Toast.LENGTH_SHORT)
            }
        }
        mainView.btn_my_is_fuwushang.setOnClickListener {
            mainView.guzhu_tuisong_show.visibility = View.GONE
            mainView.fuwu_tuisong_show.visibility = View.VISIBLE
            mainView.person_head.visibility = View.VISIBLE
            mainView.person_head_guzhu.visibility = View.GONE
            isListener = false
            if (namesList.size > 0) {
                namesList.clear()
            }
            for (i in fuwu_person_Name.indices) {
                val info = GridViewInfoBean()
                info.image = fuwu_person_Image[i]
                info.name = fuwu_person_Name[i]
                namesList.add(info)

            }
            nameAdapter!!.notifyDataSetChanged()
            tuiSongData()
        }
        mainView.btn_my_is_guzhu.setOnClickListener {
            mainView.guzhu_tuisong_show.visibility = View.VISIBLE
            mainView.fuwu_tuisong_show.visibility = View.GONE
            mainView.person_head.visibility = View.GONE
            isListener = true
            mainView.person_head_guzhu.visibility = View.VISIBLE
            if (namesList.size > 0) {
                namesList.clear()
            }
            for (i in guzhu_person_Name.indices) {
                val info = GridViewInfoBean()
                info.image = guzhu_person_Image[i]
                info.name = guzhu_person_Name[i]
                namesList.add(info)
            }
            nameAdapter!!.notifyDataSetChanged()
            myadapter!!.type = "服务"

            isLoginFresh()
        }
        mainView.grzx_cz.setOnClickListener {
            mIntent = Intent(mContext, RechargeActivity::class.java)
            dataStatisticsBean.pagenext = "充值页面"
            saveData()
            startActivity(mIntent)
        }
        mainView.grzx_tx!!.setOnClickListener {
            if (LhtTool.isLogin) {
                //                    Blance_tx();
                mIntent = Intent(mContext, WebViewActivity::class.java)
                mIntent!!.putExtra("weburl", UrlConfig.TIXIANRENZHENG + "?vkuserip=" + MyApplication.userInfo!!.cookieLoginIpt
                        + "&vktoken=" + MyApplication.userInfo!!.cookieLoginToken + "&userid=" + MyApplication.userInfo!!.userID)
                mIntent!!.putExtra("title", "提现")
                dataStatisticsBean.pagenext = "提现页面"
                saveData()
                startActivityForResult(mIntent, 1)
            } else {
                CustomToast.showToast(mContext, "亲，你还没登录哦~", Toast.LENGTH_SHORT)
                mIntent = Intent(mContext, UserLoginActivity::class.java)
                dataStatisticsBean.pagenext = "用户登录页面"
                saveData()
                startActivity(mIntent)
            }
        }
        mainView.guzhu_more.setOnClickListener {
            if (LhtTool.isLogin) {
                mIntent = Intent(mContext, PersonMyFaBuXuQiuActivity::class.java)
                dataStatisticsBean.pagenext = "我的订单页面"
                saveData()
                startActivity(mIntent)
            } else {
                CustomToast.showToast(mContext, "亲，你还没登录哦~", Toast.LENGTH_SHORT)
                mIntent = Intent(mContext, UserLoginActivity::class.java)
                dataStatisticsBean.pagenext = "用户登录页面"
                saveData()
                startActivity(mIntent)
            }
        }
        mainView.fuwu_more.setOnClickListener {
            val intent = Intent(mContext, VipMoreActivity::class.java)
            dataStatisticsBean.pagenext = "定制项目页面"
            saveData()
            startActivity(intent)
        }
    }

    private fun tuiSongData() {
        val map = HashMap<String, Any>()
        if (LhtTool.isLogin) {
            map["userid"] = MyApplication.userInfo!!.userID
            map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
            map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
        } else {
            map["userid"] = 0
        }
        OkhttpTool.getOkhttpTool().post(UrlConfig.PERSON_TUISONG, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {

                try {
                    val s = response.body()!!.string()
                    LogUtils.e("personxuqiu2", s)
                    tuiSongBean = Gson().fromJson(s, TuiSongBean::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                weikeload.clear()
                if (tuiSongBean!!.err_code == "0") {
                    if (tuiSongBean != null) {
                        for (i in 0 until tuiSongBean!!.items.size) {
                            val listBean = WeiKePageGson.ItemListBean()
                            listBean.itemid = tuiSongBean!!.items[i].itemid.toString()
                            listBean.itemname = tuiSongBean!!.items[i].itemname
                            listBean.price = tuiSongBean!!.items[i].money
                            listBean.endtime = tuiSongBean!!.items[i].overtime.toString()
                            listBean.content = tuiSongBean!!.items[i].summary
                            weikeload.add(listBean)
                        }
                    }

                    hd.sendEmptyMessage(TUISONGXIANGMU)
                }
            }
        })
    }

    /**
     * 用户信息0
     */

    private fun userMessage() {
        val map = HashMap<String, Any>()
        if (LhtTool.isLogin) {
            map["userid"] = MyApplication.userInfo!!.userID
            map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
            map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
        } else {
            map["userid"] = 0
        }
        OkhttpTool.getOkhttpTool().post(UrlConfig.PERSON_MSG, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {

                try {
                    val s = response.body()!!.string()
                    userMeassBean = Gson().fromJson(s, UserMeassBean::class.java)
                    if (userMeassBean!!.vkdata != null && userMeassBean != null) {
                        if (userMeassBean!!.err_code == "0") {
                            hd.sendEmptyMessage(USERMESSAGE)
                            aCache!!.put("userMessage", s, 100000000)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun qianDaoDongHua() {
        val left = mainView.person_qiandao_text.left
        val top = mainView.person_qiandao_text.top
        val translate = TranslateAnimation(left.toFloat(), left.toFloat(), top.toFloat(), (top - 100).toFloat())
        translate.duration = 2000
        val alpha = AlphaAnimation(1f, 0f)
        alpha.duration = 2000
        alpha.fillAfter = true
        set = AnimationSet(false)
        set!!.addAnimation(translate)
        set!!.addAnimation(alpha)
    }

    /**
     * 分类别显示头部
     */
    private fun isShowView() {
        if (LhtTool.isLogin) {
            if (MyApplication.userInfo != null) {
                if (MyApplication.userInfo!!.isGuzhu == "1") {
                    mainView.person_head.visibility = View.GONE
                    mainView.person_head_guzhu.visibility = View.VISIBLE
                    mainView.person_head_no_login.visibility = View.GONE
                } else if (MyApplication.userInfo!!.isGuzhu == "0") {
                    mainView.person_head.visibility = View.VISIBLE
                    mainView.person_head_guzhu.visibility = View.GONE
                    mainView.person_head_no_login.visibility = View.GONE
                }
            }
        } else {
            mainView.person_head.visibility = View.GONE
            mainView.person_head_guzhu.visibility = View.GONE
            mainView.person_head_no_login.visibility = View.VISIBLE
        }
        if (MyApplication.userInfo != null) {
            if (MyApplication.userInfo!!.isGuzhu == "0") {
                mainView.guzhu_tuisong_show.visibility = View.GONE
                mainView.fuwu_tuisong_show.visibility = View.VISIBLE
                isListener = false
                if (namesList.size > 0) {
                    namesList.clear()
                }
                for (i in fuwu_person_Name.indices) {
                    val info = GridViewInfoBean()
                    info.image = fuwu_person_Image[i]
                    info.name = fuwu_person_Name[i]
                    namesList.add(info)
                }

            } else {
                mainView.fuwu_tuisong_show.visibility = View.GONE
                isListener = true
                if (namesList.size > 0) {
                    namesList.clear()
                }
                for (i in guzhu_person_Name.indices) {
                    val info = GridViewInfoBean()
                    info.image = guzhu_person_Image[i]
                    info.name = guzhu_person_Name[i]
                    namesList.add(info)
                }
            }
        }

    }

    /**
     * 推送监听事件
     */
    private fun tuiSongListener() {
        if (isPress) {
            if (MyApplication.userInfo != null) {
                if (Integer.valueOf(MyApplication.userInfo!!.viptype) < 5) {
                    mainView.fuwu_tuisong_xiangmu.text = "推荐项目"
                } else {
                    mainView.fuwu_tuisong_xiangmu.text = "最新项目"
                }
            }
            isPress = false
        }
        personRefresh!!.setOnRefreshListener {
            Handler().postDelayed({
                userMessage()
                isLoginFresh()
                personRefresh!!.isRefreshing = false
            }, 2000)
        }
        myList!!.setOnLoadListener(this)
    }

    /**
     * 监听事件
     */
    private fun gvListener() {
        mainView.person_gv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (isListener) {
                if (mainView.person_gv.count == namesList.size) {
                    when (position) {
                        0 -> if (LhtTool.isLogin) {
                            mIntent = Intent(mContext, WebViewActivity::class.java)
                            var s = ""
                            if (MyApplication.userInfo != null) {
                                s = MyApplication.userInfo!!.userID
                            }
                            mIntent!!.putExtra("weburl", "http://app.680.com/user/myorder/index.aspx?userid=$s")
                            mIntent!!.putExtra("title", "我的商标")
                            dataStatisticsBean.pagenext = "我的商标页面"
                            saveData()
                            startActivityForResult(mIntent, 1)
                        } else {
                            CustomToast.showToast(mContext, "亲，你还没登录哦~", Toast.LENGTH_SHORT)
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        1 -> if (LhtTool.isLogin) {
                            mIntent = Intent(mContext, WebViewActivity::class.java)
                            mIntent!!.putExtra("weburl", "https://app.680.com/shangbiao/")
                            mIntent!!.putExtra("title", "商标注册服务")
                            dataStatisticsBean.pagenext = "商标注册页面"
                            saveData()
                            startActivityForResult(mIntent, 1)
                        } else {
                            ToastUtils.showShort("亲，你还没登录哦~")
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        2 -> if (LhtTool.isLogin) {
                            dataStatisticsBean.pagenext = "发票管理页面"
                            saveData()
                            startActivity(Intent(mContext, PersonInvoiceManageAvtivity::class.java))
                        } else {
                            ToastUtils.showShort("亲，你还没登录哦~")
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        3 -> if (LhtTool.isLogin) {
                            dataStatisticsBean.pagenext = "道具中心页面"
                            saveData()
                            startActivity(Intent(mContext, PersonDaoJuActivity::class.java))
                        } else {
                            ToastUtils.showShort("亲，你还没登录哦~")
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        4 -> if (LhtTool.isLogin) {
                            mIntent = Intent(mContext, WebViewActivity::class.java)
                            mIntent!!.putExtra("weburl", "http://app.680.com/touch/coupon_list.aspx?userid=" + MyApplication.userInfo!!.userID)
                            mIntent!!.putExtra("title", "我的优惠券")
                            dataStatisticsBean.pagenext = "我的优惠券页面"
                            saveData()
                            startActivityForResult(mIntent, 1)
                        } else {
                            ToastUtils.showShort("亲，你还没登录哦~")
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        5 -> if (LhtTool.isLogin) {
                            val intent = Intent(mContext, PersonXiaoXiActivity::class.java)
                            dataStatisticsBean.pagenext = "我的消息页面"
                            saveData()
                            startActivity(intent)
                        } else {
                            ToastUtils.showShort("亲，你还没登录哦~")
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        6 -> if (LhtTool.isLogin) {
                            val intent = Intent(mContext, PersonUpdateZuoPinActivity::class.java)
                            dataStatisticsBean.pagenext = "上传作品页面"
                            saveData()
                            startActivity(intent)
                        } else {
                            ToastUtils.showShort("亲，你还没登录哦~")
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        7 -> if (LhtTool.isLogin) {
                            val mIntent = Intent(mContext, WebViewActivity::class.java)
                            var s = ""
                            if (MyApplication.userInfo != null) {
                                s = MyApplication.userInfo!!.userID
                            }
                            mIntent.putExtra("weburl", "https://app.680.com/app/zuopin/list.aspx?userid=$s")
                            mIntent.putExtra("title", "我的作品")
                            dataStatisticsBean.pagenext = "我的作品页面"
                            saveData()
                            startActivityForResult(mIntent, 1)
                        } else {
                            ToastUtils.showShort("亲，你还没登录哦~")
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                    }
                }
            } else {
                if (mainView.person_gv.count == namesList.size) {
                    when (position) {
                        0 -> if (LhtTool.isLogin) {
                            dataStatisticsBean.pagenext = "参与的项目页面"
                            saveData()
                            startActivity(Intent(mContext, ParticipateProjectActivity::class.java))
                        } else {
                            CustomToast.showToast(mContext, "亲，你还没登录哦~", Toast.LENGTH_SHORT)
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        1 -> if (LhtTool.isLogin) {
                            mIntent = Intent(mContext, Cydxm_GuYongActivity::class.java)
                            mIntent!!.putExtra("item", "全部")
                            dataStatisticsBean.pagenext = "雇佣项目页面"
                            saveData()
                            startActivity(mIntent)
                        } else {
                            CustomToast.showToast(mContext, "亲，你还没登录哦~", Toast.LENGTH_SHORT)
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        2 -> if (LhtTool.isLogin) {
                            val intent = Intent(mContext, PersonXiaoXiActivity::class.java)
                            dataStatisticsBean.pagenext = "我的消息页面"
                            saveData()
                            startActivity(intent)
                        } else {
                            CustomToast.showToast(mContext, "亲，你还没登录哦~", Toast.LENGTH_SHORT)
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        3 -> if (LhtTool.isLogin) {
                            Blance_tx()
                        } else {
                            CustomToast.showToast(mContext, "亲，你还没登录哦~", Toast.LENGTH_SHORT)
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        4 -> if (MyApplication.userInfo != null && MyApplication.userInfo!!.userID != null) {
                            mIntent = Intent(mContext, ShopDetailsActivity::class.java)
                            mIntent!!.putExtra("ID", MyApplication.userInfo!!.userID)
                            dataStatisticsBean.pagenext = "进入店铺页面"
                            saveData()
                            startActivity(mIntent)
                        } else {
                            ToastUtils.showShort("亲，你还没有登陆哦~")
                            mIntent = Intent(mContext, UserLoginActivity::class.java)
                            dataStatisticsBean.pagenext = "用户登录页面"
                            saveData()
                            startActivity(mIntent)
                        }
                        5 ->
                            //店铺信息修改
                            if (LhtTool.isLogin) {
                                mIntent = Intent(mContext, ChangeShopActivity::class.java)
                                dataStatisticsBean.pagenext = "店铺信息页面"
                                saveData()
                                startActivity(mIntent)
                            } else {
                                ToastUtils.showShort("亲，你还没有登陆哦~")
                                mIntent = Intent(mContext, UserLoginActivity::class.java)
                                dataStatisticsBean.pagenext = "用户登录页面"
                                saveData()
                                startActivity(mIntent)
                            }
                        6 ->
                            //发布服务
                            if (LhtTool.isLogin) {
                                mIntent = Intent(mContext, SellServicesActivity::class.java)
                                dataStatisticsBean.pagenext = "发布服务页面"
                                saveData()
                                startActivity(mIntent)
                            } else {
                                ToastUtils.showShort("亲，你还没有登陆哦~")
                                dataStatisticsBean.pagenext = "用户登陆界面"
                                saveData()
                                startActivity(Intent(this.activity, UserLoginActivity::class.java))
                            }
                        7 -> if (LhtTool.isLogin) {
                            mIntent = Intent(mContext, MyServiceActivity::class.java)
                            dataStatisticsBean.pagenext = "我的服务页面"
                            saveData()
                            startActivity(mIntent)
                        } else {
                            ToastUtils.showShort("亲，你还没有登陆哦~")
                            dataStatisticsBean.pagenext = "用户登陆界面"
                            saveData()
                            startActivity(Intent(this.activity, UserLoginActivity::class.java))
                        }
                    }
                }
            }
        }

        mainView.tujian_grid.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (mainView.tujian_grid.count == tuijianList.size) {
                mIntent = Intent(mContext, OrderDetailsActivity::class.java)
                mIntent!!.putExtra("ID", xuQiuList!!.list[position].itemid)
                dataStatisticsBean.pagenext = "推荐的项目"
                saveData()
                mContext!!.startActivity(mIntent)
            }
        }
        mainView.person_wei_btn_fabu.setOnClickListener {
            if (LhtTool.isLogin) {
                mIntent = Intent(mContext, FaBuDemandActivity::class.java)
                dataStatisticsBean.pagenext = "发布需求页面"
                saveData()
                startActivityForResult(mIntent, 1)
            } else {
                CustomToast.showToast(mContext, "亲，你还没登录哦~", Toast.LENGTH_SHORT)
                mIntent = Intent(mContext, UserLoginActivity::class.java)
                startActivity(mIntent)
            }
        }

    }

    private fun isLoginFresh() {
        myList!!.isLoadEnable = false
        if (LhtTool.isLogin) {
            map["userid"] = MyApplication.userInfo!!.userID
            map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
            map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
            map["num"] = 10
            map["pages"] = 1
            map["type"] = 0
            doRefresh()
        } else {
            mView!!.person_wei_fabu_xuqiu.visibility = View.VISIBLE
        }
    }

    private fun doRefresh() {

        map["pages"] = "1"
        OkhttpTool.getOkhttpTool().post(UrlConfig.GET_FABU_LIST, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.e("personxuqiu", s)
                    xuQiuList = Gson().fromJson(s, XuQiuList::class.java)
                    if (xuQiuList!!.err_code == "0") {
                        hd.sendEmptyMessage(GETINFO)
                    } else {
                        hd.sendEmptyMessage(GUZHULISTERROR)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onLoad() {
        myList!!.isLoadEnable = true
        Handler().postDelayed({
            myList!!.isLoadEnable = false
            CustomToast.showToast(activity, "加载完成", Toast.LENGTH_SHORT)
        }, 4000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> isLoginFresh()
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.person_setting -> if (LhtTool.isLogin) {
                dataStatisticsBean.pagenext = "设置页面"
                saveData()
                startActivityForResult(Intent(mContext, SheZhiActivity::class.java), TO_OTHER)
            } else {
                CustomToast.showToast(mContext, "亲，你还没有登录哦~", Toast.LENGTH_SHORT)
            }
            R.id.person_chakan_cjl -> if (LhtTool.isLogin) {
                mIntent = Intent(mContext, WebViewActivity::class.java)
                mIntent!!.putExtra("weburl", "http://app.680.com/touch/items/month3data.aspx?userid=" + MyApplication.userInfo!!.userID
                        + "&" + "vkuserip=" + MyApplication.userInfo!!.cookieLoginIpt + "&" + "vktoken=" + MyApplication.userInfo!!.cookieLoginToken)
                mIntent!!.putExtra("title", "近三月成交")
                dataStatisticsBean.pagenext = "近三月成交页面"
                saveData()
                startActivityForResult(mIntent, 1)
            }
            R.id.person_qiandao_btn -> if (userMeassBean != null && userMeassBean!!.vkdata != null && userMeassBean!!.vkdata.vk_sign != null) {
                if (userMeassBean!!.vkdata.vk_sign == "0")
                    mainView.person_qiandao_text!!.visibility = View.GONE
                qiandaoData()
            }
            R.id.shouru_show -> if (LhtTool.isLogin) {
                dataStatisticsBean.pagenext = "收入页面"
                saveData()
                startActivity(Intent(mContext, PersonCaiFuActivity::class.java))
            } else {
                CustomToast.showToast(mContext, "亲，你还没有登录哦~", Toast.LENGTH_SHORT)
            }
            R.id.person_yue_tixian_btn -> {
            }
            R.id.yue_show -> {
                mIntent = Intent(mContext, RechargeActivity::class.java)
                dataStatisticsBean.pagenext = "余额页面"
                saveData()
                startActivity(mIntent)
            }
            R.id.guanzhu_show -> if (LhtTool.isLogin) {
                mIntent = Intent(mContext, MyCollectionActivity::class.java)
                dataStatisticsBean.pagenext = "我的收藏页面"
                saveData()
                startActivity(mIntent)
            } else {
                CustomToast.showToast(mContext, "亲，你还没登录哦~", Toast.LENGTH_SHORT)
            }
            R.id.dingzhi_show -> {
                mIntent = Intent(mContext, WebViewActivity::class.java)
                mIntent!!.putExtra("weburl", "http://apps.680.com/user/customized/info.aspx?userid=" + MyApplication.userInfo!!.userID
                        + "&" + "vkuserip=" + MyApplication.userInfo!!.cookieLoginIpt + "&" + "vktoken=" + MyApplication.userInfo!!.cookieLoginToken)
                mIntent!!.putExtra("title", "定制")
                dataStatisticsBean.pagenext = "定制页面"
                saveData()
                startActivityForResult(mIntent, 1)
            }
            R.id.person_head_btn_login -> {
                if (!LhtTool.isLogin) {
                    startActivity(Intent(this.activity, UserLoginActivity::class.java))
                }
            }
        }
    }

    /**
     * 签到方法
     */
    private fun qiandaoData() {
        val qianURL = "http://app.680.com/api/signinsjb.ashx"
        val qianMap = HashMap<String, Any>()
        qianMap["vkuserid"] = MyApplication.userInfo!!.userID
        qianMap["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
        qianMap["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
        OkhttpTool.getOkhttpTool().post(qianURL, qianMap, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }


            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    Log.e("qiandao", s)
                    val repo = s.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (repo[0] == "ok") {
                        hd.sendEmptyMessage(QIANDAO)
                    } else {

                    }
                } catch (e: Exception) {
                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                    e.printStackTrace()
                }
            }
        })
    }

    fun Blance_tx() {
        mapTi["userid"] = MyApplication.userInfo!!.userID
        mapTi["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
        mapTi["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
        mapTi["type"] = "1"
        ld = LoadingDialog(mContext!!).setMessage("加载中.....")
        ld!!.show()
        OkhttpTool.getOkhttpTool().post(UrlConfig.TX_VERIFICATION, mapTi, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {


                try {
                    val s = response.body()!!.string()
                    LogUtils.d("===================Response:$s")
                    tiXianSureGson = Gson().fromJson(s, TiXianSureGson::class.java)
                    hd.sendEmptyMessage(TX_YANZHENG)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                //                .fitsSystemWindows(true)
                .autoDarkModeEnable(true, 0.2f)
                .init()
    }

    companion object {
        private val QIANDAO = 8
        private val USERMESSAGE = 4
        private val TUISONGXIANGMU = 9
        private val GETINFO = 26
        private val GUZHULISTERROR = 24
    }
}