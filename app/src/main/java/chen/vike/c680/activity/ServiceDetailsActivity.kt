package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import chen.vike.c680.adapter.ServiceDetailsEvaluationAdapter
import chen.vike.c680.bean.ServiceDetailsGson
import chen.vike.c680.bean.ServicePingjiaGson
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.ThreadPoolManager
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.FuWuHeaderView
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.adapter.MyPagerAdapter
import com.lht.vike.a680_v1.R
import com.lht.vike.a680_v1.R.id.*
import kotterknife.bindView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2017/3/15.
 *
 *
 * 服务详情
 */

class ServiceDetailsActivity : BaseStatusBarActivity(), View.OnClickListener {

    internal val banner: FuWuHeaderView by bindView(R.id.banner)
    internal val fwmc: TextView by bindView(R.id.fwmc)
    internal val fwXj1: TextView by bindView(R.id.fw_xj_1)
    internal val fwXj: TextView by bindView(R.id.fw_xj)
    internal val unit: TextView by bindView(R.id.unit)
    internal val textView10: TextView by bindView(R.id.textView10)
    internal val fwYj: TextView by bindView(R.id.fw_yj)
    internal val viewYj: FrameLayout by bindView(R.id.view_yj)
    internal val fwNum: TextView by bindView(R.id.fw_num)
    internal val fwsLogo: ImageView by bindView(R.id.fws_logo)
    internal val fwsName: TextView by bindView(R.id.fws_name)
    internal val fwsCjjl: TextView by bindView(R.id.fws_cjjl)
    internal val fwsDizhi: TextView by bindView(R.id.fws_dizhi)
    internal val shop: RelativeLayout by bindView(R.id.shop)
    internal val fwxqFwxq: RadioButton by bindView(R.id.fwxq_fwxq)
    internal val fwxqPj: RadioButton by bindView(R.id.fwxq_pj)
    internal val radioGroup: RadioGroup by bindView(R.id.radioGroup)
    internal val viewPager: ViewPager by bindView(R.id.viewPager)
    internal val shopXqLx: LinearLayout by bindView(R.id.shop_xq_lx)
    internal val shopXqShou: LinearLayout by bindView(R.id.shop_xq_shou)
    internal val shopYiShou: LinearLayout by bindView(R.id.shop_yi_shou)
    internal val shopXqLxt: Button by bindView(R.id.shop_xq_lxt)
    private var servicePingjiaGson: ServicePingjiaGson? = null
    private var serviceDetailsGson: ServiceDetailsGson? = null
    private var xRecyclerView: XRecyclerView? = null
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    private val map = HashMap<String, Any>()
    private val SERVICE_DETAILS = 0x123
    private val SERVICE_PINGJIA = 0x133
    private val NETWORKEXCEPTION = 0x113
    private val T_MESSAGE = 0x111
    private var ld: LoadingDialog? = null
    private var imageGetter: Html.ImageGetter? = null
    private var mIntent: Intent? = null
    private val pingjiaList = ArrayList<ServicePingjiaGson.ListBean>()
    private val imgUrlList = ArrayList<String>()
    private var flag: Boolean = false
    private var id: String? = null
    private var context: Context? = null
    private val USER_GUYONG = 0x143
    private var mHandler: MHandler = MHandler(this)
    private var adapter: ServiceDetailsEvaluationAdapter? = null
    private var viewShow: View? = null
    private var popupWindow: PopupWindow? = null
    private var fuwuJine: EditText? = null
    private var fuwuPhone: EditText? = null
    private var fuwuTiJiao: Button? = null
    private var titleGoumai: TextView? = null
    private var pingjiaNetError:Boolean = false

    /**
     * 评价，详情页面
     */
    private var sy_web: WebView? = null
    private var interrupt:Boolean = false

    internal var t: Thread = Thread(object : Runnable {
        var msg = Message.obtain()

        override fun run() {
            if (interrupt) return
            val s = serviceDetailsGson!!.con.replace("font-size".toRegex(), "f").replace("line-height".toRegex(), "l")
            imageGetter = LhtTool.MyImageGetter(resources)
            //  Log.e("fuwuxiangqing",s);
            val test = Html.fromHtml("<font color='#df231b'>温馨提示：<br>&#160;&#160;&#160;&#160;雇佣任务，时间财富网不收取费用，请勿相信服务商任何理由的线下交易行为。</font><br><br>$s", imageGetter, null)
            msg.what = T_MESSAGE
            msg.obj = test
            mHandler.sendMessage(msg)
        }
    })


    class MHandler(activity: ServiceDetailsActivity) : Handler() {
        private var weakReference: WeakReference<ServiceDetailsActivity> = WeakReference(activity)
        private val serviceDetailsActivity = weakReference.get()
        @SuppressLint("SetTextI18n")
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            serviceDetailsActivity?.run {
                if (msg?.what == serviceDetailsActivity.SERVICE_DETAILS) {

                    if (null != serviceDetailsGson) {
                        if (serviceDetailsGson?.imglist != null) {
                            val s = serviceDetailsGson!!.imglist.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            for (i in s.indices) {
                                imgUrlList.add(s[i])
                            }
                        } else {
                            imgUrlList.add(serviceDetailsActivity.serviceDetailsGson!!.imgUrl)
                        }
                        banner.setImgUrlData(serviceDetailsActivity.imgUrlList)
                        banner.setOnHeaderViewClickListener { position ->
                            val intent = Intent(serviceDetailsActivity, ReviewImageActivity::class.java)
                            val bundle = Bundle()
                            bundle.putStringArrayList("mImgs", serviceDetailsActivity.imgUrlList)
                            bundle.putInt("positon", position)
                            intent.putExtra("bundle", bundle)
                            startActivity(intent)
                        }
                        if (imgUrlList.size <= 1) {
                            banner.stopRoll()
                        }
                        fwmc.text = serviceDetailsGson!!.title
                        if (serviceDetailsGson!!.unit != null) {
                            if (serviceDetailsGson!!.unit == "起" || serviceDetailsGson!!.unit.isEmpty()) {
                                unit.text = serviceDetailsGson?.unit
                            } else {
                                unit.text = "/" + serviceDetailsGson?.unit
                            }
                        }
                        if (java.lang.Float.valueOf(serviceDetailsGson!!.price) > 0) {
                            fwXj.text = "￥" + serviceDetailsGson?.price
                        } else {
                            fwXj.text = "议价"
                        }
                        try {
                            if (serviceDetailsGson!!.price_old != null) {
                                if (serviceDetailsGson!!.price_old == "0") {
                                    viewYj.visibility = View.GONE
                                } else {
                                    fwYj.text = serviceDetailsGson!!.price_old
                                }
                            } else {
                                viewYj.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            viewYj.visibility = View.GONE
                        }

                        fwNum.text = Html.fromHtml("已出售<font color='#df231b'>" + serviceDetailsGson!!.cjnum + "</font>笔")
                        //为了莫名其妙的崩溃不会报错误？
                        //java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity
                        if (!this.isDestroyed && !this.isFinishing){
                            try {
                                Glide.with(this).load(serviceDetailsGson!!.faceImg).into(fwsLogo)
                            }catch (e:IllegalArgumentException){
                                e.printStackTrace()
                            }
                        }
                        fwsName.text = serviceDetailsGson!!.shopName
                        fwsCjjl.text = "成交量：" + serviceDetailsGson!!.cj_month3_num + "笔" + "     " + "好评率：" + serviceDetailsGson!!.pjPer + "%"
                        fwsDizhi.text = serviceDetailsGson!!.prov_name + "-" + serviceDetailsGson!!.city_name
                        ld?.let {
                            it.dismiss()
                            flag = true
                        }
                        ThreadPoolManager.instance.addTask(t)
                    }
                } else if (msg?.what == NETWORKEXCEPTION) {

                    LhtTool.showNetworkException(this, msg)


                } else if (msg?.what == SERVICE_PINGJIA) {

                    if (null != servicePingjiaGson) {
                        if (servicePingjiaGson!!.pagerInfo.currPageIndex == 1) {
                            pingjiaList.clear()
                        }
                        pingjiaList.addAll(servicePingjiaGson!!.list)
                        adapter?.notifyDataSetChanged()
                        if (servicePingjiaGson != null) {
                            if (servicePingjiaGson!!.pagerInfo.currPageIndex == 1) {
                                xRecyclerView!!.refreshComplete()
                            } else {
                                xRecyclerView!!.loadMoreComplete()
                            }
                        }
                    }

                } else if (msg?.what == T_MESSAGE) {
                    //               mHeader.setText((CharSequence) msg.obj);
                    //                wait_load.setVisibility(View.GONE);


                } else if (msg?.what == 0) {
                    Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show()
                    shopXqShou.visibility = View.GONE
                    shopYiShou.visibility = View.VISIBLE
                } else if (msg?.what == 1) {
                    Toast.makeText(this, "未登录", Toast.LENGTH_SHORT).show()
                } else if (msg?.what == 2) {
                    Toast.makeText(this, "已经收藏过了", Toast.LENGTH_SHORT).show()
                } else if (msg?.what == USER_GUYONG) {

                    val s = msg.data.getString("s")
                    if (s!!.contains(",")) {
                        val strings = s.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        strings[0] = "ok"
                        CustomToast.showToast(this, "雇佣成功", Toast.LENGTH_LONG)
                        val intent = Intent(this, FuKuanAfterActivity::class.java)
                        intent.putExtra("ID", strings[1])

                        startActivity(intent)
                        finish()
                    } else {
                       ld?.dismiss()
                        when (s) {
                            "self" -> CustomToast.showToast(this, "不能雇佣自己", Toast.LENGTH_LONG)
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
                ld?.dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_details_activity)
        context = this
        titleShow()//标题栏设置
        id = getIntent().getStringExtra("ID")
        map["proid"] = id!!
        adapter = ServiceDetailsEvaluationAdapter(this, pingjiaList)
        initAndShowView()//评价详情显示视图
        fuWuXiangQingData()//服务详情网络数据

        shopXqLxt.text = "立即购买"
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        interrupt=true
//        ThreadPoolManager.instance.shutDown()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.banner -> {
            }
            R.id.fwmc -> {
            }
            R.id.fw_xj_1 -> {
            }
            R.id.fw_xj -> {
            }
            R.id.unit -> {
            }
            R.id.textView10 -> {
            }
            R.id.fw_yj -> {
            }
            R.id.view_yj -> {
            }
            fw_num -> {
            }
            R.id.fws_logo -> {
            }
            R.id.fws_name -> {
            }
            R.id.fws_cjjl -> {
            }
            fws_dizhi -> {
            }
            R.id.shop -> if (serviceDetailsGson != null && serviceDetailsGson!!.userId != null) {
                val intent = Intent(this@ServiceDetailsActivity, ShopDetailsActivity::class.java)
                intent.putExtra("ID", serviceDetailsGson!!.userId)
                startActivity(intent)
            }
            R.id.fwxq_fwxq -> {
            }
            R.id.fwxq_pj -> {
            }
            R.id.radioGroup -> {
            }
            R.id.viewPager -> {
            }
            R.id.shop_xq_lx ->
                //开始和XX聊天
                if (LhtTool.isLogin) {
                    if (serviceDetailsGson != null && serviceDetailsGson!!.userId != null) {
                        if (MyApplication.userInfo!!.userID == serviceDetailsGson!!.userId) {
                            CustomToast.showToast(this@ServiceDetailsActivity, "不能和自己聊天", Toast.LENGTH_SHORT)
                        } else {
                            //跳到聊天界面
                            mIntent = Intent(this@ServiceDetailsActivity, MessageChatActivity::class.java)
                            mIntent!!.putExtra("ID", serviceDetailsGson!!.id)
                            mIntent!!.putExtra("name", serviceDetailsGson!!.shopName)
                            startActivity(mIntent)
                        }
                    }
                } else {
                    CustomToast.showToast(this@ServiceDetailsActivity, "亲，你还没有登录哦~", Toast.LENGTH_SHORT)
                    mIntent = Intent(this@ServiceDetailsActivity, UserLoginActivity::class.java)
                    startActivity(mIntent)
                }
            R.id.shop_xq_shou -> if (LhtTool.isLogin) {
                favoriteShopRequest()
            } else {
                CustomToast.showToast(this@ServiceDetailsActivity, "请先登录", Toast.LENGTH_SHORT)
                mIntent = Intent(this@ServiceDetailsActivity, UserLoginActivity::class.java)
                startActivity(mIntent)
            }
            R.id.shop_xq_lxt -> if (LhtTool.isLogin) {
                //                    mIntent = new Intent(ServiceDetailsActivity.this, BuyServiceActivity.class);
                //                    mIntent.putExtra("ID", id);
                //                    startActivity(mIntent);
                showWindows()
            } else {
                CustomToast.showToast(this@ServiceDetailsActivity, "请先登录", Toast.LENGTH_SHORT)
                mIntent = Intent(this@ServiceDetailsActivity, UserLoginActivity::class.java)
                startActivity(mIntent)
            }
        }
    }


    /**
     * 标题栏的设置
     */
    private fun titleShow() {
        title.text = "服务详情"
        img.setImageResource(R.mipmap.fenxiang)
        img.setOnClickListener {
            //分享
            if (LhtTool.isLogin) {
                LhtTool.showShare(this@ServiceDetailsActivity, serviceDetailsGson!!.shopName,
                        serviceDetailsGson!!.share_url + "?tj=" + MyApplication.userInfo!!.userID,
                        serviceDetailsGson!!.shopName + "," + serviceDetailsGson!!.title,
                        serviceDetailsGson!!.share_url + "?tj=" + MyApplication.userInfo!!.userID, serviceDetailsGson!!.faceImg)
            } else {
                LhtTool.showShare(this@ServiceDetailsActivity, serviceDetailsGson!!.shopName,
                        serviceDetailsGson!!.share_url, serviceDetailsGson!!.shopName + "," + serviceDetailsGson!!.title,
                        serviceDetailsGson!!.share_url, serviceDetailsGson!!.faceImg)
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
        titleGoumai = viewShow!!.findViewById(R.id.guyong_goumai_title)
        titleGoumai!!.text = "购买服务"
        if (serviceDetailsGson!!.price != null) {
            fuwuJine!!.setText(serviceDetailsGson!!.price)
            fuwuPhone!!.setText(MyApplication.userInfo!!.phone)
        }
        fuwuTiJiao!!.setOnClickListener { fufuTiJiaoBtn() }

    }

    private fun fufuTiJiaoBtn() {

        if (fuwuJine!!.text.toString().isEmpty()) {
            CustomToast.showToast(this@ServiceDetailsActivity, "金额不能为空", Toast.LENGTH_LONG)
        } else {
            if (LhtTool.isLogin) {
                map["userid"] = MyApplication.userInfo!!.userID
                map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
                map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
                map["vikeid"] = serviceDetailsGson!!.userId
                map["fuwuid"] = serviceDetailsGson!!.id
                map["crv_title"] = serviceDetailsGson!!.title
                map["crv_content"] = "如：我需要" + serviceDetailsGson!!.title
                map["money"] = fuwuJine!!.text
                map["dover"] = "1"
                map["data"] = 15
                if (fuwuPhone!!.text.toString().isEmpty()) {
                    CustomToast.showToast(this@ServiceDetailsActivity, "电话号码不能为空", Toast.LENGTH_LONG)

                } else if (fuwuPhone!!.text.toString().length in 13 downTo 10) {
                    CustomToast.showToast(this@ServiceDetailsActivity, "请输入正确的电话号码", Toast.LENGTH_LONG)
                } else {
                    map["mobile"] = fuwuPhone!!.text.toString()
                    OkhttpTool.getOkhttpTool().post(UrlConfig.USER_GUYONG, map, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            LhtTool.sendMessage(mHandler, e, NETWORKEXCEPTION)
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {

                            val s = response.body()!!.string()
                            LogUtils.d("=================response:$s")
                            val ms = Message()
                            ms.what = USER_GUYONG
                            val b = Bundle()
                            b.putString("s", s)
                            ms.data = b
                            mHandler.sendMessage(ms)

                        }
                    })
                    ld = LoadingDialog(this@ServiceDetailsActivity).setMessage("加载中...")
                    ld!!.show()
                }


            } else {
                val intent = Intent(this@ServiceDetailsActivity, UserLoginActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun initAndShowView() {
        banner.setOnClickListener(this)
        fwmc.setOnClickListener(this)
        fwXj.setOnClickListener(this)
        fwXj1.setOnClickListener(this)
        unit.setOnClickListener(this)
        textView10.setOnClickListener(this)
        fwYj.setOnClickListener(this)
        viewYj.setOnClickListener(this)
        fwNum.setOnClickListener(this)
        fwsLogo.setOnClickListener(this)
        fwsName.setOnClickListener(this)
        fwsCjjl.setOnClickListener(this)
        fwsDizhi.setOnClickListener(this)
        shop.setOnClickListener(this)
        fwxqFwxq.setOnClickListener(this)
        radioGroup.setOnClickListener(this)
        viewPager.setOnClickListener(this)
        shopXqLx.setOnClickListener(this)
        shopXqShou.setOnClickListener(this)
        shopXqLxt.setOnClickListener(this)

        val view = LayoutInflater.from(this).inflate(R.layout.dianpu_shouye_item, null)
        val view1 = LayoutInflater.from(this).inflate(R.layout.view_listview, null)
        val view_no = view1.findViewById<View>(R.id.view_no)
        view_no.visibility = View.GONE
        view_no.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val button = view1.findViewById<Button>(R.id.no_text)
        val textView = view1.findViewById<TextView>(R.id.no_txt)
        button.visibility = View.GONE
        textView.visibility = View.VISIBLE
        textView.text = "暂无评价记录"
        xRecyclerView = view1.findViewById(R.id.xRecyclerView)
        xRecyclerView!!.emptyView = view_no
        xRecyclerView!!.layoutManager = LinearLayoutManager(this)
        itemDecoration = LhtTool.MyItemDecoration(resources)
        xRecyclerView!!.addItemDecoration(itemDecoration!!)
        xRecyclerView!!.adapter = adapter
        sy_web = view.findViewById(R.id.dianpu_sy_web)
        webViewDianPuShow()//店铺首页webview显示
        val listv = ArrayList<View>()
        listv.add(view)
        listv.add(view1)
        val myPagerAdapter = MyPagerAdapter(listv)
        viewPager.adapter = myPagerAdapter
        xRecyclerView!!.setPullRefreshEnabled(false)
        viewListener()//viewPage监听事件
    }

    private fun webViewDianPuShow() {
        val wSet = sy_web!!.settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wSet.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        wSet.javaScriptEnabled = true
        wSet.domStorageEnabled = true
        //设置自适应屏幕，两者合用
        wSet.useWideViewPort = true//将图片调整到适合webview的大小
        wSet.loadWithOverviewMode = true// 缩放至屏幕的大小

        wSet.setSupportZoom(true)//支持缩放，默认为true。是下面那个的前提
        wSet.builtInZoomControls = true//设置内置的缩放控件
        wSet.cacheMode = WebSettings.LOAD_DEFAULT//不适用缓存

        //设为true会有泄露错误
        wSet.displayZoomControls = false//隐藏原生的缩放控件

        wSet.allowFileAccess = true//设置可以访问文件
        wSet.loadsImagesAutomatically = true//支持自动加载图片
        wSet.defaultTextEncodingName = "utf-8"//设置编码格式

        sy_web!!.requestFocusFromTouch()
        LhtTool.setCookieManager(UrlConfig.SERVERS_DETAILS+map["proid"])
        sy_web!!.loadUrl(UrlConfig.SERVERS_DETAILS+map["proid"])
    }

    /**
     * viewPage监听事件
     */
    private fun viewListener() {
        xRecyclerView!!.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {

            }

            override fun onLoadMore() {
                if (servicePingjiaGson == null) {
                    if (servicePingjiaGson!!.pagerInfo.nextPageIndex != 0) {
                        map["pages"] = servicePingjiaGson!!.pagerInfo.nextPageIndex
                        getPingjiaInfo()
                    } else {
                        xRecyclerView!!.setNoMore(true)
                    }
                }
            }
        })
        xRecyclerView!!.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.fwxq_fwxq) {
                viewPager.setCurrentItem(0, true)
            } else if (checkedId == R.id.fwxq_pj) {
                viewPager.setCurrentItem(1, true)

            }
        }
        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    fwxqFwxq.isChecked = true
                } else if (position == 1) {
                    if (servicePingjiaGson == null && pingjiaNetError) {
                        map["id"] = id!!
                        map["num"] = 10
                        map["pages"] = 1
                        getPingjiaInfo()
                    }
                    fwxqPj.isChecked = true

                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    /**
     * 服务详情数据
     */
    private fun fuWuXiangQingData() {
        OkhttpTool.getOkhttpTool().post(UrlConfig.FUWU_XIANGQING, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                LhtTool.sendMessage(mHandler, e, NETWORKEXCEPTION)

            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    serviceDetailsGson = Gson().fromJson(s, ServiceDetailsGson::class.java)
                    mHandler.sendEmptyMessage(SERVICE_DETAILS)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun favoriteShopRequest() {
        val map = HashMap<String, Any>()
        map["login_userid"] = MyApplication.userInfo!!.userID
        map["shop_userid"] = id!!
        Log.e("map", MyApplication.userInfo!!.userID + "----" + id)
        OkhttpTool.getOkhttpTool().post(UrlConfig.DIAN_URL, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
                when (s) {
                    "unlogin" -> mHandler.sendEmptyMessage(1)
                    "ok" -> mHandler.sendEmptyMessage(0)
                    "has" -> mHandler.sendEmptyMessage(2)
                }

            }
        })

    }

    /**
     * 获取评价数据
     */
    private fun getPingjiaInfo() {
        OkhttpTool.getOkhttpTool().post(UrlConfig.FUWU_PINGJIA, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(mHandler, e, NETWORKEXCEPTION)
                pingjiaNetError = true
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d("============response:$s")
                    servicePingjiaGson = Gson().fromJson(s, ServicePingjiaGson::class.java)
                    mHandler.sendEmptyMessage(SERVICE_PINGJIA)
                    pingjiaNetError = false
                } catch (e: Exception) {
                    pingjiaNetError = true
                    e.printStackTrace()
                }


            }
        })
        //        ld = new LoadingDialog(ServiceDetailsActivity.this).setMessage("加载中....");
        //        ld.show();
    }

}

