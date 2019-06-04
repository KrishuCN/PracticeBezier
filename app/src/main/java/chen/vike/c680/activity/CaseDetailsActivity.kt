package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jcodecraeer.xrecyclerview.XRecyclerView
import chen.vike.c680.adapter.CaseDetailsRVAdapter
import chen.vike.c680.bean.CaseDetailGson
import chen.vike.c680.bean.ShopInfoGson
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.ThreadPoolManager
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import com.blankj.utilcode.util.ToastUtils
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
 * 案例详情
 *
 */

class CaseDetailsActivity : BaseStatusBarActivity() {

    private val map = HashMap<String, Any>()
    private var button: TextView? = null
    private val strings = ArrayList<String>()
    private var caseDetailGson: CaseDetailGson? = null
    private var mHeader: TextView? = null
    private var shopInfoGson: ShopInfoGson? = null
    private var imageGetter: Html.ImageGetter? = null
    private var mFooter: TextView? = null
    private var ld: LoadingDialog? = null
    private val ANLI_DETAILS = 0x123
    private val T_MESSAGE = 0x223
    private val NETWORKEXCEPTION = 0x122
    private var anli_cjl: TextView? = null
    private var anli_addres: TextView? = null
    private var dianpu: View? = null
    private var shop_xq_lx: LinearLayout? = null
    private var shop_shou_dian: LinearLayout? = null
    private var bundle: Bundle? = null
    private var adapter: CaseDetailsRVAdapter? = null
    private var mHandler: MHandler = MHandler(this)
    /**
     * 弹窗进度显示线程
     */
    private val t: Thread = Thread(object : Runnable {
        val msg = Message.obtain()
        override fun run() {
            imageGetter = LhtTool.MyImageGetter(resources)
            val test = Html.fromHtml(caseDetailGson!!.con, imageGetter, null)
            msg.what = T_MESSAGE
            msg.obj = test
            mHandler.sendMessage(msg)
        }
    })

    private class MHandler(activity: CaseDetailsActivity) : Handler() {
        private val weakReference: WeakReference<CaseDetailsActivity> = WeakReference(activity)
        private var caseDetails = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            caseDetails?.run {
                if (msg?.what == ANLI_DETAILS) {

                    if (null != caseDetailGson) {
                        if (caseDetailGson!!.zuopinimglist != null) {
                            val s = caseDetailGson!!.zuopinimglist.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            Collections.addAll(strings, *s)
                        } else {
                            strings.add(caseDetailGson!!.zuopinimg)
                        }
                        adapter?.notifyDataSetChanged()
                        mHeader!!.text = caseDetailGson!!.zuopinname
                        ThreadPoolManager.instance.addTask(t)

                        ld = LoadingDialog(this).setMessage("加载中......")
                        ld!!.show()
                    } else {
                        ToastUtils.showShort("网络错误，请重试")
                        finish()
                    }

                } else if (msg?.what == NETWORKEXCEPTION) {

                    LhtTool.showNetworkException(this, msg)

                } else if (msg?.what == T_MESSAGE) {
                    mFooter!!.text = msg.obj as CharSequence
                    ld?.dismiss()
                } else if (msg?.what == 0) {
                    Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show()
                } else if (msg?.what == 1) {
                    Toast.makeText(this, "未登录", Toast.LENGTH_SHORT).show()
                } else if (msg?.what == 2) {
                    Toast.makeText(this, "已经收藏过了", Toast.LENGTH_SHORT).show()
                } else {
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anli_activity)

        title.text = "案例详情"
        val mShopName = findViewById<TextView>(R.id.anli_name)
        val mLogo = findViewById<ImageView>(R.id.anli_logo)
        anli_cjl = findViewById(R.id.anli_cjjl)
        anli_addres = findViewById(R.id.anli_dizhi)
        dianpu = findViewById(R.id.anli)
        button = findViewById(R.id.anli_xq_lxt)
        shop_shou_dian = findViewById(R.id.anli_xq_shou)
        shop_xq_lx = findViewById(R.id.anli_xq_lx)
        val xRecyclerView = findViewById<XRecyclerView>(R.id.xRecyclerView)
        adapter = CaseDetailsRVAdapter(this, strings)
        xRecyclerView.layoutManager = LinearLayoutManager(this)
        xRecyclerView.adapter = adapter

        //加载更多被禁后内容显示不出来，所以不能禁
        xRecyclerView.setLoadingMoreEnabled(false)
        xRecyclerView.setPullRefreshEnabled(false)
        val view = LayoutInflater.from(this).inflate(R.layout.view_header, null)
        val view1 = LayoutInflater.from(this).inflate(R.layout.view_footer, null)
        view.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view1.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mHeader = view.findViewById(R.id.textView26)
        mHeader!!.gravity = Gravity.CENTER
        mHeader!!.textSize = 18f
        mFooter = view1.findViewById(R.id.textView_foot)
        xRecyclerView.addHeaderView(view)
        xRecyclerView.setFootView(view1)
        bundle = intent.extras
        if (bundle != null) {
            shopInfoGson = bundle!!.getSerializable("BEAN") as ShopInfoGson
            val id = bundle!!.getString("ID")
            map["zuopinid"] = id!!

            OkhttpTool.getOkhttpTool().post(UrlConfig.SHOP_ZUOPIN_INFO, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    LhtTool.sendMessage(mHandler, e, NETWORKEXCEPTION)

                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val s = response.body()!!.string()
                        LogUtils.d("===============response:$s")
                        caseDetailGson = Gson().fromJson(s, CaseDetailGson::class.java)
                        mHandler.sendEmptyMessage(ANLI_DETAILS)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            })
            Glide.with(this).load(shopInfoGson!!.imgurl + "?123").into(mLogo)
            //            LhtTool.jifen(Integer.valueOf(shopInfoGson.getGrade()), mJiFen);
            //            LhtTool.isVip(Integer.valueOf(shopInfoGson.getCheck()), mVip);
            anli_cjl!!.text = "成交量：" + shopInfoGson!!.cj_month3_num + "   " + "好评率：" + shopInfoGson!!.allpjper + "%"
            mShopName.text = shopInfoGson!!.shopname
            viewListener()
        }


    }

    private fun viewListener() {
        dianpu!!.setOnClickListener { finish() }
        bottomListener()
    }

    private fun bottomListener() {
        shop_xq_lx!!.setOnClickListener {
            //开始和XX聊天
            if (LhtTool.isLogin) {
                if (MyApplication.userInfo!!.userID == shopInfoGson!!.userid) {
                    CustomToast.showToast(this@CaseDetailsActivity, "不能和自己聊天", Toast.LENGTH_SHORT)
                } else {
                    //跳到聊天界面
                    val intent = Intent(this@CaseDetailsActivity, MessageChatActivity::class.java)
                    intent.putExtra("ID", shopInfoGson!!.userid)
                    intent.putExtra("name", shopInfoGson!!.username)
                    startActivity(intent)
                }
            } else {
                CustomToast.showToast(this@CaseDetailsActivity, "亲，你还没有登录哦~", Toast.LENGTH_SHORT)
                val intent = Intent(this@CaseDetailsActivity, UserLoginActivity::class.java)
                startActivity(intent)
            }
        }
        shop_shou_dian!!.setOnClickListener {
            if (LhtTool.isLogin) {
                shouDianHttpData()
            } else {
                CustomToast.showToast(this@CaseDetailsActivity, "请先登录", Toast.LENGTH_SHORT)
                val intent = Intent(this@CaseDetailsActivity, UserLoginActivity::class.java)
                startActivity(intent)
            }
        }
        button!!.setOnClickListener {
            //立即雇佣
            if (LhtTool.isLogin) {
                if (MyApplication.userInfo!!.userID == shopInfoGson!!.userid) {
                    CustomToast.showToast(this@CaseDetailsActivity, "不能雇佣自己", Toast.LENGTH_SHORT)
                } else {
                    val intent = Intent(this@CaseDetailsActivity, ImmediateGyActivity::class.java)
                    intent.putExtra("ID", shopInfoGson!!.userid)
                    startActivity(intent)
                }
            } else {
                CustomToast.showToast(this@CaseDetailsActivity, "请先登录", Toast.LENGTH_SHORT)
                val intent = Intent(this@CaseDetailsActivity, UserLoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun shouDianHttpData() {
        val map = HashMap<String, Any>()
        map["login_userid"] = MyApplication.userInfo!!.userID
        map["shop_userid"] = bundle!!.getString("ID")!!
        Log.e("map", MyApplication.userInfo!!.userID + "----" + bundle!!.getString("ID"))
        OkhttpTool.getOkhttpTool().post(UrlConfig.DIAN_URL, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
                Log.e("dianpu", s)
                when (s) {
                    "unlogin" -> mHandler.sendEmptyMessage(1)
                    "ok" -> mHandler.sendEmptyMessage(0)
                }

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ld?.dismiss()
        mHandler.removeCallbacksAndMessages(null)
    }
}
