package chen.vike.c680.WeiKe

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import chen.vike.c680.WeiKe.adapter.WeiKeRuZhuAdapter
import chen.vike.c680.activity.*
import chen.vike.c680.adapter.*
import chen.vike.c680.bean.*
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.*
import chen.vike.c680.views.*
import chen.vike.c680.webview.WebNoTitleActivity
import chen.vike.c680.webview.WebViewActivity
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.gyf.barlibrary.ImmersionBar
import com.gyf.barlibrary.SimpleImmersionFragment
import com.lht.vike.a680_v1.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.*

/**
 * Created by lht on 2017/2/22.
 * 服务商页面
 */

class WeiKeFragment : SimpleImmersionFragment(), MyListView.OnLoadListener, WeiKeHexagonView.OnHexagonViewClickListener {

    private var con: Context? = null
    private var textView: TextView? = null
    private var liuchengshuoming: ImageView? = null
    private var search: TextView? = null
    private var myListView: MyListView? = null
    private var srl: SwipeRefreshLayout? = null
    private var rhv: RollHeaderView? = null
    private var iv_hyh: ImageView? = null
    private val image = intArrayOf(R.mipmap.sheji_projec, R.mipmap.tuiguang_projec, R.mipmap.kaifa_projec, R.mipmap.zhuangxiu_projec, R.mipmap.wenan_projec, R.mipmap.gongye_projec)

    private var weiKePageGsons: WeiKePageGson? = null
    private var imageUrlList: MutableList<String>? = null
    private var imageUrlList1: MutableList<String>? = null
    private var weikeMoreGson = WeikeMoreGson()
    private var weikeload: MutableList<WeiKePageGson.ItemListBean> = ArrayList()
    private var vipPageBean: VipPageBean? = VipPageBean()
    private val moneys = ArrayList<String>()

    private val GETINFO = 0X123
    private val HYHINFO = 0x234
    private val WKLOADMORE = 0X456
    private val NETWORKIOEXCEPTION = 0X345

    private val myadapter: MyWKListViewAdapter? = null
    private var filed_id = "1"
    private var weiKePageNum = 1
    private var `in`: Intent? = null
    private var weikeBean: WeikeBean? = WeikeBean()
    private var newTuiMessage: MutableList<WeikeBean.DongtaiListBean>? = ArrayList()//最新动态
    private var shouruPai: List<WeikeBean.ShourupaihangListBean> = ArrayList()
    private val LIST_ruzhuFuwu = ArrayList<WeikeBean.ZuixinruzhuListBean>()
    private var ruzhuFuwu: MutableList<WeikeBean.ZuixinruzhuListBean> = ArrayList()
    private var intent: Intent? = null

    /**
     * 存储页面数据
     */
    private var userid = ""
    private var pagename = ""
    private var pageindex = ""
    private var pageinittime = ""
    private var dataStatisticsBean = DataStatisticsBean()
    private var vip_money_num: TextView? = null
    private var vip_push_num: TextView? = null
    private var vip_follow_num: TextView? = null
    private var vip_over_time: TextView? = null
    private var vip_company_name: TextView? = null
    private val moreProject: Button? = null
    private var gradImg: ImageView? = null
    private val vipImgs = intArrayOf(R.mipmap.vipa0, R.mipmap.vipb1, R.mipmap.vipb2, R.mipmap.vipb3, R.mipmap.vipb4, R.mipmap.vipb5)

    /**
     * vip首页数据
     */
    private val jsonObject: JSONObject? = null
    private val userMes = ArrayList<String>()
    private var vkuserip = ""
    private var vktoken = ""

    /**
     * 非vip未登录的头部
     */
    // private RecyclerView classRecy,classFuwuRecy;
    private val manager: LinearLayoutManager? = null
    private val weikeAdapter: WeikeAdapter? = null
    private val fuwuAdapter: WeiKeNewFuwuAdapter? = null
    private var bannerView: WeiKeVerticalBannerView? = null
    private var bannerView1: WeiKeVerticalBannerView? = null//文字轮播
    private var relativeLayout1: RelativeLayout? = null
    private var relativeLayout2: RelativeLayout? = null
    private var relativeLayout4: RelativeLayout? = null
    private var relativeLayout5: RelativeLayout? = null
    private var relativeLayout6: RelativeLayout? = null
    private var oneName: TextView? = null
    private var twoName: TextView? = null
    private var fourName: TextView? = null
    private var fiveName: TextView? = null
    private var sixName: TextView? = null
    private var oneMon: TextView? = null
    private var twoMon: TextView? = null
    private var fourMon: TextView? = null
    private var fiveMon: TextView? = null
    private var sixMon: TextView? = null
    private var oneFrom: TextView? = null
    private var twoFrom: TextView? = null
    private var fourFrom: TextView? = null
    private var fiveFrom: TextView? = null
    private var sixFrom: TextView? = null
    private var vipXufei: Button? = null
    private var oneImge: ImageView? = null
    private var twoImg: ImageView? = null
    private var fourImg: ImageView? = null
    private var fiveImg: ImageView? = null
    private var sixImg: ImageView? = null
    private val weikeNew: VipViewAnimatorWordComponent? = null
    //private Button ruzhuBtn;
    private var v1: View? = null
    private var xuqiuGrid: MyGridView? = null
    private var weiKeXuFenAdapter: WeiKeXuFenAdapter? = null
    private val xuqiuLists = arrayOf("LOGO设计", "软件开发", "VI设计", "房屋装修", "宣传册页", "品牌起名", "海报设计", "整站建设", "APP开发", "包装设计", "寻求帮助", "更多")

    private val xuqiufenlei = arrayOf("logo", "ruanjian", "vi", "xinfang", "xuanchuance", "pinpai", "haibao", "jianzhan", "app", "baozhuang", "xunqiubangzhu")
    private val xuqiu = LinkedList<GridViewInfoBean>()
    private var wkRuZhuAdapter: WeiKeRuZhuAdapter? = null
    private var ruzhuMore: RelativeLayout? = null
    private var tigong_jineng_fuwu: LinearLayout? = null
    private var tigong_shijian_fuwu: LinearLayout? = null
    private var vipHead: LinearLayout? = null
    private var jiaru_img: ImageView? = null
    private var name: String? = ""
    private var isPrepared = false //fragment切换标志位
    /**
     * 需求监听事件
     */
    private var intent1: Intent? = null

    /**
     * 文字轮播
     */
    internal lateinit var sampleAdapter03: WeiKeSampleAdapter03
    internal lateinit var sampleAdapter032: WeiKeSampleAdapter03
    internal var data1: MutableList<WeiKeModel01Bean>? = ArrayList()
    internal var data2: MutableList<WeiKeModel01Bean> = ArrayList()


    @SuppressLint("HandlerLeak")
    private val hd = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == GETINFO) {
                imageUrlList = ArrayList()
                if (weiKePageGsons!!.tophuandeng != null) {
                    if (weiKePageGsons!!.tophuandeng.size > 0) {
                        for (i in 0 until weiKePageGsons!!.tophuandeng.size) {
                            imageUrlList!!.add(weiKePageGsons!!.tophuandeng[i].imgurl)
                        }
                        rhv!!.setImgUrlData(imageUrlList)
                        imageUrlList1 = ArrayList()
                        for (i in 0 until weiKePageGsons!!.tabImage2.size) {
                            imageUrlList1!!.add(weiKePageGsons!!.tabImage2[i].imgurl)
                        }
                    }
                }
                if (myadapter != null) {
                    if (myadapter.count > 0) {
                        myadapter.refresh()
                    }

                    myadapter.addlist(weiKePageGsons!!.item_list)
                    myadapter.notifyDataSetChanged()

                }
                srl!!.isRefreshing = false


            } else if (msg.what == NETWORKIOEXCEPTION) {
                LhtTool.showNetworkException(con, msg)
            } else if (msg.what == WKLOADMORE) {
                if (myadapter != null) {
                    myadapter.addlist(weikeload)
                    myadapter.notifyDataSetChanged()
                }
                myListView!!.onLoadComplete()
            } else if (msg.what == HYHINFO) {
                weiKePageNum = 1
                myListView!!.isLoadEnable = true
                filed_id = weikeload[0].field_id
                myadapter!!.refresh()
                myadapter.addlist(weikeload)
                myadapter.notifyDataSetChanged()
                iv_hyh!!.setImageResource(R.mipmap.huanyihuan1)
            } else if (msg.what == LOADDATA) {
                Log.e("ruzhu", ruzhuFuwu.size.toString() + "")
                if (wkRuZhuAdapter!!.count > 0) {
                    wkRuZhuAdapter!!.refresh()
                }
                Log.e("ruzhu", ruzhuFuwu.size.toString() + "")
                wkRuZhuAdapter!!.addlist(ruzhuFuwu)
                wkRuZhuAdapter!!.notifyDataSetChanged()
                val newData = ArrayList<WeiKeModel01Bean>()
                if (data1 != null && data1!!.size > 0) {
                    data1!!.clear()
                    data2.clear()
                }
                if (newTuiMessage!!.size > 0 && newTuiMessage != null) {
                    for (i in newTuiMessage!!.indices) {
                        data1!!.add(WeiKeModel01Bean(newTuiMessage!![0].info, newTuiMessage!![0].userid))
                        data1!!.add(WeiKeModel01Bean(newTuiMessage!![1].info, newTuiMessage!![1].userid))
                        data2.add(WeiKeModel01Bean(newTuiMessage!![2].info, newTuiMessage!![2].userid))
                        data2.add(WeiKeModel01Bean(newTuiMessage!![3].info, newTuiMessage!![3].userid))
                    }
                }
                sampleAdapter03.setData(data1)
                sampleAdapter032.setData(data2)
                bannerView!!.start()
                bannerView1!!.start()
                setPaiHang()
                myListView!!.isLoadEnable = false
            }

        }
    }
    @SuppressLint("HandlerLeak")
    private val hdVip = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == GETINFO) {
                imageUrlList = ArrayList()
                for (i in 0 until weiKePageGsons!!.tabImage.size) {
                    imageUrlList!!.add(weiKePageGsons!!.tabImage[i].imgurl)
                }
                myadapter!!.addlist(weiKePageGsons!!.item_list)
                myadapter.notifyDataSetChanged()
                srl!!.isRefreshing = false
            } else if (msg.what == NETWORKIOEXCEPTION) {
                LhtTool.showNetworkException(con, msg)
            } else if (msg.what == WKLOADMORE) {
                if (myadapter != null) {
                    if (myadapter.count > 0) {
                        myadapter.refresh()
                    }
                    myadapter.addlist(weikeload)
                    myadapter.notifyDataSetChanged()
                }
                //if(ConfigTools.VIPMARK > 3) {
                setPageText()
                // }
                myListView!!.onLoadComplete()
                srl!!.isRefreshing = false
            } else if (msg.what == HYHINFO) {
                myListView!!.isLoadEnable = true
                filed_id = weikeload[0].field_id
                myadapter!!.refresh()
                myadapter.addlist(weikeload)
                myadapter.notifyDataSetChanged()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        con = activity

        for (i in xuqiuLists.indices) {
            val info = GridViewInfoBean()
            info.name = xuqiuLists[i]
            xuqiu.add(info)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = LayoutInflater.from(con).inflate(R.layout.fragment_weike, null)
        textView = v.findViewById(R.id.guzhu_tab_faxiangmu)
        liuchengshuoming = v.findViewById(R.id.guzhu_tab_tv)
        srl = v.findViewById(R.id.swipe_weike)

        srl!!.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"))

        myListView = v.findViewById(R.id.guzhu_lv_weike)
        search = v.findViewById(R.id.search)
        textView!!.text = "发服务"
        textView!!.setOnClickListener {
            if (LhtTool.isLogin) {

                if (MyApplication.userInfo!!.isShop == "1") {
                    `in` = Intent(con, SellServicesActivity::class.java)
                    `in`!!.putExtra("", "1")
                    dataStatisticsBean.pagenext = "发布服务页面"
                    stopStorage()
                    startActivity(`in`)
                } else {
                    `in` = Intent(con, OpenShopActivity::class.java)
                    dataStatisticsBean.pagenext = "开通店铺页面"
                    stopStorage()
                    startActivity(`in`)
                }
            } else {
                `in` = Intent(con, UserLoginActivity::class.java)
                dataStatisticsBean.pagenext = "用户登录页面"
                stopStorage()
                startActivity(`in`)
            }
        }

        search!!.setOnClickListener {
            intent = Intent(con, WebNoTitleActivity::class.java)
            intent!!.putExtra("weburl", UrlConfig.SOUSUOXIANGMU)
            dataStatisticsBean.pagenext = "搜索项目页面"
            stopStorage()
            startActivity(intent)
        }

        liuchengshuoming!!.setOnClickListener {
            `in` = Intent(con, WebViewActivity::class.java)
            `in`!!.putExtra("weburl", UrlConfig.WEIKE_HELP)
            `in`!!.putExtra("title", "帮助")
            dataStatisticsBean.pagenext = "帮助页面"
            stopStorage()
            startActivity(`in`)
        }
        if (ShardTools.getInstance(con).getTempSharedata("vip等级") != "null") {
            ConfigTools.VIPMARK = Integer.parseInt(ShardTools.getInstance(con).getTempSharedata("vip等级") + "")
            Log.e("TYPAEVIP", ConfigTools.VIPMARK.toString() + "" + ShardTools.getInstance(con).getTempSharedata("vip等级"))
        }
        myListView!!.setOnLoadListener(this)

        httpData()
        weikeData()
        v1ViewPage()//非vip或未登录
        listenerV1Click()// 非vip监听事件
        //   }
        return v
    }

    /**
     * setUserVisVleHint（）方法在 Fragment 1 第一次加载的时候不走，只有在Fragment之间切换的时候走该方法
     * 这里是主动调用改方法防止第一次不走这个方法，但是这里跟immersionbar库有冲突
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        userVisibleHint = true
        super.onActivityCreated(savedInstanceState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //可见的并且是初始化之后才加载
        if (isPrepared && isVisibleToUser) {
            ConfigTools.VIPMARK = Integer.parseInt(ShardTools.getInstance(con).getTempSharedata("vip等级"))
            Log.e("vip", ConfigTools.VIPMARK.toString() + "onresume")
            if (ConfigTools.VIPMARK > 3) {
                vipData()
            } else {
                //  weikeData();
            }
            //   Log.e("userMeaasgesss","fuwushang");
        }
    }

    /**
     *  因为首页四个Fragment全部做了缓存，所以onResume只走一次，代码就在onResume贴了一份
     */
    override fun onResume() {
        super.onResume()
        if (isPrepared) {
            ConfigTools.VIPMARK = Integer.parseInt(ShardTools.getInstance(con).getTempSharedata("vip等级"))
            Log.e("vip", ConfigTools.VIPMARK.toString() + "onresume")
            if (ConfigTools.VIPMARK > 3) {
                vipData()
            } else {
                //  weikeData();
            }
            //   Log.e("userMeaasgesss","fuwushang");
        }
    }

    override fun onStart() {
        super.onStart()
        storage("主页服务商页面")
    }

    private fun storage(pagenameStr: String) {
        dataStatisticsBean = DataStatisticsBean()
        if (LhtTool.isLogin) {
            userid = MyApplication.userInfo!!.userID
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
     * 网络数据
     */
    private fun httpData() {
        OkhttpTool.getOkhttpTool().get(UrlConfig.WEIKE_PAGE_INFO, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORKIOEXCEPTION)
            }


            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    // Log.e("weikehead",s);
                    LogUtils.d("========================response:$s")
                    weiKePageGsons = Gson().fromJson(s, WeiKePageGson::class.java)
                    hd.sendEmptyMessage(GETINFO)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        })
        srl!!.isRefreshing = true
        srl!!.setOnRefreshListener {
            weiKePageNum = 1
            OkhttpTool.getOkhttpTool().get(UrlConfig.WEIKE_PAGE_INFO, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LhtTool.sendMessage(hd, e, NETWORKIOEXCEPTION)
                }


                override fun onResponse(call: Call, response: Response) {
                    try {
                        val s = response.body()!!.string()
                        LogUtils.d("========================response:$s")
                        weiKePageGsons = Gson().fromJson(s, WeiKePageGson::class.java)
                        hd.sendEmptyMessage(GETINFO)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }


    }


    fun vipData() {
        val map = HashMap<String, Any>()

        MyApplication.userInfo?.let {
            userid = it.userID
            vkuserip = it.cookieLoginIpt
            vktoken = it.cookieLoginToken
        }


        map["userid"] = userid
        map["vkuserip"] = vkuserip
        map["vktoken"] = vktoken
        LogUtils.d("vipUserLogin: \n userId = $userid vkUserIp = $vkuserip vkToken = $vktoken")
        OkhttpTool.getOkhttpTool().post(VIPURL, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORKIOEXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
                Log.e("fuwushang vip", s)
                if (weikeload.size > 0) {
                    weikeload.clear()
                }
                try {
                    vipPageBean = Gson().fromJson(s, VipPageBean::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                for (i in 0 until vipPageBean!!.tuisong_item_list.size) {
                    val listBean = WeiKePageGson.ItemListBean()
                    listBean.itemid = vipPageBean!!.tuisong_item_list[i].itemid
                    listBean.itemname = vipPageBean!!.tuisong_item_list[i].itemname
                    listBean.price = vipPageBean!!.tuisong_item_list[i].money
                    listBean.content = vipPageBean!!.tuisong_item_list[i].content
                    listBean.endtime = vipPageBean!!.tuisong_item_list[i].endtime
                    listBean.itemtype = vipPageBean!!.tuisong_item_list[i].zab_do
                    weikeload.add(listBean)
                }
                ShardTools.getInstance(con).tempSaveSharedata("vip等级", vipPageBean!!.viptype)
                for (i in 0 until vipPageBean!!.good_item_list.size) {
                    moneys.add("[" + vipPageBean!!.good_item_list[i].money + "]" + "   " + vipPageBean!!.good_item_list[i].itemname)
                }
                //if(myadapter != null) {
                hdVip.sendEmptyMessage(WKLOADMORE)
                // }
            }

        })
    }

    /**
     * vip首页个人资料
     */
    fun setPageText() {
        if (vipPageBean != null && vip_money_num != null) {
            vip_money_num!!.text = vipPageBean!!.vk_mon_pay + ""
            vip_company_name!!.text = vipPageBean!!.username + ""
            vip_push_num!!.text = vipPageBean!!.tuisong_num.toString() + ""
            vip_over_time!!.text = vipPageBean!!.vip_out_date + ""
            vip_follow_num!!.text = vipPageBean!!.genjin_num.toString() + ""
            when (vipPageBean!!.viptype) {
                "4" -> gradImg!!.setBackgroundResource(vipImgs[0])
                "5" -> gradImg!!.setBackgroundResource(vipImgs[1])
                "6" -> gradImg!!.setBackgroundResource(vipImgs[2])
                "7" -> gradImg!!.setBackgroundResource(vipImgs[3])
                "8" -> gradImg!!.setBackgroundResource(vipImgs[4])
            }
        }
    }

    fun v1ViewPage() {
        v1 = LayoutInflater.from(con).inflate(R.layout.fragment_weike_content, null)
        rhv = v1!!.findViewById(R.id.weike_lunbo)
        iv_hyh = v1!!.findViewById(R.id.weike_hyh)
        xuqiuGrid = v1!!.findViewById(R.id.xuqiu_fen_lei)
        ruzhuMore = v1!!.findViewById(R.id.ruzhu_more)
        vipHead = v1!!.findViewById(R.id.vip_head)
        vip_company_name = v1!!.findViewById(R.id.vip_company_name)
        vip_money_num = v1!!.findViewById(R.id.vip_money_num)
        vip_push_num = v1!!.findViewById(R.id.vip_push_num)
        vip_follow_num = v1!!.findViewById(R.id.vip_follow_num)
        vip_over_time = v1!!.findViewById(R.id.vip_over_time)
        gradImg = v1!!.findViewById(R.id.vip_grad_img)
        jiaru_img = v1!!.findViewById(R.id.img_jiaru_vip_btn)
        tigong_shijian_fuwu = v1!!.findViewById(R.id.tigong_shijian_fuwu)
        tigong_jineng_fuwu = v1!!.findViewById(R.id.tigong_jineng_fuwu)
        vipXufei = v1!!.findViewById(R.id.vip_xufei_btn)
        //需求分类

        weiKeXuFenAdapter = WeiKeXuFenAdapter(con, xuqiu)
        if (weiKeXuFenAdapter != null && xuqiuGrid != null) {
            xuqiuGrid!!.adapter = weiKeXuFenAdapter
        }
        xuqiuListener()
        //文字轮播
        bannerView = v1!!.findViewById(R.id.weike_new_content_banner)
        bannerView1 = v1!!.findViewById(R.id.weike_new_content_banner2)
        textBanner()
        //排行
        iniPaiHang()
        wkRuZhuAdapter = WeiKeRuZhuAdapter(con!!, LIST_ruzhuFuwu)
        myListView!!.adapter = wkRuZhuAdapter
        textView!!.text = "发服务"
        search!!.text = "请输入项目编号/标题"
        myListView!!.addHeaderView(v1)
        isVip()
    }

    private fun isVip() {
        Log.e("isvip", ConfigTools.VIPMARK.toString() + "")
        if (ConfigTools.VIPMARK > 3) {
            vipHead!!.visibility = View.VISIBLE
            rhv!!.visibility = View.GONE
            vipData()
            MyApplication.userInfo?.nickame.let {
                name = it
            }
            if (name != null && name != "") {
                vip_company_name!!.text = name
            }
        } else {
            vipHead!!.visibility = View.GONE
            rhv!!.visibility = View.VISIBLE
        }
    }

    private fun xuqiuListener() {
        jiaru_img!!.setOnClickListener {
            if (LhtTool.isLogin) {
                intent = Intent(con, WeiKeBuyVipActivity::class.java)
                dataStatisticsBean.pagenext = "购买vip页面"
                stopStorage()
                startActivity(intent)
            } else {
                CustomToast.showToast(con, "亲，你还没登录哦~", Toast.LENGTH_SHORT)
                intent = Intent(con, UserLoginActivity::class.java)
                dataStatisticsBean.pagenext = "购买vip-用户登录页面"
                stopStorage()
                startActivity(intent)
            }
        }
        xuqiuGrid!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (position < 11) {
                intent = Intent(con, WebNoTitleActivity::class.java)
                intent!!.putExtra("weburl", "http://app.680.com/" + xuqiufenlei[position] + "/task.html")
                dataStatisticsBean.pagenext = "需求页面"
                stopStorage()
                startActivity(intent)
            } else {
                intent1 = Intent(con, WebNoTitleActivity::class.java)
                intent1!!.putExtra("weburl", UrlConfig.SOUSUOXIANGMU)
                dataStatisticsBean.pagenext = "更多需求页面"
                stopStorage()
                startActivity(intent1)
            }
        }
        ruzhuMore!!.setOnClickListener {
            intent1 = Intent(con, WebNoTitleActivity::class.java)
            intent1!!.putExtra("weburl", UrlConfig.SHOUSUOYEMIAN)
            dataStatisticsBean.pagenext = "入驻服务商页面"
            stopStorage()
            startActivity(intent1)
        }
        tigong_jineng_fuwu!!.setOnClickListener {
            if (LhtTool.isLogin) {
                if (MyApplication.userInfo!!.isShop == "1") {
                    intent1 = Intent(con, SellServicesActivity::class.java)
                    intent1!!.putExtra("", "1")
                    dataStatisticsBean.pagenext = "提供技能服务——发布服务页面"
                    stopStorage()
                    startActivity(intent1)

                } else {
                    intent1 = Intent(con, OpenShopActivity::class.java)
                    dataStatisticsBean.pagenext = "提供技能服务——开通店铺页面"
                    stopStorage()
                    startActivity(intent1)
                }
            } else {
                intent1 = Intent(con, UserLoginActivity::class.java)
                dataStatisticsBean.pagenext = "提供技能服务——用户登录页面"
                stopStorage()
                startActivity(intent1)
            }
        }
        tigong_shijian_fuwu!!.setOnClickListener {
            if (LhtTool.isLogin) {
                if (MyApplication.userInfo!!.isShop == "1") {

                    intent1 = Intent(con, WeiKeFabuTimeServiceActivity::class.java)
                    intent1!!.putExtra("", "1")
                    dataStatisticsBean.pagenext = "发布时间服务页面"
                    stopStorage()
                    startActivity(intent1)

                } else {

                    intent1 = Intent(con, OpenShopActivity::class.java)
                    dataStatisticsBean.pagenext = "发布时间服务——开通店铺页面"
                    stopStorage()
                    startActivity(intent1)

                }

            } else {

                intent1 = Intent(con, UserLoginActivity::class.java)
                dataStatisticsBean.pagenext = "发布时间服务——用户登录页面"
                stopStorage()
                startActivity(intent1)

            }
        }
        vipXufei!!.setOnClickListener {
            intent = Intent(con, WeiKeBuyVipActivity::class.java)
            dataStatisticsBean.pagenext = "vip续费页面"
            stopStorage()
            startActivity(intent)
        }
    }

    /**
     * 收入排行
     */
    private fun iniPaiHang() {
        oneName = v1!!.findViewById(R.id.weike_paiming_name_one)
        twoName = v1!!.findViewById(R.id.weike_paiming_name_two)
        fourName = v1!!.findViewById(R.id.weike_paiming_name_four)
        fiveName = v1!!.findViewById(R.id.weike_paiming_name_five)
        sixName = v1!!.findViewById(R.id.weike_paiming_name_six)
        oneMon = v1!!.findViewById(R.id.weike_paiming_money_one)
        twoMon = v1!!.findViewById(R.id.weike_paiming_money_two)
        fourMon = v1!!.findViewById(R.id.weike_paiming_money_four)
        fiveMon = v1!!.findViewById(R.id.weike_paiming_money_five)
        sixMon = v1!!.findViewById(R.id.weike_paiming_money_six)
        oneImge = v1!!.findViewById(R.id.weike_paiming_img_one)
        twoImg = v1!!.findViewById(R.id.weike_paiming_img_two)
        fourImg = v1!!.findViewById(R.id.weike_paiming_img_four)
        fiveImg = v1!!.findViewById(R.id.weike_paiming_img_five)
        sixImg = v1!!.findViewById(R.id.weike_paiming_img_six)
        relativeLayout4 = v1!!.findViewById(R.id.weike_paihang_four)
        relativeLayout5 = v1!!.findViewById(R.id.weike_paihang_five)
        relativeLayout6 = v1!!.findViewById(R.id.weike_paihang_six)
        relativeLayout1 = v1!!.findViewById(R.id.weike_paihang_one)
        relativeLayout2 = v1!!.findViewById(R.id.weike_paihang_two)
        oneFrom = v1!!.findViewById(R.id.weike_paiming_from_one)
        twoFrom = v1!!.findViewById(R.id.weike_paiming_from_two)
        fourFrom = v1!!.findViewById(R.id.weike_paiming_from_four)
        fiveFrom = v1!!.findViewById(R.id.weike_paiming_from_five)
        sixFrom = v1!!.findViewById(R.id.weike_paiming_from_six)

    }

    private fun setPaiHang() {
        oneName!!.text = shouruPai[0].vkname
        twoName!!.text = shouruPai[1].vkname
        fourName!!.text = shouruPai[2].vkname
        fiveName!!.text = shouruPai[3].vkname
        sixName!!.text = shouruPai[4].vkname
        oneMon!!.text = "￥" + shouruPai[0].zbcount
        twoMon!!.text = "￥" + shouruPai[1].zbcount
        fourMon!!.text = "￥" + shouruPai[2].zbcount
        fiveMon!!.text = "￥" + shouruPai[3].zbcount
        sixMon!!.text = "￥" + shouruPai[4].zbcount
        this@WeiKeFragment.activity?.let {

            val options = RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

            Glide.with(MyApplication.INSTANCE).load(shouruPai[0].imageurl).apply(options).into(oneImge!!)
            Glide.with(MyApplication.INSTANCE).load(shouruPai[1].imageurl).apply(options).into(twoImg!!)
            Glide.with(MyApplication.INSTANCE).load(shouruPai[2].imageurl).apply(options).into(fourImg!!)
            Glide.with(MyApplication.INSTANCE).load(shouruPai[3].imageurl).apply(options).into(fiveImg!!)
            Glide.with(MyApplication.INSTANCE).load(shouruPai[4].imageurl).apply(options).into(sixImg!!)
        }
        oneFrom!!.text = shouruPai[0].diqu
        twoFrom!!.text = shouruPai[1].diqu
        fourFrom!!.text = shouruPai[2].diqu
        fiveFrom!!.text = shouruPai[3].diqu
        sixFrom!!.text = shouruPai[4].diqu

        listenerPaiHang()
    }

    private fun listenerPaiHang() {
        relativeLayout1!!.setOnClickListener {
            `in` = Intent(con, ShopDetailsActivity::class.java)
            `in`!!.putExtra("ID", shouruPai[0].userid)
            startActivity(`in`)
        }
        relativeLayout2!!.setOnClickListener {
            `in` = Intent(con, ShopDetailsActivity::class.java)
            `in`!!.putExtra("ID", shouruPai[1].userid)
            startActivity(`in`)
        }

        relativeLayout4!!.setOnClickListener {
            `in` = Intent(con, ShopDetailsActivity::class.java)
            `in`!!.putExtra("ID", shouruPai[2].userid)
            startActivity(`in`)
        }
        relativeLayout5!!.setOnClickListener {
            `in` = Intent(con, ShopDetailsActivity::class.java)
            `in`!!.putExtra("ID", shouruPai[3].userid)
            startActivity(`in`)
        }
        relativeLayout6!!.setOnClickListener {
            `in` = Intent(con, ShopDetailsActivity::class.java)
            `in`!!.putExtra("ID", shouruPai[4].userid)
            startActivity(`in`)
        }

    }

    private fun textBanner() {
        data1!!.add(WeiKeModel01Bean(""))
        data2.add(WeiKeModel01Bean(""))
        sampleAdapter03 = WeiKeSampleAdapter03(data1, con)
        sampleAdapter032 = WeiKeSampleAdapter03(data2, con)
        bannerView!!.setAdapter(sampleAdapter03)
        bannerView1!!.setAdapter(sampleAdapter032)
    }


    /**
     * 普通威客界面数据
     */

    private fun weikeData() {
        val url = "http://app.680.com/api/v3/index_vk_def_data.ashx"
        val map = HashMap<String, Any>()
        var userid = ""
        MyApplication.userInfo?.userID.let {
            userid = it + ""
        }
        map["userid"] = userid
        map["user_classid"] = 0
        OkhttpTool.getOkhttpTool().post(url, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORKIOEXCEPTION)
            }


            override fun onResponse(call: Call, response: Response) {

                val s = response.body()!!.string()
                LogUtils.d("服务商: \n $s")
                try {
                    weikeBean = Gson().fromJson(s, WeikeBean::class.java)
                    if (weikeBean != null && newTuiMessage != null) {
                        if (newTuiMessage!!.size > 0) {
                            newTuiMessage!!.clear()
                        }
                        if (newTuiMessage == null) {
                            newTuiMessage = ArrayList()
                        }
                        if (ruzhuFuwu.size > 0) {
                            ruzhuFuwu.clear()
                        }
                        ruzhuFuwu = weikeBean!!.zuixinruzhu_list
                        newTuiMessage = weikeBean!!.dongtai_list
                        shouruPai = weikeBean!!.shourupaihang_list
                        Log.e("ruzhu", ruzhuFuwu.size.toString() + "")
                        hd.sendEmptyMessage(LOADDATA)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    /**
     * 非vip
     */
    fun listenerV1Click() {

        rhv!!.setOnHeaderViewClickListener { position ->
            val type = weiKePageGsons!!.tophuandeng[position].infotype
            if (type != null) {
                when (type) {
                    "shop" -> {
                        `in` = Intent(con, ShopDetailsActivity::class.java)
                        `in`!!.putExtra("ID", weiKePageGsons!!.tophuandeng[position].infoid)
                        startActivity(`in`)
                    }
                    "fuwu" -> {
                        `in` = Intent(con, ServiceDetailsActivity::class.java)
                        `in`!!.putExtra("ID", weiKePageGsons!!.tophuandeng[position].infoid)
                        startActivity(`in`)
                    }
                    "item" -> {
                        `in` = Intent(con, TaskDetailsActivity::class.java)
                        `in`!!.putExtra("ID", weiKePageGsons!!.tophuandeng[position].infoid)
                        startActivity(`in`)
                    }
                }
            }
        }

        iv_hyh!!.setOnClickListener {
            val map = HashMap<String, Any>()
            LogUtils.d("=================filed_id:$filed_id")
            map["user_classid"] = filed_id
            map["pages"] = "1"
            OkhttpTool.getOkhttpTool().post(UrlConfig.WEIKE_ITEM_MORE, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LhtTool.sendMessage(hd, e, NETWORKIOEXCEPTION)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val s = response.body()!!.string()
                        LogUtils.d("========================response.body():$s")
                        weikeMoreGson = Gson().fromJson(s, WeikeMoreGson::class.java)
                        LogUtils.d("========================weikeMoreGson.getItem_list().size():" + weikeMoreGson.item_list.size)
                        weikeload = ArrayList<WeiKePageGson.ItemListBean>()
                        for (i in 0 until weikeMoreGson.item_list.size) {
                            val listBean = WeiKePageGson.ItemListBean()
                            listBean.field_id = weikeMoreGson.item_list[i].field_id
                            listBean.itemid = weikeMoreGson.item_list[i].itemid
                            listBean.itemname = weikeMoreGson.item_list[i].itemname
                            listBean.price = weikeMoreGson.item_list[i].price
                            listBean.content = weikeMoreGson.item_list[i].content
                            listBean.endtime = weikeMoreGson.item_list[i].endtime
                            listBean.itemtype = weikeMoreGson.item_list[i].itemtype
                            weikeload.add(listBean)
                        }
                        hd.sendEmptyMessage(HYHINFO)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }
            })
            iv_hyh!!.setImageResource(R.mipmap.huanyihuan_xuan)
        }


    }

    override fun onLoad() {
        Log.e("jiazai", "jiazai")
        if (ConfigTools.VIPMARK > 3) {
            weiKePageNum++
            val map = HashMap<String, Any>()
            map["user_classid"] = filed_id
            if (weiKePageNum == 2) {
                map["pages"] = weiKePageNum.toString() + ""
            } else if (weiKePageNum > 2 && weikeMoreGson.pagerInfo.nextPageIndex != 0) {
                map["pages"] = weiKePageNum.toString() + ""
            } else {
                CustomToast.showToast(con, "已经是最后一页了!", Toast.LENGTH_SHORT)
                myListView!!.isLoadEnable = false
                return
            }
            OkhttpTool.getOkhttpTool().post(UrlConfig.WEIKE_ITEM_MORE, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LhtTool.sendMessage(hd, e, NETWORKIOEXCEPTION)
                }


                override fun onResponse(call: Call, response: Response) {
                    try {
                        val s = response.body()!!.string()
                        LogUtils.d("========================response.body():$s")
                        weikeMoreGson = Gson().fromJson(s, WeikeMoreGson::class.java)
                        LogUtils.d("========================weikeMoreGson.getItem_list().size():" + weikeMoreGson.item_list.size)
                        weikeload = ArrayList<WeiKePageGson.ItemListBean>()
                        for (i in 0 until weikeMoreGson.item_list.size) {
                            val listBean = WeiKePageGson.ItemListBean()
                            listBean.field_id = weikeMoreGson.item_list[i].field_id
                            listBean.itemid = weikeMoreGson.item_list[i].itemid
                            listBean.itemname = weikeMoreGson.item_list[i].itemname
                            listBean.price = weikeMoreGson.item_list[i].price
                            listBean.content = weikeMoreGson.item_list[i].content
                            listBean.endtime = weikeMoreGson.item_list[i].endtime
                            listBean.itemtype = weikeMoreGson.item_list[i].itemtype
                            weikeload.add(listBean)
                        }
                        hd.sendEmptyMessage(WKLOADMORE)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }
            })
        } else {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Glide.with(MyApplication.INSTANCE).pauseRequests()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onClick(view: View) {
        when (view.id) {

        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.search_bar_redcolor)
                .init()
    }

    companion object {
        private val VIPURL = "http://app.680.com/api/v3/index_vk_vip_data.ashx"
        private val XUQIUFENLEI = "http://app.680.com/logo/task.html"
        private val LOADDATA = 0X225
    }
}
