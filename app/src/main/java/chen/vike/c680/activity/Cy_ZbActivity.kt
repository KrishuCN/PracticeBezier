package chen.vike.c680.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.*
import com.blankj.utilcode.util.LogUtils
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import com.lht.vike.a680_v1.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by lht on 2017/3/17.
 *
 * 招标参与
 */

class Cy_ZbActivity : BaseStatusBarActivity() {

    private var mContent: EditText? = null
    private var mMoney: EditText? = null
    private var mTime: EditText? = null
    private var mControl: Button? = null
    private val map = HashMap<String, Any>()
    private var province_spinner: Spinner? = null
    private var city_spinner: Spinner? = null
    private val city = intArrayOf(R.array.beijin_province_item, R.array.tianjin_province_item, R.array.heibei_province_item, R.array.shanxi1_province_item, R.array.neimenggu_province_item, R.array.liaoning_province_item, R.array.jilin_province_item, R.array.heilongjiang_province_item, R.array.shanghai_province_item, R.array.jiangsu_province_item, R.array.zhejiang_province_item, R.array.anhui_province_item, R.array.fujian_province_item, R.array.jiangxi_province_item, R.array.shandong_province_item, R.array.henan_province_item, R.array.hubei_province_item, R.array.hunan_province_item, R.array.guangdong_province_item, R.array.guangxi_province_item, R.array.hainan_province_item, R.array.chongqing_province_item, R.array.sichuan_province_item, R.array.guizhou_province_item, R.array.yunnan_province_item, R.array.xizang_province_item, R.array.shanxi2_province_item, R.array.gansu_province_item, R.array.qinghai_province_item, R.array.linxia_province_item, R.array.xinjiang_province_item, R.array.hongkong_province_item, R.array.aomen_province_item, R.array.taiwan_province_item)
    private var ld: LoadingDialog? = null
    private var provinceId: Int? = null
    private val cityId: Int? = null
    private var strProvince: String? = null
    private var strCity: String? = null
    private val strCounty: String? = null
    private val SURE_BAOJIA = 0x123
    private val NETWORKEXCEPTION = 0x111
    private val mHandler = MHandler(this@Cy_ZbActivity)
    class MHandler constructor(activity: Cy_ZbActivity): Handler() {
        var weakReference = WeakReference<Cy_ZbActivity>(activity)
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == weakReference.get()?.NETWORKEXCEPTION) {

                LhtTool.showNetworkException(weakReference.get(), msg)

            } else if (msg?.what == weakReference.get()?.SURE_BAOJIA) {
                weakReference.get()?.ld?.dismiss()
                val s = msg?.data?.getString("s")
                when (s) {
                    "unlogin" -> CustomToast.showToast(weakReference.get(), "未登录", Toast.LENGTH_LONG)
                    "bjlow" -> CustomToast.showToast(weakReference.get(), "报价太低，请提高点吧", Toast.LENGTH_LONG)
                    "uncheck" -> CustomToast.showToast(weakReference.get(), "此项目未审核", Toast.LENGTH_LONG)
                    "bjover" -> CustomToast.showToast(weakReference.get(), "已截止报价", Toast.LENGTH_LONG)
                    "itemover" -> CustomToast.showToast(weakReference.get(), "项目已结束", Toast.LENGTH_LONG)
                    "dateout" -> CustomToast.showToast(weakReference.get(), "项目已结束", Toast.LENGTH_LONG)
                    "hasbj" -> CustomToast.showToast(weakReference.get(), "您已提交过报价", Toast.LENGTH_LONG)
                    "noshop" -> CustomToast.showToast(weakReference.get(), "还没开通店铺，不能报价", Toast.LENGTH_LONG)
                    "haszhongbiao" -> CustomToast.showToast(weakReference.get(), "此项目已评标了", Toast.LENGTH_LONG)
                    "fail" -> CustomToast.showToast(weakReference.get(), "提交报价失败", Toast.LENGTH_LONG)
                    "mustvip" -> CustomToast.showToast(weakReference.get(), "您暂不能参与该项目；签约VIP服务商现在可参与", Toast.LENGTH_LONG)
                    "nocon" -> CustomToast.showToast(weakReference.get(), "请填写内容", Toast.LENGTH_LONG)
                    "noarea" -> CustomToast.showToast(weakReference.get(), "请选择地区", Toast.LENGTH_LONG)
                    "noday" -> CustomToast.showToast(weakReference.get(), "请填写完成周期", Toast.LENGTH_LONG)
                    "err" -> CustomToast.showToast(weakReference.get(), "提交错误", Toast.LENGTH_LONG)
                    "ok" -> {
                        CustomToast.showToast(weakReference.get(), "提交成功", Toast.LENGTH_LONG)
                        weakReference.get()?.finish()
                    }
                    else -> CustomToast.showToast(weakReference.get(), "null", Toast.LENGTH_LONG)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zb_cy)

        title.text = "参与报价"
        iniView()//初始化
        val bundle = intent.extras
        if (bundle != null) {
            val id = bundle.getString("ID")
            map["itemid"] = id!!
        }
        viewListener()//控件监听事件

        val province_adapter = ArrayAdapter.createFromResource(this, R.array.province_item, android.R.layout.simple_spinner_item)
        province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        province_spinner!!.adapter = province_adapter
        province_spinner!!.prompt = "请选择省份"

    }

    /**
     * 初始化
     */
    private fun iniView() {
        mContent = findViewById(R.id.cy_content)
        mMoney = findViewById(R.id.mMoney)
        mTime = findViewById(R.id.mTime)
        province_spinner = findViewById(R.id.province_spinner)
        city_spinner = findViewById(R.id.city_spinner)
        mControl = findViewById(R.id.control)
    }

    /**
     * view控件监听事件
     */
    private fun viewListener() {
        mControl!!.setOnClickListener {
            if (mContent!!.text.toString().isEmpty()) {
                CustomToast.showToast(this@Cy_ZbActivity, "请输入你要提交的内容", Toast.LENGTH_LONG)
            } else {
                map["crv_content"] = mContent!!.text
                if (mMoney!!.text.toString().isEmpty()) {
                    CustomToast.showToast(this@Cy_ZbActivity, "金额", Toast.LENGTH_LONG)

                } else {
                    map["baojiao_money"] = mMoney!!.text
                    if (mTime!!.text.toString().isEmpty()) {
                        CustomToast.showToast(this@Cy_ZbActivity, "完成周期", Toast.LENGTH_LONG)

                    } else {
                        map["baojiao_day"] = mTime!!.text
                        if (LhtTool.isLogin) {
                            map["userid"] = MyApplication.userInfo!!.userID
                            map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
                            map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
                            map["provinceid"] = strProvince!!
                            map["cityid"] = strCity!!
                            ld = LoadingDialog(this@Cy_ZbActivity).setMessage("提交中....")
                            ld!!.show()
                            OkhttpTool.getOkhttpTool().post(UrlConfig.BAOJIA_JION, map, object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    LhtTool.sendMessage(mHandler, e, NETWORKEXCEPTION)
                                }

                                @Throws(IOException::class)
                                override fun onResponse(call: Call, response: Response) {

                                    val s = response.body()!!.string()
                                    LogUtils.d("================response:$s")
                                    val ms = Message()
                                    ms.what = SURE_BAOJIA
                                    val b = Bundle()
                                    b.putString("s", s)
                                    ms.data = b
                                    mHandler.sendMessage(ms)

                                }
                            })
                        } else {
                            val intent = Intent(this@Cy_ZbActivity, UserLoginActivity::class.java)
                            startActivity(intent)
                        }

                    }
                }
            }
        }
        province_spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                provinceId = province_spinner!!.selectedItemPosition
                strProvince = province_spinner!!.selectedItem.toString()
                city_spinner!!.prompt = "请选择城市"
                select(city_spinner, city[provinceId!!])
                city_spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        strCity = city_spinner!!.selectedItem.toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


    }

    /**
     * 选择城市
     * @param spin
     * @param arry
     */
    fun select(spin: Spinner?, arry: Int) {
        val adapter = ArrayAdapter.createFromResource(this, arry, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin!!.adapter = adapter
    }

}
