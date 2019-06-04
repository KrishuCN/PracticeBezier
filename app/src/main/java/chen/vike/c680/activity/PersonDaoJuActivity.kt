package chen.vike.c680.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager

import chen.vike.c680.tools.BaseStatusBarActivity
import com.lht.vike.a680_v1.R

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import chen.vike.c680.adapter.ShopFragAdapter
import chen.vike.c680.fragment.PersonDaoJuShopFragment
import chen.vike.c680.fragment.PersonMyIsDaoJuFrag
import chen.vike.c680.fragment.PersonUseDaoJuFrag
import kotterknife.bindView

/**
 * Created by lht on 2018/6/12.
 * 道具中心
 */

class PersonDaoJuActivity : BaseStatusBarActivity() {

    private val daojuTablayout: TabLayout by bindView(R.id.daoju_tablayout)
    private val daojuViewpage: ViewPager by bindView(R.id.daoju_viewpage)
    private val fraList = ArrayList<Fragment>()
    private val mTitleList = ArrayList<String>()
    private var shopFragAdapter: ShopFragAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daoju_zhongxin_page)
        title.text = "道具中心"
        fragmentList()
        initTitle()
        setView()

    }

    /**
     * 初始化
     */
    private fun setView() {
        shopFragAdapter = ShopFragAdapter(supportFragmentManager, fraList, mTitleList)
        daojuViewpage.offscreenPageLimit = 3
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
        mTitleList.add("道具商城")
        mTitleList.add("我的道具")
        mTitleList.add("使用记录")
    }

    /**
     * 主页fragment
     */
    private fun fragmentList() {
        fraList.add(PersonDaoJuShopFragment())
        fraList.add(PersonMyIsDaoJuFrag())
        fraList.add(PersonUseDaoJuFrag())
    }
}
