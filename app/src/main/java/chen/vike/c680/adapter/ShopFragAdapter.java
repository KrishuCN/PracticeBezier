package chen.vike.c680.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lht on 2018/6/12.
 * 道具中心fragment控制器
 */

public class ShopFragAdapter extends FragmentPagerAdapter {
    //添加fragment的集合
    private List<Fragment> mFragmentList;
    //添加标题的集合
    private List<String> mTilteLis;

    public ShopFragAdapter(FragmentManager fm, List<Fragment> mFragmentList, List<String> mTilteLis) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mTilteLis = mTilteLis;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //获取标题
    @Override
    public CharSequence getPageTitle(int position) {

        return mTilteLis.get(position);
    }
}
