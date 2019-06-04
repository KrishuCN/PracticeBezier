package chen.vike.c680.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import chen.vike.c680.Interface.ViewItemClick
import chen.vike.c680.activity.FaBuDemandActivity
import chen.vike.c680.activity.ServiceDetailsActivity
import chen.vike.c680.activity.ShopDetailsActivity
import chen.vike.c680.activity.TaskDetailsActivity
import chen.vike.c680.adapter.*
import chen.vike.c680.bean.GridViewInfoBean
import chen.vike.c680.bean.GuZhuBean
import chen.vike.c680.bean.GuZhuMoreBean
import chen.vike.c680.bean.WeiKeModel01Bean
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.*
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.MyGridView
import chen.vike.c680.views.MyListView
import chen.vike.c680.views.RollHeaderView
import chen.vike.c680.webview.WebNoTitleActivity
import chen.vike.c680.webview.WebViewActivity
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.gyf.barlibrary.ImmersionBar
import com.gyf.barlibrary.SimpleImmersionFragment
import com.lht.vike.a680_v1.R
import com.superluo.textbannerlibrary.TextBannerView
import kotterknife.bindView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2018/5/23.
 *
 * 雇主首页页面
 */

class GuZhuNewFragment : SimpleImmersionFragment(), MyListView.OnLoadListener, View.OnClickListener {
    val guzhuBanner: RollHeaderView by bindView(R.id.guzhu_banner)
    val guzhuFabuXuqiu: LinearLayout by bindView(R.id.guzhu_fabu_xuqiu)
    val guzhuZhishiCq: LinearLayout by bindView(R.id.guzhu_zhishi_cq)
    val guzhuFenleiList: RecyclerView by bindView(R.id.guzhu_fenlei_list)
    val guzhuGuanggaoImg: ImageView by bindView(R.id.guzhu_guanggao_img)
    val guzhuXuqiuLunbo: TextBannerView by bindView(R.id.guzhu_xuqiu_lunbo)
    val guzhuZhishiLunbo: TextBannerView by bindView(R.id.guzhu_zhishi_lunbo)
    private var guzhuRencaiGrid: MyGridView? = null
    private var guzhuKefuGrid: MyGridView? = null
    private var guzhu_search: TextView? = null
    private var guzhu_help: ImageView? = null
    private var intent: Intent? = null
    private var guzhuView: View? = null
    private var viewHead: View? = null
    private var mContext: Context? = null
    private var guzhuLv: MyListView? = null
    private var guzhuFresh: SwipeRefreshLayout? = null
    private var myadapter: TuiSongXiangMuAdapter? = null
    private val renCaiName = arrayOf("LOGO设计", "VI设计", "海报设计", "包装设计", "宣传册页"
            , "新房装修", "整站建设", "软件开发", "APP开发", "品牌取名", "公司起名", "更多")
    private val renCaiImg = intArrayOf(R.mipmap.guzhu_logo_sehji_img, R.mipmap.guzhu_vi_img, R.mipmap.guzhu_haibao_img, R.mipmap.guzhu_baozhuang_img, R.mipmap.guzhu_xuanchuan_img, R.mipmap.guzhu_zhuangxiu_img, R.mipmap.guzhu_wangzhan_img, R.mipmap.guzhu_ruanjian_img, R.mipmap.guzhu_app_img, R.mipmap.guzhu_pinpai_img, R.mipmap.guzhu_gongsi_img, R.mipmap.guzhu_more_img)
    private val names = arrayOf("平面设计", "软件开发", "装修设计", "网站建设", "翻译服务", "工业设计"
            , "建筑设计", "取名改名", "生活服务", "文案写作", "移动应用")
    private val fImages = intArrayOf(R.mipmap.guzhu_f_sheji_img, R.mipmap.guzhu_f_ruanjian_img, R.mipmap.guzhu_f_zhuangxiu_img, R.mipmap.guzhu_f_wangzhan_img, R.mipmap.guzhu_f_fanyi_img, R.mipmap.guzhu_f_gongye_img, R.mipmap.guzhu_f_jianzu_img, R.mipmap.guzhu_f_quming_img, R.mipmap.guzhu_f_shenghuo_img, R.mipmap.guzhu_f_wenan_img, R.mipmap.guzhu_f_yidong_img)
    private val guzhuBID = arrayOf("1", "34", "15", "2",
            "10", "8", "9", "16", "11", "4", "35")
    private val fenleiStr = arrayOf("Sheji", "Soft", "Zhuangxiu", "Web", "Fanyi"
            , "Gongye", "Jianzhu", "Qiming", "Fuwu", "Cehua", "Mobile")
    private val list_gone = ArrayList<GridViewInfoBean>()
    private var myGVadapter: MyGVadapter? = null
    private val list_kefu = ArrayList<GridViewInfoBean>()
    private val list_fenlei = ArrayList<GridViewInfoBean>()
    private var guZhuKeFuAdapter: GuZhuKeFuAdapter? = null
    private var guZhuFenLeiAdapter: GuZhuFenLeiAdapter? = null
    private var myListViewadapter: MyListViewadapter? = null
    private val guzhuList = ArrayList<GuZhuBean.CainixihuanBean>()
    private var manager: LinearLayoutManager? = null
    private var guzhuBannerImageUrlList: MutableList<String>? = null
    private var dataStatisticsBean = DataStatisticsBean()
    private var aCache: ACache? = null//轻量级缓存
    private val guzhuHd: MHandler = MHandler(this)
    private var guZhuMoreBean: GuZhuMoreBean? = GuZhuMoreBean()
    private var fuwuLoad: MutableList<GuZhuBean.CainixihuanBean>? = null

    /**
     * 存储页面数据
     */
    private var userid = ""
    private var pagename = ""
    private var pageindex = ""
    private var pageinittime = ""

    /**
     * 文字轮播
     */
    internal var data1: MutableList<WeiKeModel01Bean> = ArrayList()
    internal var data2: MutableList<WeiKeModel01Bean> = ArrayList()
    private val xuqius = ArrayList<String>()
    private val zhishis = ArrayList<String>()

    /**
     * 雇主页面网络数据
     */
    private val guzhuMap = HashMap<String, Any>()
    private var guZhuBean = GuZhuBean()
    private val NETWORK_EXCEPTION = 0X111

    /**
     * 按分类找人才监听
     */
    private val fenClick = ViewItemClick { position ->
        intent = Intent(mContext, WebNoTitleActivity::class.java)
        intent!!.putExtra("weburl", UrlConfig.BASE_URL + fenleiStr[position] + "/shop.html")
        dataStatisticsBean.pagenext = "搜索人才列表页面    "
        stopStorage()
        startActivity(intent)
    }

    /**
     * 客服监听
     */
    private var tel = ""

    /**
     * 客服数据
     */
    internal var kefuTel: MutableList<String> = ArrayList()

    /**
     * 雇主列表数据
     */
    private var pageNum = 1

    private class MHandler(fragment: GuZhuNewFragment) : Handler() {
        private val weakReference: WeakReference<GuZhuNewFragment> = WeakReference(fragment)
        private var fragment = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            fragment?.run {
                when (msg?.what) {
                    GUZHUDATASUCCES -> {
                        guzhuBannerImageUrlList = ArrayList()
                        for (i in 0 until guZhuBean.tophuandeng.size) {
                            guzhuBannerImageUrlList!!.add(guZhuBean.tophuandeng[i].imgurl)
                        }
                        if (!guzhuBannerImageUrlList.isNullOrEmpty()) {
                            guzhuBanner.setImgUrlData(guzhuBannerImageUrlList)
                        }

                        try {
                            if (!guZhuBean.guanggaoinfo.imgurl.isNullOrEmpty() && guZhuBean.guanggaoinfo != null) {
                                ImageLoadUtils.display(mContext!!,guzhuGuanggaoImg,guZhuBean.guanggaoinfo.imgurl)
                            }
                        } catch (e: Exception) {
                           ToastUtils.showShort(MyApplication.INSTANCE.getString(R.string.net_error), Toast.LENGTH_SHORT)
                        }
                        setFbZscq()
                        setKeFu()
                        setListView()
                    }
                    GUZHUDATAERROR -> CustomToast.showToast(mContext, guZhuBean.err_msg + "", Toast.LENGTH_SHORT)
                    GUZHULODAE -> {
                        myListViewadapter!!.addlist(fuwuLoad!!.toMutableList())
                        myListViewadapter!!.notifyDataSetChanged()
                        guzhuLv!!.onLoadComplete()
                    }
                    NETWORK_EXCEPTION -> LhtTool.showNetworkException(mContext, msg)
                    else -> {}
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        for (i in renCaiName.indices) {
            val info = GridViewInfoBean()
            info.image = renCaiImg[i]
            info.name = renCaiName[i]
            list_gone.add(info)
        }
        for (i in 0..2) {
            val kefuInfo = GridViewInfoBean()
            kefuInfo.image = R.mipmap.guzhu_kefu_img
            kefuInfo.name = "设计服务"
            list_kefu.add(kefuInfo)
        }
        for (i in names.indices) {
            val fenlei = GridViewInfoBean()
            fenlei.name = names[i]
            fenlei.image = fImages[i]
            list_fenlei.add(fenlei)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        guzhuView = LayoutInflater.from(mContext).inflate(R.layout.fragment_guzhu, null)
        viewHead = LayoutInflater.from(mContext).inflate(R.layout.guzhu_fragment_head_item, null)
        guzhuRencaiGrid = viewHead!!.findViewById(R.id.guzhu_rencai_grid)
        guzhuKefuGrid = viewHead!!.findViewById(R.id.guzhu_kefu_grid)
        return guzhuView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setView()
        aCache = ACache.get(mContext)
        if (aCache!!.getAsString("guZhuBean") != null) {
            val userMessage = aCache!!.getAsString("guZhuBean")
            try {
                guZhuBean = Gson().fromJson(userMessage, GuZhuBean::class.java)
                guzhuHd.sendEmptyMessage(GUZHUDATASUCCES)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        storage("雇主页面")
    }

    private fun storage(pagenameStr: String) {
        dataStatisticsBean = DataStatisticsBean()
        if (LhtTool.isLogin) {
            userid = MyApplication.userInfo?.userID!!
        } else {
            userid = "未登录"
        }
        pagename = pagenameStr
        pageindex = pagenameStr
        pageinittime = System.currentTimeMillis().toString() + ""
        DataTools.saveData(dataStatisticsBean, userid, pagename, pageindex, pageinittime)

    }

    /**
     * 保存数据
     */
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
    private fun initView() {
        guzhuLv = guzhuView!!.findViewById(R.id.guzhu_lv)
        guzhuFresh = guzhuView!!.findViewById(R.id.guzhu_swipe)
        myadapter = TuiSongXiangMuAdapter(MyApplication.INSTANCE)

        //按专长找人才
        myGVadapter = MyGVadapter(mContext, list_gone)
        //客服
        guZhuKeFuAdapter = GuZhuKeFuAdapter(mContext, list_kefu)
        //按分类找人才
        guZhuFenLeiAdapter = GuZhuFenLeiAdapter(mContext!!, list_fenlei)
        manager = LinearLayoutManager(mContext)
        //雇主页面列表数据
        myListViewadapter = MyListViewadapter(mContext, guzhuList)

        guzhu_search = guzhuView!!.findViewById(R.id.search)
        guzhu_help = guzhuView!!.findViewById(R.id.guzhu_help)
    }

    /**
     * 处理视图
     */
    private fun setView() {
        //整个list
//        guzhuLv!!.adapter = myadapter
        guzhuLv!!.addHeaderView(viewHead)

        //按专长找人才
        guzhuRencaiGrid!!.adapter = myGVadapter
        //客服
        guzhuKefuGrid!!.adapter = guZhuKeFuAdapter
        //按分类找人才
        manager!!.orientation = LinearLayoutManager.HORIZONTAL
        guZhuFenLeiAdapter!!.click = fenClick
        guzhuFenleiList.layoutManager = manager
        guzhuFenleiList.adapter = guZhuFenLeiAdapter
        //雇主列表数据
        guzhuFresh!!.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"))
        guzhuLv!!.adapter = myListViewadapter
        guzhuFresh!!.isRefreshing = true
        guzhuLv!!.setOnLoadListener(this)
        grivListener()//grid监听事件
        viewListener()//guzhuView监听事件
        kefuListener()//客服监听事件
        guzhuBannerListener()//雇主头部banner监听事件
        guzhuData()//雇主页面网络数据
        faBuAndZhishi()//发布需求 知识产权
    }

    private fun guzhuData() {
        if (LhtTool.isLogin) {
            guzhuMap["userid"] = MyApplication.userInfo!!.userID + ""
        } else {
            guzhuMap["userid"] = "0"
        }
        OkhttpTool.getOkhttpTool().post(UrlConfig.GUZHUPAGE, guzhuMap, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(guzhuHd, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
//                Log.e("guzhupage", s + "")
                try {
                    guZhuBean = Gson().fromJson(s, GuZhuBean::class.java)
                    aCache?.put("guZhuBean", s, 5 * ACache.TIME_DAY)
                    if (guZhuBean.err_code == "0") {
                        guzhuHd.sendEmptyMessage(GUZHUDATASUCCES)
                    } else {
                        guzhuHd.sendEmptyMessage(GUZHUDATAERROR)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    /**
     *  发布需求and知识产权
     */
    private fun faBuAndZhishi() {
        guzhuFabuXuqiu.setOnClickListener(this@GuZhuNewFragment)
        guzhuZhishiCq.setOnClickListener(this@GuZhuNewFragment)
        guzhuGuanggaoImg.setOnClickListener(this@GuZhuNewFragment)
    }

    /**
     * 雇主头部banner监听事件
     */
    private fun guzhuBannerListener() {
        guzhuBanner.setOnHeaderViewClickListener { position ->
            val type = guZhuBean.tophuandeng[position].infotype
            val id = guZhuBean.tophuandeng[position].infoid
            guangGaoTiaoZhuan(type, id)
        }
    }

    /**
     * 广告跳转
     */
    private fun guangGaoTiaoZhuan(type: String?, id: String) {
        if (type != null) {
            when (type) {
                "shop" -> {
                    intent = Intent(mContext, ShopDetailsActivity::class.java)
                    intent!!.putExtra("ID", id)
                    dataStatisticsBean.pagenext = "店铺详情"
                    stopStorage()
                    startActivity(intent)
                }
                "fuwu" -> {
                    intent = Intent(mContext, ServiceDetailsActivity::class.java)
                    intent!!.putExtra("ID", id)
                    dataStatisticsBean.pagenext = "服务详情"
                    stopStorage()
                    startActivity(intent)
                }
                "item" -> {
                    intent = Intent(mContext, TaskDetailsActivity::class.java)
                    intent!!.putExtra("ID", id)
                    dataStatisticsBean.pagenext = "任务详情"
                    stopStorage()
                    startActivity(intent)
                }
            }
        }
    }

    /**
     * guzhuView监听事件
     */
    private fun viewListener() {
        guzhu_search!!.setOnClickListener {
            intent = Intent(mContext, WebNoTitleActivity::class.java)
            intent!!.putExtra("weburl", UrlConfig.SHOUSUOFUWU)
            dataStatisticsBean.pagenext = "搜索服务列表页面"
            stopStorage()
            startActivity(intent)
        }
        guzhu_help!!.setOnClickListener {
            intent = Intent(mContext, WebViewActivity::class.java)
            intent!!.putExtra("weburl", UrlConfig.GUZHU_HELP)
            intent!!.putExtra("title", "帮助")
            dataStatisticsBean.pagenext = "帮助页面"
            stopStorage()
            startActivity(intent)
        }
        guzhuFresh!!.setOnRefreshListener { guzhuData() }
    }

    /**
     * 按专长找人才监听
     */
    private fun grivListener() {
        guzhuRencaiGrid!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> rencaiTiao("logo", "2")
                1 -> rencaiTiao("vi", "4")
                2 -> rencaiTiao("haibao", "17")
                3 -> rencaiTiao("baozhuang", "5")
                4 -> rencaiTiao("xuanchuance", "55")
                5 -> rencaiTiao("xinfang", "148")
                6 -> rencaiTiao("jianzhan", "64")
                7 -> rencaiTiao("Soft", "11")
                8 -> rencaiTiao("app", "393")
                9 -> rencaiTiao("pinpai", "156")
                10 -> rencaiTiao("gongsi", "28")
                11 -> rencaiTiao("", "")
            }
        }
    }

    /**
     * 按专长找人才跳转方法
     */
    private fun rencaiTiao(bid: String, sid: String) {
        intent = Intent(mContext, WebNoTitleActivity::class.java)
        intent!!.putExtra("weburl", "http://app.680.com/$bid/shop.html")
        dataStatisticsBean.pagenext = "搜索人才列表页面    "
        stopStorage()
        startActivity(intent)

    }

    private fun kefuListener() {
        guzhuKefuGrid!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> if (kefuTel.size > 0) {
                    tel = "tel:" + kefuTel[0]
                    bodaPhone(tel)
                }
                1 -> if (kefuTel.size > 0) {
                    tel = "tel:" + kefuTel[1]
                    bodaPhone(tel)
                }
                2 -> if (kefuTel.size > 0) {
                    tel = "tel:" + kefuTel[2]
                    bodaPhone(tel)
                }
            }
        }
    }

    /**
     * 拨打电话
     */
    private fun bodaPhone(tel: String) {
        if (Build.VERSION.SDK_INT >= M) {
            val checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext!!, Manifest.permission.CALL_PHONE)

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CALL_PHONE),
                        100)
                return
            } else {
                callPhone(tel)
            }
        }
    }

    /**
     * 拨号方法
     */
    private fun callPhone(tel: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_CALL
        intent.data = Uri.parse(tel)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone(tel)
            } else {
                // Permission Denied
                Toast.makeText(mContext, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onStop() {
        super.onStop()
        guzhuXuqiuLunbo.stopViewAnimator()
        guzhuXuqiuLunbo.stopViewAnimator()
    }

    /**
     * 发布需求，知识产权
     */
    private fun setFbZscq() {
        if (guZhuBean.fabulink != null && guZhuBean.fabulink.size > 0) {
            if (data1.size > 0) {
                data1.clear()
            }
            for (i in 0..2) {
                data1.add(WeiKeModel01Bean(guZhuBean.fabulink[i].name + "", ""))
                xuqius.add(guZhuBean.fabulink[i].name + "")
            }
        }
        if (guZhuBean.zscqlink != null && guZhuBean.zscqlink.size > 0) {
            if (data2.size > 0) {
                data2.clear()
            }
            for (i in 0..2) {
                data2.add(WeiKeModel01Bean(guZhuBean.zscqlink[i].name + "", guZhuBean.zscqlink[i].id, guZhuBean.zscqlink[i].price))
                zhishis.add(guZhuBean.zscqlink[i].name + "")
            }
        }
        guzhuXuqiuLunbo.setDatas(xuqius)
        guzhuXuqiuLunbo.startViewAnimator()
        guzhuZhishiLunbo.setDatas(zhishis)
        guzhuZhishiLunbo.startViewAnimator()
    }

    private fun setKeFu() {
        if (guZhuBean.kefulist != null && guZhuBean.kefulist.size > 0) {
            if (kefuTel.size > 0) {
                kefuTel.clear()
            }
            if (list_kefu.size > 0) {
                list_kefu.clear()
            }
            for (i in 0 until guZhuBean.kefulist.size) {
                val kefuInfo = GridViewInfoBean()
                kefuTel.add(guZhuBean.kefulist[i].tel + "")
                kefuInfo.imgUrl = guZhuBean.kefulist[i].imgurl + ""
                kefuInfo.name = guZhuBean.kefulist[i].name + ""
                list_kefu.add(kefuInfo)
            }
            guZhuKeFuAdapter!!.notifyDataSetChanged()
        }

    }

    private fun setListView() {
        pageNum = 1
        UrlConfig.alipay_flag = guZhuBean.alipay_config.alipay_flag
        UrlConfig.PARTNER = guZhuBean.alipay_config.partner
        UrlConfig.SELLER = guZhuBean.alipay_config.seller
        UrlConfig.RSA_PRIVATE = guZhuBean.alipay_config.private_key
        UrlConfig.RSA_PUBLIC = guZhuBean.alipay_config.public_key
        UrlConfig.weixinpay_flag = guZhuBean.weixinpay_config.weixinpay_flag
        guzhuLv!!.isLoadEnable = true
        if (myListViewadapter!!.count > 0) {
            myListViewadapter!!.refresh()
        }
        myListViewadapter!!.addlist(guZhuBean.cainixihuan)
        myListViewadapter!!.notifyDataSetChanged()
        guzhuFresh!!.isRefreshing = false
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.guzhu_fabu_xuqiu -> {
                intent = Intent(mContext, FaBuDemandActivity::class.java)
                dataStatisticsBean.pagenext = "发布需求页面"
                stopStorage()
                startActivity(intent)
            }
            R.id.guzhu_zhishi_cq -> {
                intent = Intent(mContext, WebViewActivity::class.java)
                intent!!.putExtra("weburl", "http://app.680.com/sipo/")
                intent!!.putExtra("title", "知识产权服务")
                dataStatisticsBean.pagenext = "知识产权服务页面    "
                stopStorage()
                startActivityForResult(intent, 1)
            }
            R.id.guzhu_guanggao_img -> {
                guZhuBean.guanggaoinfo?.let {
                    val type = guZhuBean.guanggaoinfo.infotype
                    val id = guZhuBean.guanggaoinfo.infoid
                    guangGaoTiaoZhuan(type, id)
                }
            }
        }
    }

    override fun onLoad() {
        pageNum++
        val map = HashMap<String, Any>()
        if (LhtTool.isLogin) {
            if (MyApplication.userInfo != null) {
                map["userid"] = MyApplication.userInfo!!.userID + ""
            }
        } else {
            map["userid"] = "0"
        }

        if (pageNum == 2) {
            map["pages"] = pageNum.toString() + ""
        } else if (pageNum > 2 && guZhuMoreBean!!.pagerInfo.nextPageIndex != 0) {
            map["pages"] = pageNum.toString() + ""
        } else {
            CustomToast.showToast(mContext, "已经是最后一页了!", Toast.LENGTH_SHORT)
            guzhuLv!!.isLoadEnable = false
            return
        }
        map["num"] = "10"
        OkhttpTool.getOkhttpTool().post(UrlConfig.GUZHUMORE, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(guzhuHd, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
                try {
                    guZhuMoreBean = Gson().fromJson(s, GuZhuMoreBean::class.java)
                    fuwuLoad = ArrayList<GuZhuBean.CainixihuanBean>()
                    if (guZhuMoreBean != null && guZhuMoreBean!!.cainixihuan.size > 0) {
                        for (i in 0 until guZhuMoreBean!!.cainixihuan.size) {
                            val listBean = GuZhuBean.CainixihuanBean()
                            listBean.imgurl = guZhuMoreBean!!.cainixihuan[i].imgurl
                            listBean.fuwu_name = guZhuMoreBean!!.cainixihuan[i].fuwu_name
                            listBean.price = guZhuMoreBean!!.cainixihuan[i].price
                            listBean.unit = guZhuMoreBean!!.cainixihuan[i].unit
                            listBean.cityname = guZhuMoreBean!!.cainixihuan[i].cityname
                            listBean.deal_count = guZhuMoreBean!!.cainixihuan[i].deal_count
                            listBean.fuwu_id = guZhuMoreBean!!.cainixihuan[i].fuwu_id
                            listBean.dianpu_name = guZhuMoreBean!!.cainixihuan[i].dianpu_name
                            listBean.pingfen = guZhuMoreBean!!.cainixihuan[i].pingfen
                            fuwuLoad!!.add(listBean)
                        }
                        guzhuHd.sendEmptyMessage(GUZHULODAE)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.color_fa)
                .autoDarkModeEnable(true, 0.2f)
                .init()
    }

    companion object {
        private val GUZHUDATASUCCES = 0X123
        private val GUZHUDATAERROR = 0X120
        private val GUZHULODAE = 0X124
    }
}
