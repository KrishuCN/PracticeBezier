package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.View
import android.widget.*
import chen.vike.c680.adapter.ItemAdapter
import chen.vike.c680.bean.ItemInfoGson
import chen.vike.c680.bean.JiJianFangAnGson
import chen.vike.c680.bean.XuanShangFangAnGson
import chen.vike.c680.bean.ZhaoBiaoFangAnGson
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.*
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.util.Util
import com.google.gson.Gson
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.lht.vike.a680_v1.R
import kotterknife.bindView
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
 *
 * 任务详情
 */

class TaskDetailsActivity : BaseStatusBarActivity() {

    val textView25: TextView by bindView(R.id.textView25)
    val ztl: TextView by bindView(R.id.ztl)
    val zt1: TextView by bindView(R.id.zt1)
    val endtime: CustomDigitalClockEnd by bindView(R.id.endtime)
    val imageView8: ImageView by bindView(R.id.imageView8)
    val zt2: TextView by bindView(R.id.zt2)
    val jidu: FrameLayout by bindView(R.id.jidu)
    val icon: CircleImageView by bindView(R.id.icon)
    val publisher: TextView by bindView(R.id.publisher)
    val iscnxb: ImageView by bindView(R.id.iscnxb)
    val rwxqBt: TextView by bindView(R.id.rwxq_bt)
    val nowMoney: TextView by bindView(R.id.now_money)
    val istg: ImageView by bindView(R.id.istg)
    val isjj: ImageView by bindView(R.id.isjj)
    val isvip: TextView by bindView(R.id.isvip)
    val renwuFenxiang: ImageView by bindView(R.id.renwu_fenxiang)
    val jjFpfs: TextView by bindView(R.id.jj_fpfs)
    val type: TextView by bindView(R.id.type)
    val rwxqTbrs: TextView by bindView(R.id.rwxq_tbrs)
    val starTime: CustomDigitalClockStar by bindView(R.id.starTime)
    val ts: TextView by bindView(R.id.ts)
    val rwxqInfo: TextView by bindView(R.id.rwxq_info)
    val rwxqFujian: TextView by bindView(R.id.rwxq_fujian)
    val rwxqInfoFujian: TextView by bindView(R.id.rwxq_info_fujian)
    val rwxqBcsm: TextView by bindView(R.id.rwxq_bcsm)
    val rwxqInfoBcsm: TextView by bindView(R.id.rwxq_info_bcsm)
    val syfaNum: TextView by bindView(R.id.syfa_num)
    val xRecyclerView: XRecyclerView by bindView(R.id.xRecyclerView)
    val xsXqSyfa: LinearLayout by bindView(R.id.xs_xq_syfa)
    val control: Button by bindView(R.id.control)

    private val map = HashMap<String, Any>()
    private val list = ArrayList<Any>()
    private var id: String? = null
    private var TYPE = 100
    private var adapter: ItemAdapter? = null
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    private var imageGetter: Html.ImageGetter? = null
    private var ld: LoadingDialog? = null
    private var itemInfoGson: ItemInfoGson? = null
    private var xuanShangFangAnGson: XuanShangFangAnGson? = null
    private var zhaoBiaoFangAnGson: ZhaoBiaoFangAnGson? = null
    private var jiJianFangAnGson: JiJianFangAnGson? = null
    private val GET_INFO = 0x123
    private val NETWORKEXCEPTION = 0x113
    private val XUANSHANG = 0x313
    private val ZHAOBIAO = 0x213
    private val JIJIAN = 0x132
    private val SHOUCANG = 0x432
    private val T_MESSAGE = 0x111
    private var mMsg: Message? = null
    private val mHandler = MHandler(this)

    
    private class MHandler(taskDetailsActivity: TaskDetailsActivity):Handler(){
        private val weakReference:WeakReference<TaskDetailsActivity> = WeakReference(taskDetailsActivity)
        private val ref = weakReference.get()
        @SuppressLint("SetTextI18n")
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            ref?.run {
                
                if (msg?.what == GET_INFO) {
                    if (Util.isOnMainThread()) {

                        //Buly这里偶尔会Glide报错(估计是崩溃后Activity重启造成的)：
                        //Java.lang.IllegalArgumentException
                        //You cannot start a load for a destroyed activity
                        //so 首先Handler写成弱引用并且在这里再次做判断 ╮(╯▽╰)╭

                        if (!this.isDestroyed && !this.isFinishing){
                            val options = RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                            Glide.with(this).load(itemInfoGson!!.gz_imageurl).apply(options).into(icon)
                        }
                    }
                    textView25.text = "项目编号：" + itemInfoGson!!.item_id
                    publisher.text = itemInfoGson!!.gz_username
                    rwxqBt.text = Html.fromHtml(itemInfoGson!!.item_name)
                    if (itemInfoGson!!.item_class1id == "6") {
                        TYPE = 2
                        adapter = ItemAdapter(TYPE, this, list, id, itemInfoGson!!.item_userid)
                        xRecyclerView.adapter = adapter!!
                        OkhttpTool.getOkhttpTool().post(UrlConfig.GAOJIAN_LIST_JJ, map, object : okhttp3.Callback {
                            override fun onFailure(call: Call, e: IOException) {

                                LhtTool.sendMessage(msg.target, e, NETWORKEXCEPTION)

                            }


                            override fun onResponse(call: Call, response: Response) {

                                val s = response.body()!!.string()
                                LogUtils.d("==============response:$s")
                                jiJianFangAnGson = try {
                                    Gson().fromJson(s, JiJianFangAnGson::class.java)
                                }catch (e:Exception){
                                    e.printStackTrace()
                                    null
                                }

                            }
                        })
                        type.setText(R.string.rwxq_jj)
                        nowMoney.text = Html.fromHtml("￥" + itemInfoGson!!.item_money)
                        jjFpfs.visibility = View.VISIBLE
                        val s = itemInfoGson!!.item_zb_fenpei.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        try {
                            jjFpfs.text = Html.fromHtml("雇主需要<font color='#df231b'>" + s[0] + "</font>个稿件，每个" + s[1] + "元，还需要" +
                                    (Integer.valueOf(s[0].trim { it <= ' ' }) - Integer.valueOf(itemInfoGson!!.item_zb_ok))).toString() + "个稿件"

                        }catch (e:NumberFormatException){
                            e.printStackTrace()
                        }
                    } else {
                        when {
                            itemInfoGson!!.item_type == "1" -> {
                                control.text = "参与报价"
                                TYPE = 1
                                type.setText(R.string.rwxq_zb)
                                if (itemInfoGson!!.item_zab_yusuan1 == itemInfoGson!!.item_zab_yusuan2) {
                                    nowMoney.text = Html.fromHtml("￥" + itemInfoGson!!.item_zab_yusuan1)
                                } else {
                                    nowMoney.text = Html.fromHtml("￥" + itemInfoGson!!.item_zab_yusuan1 + "—￥" + itemInfoGson!!.item_zab_yusuan2)
                                }
                                adapter = ItemAdapter(TYPE, this, list, id, itemInfoGson!!.item_userid)
                                xRecyclerView.adapter = adapter!!
                                OkhttpTool.getOkhttpTool().post(UrlConfig.BAOJIA_LIST, map, object : okhttp3.Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        LhtTool.sendMessage(msg.target, e, NETWORKEXCEPTION)
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        try {
                                            val s = response.body()!!.string()
                                            LogUtils.d("==================response:$s")
                                            zhaoBiaoFangAnGson = Gson().fromJson(s, ZhaoBiaoFangAnGson::class.java)
                                            msg.target?.sendEmptyMessage(ZHAOBIAO)

                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }


                                    }
                                })
                            }
                            itemInfoGson!!.item_type == "6" -> {
                                TYPE = 0
                                type.setText(R.string.rwxq_gy)
                                nowMoney.text = Html.fromHtml("￥" + itemInfoGson!!.item_money)
                                adapter = ItemAdapter(TYPE, this, list, id, itemInfoGson!!.item_userid)
                                xRecyclerView.adapter = adapter!!
                                getXuanShangList()

                            }
                            else -> {
                                TYPE = 0
                                type.setText(R.string.rwxq_xs)
                                nowMoney.text = Html.fromHtml("￥" + itemInfoGson!!.item_money)
                                adapter = ItemAdapter(TYPE, this, list, id, itemInfoGson!!.item_userid)
                                xRecyclerView.adapter = adapter!!
                                getXuanShangList()

                            }
                        }
                    }
                    rwxqTbrs.text = itemInfoGson!!.item_tjnum + "人投标"
                    syfaNum.text = "所有方案（" + itemInfoGson!!.item_tjnum + "）"

                    val msg = Message.obtain()
                    val s = itemInfoGson!!.item_con.replace("font-size".toRegex(), "f").replace("line-height".toRegex(), "l")
                    imageGetter = LhtTool.MyImageGetter(resources)
                    val test = Html.fromHtml(s, imageGetter, null)
                    msg.what = T_MESSAGE
                    msg.obj = test

                    this@MHandler.sendMessage(msg)

                    if (itemInfoGson!!.item_isxuangao == "10") {
                        iscnxb.visibility = View.VISIBLE
                    } else {
                        iscnxb.visibility = View.GONE
                    }
                    if (itemInfoGson!!.item_type == "0" && itemInfoGson!!.item_isxuangao == "1") {
                        iscnxb.visibility = View.VISIBLE
                        iscnxb.setImageResource(R.mipmap.icon_79)

                    } else if (itemInfoGson!!.item_type == "0" && itemInfoGson!!.item_isxuangao == "0") {
                        iscnxb.visibility = View.GONE
                    } else {
                        iscnxb.visibility = View.GONE
                    }
                    if (itemInfoGson!!.item_ispay == "1") {
                        istg.visibility = View.VISIBLE

                    } else {
                        istg.visibility = View.GONE

                    }
                    if (Integer.valueOf(itemInfoGson!!.item_yanqi) == 1) {
                        isjj.visibility = View.GONE
                    } else if (Integer.valueOf(itemInfoGson!!.item_yanqi) == 2) {
                        isjj.visibility = View.VISIBLE
                    } else {
                        isjj.visibility = View.GONE
                    }
                    if (Integer.valueOf(itemInfoGson!!.item_isvipstore) == 1) {
                        isvip.visibility = View.VISIBLE
                        if (itemInfoGson!!.login_isvip == "1") {


                        } else {
                            ts.visibility = View.VISIBLE
                            ts.text = Html.fromHtml("您暂不能参与该项目；<br/>\n" +
                                    "该项目在<font color='#df231b'>" + itemInfoGson!!.item_toupiaotime + "</font>后才能参与！<br/>\n" +
                                    "签约VIP服务商现在可参与！")
                            control.isEnabled = false
                            control.setBackgroundColor(resources.getColor(R.color.text_color_7))
                            control.text = "暂不能参与"
                        }
                    } else {
                        isvip.visibility = View.GONE
                    }
                    val startime = Date(dateFormat.format(Date())).time - Date(itemInfoGson!!.item_fbtime).time
                    starTime.endTime = startime - System.currentTimeMillis()
                    val endtime1 = Date(itemInfoGson!!.item_endtime).time - Date(dateFormat.format(Date())).time
                    endtime.setEndTime(System.currentTimeMillis() + endtime1, Integer.valueOf(itemInfoGson!!.item_state))
                    if (Integer.valueOf(itemInfoGson!!.item_check) == 0) {
                        zt1.text = "审核中"
                    } else if (Integer.valueOf(itemInfoGson!!.item_check) == 1) {
                        zt1.text = "审核未通过"
                    } else {
                        if (Integer.valueOf(itemInfoGson!!.item_jindu) == 10) {
                            zt2.text = "已流标"
                            zt2.setTextColor(resources.getColor(R.color.text_color_10))
                            zt2.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.mipmap.zt1), null, null, null)
                            control.text = "已结束"
                            control.setBackgroundColor(resources.getColor(R.color.text_color_7))
                            control.isEnabled = false
                        } else {
                            if (Integer.valueOf(itemInfoGson!!.item_state) > 0) {
                                if (Integer.valueOf(itemInfoGson!!.zb_zhuankuan) == 0) {
                                    zt2.text = "项目交接中"
                                } else {
                                    zt2.text = "已圆满结束"
                                }
                                zt2.setTextColor(resources.getColor(R.color.text_color_10))
                                zt2.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.mipmap.zt1), null, null, null)
                                control.text = "查看中标方案"
                                control.setBackgroundColor(resources.getColor(R.color.text_color_10))
                                control.setOnClickListener {
                                    val intent = Intent(this, AllPlanActivity::class.java)
                                    intent.putExtra("ID", id)
                                    intent.putExtra("TYPE", TYPE)
                                    intent.putExtra("GZID", itemInfoGson!!.item_userid)
                                    startActivityForResult(intent, 1)
                                }
                            } else {
                                if (endtime1 < 0) {
                                    zt2.text = "已到期"
                                    zt2.setTextColor(resources.getColor(R.color.text_color_10))
                                    zt2.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.mipmap.zt1), null, null, null)
                                    control.text = "已结束"
                                    control.setBackgroundColor(resources.getColor(R.color.text_color_7))
                                    control.isEnabled = false
                                } else {


                                }
                            }
                        }
                    }
                    if (itemInfoGson!!.is_favitem == "1") {
                        img.setImageResource(R.mipmap.icon_115)
                    } else {
                        img.setImageResource(R.mipmap.icon_115)
                        img.setOnClickListener {
                            if (LhtTool.isLogin) {
                                map["ProId"] = id!!
                                TYPE = 101
                                OkhttpTool.getOkhttpTool().post(UrlConfig.COLLECTION, map, object : okhttp3.Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        LhtTool.sendMessage(mMsg?.target, e, NETWORKEXCEPTION)
                                    }

                                    @Throws(IOException::class)
                                    override fun onResponse(call: Call, response: Response) {

                                        val s = response.body()!!.string()
                                        LogUtils.d("================response:$s")
                                        val ms = Message()
                                        ms.what = SHOUCANG
                                        val b = Bundle()
                                        b.putString("s", s)
                                        ms.data = b
                                        mMsg?.target?.sendMessage(ms)

                                    }
                                })
                            } else {
                                val intent = Intent(this, UserLoginActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }


                } else if (msg?.what == NETWORKEXCEPTION) {

                    Toast.makeText(this,"网络异常，请重试",Toast.LENGTH_SHORT).show()
                    LhtTool.showNetworkException(this, msg)
                    this.finish()

                } else if (msg?.what == XUANSHANG) {
                    list.clear()
                    if (null != xuanShangFangAnGson) {
                        list.addAll(xuanShangFangAnGson!!.list)
                    }
                    if (list.size > 0) {
                        xsXqSyfa.visibility = View.VISIBLE
                        adapter!!.notifyDataSetChanged()
                    }
                } else if (msg?.what == ZHAOBIAO) {
                    list.clear()
                    if (null != zhaoBiaoFangAnGson) {
                        list.addAll(zhaoBiaoFangAnGson!!.list)
                    }
                    if (list.size > 0) {
                        xsXqSyfa.visibility = View.VISIBLE
                        adapter!!.notifyDataSetChanged()
                    }
                } else if (msg?.what == JIJIAN) {
                    list.clear()
                    if (null != jiJianFangAnGson) {
                        list.addAll(jiJianFangAnGson!!.list)
                    }
                    if (list.size > 0) {
                        xsXqSyfa.visibility = View.VISIBLE
                        adapter!!.notifyDataSetChanged()
                    }
                } else if (msg?.what == SHOUCANG) {

                    val s = msg.data.getString("s")
                    when (s) {
                        "unlogin" -> CustomToast.showToast(this, "未登录", Toast.LENGTH_LONG)
                        "noitem" -> CustomToast.showToast(this, "项目获取失败", Toast.LENGTH_LONG)
                        "1" -> {
                            CustomToast.showToast(this, "收藏成功", Toast.LENGTH_LONG)
                            img.setImageResource(R.mipmap.icon_115)
                        }
                        "0" -> CustomToast.showToast(this, "您已经收藏过了", Toast.LENGTH_LONG)
                    }

                } else if (msg?.what == T_MESSAGE) {
                    rwxqInfo.text = msg.obj as CharSequence
                    if (itemInfoGson!!.item_fujian.isEmpty()) {
                        rwxqInfoFujian.visibility = View.GONE
                        rwxqFujian.visibility = View.GONE
                    } else {
                        rwxqInfoFujian.text = Html.fromHtml(itemInfoGson!!.item_fujian)
                    }
                    if (itemInfoGson!!.item_othercon.isEmpty()) {
                        rwxqInfoBcsm.visibility = View.GONE
                        rwxqBcsm.visibility = View.GONE

                    } else {
                        rwxqInfoBcsm.text = Html.fromHtml(itemInfoGson!!.item_othercon.replace("font-size".toRegex(), "f").replace("line-height".toRegex(), "l").replace("<br>".toRegex(), ""))
                    }
                }
                ld?.dismiss()
            }
        }
    }
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_details_activity)

        title.text = "任务详情"
        ld = LoadingDialog(this).setMessage("加载中....")
        ld!!.show()
        control.setOnClickListener {
            if (LhtTool.isLogin) {
                var intent: Intent? = null
                if (TYPE == 1) {
                    intent = Intent(this@TaskDetailsActivity, Cy_ZbActivity::class.java)
                } else {
                    intent = Intent(this@TaskDetailsActivity, Cy_XsActivity::class.java)
                    intent.putExtra("TYPE", TYPE)
                }
                intent.putExtra("ID", id)
                startActivityForResult(intent, 1)
            } else {
                val intent = Intent(this@TaskDetailsActivity, UserLoginActivity::class.java)
                startActivity(intent)
            }
        }
        xRecyclerView.layoutManager = LinearLayoutManager(this)
        xRecyclerView.setLoadingMoreEnabled(false)
        xRecyclerView.setPullRefreshEnabled(false)
        xsXqSyfa.visibility = View.GONE
        xsXqSyfa.setOnClickListener {
            val intent = Intent(this@TaskDetailsActivity, AllPlanActivity::class.java)
            intent.putExtra("ID", id)
            intent.putExtra("TYPE", TYPE)
            intent.putExtra("GZID", itemInfoGson!!.item_userid)
            startActivityForResult(intent, 1)
        }
        id = intent.getStringExtra("ID")
        if (id != null) {
            map["itemId"] = id!!
            map["hits"] = 1
            if (LhtTool.isLogin) {
                map["loginUserId"] = MyApplication.userInfo!!.userID
                map["userid"] = MyApplication.userInfo!!.userID
                map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
                map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
            }
            /**
             * userid 用户ID ( 用于判断当前登录账号与该项目发布人权限问题)
             * vkuserip 登录cookieIPT
             * vktoken 登录加密串
             * itemid 项目编号ID
             * itemtype 项目类型：=canyuxs 表示参与悬赏项目提交稿件；
             * =canyujj 表示参与项目所属大分类为6的营销推广计件项目提交稿件
             * num 每页显示多少条记录
             * pages 当前页码(请求第几页)
             * seltype 查询条件：情况一、如果itemtype=canyujj时，
             * 那么seltype =all 表示所有，=un 未评标的，=yes合格的，=no不合格的 ；
             * 情况二、如果itemtype=canyuxs时，那么seltype =all
             * 表示所有，unread 未查看的方案，=bak 备选的方案，=yes 中标的方案
             */
            map["num"] = "1"
            map["pages"] = "1"
            map["seltype"] = "all"
            getItemInfo()


        }

        renwuFenxiang.setOnClickListener {
            //分享
            if (LhtTool.isLogin) {
                LhtTool.showShare(this@TaskDetailsActivity, itemInfoGson?.item_name,
                        "", itemInfoGson?.item_name, itemInfoGson?.item_url, "")
            } else {
                LhtTool.showShare(this@TaskDetailsActivity, itemInfoGson?.item_name, "", "", "", "")
            }
        }

    }

    /**
     * 请求任务详情数据
     */
    private fun getItemInfo() {
        OkhttpTool.getOkhttpTool().post(UrlConfig.ITEM_INFO, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                LhtTool.sendMessage(mHandler, e, NETWORKEXCEPTION)

            }

            override fun onResponse(call: Call, response: Response) {
                try {

                    val s = response.body()!!.string()
                    itemInfoGson = Gson().fromJson(s, ItemInfoGson::class.java)
                    mHandler.sendEmptyMessage(GET_INFO)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }


    private fun getXuanShangList() {
        OkhttpTool.getOkhttpTool().post(UrlConfig.GAOJIAN_LIST, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(mHandler, e, NETWORKEXCEPTION)
            }


            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    xuanShangFangAnGson = Gson().fromJson(s, XuanShangFangAnGson::class.java)
                    mHandler.sendEmptyMessage(XUANSHANG)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            TYPE = 100
            getItemInfo()
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onPause() {
        super.onPause()
    }


    override fun onStop() {
        super.onStop()
        endtime.destory()
        starTime.destory()
        Glide.with(this).pauseRequests()
    }

    override fun onDestroy() {
        super.onDestroy()
//        OkhttpTool.getOkhttpTool().cancelRequest()
    }
}
