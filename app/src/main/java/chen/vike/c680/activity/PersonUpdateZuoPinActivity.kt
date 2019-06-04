package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.*
import chen.vike.c680.adapter.GridViewAdapter
import chen.vike.c680.bean.ImageBean
import chen.vike.c680.bean.ZuoPinBean
import chen.vike.c680.main.PictureSelectorConfig
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import chen.vike.c680.tools.BaseStatusBarActivity
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.webview.WebViewActivity
import chen.vike.c680.views.LoadingDialog
import com.lht.vike.a680_v1.R
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import kotterknife.bindView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Created by lht on 2018/4/26.
 * 上传作品
 */

class PersonUpdateZuoPinActivity : BaseStatusBarActivity() {
    internal val zuopinTitle: EditText by bindView(R.id.zuopin_title)
    internal val zuopinPrice: EditText by bindView(R.id.zuopin_price)
    internal val zuopinZhuangtai: Spinner by bindView(R.id.zuopin_zhuangtai)
    internal val zuopinCategory: Spinner by bindView(R.id.zuopin_category)
    internal val gridView: GridView by bindView(R.id.gridView)
    internal val zuopinTijiao: Button by bindView(R.id.zuopin_tijiao)

    private var mContext: Context? = null
    private val mPicList = ArrayList<String>() //上传的图片凭证的数据源
    private val pathImgList = ArrayList<String>()
    private var mGridViewAddImgAdapter: GridViewAdapter? = null //展示上传的图片的适配器
    private var Ztai = ""
    private val files = ArrayList<File>()
    private val NETWORK_EXCEPTION = 0X111
    private var ld: LoadingDialog? = null


    /**
     * 上传图片
     */
    private var imageBean: ImageBean? = null
    private var imagHttpPath: String? = null

    /**
     * 上传作品
     */
    private var zuoPinBean: ZuoPinBean? = null









    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                ERROR -> Toast.makeText(this@PersonUpdateZuoPinActivity, "未知错误", Toast.LENGTH_SHORT).show()
                IMGLOAD -> {
                    val imgPATH = imagHttpPath!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    for (i in imgPATH.indices) {
                        mPicList.add(imgPATH[i])
                    }
                    mGridViewAddImgAdapter!!.notifyDataSetChanged()
                }
                ZERROR -> Toast.makeText(this@PersonUpdateZuoPinActivity, zuoPinBean!!.err_msg, Toast.LENGTH_SHORT).show()
                ZPLOAD -> {
                    Toast.makeText(this@PersonUpdateZuoPinActivity, zuoPinBean!!.err_msg, Toast.LENGTH_SHORT).show()
                    val intent = Intent(mContext, WebViewActivity::class.java)
                    var s = ""
                    if (MyApplication.userInfo != null) {
                        s = MyApplication.userInfo!!.userID
                    }
                    intent.putExtra("weburl", "http://apps.680.com/app/zuopin/list.aspx?userid=$s")
                    intent.putExtra("title", "我的作品")
                    startActivityForResult(intent, 1)
                    finish()
                }
                NETWORK_EXCEPTION -> LhtTool.showNetworkException(this@PersonUpdateZuoPinActivity, msg)
                else -> {
                }
            }
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_zuopin_page)
        title.text = "上传作品"
        mContext = this
        initGridView()
        setSp()
        listener()
    }

    /**
     * 监听事件
     */
    private fun listener() {
        zuopinTijiao.setOnClickListener {
            val s = zuopinTitle.text.toString()
            if (zuopinTitle.text.toString() == "") {
                Toast.makeText(this@PersonUpdateZuoPinActivity, "请输入标题", Toast.LENGTH_SHORT).show()
            } else if (zuopinPrice.text.toString() == " ") {
                Toast.makeText(this@PersonUpdateZuoPinActivity, "请输入价格", Toast.LENGTH_SHORT).show()
            } else {
                ld = LoadingDialog(this@PersonUpdateZuoPinActivity).setMessage("提交中....")
                ld?.show()
                uploadZuopin()
            }
        }
    }

    private fun updateImg() {
        val mapImg = HashMap<String, Any>()
        mapImg["userid"] = MyApplication.userInfo!!.userID
        mapImg["type"] = "zuopin"
        // mapImg.put("debug","1");
        OkhttpTool.getOkhttpTool().updateMoreImg(UrlConfig.UPDATEZUOPINGIMG, PATH_FILES, mapImg, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d("fileIMG : $s")
                    imageBean = Gson().fromJson(s, ImageBean::class.java)
                    if (imageBean != null) {
                        if (imageBean!!.error == 1) {
                            handler.sendEmptyMessage(ERROR)
                        } else {
                            imagHttpPath = imageBean!!.url
                            handler.sendEmptyMessage(IMGLOAD)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

    }

    private fun uploadZuopin() {
        val zuoMap = HashMap<String, Any>()
        zuoMap["userid"] = MyApplication.userInfo!!.userID
        zuoMap["act"] = "add"
        zuoMap["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
        zuoMap["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
        zuoMap["crv_title"] = zuopinTitle.text.toString() + ""
        zuoMap["price"] = zuopinPrice.text.toString() + ""
        if (Ztai == "已中标") {
            zuoMap["iszb"] = "1"
        } else {
            zuoMap["iszb"] = "0"
        }
        if (mPicList.size > 0) {
            when (mPicList.size) {
                1 -> zuoMap["fm"] = mPicList[0]
                2 -> {
                    zuoMap["fm"] = mPicList[0]
                    zuoMap["zp"] = mPicList[1]
                }
                3 -> {
                    zuoMap["fm"] = mPicList[0]
                    zuoMap["zp"] = mPicList[1]
                    zuoMap["zp1"] = mPicList[2]
                }
                4 -> {
                    zuoMap["fm"] = mPicList[0]
                    zuoMap["zp"] = mPicList[1]
                    zuoMap["zp1"] = mPicList[2]
                    zuoMap["zp2"] = mPicList[3]
                }
                5 -> {
                    zuoMap["fm"] = mPicList[0]
                    zuoMap["zp"] = mPicList[1]
                    zuoMap["zp1"] = mPicList[2]
                    zuoMap["zp2"] = mPicList[3]
                    zuoMap["zp3"] = mPicList[4]
                }
            }
        } else {
            ToastUtils.showShort("请上传图片")
        }
        OkhttpTool.getOkhttpTool().post(UrlConfig.UPDATEZUOPING, zuoMap, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    Log.e("zuopin", s + "")
                    zuoPinBean = Gson().fromJson(s, ZuoPinBean::class.java)
                    if (zuoPinBean != null) {
                        if (zuoPinBean!!.err_code == "1") {
                            handler.sendEmptyMessage(ZERROR)
                        } else {
                            handler.sendEmptyMessage(ZPLOAD)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                ld?.dismiss()
            }
        })
    }

    /**
     * 作品状态
     */
    private fun setSp() {
        val province_adapter = ArrayAdapter.createFromResource(this, R.array.zuopin_ztai, android.R.layout.simple_spinner_item)
        province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val ican_adapter = ArrayAdapter.createFromResource(this, R.array.zuopin_fenlei, android.R.layout.simple_spinner_item)
        ican_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        zuopinCategory.adapter = ican_adapter
        zuopinZhuangtai.adapter = province_adapter
        // zuopinZhuangtai.setSelection(1, true);
        zuopinZhuangtai.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                Ztai = zuopinZhuangtai.selectedItem.toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
        //  zuopinCategory.setAdapter(category_adapter);
    }

    //初始化展示上传图片的GridView
    private fun initGridView() {
        mGridViewAddImgAdapter = GridViewAdapter(mContext, mPicList)
        gridView.adapter = mGridViewAddImgAdapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (position == parent.childCount - 1) {
                //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过5张，才能点击
                if (mPicList.size == MAX_SELECT_PIC_NUM) {
                    //最多添加5张图片
                    viewPluImg(position)
                } else {
                    //添加凭证图片
                    selectPic(MAX_SELECT_PIC_NUM - mPicList.size)
                }
            } else {
                viewPluImg(position)
            }
        }
    }

    //查看大图
    private fun viewPluImg(position: Int) {
        val intent = Intent(mContext, PersonPlusImageActivity::class.java)
        intent.putStringArrayListExtra(IMG_LIST, mPicList)
        intent.putExtra(POSITION, position)
        intent.putExtra(PIC, 1)
        startActivityForResult(intent, REQUEST_CODE_MAIN)
    }

    /**
     * 打开相册或者照相机选择凭证图片，最多5张
     *
     * @param maxTotal 最多选择的图片的数量
     */
    private fun selectPic(maxTotal: Int) {
        PictureSelectorConfig.initMultiConfig(this, maxTotal)
    }

    // 处理选择的照片的地址
    private fun refreshAdapter(picList: List<LocalMedia>) {
//        if (PATH_FILES.size > 0) {
//            PATH_FILES.clear()
//        }
        for (localMedia in picList) {
            //被压缩后的图片路径
            if (localMedia.isCompressed) {
                val compressPath = localMedia.compressPath //压缩后的图片路径
                val file = File(compressPath)
                files.add(file)
                // mPicList.add(""); //把图片添加到将要上传的图片数组中
                if (!PATH_FILES.contains(file)){
                    PATH_FILES.add(file)
                }
                mGridViewAddImgAdapter!!.notifyDataSetChanged()
            }
        }
        updateImg()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST ->
                    // 图片选择结果回调
                refreshAdapter(PictureSelector.obtainMultipleResult(data))
            }
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
        }
        if (requestCode == REQUEST_CODE_MAIN && resultCode == RESULT_CODE_VIEW_IMG) {
            //查看大图页面删除了图片
            val toDeletePicList = data!!.getStringArrayListExtra(IMG_LIST) //要删除的图片的集合
            mPicList.clear()
            mPicList.addAll(toDeletePicList)
            mGridViewAddImgAdapter!!.notifyDataSetChanged()
            // updateImg();
        }
    }

    companion object {
        private val TAG = "PersonUpdateZuoPinActivity"
        private val ERROR = 0x123
        private val IMGLOAD = 0x125
        private val ZERROR = 0x126
        private val ZPLOAD = 0x127
        val IMG_LIST = "img_list" //第几张图片
        val POSITION = "position" //第几张图片
        val PIC_PATH = "pic_path" //图片路径
        val PIC = "ispic"
        var PATH_FILES = ArrayList<File>()//图片地址
        val MAX_SELECT_PIC_NUM = 5 // 最多上传5张图片
        val REQUEST_CODE_MAIN = 10 //请求码
        val RESULT_CODE_VIEW_IMG = 11 //查看大图页面的结果码
    }
}
