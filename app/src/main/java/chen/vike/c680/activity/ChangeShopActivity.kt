package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutCompat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import chen.vike.c680.bean.MyShopGson
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.views.LodaWindow
import chen.vike.c680.views.MyGridView
import com.lht.vike.a680_v1.R
import com.lljjcoder.citypickerview.widget.CityPicker
import kotterknife.bindView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

/*
 * Created by lht on 2017/3/14.
 * 修改店铺
 */

class ChangeShopActivity : BaseStatusBarActivity() {

    val suozaiDiqu: TextView by bindView(R.id.suozai_diqu)
    val guoqu: ImageView by bindView(R.id.guoqu)
    val updateDianpuDiqu: RelativeLayout by bindView(R.id.update_dianpu_diqu)
    val dianpuLeixing: TextView by bindView(R.id.dianpu_leixing)
    val updateDianpuLeixing: RelativeLayout by bindView(R.id.update_dianpu_leixing)
    val dianpuName: TextView by bindView(R.id.dianpu_name)
    val updateDianpuName: RelativeLayout by bindView(R.id.update_dianpu_name)
    val jinengXuanze: TextView by bindView(R.id.jineng_xuanze)
    val openShopSkill: RelativeLayout by bindView(R.id.open_shop_skill)
    val shopNa: TextView by bindView(R.id.shop_na)
    val shopLinrar: LinearLayout by bindView(R.id.shop_linrar)
    val gridView: MyGridView by bindView(R.id.gridView)
    val openShopSk: LinearLayout by bindView(R.id.open_shop_sk)
    val shopDescrible: EditText by bindView(R.id.shop_describle)
    val foucuseLinear: LinearLayout by bindView(R.id.foucuse_linear)
    val shopNowBut: Button by bindView(R.id.shop_nowBut)
    private val map = HashMap<String, Any>()
    private val citty: Array<String>? = null
    private var myShopGson: MyShopGson? = null
    private val Stringlist = ArrayList<String>()
    private val StringClassId = ArrayList<String>()
    private var Stringid: String? = null
    private val foucuse_linear: LinearLayout? = null
    private var stringcheck: String? = null
    private val ld: LoadingDialog? = null
    private var lod: LodaWindow? = null
    private val GETINFO = 0x123
    private val SURE_CHANGE = 0x132
    private val NETWORKEXCEPTION = 0X111
    private var sID = ""
    private var context: Context? = null


    /**
     * 弹窗
     */
    private var viewShow: View? = null
    private var popupWindow: PopupWindow? = null

    /**
     * 弹窗数据处理
     */
    private var leixing_geren: RelativeLayout? = null
    private var leixing_gongzuoshi: RelativeLayout? = null
    private var leixing_qiye: RelativeLayout? = null

    /**
     * 选择地区
     */
    private var province: String? = null
    private var city: String? = null

    internal var adapter: BaseAdapter = object : BaseAdapter() {
        override fun getCount(): Int {
            return Stringlist.size
        }

        override fun getItem(position: Int): Any {
            return Stringlist[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var mConvertView = convertView
            val mHolder: ViewHodler
            if (mConvertView == null) {
                mHolder = ViewHodler()
                mConvertView = LayoutInflater.from(this@ChangeShopActivity).inflate(R.layout.openshop, null)
                mHolder.openShop = mConvertView!!.findViewById(R.id.open_shop)
                mConvertView.tag = mHolder
            } else {
                mHolder = mConvertView.tag as ViewHodler
            }
            for (i in Stringlist.indices) {
                //  Log.e("jineng",Stringlist.get(i));
            }

            mHolder.openShop!!.text = Stringlist[position]
            mHolder.openShop!!.setOnLongClickListener {
                Stringlist.removeAt(position)
                StringClassId.removeAt(position)
                if (Stringlist.size == 0) {
                    shopLinrar.visibility = View.GONE
                    openShopSk.visibility = View.GONE
                }
                this.notifyDataSetChanged()
                true
            }

            return mConvertView
        }

        inner class ViewHodler {
            var openShop: TextView? = null
        }
    }

    @SuppressLint("HandlerLeak")
    private val hd = object : Handler() {
        @SuppressLint("SetTextI18n")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == GETINFO) {
                dianpuName.text = myShopGson!!.shopInfo?.shop_name
                shopDescrible.setText(myShopGson!!.shopInfo?.shop_des)
                shopDescrible.setSelection(shopDescrible.text.length)
                if (myShopGson!!.shopInfo?.shop_type == "10") {
                    dianpuLeixing.text = "个人"
                } else if (myShopGson!!.shopInfo?.shop_type == "12") {
                    dianpuLeixing.text = "工作室"
                } else {
                    dianpuLeixing.text = "企业"
                }
                stringcheck = dianpuLeixing.text.toString() + ""
                if (myShopGson!!.shopInfo?.province == "") {
                    suozaiDiqu.text = ""
                } else {
                    suozaiDiqu.text = myShopGson!!.shopInfo?.province + "-" + myShopGson!!.shopInfo?.city
                }
                var a: Array<String>? = null
                if (myShopGson!!.class_list!!.isNotEmpty()) {
                    a = myShopGson!!.class_list!![0].classname?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }!!.toTypedArray()
                    if (Stringlist.size > 0) {
                        Stringlist.clear()
                    }
                    for (i in 1 until a.size) {
                        Stringlist.add(a[i])
                    }

                    if (Stringlist.size == 0) {
                        shopLinrar.visibility = View.GONE
                        openShopSk.visibility = View.GONE
                    } else {
                        openShopSk.visibility = View.VISIBLE


                    }
                    val aa = myShopGson!!.class_list!![0].classid!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    var l: String? = null
                    for (k in 1 until aa.size) {
                        if (k == 1) {
                            l = aa[k]
                            StringClassId.add(aa[k])
                        } else {
                            l = l + "," + aa[k]
                            StringClassId.add(aa[k])
                        }
                    }
                    Stringid = l
                }
                gridView.adapter = adapter
                adapter.notifyDataSetChanged()

            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(this@ChangeShopActivity, msg)

            } else if (msg.what == SURE_CHANGE) {
                val s = msg.data.getString("s")
                when (s) {
                    "nouser" -> CustomToast.showToast(this@ChangeShopActivity, "用户不存在", Toast.LENGTH_LONG)
                    "stop" -> CustomToast.showToast(this@ChangeShopActivity, "账户被禁用", Toast.LENGTH_LONG)
                    "noverify" -> CustomToast.showToast(this@ChangeShopActivity, "用户未进行实名认证", Toast.LENGTH_LONG)
                    "noshopnam" -> CustomToast.showToast(this@ChangeShopActivity, "未提供店铺名称", Toast.LENGTH_LONG)
                    "isshop" -> CustomToast.showToast(this@ChangeShopActivity, "店铺已开通", Toast.LENGTH_LONG)
                    "noprovname" -> CustomToast.showToast(this@ChangeShopActivity, "未提供省份名称", Toast.LENGTH_LONG)
                    "nocityname" -> CustomToast.showToast(this@ChangeShopActivity, "未提供城市名称", Toast.LENGTH_LONG)
                    "hasshopname" -> CustomToast.showToast(this@ChangeShopActivity, "店铺名称已存在", Toast.LENGTH_LONG)
                    "noshopdes" -> CustomToast.showToast(this@ChangeShopActivity, "未提店铺简介", Toast.LENGTH_LONG)
                    "edit_ok" -> {
                        finish()
                        CustomToast.showToast(this@ChangeShopActivity, "店铺修改成功", Toast.LENGTH_LONG)
                    }
                    "edit_fail" -> CustomToast.showToast(this@ChangeShopActivity, "店铺修改失败", Toast.LENGTH_LONG)
                }
            }
            lod?.dis()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myshopchangeactivity)
        context = this
        title.text = "店铺修改"
        shopNowBut.setOnClickListener {
            //修改店铺提交

            for (i in StringClassId.indices) {
                sID = sID + StringClassId[i] + ","
            }
            Log.e("jineng", sID)
            ChangeShop()
        }
        openShopSkill.setOnClickListener {
            //修改技能
            val intent = Intent(this@ChangeShopActivity, OpenShopSkillsActivity::class.java)
            startActivityForResult(intent, 10)
        }
        //因为需要点击两次EditText才能获得焦点所以这里用的OnTouchListener
//        foucuseLinear.setOnTouchListener(object : View.OnTouchListener{
//            var touchFlag:Int = 0
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                touchFlag++
//                if (touchFlag==2){
//                    touchFlag
//                    val imm = shopDescrible.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
//                }
//                return false
//            }
//        })
//        foucuseLinear.setOnClickListener {
//            val imm = shopDescrible.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
//        }
        getShopInfo()
        viewListener()
    }

    /**
     * 控件监听
     */
    private fun viewListener() {
        updateDianpuLeixing.setOnClickListener { showWindows() }
        updateDianpuName.setOnClickListener {
            val intent = Intent(context, ChangeShopNameActivity::class.java)
            intent.putExtra("dianpuname", dianpuName.text.toString() + "")
            startActivityForResult(intent, 789)
        }
        updateDianpuDiqu.setOnClickListener { selectAddress() }
    }

    private fun showWindows() {
        viewShow = LayoutInflater.from(context).inflate(R.layout.dianpu_leixing_window_page, null)
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
        setWindView()
    }


    private fun setWindView() {
        leixing_geren = viewShow!!.findViewById(R.id.leixing_geren)
        leixing_gongzuoshi = viewShow!!.findViewById(R.id.leixing_gongzuoshi)
        leixing_qiye = viewShow!!.findViewById(R.id.leixing_qiye)
        leixing_geren!!.setOnClickListener {
            popupWindow!!.dismiss()
            dianpuLeixing.text = "个人"
            stringcheck = "个人"
        }
        leixing_gongzuoshi!!.setOnClickListener {
            popupWindow!!.dismiss()
            dianpuLeixing.text = "工作室"
            stringcheck = "工作室"
        }
        leixing_qiye!!.setOnClickListener {
            popupWindow!!.dismiss()
            dianpuLeixing.text = "企业"
            stringcheck = "企业 "
        }
    }

    private fun selectAddress() {
        val cityPicker = CityPicker.Builder(this@ChangeShopActivity)
                .textSize(16)
                .title("地址选择")
                .titleBackgroundColor("#FFFFFF")
                .confirTextColor("#fa4242")
                .cancelTextColor("#fa4242")
                //.backgroundPop(Color.parseColor("#50ffffff"))
                .province(myShopGson!!.shopInfo!!.province + "")
                .city(myShopGson!!.shopInfo!!.city + "")
                .district("")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(6)
                .itemPadding(10)
                .onlyShowProvinceAndCity(true)
                .build()
        cityPicker.show()
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(object : CityPicker.OnCityItemClickListener {
            @SuppressLint("SetTextI18n")
            override fun onSelected(vararg citySelected: String) {
                //省份
                province = citySelected[0]
                //城市
                city = citySelected[1]
                //区县（如果设定了两级联动，那么该项返回空）
                val district = citySelected[2]
                //邮编
                val code = citySelected[3]
                //为TextView赋值
                suozaiDiqu.text = province!!.trim { it <= ' ' } + "-" + city!!.trim { it <= ' ' } + "" + district.trim { it <= ' ' }
            }

            override fun onCancel() {

            }
        })
    }

    //获取店铺信息
    private fun getShopInfo() {
        if (LhtTool.isLogin) {
//            lod = LodaWindow(this@ChangeShopActivity,this@ChangeShopActivity)
//            lod?.setMessage("加载中...")

            map["userid"] = MyApplication.userInfo!!.userID
            OkhttpTool.getOkhttpTool().post(UrlConfig.MY_SHOP_INFO, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                    lod?.dis()
                }

                override fun onResponse(call: Call, response: Response) {
                    try{
                        val s = response.body()!!.string()
                        LogUtils.d("店铺信息： \n $s")
                        myShopGson = Gson().fromJson(s, MyShopGson::class.java)
                        if (myShopGson?.shopInfo != null && myShopGson != null) {
                            hd.sendEmptyMessage(GETINFO)
                        } else {
                            lod?.dis()
                            CustomToast.showToast(context, "网络错误,请重试", Toast.LENGTH_SHORT)
                            this@ChangeShopActivity.finish()
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            })
        }else{
            this@ChangeShopActivity.startActivity(Intent(this@ChangeShopActivity, UserLoginActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lod?.dis()
    }


    //修改店铺信息
    fun ChangeShop() {

        if (!dianpuName.text.toString().isEmpty()) {
            if (!shopDescrible.text.toString().isEmpty()) {
                if (Stringlist.size > 0) {
                    //店铺类型
                    if (LhtTool.isLogin) {
                        map["userid"] = MyApplication.userInfo!!.userID
                        map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
                        map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
                        if (stringcheck == "个人") {
                            map["shop_type"] = "10"
                        } else if (stringcheck == "工作室") {
                            map["shop_type"] = "12"
                        } else {
                            map["shop_type"] = "13"
                        }
                        map["type"] = "1"
                        //店铺名称
                        map["shop_name"] = dianpuName.text.toString()
                        //店铺描述
                        map["shop_des"] = shopDescrible.text.toString()
                        //店铺技能
                        map["jineng"] = sID
                        //地址
                        map["province"] = province?:myShopGson!!.shopInfo!!.province!!
                        map["city"] = city?:myShopGson!!.shopInfo!!.city!!

                        OkhttpTool.getOkhttpTool().post(UrlConfig.OPEN_SHOP, map, object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                            }

                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {
                                val s = response.body()!!.string()
                                LogUtils.d("店铺修改结果： \n $s")
                                val ms = Message()
                                ms.what = SURE_CHANGE
                                val b = Bundle()
                                b.putString("s", s)
                                ms.data = b
                                hd.sendMessage(ms)
                            }
                        })
                        lod = LodaWindow(this, this)
                        lod!!.setMessage("修改中....")
                    }

                } else {
                    CustomToast.showToast(this, "请选择技能", Toast.LENGTH_SHORT)
                }
            } else {
                CustomToast.showToast(this, "请填写店铺的描述", Toast.LENGTH_SHORT)
            }
        } else {
            CustomToast.showToast(this, "请填写店铺的名称", Toast.LENGTH_SHORT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //不能选不同类的东西，未做处理，且超过5个也没做处理
        if (resultCode == 10) {
            //                显示两个linear
            if (OpenShopSkillsActivity.nalist.size == 0) {
                shopLinrar.visibility = View.GONE
                openShopSk.visibility = View.GONE
            } else {
                Stringlist.addAll(OpenShopSkillsActivity.nalist)
                StringClassId.addAll(OpenShopSkillsActivity.xid)
                if (Stringlist.size > 5) {
                    val x = Stringlist.size - 5
                    for (i in 0 until x) {
                        Stringlist.removeAt(i)
                    }
                }
                shopLinrar.visibility = View.VISIBLE
                openShopSk.visibility = View.VISIBLE
            }
            gridView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        if (requestCode == 789) {
            if (!data?.getStringExtra("dianpuname").isNullOrEmpty()) {
                dianpuName.text = data?.getStringExtra("dianpuname")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
