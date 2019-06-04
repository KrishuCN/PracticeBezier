package chen.vike.c680.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import chen.vike.c680.bean.FangAnXqGson
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.tools.UrlConfig
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
import java.util.*

/**
 * Created by lht on 2017/3/16.
 *
 *
 * 方案详情
 */

class PlanDetailsActivity : BaseStatusBarActivity() {

    private val map = HashMap<String, Any>()
    private var xRecyclerView: XRecyclerView? = null
    private var mLogo: ImageView? = null
    private var mShopname: TextView? = null
    private var mJifen: TextView? = null
    private var mVip: ImageView? = null
    private var mZb: ImageView? = null
    private var button: Button? = null
    private var imageGetter: Html.ImageGetter? = null
    private val strings = ArrayList<String>()
    private val url = ArrayList<String>()
    private var mHeader: TextView? = null
    private var mFooter: TextView? = null
    private var tab1: Button? = null
    private var tab2: Button? = null
    private var tab3: Button? = null
    private var view: View? = null
    private var fangAnXqGson: FangAnXqGson? = null
    private var type: Int = 0
    private var viewh: View? = null
    private var viewm: View? = null
    private var ld: LoadingDialog? = null
    private val rank = arrayOf("一等奖", "二等奖", "三等奖")
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    private val GET_XQ = 0x123
    private val T_MESSAGE = 0x122
    private val NETWORK_EXCEPTION = 0X111
    private val XUANBIAO = 0X121
    private val JJ_XUANBIAO = 0X211
    private val hd = MHandler(this)



    private class MHandler constructor(planDetailsActivity: PlanDetailsActivity):Handler(){
        private val weakReference:WeakReference<PlanDetailsActivity> = WeakReference(planDetailsActivity)
        private val planDetailsActivity = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            planDetailsActivity?.run {
                ld?.dismiss()
                if (msg?.what == GET_XQ) {

                    if (fangAnXqGson != null) {

                        val options = RequestOptions()
                                .placeholder(R.mipmap.image_loading)
                                .error(R.mipmap.image_erroe)

                        Glide.with(this).load(fangAnXqGson!!.cy_user_imageurl).apply(options).into(mLogo!!)
                        LhtTool.jifen(Integer.valueOf(fangAnXqGson!!.cy_user_grade), mJifen)
                        LhtTool.isVip(Integer.valueOf(fangAnXqGson!!.cy_user_check), mVip!!)
                        mShopname!!.text = fangAnXqGson!!.cy_user_name
                        val s = Arrays.asList(*fangAnXqGson!!.cy_fujian_list.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                        for (i in s.indices) {
                            if (s[i].contains(".jpg") || s[i].contains(".png") || s[i].contains(".gif") || s[i].contains(".jpeg")) {
                                strings.add(s[i])
                            } else {
                                url.add(s[i])
                            }
                        }
                        adapter.notifyDataSetChanged()
                        Thread(object : Runnable {
                            internal var msg1 = Message.obtain()

                            override fun run() {
                                val test: CharSequence
                                imageGetter = LhtTool.MyImageGetter(resources)
                                if (url.size == 0) {
                                    test = Html.fromHtml(fangAnXqGson!!.cy_zuopin, imageGetter, null)
                                } else {
                                    var s1 = ""
                                    for (i in url.indices) {
                                        s1 = s1 + url[i]
                                    }
                                    test = Html.fromHtml(fangAnXqGson!!.cy_zuopin + s1, imageGetter, null)
                                }
                                msg1.what = T_MESSAGE
                                msg1.obj = test
                                hd.sendMessage(msg1)
                            }
                        }).start()

                        LogUtils.d("==================fangAnXqGson.getCy_info_state():" + fangAnXqGson!!.cy_info_state)
                        when (fangAnXqGson!!.cy_info_state) {
                            "qixuan" -> {
                                mZb!!.setImageDrawable(resources.getDrawable(R.mipmap.bhg))
                                mZb!!.visibility = View.VISIBLE
                            }
                            "zhongbiao" -> {
                                mZb!!.setImageDrawable(resources.getDrawable(R.mipmap.zb))
                                mZb!!.visibility = View.VISIBLE
                            }
                            "jiang1" -> {
                                mZb!!.setImageDrawable(resources.getDrawable(R.mipmap.ydj))
                                mZb!!.visibility = View.VISIBLE
                            }
                            "jiang2" -> {
                                mZb!!.setImageDrawable(resources.getDrawable(R.mipmap.edj))
                                mZb!!.visibility = View.VISIBLE
                            }
                            "jiang3" -> {
                                mZb!!.setImageDrawable(resources.getDrawable(R.mipmap.sdj))
                                mZb!!.visibility = View.VISIBLE
                            }
                            "hege" -> {
                                mZb!!.setImageDrawable(resources.getDrawable(R.mipmap.hg))
                                mZb!!.visibility = View.VISIBLE
                            }
                            "youxiu" -> {
                                mZb!!.setImageDrawable(resources.getDrawable(R.mipmap.yxj))
                                mZb!!.visibility = View.VISIBLE
                            }
                            "beixuan" -> {
                                mZb!!.setImageDrawable(resources.getDrawable(R.mipmap.ybx))
                                mZb!!.visibility = View.VISIBLE
                            }
                            "nohege" -> {
                                mZb!!.setImageDrawable(resources.getDrawable(R.mipmap.bhg))
                                mZb!!.visibility = View.VISIBLE
                            }
                            "yeshege" -> {
                                mZb!!.setImageDrawable(resources.getDrawable(R.mipmap.hg))
                                mZb!!.visibility = View.VISIBLE
                            }
                            else -> mZb!!.visibility = View.GONE
                        }
                        if (fangAnXqGson != null && fangAnXqGson!!.item_zab_do != null) {
                            if (fangAnXqGson!!.item_zab_do == "1") {
                                xRecyclerView!!.addHeaderView(viewh)
                                mHeader!!.textSize = 16f
                                mHeader!!.text = Html.fromHtml("竞标报价：<font color='#df231b'>￥" + fangAnXqGson!!.cy_baojia + "</font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;开发周期：<font color='#df231b'>" + fangAnXqGson!!.cy_ybtime + "</font> 天")
                            }
                        }

                        if (Integer.valueOf(fangAnXqGson!!.item_state) == 0 && MyApplication.userInfo != null) {
                            if (fangAnXqGson!!.item_uid == MyApplication.userInfo!!.userID) {
                                view!!.visibility = View.VISIBLE
                            } else {
                                view!!.visibility = View.GONE
                            }
                        }

                        if (Integer.valueOf(fangAnXqGson!!.cy_zhongbiao) > 0) {
                            view!!.visibility = View.GONE
                        } else {
                            view!!.visibility = View.VISIBLE
                        }
                        //                    }
                    }
                    //
                } else if (msg?.what == NETWORK_EXCEPTION) {
                    LhtTool.showNetworkException(this, msg)
                } else if (msg?.what == T_MESSAGE) {
                    //妈的，找错ID弄了好久，擦！
                    mFooter!!.text = msg.obj as CharSequence
                } else if (msg?.what == XUANBIAO) {

                    val s = msg.data.getString("s")
                    when (s) {
                        "nodo" -> CustomToast.showToast(this, "无效请求参数", Toast.LENGTH_LONG)
                        "nojj" -> CustomToast.showToast(this, "无效操作方式", Toast.LENGTH_LONG)
                        "itemover" -> CustomToast.showToast(this, "项目已结束", Toast.LENGTH_LONG)
                        "doover" -> CustomToast.showToast(this, "该项目已评标了", Toast.LENGTH_LONG)
                        "norole" -> CustomToast.showToast(this, "您不是雇主，无权操作", Toast.LENGTH_LONG)
                        "ok" -> {
                            CustomToast.showToast(this, "评标成功", Toast.LENGTH_SHORT)
                            //可能有问题，注意下
                            getFaXq()
                        }
                        "haszhaobiaobjzb" -> CustomToast.showToast(this, "此招标项目已评标了", Toast.LENGTH_LONG)
                        "fail" -> CustomToast.showToast(this, "评标失败", Toast.LENGTH_LONG)
                        "nozhaobiao" -> CustomToast.showToast(this, "无效招标信息，不能评标", Toast.LENGTH_LONG)
                        "nocanyuitem" -> CustomToast.showToast(this, "暂不能评标", Toast.LENGTH_LONG)
                        "nopos1" -> CustomToast.showToast(this, "一等奖已满了", Toast.LENGTH_LONG)
                        "nopos2" -> CustomToast.showToast(this, "二等奖已满了", Toast.LENGTH_LONG)
                        "nopos3" -> CustomToast.showToast(this, "三等奖已满了", Toast.LENGTH_LONG)
                        "nopay" -> CustomToast.showToast(this, "不能评标，还没有托管赏金", Toast.LENGTH_LONG)
                        "jj_num_full" -> CustomToast.showToast(this, "此项目合格稿件已选满了", Toast.LENGTH_LONG)
                        "nocy" -> CustomToast.showToast(this, "已被删除或屏蔽", Toast.LENGTH_LONG)
                        "unlogin" -> CustomToast.showToast(this, "请先登录后操作", Toast.LENGTH_LONG)
                    }

                } else if (msg?.what == JJ_XUANBIAO) {

                    val s = msg.data.getString("s")
                    when (s) {
                        "nodo" -> CustomToast.showToast(this, "无效请求参数", Toast.LENGTH_LONG)
                        "nojj" -> CustomToast.showToast(this, "无效操作方式", Toast.LENGTH_LONG)
                        "itemover" -> CustomToast.showToast(this, "项目已结束", Toast.LENGTH_LONG)
                        "doover" -> CustomToast.showToast(this, "该项目已评标了", Toast.LENGTH_LONG)
                        "norole" -> CustomToast.showToast(this, "您不是雇主，无权操作", Toast.LENGTH_LONG)
                        "ok" -> {
                            CustomToast.showToast(this, "评标成功", Toast.LENGTH_SHORT)
                            getFaXq()
                        }
                        "fail" -> CustomToast.showToast(this, "评标失败", Toast.LENGTH_LONG)
                        "nopay" -> CustomToast.showToast(this, "不能评标，还没有托管赏金", Toast.LENGTH_LONG)
                        "full" -> CustomToast.showToast(this, "此项目合格稿件已选满了", Toast.LENGTH_LONG)
                        "nocy" -> CustomToast.showToast(this, "已被删除或屏蔽", Toast.LENGTH_LONG)
                        "unlogin" -> CustomToast.showToast(this, "请先登录后操作", Toast.LENGTH_LONG)
                    }
                }
            }
        }
    }


    internal var adapter:RecyclerView.Adapter<ImgViewHolder> = object : RecyclerView.Adapter<ImgViewHolder>() {
        override fun onBindViewHolder(p0: ImgViewHolder, p1: Int) {
                val params = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                p0.view.layoutParams = params
                Glide.with(this@PlanDetailsActivity).load(strings[p1]).into(p0.imageView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgViewHolder {
            return ImgViewHolder(LayoutInflater.from(this@PlanDetailsActivity).inflate(R.layout.view_img, parent, false))
        }

        override fun getItemCount(): Int {
            return strings.size
        }
    }

      open class ImgViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
         internal var imageView: ImageView = view.findViewById<View>(R.id.icon) as ImageView
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fangan_xiangqing_activity)

        title.text = "方案详情"
        mShopname = findViewById(R.id.cyTitle)
        mLogo = findViewById(R.id.cyLogo)
        mJifen = findViewById(R.id.grade)
        mVip = findViewById(R.id.vip_logo)
        mZb = findViewById(R.id.zb)
        button = findViewById(R.id.button)
        tab1 = findViewById(R.id.tab1)
        tab2 = findViewById(R.id.tab2)
        tab3 = findViewById(R.id.tab3)
        view = findViewById(R.id.bottom)
        xRecyclerView = findViewById(R.id.xRecyclerView)
        xRecyclerView!!.layoutManager = LinearLayoutManager(this)
        xRecyclerView!!.adapter = adapter
        xRecyclerView!!.setLoadingMoreEnabled(true)
        xRecyclerView!!.setPullRefreshEnabled(false)
        viewm = LayoutInflater.from(this).inflate(R.layout.view_footer, null)
        viewh = LayoutInflater.from(this).inflate(R.layout.view_header, null)
        mHeader = viewh!!.findViewById(R.id.textView26)
        mFooter = viewm!!.findViewById(R.id.textView_foot)
        xRecyclerView!!.setFootView(viewm)
        itemDecoration = LhtTool.MyItemDecoration(resources)
        xRecyclerView!!.addItemDecoration(itemDecoration!!)
        val bundle = intent.extras
        if (bundle != null) {
            val itemid = bundle.get("ID").toString()
            val CYID = bundle.get("CYID").toString()
            type = Integer.parseInt(bundle.get("TYPE").toString())
            if (LhtTool.isLogin) {
                map["userid"] = MyApplication.userInfo!!.userID
                map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
                map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
            }
            map["cyid"] = CYID
            map["itemid"] = itemid
            if (type == 0) {
                tab1!!.text = "中标"
                tab2!!.text = "备选"
                tab3!!.text = "淘汰"
            } else if (type == 1) {
                tab1!!.visibility = View.GONE
                tab2!!.text = "选他中标"
                tab3!!.visibility = View.GONE
            } else if (type == 2) {
                tab1!!.text = "合格"
                tab2!!.visibility = View.GONE
                tab3!!.text = "不合格"
                map["cytype"] = 1
            }
            getFaXq()
            button!!.setOnClickListener {
                if (LhtTool.isLogin) {
                    //                        跳到聊天界面
                    if (fangAnXqGson != null && fangAnXqGson!!.cy_userid != null) {
                        val intent = Intent(this@PlanDetailsActivity, MessageChatActivity::class.java)
                        intent.putExtra("ID", fangAnXqGson!!.cy_userid)
                        intent.putExtra("name", fangAnXqGson!!.cy_user_name)
                        startActivity(intent)
                    }
                } else {
                    CustomToast.showToast(this@PlanDetailsActivity, "亲，你还没有登录哦~", Toast.LENGTH_SHORT)
                    val intent = Intent(this@PlanDetailsActivity, UserLoginActivity::class.java)
                    startActivity(intent)
                }
            }

            tab1!!.setOnClickListener {
                Log.e("fangan", "sssss")
                if (fangAnXqGson!!.item_zb_mode == "0") {

                    val builder = AlertDialog.Builder(this@PlanDetailsActivity)
                            .setTitle("温馨提示")
                            .setMessage("您确定该方案" + tab1!!.text.toString() + "吗？")
                            .setPositiveButton("确定") { dialog, which ->
                                if (type == 0) {
                                    map["dotype"] = "ok"
                                    getXB()
                                } else if (type == 1) {

                                } else if (type == 2) {
                                    map["dotype"] = "yeshg"
                                    getJJ_XB()
                                }
                                ld = LoadingDialog(this@PlanDetailsActivity).setMessage("数据请求中...")
                                ld!!.show()
                            }
                            .setNeutralButton("取消") { dialog, which -> dialog.dismiss() }
                    builder.show()
                } else if (fangAnXqGson!!.item_zb_mode == "1") {
                    map["dotype"] = "1"
                    val builder = AlertDialog.Builder(this@PlanDetailsActivity)
                            .setTitle("您将设置该方案为：")
                            .setSingleChoiceItems(rank, 0) { dialog, which ->
                                when (which) {
                                    0 -> map["dotype"] = "1"
                                    1 -> map["dotype"] = "2"
                                    2 -> map["dotype"] = "3"
                                }
                            }
                            .setPositiveButton("确定") { dialog, which ->
                                if (type == 0) {
                                    getXB()
                                } else if (type == 1) {

                                } else if (type == 2) {
                                    map["dotype"] = "yeshg"
                                    getJJ_XB()
                                }
                                ld = LoadingDialog(this@PlanDetailsActivity).setMessage("数据请求中...")
                                ld!!.show()
                            }
                            .setNeutralButton("取消") { dialog, which -> dialog.dismiss() }
                    builder.show()
                } else if (fangAnXqGson!!.item_zb_mode == "2") {
                    val builder = AlertDialog.Builder(this@PlanDetailsActivity)
                            .setTitle("温馨提示")
                            .setMessage("您确定该方案" + tab1!!.text.toString() + "吗？")
                            .setNegativeButton("确定") { dialog, which ->
                                if (type == 0) {
                                    map["dotype"] = "out"
                                    getXB()
                                } else if (type == 1) {
                                } else if (type == 2) {
                                    map["dotype"] = "yeshg"
                                    getJJ_XB()
                                }
                                ld = LoadingDialog(this@PlanDetailsActivity).setMessage("数据请求中...")
                                ld!!.show()
                            }
                            .setNeutralButton("否") { dialog, which -> dialog.dismiss() }
                    builder.show()
                }
            }
            tab2!!.setOnClickListener {
                val builder = AlertDialog.Builder(this@PlanDetailsActivity)
                        .setTitle("温馨提示")
                        .setMessage("您确定该方案" + tab2!!.text.toString() + "吗？")
                        .setNegativeButton("确定") { dialog, which ->
                            if (type == 0) {
                                map["dotype"] = "bak"
                                getXB()
                            } else if (type == 1) {
                                map["dotype"] = "zhaobiao"
                                getXB()
                            } else if (type == 2) {

                            }
                            ld = LoadingDialog(this@PlanDetailsActivity).setMessage("数据请求中...")
                            ld!!.show()
                        }
                        .setNeutralButton("否") { dialog, which -> dialog.dismiss() }
                builder.show()
            }
            tab3!!.setOnClickListener {
                Log.e("fangan", "lllllll")
                val builder = AlertDialog.Builder(this@PlanDetailsActivity)
                        .setTitle("温馨提示")
                        .setMessage("您确定该方案" + tab3!!.text.toString() + "吗？")
                        .setNegativeButton("确定") { dialog, which ->
                            if (type == 0) {
                                map["dotype"] = "out"
                                getXB()
                            } else if (type == 1) {
                            } else if (type == 2) {
                                map["dotype"] = "nohg"
                                getJJ_XB()
                            }
                            ld = LoadingDialog(this@PlanDetailsActivity).setMessage("数据请求中...")
                            ld!!.show()
                        }
                        .setNeutralButton("否") { dialog, which -> dialog.dismiss() }
                builder.show()
            }
        }
    }

    private fun getFaXq() {
        OkhttpTool.getOkhttpTool().post(UrlConfig.GAOJIAN_XQ, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d("================response:$s")
                    fangAnXqGson = Gson().fromJson(s, FangAnXqGson::class.java)
                    if (fangAnXqGson != null) {
                        hd.sendEmptyMessage(GET_XQ)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        })
    }

    private fun getXB() {
        LogUtils.d("================map cyid:" + map["cyid"]!!)
        LogUtils.d("================map dotype:" + map["dotype"]!!)
        OkhttpTool.getOkhttpTool().post(UrlConfig.XUANBIAO_ACTION, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val s = response.body()!!.string()
                LogUtils.d("==============response:$s")
                val ms = Message()
                ms.what = XUANBIAO
                val b = Bundle()
                b.putString("s", s)
                ms.data = b
                hd.sendMessage(ms)
            }
        })
    }

    private fun getJJ_XB() {
        OkhttpTool.getOkhttpTool().post(UrlConfig.XUANBIAO_ACTION_JJ, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val s = response.body()!!.string()
                LogUtils.d("==============response:$s")
                val ms = Message()
                ms.what = JJ_XUANBIAO
                val b = Bundle()
                b.putString("s", s)
                ms.data = b
                hd.sendMessage(ms)

            }
        })
    }

    override fun onStop() {
        super.onStop()
        hd.removeCallbacksAndMessages(null)
        Glide.with(this).pauseRequests()
    }
}