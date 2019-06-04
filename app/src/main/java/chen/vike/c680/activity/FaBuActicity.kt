package chen.vike.c680.activity

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.text.Html
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import chen.vike.c680.bean.ItemInfoGson
import chen.vike.c680.bean.UpLoadGson
import chen.vike.c680.bean.XiangMuFenLeiGson
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.*
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.views.MyGridView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lht.vike.a680_v1.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lht on 2017/3/16.
 *
 *
 * 发布需求
 */

class FaBuActicity : BaseStatusBarActivity() {

    private var mLxfs: ImageView? = null
    private var xq_fujian: ImageView? = null
    private var mContent: EditText? = null
    private var mPhone: EditText? = null
    private var button: Button? = null
    private var money: EditText? = null
    private var jjnumber: EditText? = null
    private var jjmoney: EditText? = null
    private var fbxq_lm: TextView? = null
    private var fabu_ts: TextView? = null
    private val strings = ArrayList<String>()
    private val map = HashMap<String, Any>()
    private val list = ArrayList<XiangMuFenLeiGson.ListBean>()
    private val list1 = ArrayList<XiangMuFenLeiGson.ListBean>()
    private var id: String? = null
    private var itemInfoGson: ItemInfoGson? = null
    private var upLoadGson: UpLoadGson? = null
    private var ld: LoadingDialog? = null
    private var pop: PopupWindow? = null
    private var popMoney: PopupWindow? = null
    private var takePhoto: TextView? = null
    private var pickPhoto: TextView? = null
    private var Canacel: TextView? = null

    private var money_one: View? = null
    private var money_two: View? = null
    private var money_three: View? = null
    private var money_four: View? = null
    private var mImagePath: String? = null
    private var imageFileUri: Uri? = null
    private var photoPath: String? = null
    private var UrlImag: String? = null
    private var fjj: View? = null
    private var jijiandanjia: View? = null
    private var jijiantishi: View? = null
    private var jj: View? = null
    private var ll_xs_zb: LinearLayout? = null
    private var rg_xs_zb: RadioGroup? = null
    private var ll_cnxg: LinearLayout? = null
    private var cnxg: CheckBox? = null
    private var tishi: CheckBox? = null
    private val metrics = DisplayMetrics()
    private val GET_INFO = 0x123
    private val NETWORK_EXCEPTION = 0x111
    private val SURE_XQ = 0x121
    private val UPLOAD = 0x122

    private var xuqiuMoney: TextView? = null
    private var vMoney: View? = null
    private var isMoney = true
    private var isjj = true
    private val hd = MHandler(this)

    /**
     * 存储页面数据
     */
    private var userid = ""
    private var pagename = ""
    private var pageindex = ""
    private var pageinittime = ""
    private var dataStatisticsBean = DataStatisticsBean()


    /**
     * 提交需求数据
     */
    lateinit var sp: SharedPreferences

    internal var adapter: BaseAdapter = object : BaseAdapter() {
        override fun getCount(): Int {
            return strings.size
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val imageView = ImageView(this@FaBuActicity)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.layoutParams = AbsListView.LayoutParams(metrics.widthPixels / 10, metrics.widthPixels / 10)

            val options = RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)

            Glide.with(this@FaBuActicity).load(strings[position]).apply(options).into(imageView)
            return imageView
        }
    }

    private class MHandler(activity: FaBuActicity) : Handler() {
        private val weakReference: WeakReference<FaBuActicity> = WeakReference(activity)
        private val activity: FaBuActicity? = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            activity?.run {
                if (msg?.what == 0) {

                } else if (msg?.what == NETWORK_EXCEPTION) {
                    LhtTool.showNetworkException(this, msg)
                } else if (msg?.what == GET_INFO) {

                    if (null != itemInfoGson) {

                        for (i in list.indices) {
                            if (list1[i].classID == itemInfoGson!!.item_class2id) {
                                fbxq_lm!!.text = list1[i].className
                            }
                        }

                        map["class2id"] = itemInfoGson!!.item_class2id
                        mPhone!!.setText(itemInfoGson!!.contactme)
                        mContent!!.setText(Html.fromHtml(itemInfoGson!!.item_con))
                        if (itemInfoGson!!.item_class1id == "6") {
                            val s = itemInfoGson!!.item_zb_fenpei.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            jjnumber!!.setText(s[0])
                            jjmoney!!.setText(s[1])
                            rg_xs_zb!!.check(R.id.xs_zb_rb2)

                        } else {
                            if (itemInfoGson!!.item_type == "1") {
                                money!!.setText(itemInfoGson!!.item_zab_yusuan1)
                            } else {
                                money!!.setText(itemInfoGson!!.item_money)
                            }
                        }
                    }

                } else if (msg?.what == SURE_XQ) {

                    val s = msg.data.getString("s")
                    when (s) {
                        "unlogin" -> CustomToast.showToast(this, "请登录后操作", Toast.LENGTH_LONG)
                        "unclass1id" -> CustomToast.showToast(this, "请选择大分类", Toast.LENGTH_LONG)
                        "unclass2id" -> CustomToast.showToast(this, "请选择小分类", Toast.LENGTH_LONG)
                        "nocon" -> CustomToast.showToast(this, "请填写项目描述", Toast.LENGTH_LONG)
                        "jjnonum" -> CustomToast.showToast(this, "请填写件数", Toast.LENGTH_LONG)
                        "jjnoprice" -> CustomToast.showToast(this, "单价不能低于0.3元", Toast.LENGTH_LONG)
                        "noitemmoney" -> CustomToast.showToast(this, "请填写预算金额", Toast.LENGTH_LONG)
                        "unbindmobile" -> {
                            CustomToast.showToast(this, "请先绑定手机", Toast.LENGTH_LONG)
                            var intent = Intent(this, MobileYanZhengActivity::class.java)
                            dataStatisticsBean.pagenext = "详细需求——绑定手机页面"
                            stopStorage()
                            startActivity(intent)
                        }
                        "null_phone" -> CustomToast.showToast(this, "请输入手机号码", Toast.LENGTH_LONG)
                        "less50" -> CustomToast.showToast(this, "预算金额不得低于100元", Toast.LENGTH_LONG)
                        "fabufail" -> CustomToast.showToast(this, "发布(或修改)项目失败", Toast.LENGTH_LONG)
                        else -> {
                            finish()
                            CustomToast.showToast(this, "项目发布成功", Toast.LENGTH_LONG)
                            LhtTool.isFaLogin = "0"
                            intent = Intent(this, FaBuHouActivity::class.java)
                            intent.putExtra("ID", s)
                            dataStatisticsBean.pagenext = "详细需求——付款页面"
                            stopStorage()
                            startActivity(intent)
                        }
                    }
                } else if (msg?.what == UPLOAD) {
                    if (upLoadGson!!.error == 1) {
                        CustomToast.showToast(this, "上传失败", Toast.LENGTH_LONG)
                    } else {
                        UrlImag = upLoadGson!!.url
                        strings.add(upLoadGson!!.url)
                        adapter.notifyDataSetChanged()
                    }
                }
                ld?.dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowManager.defaultDisplay.getMetrics(metrics)
        setContentView(R.layout.activity_fabu_xuqiu)
        sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        xuqiuMoney = findViewById(R.id.xuqiu_money)
        mLxfs = findViewById(R.id.fbxq_lxfs)
        xq_fujian = findViewById(R.id.xq_fujian)
        mContent = findViewById(R.id.xq_content)
        mPhone = findViewById(R.id.fbxq_sjhm)
        money = findViewById(R.id.fbxq_ys)
        jjnumber = findViewById(R.id.jijian_number)
        jjmoney = findViewById(R.id.jijian_jiage)
        ll_cnxg = findViewById(R.id.ll_cnxg)
        ll_xs_zb = findViewById(R.id.ll_xs_and_zb)
        rg_xs_zb = findViewById(R.id.xs_zb_rg)
        cnxg = findViewById(R.id.cnxg)
        tishi = findViewById(R.id.tishi)
        pop = PopupWindow(this@FaBuActicity)
        popMoney = PopupWindow(this@FaBuActicity)
        val v = LayoutInflater.from(this@FaBuActicity).inflate(R.layout.selectview, null)
        vMoney = LayoutInflater.from(this@FaBuActicity).inflate(R.layout.fubu_xuqiu_money_window_item, null)
        takePhoto = v.findViewById(R.id.btn_take_photo)
        pickPhoto = v.findViewById(R.id.btn_pick_photo)
        Canacel = v.findViewById(R.id.btn_cancel)
        money_one = vMoney!!.findViewById(R.id.xuqiu_money_one)
        money_two = vMoney!!.findViewById(R.id.xuqiu_money_two)
        money_three = vMoney!!.findViewById(R.id.xuqiu_money_three)
        money_four = vMoney!!.findViewById(R.id.xuqiu_money_four)
        if (LhtTool.isLogin) {
            if (MyApplication.userInfo != null && MyApplication.userInfo!!.phone != null) {
                mPhone!!.setText(MyApplication.userInfo!!.phone + "")
            }
        }
        money_one!!.setOnClickListener {
            xuqiuMoney!!.text = "2000元以下"
            popMoney!!.dismiss()
        }
        money_two!!.setOnClickListener {
            xuqiuMoney!!.text = "2000-10000元"
            popMoney!!.dismiss()
        }
        money_three!!.setOnClickListener {
            xuqiuMoney!!.text = "10000-50000元"
            popMoney!!.dismiss()
        }
        money_four!!.setOnClickListener {
            xuqiuMoney!!.text = "50000元以上"
            popMoney!!.dismiss()
        }
        takePhoto!!.setOnClickListener {
            pop?.dismiss()
            XXPermissions.with(this).permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                    .request(object : OnPermission {
                        override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                            ToastUtils.showShort("需要存储和拍照权限")
                        }

                        override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                            if (isAll) {
                                takePhoto()
                            } else {
                                ToastUtils.showShort("需要存储和拍照权限")
                            }
                        }
                    })
        }

        pickPhoto!!.setOnClickListener {
            pop?.dismiss()
            XXPermissions.with(this).permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                    .request(object : OnPermission {
                        override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                            ToastUtils.showShort("需要存储和拍照权限")
                        }

                        override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                            if (isAll) {
                                openPickPhoto()
                            } else {
                                ToastUtils.showShort("需要存储和拍照权限")
                            }
                        }
                    })
        }

        Canacel!!.setOnClickListener { pop!!.dismiss() }


        pop!!.contentView = v
        pop!!.height = (DisplayUtil.getMobileWidth(this) * 0.4).toInt()
        pop!!.width = (DisplayUtil.getMobileHeight(this) * 0.5).toInt()
        pop!!.animationStyle = R.style.popWindow_animation
        //设置SelectPicPopupWindow弹出窗体的背景
        pop!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        pop!!.isFocusable = true
        pop!!.isOutsideTouchable = true

        dabuTongji()//后台统计
        fjj = findViewById(R.id.fjj)
        jijiandanjia = findViewById(R.id.jijian_danjia)
        jijiantishi = findViewById(R.id.jijian_tishi)
        jj = findViewById(R.id.jj)
        button = findViewById(R.id.control)
        fbxq_lm = findViewById(R.id.fbxq_lm)
        fabu_ts = findViewById(R.id.fabu_ts)
        val xRecyclerView = findViewById<MyGridView>(R.id.listview)
        if (PickPhotoActivity.stringList.size > 0) {
            PickPhotoActivity.stringList.clear()
        }
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.getString("ID") != null) {
                id = bundle.getString("ID")
            } else {
                id = "1"
            }

            map["class1id"] = id!!
            if (bundle.getString("ItemID") != null) {
                map["itemId"] = bundle.getString("ItemID")!!
                map["type"] = "edit"
                map["loginUserId"] = MyApplication.userInfo!!.userID
                OkhttpTool.getOkhttpTool().post(UrlConfig.ITEM_INFO, map, object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                    }


                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val s = response.body()!!.string()
                            LogUtils.d("==================response:$s")
                            Log.e("wanshan", s)
                            itemInfoGson = Gson().fromJson(s, ItemInfoGson::class.java)
                            hd.sendEmptyMessage(GET_INFO)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            } else {
                if (LhtTool.isLogin) {
                    mPhone!!.setText(MyApplication.userInfo!!.cellPhone)
                }
            }
        }
        map["apptype"] = "an"
        title.setText(R.string.fbxq_msxq)
        fjj!!.setOnClickListener { moneyWindow() }
        mLxfs!!.setOnClickListener {
            try {
                LhtTool.Call(Intent.ACTION_CALL, "400–630-6800", this@FaBuActicity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        rg_xs_zb!!.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.xs_zb_rb) {
                ll_cnxg!!.visibility = View.VISIBLE
                xuqiuMoney!!.visibility = View.GONE
                money!!.visibility = View.VISIBLE
                jijiandanjia!!.visibility = View.GONE
                jijiantishi!!.visibility = View.GONE
                fjj!!.visibility = View.VISIBLE
                isMoney = true  //悬赏
            } else if (checkedId == R.id.xs_zb_rb1) {
                ll_cnxg!!.visibility = View.GONE
                money!!.visibility = View.GONE
                xuqiuMoney!!.visibility = View.VISIBLE
                jijiandanjia!!.visibility = View.GONE
                jijiantishi!!.visibility = View.GONE
                fjj!!.visibility = View.VISIBLE
                isMoney = false  //招标
            } else {
                fjj!!.visibility = View.GONE
                jijiandanjia!!.visibility = View.VISIBLE
                jijiantishi!!.visibility = View.GONE
                ll_cnxg!!.visibility = View.GONE
                isjj = false
            }
        }

        button!!.setOnClickListener { tijiaoXuqiu() }
        fbxq_lm!!.setOnClickListener {
            val intent = Intent(this@FaBuActicity, CatoryActivity::class.java)
            startActivityForResult(intent, 400)
        }
        xRecyclerView.adapter = adapter
        xq_fujian!!.setOnClickListener { pop!!.showAtLocation(findViewById(R.id.ll), Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0) }


    }

    public override fun onStart() {
        super.onStart()
        storage("雇主页面")
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

    private fun tijiaoXuqiu() {
        if (mContent!!.text.toString().isEmpty()) {
            CustomToast.showToast(this@FaBuActicity, "描述的需求不能为空", Toast.LENGTH_LONG)
        } else {
            map["crv_content"] = mContent!!.text
            var s = ""
            for (i in strings.indices) {
                if (s.isEmpty()) {
                    s = strings[i]
                } else {
                    s = s + "," + strings[i]
                }
            }
            map["fujianlist"] = s

            if (jjmoney!!.text.toString().isEmpty()) {
                //  CustomToast.showToast(FaBuActicity.this, "单价不能为空", Toast.LENGTH_LONG);
            } else {
                if (java.lang.Float.valueOf(jjmoney!!.text.toString()) < 0.5f) {
                    CustomToast.showToast(this@FaBuActicity, "单价不能低于0.3元", Toast.LENGTH_LONG)
                } else {
                    if (jjnumber!!.text.toString().isEmpty() || Integer.valueOf(jjnumber!!.text.toString()) == 0) {
                        CustomToast.showToast(this@FaBuActicity, "件数必须大于0", Toast.LENGTH_LONG)

                    } else {
                        map["jj_num"] = jjnumber!!.text
                        map["jj_price"] = jjmoney!!.text
                        if (mPhone!!.text.toString().isEmpty()) {
                            CustomToast.showToast(this@FaBuActicity, "手机号码不能为空", Toast.LENGTH_LONG)
                        } else if (mPhone!!.text.toString().length > 12) {
                            CustomToast.showToast(this@FaBuActicity, "输入正确的号码", Toast.LENGTH_LONG)
                        } else {
                            map["phone"] = mPhone!!.text
                            if (LhtTool.isLogin) {
                                map["userid"] = MyApplication.userInfo!!.userID
                                ld = LoadingDialog(this@FaBuActicity).setMessage("提交中....")
                                ld!!.show()
                                isDataNull()
                            } else {
                                LhtTool.isLoginFa = false
                                val editor = sp.edit()
                                val intent = Intent(this@FaBuActicity, UserLoginActivity::class.java)
                                // intent.putExtra("userphone", mPhone.getText());
                                editor.putString("USERPHONE", mPhone!!.text.toString())
                                dataStatisticsBean.pagenext = "详细需求——用户登录页面"
                                stopStorage()
                                startActivity(intent)
                            }
                        }
                    }
                }
            }

            if (isjj) {
                if (isMoney) {
                    if (money!!.text.toString().isEmpty()) {
                        CustomToast.showToast(this@FaBuActicity, "预算不能为空", Toast.LENGTH_LONG)
                    } else {
                        map["item_money"] = money!!.text
                        isDataNull()
                    }
                } else {
                    if (xuqiuMoney!!.text.toString().isEmpty()) {
                        CustomToast.showToast(this@FaBuActicity, "预算不能为空", Toast.LENGTH_LONG)
                    } else {
                        map["item_money"] = xuqiuMoney!!.text
                        isDataNull()
                    }
                }
            }

        }
    }

    private fun isDataNull() {
        //  map.put("item_money", xuqiuMoney.getText());
        if (mPhone!!.text.toString().isEmpty()) {
            CustomToast.showToast(this@FaBuActicity, "手机号码不能为空", Toast.LENGTH_LONG)
        } else {
            map["phone"] = mPhone!!.text
            var k = "-1"
            if (rg_xs_zb!!.checkedRadioButtonId == R.id.xs_zb_rb) {
                k = "0"
            } else if (rg_xs_zb!!.checkedRadioButtonId == R.id.xs_zb_rb1) {
                k = "1"
            } else {
                k = "2"
                isjj = false
            }
            map["itemtype"] = k
            var k1 = "1"
            var k2 = "1"
            if (cnxg!!.isChecked) {
                k1 = "1"
            } else {
                k1 = "0"
            }
            if (tishi!!.isChecked) {
                k2 = "1"
            } else {
                k2 = "0"
            }
            LogUtils.d("=============k1:$k1")
            map["is_100_xuanshang"] = k1
            map["is_100_jijian"] = k2
            if (LhtTool.isLogin) {
                map["userid"] = MyApplication.userInfo!!.userID
                ld = LoadingDialog(this@FaBuActicity).setMessage("提交中....")
                ld!!.show()
                getSureInfo()
            } else {
                LhtTool.isLoginFa = false
                val intent = Intent(this@FaBuActicity, UserLoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * 预算金额弹窗
     */
    private fun moneyWindow() {
        popMoney!!.contentView = vMoney
        popMoney!!.height = (DisplayUtil.getMobileWidth(this) * 0.8).toInt()
        // popMoney.setWidth((int) (DisplayUtil.getMobileHeight(this) * 0.5));
        popMoney!!.animationStyle = R.style.popWindow_animation
        //设置SelectPicPopupWindow弹出窗体的背景
        popMoney!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popMoney!!.isFocusable = true
        popMoney!!.isOutsideTouchable = true
        val lp = window.attributes
        lp.alpha = 0.6f //0.0-1.0
        window.attributes = lp
        popMoney!!.setOnDismissListener {
            val lp = window.attributes
            lp.alpha = 1.0f //0.0-1.0
            window.attributes = lp
        }
        popMoney!!.showAtLocation(vMoney, Gravity.CENTER, 0, 1000)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == 1112) {
            val f = File(PickPhotoActivity.stringList[0])
            OkhttpTool.getOkhttpTool().upLoadImage(UrlConfig.UPLOAD_TOUXIANG, f, object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)

                }


                override fun onResponse(call: Call, response: Response) {
                    try {
                        val s = response.body()!!.string()
                        LogUtils.d("=============================Response:$s")
                        upLoadGson = Gson().fromJson(s, UpLoadGson::class.java)
                        hd.sendEmptyMessage(UPLOAD)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }


        if (resultCode == Activity.RESULT_OK) {
            if (null != data) {
                //指定了路径，部分手机没有data，但是有的貌似还是有，很奇怪的事
                if (data.hasExtra("data")) {

                    val name = DateFormat.format("yyyymmdd",
                            Calendar.getInstance(Locale.CHINA)).toString() + ".jpg"
                    val bmp = data.getParcelableExtra<Bitmap>("data")// 解析返回的图片成bitmap

                    // 保存文件
                    var fos: FileOutputStream? = null
                    val file = File(mImagePath)
                    if (!file.exists()) {
                        file.mkdirs()// 创建文件夹
                    }
                    val fileName = mImagePath!! + name// 保存路径

                    try {
                        fos = FileOutputStream(fileName)
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } finally {
                        try {
                            fos!!.flush()
                            fos.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    photoPath = fileName
                    LogUtils.d("=======================photoPath:" + photoPath!!)
                    val f = File(photoPath)
                    map["imgFile"] = f
                    LogUtils.d("==================1")
                    OkhttpTool.getOkhttpTool().upLoadImage(UrlConfig.UPLOAD_TOUXIANG, f, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                        }

                        override fun onResponse(call: Call, response: Response) {
                            try {
                                val s = response.body()!!.string()
                                LogUtils.d("=============================Response:$s")
                                upLoadGson = Gson().fromJson(s, UpLoadGson::class.java)
                                hd.sendEmptyMessage(UPLOAD)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    })
                } else {
                    CustomToast.showToast(this@FaBuActicity, "没有data", Toast.LENGTH_SHORT)
                }
            } else {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                val actualimagecursor = managedQuery(imageFileUri, proj, null, null, null)
                val actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                actualimagecursor.moveToFirst()
                val img_path = actualimagecursor.getString(actual_image_column_index)
                val file = File(img_path)
                OkhttpTool.getOkhttpTool().upLoadImage(UrlConfig.UPLOAD_TOUXIANG, file, object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                        LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)

                    }

                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val s = response.body()!!.string()
                            LogUtils.d("=============================Response:$s")
                            upLoadGson = Gson().fromJson(s, UpLoadGson::class.java)
                            hd.sendEmptyMessage(UPLOAD)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 统计
     */
    private fun dabuTongji() {
        val tMap = HashMap<String, Any>()
        if (MyApplication.userInfo != null && MyApplication.userInfo!!.userID != null) {
            tMap["userid"] = MyApplication.userInfo!!.userID
        } else {
            tMap["userid"] = "0"
        }
        tMap["pagetype"] = "fabu"
        OkhttpTool.getOkhttpTool().post("http://app.680.com/api/v4/app_tongji_viewpage.ashx", tMap, TongJi())
    }

    class TongJi : Callback {
        override fun onFailure(call: Call, e: IOException) {
//            LhtTool.sendMessage(FaBuActicity.hd, e, NETWORK_EXCEPTION)
        }

        override fun onResponse(call: Call, response: Response) {
        }
    }

    private fun getSureInfo() {
        val fps = ArrayList<String>()
        Log.e("money", map["item_money"]!!.toString() + "")
        // map.put("debug","1");
        OkhttpTool.getOkhttpTool().post(UrlConfig.SURE_FB, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val s = response.body()!!.string()
                Log.e("fabuxuqiu", s)
                val ms = Message()
                ms.what = SURE_XQ
                val b = Bundle()
                b.putString("s", s)
                ms.data = b
                hd.sendMessage(ms)

            }
        })
    }

    private fun openPickPhoto() {

        val mIntent = Intent(this, PickPhotoActivity::class.java)
        mIntent.putExtra("number", 1.toString())
        startActivityForResult(mIntent, 111)

    }

    private fun takePhoto() {
        val SDState = Environment.getExternalStorageState()
        if (SDState == Environment.MEDIA_MOUNTED) {
            //获取与应用相关联的路径
            val imageFilePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath
            val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
            //            //根据当前时间生成图片的名称
            val imageFile = File(imageFilePath)// 通过路径创建保存文件
            mImagePath = imageFile.absolutePath
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri)// 告诉相机拍摄完毕输出图片到指定的Uri
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            imageFileUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            try {
                startActivityForResult(intent, 0x123)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                ToastUtils.showShort("无法读取相册请重试")
            }
        } else {
            CustomToast.showToast(this, "内存卡不存在！", Toast.LENGTH_LONG)
        }
    }

    override fun onResume() {
        super.onResume()
        if (LhtTool.isFaLogin == "1") {
            tijiaoXuqiu()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        hd.removeCallbacksAndMessages(null)
    }
}
