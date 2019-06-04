package chen.vike.c680.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.View
import android.widget.TextView

import chen.vike.c680.main.BaseActivity
import com.lht.vike.a680_v1.R

import java.io.File
import java.util.ArrayList

import chen.vike.c680.adapter.ViewPagerAdapter
import chen.vike.c680.views.CancelOrOkDialog

class PersonPlusImageActivity : BaseActivity(), ViewPager.OnPageChangeListener, View.OnClickListener {

    private var viewPager: ViewPager? = null //展示图片的ViewPager
    private var positionTv: TextView? = null //图片的位置，第几张图片
    private var imgList: ArrayList<String>? = null //图片的数据源
    private val pathList: ArrayList<File>? = null//图片文字地址
    private var mPosition: Int = 0
    private var ispic: Int = 0 //第几张图片
    private var mAdapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plus_image)

        imgList = intent.getStringArrayListExtra(PersonUpdateZuoPinActivity.IMG_LIST)
        mPosition = intent.getIntExtra(PersonUpdateZuoPinActivity.POSITION, 0)
        ispic = intent.getIntExtra(PersonUpdateZuoPinActivity.PIC, 0)
        initView()
    }

    private fun initView() {
        viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        positionTv = findViewById<View>(R.id.position_tv) as TextView
        findViewById<View>(R.id.back_iv).setOnClickListener(this)
        findViewById<View>(R.id.delete_iv).setOnClickListener(this)
        viewPager!!.addOnPageChangeListener(this)

        mAdapter = ViewPagerAdapter(this, imgList)
        viewPager!!.adapter = mAdapter
        positionTv!!.text = (mPosition + 1).toString() + "/" + imgList!!.size
        viewPager!!.currentItem = mPosition
    }

    //删除图片
    private fun deletePic() {
        val dialog = object : CancelOrOkDialog(this, "要删除这张图片吗?") {
            override fun ok() {
                super.ok()
                imgList!!.removeAt(mPosition) //从数据源移除删除的图片
                if (ispic == 1) {
                    if (PersonUpdateZuoPinActivity.PATH_FILES.size > 0) {
                        PersonUpdateZuoPinActivity.PATH_FILES.removeAt(mPosition)
                    }
                }
                setPosition()
                dismiss()
            }
        }
        dialog.show()
    }

    //设置当前位置
    @SuppressLint("SetTextI18n")
    private fun setPosition() {
        positionTv!!.text = (mPosition + 1).toString() + "/" + imgList!!.size
        viewPager!!.currentItem = mPosition
        mAdapter!!.notifyDataSetChanged()
    }

    //返回上一个页面
    private fun back() {
        val intent = intent
        intent.putStringArrayListExtra(PersonUpdateZuoPinActivity.IMG_LIST, imgList)
        setResult(PersonUpdateZuoPinActivity.RESULT_CODE_VIEW_IMG, intent)
        finish()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        mPosition = position
        positionTv!!.text = (position + 1).toString() + "/" + imgList!!.size
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back_iv ->
                //返回
                back()
            R.id.delete_iv ->
                //删除图片
                deletePic()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //按下了返回键
            back()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
