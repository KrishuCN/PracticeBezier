package chen.vike.c680.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import chen.vike.c680.adapter.FaBuTaoAdapter
import chen.vike.c680.adapter.KuaiSuAdapter
import chen.vike.c680.bean.IsErrorBean
import chen.vike.c680.bean.KuaiSuBean
import chen.vike.c680.bean.TaoCanListBean
import chen.vike.c680.tools.DataStatisticsBean
import chen.vike.c680.tools.DataTools
import chen.vike.c680.Interface.ViewItemClick
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.google.gson.Gson
import chen.vike.c680.bean.UserInfoBean
import chen.vike.c680.bean.GridViewInfoBean
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MainActivity
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.views.MyGridView
import com.lht.vike.a680_v1.R
import com.lht.vike.a680_v1.R.id.fbxq_phone
import kotterknife.bindView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2018/5/9.
 * 发布需求
 */

class FaBuDemandActivity : BaseStatusBarActivity(), View.OnClickListener {

    val fabuDemandContent: EditText by bindView(R.id.fabu_demand_content)
    val kuaisuXuanze: RelativeLayout by bindView(R.id.kuaisu_xuanze)
    val fabuLinear: LinearLayout by bindView(R.id.fabu_linear)
    val fabuPhone: EditText by bindView(R.id.fabu_phone)
    val fabuPhoneLayout: LinearLayout by bindView(R.id.fabu_phone_layout)
    val fabuShuruYanzm: EditText by bindView(R.id.fabu_shuru_yanzm)
    val fabuHuoquYanzm: Button by bindView(R.id.fabu_huoqu_yanzm)
    val fabuNologinShow: LinearLayout by bindView(R.id.fabu_nologin_show)
    val kuaisuBtnFabu: Button by bindView(R.id.kuaisu_btn_fabu)
    val xiangxiXuqiu: RelativeLayout by bindView(R.id.xiangxi_xuqiu)
    val taocanLogoText: TextView by bindView(R.id.taocan_logo_text)
    val taocanLogoLin: TextView by bindView(R.id.taocan_logo_lin)
    val taocanLogoClass: LinearLayout by bindView(R.id.taocan_logo_class)
    val taocanViText: TextView by bindView(R.id.taocan_vi_text)
    val taocanViLin: TextView by bindView(R.id.taocan_vi_lin)
    val taocanViClass: LinearLayout by bindView(R.id.taocan_vi_class)
    val taocanHttpText: TextView by bindView(R.id.taocan_http_text)
    val taocanHttpLin: TextView by bindView(R.id.taocan_http_lin)
    val taocanHttpClass: LinearLayout by bindView(R.id.taocan_http_class)
    val taocanAppText: TextView by bindView(R.id.taocan_app_text)
    val taocanAppLin: TextView by bindView(R.id.taocan_app_lin)
    val taocanAppClass: LinearLayout by bindView(R.id.taocan_app_class)
    val taoCanRV: RecyclerView by bindView(R.id.taocan_list)
    val modifyFabuPhone: TextView by bindView(R.id.modify_fabu_phone)
    private var timer: Timer? = null
    private var currentTime = (60 * 1000).toLong()
    private var timeGuanbi = (3 * 1000).toLong()
    private val GETYANZHENGMA = 0x124
    private val map = HashMap<String, Any>()
    private val kuaiMap = HashMap<String, Any>()
    private var manager: LinearLayoutManager? = null
    private var context: Context? = null
    private var faBuTaoAdapter: FaBuTaoAdapter? = null
    private var mIntent: Intent? = null
    private val jineList = intArrayOf(300, 500, 1000, 3000, 10000, 15000)
    private val xuqiuJiage = 300
    private val gaojineList = intArrayOf(20000, 50000, 100000)
    private val isXian = true
    private var isClass = 0
    private var ld: LoadingDialog? = null
    private var driveId: String? = ""
    private val kuaiTitle = arrayOf("LOGO设计", "APP开发", "网站开发", "软件开发", "寻求帮助", "公司起名", "新房装修", "包装设计", "品牌起名", "海报设计", "宣传册页", "宝宝起名")
    private val kuaiJiage = arrayOf("300以内", "300-500元", "500-1000元", "1000-3000元", "3000-1万元", "1万元以上")
    private val gaoJiage = arrayOf("1万元-3万元", "3万元-10万元", "10万元以上")
    private val list_title = ArrayList<GridViewInfoBean>()
    private val list_jiage = ArrayList<GridViewInfoBean>()
    private val list_biaoji = ArrayList<Boolean>()
    private val jiage_biaoji = ArrayList<Boolean>()
    private var kuaiSuAdapter: KuaiSuAdapter? = null
    private var kuaiJiaAdapter: KuaiSuAdapter? = null
    private var biaoqianStr = ""
    private var biaoqianJia = ""
    private val jsonList = ArrayList<DataStatisticsBean>()
    private var dataStatisticsBean = DataStatisticsBean()
    private val NETWORK_EXCEPTION = 0X111
    private var taoCanListBean: TaoCanListBean? = null
    private val hd = MHandler(this)

    /**
     * 存储页面数据
     */
    private var userid = ""
    private var pagename = ""
    private var pageindex = ""
    private var pageinittime = ""

    /**
     * 快速发布标签数据
     */
    private val kuaiNumber = ArrayList<Int>()

    /**
     * 套餐发布按钮监听
     */
    internal var viewItemClick: ViewItemClick = ViewItemClick { position ->
        if (LhtTool.isLogin) {
            showWindows(taoCanListBean!!.taocan_list[isClass].colname_list[position].tc_id)
        } else {
            CustomToast.showToast(this@FaBuDemandActivity, "请先登录后操作", Toast.LENGTH_LONG)
            val intent = Intent(this@FaBuDemandActivity, UserLoginActivity::class.java)
            dataStatisticsBean.pagenext = "套餐发布——登录页面"
            stopStorage()
            startActivity(intent)
        }
    }

    /**
     * 快速需求
     */
    private var kuaisuFabuGrid: MyGridView? = null
    private var jiageGrid: MyGridView? = null
    private var btnQueRen: Button? = null
    private var isKuaiXuan = 0
    private val isXuanBiao = 0
    private var isKuaiJia = 0
    private val isXuanJia = 0


    /**
     * 方法
     */
    private var kuaiSuBean = KuaiSuBean()

    /**
     * 弹窗
     */
    private var viewShow: View? = null
    private var popupWindow: PopupWindow? = null

    /**
     * 弹窗数据
     */
    private var fuwuJine: EditText? = null
    private var fuwuPhone: EditText? = null
    private var fuwumiaosu: EditText? = null
    private var fuwuTiJiao: Button? = null
    private var tc_jine: RelativeLayout? = null
    private var xuqiumiaosu: RelativeLayout? = null
    private var tc_title: LinearLayout? = null

    /**
     * 预约成功弹窗
     */
    private var yuyueTime: TextView? = null
    private var yuyueX: ImageView? = null

    /**
     * 套餐订单
     *
     * @param taocanId
     */
    private var isErrorBean: IsErrorBean? = IsErrorBean()


    private class MHandler constructor(activity: FaBuDemandActivity) : Handler() {
        private val weakReference: WeakReference<FaBuDemandActivity> = WeakReference(activity)
        private val activity: FaBuDemandActivity? = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            activity?.run {
                when (msg?.what) {
                    TIMETZM -> {
                        currentTime -= 1000
                        fabuHuoquYanzm.text = (currentTime / 1000).toString() + "秒后重新获取"
                        fabuHuoquYanzm.isEnabled = false
                        if (currentTime <= 0) {
                            currentTime = (60 * 1000).toLong()
                            timer!!.cancel()
                            fabuHuoquYanzm.text = "获取验证码"
                            fabuHuoquYanzm.isEnabled = true
                        }
                    }
                    TIMEGUANBI -> {
                        timeGuanbi -= 1000
                        yuyueTime!!.text = (timeGuanbi / 1000).toString() + "秒后重新获取"
                        if (timeGuanbi <= 0) {
                            timeGuanbi = (3 * 1000).toLong()
                            timer!!.cancel()
                            if (popupWindow != null) {
                                popupWindow!!.dismiss()
                                fabuDemandContent.setText("")
                            }
                        }
                    }
                    GETYANZHENGMA -> {
                        val s = msg.data.getString("s")
                        when (s) {
                            "false" -> {
                                CustomToast.showToast(this, "发送失败", Toast.LENGTH_LONG)
                                fabuHuoquYanzm.isEnabled = true
                                timer!!.cancel()
                                fabuHuoquYanzm.text = "获取验证码"
                            }
                            "has" -> {
                                CustomToast.showToast(this, "发送失败", Toast.LENGTH_LONG)
                                fabuHuoquYanzm.isEnabled = true
                                timer!!.cancel()
                                fabuHuoquYanzm.text = "获取验证码"
                            }
                            "no_reg" -> {
                                CustomToast.showToast(this, "还未注册", Toast.LENGTH_LONG)
                                fabuHuoquYanzm.isEnabled = true
                                timer!!.cancel()
                                fabuHuoquYanzm.text = "获取验证码"
                            }
                            "stopcache" -> {
                                CustomToast.showToast(this, "发送失败", Toast.LENGTH_LONG)
                                fabuHuoquYanzm.isEnabled = true
                                timer!!.cancel()
                                fabuHuoquYanzm.text = "获取验证码"
                            }
                            else -> {
                                kuaiMap["yzmkey"] = s!!
                                CustomToast.showToast(this, "发送成功", Toast.LENGTH_LONG)
                            }
                        }
                    }
                    TCLISTDATA -> if (taoCanListBean?.taocan_list != null) {
                        faBuTaoAdapter!!.lists = taoCanListBean!!.taocan_list[0].colname_list
                        faBuTaoAdapter!!.notifyDataSetChanged()
                    } else {
                        CustomToast.showToast(this, "未知错误", Toast.LENGTH_LONG)
                    }
                    TAOANTIJIAO -> {
                        ld?.dismiss()
                        finish()
                        CustomToast.showToast(this, "项目发布成功", Toast.LENGTH_LONG)
                        LhtTool.isFaLogin = "0"
                        mIntent = Intent(this, FaBuHouActivity::class.java)
                        mIntent!!.putExtra("ID", isErrorBean!!.err_msg)
                        dataStatisticsBean.pagenext = "发布成功——托管赏金页面"
                        stopStorage()
                        startActivity(mIntent)
                    }
                    ERROR -> {
                        ld?.dismiss()
                        CustomToast.showToast(this, isErrorBean!!.err_msg, Toast.LENGTH_LONG)
                    }
                    KUAISUSUCCES -> {
                        if (ld != null) {
                            ld!!.dismiss()
                        }
                        //                    finish();
                        //                    CustomToast.showToast(FaBuDemandActivity.this, "预约成功", Toast.LENGTH_LONG);
                        //                    LhtTool.isFaLogin = "0";
                        ////                    mIntent = new Intent(FaBuDemandActivity.this, FuKuanAfterActivity.class);
                        ////                    mIntent.putExtra("ID", kuaiSuBean.getErr_msg());
                        ////                    startActivity(mIntent);
                        yuYueWindow()
                    }
                    KUAIERROR -> {
                        ld?.dismiss()
                        CustomToast.showToast(this, kuaiSuBean.err_msg, Toast.LENGTH_LONG)
                    }
                    NETWORK_EXCEPTION -> LhtTool.showNetworkException(this, msg)
                    else -> {
                    }
                }
            }
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fabu_xuqiu_page)
        context = this
        driveId = LhtTool.getHouse_CID(this)
        title.text = "发布需求"
        if (LhtTool.isLogin) {
            fabuPhone.setText(MyApplication.userInfo!!.phone)
            fabuNologinShow.visibility = View.GONE
            fabuPhoneLayout.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2.0f)
        }
        getTaoCanListDataFromMain()
        viewListener()
        hListTaocan()
    }

    /**
     *  从MainActivity取出数据
     */

    private fun getTaoCanListDataFromMain() {
        when {

            !StringUtils.isEmpty(MainActivity.TAOCANDATA) ->
                try {
                    taoCanListBean = Gson().fromJson(MainActivity.TAOCANDATA, TaoCanListBean::class.java)
                    if (taoCanListBean == null) {
                        getTaoCanListDataFromNet()
                        return
                    }
                    hd.sendEmptyMessage(TCLISTDATA)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    getTaoCanListDataFromNet()
                }
            else -> getTaoCanListDataFromNet()
        }
    }


    /**
     * 快速发布标签
     */
    private fun setGridviewShow() {
        if (list_title.size > 0) {
            list_title.clear()
            list_biaoji.clear()
            list_jiage.clear()
        }
        for (i in kuaiTitle.indices) {
            val info = GridViewInfoBean()
            info.name = kuaiTitle[i]
            list_title.add(info)
            list_biaoji.add(false)
        }
        for (i in kuaiJiage.indices) {
            val info = GridViewInfoBean()
            info.name = kuaiJiage[i]
            list_jiage.add(info)
            jiage_biaoji.add(false)
        }
        kuaiSuAdapter = KuaiSuAdapter(context, list_title, list_biaoji)
        kuaiJiaAdapter = KuaiSuAdapter(context, list_jiage, jiage_biaoji)
        jiageGrid!!.adapter = kuaiJiaAdapter
        kuaisuFabuGrid!!.adapter = kuaiSuAdapter
    }

    override fun onResume() {
        super.onResume()

    }

    public override fun onStart() {
        super.onStart()
        storage("发布项目页面")
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
     * 套餐选择横向列表
     */
    private fun hListTaocan() {
        manager = LinearLayoutManager(MyApplication.INSTANCE.applicationContext)
        manager!!.orientation = LinearLayoutManager.HORIZONTAL
        taoCanRV.layoutManager = manager
        faBuTaoAdapter = FaBuTaoAdapter(context!!)
        faBuTaoAdapter!!.viewItemClick = viewItemClick
        taoCanRV.adapter = faBuTaoAdapter
    }

    /**
     * view监听事件
     */
    private fun viewListener() {
        fabuHuoquYanzm.setOnClickListener { huoquYan() }
        xiangxiXuqiu.setOnClickListener {
            mIntent = Intent(context, FaBuActicity::class.java)
            dataStatisticsBean.pagenext = "发布详细需求页面"
            stopStorage()
            startActivity(mIntent)
        }
        kuaisuBtnFabu.setOnClickListener {
            if (fabuDemandContent.text.toString() == "") {
                CustomToast.showToast(this@FaBuDemandActivity, "请输入或选择你想要的服务", Toast.LENGTH_LONG)
            } else if (fabuPhone.text.toString() == "") {
                CustomToast.showToast(this@FaBuDemandActivity, "联系方式不能为空", Toast.LENGTH_LONG)
            } else if (!LhtTool.isLogin) {
                if (fabuShuruYanzm.text.toString() == "") {
                    CustomToast.showToast(this@FaBuDemandActivity, "请输入验证码", Toast.LENGTH_LONG)
                } else {
                    kuaiFa()
                }
            } else {
                kuaiFa()
            }
            // yuYueWindow();
            //
            //                mIntent = new Intent(context, WebNoTitleActivity.class);
            //                mIntent.putExtra("weburl", UrlConfig.DIANPUXIANGQING);
            //                startActivity(mIntent);
        }
        kuaisuXuanze.setOnClickListener { setSpnner() }

        modifyFabuPhone.setOnClickListener { fabuPhone.setText("") }

        taocanLogoClass.setOnClickListener(this)
        taocanViClass.setOnClickListener(this)
        taocanHttpClass.setOnClickListener(this)
        taocanAppClass.setOnClickListener(this)
    }

    private fun biaoQianData() {
        if (kuaiNumber.size > 0) {
        }

        kuaiNumber.add(isKuaiXuan)
        kuaiNumber.add(isKuaiJia)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.taocan_logo_class -> {
                taocanLogoText.setTextColor(resources.getColor(R.color.vip_red))
                taocanViText.setTextColor(resources.getColor(R.color.black))
                taocanHttpText.setTextColor(resources.getColor(R.color.black))
                taocanAppText.setTextColor(resources.getColor(R.color.black))

                taocanLogoLin.setBackgroundResource(R.color.vip_red)
                taocanViLin.setBackgroundResource(R.color.white)
                taocanHttpLin.setBackgroundResource(R.color.white)
                taocanAppLin.setBackgroundResource(R.color.white)
                //faBuTaoAdapter.refresh();
                if (taoCanListBean?.taocan_list != null && taoCanListBean!!.taocan_list.size > 0) {
                    faBuTaoAdapter!!.lists = taoCanListBean!!.taocan_list[0].colname_list
                    faBuTaoAdapter!!.notifyDataSetChanged()
                    isClass = 0
                } else {
                    CustomToast.showToast(this@FaBuDemandActivity, "未知错误，请重试", Toast.LENGTH_LONG)
                }
            }
            R.id.taocan_vi_class -> {
                taocanLogoText.setTextColor(resources.getColor(R.color.black))
                taocanViText.setTextColor(resources.getColor(R.color.vip_red))
                taocanHttpText.setTextColor(resources.getColor(R.color.black))
                taocanAppText.setTextColor(resources.getColor(R.color.black))

                taocanLogoLin.setBackgroundResource(R.color.white)
                taocanViLin.setBackgroundResource(R.color.vip_red)
                taocanHttpLin.setBackgroundResource(R.color.white)
                taocanAppLin.setBackgroundResource(R.color.white)
                //  faBuTaoAdapter.refresh();
                if (taoCanListBean?.taocan_list != null && taoCanListBean!!.taocan_list.size > 0) {
                    faBuTaoAdapter!!.lists = taoCanListBean!!.taocan_list[1].colname_list
                    faBuTaoAdapter!!.notifyDataSetChanged()
                    isClass = 1
                } else {
                    CustomToast.showToast(this@FaBuDemandActivity, "未知错误，请重试", Toast.LENGTH_LONG)
                }
            }
            R.id.taocan_http_class -> {
                taocanLogoText.setTextColor(resources.getColor(R.color.black))
                taocanViText.setTextColor(resources.getColor(R.color.black))
                taocanHttpText.setTextColor(resources.getColor(R.color.vip_red))
                taocanAppText.setTextColor(resources.getColor(R.color.black))

                taocanLogoLin.setBackgroundResource(R.color.white)
                taocanViLin.setBackgroundResource(R.color.white)
                taocanHttpLin.setBackgroundResource(R.color.vip_red)
                taocanAppLin.setBackgroundResource(R.color.white)
                //  faBuTaoAdapter.refresh();
                if (taoCanListBean?.taocan_list != null && taoCanListBean!!.taocan_list.size > 0) {
                    faBuTaoAdapter!!.lists = taoCanListBean!!.taocan_list[2].colname_list
                    faBuTaoAdapter!!.notifyDataSetChanged()
                    isClass = 2
                } else {
                    CustomToast.showToast(this@FaBuDemandActivity, "未知错误，请重试", Toast.LENGTH_LONG)
                }
            }
            R.id.taocan_app_class -> {
                taocanLogoText.setTextColor(resources.getColor(R.color.black))
                taocanViText.setTextColor(resources.getColor(R.color.black))
                taocanHttpText.setTextColor(resources.getColor(R.color.black))
                taocanAppText.setTextColor(resources.getColor(R.color.vip_red))

                taocanLogoLin.setBackgroundResource(R.color.white)
                taocanViLin.setBackgroundResource(R.color.white)
                taocanHttpLin.setBackgroundResource(R.color.white)
                taocanAppLin.setBackgroundResource(R.color.vip_red)
                //faBuTaoAdapter.refresh();
                if (taoCanListBean?.taocan_list != null && taoCanListBean!!.taocan_list.size > 0) {
                    faBuTaoAdapter!!.lists = taoCanListBean!!.taocan_list[3].colname_list
                    faBuTaoAdapter!!.notifyDataSetChanged()
                    isClass = 3
                } else {
                    CustomToast.showToast(this@FaBuDemandActivity, "未知错误，请重试", Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun setSpnner() {
        viewShow = LayoutInflater.from(context).inflate(R.layout.kuaisu_fabu_show_window, null)
        popupWindow = PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isFocusable = true
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#F8F8F8")))//也可以直接把Color.TRANSPARENT换成0
        val lp = window.attributes
        window.attributes = lp
        val width = fabuLinear.width
        popupWindow!!.showAsDropDown(fabuDemandContent, width - 670, -5)
        popupWindow!!.setOnDismissListener {
            val lp = window.attributes
            lp.alpha = 1.0f //0.0-1.0
            window.attributes = lp
        }
        kuaisuFabuGrid = viewShow!!.findViewById(R.id.kuaisu_fabu_grid)
        jiageGrid = viewShow!!.findViewById(R.id.kuaisu_fabu_jiage_grid)
        btnQueRen = viewShow!!.findViewById(R.id.kuaisu_btn_queren)
        setGridviewShow()
        kuaisuFabuGrid!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            kuaiSuAdapter!!.setSeclection(position)
            isKuaiXuan = position

            kuaiSuAdapter!!.notifyDataSetChanged()
            if ((isKuaiXuan == 1) or (isKuaiXuan == 2) or (isKuaiXuan == 3)) {
                if (list_jiage.size > 0) {
                    jiage_biaoji.clear()
                    list_jiage.clear()
                }
                for (i in gaoJiage.indices) {
                    val info = GridViewInfoBean()
                    info.name = gaoJiage[i]
                    list_jiage.add(info)
                    jiage_biaoji.add(false)
                }
                kuaiJiaAdapter!!.notifyDataSetChanged()
            } else {
                if (list_jiage.size > 0) {
                    jiage_biaoji.clear()
                    list_jiage.clear()
                }
                for (i in kuaiJiage.indices) {
                    val info = GridViewInfoBean()
                    info.name = kuaiJiage[i]
                    list_jiage.add(info)
                    jiage_biaoji.add(false)
                }
                kuaiJiaAdapter!!.notifyDataSetChanged()
            }
        }
        jiageGrid!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            kuaiJiaAdapter!!.setSeclection(position)
            isKuaiJia = position
            kuaiJiaAdapter!!.notifyDataSetChanged()
        }
        btnQueRen!!.setOnClickListener {
            biaoQianData()
            if (kuaiNumber.size > 0) {
                biaoqianStr = kuaiTitle[kuaiNumber[0]]
                if ((isKuaiXuan == 1) or (isKuaiXuan == 2) or (isKuaiXuan == 3)) {
                    biaoqianJia = gaoJiage[kuaiNumber[1]]
                } else {
                    biaoqianJia = kuaiJiage[kuaiNumber[1]]
                }
                fabuDemandContent.setText(biaoqianStr + biaoqianJia + "")
                Log.e("fabuxuqiubiaoqian", kuaiTitle[kuaiNumber[0]] + kuaiTitle[kuaiNumber[1]] + kuaiNumber[0] + kuaiNumber[1])
                if (popupWindow != null) {
                    popupWindow!!.dismiss()
                    isKuaiXuan = 0
                    isKuaiJia = 0
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer?.purge()

        hd.removeCallbacksAndMessages(null)
    }

    private fun kuaiFa() {
        if (LhtTool.isLogin) {
            kuaiMap["userid"] = MyApplication.userInfo!!.userID
        } else {
            kuaiMap["userid"] = 0
            kuaiMap["yzm"] = fabuShuruYanzm.text.toString()
            kuaiMap["mobile_id"] = driveId!!
        }
        kuaiMap["crv_content"] = fabuDemandContent.text.toString()
        when (biaoqianJia) {
            "300以内" -> kuaiMap["item_money"] = xuqiuJiage
            "300-500元" -> kuaiMap["item_money"] = jineList[1]
            "500-1000元" -> kuaiMap["item_money"] = jineList[2]
            "1000-3000元" -> kuaiMap["item_money"] = jineList[3]
            "3000-1万元" -> kuaiMap["item_money"] = jineList[4]
            "1万元以上" -> kuaiMap["item_money"] = jineList[5]
            "1万元-3万元" -> kuaiMap["item_money"] = gaojineList[0]
            "3万元-10万元" -> kuaiMap["item_money"] = gaojineList[1]
            "10万元以上" -> kuaiMap["item_money"] = gaojineList[2]
        }

        Log.e("kuaisu", xuqiuJiage.toString() + "")
        kuaiMap["phone"] = fabuPhone.text.toString()
        kuaiMap["apptype"] = "an"
        kuaiMap["yuyue"] = "1"
        //kuaiMap.put("debug","1");
        ld = LoadingDialog(this@FaBuDemandActivity).setMessage("提交中....")
        ld!!.show()
        OkhttpTool.getOkhttpTool().post(UrlConfig.KUAISUFABU, kuaiMap, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val s = response.body()!!.string()
                Log.e("kuaisu", s)
                kuaiSuBean = Gson().fromJson(s, KuaiSuBean::class.java)
                if (kuaiSuBean.err_code == "0") {
                    hd.sendEmptyMessage(KUAISUSUCCES)
                    if (LhtTool.isLogin) {
                    } else {
                        //输入手机号验证码后发布需求成功时  这里拿到数据后改为已登录状态
                        MyApplication.userInfo = UserInfoBean()
                        MyApplication.userInfo!!.userID = kuaiSuBean.userInfo.userID
                        MyApplication.userInfo!!.cookieLoginToken = kuaiSuBean.userInfo.cookieLoginToken
                        MyApplication.userInfo!!.cookieLoginIpt = kuaiSuBean.userInfo.cookieLoginIpt
                        MyApplication.userInfo!!.address = kuaiSuBean.userInfo.address
                        MyApplication.userInfo!!.area = kuaiSuBean.userInfo.area
                        MyApplication.userInfo!!.balance = kuaiSuBean.userInfo.balance
                        MyApplication.userInfo!!.bankcardno = kuaiSuBean.userInfo.bankcardno
                        MyApplication.userInfo!!.bankname = kuaiSuBean.userInfo.bankname
                        MyApplication.userInfo!!.birthday = kuaiSuBean.userInfo.birthday
                        MyApplication.userInfo!!.cellPhone = kuaiSuBean.userInfo.cellPhone
                        MyApplication.userInfo!!.cookieLoginLogs = kuaiSuBean.userInfo.cookieLoginLogs
                        MyApplication.userInfo!!.cookieLoginName = kuaiSuBean.userInfo.cookieLoginName
                        MyApplication.userInfo!!.cookieLoginTime = kuaiSuBean.userInfo.cookieLoginTime
                        MyApplication.userInfo!!.email = kuaiSuBean.userInfo.email
                        //MyApplication.userInfo.setFavItems(kuaiSuBean.getUserInfo().getFavItems());
                        MyApplication.userInfo!!.homeName = kuaiSuBean.userInfo.homeName
                        MyApplication.userInfo!!.huanxin_api_password = kuaiSuBean.userInfo.huanxin_api_password
                        MyApplication.userInfo!!.icon = kuaiSuBean.userInfo.icon
                        MyApplication.userInfo!!.is_bind_bankinfo = kuaiSuBean.userInfo.is_bind_bankinfo
                        MyApplication.userInfo!!.is_verify_email = kuaiSuBean.userInfo.is_verify_email
                        MyApplication.userInfo!!.is_verify_fullname = kuaiSuBean.userInfo.is_verify_fullname
                        MyApplication.userInfo!!.is_verify_phone = kuaiSuBean.userInfo.is_verify_phone
                        MyApplication.userInfo!!.isGuzhu = kuaiSuBean.userInfo.isGuzhu
                        MyApplication.userInfo!!.isNew = kuaiSuBean.userInfo.isNew
                        //  MyApplication.userInfo.setIsSecrecy(Integer.valueOf(kuaiSuBean.getUserInfo().getIsNew()+""));
                        MyApplication.userInfo!!.isSecrecy = kuaiSuBean.userInfo.isSecrecy
                        MyApplication.userInfo!!.isShop = kuaiSuBean.userInfo.isShop
                        MyApplication.userInfo!!.new_info_name = kuaiSuBean.userInfo.new_info_name
                        MyApplication.userInfo!!.new_info_url = kuaiSuBean.userInfo.new_info_url
                        MyApplication.userInfo!!.nickame = kuaiSuBean.userInfo.nickame
                        MyApplication.userInfo!!.passWord = kuaiSuBean.userInfo.passWord
                        MyApplication.userInfo!!.phone = kuaiSuBean.userInfo.phone
                        MyApplication.userInfo!!.postalCode = kuaiSuBean.userInfo.postalCode
                        MyApplication.userInfo!!.qq = kuaiSuBean.userInfo.qq
                        MyApplication.userInfo!!.realName = kuaiSuBean.userInfo.realName
                        MyApplication.userInfo!!.selfIntroduction = kuaiSuBean.userInfo.selfIntroduction
                        MyApplication.userInfo!!.sex = kuaiSuBean.userInfo.sex
                        MyApplication.userInfo!!.sign = kuaiSuBean.userInfo.sign
                        MyApplication.userInfo!!.tuiguang_money = kuaiSuBean.userInfo.tuiguang_money
                        MyApplication.userInfo!!.viptype = kuaiSuBean.userInfo.viptype
                        LhtTool.isLoginPage = true
                        LhtTool.isLogin = true
                    }
                } else {
                    hd.sendEmptyMessage(KUAIERROR)
                }
            }
        })
    }

    private fun showWindows(taocanId: String) {
        viewShow = LayoutInflater.from(context).inflate(R.layout.fuwu_goumai_item, null)
        popupWindow = PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isFocusable = true
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#F8F8F8")))//也可以直接把Color.TRANSPARENT换成0
        popupWindow!!.animationStyle = R.style.popWindow_animation
        popupWindow!!.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        val lp = window.attributes
        lp.alpha = 0.6f //0.0-1.0
        window.attributes = lp
        if (!popupWindow!!.isShowing) {
            popupWindow!!.showAtLocation(viewShow, Gravity.CENTER, 0, 1000)
        }
        popupWindow!!.setOnDismissListener {
            val lp = window.attributes
            lp.alpha = 1.0f //0.0-1.0
            window.attributes = lp
        }
        showData(taocanId)
    }

    private fun showData(taocanId: String) {
        fuwuJine = viewShow!!.findViewById(R.id.fbxq_ys)
        fuwuPhone = viewShow!!.findViewById(fbxq_phone)
        fuwuTiJiao = viewShow!!.findViewById(R.id.fuwu_goumai_enter)
        fuwumiaosu = viewShow!!.findViewById(R.id.fabu_taocan_xuqiu_miaosu)
        tc_jine = viewShow!!.findViewById(R.id.tanc_jine)
        tc_title = viewShow!!.findViewById(R.id.fuwu_tanc_title)
        xuqiumiaosu = viewShow!!.findViewById(R.id.xuqiumiaosu_taocan)
        tc_title!!.visibility = View.GONE
        tc_jine!!.visibility = View.GONE
        xuqiumiaosu!!.visibility = View.VISIBLE
        if (LhtTool.isLogin) {
            fuwuPhone!!.setText(MyApplication.userInfo!!.phone)
        }
        fuwuTiJiao!!.text = "提交发布"
        fuwuTiJiao!!.setOnClickListener {
            if (LhtTool.isLogin) {
                ld = LoadingDialog(this@FaBuDemandActivity).setMessage("提交中....")
                ld!!.show()
                if (fuwuPhone!!.text.toString() == "") {
                    CustomToast.showToast(this@FaBuDemandActivity, "请输入联系方式", Toast.LENGTH_LONG)
                }
                taocanDing(taocanId)
            } else {
                LhtTool.isLoginFa = false
                val intent = Intent(this@FaBuDemandActivity, UserLoginActivity::class.java)
                dataStatisticsBean.pagenext = "套餐发布——登录页面"
                stopStorage()
                startActivity(intent)

            }
        }
    }

    private fun yuYueWindow() {
        viewShow = LayoutInflater.from(context).inflate(R.layout.yuyue_chenggong_item, null, false)
        popupWindow = PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isFocusable = true
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#F8F8F8")))//也可以直接把Color.TRANSPARENT换成0
        popupWindow!!.animationStyle = R.style.popWindow_animation
        val lp = window.attributes
        lp.alpha = 0.6f //0.0-1.0
        window.attributes = lp
        popupWindow!!.showAtLocation(viewShow, Gravity.CENTER, 0, 0)
        popupWindow!!.setOnDismissListener {
            val lp = window.attributes
            lp.alpha = 1.0f //0.0-1.0
            window.attributes = lp
        }
        yuyueTime = viewShow!!.findViewById(R.id.time_guanbi)
        yuyueX = viewShow!!.findViewById(R.id.back_x)
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                hd.sendEmptyMessage(TIMEGUANBI)
            }
        }, 0, 1000)

        yuyueX!!.setOnClickListener {
            if (popupWindow != null) {
                popupWindow!!.dismiss()
                fabuDemandContent.setText("")
            }
        }
    }

    private fun taocanDing(taocanId: String) {
        val mapDing = HashMap<String, Any>()
        mapDing["userid"] = MyApplication.userInfo!!.userID
        mapDing["taocanid"] = taocanId
        mapDing["phone"] = fuwuPhone!!.text.toString()
        mapDing["des"] = fuwumiaosu!!.text.toString()
        mapDing["apptype"] = "an"
//        mapDing["debug"] = "1"
        OkhttpTool.getOkhttpTool().post(UrlConfig.TAOCANDING, mapDing, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
                LogUtils.e("taocding", s)
                try {
                    isErrorBean = Gson().fromJson(s, IsErrorBean::class.java)
                    if (isErrorBean != null) {
                        if (isErrorBean!!.err_code == "0") {
                            hd.sendEmptyMessage(TAOANTIJIAO)
                        } else {
                            hd.sendEmptyMessage(ERROR)
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        })
    }


    /**
     * 套餐发布列表数据
     */

    private fun getTaoCanListDataFromNet() {
        val mapList = HashMap<String, Any>()
        //   map.put("userid","0");
        OkhttpTool.getOkhttpTool().post(UrlConfig.TAOCANLIST, mapList, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }


            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    Log.e("fabudemand", s)
                    MainActivity.TAOCANDATA = s //同步至MainActivity
                    taoCanListBean = Gson().fromJson(s, TaoCanListBean::class.java)
                    hd.sendEmptyMessage(TCLISTDATA)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    /**
     * 获取验证码
     */
    private fun huoquYan() {
        fabuHuoquYanzm.isEnabled = false

        if (fabuPhone.text.toString().length == 11 && LhtTool.isMobile(fabuPhone.text.toString())) {
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    hd.sendEmptyMessage(TIMETZM)
                }
            }, 0, 1000)
            map["mobile"] = fabuPhone.text
            map["key"] = UrlConfig.MOBILE_VERIFICATION_KEY
            map["type"] = "verify"
            map["check"] = "0"
            map["android"] = "1"
            OkhttpTool.getOkhttpTool().post(UrlConfig.MOBILE_VERIFICATION, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val s = response.body()!!.string()
                    Log.e("yanzhengma", s)
                    val ms = Message()
                    ms.what = GETYANZHENGMA
                    val b = Bundle()
                    b.putString("s", s)
                    ms.data = b
                    hd.sendMessage(ms)
                }
            })
        } else {
            CustomToast.showToast(this@FaBuDemandActivity, "请输入正确的手机号码", Toast.LENGTH_LONG)
            fabuHuoquYanzm.isEnabled = true
        }
    }


    override fun onPointerCaptureChanged(hasCapture: Boolean) {

    }

    companion object {
        private val TIMETZM = 0X120
        private val TIMEGUANBI = 0X121
        private val TCLISTDATA = 0x125
        private val TAOANTIJIAO = 0x126
        private val ERROR = 0x130
        private val KUAISUSUCCES = 0x127
        private val KUAIERROR = 0x128
    }
}
