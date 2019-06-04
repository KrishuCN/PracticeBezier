package chen.vike.c680.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.gyf.barlibrary.BarHide
import com.gyf.barlibrary.ImmersionBar
import com.lht.vike.a680_v1.R
import java.lang.ref.WeakReference
import java.util.*

/**
 * `
 * Created by lht on 2017/3/2.
 */

class GuidePage : BaseActivity() {

    private var gallery: Gallery? = null
    private var iv: ImageView? = null
    private val image = intArrayOf(R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3)
    private var radioGroup: RadioGroup? = null
    private var timer: Timer? = null
    private var btn: Button? = null
    private var k: Int = 0
    private val mHandler: MHandler = MHandler(this)
    private val LUNBO = 0x123


    private class MHandler constructor(guidePage: GuidePage):Handler(){
        private val weakReference: WeakReference<GuidePage> = WeakReference(guidePage)
        private val gudePage = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg!!.what == gudePage?.LUNBO) {
                if (gudePage.k >= 3) {
                    gudePage.timer?.cancel()
                    return
                }
                gudePage.k %= 3
                gudePage.gallery?.setSelection(gudePage.k)
                gudePage.k++
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        /**
         * 隐藏状态栏
         */
        ImmersionBar.with(this)
                .fullScreen(true)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init()


        gallery = findViewById(R.id.guide_vp)
        radioGroup = findViewById(R.id.rg)

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        gallery!!.adapter = object : BaseAdapter() {
            override fun getCount(): Int {
                return image.size
            }

            override fun getItem(position: Int): Any {
                return position
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
                var mView = view

                if (position <= 1) {
                    mView = ImageView(this@GuidePage)
                    mView.scaleType = ImageView.ScaleType.CENTER_CROP
                    mView.setImageResource(image[position])
                    mView.setLayoutParams(Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                } else if (position == 2) {
                    mView = LayoutInflater.from(this@GuidePage).inflate(R.layout.guide_item, null)
                    iv = mView.findViewById(R.id.iv)
                    iv!!.scaleType = ImageView.ScaleType.CENTER_CROP
                    iv!!.setImageResource(image[position])
                    btn = mView.findViewById(R.id.bt_yin)
                    btn!!.setOnClickListener {
                        val `in` = Intent(this@GuidePage, MainActivity::class.java)
                        startActivity(`in`)
                        this@GuidePage.finish()
                    }
                    mView.layoutParams = Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                }
                return mView!!
            }
        }

        radioGroup!!.setOnCheckedChangeListener { _, i ->
            var xuanzhong = 0
            when (i) {
                R.id.rb -> xuanzhong = 0
                R.id.rb1 -> xuanzhong = 1
                R.id.rb2 -> xuanzhong = 2
            }
            gallery!!.setSelection(xuanzhong)
        }

        gallery!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, i: Int, l: Long) {
                val rb = radioGroup?.getChildAt(i) as RadioButton
                rb.isChecked = true
                k = i
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
    }


    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                mHandler.sendEmptyMessage(LUNBO)
            }
        }, 2500, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        mHandler.removeCallbacksAndMessages(null)
    }
}
