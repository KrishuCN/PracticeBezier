package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import chen.vike.c680.ALiPay.Alipay
import chen.vike.c680.bean.ItemAddToPay
import chen.vike.c680.bean.ItemPayGson
import chen.vike.c680.bean.PayResultGson
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.WXPay.GetPrepayIdTask
import com.lht.vike.a680_v1.R

import java.io.IOException
import java.util.HashMap

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

/**
 * Created by lht on 2017/3/16.
 *
 * 支付界面
 */

class FuKuanActivity : BaseStatusBarActivity() {

    private val map = HashMap<String, Any>()
    private var mBianhao: TextView? = null
    private var mMoney: TextView? = null
    private var mBalance: TextView? = null

    private var zfb: View? = null
    private var wxzf: View? = null
    private var yezf: View? = null

    private var id: String? = null
    private var ld: LoadingDialog? = null
    private var itemPayGson: ItemPayGson? = null
    private var itemAddToPay: ItemAddToPay? = null
    private var payResultGson: PayResultGson? = null
    private var isAddToPay: Boolean = false
    private val ADDTOPAY = 0x123
    private val ADDTOPAYDO = 0x133
    private val PAYRESULT = 0x114
    private val ITEMPAY = 0x113
    private val NETWORKEXCEPTION = 0X111
    @SuppressLint("HandlerLeak")
    private val hd = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (ld != null) {
                ld!!.dismiss()
            }
            if (msg.what == ADDTOPAY) {
                isAddToPay = true
                mMoney!!.text = Html.fromHtml("支付金额：<font color='#df231b'>￥" + itemAddToPay!!.totalfee + "<font color='#df231b'>")

            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(this@FuKuanActivity, msg)

            } else if (msg.what == ITEMPAY) {

                mBianhao!!.text = "项目编号：" + itemPayGson!!.itemId
                mMoney!!.text = Html.fromHtml("项目金额：<font color='#df231b'>￥" + itemPayGson!!.total + "</font>")
                mBalance!!.text = Html.fromHtml("余额  <font color='#cccccc'>（可用余额：￥" + itemPayGson!!.balance + "）</font>")

            } else if (msg.what == ADDTOPAYDO) {
                isAddToPay = true
                val s = msg.data.getString("s")
                when (s) {
                    "nopay" -> CustomToast.showToast(this@FuKuanActivity, " 无效支付请求", Toast.LENGTH_LONG)
                    "moneyless" -> CustomToast.showToast(this@FuKuanActivity, "余额不足", Toast.LENGTH_LONG)
                    "fail" -> CustomToast.showToast(this@FuKuanActivity, "支付失败", Toast.LENGTH_LONG)
                    "orderno_err" -> CustomToast.showToast(this@FuKuanActivity, "无效订单编号", Toast.LENGTH_LONG)
                    "ok" -> {
                        CustomToast.showToast(this@FuKuanActivity, "余额支付增加悬赏成功", Toast.LENGTH_LONG)
                        finish()
                    }
                    "unlogin" -> CustomToast.showToast(this@FuKuanActivity, "未登录", Toast.LENGTH_LONG)
                    else -> CustomToast.showToast(this@FuKuanActivity, "支付失败", Toast.LENGTH_LONG)
                }
            } else if (msg.what == PAYRESULT) {

                when (payResultGson!!.payResult) {
                    "has" -> CustomToast.showToast(this@FuKuanActivity, "该项目已经支付过了", Toast.LENGTH_LONG)
                    "moneyless" -> CustomToast.showToast(this@FuKuanActivity, "余额不足", Toast.LENGTH_LONG)
                    "fail" -> CustomToast.showToast(this@FuKuanActivity, "支付失败", Toast.LENGTH_LONG)
                    "nomoney" -> CustomToast.showToast(this@FuKuanActivity, "项目金额为0，不能支付", Toast.LENGTH_LONG)
                    "ok" -> {
                        val builder = AlertDialog.Builder(this@FuKuanActivity).setTitle("付款成功").setMessage("已支付成功，待审核通过后将发布； 您随时可以登录您的用户名查看威客们提交的方案，并采纳满意的方案，如需与威客沟通，请直接给他们发站内消息或者向时间财富客服索取相关威客的联系方式，如有不明白的地方请致电：4006306800 \n" + "QQ：4006306800、1416446001").setNegativeButton("确定") { dialog, which ->
                            dialog.dismiss()
                            MyApplication.userInfo!!.balance = (java.lang.Float.valueOf(itemPayGson!!.balance) - java.lang.Float.valueOf(itemPayGson!!.total)).toString()
                            finish()
                        }
                        val dialog = builder.create()
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.show()
                    }
                    else -> CustomToast.showToast(this@FuKuanActivity, payResultGson!!.payResult, Toast.LENGTH_LONG)
                }

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fukuan_one_activtity)

        title.text = "托管赏金"
        mBianhao = findViewById(R.id.fk_ddbh)
        mMoney = findViewById(R.id.fk_ddje)
        mBalance = findViewById(R.id.fk_ye_num)
        zfb = findViewById(R.id.zfb)
        wxzf = findViewById(R.id.wxzf)
        yezf = findViewById(R.id.yezf)
        zfb!!.setOnClickListener {
            if (UrlConfig.alipay_flag == "1") {
                if (isAddToPay) {
                    ld = LoadingDialog(this@FuKuanActivity).setMessage("加载中.....")
                    ld!!.show()
                    Alipay.pay(this@FuKuanActivity, LhtTool.getHander(this@FuKuanActivity, itemAddToPay!!.totalfee, ld), Alipay.getOrderInfo("支付" + id + "号项目金额", "支付" + id + "号项目订单" + itemAddToPay!!.orderno, itemAddToPay!!.totalfee, itemAddToPay!!.orderno))
                } else {
                    Alipay.pay(this@FuKuanActivity, LhtTool.getHander(this@FuKuanActivity, itemPayGson!!.total, ld), Alipay.getOrderInfo("支付" + itemPayGson!!.itemId + "号项目金额", "支付" + itemPayGson!!.itemId + "号项目订单" + itemPayGson!!.orderno, itemPayGson!!.total, itemPayGson!!.orderno))
                }
            } else {
                CustomToast.showToast(this@FuKuanActivity, "该版本暂时不支持支付宝支付",
                        Toast.LENGTH_SHORT)
            }
        }
        wxzf!!.setOnClickListener {
            if (UrlConfig.weixinpay_flag == "1") {
                if (isAddToPay) {
                    val get = GetPrepayIdTask(this@FuKuanActivity, itemAddToPay!!.orderno, "订单编号：" + id + "付款", itemAddToPay!!.totalfee)
                    get.execute()
                } else {
                    val get = GetPrepayIdTask(this@FuKuanActivity, itemPayGson!!.orderno, "订单编号：" + itemPayGson!!.itemId + "付款", itemPayGson!!.total)
                    get.execute()
                }
            } else {
                CustomToast.showToast(this@FuKuanActivity, "该版本暂时不支持微信支付",
                        Toast.LENGTH_SHORT)
            }
        }
        yezf!!.setOnClickListener {
            val builder = AlertDialog.Builder(this@FuKuanActivity)
            builder.setTitle("温馨提示").setMessage("您确定使用余额支付？").setNegativeButton("确定") { dialog, which ->
                if (isAddToPay) {
                    if (java.lang.Float.valueOf(MyApplication.userInfo!!.balance) >= java.lang.Float.valueOf(itemAddToPay!!.totalfee)) {
                        map["orderno"] = itemAddToPay!!.orderno
                        OkhttpTool.getOkhttpTool().post(UrlConfig.GET_ITEM_ADD_DO, map, object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                            }

                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {

                                val s = response.body()!!.string()
                                LogUtils.d("===============response:$s")
                                val ms = Message()
                                ms.what = ADDTOPAYDO
                                val b = Bundle()
                                b.putString("s", s)
                                ms.data = b
                                hd.sendMessage(ms)

                            }
                        })

                    } else {
                        val builder = AlertDialog.Builder(this@FuKuanActivity).setTitle("提示信息").setMessage("余额不足，是否充值").setNegativeButton("是") { dialog, which -> startActivityForResult(Intent(this@FuKuanActivity, RechargeActivity::class.java), 1) }.setNeutralButton("否") { dialog, which -> dialog.dismiss() }
                        val dialog1 = builder.create()
                        //                                dialog1.setInverseBackgroundForced(false);
                        dialog1.show()
                    }
                } else {
                    if (java.lang.Float.valueOf(itemPayGson!!.balance) >= java.lang.Float.valueOf(itemPayGson!!.total)) {
                        map["orderno"] = itemPayGson!!.orderno
                        OkhttpTool.getOkhttpTool().post(UrlConfig.GET_TOPAY_ITEM, map, object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                            }


                            override fun onResponse(call: Call, response: Response) {
                                try {
                                    val s = response.body()!!.string()
                                    LogUtils.d("===================response:$s")
                                    payResultGson = Gson().fromJson(s, PayResultGson::class.java)
                                    hd.sendEmptyMessage(PAYRESULT)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }


                            }
                        })
                        ld = LoadingDialog(this@FuKuanActivity).setMessage("加载中.....")
                        ld!!.show()

                    } else {
                        val builder = AlertDialog.Builder(this@FuKuanActivity).setTitle("提示信息").setMessage("余额不足，是否充值").setNegativeButton("是") { dialog, which -> startActivityForResult(Intent(this@FuKuanActivity, RechargeActivity::class.java), 1) }.setNeutralButton("否") { dialog, which -> dialog.dismiss() }
                        val dialog1 = builder.create()
                        //                                dialog1.setInverseBackgroundForced(false);
                        dialog1.show()
                    }
                }
            }.setNeutralButton("取消") { dialog, which -> dialog.dismiss() }
            val dialog = builder.create()
            //                dialog.setInverseBackgroundForced(false);
            dialog.show()
        }
        val bundle = intent.extras
        if (bundle!!.containsKey("ZJXS")) {
            title.text = "增加悬赏"
            /**
             * * userid 用户ID
             * vkuserip 登录cookieIPT
             * vktoken 登录加密串
             * projectId 项目ID
             * addmoney 加价金额
             * addtime 增加天数
             * type 是安卓app还是IOS版app支付 ，=an 表示安卓app支付
             */
            val list = bundle.getStringArrayList("ZJXS")
            if (list != null) {
                id = list[0] as String
                map["projectId"] = list[0]
                mBianhao!!.text = "项目编号：" + list[0]
                if (LhtTool.isLogin) {
                    map["userid"] = MyApplication.userInfo!!.userID
                    map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
                    map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
                    map["type"] = "an"
                    map["addmoney"] = list[1]
                    if (list.size == 3) {
                        map["addtime"] = list[2]
                    }
                    mBalance!!.text = Html.fromHtml("余额  <font color='#cccccc'>（可用余额：￥" + MyApplication.userInfo!!.balance + "）</font>")
                    OkhttpTool.getOkhttpTool().post(UrlConfig.GET_ITEM_ADD_TO_PAY, map, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {

                            LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)

                        }


                        override fun onResponse(call: Call, response: Response) {
                            try {
                                val s = response.body()!!.string()
                                LogUtils.d("=============response:$s")
                                itemAddToPay = Gson().fromJson(s, ItemAddToPay::class.java)
                                hd.sendEmptyMessage(ADDTOPAY)

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }


                        }
                    })
                    ld = LoadingDialog(this@FuKuanActivity).setMessage("加载中.....")
                    ld!!.show()
                }
            }
        } else if (bundle.containsKey("ZB")) {
            map["ItemId"] = bundle.getString("ID")!!
            if (LhtTool.isLogin) {
                map["UserId"] = MyApplication.userInfo!!.userID
                map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
                map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
            }
            map["type"] = "an"
            map["fenqi_id"] = bundle.getString("FENQI")!!
            LogUtils.d("ItemId:${bundle.getString("ID")} \n UserId: ${MyApplication.userInfo?.userID} \n fenqi_id: ${bundle.getInt("FENQI")}")
            OkhttpTool.getOkhttpTool().post(UrlConfig.GET_ITEMPAY, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                }


                override fun onResponse(call: Call, response: Response) {
                    try {
                        val s = response.body()!!.string()
                        LogUtils.d("response: $s")
                        itemPayGson = Gson().fromJson(s, ItemPayGson::class.java)
                        hd.sendEmptyMessage(ITEMPAY)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }



                }
            })

            ld = LoadingDialog(this@FuKuanActivity).setMessage("加载中.....")
            ld!!.show()

        } else {
            val id = bundle.getString("ID")
            if (id != null) {
                map["ItemId"] = id
                if (LhtTool.isLogin) {
                    map["UserId"] = MyApplication.userInfo!!.userID
                    map["vkuserid"] = MyApplication.userInfo!!.userID
                    map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
                    map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
                    map["type"] = "an"
                    OkhttpTool.getOkhttpTool().post(UrlConfig.GET_ITEMPAY, map, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                        }


                        override fun onResponse(call: Call, response: Response) {
                            try {
                                val s = response.body()!!.string()
                                LogUtils.d("============response:$s")
                                itemPayGson = Gson().fromJson(s, ItemPayGson::class.java)
                                hd.sendEmptyMessage(ITEMPAY)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }



                        }
                    })
                    ld = LoadingDialog(this@FuKuanActivity).setMessage("加载中.....")
                    ld!!.show()

                }

            }
        }

    }

}
