package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import chen.vike.c680.tools.TimeOut
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.jcodecraeer.xrecyclerview.XRecyclerView
import chen.vike.c680.bean.ItemInfoGson
import chen.vike.c680.bean.JiJianFangAnGson
import chen.vike.c680.bean.XuanShangFangAnGson
import chen.vike.c680.bean.ZhaoBiaoFangAnGson
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomDigitalClockStar
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.adapter.ItemAdapter
import com.lht.vike.a680_v1.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lht on 2017/3/16.
 *
 * 订单详情
 */

class OrderDetailsActivity : BaseStatusBarActivity() {

    private val map = HashMap<String, Any>()
    private var itemInfoGson: ItemInfoGson? = null
    private var xuanShangFangAnGson: XuanShangFangAnGson? = null
    private var zhaoBiaoFangAnGson: ZhaoBiaoFangAnGson? = null
    private var jiJianFangAnGson: JiJianFangAnGson? = null
    private var id: String? = null
    private var dd_ddzt: TextView? = null
    private var dd_money: TextView? = null
    private var dd_type: TextView? = null
    private var dd_number: TextView? = null
    private var dd_title: TextView? = null
    private var dd_fabu_time: CustomDigitalClockStar? = null
    private var dd_content: TextView? = null
    private var dd_delete: TextView? = null
    private var dd_update: TextView? = null
    private var dd_istg: ImageView? = null
    private var dd_state: View? = null
    private var dd_footer: View? = null
    private var xRecyclerView: XRecyclerView? = null
    private var adapter: ItemAdapter? = null
    private val list = ArrayList<Any>()
    private var type: Int = 0
    private var allCase: View? = null
    private var TYPE: Int = 0
    private var mControl: Button? = null
    private var scrollView: View? = null
    private var cynum: TextView? = null
    private var ld: LoadingDialog? = null
    private var imageGetter: Html.ImageGetter? = null
    private val GET_MESSAGE = 0x123
    private val GET_XS_MESSAGE = 0x143
    private val GET_ZB_MESSAGE = 0x153
    private val GET_JJ_MESSAGE = 0x163
    private val DELETE_PRO = 0x173
    private val GET_HTML = 0x113
    private val NETWORKEXCEPTION = 0X111
    private val hd: MHandler = MHandler(this)

    private class MHandler(activity: OrderDetailsActivity) : Handler() {
        private val weakReference: WeakReference<OrderDetailsActivity> = WeakReference(activity)
        private var activity = weakReference.get()

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            activity?.run {

                TimeOut.timeCancel()
                if (msg?.what == GET_MESSAGE) {
                    ld?.dismiss()
                    if (itemInfoGson != null) {
                        cynum!!.text = "服务商提交的稿件(" + itemInfoGson!!.item_tjnum + ")"
                        try {
                            @SuppressLint("SimpleDateFormat") val starTime = Date(SimpleDateFormat(
                                    "yyyy/MM/dd HH:mm:ss").format(Date())).time - Date(itemInfoGson!!.item_addtime).time
                            dd_fabu_time!!.endTime = starTime - System.currentTimeMillis()
                        } catch (e: Exception) {
                            Log.e(BaseStatusBarActivity.TAG, e.message + itemInfoGson!!.item_addtime)
                        }

                        dd_money!!.text = "订单金额：￥" + itemInfoGson!!.item_money
                        if (itemInfoGson!!.item_class1id == "6") {
                            dd_type!!.text = "订单类型：计件"
                            TYPE = 2
                            OkhttpTool.getOkhttpTool().post(UrlConfig.GAOJIAN_LIST_JJ, map, object : okhttp3.Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    try {
                                        val s = response.body()!!.string()
                                        LogUtils.d("==============response:$s")
                                        jiJianFangAnGson = Gson().fromJson(s, JiJianFangAnGson::class.java)
                                        hd.sendEmptyMessage(GET_JJ_MESSAGE)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            })
                        } else {
                            if (Integer.valueOf(itemInfoGson!!.item_type) == 6) {
                                dd_type!!.text = "订单类型：雇佣"
                                TYPE = 0
                                OkhttpTool.getOkhttpTool().post(UrlConfig.GAOJIAN_LIST, map, object : okhttp3.Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        try {
                                            val s = response.body()!!.string()
                                            LogUtils.d("==============response:$s")
                                            xuanShangFangAnGson = Gson().fromJson(s, XuanShangFangAnGson::class.java)
                                            hd.sendEmptyMessage(GET_XS_MESSAGE)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                })


                            } else if (Integer.valueOf(itemInfoGson!!.item_type) == 1) {
                                dd_type!!.text = "订单类型：招标"
                                TYPE = 1
                                OkhttpTool.getOkhttpTool().post(UrlConfig.BAOJIA_LIST, map, object : okhttp3.Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        try {
                                            val s = response.body()!!.string()
                                            LogUtils.d("==============response:$s")
                                            zhaoBiaoFangAnGson = Gson().fromJson(s, ZhaoBiaoFangAnGson::class.java)
                                            hd.sendEmptyMessage(GET_ZB_MESSAGE)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }


                                    }
                                })
                                if (itemInfoGson!!.item_zab_yusuan1 == itemInfoGson!!.item_zab_yusuan2) {
                                    dd_money!!.text = "订单金额：￥" + itemInfoGson!!.item_zab_yusuan1
                                } else {
                                    dd_money!!.text = "订单金额：￥" + itemInfoGson!!.item_zab_yusuan1 + "-￥" + itemInfoGson!!.item_zab_yusuan2
                                }
                            } else if (Integer.valueOf(itemInfoGson!!.item_type) == 0) {
                                TYPE = 0
                                dd_type!!.text = "订单类型：悬赏"
                                OkhttpTool.getOkhttpTool().post(UrlConfig.GAOJIAN_LIST, map, object : okhttp3.Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        try {
                                            val s = response.body()!!.string()
                                            LogUtils.d("==============response:$s")
                                            xuanShangFangAnGson = Gson().fromJson(s, XuanShangFangAnGson::class.java)
                                            hd.sendEmptyMessage(GET_XS_MESSAGE)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }


                                    }
                                })

                            } else {
                                TYPE = 2
                                dd_type!!.text = "订单类型：计件"
                            }
                        }
                        dd_number!!.text = "项目编号：" + itemInfoGson!!.item_id
                        dd_title!!.text = itemInfoGson!!.item_name
                        //                    new Thread(new Runnable() {
                        //
                        //                        @Override
                        //                        public void run() {
                        //
                        //                        }
                        //                    }).start();

                        val lMsg = Message.obtain()
                        val s = itemInfoGson!!.item_con.replace("font-size".toRegex(), "f").replace("line-height".toRegex(), "l")
                        imageGetter = LhtTool.MyImageGetter(resources)
                        val test = Html.fromHtml(s + "<br>" + itemInfoGson!!.item_fujian +
                                "</br><br>" + itemInfoGson!!.item_othercon + "</br>", imageGetter, null)
                        lMsg.what = GET_HTML
                        lMsg.obj = test
                        this@MHandler.sendMessage(lMsg)

                        type = TYPE
                        adapter = ItemAdapter(TYPE, this, list, id, itemInfoGson!!.item_userid)
                        xRecyclerView!!.adapter = adapter!!
                        val endtime = Date(itemInfoGson!!.item_endtime).time - Date(SimpleDateFormat(
                                "yyyy/MM/dd HH:mm:ss").format(Date())).time
                        if (Integer.valueOf(itemInfoGson!!.item_check) == 0) {
                            dd_ddzt!!.text = "订单状态：待审核"
                            if (Integer.valueOf(itemInfoGson!!.item_ispay) == 0 && Integer.valueOf(itemInfoGson!!.zhaobiao_pay) == 0) {
                                dd_delete!!.visibility = View.VISIBLE
                                if (Integer.valueOf(itemInfoGson!!.state_z) == 687) {
                                    dd_update!!.visibility = View.VISIBLE
                                } else {
                                    dd_update!!.visibility = View.GONE
                                }
                            } else {
                                dd_delete!!.visibility = View.GONE
                                dd_update!!.visibility = View.GONE
                            }
                        } else if (Integer.valueOf(itemInfoGson!!.item_check) == 1) {
                            dd_ddzt!!.text = "订单状态：审核未通过"
                        } else {
                            if (Integer.valueOf(itemInfoGson!!.item_jindu) == 10) {
                                dd_ddzt!!.text = "订单状态：该项目已流标"
                            } else {
                                if (Integer.valueOf(itemInfoGson!!.item_state) > 0) {
                                    dd_ddzt!!.text = "订单状态：已圆满结束"
                                } else {
                                    if (endtime < 0) {
                                        dd_ddzt!!.text = "订单状态：已到期"
                                    }
                                }
                            }
                        }
                        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        params.bottomMargin = 0
                        if (Integer.valueOf(itemInfoGson!!.item_ispay) == 1) {
                            dd_istg!!.visibility = View.VISIBLE
                            if (Integer.valueOf(itemInfoGson!!.item_type) == 1) {
                                dd_footer!!.visibility = View.GONE
                                scrollView!!.layoutParams = params

                            } else {
                                val endt = Date(itemInfoGson!!.item_endtime).time - Date(SimpleDateFormat(
                                        "yyyy/MM/dd HH:mm:ss").format(Date())).time
                                if (endt < -(30.toLong() * 24 * 60 * 60 * 1000)) {
                                    dd_footer!!.visibility = View.GONE
                                    scrollView!!.layoutParams = params

                                } else {
                                    mControl!!.text = "增加悬赏"
                                    mControl!!.setOnClickListener {
                                        val intent = Intent(this, PriceMakUpActivity::class.java)
                                        intent.putExtra("ID", itemInfoGson!!.item_id)
                                        startActivityForResult(intent, 1)
                                    }
                                }
                            }
                        } else {
                            dd_istg!!.visibility = View.GONE
                            if (Integer.valueOf(itemInfoGson!!.item_type) == 1) {
                                mControl!!.text = "托管定金"
                                if (Integer.valueOf(itemInfoGson!!.item_money) > 0) {

                                    mControl!!.setOnClickListener {
                                        val intent = Intent(this, FuKuanActivity::class.java)
                                        intent.putExtra("ID", itemInfoGson!!.item_id)
                                        startActivityForResult(intent, 1)
                                    }
                                } else {
                                    dd_footer!!.visibility = View.GONE
                                    scrollView!!.layoutParams = params

                                }

                            } else {
                                mControl!!.setOnClickListener {
                                    val intent = Intent(this, FuKuanActivity::class.java)
                                    intent.putExtra("ID", itemInfoGson!!.item_id)
                                    startActivityForResult(intent, 1)
                                }
                            }
                        }
                    }
                } else if (msg?.what == NETWORKEXCEPTION) {

                    ld?.dismiss()

                    Toast.makeText(this, "网络异常，请重试", Toast.LENGTH_SHORT).show()
                    //                OkhttpTool.Companion.getOkhttpTool().cancelRequest();
                    finish()

                } else if (msg?.what == GET_HTML) {
                    if (ld != null) {
                        ld!!.dismiss()
                    }
                    dd_content!!.text = msg.obj as CharSequence
                } else if (msg?.what == GET_XS_MESSAGE) {

                    list.clear()
                    if (null != xuanShangFangAnGson) {
                        list.addAll(xuanShangFangAnGson!!.list)
                    }
                    if (list.size > 0) {
                        allCase!!.visibility = View.VISIBLE
                    }
                    adapter!!.notifyDataSetChanged()
                    if (ld != null) {
                        ld!!.dismiss()
                    }
                } else if (msg?.what == GET_ZB_MESSAGE) {

                    list.clear()
                    if (null != zhaoBiaoFangAnGson) {
                        list.addAll(zhaoBiaoFangAnGson!!.list)
                    }
                    if (list.size > 0) {
                        allCase!!.visibility = View.VISIBLE
                    }
                    adapter!!.notifyDataSetChanged()
                    if (ld != null) {
                        ld!!.dismiss()
                    }

                } else if (msg?.what == GET_JJ_MESSAGE) {

                    list.clear()
                    if (null != jiJianFangAnGson) {
                        list.addAll(jiJianFangAnGson!!.list)
                    }
                    if (list.size > 0) {
                        allCase!!.visibility = View.VISIBLE

                    }
                    adapter!!.notifyDataSetChanged()
                    if (ld != null) {
                        ld!!.dismiss()
                    }

                } else if (msg?.what == DELETE_PRO) {

                    val s = msg?.data.getString("s")
                    when (s) {
                        "unlogin" -> CustomToast.showToast(this, "请登录", Toast.LENGTH_LONG)
                        "noitem" -> CustomToast.showToast(this, "删除失败", Toast.LENGTH_LONG)
                        "fail" -> CustomToast.showToast(this, "删除失败", Toast.LENGTH_LONG)
                        "ok" -> {
                            CustomToast.showToast(this, "刪除成功", Toast.LENGTH_LONG)
                            finish()
                        }
                    }

                } else if (msg?.what == TimeOut.LOADTIMEOUT) {
                    TimeOut.timeCancel()

                    ld?.dismiss()

                }

            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_detail_activity)

        title.setText(R.string.ddxq)
        ld = LoadingDialog(this).setMessage("加载中....")
        ld!!.show()
        TimeOut.webTimeOut(hd)
        scrollView = findViewById(R.id.scrollView)
        val bundle = intent.extras
        id = bundle!!.getString("ID")
        dd_ddzt = findViewById(R.id.dd_ddtz)
        dd_money = findViewById(R.id.dd_ddje)
        dd_type = findViewById(R.id.dd_type)
        dd_number = findViewById(R.id.dd_bh)
        dd_title = findViewById(R.id.dd_title)
        dd_fabu_time = findViewById(R.id.timeStar1)
        dd_content = findViewById(R.id.dd_content)
        dd_content!!.setTextIsSelectable(true)
        dd_delete = findViewById(R.id.delete)
        dd_update = findViewById(R.id.update)
        dd_istg = findViewById(R.id.istg)
        allCase = findViewById(R.id.allCase)
        xRecyclerView = findViewById(R.id.xRecyclerView)
        xRecyclerView!!.layoutManager = LinearLayoutManager(this)
        dd_state = findViewById(R.id.state)
        dd_footer = findViewById(R.id.footer)
        mControl = findViewById(R.id.control)
        val ts = findViewById<TextView>(R.id.ts)
        cynum = findViewById(R.id.cynum)
        xRecyclerView!!.setLoadingMoreEnabled(false)
        xRecyclerView!!.setPullRefreshEnabled(false)
        if (LhtTool.isLogin) {
            map["itemId"] = id!!
            map["loginUserId"] = MyApplication.userInfo!!.userID
            map["userid"] = MyApplication.userInfo!!.userID
            map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
            map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
            //            map.put("debug","1");
            OkhttpTool.getOkhttpTool().post(UrlConfig.ITEM_INFO, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)

                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val s = response.body()!!.string()
                        LogUtils.d(" ==============response:$s")
                        LogUtils.e("dingdanxiang", s)
                        itemInfoGson = Gson().fromJson(s, ItemInfoGson::class.java)
                        hd.sendEmptyMessage(GET_MESSAGE)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            })
        }
        map["num"] = "1"
        map["pages"] = "1"
        map["seltype"] = "all"
        dd_state!!.setOnClickListener {
            //            Intent intent = new Intent(OrderDetailsActivity.this, TaskStateActivity.class);
            //            intent.putExtra("ID", id);
            //            startActivity(intent);
        }
        dd_update!!.setOnClickListener {
            if (Integer.valueOf(itemInfoGson!!.item_type) == 6) {
                if (Integer.valueOf(itemInfoGson!!.slide_id) > 0) {
                    val intent = Intent(this@OrderDetailsActivity, BuyServiceActivity::class.java)
                    intent.putExtra("ID", itemInfoGson!!.slide_id)
                    intent.putExtra("ITEMID", itemInfoGson!!.item_id)
                    startActivityForResult(intent, 1)
                } else {
                    val intent = Intent(this@OrderDetailsActivity, ImmediateGyActivity::class.java)
                    intent.putExtra("ID", itemInfoGson!!.item_toupiaouserid)
                    intent.putExtra("itemId", itemInfoGson!!.item_id)
                    startActivityForResult(intent, 1)
                }
            } else {
                val intent = Intent(this@OrderDetailsActivity, FaBuActicity::class.java)
                intent.putExtra("ID", itemInfoGson!!.item_class1id)
                intent.putExtra("ItemID", itemInfoGson!!.item_id)
                startActivityForResult(intent, 1)
            }
        }
        dd_delete!!.setOnClickListener {
            val builder = AlertDialog.Builder(this@OrderDetailsActivity).setTitle("提示信息")
                    .setMessage("您是否确定删除该订单？").setCancelable(false)
                    .setNeutralButton("确定") { dialog, which ->
                        OkhttpTool.getOkhttpTool().post(UrlConfig.DELETE_PROJECT, map, object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                            }

                            override fun onResponse(call: Call, response: Response) {
                                try {
                                    val s = response.body()!!.string()
                                    LogUtils.d("===============response:$s")
                                    val ms = Message()
                                    ms.what = DELETE_PRO
                                    val b = Bundle()
                                    b.putString("s", s)
                                    ms.data = b
                                    hd.sendMessage(ms)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                        })
                    }.setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
        allCase!!.setOnClickListener {
            val intent = Intent(this@OrderDetailsActivity, PlanListActivity::class.java)
            intent.putExtra("ID", id)
            intent.putExtra("TYPE", type)
            intent.putExtra("GZID", itemInfoGson!!.item_userid)
            startActivityForResult(intent, 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        OkhttpTool.getOkhttpTool().post(UrlConfig.ITEM_INFO, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)

            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d(" ==============response:$s")
                    itemInfoGson = Gson().fromJson(s, ItemInfoGson::class.java)
                    hd.sendEmptyMessage(GET_MESSAGE)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        })
        super.onActivityResult(requestCode, resultCode, data)
    }

}
