package chen.vike.c680.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import chen.vike.c680.adapter.ShopFragAdapter
import chen.vike.c680.fragment.XuQiuJiJianFrag
import chen.vike.c680.fragment.XuQiuXuanShangFrag
import chen.vike.c680.fragment.XuQiuZhaoBiaoFrag
import chen.vike.c680.tools.BaseStatusBarActivity
import com.lht.vike.a680_v1.R
import kotterknife.bindView
import java.util.*

/**
 * Created by chen on 2018/11/9.
 * 8 *
 * 8
 */
class PersonMyFaBuXuQiuActivity : BaseStatusBarActivity() {
    val daojuTablayout: TabLayout by bindView(R.id.daoju_tablayout)
    val daojuViewpage: ViewPager by bindView(R.id.daoju_viewpage)
    private val fraList = ArrayList<Fragment>()
    private val mTitleList = ArrayList<String>()
    private var shopFragAdapter: ShopFragAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daoju_zhongxin_page)
        title.text = "我发布的需求"
        fragmentList()
        initTitle()

        setView()

    }

    /**
     * 初始化
     */
    private fun setView() {
        shopFragAdapter = ShopFragAdapter(supportFragmentManager, fraList, mTitleList)
        daojuViewpage.adapter = shopFragAdapter
        daojuTablayout.tabMode = TabLayout.MODE_FIXED
        for (i in mTitleList.indices) {
            daojuTablayout.addTab(daojuTablayout.newTab().setText(mTitleList[i]))
        }
        daojuTablayout.setupWithViewPager(daojuViewpage)
    }

    /**
     * tab的标题
     */
    private fun initTitle() {
        mTitleList.add("悬赏")
        mTitleList.add("招标")
        mTitleList.add("计件")
        mTitleList.add("雇佣")
        mTitleList.add("待转款")
    }

    /**
     * 主页fragment
     */
    private fun fragmentList() {
        fraList.add(XuQiuXuanShangFrag())
        fraList.add(XuQiuZhaoBiaoFrag())
        val bundle3 = Bundle()
        bundle3.putString("type","3")
        val xuQiuJiJianFrag3 = XuQiuJiJianFrag()
        xuQiuJiJianFrag3.arguments = bundle3
        fraList.add(xuQiuJiJianFrag3)

        val bundle4 = Bundle()
        bundle4.putString("type","4")
        val xuQiuJiJianFrag4 = XuQiuJiJianFrag()
        xuQiuJiJianFrag4.arguments = bundle4
        fraList.add(xuQiuJiJianFrag4)

        val bundle5 = Bundle()
        bundle5.putString("type","5")
        val xuQiuJiJianFrag5 = XuQiuJiJianFrag()
        xuQiuJiJianFrag5.arguments = bundle5

        fraList.add(xuQiuJiJianFrag5)
    }
}
