package chen.vike.c680.adapter;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chen.vike.c680.views.WeiKeVerticalBannerView;

/**
 */
@SuppressWarnings("unused")
public abstract class WeiKeBaseBannerAdapter<T> {
    private List<T> mDatas;
    private OnDataChangedListener mOnDataChangedListener;

    public WeiKeBaseBannerAdapter(List<T> datas) {
        mDatas = datas;
        if (datas == null || datas.isEmpty()) {
            throw new RuntimeException("nothing to show");
        }
    }

    public WeiKeBaseBannerAdapter(T[] datas) {
        mDatas = new ArrayList<>(Arrays.asList(datas));
    }

    /**
     * 设置banner填充的数据
     */
    public void setData(List<T> datas) {
        this.mDatas = datas;
        notifyDataChanged();
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    void notifyDataChanged() {
        mOnDataChangedListener.onChanged();
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * 设置banner的样式
     */
    public abstract View getView(WeiKeVerticalBannerView parent);

    /**
     * 设置banner的数据
     */
    public abstract void setItem(View view, T data);


    public interface OnDataChangedListener {
        void onChanged();
    }
}
