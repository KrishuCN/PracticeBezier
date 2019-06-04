package chen.vike.c680.activity

import android.app.Activity
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
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import chen.vike.c680.bean.UpLoadGson
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
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lht on 2017/3/17.
 *
 * 参与悬赏
 */

class Cy_XsActivity : BaseStatusBarActivity() {

    private var mContent: EditText? = null
    private var mControl: Button? = null
    private var mPic: ImageView? = null
    private val map = HashMap<String, Any>()
    private val strings = ArrayList<String>()
    private var upLoadGson: UpLoadGson? = null
    private val metrics = DisplayMetrics()
    private var ld: LoadingDialog? = null
    private var pop: PopupWindow? = null
    private var takePhoto: TextView? = null
    private var pickPhoto: TextView? = null
    private var Canacel: TextView? = null
    private var mImagePath: String? = null
    private var imageFileUri: Uri? = null
    private var photoPath: String? = null
    private val SURE_BAOMING = 0x123
    private val NETWORKEXCEPTION = 0x111
    private val UPLOAD = 0x223
    private val hd = MHandler(this)
    /**
     * 适配器
     */
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
            val imageView = ImageView(this@Cy_XsActivity)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.setPadding(2, 2, 2, 2)

            imageView.layoutParams = AbsListView.LayoutParams(metrics.widthPixels / 10, metrics.widthPixels / 10)
            val options = RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(this@Cy_XsActivity).load(strings[position]).apply(options).into(imageView)
            return imageView
        }
    }


    private class MHandler(cy_XsActivity: Cy_XsActivity):Handler(){
        private val weakReference:WeakReference<Cy_XsActivity> = WeakReference(cy_XsActivity)
        private val cy_XsActivity = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            cy_XsActivity?.run {
                if (msg?.what == SURE_BAOMING) {

                    val s = msg.data.getString("s")
                    when (s) {
                        "unlogin" -> CustomToast.showToast(this, "未登录", Toast.LENGTH_LONG)
                        "jpunin" -> CustomToast.showToast(this, "您不能参与，只有金牌威客才能参与精品项目", Toast.LENGTH_LONG)
                        "ok" -> {
                            CustomToast.showToast(this, "提交成功", Toast.LENGTH_LONG)
                            finish()
                        }
                        "mustvip" -> CustomToast.showToast(this, "您暂不能参与该项目,签约VIP服务商现在可参与", Toast.LENGTH_LONG)
                        "err" -> CustomToast.showToast(this, "提交失败", Toast.LENGTH_LONG)
                        "null_zp" -> CustomToast.showToast(this, "请填写方案说明/方案内容", Toast.LENGTH_LONG)
                        "noitem" -> CustomToast.showToast(this, "无效项目", Toast.LENGTH_LONG)
                        "noflag" -> CustomToast.showToast(this, "没有此项目或正在审核中", Toast.LENGTH_LONG)
                        "self" -> CustomToast.showToast(this, "不能参与自己发布的项目", Toast.LENGTH_LONG)
                        "timeout" -> CustomToast.showToast(this, "项目已结束，不能参与了", Toast.LENGTH_LONG)
                        "max5" -> CustomToast.showToast(this, "一个项目最多能提交5个方案", Toast.LENGTH_LONG)
                        "over" -> CustomToast.showToast(this, "项目已结束，不能参与了", Toast.LENGTH_LONG)
                        "notjj" -> CustomToast.showToast(this, "此项目是非计件项目，暂不能参与", Toast.LENGTH_LONG)
                        "nofj" -> CustomToast.showToast(this, "请上传附件或填写附件地址", Toast.LENGTH_LONG)
                        "null_con" -> CustomToast.showToast(this, "请填写内容 ", Toast.LENGTH_LONG)
                        "user_tj_max" -> CustomToast.showToast(this, "不能再提交了，您提交的数量已达到雇主限制", Toast.LENGTH_LONG)
                        "fenpeiok" -> CustomToast.showToast(this, "此项目已结束，合格稿件数量已达到要求", Toast.LENGTH_LONG)
                        "hasunpay_full" -> CustomToast.showToast(this, "暂不能提交，当前未评标的稿件数量已达到最大限制", Toast.LENGTH_LONG)
                        "stop500item" -> CustomToast.showToast(this, "能力等级小于三星级的只能参与500元以内的项目", Toast.LENGTH_LONG)
                        "stop800item" -> CustomToast.showToast(this, "能力等级小于钻石二级的只能参与800元以内的项目！", Toast.LENGTH_LONG)
                        "stopnocynum" -> CustomToast.showToast(this, "非VIP服务商每天最多可参与5个项目", Toast.LENGTH_LONG)
                        else -> CustomToast.showToast(this, "提交失败", Toast.LENGTH_LONG)
                    }

                } else if (msg?.what == NETWORKEXCEPTION) {

                    LhtTool.showNetworkException(this, msg)

                } else if (msg?.what == UPLOAD) {

                    if (upLoadGson!!.error == 1) {
                        CustomToast.showToast(this, "上传失败", Toast.LENGTH_LONG)
                    } else {
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
        setContentView(R.layout.activity_xs_cy)
        title.text = "报名参与"
        mContent = findViewById(R.id.cy_content)
        mControl = findViewById(R.id.control)
        val xRecyclerView = findViewById<MyGridView>(R.id.listview)
        mPic = findViewById(R.id.xq_fujian)
        pop = PopupWindow(this@Cy_XsActivity)
        xRecyclerView.adapter = adapter
        showWindPop()//弹窗
        viewListener()//控件监听事件
        viewBundle()//view的Bundle数据
    }

    /**
     * view控件的监听事件
     */
    private fun viewListener() {
        mPic!!.setOnClickListener { pop!!.showAtLocation(findViewById(R.id.fl), Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0) }
        mControl!!.setOnClickListener {
            if (mContent!!.text.toString().isEmpty()) {
                CustomToast.showToast(this@Cy_XsActivity, "请输入你要提交的内容", Toast.LENGTH_LONG)
            } else {
                map["crv_canyuzuopin_content"] = mContent!!.text
                map["baomi"] = 1
                for (i in strings.indices) {
                    map["fj" + (i + 1)] = strings[i]
                }
                OkhttpTool.getOkhttpTool().post(UrlConfig.USER_JION, map, object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)
                    }

                    override fun onResponse(call: Call, response: Response) {

                        val s = response.body()!!.string()
                        Log.e("tijiao", s)
                        LogUtils.d("===============response:$s")
                        val ms = Message()
                        ms.what = SURE_BAOMING
                        val b = Bundle()
                        b.putString("s", s)
                        ms.data = b
                        hd.sendMessage(ms)

                    }
                })
                ld = LoadingDialog(this@Cy_XsActivity).setMessage("提交中....")
                ld?.show()

            }
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

    }

    /**
     * 弹窗
     */
    private fun showWindPop() {
        val v = LayoutInflater.from(this@Cy_XsActivity).inflate(R.layout.selectview, null)
        takePhoto = v.findViewById<View>(R.id.btn_take_photo) as TextView
        pickPhoto = v.findViewById<View>(R.id.btn_pick_photo) as TextView
        Canacel = v.findViewById<View>(R.id.btn_cancel) as TextView
        pop!!.contentView = v
        pop!!.height = (DisplayUtil.getMobileWidth(this) * 0.4).toInt()
        pop!!.width = (DisplayUtil.getMobileHeight(this) * 0.5).toInt()
        pop!!.animationStyle = R.style.popWindow_animation
        //设置SelectPicPopupWindow弹出窗体的背景
        pop!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        pop!!.isFocusable = true
        pop!!.isOutsideTouchable = true
    }

    /**
     * view的Bundle数据
     */
    private fun viewBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            val id = bundle.getString("ID")
            val type = bundle.getInt("TYPE")
            if (type == 0) {
                map["itemtype"] = "canyuxs"
            } else {
                map["itemtype"] = "canyujj"
            }
            map["itemid"] = id!!
        }
        if (LhtTool.isLogin) {
            map["userid"] = MyApplication.userInfo!!.userID
            map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
            map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == 1112) {
            val f = File(PickPhotoActivity.stringList[0])
            OkhttpTool.getOkhttpTool().upLoadImage(UrlConfig.UPLOAD_TOUXIANG, f, object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)

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

                            LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)

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
                    CustomToast.showToast(this@Cy_XsActivity, "没有data", Toast.LENGTH_SHORT)
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

                        LhtTool.sendMessage(hd, e, NETWORKEXCEPTION)

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
     * 打开图片
     */
    private fun openPickPhoto() {
        val `in` = Intent(this, PickPhotoActivity::class.java)
        `in`.putExtra("number", 1.toString())
        startActivityForResult(`in`, 111)

    }

    /**
     * 拍摄存储图片
     */
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
            startActivityForResult(intent, 0x123)
        } else {
            CustomToast.showToast(this, "内存卡不存在！", Toast.LENGTH_LONG)
        }
    }

}

