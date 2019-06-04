package chen.vike.c680.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast

import com.google.gson.Gson
import chen.vike.c680.tools.LhtTool
import chen.vike.c680.tools.OkhttpTool
import chen.vike.c680.main.MyApplication
import chen.vike.c680.views.CustomToast
import chen.vike.c680.views.EmptySwipeRefreshLayout
import chen.vike.c680.views.MyListView2
import com.lht.vike.a680_v1.R

import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

import chen.vike.c680.adapter.UseDaoJuAdapter
import chen.vike.c680.bean.UseDaoJuBean
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

/**
 * Created by lht on 2018/6/12.
 */

class PersonUseDaoJuFrag : Fragment(), MyListView2.OnLoadListener {
    internal var useDaojuListLv: MyListView2? = null
    internal var useDaojuListSwipe: EmptySwipeRefreshLayout? = null
    private var useView: View? = null
    private var mContext: Context? = null
    private var useDaoJuAdapter: UseDaoJuAdapter? = null
    private val map = HashMap<String, Any>()
    private val lists = ArrayList<UseDaoJuBean.ListBean>()
    private val NETWORK_EXCEPTION = 0X111
    private val ONLOAD = 0X131

    /**
     * 刷新数据
     */
    private var useDaoJuBean: UseDaoJuBean? = null

    @SuppressLint("HandlerLeak")
    internal var useHd: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                JIAZAISHUJU -> {
                    useDaojuListLv!!.isLoadEnable = true
                    if (useDaoJuBean!!.pagerInfo.nextPageIndex == 0) {
                        //此处判断是否有下一页，用于将footview直接屏蔽掉
                        useDaojuListLv!!.isLoadEnable = false
                    }
                    useDaoJuAdapter!!.refresh()
                    useDaoJuAdapter!!.addList(useDaoJuBean!!.list)
                    useDaojuListSwipe!!.isRefreshing = false
                    useDaoJuAdapter!!.notifyDataSetChanged()
                    useDaojuListLv!!.setSelection(0)
                }
                ONLOAD -> if (useDaoJuBean!!.list != null) {
                    useDaoJuAdapter!!.addList(useDaoJuBean!!.list)
                    useDaojuListLv!!.onLoadComplete()
                    useDaoJuAdapter!!.notifyDataSetChanged()
                }
                ERRO -> {
                    CustomToast.showToast(mContext, "暂无数据", Toast.LENGTH_SHORT)
                    useDaojuListSwipe?.isRefreshing = false
                }
                else -> {
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = activity
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        useView = LayoutInflater.from(mContext).inflate(R.layout.activity_use_daoju, null)
        return useView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        viewListener()
        isLoginFresh()
        dingdanData()
    }

    /**
     * 初始化
     */
    private fun setView() {
        useDaojuListLv = useView!!.findViewById(R.id.use_daoju_list_lv)
        useDaojuListSwipe = useView!!.findViewById(R.id.use_daoju_list_swipe)
        useDaojuListSwipe!!.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"))
        val view = LayoutInflater.from(mContext).inflate(R.layout.view_no, null)
        val t = view.findViewById<TextView>(R.id.no_txt)
        //t在view里写的的字符串会被消掉，只有自己手动添加
        t.text = "暂无使用记录!"
        (useDaojuListLv!!.parent as ViewGroup).addView(view)
        useDaojuListLv!!.emptyView = view
        useDaoJuAdapter = UseDaoJuAdapter(mContext)
        useDaojuListLv!!.adapter = useDaoJuAdapter
        useDaojuListLv!!.setOnLoadListener(this)
    }

    /**
     * 判断登录  加载数据
     */
    private fun isLoginFresh() {
        if (LhtTool.isLogin) {
            map["userid"] = MyApplication.userInfo!!.userID
            map["num"] = 10
            map["pages"] = 1
            useDaojuListSwipe!!.isRefreshing = true
        }
    }

    /**
     * 监听事件
     */
    private fun viewListener() {
        useDaojuListSwipe!!.setOnRefreshListener { dingdanData() }
        useDaojuListLv!!.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {

                useDaojuListLv!!.scrollState = scrollState
                useDaojuListLv!!.ifNeedLoad(view, scrollState)

            }

            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                useDaojuListLv!!.firstVisibleItem = firstVisibleItem
                var enable = false
                if (useDaojuListLv != null && useDaojuListLv!!.childCount > 0) {
                    // 检查listView第一个item是否可见
                    val firstItemVisible = useDaojuListLv!!.firstVisiblePosition == 0
                    // 检查第一个item的顶部是否可见
                    val topOfFirstItemVisible = useDaojuListLv!!.getChildAt(0).top == 0
                    // 启用或者禁用SwipeRefreshLayout刷新标识
                    enable = firstItemVisible && topOfFirstItemVisible
                } else if (useDaojuListLv != null && useDaojuListLv!!.childCount == 0) {
                    // 没有数据的时候允许刷新
                    enable = true
                }
                // 把标识传给swipeRefreshLayout
                useDaojuListSwipe!!.isEnabled = enable
            }
        })

    }

    private fun dingdanData() {
        map["userid"] = MyApplication.userInfo!!.userID
        map["num"] = 10
        map["pages"] = 1
        OkhttpTool.getOkhttpTool().post(useUrl, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(useHd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val s = response.body()!!.string()
                Log.e("daoju", s)
                if (s != null && s != "") {
                    useDaoJuBean = Gson().fromJson(s, UseDaoJuBean::class.java)
                    if (useDaoJuBean!!.err_code == "0") {
                        useHd.sendEmptyMessage(JIAZAISHUJU)
                    } else {
                        useHd.sendEmptyMessage(ERRO)
                    }
                } else {
                    useHd.sendEmptyMessage(ERRO)
                }
            }
        })
    }

    /**
     * 加载数据
     */
    private fun doLoad() {
        OkhttpTool.getOkhttpTool().post(useUrl, map, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                LhtTool.sendMessage(useHd, e, NETWORK_EXCEPTION)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val s = response.body()!!.string()

                useDaoJuBean = Gson().fromJson(s, UseDaoJuBean::class.java)
                useHd.sendEmptyMessage(ONLOAD)

            }
        })
    }

    override fun onLoad() {
        if (useDaoJuBean!!.pagerInfo.nextPageIndex == 0) {
            CustomToast.showToast(mContext, "没有更多了！", Toast.LENGTH_SHORT)
            useDaojuListLv!!.isLoadEnable = false
            return
        }
        map["pages"] = useDaoJuBean!!.pagerInfo.nextPageIndex
        doLoad()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        private val JIAZAISHUJU = 8
        private val ERRO = 0
        private val useUrl = "http://app.680.com/api/v4/daoju_history_list.ashx "
    }
}
