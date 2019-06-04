/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: XuQiuJiJianFrag
 * Author: chen
 * Date: 2019/1/23 15:14
 * Description: 我的需求计件项目
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
</desc></version></time></author> */
package chen.vike.c680.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import chen.vike.c680.Interface.ViewItemClick
import chen.vike.c680.adapter.MyXuQiuAdapter
import chen.vike.c680.bean.XuQiuList
import chen.vike.c680.main.MyApplication
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.tools.UrlConfig
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.EmptySwipeRefreshLayout
import chen.vike.c680.views.LoadingDialog
import chen.vike.c680.views.MyListView2
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.lht.vike.a680_v1.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

class XuQiuJiJianFrag : Fragment(), MyListView2.OnLoadListener {


    internal lateinit var fabuListLv: MyListView2
    internal lateinit var fabuListSwipe: EmptySwipeRefreshLayout
    private var mContext: Context? = null
    private var quanBuView: View? = null
    private val map = HashMap<String, Any>()
    private var xuQiuList: XuQiuList? = null
    private val GETINFO = 0X123
    private val NETWORK_EXCEPTION = 0X121
    private val ONLOAD = 0X111
    private var myXuQiuAdapter: MyXuQiuAdapter? = null

    /**
     * 删除监听
     */
    internal var viewItemClick: ViewItemClick = ViewItemClick { position -> showDleWindow(xuQiuList!!.list, position) }
    /**
     * 显示删除的弹窗
     */
    private var viewShow: View? = null
    private var popupWindow: PopupWindow? = null

    private var del_price: TextView? = null
    private var del_content: TextView? = null
    private var del_queren: Button? = null
    private var del_quexiao: Button? = null
    private var deleteNum: Int = 0
    private var ld: LoadingDialog? = null
    private val DELETE = 0X122
    private val hd = MHandler(this)
    private var type: String? = null

    private class MHandler(xuQiuJiJianFrag: XuQiuJiJianFrag) : Handler() {
        private val weakReference: WeakReference<XuQiuJiJianFrag> = WeakReference(xuQiuJiJianFrag)
        private val ref = weakReference.get()
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            ref?.run {
                if (msg?.what == GETINFO) {

                    fabuListLv.isLoadEnable = true
                    if (null != xuQiuList) {
                        if (xuQiuList!!.pagerInfo.nextPageIndex == 0) {
                            //此处判断是否有下一页，用于将footview直接屏蔽掉
                            fabuListLv.isLoadEnable = false
                        }
                        myXuQiuAdapter!!.refresh()
                        myXuQiuAdapter!!.addList(xuQiuList!!.list)
                    } else {
                        myXuQiuAdapter!!.refresh()
                    }
                    fabuListSwipe.isRefreshing = false
                    myXuQiuAdapter!!.notifyDataSetChanged()
                    fabuListLv.setSelection(0)

                } else if (msg?.what == NETWORK_EXCEPTION) {

                    LhtTool.showNetworkException(mContext, msg)

                } else if (msg?.what == ONLOAD) {

                    myXuQiuAdapter!!.addList(xuQiuList!!.list)
                    fabuListLv.onLoadComplete()
                    myXuQiuAdapter!!.notifyDataSetChanged()

                } else if (msg?.what == DELETE) {
                    val s = msg.data.getString("s")
                    when (s) {
                        "unlogin" -> CustomToast.showToast(mContext, "未登录", Toast.LENGTH_SHORT)
                        "nofwid" -> CustomToast.showToast(mContext, "id无效", Toast.LENGTH_SHORT)
                        "ok" -> {
                            CustomToast.showToast(mContext, "删除成功", Toast.LENGTH_SHORT)
                            myXuQiuAdapter!!.remove(deleteNum)
                            myXuQiuAdapter!!.notifyDataSetChanged()
                        }
                        "fail" -> CustomToast.showToast(mContext, "删除失败", Toast.LENGTH_SHORT)
                    }
                    ld?.dismiss()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = activity
        type = arguments?.getString("type")
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        quanBuView = LayoutInflater.from(mContext).inflate(R.layout.activity_myxuqiu_list_item, null)
        return quanBuView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabuListLv = view.findViewById(R.id.fabu_list_lv)
        fabuListSwipe = view.findViewById(R.id.fabu_list_swipe)
        iniview()
        isLoginFresh()


        val viewNo = LayoutInflater.from(mContext).inflate(R.layout.view_no, null)
        val t = viewNo.findViewById<TextView>(R.id.no_txt)
        //t在view里写的的字符串会被消掉，只有自己手动添加
        t.text = "没有发布项目"
        val viewGroup:ViewGroup = view.parent as ViewGroup
        viewGroup.removeAllViews()
        viewGroup.addView(view)
        fabuListLv.emptyView = view
        fabuListSwipe.setOnRefreshListener { doRefresh() }

    }

    private fun iniview() {
        myXuQiuAdapter = MyXuQiuAdapter(ArrayList(), mContext, map)
        myXuQiuAdapter!!.viewItemClick = viewItemClick
        fabuListLv.adapter = myXuQiuAdapter
        fabuListLv.setOnLoadListener(this)
    }

    /**
     * 判断登录  加载数据
     */
    private fun isLoginFresh() {
        if (LhtTool.isLogin) {
            map["userid"] = MyApplication.userInfo!!.userID
            map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
            map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
            map["num"] = 10
            map["pages"] = 1
            map["type"] = type!!
            fabuListSwipe.isRefreshing = true
            doRefresh()
        }
    }

    /**
     * 刷新数据
     */
    private fun doRefresh() {

        map["pages"] = "1"
        OkhttpTool.getOkhttpTool().post(UrlConfig.GET_FABU_LIST, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d("======================Response:$s")
                    xuQiuList = Gson().fromJson(s, XuQiuList::class.java)
                    hd.sendEmptyMessage(GETINFO)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        })
    }

    /**
     * 加载数据
     */
    private fun doLoad() {
        OkhttpTool.getOkhttpTool().post(UrlConfig.GET_FABU_LIST, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val s = response.body()!!.string()
                    LogUtils.d("======================Response:$s")
                    xuQiuList = Gson().fromJson(s, XuQiuList::class.java)
                    hd.sendEmptyMessage(ONLOAD)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        })
    }

    private fun showDleWindow(list: List<XuQiuList.ListBean>, position: Int) {
        viewShow = LayoutInflater.from(mContext).inflate(R.layout.delete_my_fuwu_window_item, null)
        popupWindow = PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        popupWindow!!.isFocusable = true
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#F8F8F8")))//也可以直接把Color.TRANSPARENT换成0
        popupWindow!!.animationStyle = R.style.popWindow_animation
        val lp = activity!!.window.attributes
        lp.alpha = 0.6f //0.0-1.0
        activity!!.window.attributes = lp
        popupWindow!!.showAtLocation(viewShow, Gravity.CENTER, 0, 0)
        popupWindow!!.setOnDismissListener {
            val lp = activity!!.window.attributes
            lp.alpha = 1.0f //0.0-1.0
            activity!!.window.attributes = lp
        }
        showData(list, position)
    }

    private fun showData(list: List<XuQiuList.ListBean>?, position: Int) {
        del_price = viewShow!!.findViewById(R.id.fuwu_delete_price_text)
        del_content = viewShow!!.findViewById(R.id.fuwu_delete_content_text)
        del_queren = viewShow!!.findViewById(R.id.fuwu_delete_fuwu_queren)
        del_quexiao = viewShow!!.findViewById(R.id.fuwu_delete_fuwu_quxiao)
        if (list != null && list.size > 0) {
            del_price!!.text = "￥" + list[position].money + "元"
            del_content!!.text = list[position].itemname
        } else {
            del_price!!.text = "未知错误请重新进入尝试"
        }
        del_queren!!.setOnClickListener {
            deleteNum = position
            DeleSeverce(list!![position].itemid)
            popupWindow!!.dismiss()
        }
        del_quexiao!!.setOnClickListener { popupWindow!!.dismiss() }
    }

    fun DeleSeverce(id: String) {
        if (LhtTool.isLogin) {
            map["itemId"] = id
            map["loginUserId"] = MyApplication.userInfo!!.userID
            map["userid"] = MyApplication.userInfo!!.userID
            map["vkuserip"] = MyApplication.userInfo!!.cookieLoginIpt
            map["vktoken"] = MyApplication.userInfo!!.cookieLoginToken
            //  map.put("fuwu_id",id);
            // map.put("type","del");
            map["num"] = "1"
            map["pages"] = "1"
            map["seltype"] = "all"
            OkhttpTool.getOkhttpTool().post(UrlConfig.DELETE_PROJECT, map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                    LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION)

                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {

                    val s = response.body()!!.string()
                    LogUtils.d("=============response:$s")
                    val ms = Message()
                    ms.what = DELETE
                    val b = Bundle()
                    b.putString("s", s)
                    ms.data = b
                    hd.sendMessage(ms)

                }
            })
            ld = LoadingDialog(mContext!!).setMessage("删除中...")
            ld!!.show()
        }
    }

    override fun onLoad() {

        if (xuQiuList!!.pagerInfo.nextPageIndex == 0) {
            CustomToast.showToast(mContext, "没有更多了！", Toast.LENGTH_SHORT)
            fabuListLv.isLoadEnable = false
            return
        }
        map["pages"] = xuQiuList!!.pagerInfo.nextPageIndex
        doLoad()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        hd.removeCallbacksAndMessages(null)
    }

}
