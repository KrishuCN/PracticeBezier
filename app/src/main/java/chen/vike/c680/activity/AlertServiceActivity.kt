package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
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
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import chen.vike.c680.bean.ServiceDetailsGson
import chen.vike.c680.bean.UpLoadGson
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.*
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.LoadingDialog
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
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lht on 2017/3/17.
 *
 *
 * 修改服务
 */

class AlertServiceActivity : BaseStatusBarActivity() {

    private var id: String? = null
    private val map = HashMap<String, Any>()
    private var serviceDetailsGson: ServiceDetailsGson? = null
    private var sell_title: EditText? = null
    private var sell_price: EditText? = null
    private var sell_edt: EditText? = null
    private var sell_imgw: ImageView? = null
    private var sell_img: ImageView? = null
    private var spinner_sprice: Spinner? = null
    private var price_dw: String? = null
    private var sell_commit: Button? = null
    private var UrlImag: String? = null
    private var linear_edt: LinearLayout? = null
    private var selllinear_category: LinearLayout? = null
    private var sell_text_category1: TextView? = null
    private var sell_text_category2: TextView? = null
    private var sell_text_category3: TextView? = null
    private var sell_category: TextView? = null
    private var sell_numtext: TextView? = null
    private var Did: String? = null
    private var Xid: String? = null
    private var idd: String? = null
    private var pop: PopupWindow? = null
    private var takePhoto: TextView? = null
    private var pickPhoto: TextView? = null
    private var Canacel: TextView? = null
    private var mImagePath: String? = null
    private var imageFileUri: Uri? = null
    private var photoPath: String? = null
    private var upLoadGson: UpLoadGson? = null
    private var ld: LoadingDialog? = null
    private var temp: CharSequence? = null
    internal var edt: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            sell_numtext!!.text = (count + 1).toString()
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            temp = s
        }

        override fun afterTextChanged(s: Editable) {
            sell_numtext!!.text = temp!!.length.toString()
        }
    }

    private val UPLOAD_TUPIAN = 0x123
    private val FUWU_INFO = 0x121
    private val SURE_FABU = 0x132
    private val NETWORK_EXCEPTION = 0X111
    @SuppressLint("HandlerLeak")
    private val hd = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == UPLOAD_TUPIAN) {

                if (upLoadGson!!.error == 1) {
                    CustomToast.showToast(this@AlertServiceActivity, "上传失败", Toast.LENGTH_SHORT)
                } else {
                    UrlImag = upLoadGson!!.url
                    sell_imgw!!.visibility = View.VISIBLE
                    sell_img!!.visibility = View.GONE

                    val options = RequestOptions()
                            .placeholder(R.mipmap.image_loading)
                            .error(R.mipmap.image_erroe)
                    Glide.with(this@AlertServiceActivity).load(upLoadGson!!.url).apply(options).into(sell_imgw!!)
                }


            } else if (msg.what == SURE_FABU) {

                val s = msg.data.getString("s")
                when (s) {
                    "unlogin" -> CustomToast.showToast(this@AlertServiceActivity, "未登录", Toast.LENGTH_LONG)
                    "unclass1id" -> CustomToast.showToast(this@AlertServiceActivity, "请选择分类", Toast.LENGTH_LONG)
                    "unclass2id" -> CustomToast.showToast(this@AlertServiceActivity, "请选择分类", Toast.LENGTH_LONG)
                    "has" -> CustomToast.showToast(this@AlertServiceActivity, "服务标题已存在，你已发不过此类服务", Toast.LENGTH_LONG)
                    "okedit" -> {
                        CustomToast.showToast(this@AlertServiceActivity, "修改服务成功", Toast.LENGTH_LONG)
                        finish()
                    }
                    "failedit" -> CustomToast.showToast(this@AlertServiceActivity, "修改服务失败", Toast.LENGTH_LONG)
                    "ok" -> {
                        CustomToast.showToast(this@AlertServiceActivity, "发布成功", Toast.LENGTH_LONG)
                        finish()
                    }
                    "fail" -> CustomToast.showToast(this@AlertServiceActivity, "发布失败", Toast.LENGTH_LONG)
                    "noshopid" -> CustomToast.showToast(this@AlertServiceActivity, "你还未开通店铺不能发布服务", Toast.LENGTH_LONG)
                    "nocon" -> CustomToast.showToast(this@AlertServiceActivity, "请填写服务详情", Toast.LENGTH_LONG)
                    "nounit" -> CustomToast.showToast(this@AlertServiceActivity, "请选择单位", Toast.LENGTH_LONG)
                    "nobzpic" -> CustomToast.showToast(this@AlertServiceActivity, "请上传一张封面图片", Toast.LENGTH_LONG)
                    "notitle" -> CustomToast.showToast(this@AlertServiceActivity, "请先输入标题", Toast.LENGTH_LONG)
                    "noprice" -> CustomToast.showToast(this@AlertServiceActivity, "请先输入价格", Toast.LENGTH_LONG)
                }

            } else if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(this@AlertServiceActivity, msg)

            } else if (msg.what == FUWU_INFO) {

                sell_title!!.setText(serviceDetailsGson!!.title)
                sell_price!!.setText(serviceDetailsGson!!.price)
                sell_edt!!.setText(serviceDetailsGson!!.con)
                sell_text_category1!!.text = serviceDetailsGson!!.class1name + " > "
                sell_text_category2!!.text = serviceDetailsGson!!.class2name
                idd = serviceDetailsGson!!.id
                Did = serviceDetailsGson!!.class1id
                Xid = serviceDetailsGson!!.class2id
                UrlImag = serviceDetailsGson!!.imgUrl
                sell_imgw!!.visibility = View.VISIBLE
                sell_title!!.setSelection(sell_title!!.text.length)
                sell_price!!.setSelection(sell_price!!.text.length)
                Glide.with(this@AlertServiceActivity).load(serviceDetailsGson!!.imgUrl).into(sell_imgw!!)
                val a = "元/" + serviceDetailsGson!!.unit
                val province_adapter = ArrayAdapter.createFromResource(this@AlertServiceActivity, R.array.sell_service, android.R.layout.simple_spinner_item)
                province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_sprice!!.adapter = province_adapter
                val ss = resources.getStringArray(R.array.sell_service)
                for (i in ss.indices) {
                    if (a == ss[i]) {
                        spinner_sprice!!.setSelection(i, true)
                    }
                }
                price_dw = a
                spinner_sprice!!.prompt = "单位"
                spinner_sprice!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        price_dw = spinner_sprice!!.selectedItem.toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }

            }
            if (ld != null) {
                ld!!.dismiss()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alert_service_activity)

        title.text = "修改服务"
        pop = PopupWindow(this@AlertServiceActivity)
        val v = LayoutInflater.from(this@AlertServiceActivity).inflate(R.layout.selectview, null)
        takePhoto = v.findViewById(R.id.btn_take_photo)
        pickPhoto = v.findViewById(R.id.btn_pick_photo)
        Canacel = v.findViewById(R.id.btn_cancel)
        sell_title = findViewById(R.id.sell_title)
        sell_price = findViewById(R.id.sell_price)
        sell_edt = findViewById(R.id.sell_edt)
        sell_edt!!.addTextChangedListener(edt)
        sell_imgw = findViewById(R.id.sell_imgw)
        sell_img = findViewById(R.id.sell_img)
        spinner_sprice = findViewById(R.id.spinner_sprice)
        sell_commit = findViewById(R.id.sell_commit)
        sell_text_category2 = findViewById(R.id.sell_text_category2)
        sell_text_category1 = findViewById(R.id.sell_text_category1)
        sell_text_category3 = findViewById(R.id.sell_text_category3)
        sell_numtext = findViewById(R.id.sell_numtext)
        sell_category = findViewById(R.id.sell_category)
        linear_edt = findViewById(R.id.linear_edt)
        selllinear_category = findViewById(R.id.selllinear_category)


        sell_commit!!.setOnClickListener { goCommit() }
        sell_img!!.setOnClickListener { pop!!.showAtLocation(findViewById(R.id.ll), Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0) }

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


        selllinear_category!!.setOnClickListener {
            val intent = Intent(this@AlertServiceActivity, CatoryActivity::class.java)
            startActivityForResult(intent, 400)
            sell_category!!.isFocusable = false
            sell_text_category3!!.isFocusable = false
        }
        sell_category!!.setOnClickListener {
            val intent = Intent(this@AlertServiceActivity, CatoryActivity::class.java)
            startActivityForResult(intent, 400)
            sell_category!!.isFocusable = false
            sell_text_category3!!.isFocusable = false
        }
        linear_edt!!.setOnClickListener {
            //真不知道这个监听有什么屌用
            sell_edt!!.requestFocus()
            val imm = sell_edt!!.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
        }
        sell_imgw!!.setOnClickListener { pop!!.showAtLocation(findViewById(R.id.ll), Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0) }
        id = intent.getStringExtra("ID")
        map["proid"] = id!!
        if (LhtTool.isLogin) {
            map["userid"] = MyApplication.userInfo!!.userID
            OkhttpTool.getOkhttpTool().post(UrlConfig.FUWU_XIANGQING, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)

                }


                override fun onResponse(call: Call, response: Response) {

                    val s = response.body()!!.string()
                    LogUtils.d("=================response:$s")
                    try {
                        serviceDetailsGson = Gson().fromJson(s, ServiceDetailsGson::class.java)
                        hd.sendEmptyMessage(FUWU_INFO)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == 1112) {
            val f = File(PickPhotoActivity.stringList[0])
            OkhttpTool.getOkhttpTool().upLoadImage(UrlConfig.UPLOAD_TOUXIANG, f, object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)


                }


                override fun onResponse(call: Call, response: Response) {
                    val s = response.body()!!.string()
                    try {
                        LogUtils.d("=============================Response:$s")
                        upLoadGson = Gson().fromJson(s, UpLoadGson::class.java)
                        hd.sendEmptyMessage(UPLOAD_TUPIAN)
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
                            val s = response.body()!!.string()
                            LogUtils.d("=============================Response:$s")
                            try {
                                upLoadGson = Gson().fromJson(s, UpLoadGson::class.java)
                                hd.sendEmptyMessage(UPLOAD_TUPIAN)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    })
                } else {
                    CustomToast.showToast(this@AlertServiceActivity, "没有data", Toast.LENGTH_SHORT)
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

                        val s = response.body()!!.string()
                        try {
                            LogUtils.d("=============================Response:$s")
                            upLoadGson = Gson().fromJson(s, UpLoadGson::class.java)
                            hd.sendEmptyMessage(UPLOAD_TUPIAN)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                })
            }
        }


        if (resultCode == 400) {
            if (CatoryActivity.ti.isEmpty()) {
                sell_text_category1!!.text = "平面设计" + " > "
            } else {
                sell_text_category1!!.text = CatoryActivity.ti + " > "
            }
            Did = CatoryActivity.Did
            Xid = CatoryActivity.Xid
            sell_text_category2!!.text = CatoryActivity.na
        }


        super.onActivityResult(requestCode, resultCode, data)
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
            }catch (e: ActivityNotFoundException){
                e.printStackTrace()
                ToastUtils.showShort("无法读取相册请重试")
            }
        } else {
            CustomToast.showToast(this, "内存卡不存在！", Toast.LENGTH_LONG)
        }
    }

    private fun goCommit() {
        if (LhtTool.isLogin) {
            map["userid"] = MyApplication.userInfo!!.userID
            map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
            map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
            if (!Xid!!.isEmpty()) {
                map["class1id"] = Did!!
                map["class2id"] = Xid!!
                if (!sell_title!!.text.toString().isEmpty()) {
                    map["title"] = sell_title!!.text.toString()
                    if (!sell_price!!.text.toString().isEmpty()) {
                        map["price"] = sell_price!!.text.toString()
                        val a = price_dw!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        map["unit"] = a[1]
                        if (!UrlImag!!.isEmpty()) {
                            Log.d(BaseStatusBarActivity.Companion.TAG, "FabuSeverce: " + UrlImag!!)
                            map["mainpicpath"] = UrlImag!!

                            if (!sell_edt!!.text.toString().isEmpty()) {
                                map["content"] = sell_edt!!.text.toString()
                                map["apptype"] = "an"
                                map["id"] = idd!!
                                OkhttpTool.getOkhttpTool().post(UrlConfig.FABU_SERVICE, map, object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                                    }

                                    @Throws(IOException::class)
                                    override fun onResponse(call: Call, response: Response) {

                                        val s = response.body()!!.string()
                                        LogUtils.d("===================response:$s")
                                        val ms = Message()
                                        ms.what = SURE_FABU
                                        val b = Bundle()
                                        b.putString("s", s)
                                        ms.data = b
                                        hd.sendMessage(ms)


                                    }
                                })
                                ld = LoadingDialog(this@AlertServiceActivity).setMessage("提交中....")
                                ld!!.show()
                            } else {
                                CustomToast.showToast(this@AlertServiceActivity, "请输入服务详情", Toast.LENGTH_SHORT)
                            }
                        } else {
                            CustomToast.showToast(this@AlertServiceActivity, "请上传一张封面图片", Toast.LENGTH_SHORT)
                        }
                    } else {
                        CustomToast.showToast(this@AlertServiceActivity, "请输入价格", Toast.LENGTH_SHORT)
                    }
                } else {
                    CustomToast.showToast(this@AlertServiceActivity, "请输入标题", Toast.LENGTH_SHORT)
                }
            } else {
                CustomToast.showToast(this@AlertServiceActivity, "请选择分类", Toast.LENGTH_SHORT)
            }

        }
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {

    }
}
