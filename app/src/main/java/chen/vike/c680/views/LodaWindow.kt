package chen.vike.c680.views

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import chen.vike.c680.activity.SheZhiActivity
import com.lht.vike.a680_v1.R

/**
 * Created by lht on 2018/8/1.
 * 加载中弹窗
 */

class LodaWindow(context: Context, activity: Activity) : PopupWindow() {
    private val viewShow: View = LayoutInflater.from(context).inflate(R.layout.loading, null)
    private val popupWindow: PopupWindow?
    private val loadLayout: RelativeLayout
    private var textView: TextView? = null

    init {
        popupWindow = PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00ffffff")))//也可以直接把Color.TRANSPARENT换成0
        // popupWindow.setAnimationStyle(R.style.popWindow_animation);
        val lp = activity.window.attributes
        //lp.alpha = 0.6f; //0.0-1.0
        activity.window.attributes = lp
        //搞这么多判断都是为了Monkey测试下的特殊情况这里会报错
        if (viewShow != null && activity != null && !activity.isDestroyed && !activity.isFinishing) {
            if (ActivityUtils.isActivityExists(AppUtils.getAppPackageName(), SheZhiActivity::class.java.name)) {
                popupWindow.showAtLocation(viewShow, Gravity.CENTER, 0, 0)
                popupWindow.setOnDismissListener {
                    val lp = activity.window.attributes
                    lp.alpha = 1.0f //0.0-1.0
                    activity.window.attributes = lp
                }
            }
        }
        loadLayout = viewShow.findViewById(R.id.load_layout)
    }

    fun setMessage(message: String) {
        textView = viewShow.findViewById(R.id.tv_text)
        textView!!.text = message
    }

    fun dis() {
        popupWindow?.dismiss()
    }

    fun gone() {
        if (popupWindow != null) {
            loadLayout.visibility = View.INVISIBLE
        }
    }
}
