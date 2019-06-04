package chen.vike.c680.main

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import chen.vike.c680.tools.ActivityController
import com.gyf.barlibrary.ImmersionBar

/**
 * Created by lht on 2017/1/17.
 */

open class BaseActivity : AppCompatActivity() {
    internal var i: Int = 0
    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private val contentViewGroup: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //强制竖屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ActivityController.addActivity(this)

        val actionBar = supportActionBar
        actionBar?.hide()

        ImmersionBar.with(this)
                //                .fitsSystemWindows(true)
                //                .statusBarColor(R.color.color_f0)
                //                .autoDarkModeEnable(true,0.2f)
                //                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .init()
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityController.removeActivity(this)
//        OkhttpTool.getOkhttpTool().cancelRequest()
        ImmersionBar.with(this).destroy()
    }

    //    protected void setFitSystemWindow(boolean fitSystemWindow) {
    //        if (contentViewGroup == null) {
    //            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    //        }
    //        contentViewGroup.setFitsSystemWindows(false);
    //    }

    //    @Override
    //    public boolean onTouchEvent(MotionEvent event) {
    //        if(null != this.getCurrentFocus()){
    //            /**
    //             * 点击空白位置 隐藏软键盘
    //             * 有时候会失效,可用于布局较少和点击事件较少的界面
    //             */
    //            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    //            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    //        }
    //        return super.onTouchEvent(event);
    //    }


    //隐藏软键盘，基本不会出问题，缺点是代码有点多
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v!!.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }

    companion object {

        protected var TAG = "BaseActivity"
    }


}
