package chen.vike.c680.tools

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.gyf.barlibrary.ImmersionBar
import com.lht.vike.a680_v1.R


/**
 * Created by lht on 2017/3/3.
 */

open class BaseStatusBarActivity : AppCompatActivity() {
    lateinit var back: ImageView
    lateinit var title: TextView
    lateinit var img: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getTitle()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ActivityController.addActivity(this)
        val lp = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        val mActionBarView = LayoutInflater.from(this).inflate(R.layout.nomal_actionbar, null)
        val actionBar = supportActionBar
//        actionBar!!.hide()
        actionBar!!.setCustomView(mActionBarView, lp)
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setDisplayShowCustomEnabled(true)// 使自定义的普通View能在title栏显示
        actionBar.setDisplayShowHomeEnabled(true)// 隐藏 ActionBar 图标
        actionBar.setDisplayShowTitleEnabled(true)// 隐藏 ActionBar 标题
        actionBar.setDisplayHomeAsUpEnabled(false)// 将应用程序图标设置为可点击，并在图标上添加向左箭头

        back = actionBar.customView.findViewById(R.id.action_back)
        img = actionBar.customView.findViewById(R.id.bar_img)
        title = actionBar.customView.findViewById(R.id.action_title)
        back.setOnClickListener { finish() }

        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .autoDarkModeEnable(true, 0.2f)
                .supportActionBar(true)
                .init()


    }


    override fun onDestroy() {
        super.onDestroy()
        ActivityController.removeActivity(this)
//        OkhttpTool.getOkhttpTool().cancelRequest()
        //        TcStatInterface.recordAppEnd();
        ImmersionBar.with(this).destroy()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (null != this.currentFocus) {
            /**
             * 点击空白位置 隐藏软键盘
             * 有时候会失效,可用于布局较少和点击事件较少的界面
             */
            val mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    companion object {
        var TAG = "BaseStatusBarActivity"
    }

}
