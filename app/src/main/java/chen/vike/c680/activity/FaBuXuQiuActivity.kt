package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.DisplayMetrics
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
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lht on 2017/3/16.
 *
 *
 * 发布需求
 */

class FaBuXuQiuActivity : BaseStatusBarActivity() {

    private var mLxfs: ImageView? = null
    private var xq_fujian: ImageView? = null
    private var mContent: EditText? = null
    private var mPhone: EditText? = null
    private var button: Button? = null
    private var money: EditText? = null
    private var mDj: EditText? = null
    private var mJs: EditText? = null
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
    private var takePhoto: TextView? = null
    private var pickPhoto: TextView? = null
    private var Canacel: TextView? = null
    private var mImagePath: String? = null
    private var imageFileUri: Uri? = null
    private var photoPath: String? = null
    private var UrlImag: String? = null
    private var fjj: View? = null
    private var jj: View? = null
    private var ll_xs_zb: LinearLayout? = null
    private var rg_xs_zb: RadioGroup? = null
    private val metrics = DisplayMetrics()
    private val GET_INFO = 0x123
    private val NETWORK_EXCEPTION = 0x111
    private val SURE_XQ = 0x121
    private val UPLOAD = 0x122

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

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            val imageView = ImageView(this@FaBuXuQiuActivity)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.layoutParams = AbsListView.LayoutParams(metrics.widthPixels / 10, metrics.widthPixels / 10)
            val options = RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(this@FaBuXuQiuActivity).load(strings[position]).apply(options).into(imageView)
            return imageView
        }
    }
    @SuppressLint("HandlerLeak")
    private val hd = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                if (mJs!!.text.toString().isEmpty() || mDj!!.text.toString().isEmpty()) {
                    fabu_ts!!.visibility = View.GONE
                    fabu_ts!!.text = ""
                } else {

                    val d = Math.ceil(java.lang.Double.valueOf(mDj!!.text.toString()) * java.lang.Double.valueOf(mJs!!.text.toString()))
                    if (d < 50) {
                        fabu_ts!!.visibility = View.VISIBLE
                        fabu_ts!!.text = "金额：" + d + "元+10元(项目金额低于50元时需支付的发布费)"
                    } else {
                        fabu_ts!!.visibility = View.GONE
                    }
                }
            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(this@FaBuXuQiuActivity, msg)
            } else if (msg.what == GET_INFO) {

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
                        mJs!!.setText(s[0])
                        mDj!!.setText(s[1])
                    } else {
                        if (itemInfoGson!!.item_type == "1") {
                            money!!.setText(itemInfoGson!!.item_zab_yusuan1)
                        } else {
                            money!!.setText(itemInfoGson!!.item_money)
                        }
                    }
                }

            } else if (msg.what == SURE_XQ) {

                val s = msg.data.getString("s")
                when (s) {
                    "unlogin" -> CustomToast.showToast(this@FaBuXuQiuActivity, "请登录后操作", Toast.LENGTH_LONG)
                    "unclass1id" -> CustomToast.showToast(this@FaBuXuQiuActivity, "请选择大分类", Toast.LENGTH_LONG)
                    "unclass2id" -> CustomToast.showToast(this@FaBuXuQiuActivity, "请选择小分类", Toast.LENGTH_LONG)
                    "nocon" -> CustomToast.showToast(this@FaBuXuQiuActivity, "请填写项目描述", Toast.LENGTH_LONG)
                    "jjnonum" -> CustomToast.showToast(this@FaBuXuQiuActivity, "请填写件数", Toast.LENGTH_LONG)
                    "jjnoprice" -> CustomToast.showToast(this@FaBuXuQiuActivity, "单价qq不能低于0.3元", Toast.LENGTH_LONG)
                    "noitemmoney" -> CustomToast.showToast(this@FaBuXuQiuActivity, "请填写预算金额", Toast.LENGTH_LONG)
                    "unbindmobile" -> {
                        CustomToast.showToast(this@FaBuXuQiuActivity, "请先绑定手机", Toast.LENGTH_LONG)
                        var intent = Intent(this@FaBuXuQiuActivity, MobileYanZhengActivity::class.java)
                        startActivity(intent)
                    }
                    "null_phone" -> CustomToast.showToast(this@FaBuXuQiuActivity, "请输入手机号码", Toast.LENGTH_LONG)
                    "less50" -> CustomToast.showToast(this@FaBuXuQiuActivity, "预算金额不得低于50元", Toast.LENGTH_LONG)
                    "fabufail" -> CustomToast.showToast(this@FaBuXuQiuActivity, "发布(或修改)项目失败", Toast.LENGTH_LONG)
                    else -> {
                        //  finish();
                        CustomToast.showToast(this@FaBuXuQiuActivity, "项目发布成功", Toast.LENGTH_LONG)
                        intent = Intent(this@FaBuXuQiuActivity, FuKuanAfterActivity::class.java)
                        intent.putExtra("ID", s)
                        startActivity(intent)
                    }
                }


            } else if (msg.what == UPLOAD) {
                if (upLoadGson!!.error == 1) {
                    CustomToast.showToast(this@FaBuXuQiuActivity, "上传失败", Toast.LENGTH_LONG)
                } else {
                    UrlImag = upLoadGson!!.url
                    strings.add(upLoadGson!!.url)
                    adapter.notifyDataSetChanged()
                }
            }
            ld?.dismiss()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowManager.defaultDisplay.getMetrics(metrics)
        setContentView(R.layout.fabu_xuqiu_activity)

        mLxfs = findViewById(R.id.fbxq_lxfs)
        xq_fujian = findViewById(R.id.xq_fujian)
        mContent = findViewById(R.id.xq_content)
        mPhone = findViewById(R.id.fbxq_sjhm)
        money = findViewById(R.id.fbxq_ys)
        mDj = findViewById(R.id.fbxq_dj)
        mJs = findViewById(R.id.fbxq_js)
        ll_xs_zb = findViewById(R.id.ll_xs_and_zb)
        rg_xs_zb = findViewById(R.id.xs_zb_rg)
        pop = PopupWindow(this@FaBuXuQiuActivity)
        val v = LayoutInflater.from(this@FaBuXuQiuActivity).inflate(R.layout.selectview, null)
        takePhoto = v.findViewById(R.id.btn_take_photo)
        pickPhoto = v.findViewById(R.id.btn_pick_photo)
        Canacel = v.findViewById(R.id.btn_cancel)

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

        fjj = findViewById(R.id.fjj)
        jj = findViewById(R.id.jj)
        button = findViewById<View>(R.id.control) as Button
        fbxq_lm = findViewById<View>(R.id.fbxq_lm) as TextView
        fabu_ts = findViewById<View>(R.id.fabu_ts) as TextView
        val xRecyclerView = findViewById<View>(R.id.listview) as MyGridView
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
            //            if (id.equals("6")) {
            //                fjj.setVisibility(View.GONE);
            //                jj.setVisibility(View.VISIBLE);
            //                mDj.addTextChangedListener(new TextWatcher() {
            //                    @Override
            //                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //
            //                    }
            //
            //                    @Override
            //                    public void onTextChanged(CharSequence s, int start, int before, int count) {
            //                        MHandler.sendEmptyMessage(0);
            //                    }
            //
            //                    @Override
            //                    public void afterTextChanged(Editable s) {
            //
            //                    }
            //                });
            //                mJs.addTextChangedListener(new TextWatcher() {
            //                    @Override
            //                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //
            //                    }
            //
            //                    @Override
            //                    public void onTextChanged(CharSequence s, int start, int before, int count) {
            //                        MHandler.sendEmptyMessage(0);
            //
            //                    }
            //
            //                    @Override
            //                    public void afterTextChanged(Editable s) {
            //                    }
            //                });
            //
            //            }
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
        back.setImageResource(R.mipmap.back)
        mLxfs!!.setOnClickListener {
            try {
                LhtTool.Call(Intent.ACTION_CALL, "400–630-6800", this@FaBuXuQiuActivity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        rg_xs_zb!!.setOnCheckedChangeListener { group, checkedId -> }

        button!!.setOnClickListener {
            if (mContent!!.text.toString().isEmpty()) {
                CustomToast.showToast(this@FaBuXuQiuActivity, "描述的需求不能为空", Toast.LENGTH_LONG)
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
                if (null != id && id == "6") {
                    if (mDj!!.text.toString().isEmpty()) {
                        CustomToast.showToast(this@FaBuXuQiuActivity, "单价不能为空", Toast.LENGTH_LONG)
                    } else {
                        if (java.lang.Float.valueOf(mDj!!.text.toString()) < 0.3f) {
                            CustomToast.showToast(this@FaBuXuQiuActivity, "单价不能低于0.3元", Toast.LENGTH_LONG)
                        } else {
                            if (mJs!!.text.toString().isEmpty() || Integer.valueOf(mJs!!.text.toString()) == 0) {
                                CustomToast.showToast(this@FaBuXuQiuActivity, "件数必须大于0", Toast.LENGTH_LONG)

                            } else {
                                map["jj_num"] = mJs!!.text
                                map["jj_price"] = mDj!!.text
                                map["item_money"] = java.lang.Float.valueOf(mDj!!.text.toString()) * java.lang.Float.valueOf(mJs!!.text.toString())
                                if (mPhone!!.text.toString().isEmpty()) {
                                    CustomToast.showToast(this@FaBuXuQiuActivity, "手机号码不能为空", Toast.LENGTH_LONG)
                                } else {
                                    map["phone"] = mPhone!!.text
                                    if (LhtTool.isLogin) {
                                        map["userid"] = MyApplication.userInfo!!.userID
                                        ld = LoadingDialog(this@FaBuXuQiuActivity).setMessage("提交中....")
                                        ld!!.show()
                                        getSureInfo()
                                    } else {
                                        val intent = Intent(this@FaBuXuQiuActivity, UserLoginActivity::class.java)
                                        this@FaBuXuQiuActivity.startActivity(intent)
                                    }
                                }
                            }
                        }
                    }

                } else {
                    if (money!!.text.toString().isEmpty()) {
                        CustomToast.showToast(this@FaBuXuQiuActivity, "预算不能为空", Toast.LENGTH_LONG)
                    } else {
                        map["item_money"] = money!!.text
                        if (mPhone!!.text.toString().isEmpty()) {
                            CustomToast.showToast(this@FaBuXuQiuActivity, "手机号码不能为空", Toast.LENGTH_LONG)
                        } else {
                            map["phone"] = mPhone!!.text
                            var k = "-1"
                            if (rg_xs_zb!!.checkedRadioButtonId == R.id.xs_zb_rb) {
                                k = "0"
                            } else {
                                k = "1"
                            }
                            map["itemtype"] = k
                            val k1 = "0"
                            LogUtils.d("=============k1:$k1")
                            map["is_xuangao"] = k1
                            if (LhtTool.isLogin) {
                                map["userid"] = MyApplication.userInfo!!.userID
                                ld = LoadingDialog(this@FaBuXuQiuActivity).setMessage("提交中....")
                                ld!!.show()
                                getSureInfo()
                            } else {
                                val intent = Intent(this@FaBuXuQiuActivity, UserLoginActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
        fbxq_lm!!.setOnClickListener {
            val intent = Intent(this@FaBuXuQiuActivity, CatoryActivity::class.java)
            startActivityForResult(intent, 400)
        }
        xRecyclerView.adapter = adapter
        xq_fujian!!.setOnClickListener { pop!!.showAtLocation(findViewById(R.id.ll), Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0) }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (resultCode == 400) {
            if (CatoryActivity.na.isEmpty()) {
                fbxq_lm!!.text = "LOGO设计"
                map["class1id"] = "1"
                map["class2id"] = "1"
                id = "1"
            } else {
                fbxq_lm!!.text = CatoryActivity.na
                map["class1id"] = CatoryActivity.Did
                map["class2id"] = CatoryActivity.Xid
                id = CatoryActivity.Did
            }
            if (id == "6") {
                fjj!!.visibility = View.GONE
                jj!!.visibility = View.VISIBLE
                ll_xs_zb!!.visibility = View.GONE
                mDj!!.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        hd.sendEmptyMessage(0)
                    }

                    override fun afterTextChanged(s: Editable) {

                    }
                })
                mJs!!.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        hd.sendEmptyMessage(0)

                    }

                    override fun afterTextChanged(s: Editable) {}
                })

            } else {
                fjj!!.visibility = View.VISIBLE
                jj!!.visibility = View.GONE
                ll_xs_zb!!.visibility = View.VISIBLE
                if (rg_xs_zb!!.checkedRadioButtonId == R.id.xs_zb_rb) {

                } else {

                }

            }
        }

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
                    CustomToast.showToast(this@FaBuXuQiuActivity, "没有data", Toast.LENGTH_SHORT)
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


    private fun getSureInfo() {
//        map["debug"]="1"
        OkhttpTool.getOkhttpTool().post(UrlConfig.SURE_FB, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val s = response.body()!!.string()
                LogUtils.d("================response:$s")
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

        val `in` = Intent(this, PickPhotoActivity::class.java)
        `in`.putExtra("number", 1.toString())
        startActivityForResult(`in`, 111)

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
            }catch (e:ActivityNotFoundException){
                e.printStackTrace()
                ToastUtils.showShort("无法读取相册请重试")
            }
        } else {
            CustomToast.showToast(this, "内存卡不存在！", Toast.LENGTH_LONG)
        }
    }

}
