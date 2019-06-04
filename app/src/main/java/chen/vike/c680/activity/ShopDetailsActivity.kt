package chen.vike.c680.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.Toolbar
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*
import chen.vike.c680.adapter.BiaoQianAdapter
import chen.vike.c680.adapter.MyPagerAdapter
import chen.vike.c680.bean.GridViewInfoBean
import chen.vike.c680.bean.ShopFuwuListGson
import chen.vike.c680.bean.ShopInfoGson
import chen.vike.c680.bean.ShopZuopinGson
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.*
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.views.MyGridView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.util.Util
import com.google.gson.Gson
import com.gyf.barlibrary.ImmersionBar
import com.hyphenate.util.NetUtils
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.lht.vike.a680_v1.R
import com.lht.vike.a680_v1.R.id.fbxq_phone
import kotterknife.bindView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.*

/**
 * Created by lht on 2017/3/15.
 *
 *
 * 店铺详情
 */

class ShopDetailsActivity : AppCompatActivity() {

    internal val shopLogo: ImageView by bindView(R.id.shop_logo)
    internal val shopXyDj: TextView by bindView(R.id.shop_xy_dj)
    internal val shopVipImg: ImageView by bindView(R.id.shop_vip_img)
    internal val shopSycj: TextView by bindView(R.id.shop_sycj)
    internal val shipCjbs: TextView by bindView(R.id.ship_cjbs)
    internal val shipAddress: TextView by bindView(R.id.ship_address)
    internal val jineng: TextView by bindView(R.id.jineng)
    internal val dianpuJinengGrid: MyGridView by bindView(R.id.dianpu_jineng_grid)
    internal val headLayout: LinearLayout by bindView(R.id.head_layout)
    internal val toolbar: Toolbar by bindView(R.id.toolbar)
    internal val collapsingToolbarLayout: CollapsingToolbarLayout by bindView(R.id.collapsingToolbarLayout)
    internal val shopXqDpsy: RadioButton by bindView(R.id.shop_xq_dpsy)
    internal val shopXqCsfw: RadioButton by bindView(R.id.shop_xq_csfw)
    internal val shopXqCgal: RadioButton by bindView(R.id.shop_xq_cgal)
    internal val shopXqDpxq: RadioButton by bindView(R.id.shop_xq_dpxq)
    internal val radioGroup0: RadioGroup by bindView(R.id.radioGroup0)
    internal val appbarLayout: AppBarLayout by bindView(R.id.appbar_layout)
    internal val shopXqLx: LinearLayout by bindView(R.id.shop_xq_lx)
    internal val shopShouDian: LinearLayout by bindView(R.id.shop_shou_dian)
    internal val shopShouSrc: LinearLayout by bindView(R.id.shop_shou_src)
    internal val shopXqLxt: RelativeLayout by bindView(R.id.shop_xq_lxt)
    private var bundle: Bundle? = null

    private var mWczl_num1: TextView? = null
    private var mWczl_bj1: TextView? = null
    private var mWcsd_num1: TextView? = null
    private var mWcsd_bj1: TextView? = null
    private var mFwtd_num1: TextView? = null
    private var mFwtd_bj1: TextView? = null

    private var view: View? = null
    private var dpxq_by_sjs: TextView? = null
    private var dpxq_sy_cjs: TextView? = null
    private var dpxq_by_cje: TextView? = null
    private var dpxq_sy_cje: TextView? = null
    private var dpxq_hpl: TextView? = null
    private var dpxq_hpl_jdt: TextView? = null
    private var dpxq_hprs: TextView? = null
    private var dpxq_cprs: TextView? = null
    private var mContent: TextView? = null
    private var button: RelativeLayout? = null
    private val map = HashMap<String, Any>()
    private var radioGroup: RadioGroup? = null
    private var viewPager: ViewPager? = null
    private var recyclerView1: XRecyclerView? = null
    private var recyclerView2: XRecyclerView? = null
    private var view_no: TextView? = null
    private var view_no1: TextView? = null
    private var shopInfoGson: ShopInfoGson? = null
    private var shopFuwuListGson: ShopFuwuListGson? = null

    private var shopZuopinGson: ShopZuopinGson? = null
    private val list1 = ArrayList<ShopFuwuListGson.ListBean>()
    private val list2 = ArrayList<ShopZuopinGson.ListBean>()
    private lateinit var adapter1: BaseRecyclerAdapter<*>
    private lateinit var adapter2: BaseRecyclerAdapter<*>
    private val metrics = DisplayMetrics()
    private val ld: LoadingDialog? = null
    private val GET_SHOP_INFO = 0x123
    private val SHOP_FUWU_LIST = 0x122
    private val SHOP_ZUOPIN_LIST = 0x121
    private val NETWORK_EXCEPTION = 0X111
    private val GUYONG_FUWU = 0X124
    private var firstFlag: Boolean = false
    private var text1: TextView? = null
    private var text2: TextView? = null
    private var text3: TextView? = null
    private var ping1: TextView? = null
    private var ping2: TextView? = null
    private var ping3: TextView? = null
    private var context: Context? = null
    private var biaoQianAdapter: BiaoQianAdapter? = null
    private val biaoQians = ArrayList<GridViewInfoBean>()

    /**
     * 存储页面数据
     */
    private var userid = ""
    private var pagename = ""
    private var pageindex = ""
    private var pageinittime = ""
    private var dataStatisticsBean = DataStatisticsBean()

    private var sy_web: WebView? = null
    private var view1: View? = null
    private var view0: View? = null
    private var view2: View? = null

    private var viewShow: View? = null
    private var popupWindow: PopupWindow? = null

    private var fuwuJine: EditText? = null
    private var fuwuPhone: EditText? = null
    private var fuwuTiJiao: Button? = null
    private var titleGuyong: TextView? = null
    private val hd = MHandler(this)

    private class MHandler(activity: ShopDetailsActivity) : Handler() {
        private val weakReference: WeakReference<ShopDetailsActivity> = WeakReference(activity)
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val activity = weakReference.get()
            activity?.run {
                ld?.dismiss()
                if (msg?.what == GET_SHOP_INFO) {
                    if (shopInfoGson!!.isfav == "1") {
                        shopShouDian.visibility = View.GONE

                        shopShouSrc.visibility = View.VISIBLE
                    }
                    if (Util.isOnMainThread()) {

                        try {
                            val options = RequestOptions()
                                    .placeholder(R.mipmap.image_loading)
                                    .error(R.mipmap.image_erroe)
                            Glide.with(this).load(shopInfoGson!!.imgurl + "?123").apply(options).into(shopLogo!!)
                        } catch (e: Exception) {

                        }
                    }
                    //                title.setText(shopInfoGson.getShopname());
                    shopXyDj.text = shopInfoGson!!.shopname
                    // LhtTool.jifen(Integer.valueOf(shopInfoGson.getGrade()), shopXyDj);
                    LhtTool.shopDetailsIsVip(Integer.valueOf(shopInfoGson!!.check), shopVipImg!!)
                    shopSycj.text = "近三月成交" + shopInfoGson!!.cj_month3_money + "元"
                    shipCjbs.text = "交易：" + shopInfoGson!!.cj_month3_num + "笔"
                    mWczl_num1!!.text = shopInfoGson!!.pj_zhiliang
                    shipAddress.text = shopInfoGson!!.provname + "-" + shopInfoGson!!.cityname
                    text1!!.text = shopInfoGson!!.pj_zhiliang
                    setTitleToCollapsingToolbarLayout()
                    val decimalFormat = DecimalFormat("0.00")

                    if (java.lang.Float.valueOf(shopInfoGson!!.pj_zhiliang) > java.lang.Float.valueOf(shopInfoGson!!.pj_avg)) {

                        mWczl_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_avg))
                        mWczl_bj1!!.setTextColor(resources.getColor(R.color.text_color_2))
                    } else {
                        mWczl_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_avg))
                        mWczl_bj1!!.setTextColor(resources.getColor(R.color.text_color_10))

                    }

                    mWcsd_num1!!.text = shopInfoGson!!.pj_xiaolv

                    text2!!.text = shopInfoGson!!.pj_xiaolv
                    if (java.lang.Float.valueOf(shopInfoGson!!.pj_xiaolv) > java.lang.Float.valueOf(shopInfoGson!!.pj_avg)) {
                        mWcsd_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_avg))
                        mWcsd_bj1!!.setTextColor(resources.getColor(R.color.text_color_2))

                    } else {
                        mWcsd_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_avg))
                        mWcsd_bj1!!.setTextColor(LhtTool.getColor(baseContext, R.color.text_color_10))


                    }

                    mFwtd_num1!!.text = shopInfoGson!!.pj_taidu
                    text3!!.text = shopInfoGson!!.pj_taidu
                    if (java.lang.Float.valueOf(shopInfoGson!!.pj_taidu) > java.lang.Float.valueOf(shopInfoGson!!.pj_avg)) {
                        mFwtd_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_taidu))
                        mFwtd_bj1!!.setTextColor(resources.getColor(R.color.text_color_2))

                    } else {
                        mFwtd_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_avg))
                        mFwtd_bj1!!.setTextColor(resources.getColor(R.color.text_color_10))

                    }

                    val strings = shopInfoGson!!.jineng.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    var s = ""
                    for (i in strings.indices) {
                        if (i == strings.size - 1) {
                            s = s + strings[i]
                        } else {
                            s = s + strings[i] + " | "
                        }

                    }
                    for (i in strings.indices) {
                        val info = GridViewInfoBean()
                        info.name = strings[i]
                        biaoQians.add(info)

                    }
                    biaoQianAdapter!!.notifyDataSetChanged()
                    jineng.text = s
                    dpxq_by_cje!!.text = shopInfoGson!!.this_month_money
                    dpxq_by_sjs!!.text = shopInfoGson!!.this_month_num
                    dpxq_sy_cjs!!.text = shopInfoGson!!.pre_month_num
                    dpxq_sy_cje!!.text = shopInfoGson!!.pre_month_money
                    dpxq_hpl!!.text = shopInfoGson!!.allpjper
                    if (shopInfoGson!!.allpjper.indexOf(".") == -1) {
                        dpxq_hpl_jdt!!.layoutParams = LinearLayout.LayoutParams(metrics.widthPixels / 300 * Integer.valueOf(shopInfoGson!!.allpjper), 20)
                    } else {
                        val a = shopInfoGson!!.allpjper.substring(0, shopInfoGson!!.allpjper.indexOf("."))
                        dpxq_hpl_jdt!!.layoutParams = LinearLayout.LayoutParams(metrics.widthPixels / 300 * Integer.valueOf(a), 20)
                    }
                    dpxq_hprs!!.text = shopInfoGson!!.allpjgood + "人好评"
                    dpxq_cprs!!.text = shopInfoGson!!.allpjbad + "人差评"
                    mContent!!.text = Html.fromHtml(shopInfoGson!!.jianjie)
                } else if (msg?.what == SHOP_FUWU_LIST) {

                    if (shopFuwuListGson != null) {
                        if (shopFuwuListGson!!.pagerInfo.currPageIndex == 1) {
                            list1.clear()
                        } else {
                            recyclerView1!!.loadMoreComplete()
                        }
                        list1.addAll(shopFuwuListGson!!.list)
                        adapter1.notifyDataSetChanged()
                    } else {
                        adapter1.notifyDataSetChanged()
                    }
                    view_no1!!.visibility = View.GONE
                    if (list1.size == 0) {
                        view_no1!!.visibility = View.VISIBLE
                        if (!firstFlag) {
                            viewPager!!.currentItem = 2
                            firstFlag = true
                        }
                    }

                } else if (msg?.what == SHOP_ZUOPIN_LIST) {

                    if (shopZuopinGson != null) {
                        if (shopZuopinGson!!.pagerInfo.currPageIndex == 1) {
                            list2.clear()
                        } else {
                            recyclerView2!!.loadMoreComplete()
                        }
                        list2.addAll(shopZuopinGson!!.list)
                        adapter2.notifyDataSetChanged()
                    } else {
                        adapter2.notifyDataSetChanged()
                    }
                    view_no!!.visibility = View.GONE
                    if (list2.size == 0) {
                        view_no!!.visibility = View.VISIBLE
                    }

                } else if (msg?.what == NETWORK_EXCEPTION) {

                    LhtTool.showNetworkException(this, msg)

                } else if (msg?.what == 0) {
                    ToastUtils.showShort("收藏成功")
                    shopShouDian.visibility = View.GONE
                    shopShouSrc.visibility = View.VISIBLE
                } else if (msg?.what == 1) {
                    Toast.makeText(this, "未登录", Toast.LENGTH_SHORT).show()
                } else if (msg?.what == 2) {
                    Toast.makeText(this, "已经收藏过了", Toast.LENGTH_SHORT).show()
                } else if (msg?.what == GUYONG_FUWU) {
                    val s = msg.data.getString("s")
                    val strings = s!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    when (strings[0]) {
                        "self" -> CustomToast.showToast(this, "不能雇佣自己", Toast.LENGTH_LONG)
                        "ok" -> {
                            CustomToast.showToast(this, "雇佣成功", Toast.LENGTH_LONG)
                            val intent = Intent(this, FuKuanAfterActivity::class.java)
                            intent.putExtra("ID", strings[1])
                            dataStatisticsBean.pagenext = "服务商页面——雇佣成功——付款页面"
                            saveData()
                            startActivity(intent)
                            finish()
                        }
                        "fail" -> CustomToast.showToast(this, "雇佣失败", Toast.LENGTH_LONG)
                        "nocon" -> CustomToast.showToast(this, "项目内容不能为空", Toast.LENGTH_LONG)
                        "nomoney" -> CustomToast.showToast(this, "雇佣金额无效", Toast.LENGTH_LONG)
                        "notitle" -> CustomToast.showToast(this, "项目标题不能为空", Toast.LENGTH_LONG)
                        "unlogin" -> CustomToast.showToast(this, "您还未登录", Toast.LENGTH_LONG)
                        else -> {
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowManager.defaultDisplay.getMetrics(metrics)
        loadBlurAndSetStatusBar()
        setContentView(R.layout.activity_shop_details)
        ImmersionBar.with(this).init()

        context = this
        biaoQianAdapter = BiaoQianAdapter(context, biaoQians)
        dianpuJinengGrid.adapter = biaoQianAdapter
        radioGroup = findViewById(R.id.radioGroup0)
        viewPager = findViewById(R.id.viewPager)
        viewPager!!.offscreenPageLimit = 5
        view = LayoutInflater.from(this).inflate(R.layout.item_dianpuxiangqing, null)
        mWczl_num1 = view!!.findViewById(R.id.dpxq_wczl_fs)
        mWczl_bj1 = view!!.findViewById(R.id.dpxq_wczl_c)
        mWcsd_num1 = view!!.findViewById(R.id.dpxq_fzsd_fs)
        mWcsd_bj1 = view!!.findViewById(R.id.dpxq_gzsd_c)
        mFwtd_num1 = view!!.findViewById(R.id.dpxq_fwtd_fs)
        mFwtd_bj1 = view!!.findViewById(R.id.dpxq_fwtd_c)
        dpxq_by_sjs = view!!.findViewById(R.id.dpxq_by_sjs)
        dpxq_by_cje = view!!.findViewById(R.id.dpxq_by_cje)
        dpxq_sy_cje = view!!.findViewById(R.id.dpxq_sy_cje)
        dpxq_sy_cjs = view!!.findViewById(R.id.dpxq_sy_cjs)
        dpxq_hpl = view!!.findViewById(R.id.dpxq_hpl)
        dpxq_hpl_jdt = view!!.findViewById(R.id.dpxq_hpl_jdt)
        dpxq_hprs = view!!.findViewById(R.id.dpxq_hprs)
        dpxq_cprs = view!!.findViewById(R.id.dpxq_cprs)
        mContent = view!!.findViewById(R.id.content)
        button = findViewById(R.id.shop_xq_lxt)
        text1 = view!!.findViewById(R.id.shapeTextView1)
        text2 = view!!.findViewById(R.id.shapeTextView2)
        text3 = view!!.findViewById(R.id.shapeTextView3)
        ping1 = view!!.findViewById(R.id.pingjunzhi1)
        ping2 = view!!.findViewById(R.id.pingjunzhi2)
        ping3 = view!!.findViewById(R.id.pingjunzhi3)

        bundle = intent.extras
        LoadData()
        setToolBarReplaceActionBar()
    }


    /**
     * 用toolBar替换ActionBar
     */
    private fun setToolBarReplaceActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadBlurAndSetStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.parseColor("#20ffffff")
        }
    }


    public override fun onStart() {
        super.onStart()
        storage("服务商店铺主页")
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

    /**
     * 保存数据
     */
    fun saveData() {
        ThreadPoolManager.instance.addTask(Runnable {
            if (dataStatisticsBean.save()) {

                LogUtils.d( "保存成功")
            } else {
                LogUtils.d( "保存失败")
            }
        })
    }

    /**
     * 使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，
     * 设置到Toolbar上则不会显示
     */
    private fun setTitleToCollapsingToolbarLayout() {
        collapsingToolbarLayout.expandedTitleGravity = Gravity.LEFT
        collapsingToolbarLayout.collapsedTitleGravity = Gravity.LEFT
        collapsingToolbarLayout.title = ""
        appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            //                if (verticalOffset <= -headLayout.getHeight() / 2) {
            //                    collapsingToolbarLayout.setTitle(shopInfoGson.getShopname());
            //                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
            //                     collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
            //                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
            //                } else {
            //
            //                }

            val Offset = Math.abs(verticalOffset) //目的是将负数转换为绝对正数；
            //标题栏的渐变
            collapsingToolbarLayout.setBackgroundColor(changeAlpha(resources.getColor(R.color.vip_red), Math.abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange))

            /**
             * 当前最大高度便宜值除以2 在减去已偏移值 获取浮动 先显示在隐藏
             */

            /**
             * 当前最大高度便宜值除以2 在减去已偏移值 获取浮动 先显示在隐藏
             */
            if (Offset < appBarLayout.totalScrollRange / 2) {
                collapsingToolbarLayout.title = ""
                collapsingToolbarLayout.alpha = (appBarLayout.totalScrollRange / 2 - Offset * 1.0f) / (appBarLayout.totalScrollRange / 2)


                /**
                 * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                 * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                 */


                /**
                 * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                 * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                 */
            } else if (Offset > appBarLayout.totalScrollRange / 2) {
                val floate = (Offset - appBarLayout.totalScrollRange / 2) * 1.0f / (appBarLayout.totalScrollRange / 2)
                collapsingToolbarLayout.alpha = floate
                collapsingToolbarLayout.title = shopInfoGson!!.shopname
                collapsingToolbarLayout.alpha = floate
            }
        })
    }

    /**
     * 根据百分比改变颜色透明度
     */
    fun changeAlpha(color: Int, fraction: Float): Int {
        val alpha = (Color.alpha(color) * fraction).toInt()
        return Color.argb(alpha, 0, 0, 0)
    }

    /**
     * 页面加载数据
     */
    fun LoadData() {
        view1 = LayoutInflater.from(this).inflate(R.layout.view_listview, null)
        view0 = LayoutInflater.from(this).inflate(R.layout.view_listview, null)
        view2 = LayoutInflater.from(this).inflate(R.layout.dianpu_shouye_item, null)
        view_no = TextView(this)
        view_no!!.visibility = View.VISIBLE
        view_no!!.setTextColor(resources.getColor(R.color.colorhit))
        view_no!!.textSize = 14f
        view_no!!.gravity = Gravity.CENTER
        view_no!!.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, metrics.heightPixels / 10)//可能有坑
        view_no!!.text = "服务商暂未上传案例"
        view_no1 = TextView(this)
        view_no1!!.visibility = View.VISIBLE
        view_no1!!.setTextColor(resources.getColor(R.color.colorhit))
        view_no1!!.textSize = 14f
        view_no1!!.text = "暂无可出售的服务"
        view_no1!!.gravity = Gravity.CENTER
        view_no1!!.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, metrics.heightPixels / 10)//可能有坑

        val headerparent = LinearLayout(this)
        val headerparent2 = LinearLayout(this)
        sy_web = view2!!.findViewById(R.id.dianpu_sy_web)
        webViewDianPuShow()//店铺首页webview显示

        recyclerView2 = view1!!.findViewById(R.id.xRecyclerView)
        recyclerView2!!.setBackgroundColor(resources.getColor(R.color.colorbg))
        recyclerView1 = view0!!.findViewById(R.id.xRecyclerView)
        headerparent.addView(view_no1)
        headerparent2.addView(view_no)
        recyclerView2!!.addHeaderView(headerparent2)
        recyclerView1!!.addHeaderView(headerparent)

        recyclerView1!!.layoutManager = GridLayoutManager(this, 1)
        recyclerView2!!.layoutManager = GridLayoutManager(this, 2)
        recyclerView1!!.setPullRefreshEnabled(false)
        recyclerView2!!.setPullRefreshEnabled(false)
        map["userid"] = bundle!!.getString("ID")!!
        if (MyApplication.userInfo != null && MyApplication.userInfo!!.userID != null) {
            map["login_userid"] = MyApplication.userInfo!!.userID
        }
        map["hits"] = 1
        map["num"] = 10
        map["pages"] = 1

        OkhttpTool.getOkhttpTool().post(UrlConfig.GET_SHOP_INFO, map, ShopInfoCallBack(this@ShopDetailsActivity))
        getFuWuAndZuoPinList(UrlConfig.SHOP_FUWU_LIST)
        getFuWuAndZuoPinList(UrlConfig.SHOP_ZUOPIN_LIST)
        recyclerViewShow()//列表显示数据服务，案例
        viewPageShow()//viewpage的显示
        bottomListener()//页面底部按钮相应事件
    }

    private class ShopInfoCallBack(shopDetailsActivity: ShopDetailsActivity):Callback{
        private val ref:WeakReference<ShopDetailsActivity> = WeakReference(shopDetailsActivity)
        override fun onFailure(call: Call, e: IOException) {
            val shopDetailsActivity = ref.get()
            shopDetailsActivity?.let {  LhtTool.sendMessage(it.hd, e, it.NETWORK_EXCEPTION) }
        }

        override fun onResponse(call: Call, response: Response) {
        val shopDetailsActivity = ref.get()
            shopDetailsActivity?.run {
                try {
                    val s = response.body()!!.string()
                    Log.e("fuwushangdian", s)
                    shopInfoGson = Gson().fromJson(s, ShopInfoGson::class.java)
                    if (shopInfoGson != null) {
                        hd.sendEmptyMessage(GET_SHOP_INFO)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }




    /**
     * 页面底部按钮响应事件
     */
    private fun bottomListener() {
        shopXqLx.setOnClickListener {
            //开始和XX聊天
            if (LhtTool.isLogin) {
                if (shopInfoGson != null && shopInfoGson!!.userid != null) {
                    if (MyApplication.userInfo!!.userID == shopInfoGson!!.userid) {
                        ToastUtils.showShort("不能和自己聊天")
                    } else {
                        //跳到聊天界面
                        val intent = Intent(this@ShopDetailsActivity, MessageChatActivity::class.java)
                        intent.putExtra("ID", shopInfoGson!!.userid)
                        intent.putExtra("name", shopInfoGson!!.username)
                        dataStatisticsBean.pagenext = "聊天页面"
                        saveData()
                        startActivity(intent)
                    }
                }
            } else {
                ToastUtils.showShort("亲，你还没有登录哦~")
                val intent = Intent(this@ShopDetailsActivity, UserLoginActivity::class.java)
                dataStatisticsBean.pagenext = "聊天页面——用户登录页面"
                saveData()
                startActivity(intent)
            }
        }
        shopShouDian.setOnClickListener {
            if (LhtTool.isLogin) {
                shouDianHttpData()
            } else {
                CustomToast.showToast(this@ShopDetailsActivity, "请先登录", Toast.LENGTH_SHORT)
                val intent = Intent(this@ShopDetailsActivity, UserLoginActivity::class.java)
                dataStatisticsBean.pagenext = "收藏——用户登录页面"
                saveData()
                startActivity(intent)
            }
        }
        button!!.setOnClickListener {
            //立即雇佣
            if (LhtTool.isLogin) {
                if (shopInfoGson != null && shopInfoGson!!.userid != null) {
                    if (MyApplication.userInfo!!.userID == shopInfoGson!!.userid) {
                        CustomToast.showToast(this@ShopDetailsActivity, "不能雇佣自己", Toast.LENGTH_SHORT)
                    } else {
                        showWindows()
                    }
                }

            } else {
                CustomToast.showToast(this@ShopDetailsActivity, "请先登录", Toast.LENGTH_SHORT)
                val intent = Intent(this@ShopDetailsActivity, UserLoginActivity::class.java)
                dataStatisticsBean.pagenext = "雇佣——用户登录页面"
                saveData()
                startActivity(intent)
            }
        }


    }

    /**
     * 弹窗
     */
    private fun showWindows() {
        viewShow = LayoutInflater.from(context).inflate(R.layout.fuwu_goumai_item, null)
        popupWindow = PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isFocusable = true
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#F8F8F8")))//也可以直接把Color.TRANSPARENT换成0
        popupWindow!!.animationStyle = R.style.popWindow_animation
        val lp = window.attributes
        lp.alpha = 0.6f //0.0-1.0
        window.attributes = lp
        popupWindow!!.showAtLocation(viewShow, Gravity.CENTER, 0, 1000)
        popupWindow!!.setOnDismissListener {
            val lp = window.attributes
            lp.alpha = 1.0f //0.0-1.0
            window.attributes = lp
        }
        showData()
    }

    private fun showData() {
        fuwuJine = viewShow!!.findViewById(R.id.fbxq_ys)
        fuwuPhone = viewShow!!.findViewById(fbxq_phone)
        fuwuTiJiao = viewShow!!.findViewById(R.id.fuwu_goumai_enter)
        titleGuyong = viewShow!!.findViewById(R.id.guyong_goumai_title)
        fuwuPhone!!.setText(MyApplication.userInfo!!.phone)
        titleGuyong!!.text = "雇佣： " + shopInfoGson!!.shopname
        fuwuTiJiao!!.setOnClickListener { guYong() }

    }

    /**
     * 雇佣
     */
    private fun guYong() {
        run {

            if (fuwuJine!!.text.toString().isEmpty()) {
                CustomToast.showToast(this@ShopDetailsActivity, "金额不能为空", Toast.LENGTH_LONG)
            } else {
                if (LhtTool.isLogin) {
                    val map = HashMap<String, Any>()
                    map["userid"] = MyApplication.userInfo!!.userID
                    map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
                    map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
                    map["vikeid"] = shopInfoGson!!.userid
                    map["crv_title"] = "雇佣" + shopInfoGson!!.shopname
                    map["crv_content"] = mContent!!.text
                    map["money"] = fuwuJine!!.text
                    map["dover"] = "1"
                    map["data"] = 15
                    if (fuwuPhone!!.text.toString().isEmpty()) {
                        CustomToast.showToast(this@ShopDetailsActivity, "电话号码不能为空", Toast.LENGTH_LONG)
                    } else if (fuwuPhone!!.text.toString().length > 12) {
                        CustomToast.showToast(this@ShopDetailsActivity, "请输入正确的电话号码", Toast.LENGTH_LONG)
                    } else {
                        map["mobile"] = fuwuPhone!!.text.toString()
                        //                            ld = new LoadingDialog(GeRenZhongXinShopDetailsActivity.this).setMessage("加载中....");
                        //                            ld.show();
                        OkhttpTool.getOkhttpTool().post(UrlConfig.GUYONG_FUWU, map, object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                            }

                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {

                                val s = response.body()!!.string()
                                Log.e("fuwushang", s)
                                val ms = Message()
                                ms.what = GUYONG_FUWU
                                val b = Bundle()
                                b.putString("s", s)
                                ms.data = b
                                hd.sendMessage(ms)

                            }
                        })
                    }
                } else {
                    val intent = Intent(this@ShopDetailsActivity, UserLoginActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }

    /**
     * 收藏店铺
     */
    private fun shouDianHttpData() {
        val map = HashMap<String, Any>()
        map["login_userid"] = MyApplication.userInfo!!.userID
        map["shop_userid"] = bundle!!.getString("ID")!!
        LogUtils.d("map", MyApplication.userInfo!!.userID + "----" + bundle!!.getString("ID"))
        OkhttpTool.getOkhttpTool().post(UrlConfig.SHOP_FAVORITE, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
                Log.e("dianpu", s)
                when (s) {
                    "unlogin" -> hd.sendEmptyMessage(1)
                    "ok" -> hd.sendEmptyMessage(0)
                    "has" -> hd.sendEmptyMessage(2)
                }

            }
        })

    }

    /**
     * 店铺首页webview显示
     */
    private fun webViewDianPuShow() {
        val wSet = sy_web!!.settings
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wSet.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        wSet.javaScriptEnabled = true
        wSet.builtInZoomControls = false
        wSet.useWideViewPort = true//将图片调整到适合webview的大小
        wSet.loadWithOverviewMode = true// 缩放至屏幕的大小
        wSet.setSupportZoom(true)
        wSet.cacheMode = WebSettings.LOAD_DEFAULT
        wSet.setAppCachePath(MyApplication.INSTANCE.getDir("webcache", 0).path)
        wSet.setAppCacheEnabled(true)
        wSet.loadWithOverviewMode = false

        sy_web!!.requestFocusFromTouch()
        val str = "http://app.680.com/touch/shop/v2017/index_imgs.aspx?para=" + bundle!!.getString("ID")!!
        sy_web!!.loadUrl(str)
    }

//     /**
//     * 店铺首页webview显示
//     */
//    fun webViewDianPuShow() {
//        val wSet = sy_web!!.getSettings();
//        wSet.setJavaScriptEnabled(true);
//        wSet.setBuiltInZoomControls(true);
//        wSet.setSupportZoom(true);
//        sy_web!!.requestFocusFromTouch();
//        val str = "http://app.680.com/touch/shop/v2017/index_imgs.aspx?para=" + bundle?.getString("ID");
//        sy_web!!.loadUrl(str)
//    }

    /**
     * 列表显示数据
     */
    private fun recyclerViewShow() {
        //服务列表
        adapter1 = object : BaseRecyclerAdapter<ShopFuwuListGson.ListBean>(list1, this) {
            override fun getItemLayoutId(viewType: Int): Int {
                return R.layout.fuwu_list_item
            }

            override fun bindData(holder: RecyclerViewHolder, position: Int, item: ShopFuwuListGson.ListBean) {

                holder.setVisibility(R.id.textView27, View.GONE)
                holder.setText(R.id.title, item.title)
                ImageLoadUtils.display(applicationContext,holder.getImageView(R.id.fw_logo),item.imgUrl)
                holder.itemView.setOnClickListener {
                    val intent = Intent(this@ShopDetailsActivity, ServiceDetailsActivity::class.java)
                    intent.putExtra("ID", item.id)
                    dataStatisticsBean.pagenext = "服务商店铺——服务详情页面"
                    saveData()
                    startActivityForResult(intent, 3)
                }
                holder.setText(R.id.fw_je, item.showmoney)
                holder.setText(R.id.fw_class, item.typename)
                holder.setText(R.id.cjs, "成交" + item.cjnum + "笔")
            }
        }
        recyclerView1!!.adapter = adapter1
        //作品案例列表
        adapter2 = object : BaseRecyclerAdapter<ShopZuopinGson.ListBean>(list2, this) {
            override fun getItemLayoutId(viewType: Int): Int {
                return R.layout.view_zuoping
            }

            override fun bindData(holder: RecyclerViewHolder, position: Int, item: ShopZuopinGson.ListBean) {
                if (position % 2 == 0) {
                    if (position == 0) {
                        holder.itemView.setPadding(30, 30, 15, 15)
                    } else {
                        holder.itemView.setPadding(30, 15, 15, 15)
                    }

                } else {
                    if (position == 1) {
                        holder.itemView.setPadding(15, 30, 30, 15)
                    } else {
                        holder.itemView.setPadding(15, 15, 30, 15)

                    }
                }
                holder.setText(R.id.textView, list2[position].zuopinname)
                holder.getImageView(R.id.imageView).layoutParams = LinearLayout.LayoutParams(metrics.widthPixels / 2 - 60, metrics.widthPixels / 2 - 60)
                holder.getImageView(R.id.imageView).scaleType = ImageView.ScaleType.FIT_CENTER

                val options = RequestOptions()
                        .placeholder(R.mipmap.image_loading)
                        .error(R.mipmap.image_erroe)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                Glide.with(this@ShopDetailsActivity).load(list2[position].zuopinimg).apply(options).into(holder.getImageView(R.id.imageView))
                holder.itemView.setOnClickListener {
                    val intent = Intent(this@ShopDetailsActivity, CaseDetailsActivity::class.java)
                    intent.putExtra("BEAN", shopInfoGson)
                    intent.putExtra("ID", list2[position].zuopinid)
                    dataStatisticsBean.pagenext = "服务商店铺——案例详情页面"
                    saveData()
                    startActivity(intent)
                }
            }
        }
        recyclerView2!!.adapter = adapter2

    }

    /**
     * viewpage显示
     */
    private fun viewPageShow() {
        val listv = ArrayList<View>()
        listv.add(view2!!)
        listv.add(view0!!)
        listv.add(view1!!)
        listv.add(view!!)
        val myPagerAdapter = MyPagerAdapter(listv)
        viewPager!!.adapter = myPagerAdapter
        viewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    shopXqDpsy.isChecked = true

                } else if (position == 1) {
                    shopXqCsfw.isChecked = true
                    if (shopFuwuListGson == null && netError){
                        getFuWuAndZuoPinList(UrlConfig.SHOP_FUWU_LIST)
                    }
                } else if (position == 2) {

                    shopXqCgal.isChecked = true
                    if (shopZuopinGson == null && netError){
                        getFuWuAndZuoPinList(UrlConfig.SHOP_ZUOPIN_LIST)
                    }

                } else if (position == 3) {
                    shopXqDpxq.isChecked = true
                }


            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        radioGroup!!.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.shop_xq_dpsy) {
                viewPager!!.setCurrentItem(0, true)
            } else if (checkedId == R.id.shop_xq_csfw) {
                viewPager!!.setCurrentItem(1, true)
            } else if (checkedId == R.id.shop_xq_cgal) {
                viewPager!!.setCurrentItem(2, true)
            } else if (checkedId == R.id.shop_xq_dpxq) {
                viewPager!!.setCurrentItem(3, true)
            }
        }

    }

    /**
     * 获取服务和作品案例列表
     * 以URL来区分
     */

    private var netError:Boolean = false
    private fun getFuWuAndZuoPinList(url:String){

        OkhttpTool.getOkhttpTool().post(url, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                netError = true
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d("====================response:$s")
                    when(url){
                        UrlConfig.SHOP_FUWU_LIST -> {
                            shopFuwuListGson = Gson().fromJson(s, ShopFuwuListGson::class.java)
                            hd.sendEmptyMessage(SHOP_FUWU_LIST)
                        }
                        UrlConfig.SHOP_ZUOPIN_LIST ->{
                            shopZuopinGson = Gson().fromJson(s, ShopZuopinGson::class.java)
                            hd.sendEmptyMessage(SHOP_ZUOPIN_LIST)
                        }
                    }
                    netError = false
                } catch (e: Exception) {
                    netError = true
                    e.printStackTrace()
                }

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // Glide.with(GeRenZhongXinShopDetailsActivity.this).pauseRequests();
        hd.removeCallbacksAndMessages(null)
        ImmersionBar.with(this).destroy()
    }


}


