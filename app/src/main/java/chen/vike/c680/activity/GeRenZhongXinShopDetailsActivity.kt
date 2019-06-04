package chen.vike.c680.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import chen.vike.c680.adapter.MyPagerAdapter
import chen.vike.c680.bean.ShopFuwuListGson
import chen.vike.c680.bean.ShopInfoGson
import chen.vike.c680.bean.ShopZuopinGson
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.*
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.lht.vike.a680_v1.R
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
 * 店铺详情
 */

class GeRenZhongXinShopDetailsActivity : BaseStatusBarActivity() {

    private var bundle: Bundle? = null
    private var shop_logo: ImageView? = null
    private var mVip: ImageView? = null
    private var mXydj: TextView? = null
    private var shop_sycj: TextView? = null
    private var ship_cjbs: TextView? = null
    private var mWczl_num: TextView? = null
    private var mWczl_bj: TextView? = null
    private var mWcsd_num: TextView? = null
    private var mWcsd_bj: TextView? = null
    private var mFwtd_num: TextView? = null
    private var mFwtd_bj: TextView? = null
    private var mFwfw: TextView? = null
    private var shop_xq_lx: TextView? = null
    private var mWczl_num1: TextView? = null
    private var mWczl_bj1: TextView? = null
    private var mWcsd_num1: TextView? = null
    private var mWcsd_bj1: TextView? = null
    private var mFwtd_num1: TextView? = null
    private var mFwtd_bj1: TextView? = null
    private var mView: View? = null
    private var dpxq_by_sjs: TextView? = null
    private var dpxq_sy_cjs: TextView? = null
    private var dpxq_by_cje: TextView? = null
    private var dpxq_sy_cje: TextView? = null
    private var dpxq_hpl: TextView? = null
    private var dpxq_hpl_jdt: TextView? = null
    private var dpxq_hprs: TextView? = null
    private var dpxq_cprs: TextView? = null
    private var mContent: TextView? = null
    private var button: Button? = null
    private val map = HashMap<String, Any>()
    private var radioGroup: RadioGroup? = null
    private var tab1: RadioButton? = null
    private var tab2: RadioButton? = null
    private var tab3: RadioButton? = null
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
    private var adapter1: BaseRecyclerAdapter<*>? = null
    private var adapter2: BaseRecyclerAdapter<*>? = null
    private val metrics = DisplayMetrics()
    private var ld: LoadingDialog? = null
    private val GET_SHOP_INFO = 0x123
    private val SHOP_FUWU_LIST = 0x122
    private val SHOP_ZUOPIN_LIST = 0x121
    private val NETWORKEXCEPTION = 0X111
    private var firstFlag: Boolean = false
    private val hd:MHandler = MHandler(this)



    private class MHandler(geRenZhongXinShopDetailsActivity: GeRenZhongXinShopDetailsActivity):Handler(){
        private val weakReference:WeakReference<GeRenZhongXinShopDetailsActivity> = WeakReference(geRenZhongXinShopDetailsActivity)
        private val activity = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            activity?.run {
                ld?.dismiss()
                if (msg?.what == GET_SHOP_INFO) {

                    getFuwuList()

                    val options = RequestOptions()
                            .placeholder(R.mipmap.image_loading)
                            .error(R.mipmap.image_erroe)

                    Glide.with(this)
                            .load(shopInfoGson!!.imgurl + "?123")
                            .apply(options)
                            .into(shop_logo!!)


                    title.text = shopInfoGson!!.shopname
                    LhtTool.jifen(Integer.valueOf(shopInfoGson!!.grade), mXydj)
                    LhtTool.shopDetailsIsVip(Integer.valueOf(shopInfoGson!!.check), mVip!!)
                    shop_sycj!!.text = "近三月成交" + shopInfoGson!!.cj_month3_money + "元"
                    ship_cjbs!!.text = "交易：" + shopInfoGson!!.cj_month3_num + "笔"
                    mWczl_num!!.text = shopInfoGson!!.pj_zhiliang
                    mWczl_num1!!.text = shopInfoGson!!.pj_zhiliang
                    val decimalFormat = DecimalFormat("0.00")

                    if (java.lang.Float.valueOf(shopInfoGson!!.pj_zhiliang) > java.lang.Float.valueOf(shopInfoGson!!.pj_avg)) {

                        mWczl_bj!!.text = "高"
                        mWczl_bj!!.setBackgroundColor(resources.getColor(R.color.text_color_2))

                        mWczl_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_avg))
                        mWczl_bj1!!.setTextColor(resources.getColor(R.color.text_color_2))
                    } else {
                        mWczl_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_avg))
                        mWczl_bj1!!.setTextColor(resources.getColor(R.color.text_color_10))
                        mWczl_bj!!.text = "低"
                        mWczl_bj!!.setBackgroundColor(resources.getColor(R.color.text_color_10))
                    }
                    mWcsd_num!!.text = shopInfoGson!!.pj_xiaolv
                    mWcsd_num1!!.text = shopInfoGson!!.pj_xiaolv
                    if (java.lang.Float.valueOf(shopInfoGson!!.pj_xiaolv) > java.lang.Float.valueOf(shopInfoGson!!.pj_avg)) {
                        mWcsd_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_avg))
                        mWcsd_bj1!!.setTextColor(resources.getColor(R.color.text_color_2))
                        mWcsd_bj!!.text = "高"
                        mWcsd_bj!!.setBackgroundColor(resources.getColor(R.color.text_color_2))
                    } else {
                        mWcsd_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_avg))
                        mWcsd_bj1!!.setTextColor(LhtTool.getColor(baseContext, R.color.text_color_10))

                        mWcsd_bj!!.text = "低"
                        mWcsd_bj!!.setBackgroundColor(resources.getColor(R.color.text_color_10))
                    }
                    mFwtd_num!!.text = shopInfoGson!!.pj_taidu
                    mFwtd_num1!!.text = shopInfoGson!!.pj_taidu

                    if (java.lang.Float.valueOf(shopInfoGson!!.pj_taidu) > java.lang.Float.valueOf(shopInfoGson!!.pj_avg)) {
                        mFwtd_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_taidu))
                        mFwtd_bj1!!.setTextColor(resources.getColor(R.color.text_color_2))
                        mFwtd_bj!!.text = "高"
                        mFwtd_bj!!.setBackgroundColor(resources.getColor(R.color.text_color_2))
                    } else {
                        mFwtd_bj1!!.text = decimalFormat.format(java.lang.Float.valueOf(shopInfoGson!!.pj_avg))
                        mFwtd_bj1!!.setTextColor(resources.getColor(R.color.text_color_10))
                        mFwtd_bj!!.text = "低"
                        mFwtd_bj!!.setBackgroundColor(resources.getColor(R.color.text_color_10))
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
                    mFwfw!!.text = s
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
                        adapter1!!.notifyDataSetChanged()
                    } else {
                        adapter1!!.notifyDataSetChanged()
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
                        adapter2!!.notifyDataSetChanged()
                    } else {
                        adapter2!!.notifyDataSetChanged()
                    }
                    view_no!!.visibility = View.GONE
                    if (list2.size == 0) {
                        view_no!!.visibility = View.VISIBLE
                    }

                } else if (msg?.what == NETWORKEXCEPTION) {

                    LhtTool.showNetworkException(this, msg)

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowManager.defaultDisplay.getMetrics(metrics)
        setContentView(R.layout.shop_details_activity)

        title.text = "店铺详情"
        shop_logo = findViewById<View>(R.id.shop_logo) as ImageView
        mVip = findViewById<View>(R.id.shop_vip_img) as ImageView
        mXydj = findViewById<View>(R.id.shop_xy_dj) as TextView
        shop_sycj = findViewById<View>(R.id.shop_sycj) as TextView
        ship_cjbs = findViewById<View>(R.id.ship_cjbs) as TextView
        radioGroup = findViewById<View>(R.id.radioGroup) as RadioGroup
        tab1 = findViewById<View>(R.id.shop_xq_csfw) as RadioButton
        tab2 = findViewById<View>(R.id.shop_xq_cgal) as RadioButton
        tab3 = findViewById<View>(R.id.shop_xq_dpxq) as RadioButton
        mWczl_num = findViewById<View>(R.id.shop_xq_wczl_fs) as TextView
        mWczl_bj = findViewById<View>(R.id.shop_xq_wczl_zt) as TextView
        mWcsd_num = findViewById<View>(R.id.shop_xq_gzsd_fs) as TextView
        mWcsd_bj = findViewById<View>(R.id.shop_xq_gzsd_zt) as TextView
        mFwtd_num = findViewById<View>(R.id.shop_xq_fwtd_fs) as TextView
        mFwtd_bj = findViewById<View>(R.id.shop_xq_fwtd_zt) as TextView
        mFwfw = findViewById<View>(R.id.jineng) as TextView
        viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        viewPager!!.offscreenPageLimit = 3
        shop_xq_lx = findViewById<View>(R.id.shop_xq_lx) as TextView
        mView = LayoutInflater.from(this).inflate(R.layout.item_dianpuxiangqing, null)
        mWczl_num1 = mView!!.findViewById<View>(R.id.dpxq_wczl_fs) as TextView
        mWczl_bj1 = mView!!.findViewById<View>(R.id.dpxq_wczl_c) as TextView
        mWcsd_num1 = mView!!.findViewById<View>(R.id.dpxq_fzsd_fs) as TextView
        mWcsd_bj1 = mView!!.findViewById<View>(R.id.dpxq_gzsd_c) as TextView
        mFwtd_num1 = mView!!.findViewById<View>(R.id.dpxq_fwtd_fs) as TextView
        mFwtd_bj1 = mView!!.findViewById<View>(R.id.dpxq_fwtd_c) as TextView
        dpxq_by_sjs = mView!!.findViewById<View>(R.id.dpxq_by_sjs) as TextView
        dpxq_by_cje = mView!!.findViewById<View>(R.id.dpxq_by_cje) as TextView
        dpxq_sy_cje = mView!!.findViewById<View>(R.id.dpxq_sy_cje) as TextView
        dpxq_sy_cjs = mView!!.findViewById<View>(R.id.dpxq_sy_cjs) as TextView
        dpxq_hpl = mView!!.findViewById<View>(R.id.dpxq_hpl) as TextView
        dpxq_hpl_jdt = mView!!.findViewById<View>(R.id.dpxq_hpl_jdt) as TextView
        dpxq_hprs = mView!!.findViewById<View>(R.id.dpxq_hprs) as TextView
        dpxq_cprs = mView!!.findViewById<View>(R.id.dpxq_cprs) as TextView
        mContent = mView!!.findViewById<View>(R.id.content) as TextView
        button = findViewById<View>(R.id.shop_xq_lxt) as Button
        button!!.setOnClickListener {
            //立即雇佣
            if (LhtTool.isLogin) {
                if (MyApplication.userInfo!!.userID == shopInfoGson!!.userid) {
                    CustomToast.showToast(this@GeRenZhongXinShopDetailsActivity, "不能雇佣自己", Toast.LENGTH_SHORT)
                } else {
                    val intent = Intent(this@GeRenZhongXinShopDetailsActivity, ImmediateGyActivity::class.java)
                    intent.putExtra("ID", shopInfoGson!!.userid)
                    startActivity(intent)
                }
            } else {
                CustomToast.showToast(this@GeRenZhongXinShopDetailsActivity, "请先登录", Toast.LENGTH_SHORT)
                val intent = Intent(this@GeRenZhongXinShopDetailsActivity, UserLoginActivity::class.java)
                startActivity(intent)
            }
        }
        bundle = intent.extras
        LoadData()

    }

    fun LoadData() {
        val view1 = LayoutInflater.from(this).inflate(R.layout.view_listview, null)
        val view0 = LayoutInflater.from(this).inflate(R.layout.view_listview, null)
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

        recyclerView2 = view1.findViewById<View>(R.id.xRecyclerView) as XRecyclerView
        recyclerView2!!.setBackgroundColor(resources.getColor(R.color.colorbg))
        recyclerView1 = view0.findViewById<View>(R.id.xRecyclerView) as XRecyclerView
        headerparent.addView(view_no1)
        headerparent2.addView(view_no)
        recyclerView2!!.addHeaderView(headerparent2)
        recyclerView1!!.addHeaderView(headerparent)

        recyclerView1!!.layoutManager = GridLayoutManager(this, 1)
        recyclerView2!!.layoutManager = GridLayoutManager(this, 2)
        recyclerView1!!.setPullRefreshEnabled(false)
        recyclerView2!!.setPullRefreshEnabled(false)
        map["userid"] = bundle!!.getString("ID")!!
        map["hits"] = 1
        map["num"] = 10
        map["pages"] = 1
        shop_xq_lx!!.setOnClickListener {
            //开始和XX聊天
            if (LhtTool.isLogin) {
                if (MyApplication.userInfo!!.userID == shopInfoGson!!.userid) {
                    CustomToast.showToast(this@GeRenZhongXinShopDetailsActivity, "不能和自己聊天", Toast.LENGTH_SHORT)
                } else {
                    //跳到聊天界面
                    val intent = Intent(this@GeRenZhongXinShopDetailsActivity, MessageChatActivity::class.java)
                    intent.putExtra("ID", shopInfoGson!!.userid)
                    intent.putExtra("name", shopInfoGson!!.username)
                    startActivity(intent)
                }
            } else {
                CustomToast.showToast(this@GeRenZhongXinShopDetailsActivity, "亲，你还没有登录哦~", Toast.LENGTH_SHORT)
                val intent = Intent(this@GeRenZhongXinShopDetailsActivity, UserLoginActivity::class.java)
                startActivity(intent)
            }
        }
        OkhttpTool.getOkhttpTool().post(UrlConfig.GET_SHOP_INFO, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)

            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d("==================response:$s")
                    shopInfoGson = Gson().fromJson(s, ShopInfoGson::class.java)
                    hd.sendEmptyMessage(GET_SHOP_INFO)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
        ld = LoadingDialog(this@GeRenZhongXinShopDetailsActivity).setMessage("加载中....")
        ld!!.show()
        img.setImageResource(R.mipmap.fenxiang)
        img.setOnClickListener {
            //分享
            if (LhtTool.isLogin) {
                LhtTool.showShare(this@GeRenZhongXinShopDetailsActivity, shopInfoGson!!.shopname,
                        shopInfoGson!!.shop_url + "?tj=" + MyApplication.userInfo!!.userID,
                        shopInfoGson!!.shopname + "，擅长技能：" + shopInfoGson!!.jineng,
                        shopInfoGson!!.shop_url, shopInfoGson!!.imgurl)
            } else {
                LhtTool.showShare(this@GeRenZhongXinShopDetailsActivity, shopInfoGson!!.shopname,
                        shopInfoGson!!.shop_url, shopInfoGson!!.shopname + "，擅长技能：" + shopInfoGson!!.jineng,
                        shopInfoGson!!.shop_url, shopInfoGson!!.imgurl)
            }
        }
        //服务列表
        adapter1 = object :BaseRecyclerAdapter<ShopFuwuListGson.ListBean>(list1,this){
            override fun getItemLayoutId(viewType: Int): Int {
                return R.layout.fuwu_list_item
            }

            override fun bindData(holder: RecyclerViewHolder, position: Int, item: ShopFuwuListGson.ListBean) {

                holder.setVisibility(R.id.textView27, View.GONE)
                holder.setText(R.id.title, item.title)

                val options = RequestOptions()
                        .placeholder(R.mipmap.image_loading)
                        .error(R.mipmap.image_erroe)
                        .centerCrop()
                Glide.with(applicationContext)
                        .load(item.imgUrl)
                        .apply(options)
                        .into(holder.getImageView(R.id.fw_logo))

                holder.itemView.setOnClickListener {
                    val intent = Intent(this@GeRenZhongXinShopDetailsActivity, ServiceDetailsActivity::class.java)
                    intent.putExtra("ID", item.id)
                    startActivityForResult(intent, 3)
                }
                holder.setText(R.id.fw_je, item.showmoney)
                holder.setText(R.id.fw_class, item.typename)
                holder.setText(R.id.cjs, "成交" + item.cjnum + "笔")
            }
        }
        adapter2 = object : BaseRecyclerAdapter<ShopZuopinGson.ListBean>(list2,this){
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

                Glide.with(this@GeRenZhongXinShopDetailsActivity)
                        .load(list2[position].zuopinimg)
                        .apply(options)
                        .into(holder.getImageView(R.id.imageView))

                holder.itemView.setOnClickListener {
                    val intent = Intent(this@GeRenZhongXinShopDetailsActivity, CaseDetailsActivity::class.java)
                    intent.putExtra("BEAN", shopInfoGson)
                    intent.putExtra("ID", list2[position].zuopinid)
                    startActivity(intent)
                }
            }

        }
        recyclerView1!!.adapter = adapter1
        recyclerView2!!.adapter = adapter2
        //作品案例列表
        val listv = ArrayList<View>()
        listv.add(view0)
        listv.add(view1)
        listv.add(mView!!)
        val myPagerAdapter = MyPagerAdapter(listv)
        viewPager!!.adapter = myPagerAdapter
        viewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    tab1!!.isChecked = true
                    getFuwuList()
                } else if (position == 1) {

                    tab2!!.isChecked = true
                    OkhttpTool.getOkhttpTool().post(UrlConfig.SHOP_ZUOPIN_LIST, map, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)

                        }

                        override fun onResponse(call: Call, response: Response) {
                            try {
                                val s = response.body()!!.string()
                                LogUtils.d("====================response:$s")
                                shopZuopinGson = Gson().fromJson(s, ShopZuopinGson::class.java)
                                hd.sendEmptyMessage(SHOP_ZUOPIN_LIST)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    })
                    ld = LoadingDialog(this@GeRenZhongXinShopDetailsActivity).setMessage("加载中....")
                    ld!!.show()

                } else if (position == 2) {
                    tab3!!.isChecked = true
                }


            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        radioGroup!!.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.shop_xq_csfw) {
                viewPager!!.setCurrentItem(0, true)
            } else if (checkedId == R.id.shop_xq_cgal) {
                viewPager!!.setCurrentItem(1, true)
            } else if (checkedId == R.id.shop_xq_dpxq) {
                viewPager!!.setCurrentItem(2, true)
            }
        }

    }

    private fun getFuwuList() {
        OkhttpTool.getOkhttpTool().post(UrlConfig.SHOP_FUWU_LIST, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)

            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d("===============response:$s")
                    shopFuwuListGson = Gson().fromJson(s, ShopFuwuListGson::class.java)
                    hd.sendEmptyMessage(SHOP_FUWU_LIST)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
        ld = LoadingDialog(this@GeRenZhongXinShopDetailsActivity).setMessage("加载中....")
        ld!!.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        hd.removeCallbacksAndMessages(null)
    }
}
