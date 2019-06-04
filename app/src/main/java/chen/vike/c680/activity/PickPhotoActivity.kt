package chen.vike.c680.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import chen.vike.c680.adapter.TuKuGroupAdapter
import chen.vike.c680.bean.TuKuImageBean
import com.blankj.utilcode.util.ToastUtils
import com.gyf.barlibrary.ImmersionBar
import com.lht.vike.a680_v1.R
import java.io.File
import java.lang.ref.WeakReference
import java.util.*

/**
 * 自定义图库Activity
 * 遍历图片
 */
class PickPhotoActivity : Activity() {
    private var mGruopMap = HashMap<String, List<String>>()
    private var list: List<TuKuImageBean>? = ArrayList()
    private var mProgressDialog: ProgressDialog? = null
    private var adapter: TuKuGroupAdapter? = null
    private var mGroupGridView: GridView? = null
    private var mHandler: MHandler = MHandler(this)

    private class MHandler(activity: PickPhotoActivity) : Handler() {
        private val weakReference: WeakReference<PickPhotoActivity> = WeakReference(activity)
        private var activity = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                SCAN_OK -> {
                    //关闭进度条
                    activity?.run {
                        mProgressDialog?.dismiss()
                        list = subGroupOfImage(mGruopMap)
                        if (list!=null){
                            adapter = TuKuGroupAdapter(this@run, list!!, mGroupGridView!!)
                            mGroupGridView!!.adapter = adapter
                        }else{
                            ToastUtils.showShort("读取相册失败，请重试")
                        }
                    }
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imag)
        ImmersionBar.with(this).init()
        mGroupGridView = findViewById<View>(R.id.main_grid) as GridView

        getImages()

        mGroupGridView!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val childList = mGruopMap[list!![position].folderName]
            if (stringList.size > 0) {
                stringList.clear()
            }
            val mIntent = Intent(this@PickPhotoActivity, TuKuShowImageActivity::class.java)
            mIntent.putStringArrayListExtra("data", childList as ArrayList<String>?)
            mIntent.putExtra("number", intent.getStringExtra("number"))
            startActivityForResult(mIntent, 1)
        }

    }

    //这里判断返回的有没有选择图片，有就退出，没有就继续在这个页面。
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (stringList.size > 0) {
            setResult(1112)
            //            Toast.makeText(this,stringList.get(0),Toast.LENGTH_SHORT).show();
            finish()
        } else if (stringList.size == 0) {

        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private fun getImages() {
        //显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...")

        Thread(Runnable {
            val mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val mContentResolver = this@PickPhotoActivity.contentResolver

            //只查询jpeg和png的图片
            val mCursor = mContentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    arrayOf("image/jpeg", "image/png"), MediaStore.Images.Media.DATE_MODIFIED)
                    ?: return@Runnable

            while (mCursor.moveToNext()) {
                //获取图片的路径
                val path = mCursor.getString(mCursor
                        .getColumnIndex(MediaStore.Images.Media.DATA))

                //获取该图片的父路径名
                val parentName = File(path).parentFile.name


                //根据父路径名将图片放入到mGruopMap中
                if (!mGruopMap.containsKey(parentName)) {
                    val chileList = ArrayList<String>()
                    chileList.add(path)
                    mGruopMap[parentName] = chileList
                } else {
                    mGruopMap[parentName]!!.toMutableList().add(path)
                }
            }

            //通知Handler扫描图片完成
            mHandler.sendEmptyMessage(SCAN_OK)
            mCursor.close()
        }).start()

    }


    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     *
     * @param mGruopMap
     * @return
     */
    private fun subGroupOfImage(mGruopMap: HashMap<String, List<String>>): List<TuKuImageBean>? {
        if (mGruopMap.size == 0) {
            return null
        }
        val list = ArrayList<TuKuImageBean>()

        val it = mGruopMap.entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            val mImageBean = TuKuImageBean()
            val key = entry.key
            val value = entry.value

            mImageBean.folderName = key
            mImageBean.imageCounts = value.size
            mImageBean.topImagePath = value[0]//获取该组的第一张图片

            list.add(mImageBean)
        }

        return list

    }

    override fun onDestroy() {
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }

    companion object {
        private val SCAN_OK = 1
        var stringList: MutableList<String> = ArrayList()
    }

}
