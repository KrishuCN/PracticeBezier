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
import android.text.format.DateFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils

import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import chen.vike.c680.bean.UpLoadGson
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.DisplayUtil
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import com.lht.vike.a680_v1.R

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.HashMap
import java.util.Locale

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

/**
 * Created by lht on 2017/3/6.
 */

class ShiMingRenZhengActivity : BaseStatusBarActivity() {

    private var zsxm: EditText? = null
    private var zjhm: EditText? = null
    private var sfz_zm: ImageView? = null
    private var sfz_fm: ImageView? = null
    private var tjsh: Button? = null
    private val map = HashMap<String, Any>()
    private val `in`: Intent? = null
    private var photoPath: String? = null
    private val UPLOAD_SFZ = 0x123
    private val SURE_CHECK = 0x132
    private val NETWORK_EXCEPTION = 0X111
    private var pop: PopupWindow? = null
    private var takePhoto: TextView? = null
    private var pickPhoto: TextView? = null
    private var Canacel: TextView? = null
    private var mImagePath: String? = ""
    private var imageFileUri: Uri? = null
    private var upLoadGson: UpLoadGson? = null
    private var exitTime: Long = 0
    private var exitTime2: Long = 0
    internal var target_sfz: Int = 0//1是正面，2是反面

    @SuppressLint("HandlerLeak")
    private val hd = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == UPLOAD_SFZ) {

                if (upLoadGson!!.error == 1) {
                    CustomToast.showToast(this@ShiMingRenZhengActivity, "上传失败", Toast.LENGTH_LONG)
                } else {
                    if (target_sfz == 1) {
                        Glide.with(this@ShiMingRenZhengActivity).load(upLoadGson!!.url).into(sfz_zm!!)
                        map["crv_sfz_zmimg"] = upLoadGson!!.url
                    } else if (target_sfz == 2) {
                        Glide.with(this@ShiMingRenZhengActivity).load(upLoadGson!!.url).into(sfz_fm!!)
                        map["crv_sfz_bmimg"] = upLoadGson!!.url
                    }
                }

            } else if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(this@ShiMingRenZhengActivity, msg)

            } else if (msg.what == SURE_CHECK) {

                LogUtils.d("===============进来了")
                val s = msg.data.getString("s")
                LogUtils.d("===============传来的s:" + s!!)
                when (s) {
                    "unlogin" -> CustomToast.showToast(this@ShiMingRenZhengActivity, "未登录", Toast.LENGTH_LONG)
                    "errsfzno" -> CustomToast.showToast(this@ShiMingRenZhengActivity, "填写的身份证号码无效", Toast.LENGTH_LONG)
                    "hassfzcode" -> CustomToast.showToast(this@ShiMingRenZhengActivity, "填写的身份证号码已经被认证使用了", Toast.LENGTH_LONG)
                    "ok" -> {
                        CustomToast.showToast(this@ShiMingRenZhengActivity, "提交身份认证资料成功，待审核", Toast.LENGTH_LONG)
                        MyApplication.userInfo!!.is_verify_fullname = "2"
                        MyApplication.userInfo!!.realName = zsxm!!.text.toString()
                        finish()
                    }
                    "err" -> CustomToast.showToast(this@ShiMingRenZhengActivity, "提交身份认证资料失败", Toast.LENGTH_LONG)
                    "nosfz1" -> CustomToast.showToast(this@ShiMingRenZhengActivity, "请上传正面身份证照片", Toast.LENGTH_LONG)
                    "nosfz2" -> CustomToast.showToast(this@ShiMingRenZhengActivity, "请上传背面身份证照片", Toast.LENGTH_LONG)
                    "nosfzno" -> CustomToast.showToast(this@ShiMingRenZhengActivity, "请填写身份证号码", Toast.LENGTH_LONG)
                    "noname" -> CustomToast.showToast(this@ShiMingRenZhengActivity, "请填写真实姓名", Toast.LENGTH_LONG)
                }

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shimingrenzheng)

        title.text = "实名认证"
        zsxm = findViewById(R.id.smrz_zsxm)
        zjhm = findViewById(R.id.smrz_zjhm)
        sfz_zm = findViewById(R.id.smrz_sfz_zm)
        sfz_fm = findViewById(R.id.smrz_sfz_bm)
        tjsh = findViewById(R.id.smrz_tjsh)

        pop = PopupWindow(this@ShiMingRenZhengActivity)
        val v = LayoutInflater.from(this@ShiMingRenZhengActivity).inflate(R.layout.selectview, null)
        takePhoto = v.findViewById(R.id.btn_take_photo)
        pickPhoto = v.findViewById(R.id.btn_pick_photo)
        Canacel = v.findViewById(R.id.btn_cancel)



        if (LhtTool.isLogin) {
            map["userid"] = MyApplication.userInfo!!.userID
            map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
            map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
            zsxm!!.setText(MyApplication.userInfo!!.realName)
        }

        takePhoto!!.setOnClickListener {
            pop?.dismiss()
            XXPermissions.with(this).permission(Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                    .request(object : OnPermission{
                        override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                            ToastUtils.showShort("需要存储和拍照权限")
                        }
                        override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                            if (isAll){
                                takePhoto()
                            }else{
                                ToastUtils.showShort("需要存储和拍照权限")
                            }
                        }
                    })
        }

        pickPhoto!!.setOnClickListener {
            pop?.dismiss()
            XXPermissions.with(this).permission(Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA)
                    .request(object : OnPermission{
                        override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                            ToastUtils.showShort("需要存储和拍照权限")
                        }

                        override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                            if (isAll){
                                openPickPhoto()
                            }else{
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

        sfz_zm!!.setOnClickListener {
            target_sfz = 1
            pop!!.showAtLocation(findViewById(R.id.ll), Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
        }

        sfz_fm!!.setOnClickListener {
            target_sfz = 2
            pop!!.showAtLocation(findViewById(R.id.ll), Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
        }

        tjsh!!.setOnClickListener {
            if (zsxm!!.text.toString().isEmpty()) {
                if (System.currentTimeMillis() - exitTime > 4000) {
                    CustomToast.showToast(this@ShiMingRenZhengActivity, "姓名不能为空", Toast.LENGTH_LONG)
                    exitTime = System.currentTimeMillis()
                }
            } else {
                if (zjhm!!.text.toString().isEmpty() || zjhm!!.text.toString().length < 18) {
                    if (System.currentTimeMillis() - exitTime2 > 4000) {
                        CustomToast.showToast(this@ShiMingRenZhengActivity, "请正确输入18位身份证号码", Toast.LENGTH_LONG)
                        exitTime2 = System.currentTimeMillis()
                    }
                } else {
                    map["crv_truename"] = zsxm!!.text
                    map["crv_sfznumber"] = zjhm!!.text
                    OkhttpTool.getOkhttpTool().post(UrlConfig.SHIMINGRENZHENG, map, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {

                            val s = response.body()!!.string()
                            LogUtils.d("================Response:$s")
                            val ms = Message()
                            val b = Bundle()
                            b.putString("s", s)
                            ms.data = b
                            ms.what = SURE_CHECK
                            hd.sendMessage(ms)

                        }
                    })
                }
            }
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == 1112) {
            val f = File(PickPhotoActivity.stringList[0])
            OkhttpTool.getOkhttpTool().upLoadImage(UrlConfig.UPLOAD_SFZ, f, object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)


                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val s = response.body()!!.string()
                    LogUtils.d( "=============================Response:$s")
                    upLoadGson = Gson().fromJson(s, UpLoadGson::class.java)
                    hd.sendEmptyMessage(UPLOAD_SFZ)
                }
            })
        }


        if (resultCode == Activity.RESULT_OK) {
            if (null != data) {
                //指定了路径，部分手机没有data，但是有的貌似还是有，很奇怪的事
                if (data.hasExtra("data")) {

                    val name = DateFormat.format("yyyymmdd",
                            Calendar.getInstance(Locale.CHINA)).toString()+ ".jpg"
                    val bmp = data.getParcelableExtra<Bitmap>("data")// 解析返回的图片成bitmap

                    // 保存文件
                    var fos: FileOutputStream? = null
                    if (mImagePath != null) {
                        try {
                            val file = File(mImagePath)
                            if (!file.exists()) {
                                file.mkdirs()// 创建文件夹
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@ShiMingRenZhengActivity, "未知错误", Toast.LENGTH_SHORT).show()
                        }

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
                    OkhttpTool.getOkhttpTool().upLoadImage(UrlConfig.UPLOAD_SFZ, f, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {

                            LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)

                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            val s = response.body()!!.string()
                            LogUtils.d( "=============================Response:$s")
                            upLoadGson = Gson().fromJson(s, UpLoadGson::class.java)
                            hd.sendEmptyMessage(UPLOAD_SFZ)
                        }
                    })
                } else {
                    CustomToast.showToast(this@ShiMingRenZhengActivity, "没有data", Toast.LENGTH_SHORT)
                }
            } else {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                if (imageFileUri != null) {
                    val actualimagecursor = managedQuery(imageFileUri, proj, null, null, null)
                    val actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    actualimagecursor.moveToFirst()
                    val img_path = actualimagecursor.getString(actual_image_column_index)
                    val file = File(img_path)
                    OkhttpTool.getOkhttpTool().upLoadImage(UrlConfig.UPLOAD_SFZ, file, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {

                            LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)

                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {

                            val s = response.body()!!.string()
                            LogUtils.d( "=============================Response:$s")
                            upLoadGson = Gson().fromJson(s, UpLoadGson::class.java)
                            hd.sendEmptyMessage(UPLOAD_SFZ)

                        }
                    })
                }
            }
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
                ToastUtils.showShort("打开相机失败，请重试")
            }
        } else {
            CustomToast.showToast(this, "内存卡不存在！", Toast.LENGTH_LONG)
        }
    }

}
