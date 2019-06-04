package chen.vike.c680.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chen.vike.c680.views.EmptySwipeRefreshLayout;
import chen.vike.c680.views.MyListView2;
import com.lht.vike.a680_v1.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chen on 2018/11/9.
 * 8 *
 * 8    我的订单之全部
 */
public class XuQiuQuanBuFrag extends Fragment {
    @BindView(R.id.fabu_list_lv)
    MyListView2 fabuListLv;
    @BindView(R.id.fabu_list_swipe)
    EmptySwipeRefreshLayout fabuListSwipe;
    Unbinder unbinder;
    private Context mContext;
    private View quanBuView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        quanBuView = LayoutInflater.from(mContext).inflate(R.layout.activity_myxuqiu_list_item, null);
        unbinder = ButterKnife.bind(this, quanBuView);
        return quanBuView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
