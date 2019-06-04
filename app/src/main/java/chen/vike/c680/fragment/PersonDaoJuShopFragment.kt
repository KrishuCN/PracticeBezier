package chen.vike.c680.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import chen.vike.c680.ALiPay.Alipay
import chen.vike.c680.Interface.ViewItemClick
import chen.vike.c680.WXPay.GetPrepayIdTask
import chen.vike.c680.adapter.DaoJuShopAdapter
import chen.vike.c680.bean.DaoJuBean
import chen.vike.c680.bean.GouMaiBean
import chen.vike.c680.bean.ZhifuDaojuBean
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.lht.vike.a680_v1.R
import kotterknife.bindView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2018/6/12.
 * 道具商城fragment.
 */

class PersonDaoJuShopFragment : Fragment() {

    internal val daojuHeadImg: ImageView by bindView(R.id.daoju_head_img)
    internal val daojuHeadName: TextView by bindView(R.id.daoju_head_name)
    internal val daojuHeadContent: TextView by bindView(R.id.daoju_head_content)
    internal val daojuHeadJiaqian: TextView by bindView(R.id.daoju_head_jiaqian)
    internal val daojuHeadGoumai: Button by bindView(R.id.daoju_head_goumai)
    internal val daojuHead: LinearLayout by bindView(R.id.daoju_head)
    internal val daojuShopList: RecyclerView by bindView(R.id.daoju_shop_list)
    private var shopView: View? = null
    private var mContext: Context? = null
    private var manager: LinearLayoutManager? = null
    private var shopAdapter: DaoJuShopAdapter? = null
    private val lists = ArrayList<DaoJuBean.ItemsBean>()
    private var daojuNumberWindow: LinearLayout? = null
    private var daojuMoneyWindow: LinearLayout? = null
    private val NETWORK_EXCEPTION = 0X111
    private var ld: LoadingDialog? = null

    internal var viewItemClick: ViewItemClick = ViewItemClick { position -> showWindows(position, true) }
    /**
     * 购买弹窗
     */
    private var viewShow: View? = null
    private var popupWindow: PopupWindow? = null

    /**
     * 购买弹窗数据
     */
    private var btn_jian: Button? = null
    private var btn_number: Button? = null
    private var btn_jia: Button? = null
    private var btn_goumai: Button? = null
    private var daojuNumberMoney: TextView? = null
    private var yueshow: TextView? = null
    private var zhifubao: RelativeLayout? = null
    private var weixin: RelativeLayout? = null
    private var yue: RelativeLayout? = null
    private var waitData: LinearLayout? = null
    private var number = 0

    /**
     * 支付监听
     */
    private var isZhifuf = true
    private val isYue = false

    private val zhimap = HashMap<String, Any>()
    private var zhifuDaojuBean = ZhifuDaojuBean()
    private var str = ""
    private val gouMap = HashMap<String, Any>()
    private var gouMaiBean = GouMaiBean()
    private val map = HashMap<String, Any>()
    private var daoJuBean = DaoJuBean()
    private val handler = MHandler(this)


    private class MHandler(personDaoJuShopFragment: PersonDaoJuShopFragment):Handler(){
        private val weakReference:WeakReference<PersonDaoJuShopFragment> = WeakReference(personDaoJuShopFragment)
        private val ref = weakReference.get()

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            ref?.run {
                when (msg?.what) {
                    DAOJUNUMBER -> {
                        shopAdapter!!.addList(lists)
                        shopAdapter!!.notifyDataSetChanged()
                        daojuHeadContent.text = daoJuBean.items[0].cont
                        daojuHeadName.text = daoJuBean.items[0].name
                        daojuHeadJiaqian.text = daoJuBean.items[0].danjia
                        Glide.with(mContext!!).load(daoJuBean.items[0].img).into(daojuHeadImg!!)
                        daojuHead.visibility = View.VISIBLE
                    }
                    GOUMAIDAOJU -> if (gouMaiBean.err_code == "0") {
                        Toast.makeText(mContext, gouMaiBean.err_msg, Toast.LENGTH_SHORT).show()
                        waitData!!.visibility = View.GONE
                        yueshow!!.text = "余额:" + gouMaiBean.user_yumoney
                        MyApplication.userInfo!!.balance = gouMaiBean.user_yumoney
                    } else {//余额不足
                        Toast.makeText(mContext, gouMaiBean.err_msg, Toast.LENGTH_SHORT).show()
                        waitData!!.visibility = View.GONE
                    }
                    ZHIFUDAOJU -> if (isZhifuf) {
                        waitData!!.visibility = View.GONE
                        if (UrlConfig.alipay_flag == "1") {
                            ld = LoadingDialog(mContext!!).setMessage("加载中.....")
                            ld!!.show()
                            Alipay.pay(mContext, LhtTool.getHander(mContext, zhifuDaojuBean.total, ld), Alipay.getOrderInfo("支付:" + zhifuDaojuBean.daojuname, "支付" + zhifuDaojuBean.djid + "号项目订单" + zhifuDaojuBean.orderno, zhifuDaojuBean.total, zhifuDaojuBean.orderno))

                        } else {
                            CustomToast.showToast(mContext, "该版本暂时不支持支付宝支付",
                                    Toast.LENGTH_SHORT)
                        }
                    } else {
                        waitData!!.visibility = View.GONE
                        if (UrlConfig.weixinpay_flag == "1") {
                            val get = GetPrepayIdTask(activity, zhifuDaojuBean.orderno, zhifuDaojuBean.daojuname + "付款", zhifuDaojuBean.total)
                            get.execute()
                        } else {
                            CustomToast.showToast(mContext, "该版本暂时不支持微信支付",
                                    Toast.LENGTH_SHORT)
                        }
                        isZhifuf = true
                    }
                    ZHIFUERRO -> {
                    }
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = activity
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        shopView = LayoutInflater.from(mContext).inflate(R.layout.activity_daoju_shop, null)
        return shopView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * 初始化
     */
    private fun initView() {
        manager = LinearLayoutManager(mContext)
        manager!!.orientation = LinearLayoutManager.VERTICAL
        daojuShopList.layoutManager = manager
        shopAdapter = DaoJuShopAdapter(mContext, lists)
        shopAdapter!!.viewItemClick = viewItemClick
        daojuShopList.adapter = shopAdapter
        daojuHead.visibility = View.GONE
        listData()
        daojuHeadGoumai.setOnClickListener { showWindows(0, false) }
    }

    private fun showWindows(position: Int, head: Boolean) {
        viewShow = LayoutInflater.from(mContext).inflate(R.layout.daoju_goumai_window, null)
        popupWindow = PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isFocusable = true
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#F8F8F8")))//也可以直接把Color.TRANSPARENT换成0
        popupWindow!!.animationStyle = R.style.popWindow_animation
        val lp = activity!!.window.attributes
        lp.alpha = 0.6f //0.0-1.0
        activity!!.window.attributes = lp
        popupWindow!!.showAtLocation(viewShow, Gravity.CENTER, 0, 1000)
        popupWindow!!.setOnDismissListener {
            val lp = activity!!.window.attributes
            lp.alpha = 1.0f //0.0-1.0
            activity!!.window.attributes = lp
        }
        showData(position, head)
    }

    private fun showData(position: Int, head: Boolean) {
        btn_jian = viewShow!!.findViewById(R.id.btn_number_jian)
        btn_number = viewShow!!.findViewById(R.id.btn_number)
        btn_jia = viewShow!!.findViewById(R.id.btn_number_jia)
        btn_goumai = viewShow!!.findViewById(R.id.btn_enter_goumai)
        daojuNumberWindow = viewShow!!.findViewById(R.id.doaju_number_window)
        daojuMoneyWindow = viewShow!!.findViewById(R.id.daoju_money_window)
        daojuNumberMoney = viewShow!!.findViewById(R.id.daoju_money_number)
        zhifubao = viewShow!!.findViewById(R.id.daoju_zhifu_zfb)
        weixin = viewShow!!.findViewById(R.id.daoju_zhifu_wx)
        yue = viewShow!!.findViewById(R.id.daoju_zhifu_yue)
        waitData = viewShow!!.findViewById(R.id.wait_zhifu_data)
        yueshow = viewShow!!.findViewById(R.id.yue_num_show)
        yueshow!!.text = "余额:" + MyApplication.userInfo!!.balance
        Log.e("daojuposition", position.toString() + "")
        if (head == true) {
            btn_jian!!.setOnClickListener {
                if (number > 0) {
                    number -= 1
                    btn_number!!.text = number.toString() + ""
                    val strDanJia = lists[position].danjia
                    val intDanJia = Integer.valueOf(strDanJia)
                    val needMoney = number * intDanJia
                    daojuNumberMoney!!.text = needMoney.toString() + ""
                }
            }
            btn_jia!!.setOnClickListener {
                number += 1
                btn_number!!.text = number.toString() + ""
                val strDanJia = lists[position].danjia
                val intDanJia = Integer.valueOf(strDanJia)
                val needMoney = number * intDanJia
                daojuNumberMoney!!.text = needMoney.toString() + ""
            }
            number = 1
            val strDanJia = lists[position].danjia
            val intDanJia = Integer.valueOf(strDanJia)
            val needMoney = number * intDanJia
            daojuNumberMoney!!.text = needMoney.toString() + ""

            zhifuListener(position, true)//支付监听
        } else {
            btn_jian!!.setOnClickListener {
                if (number > 0) {
                    number -= 1
                    btn_number!!.text = number.toString() + ""
                    val strDanJia = daoJuBean.items[0].danjia
                    val intDanJia = Integer.valueOf(strDanJia)
                    val needMoney = number * intDanJia
                    daojuNumberMoney!!.text = needMoney.toString() + ""
                }
            }
            btn_jia!!.setOnClickListener {
                number += 1
                btn_number!!.text = number.toString() + ""
                val strDanJia = daoJuBean.items[0].danjia
                val intDanJia = Integer.valueOf(strDanJia)
                val needMoney = number * intDanJia
                daojuNumberMoney!!.text = needMoney.toString() + ""
            }
            number = 1
            val strDanJia = daoJuBean.items[0].danjia
            val intDanJia = Integer.valueOf(strDanJia)
            val needMoney = number * intDanJia
            daojuNumberMoney!!.text = needMoney.toString() + ""
            zhifuListener(0, false)//支付监听

        }
        btn_goumai!!.setOnClickListener {
            if (LhtTool.isLogin) {
                tijiao(position, head)
            } else {
                Toast.makeText(mContext, "请登录后进行操作", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun zhifuListener(position: Int, head: Boolean) {
        zhifubao!!.setOnClickListener {
            if (number == 0) {
                Toast.makeText(mContext, "请选择数量", Toast.LENGTH_SHORT).show()
            } else {
                zhifuData(position, head, number)
                waitData!!.visibility = View.VISIBLE
            }
        }
        weixin!!.setOnClickListener {
            isZhifuf = false
            if (number == 0) {
                Toast.makeText(mContext, "请选择数量", Toast.LENGTH_SHORT).show()
            } else {
                zhifuData(position, head, number)
                waitData!!.visibility = View.VISIBLE
            }
        }
        yue!!.setOnClickListener {
            tijiao(position, head)
            waitData!!.visibility = View.VISIBLE
        }
    }

    private fun zhifuData(position: Int, head: Boolean, number: Int) {
        if (head) {
            zhimap["djid"] = lists[position].sid
        } else {
            zhimap["djid"] = daoJuBean.items[0].sid
        }
        zhimap["buynum"] = number
        zhimap["userid"] = MyApplication.userInfo!!.userID
        zhimap["type"] = "an"
        OkhttpTool.getOkhttpTool().post(DAOJUKA, zhimap, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    zhifuDaojuBean = Gson().fromJson(s, ZhifuDaojuBean::class.java)
                    if (zhifuDaojuBean.userid != null) {
                        handler.sendEmptyMessage(ZHIFUDAOJU)
                    } else {
                        handler.sendEmptyMessage(ZHIFUERRO)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun tijiao(position: Int, head: Boolean) {
        gouMap["userid"] = MyApplication.userInfo!!.userID
        gouMap["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
        gouMap["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
        if (head) {
            gouMap["djid"] = lists[position].sid
            gouMap["buynum"] = btn_number!!.text
            str = gouMaiUrl
        } else {
            gouMap["buy_need_year"] = btn_number!!.text
            str = headUrl
        }

        OkhttpTool.getOkhttpTool().post(str, gouMap, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    Log.e("head", s)
                    gouMaiBean = Gson().fromJson(s, GouMaiBean::class.java)
                    handler.sendEmptyMessage(GOUMAIDAOJU)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

    }

    private fun listData() {
        map["propid"] = 0
        map["userid"] = MyApplication.userInfo!!.userID
        OkhttpTool.getOkhttpTool().post(strUrl, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    // Log.e("daoju", s);
                    daoJuBean = Gson().fromJson(s, DaoJuBean::class.java)
                    for (i in 1 until daoJuBean.items.size) {
                        val daoju = DaoJuBean.ItemsBean()
                        daoju.bianhao = daoJuBean.items[i].bianhao
                        daoju.cont = daoJuBean.items[i].cont
                        daoju.danjia = daoJuBean.items[i].danjia
                        daoju.img = daoJuBean.items[i].img
                        daoju.name = daoJuBean.items[i].name
                        daoju.sid = daoJuBean.items[i].sid
                        lists.add(daoju)
                    }
                    handler.sendEmptyMessage(DAOJUNUMBER)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private val DAOJUNUMBER = 8
        private val GOUMAIDAOJU = 9
        private val DAOJUKA = "http://app.680.com/api/v4/daojuka_pay.ashx"
        private val ZHIFUDAOJU = 11
        private val ZHIFUERRO = 0

        /**
         * 购买道具
         */
        private val gouMaiUrl = "http://app.680.com/api/v4/buy_props.ashx"
        private val headUrl = "http://app.680.com/api/v4/buy_gold_service.ashx"

        /**
         * 列表数据
         */
        private val strUrl = "http://app.680.com/api/v4/props.ashx"
    }
}
