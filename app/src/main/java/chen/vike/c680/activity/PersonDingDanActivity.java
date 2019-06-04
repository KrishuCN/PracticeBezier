package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import chen.vike.c680.bean.XuQiuList;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.EmptySwipeRefreshLayout;
import chen.vike.c680.views.MyListView2;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.vike.c680.adapter.DingShowAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/12/1.
 */

public class PersonDingDanActivity extends BaseStatusBarActivity implements MyListView2.OnLoadListener {

    @BindView(R.id.dingdan_list_lv)
    MyListView2 dingdanListLv;
    @BindView(R.id.dingdan_list_swipe)
    EmptySwipeRefreshLayout dingdanListSwipe;
    private Intent intent;
    private String strTitle;
    private String type;
    private final static String dingUrl = "http://app.680.com/api/v4/user_item_gz_list.ashx";
    private XuQiuList xuQiuList;
    private Map<String, Object> map = new HashMap<>();
    private final static int JIAZAISHUJU = 010;
    private final static int ERRO = 000;
    private DingShowAdapter dingShowAdapter;
    private final int NETWORK_EXCEPTION = 0X111;
    private final int ONLOAD = 0X121;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_dingdan_show);
        ButterKnife.bind(this);
        strTitle = getIntent().getStringExtra("acTitle");
        type = getIntent().getStringExtra("type");
        getTitle().setText(strTitle);
        dingdanListSwipe.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));
        View view = LayoutInflater.from(this).inflate(R.layout.view_no, null);
        TextView t = (TextView) view.findViewById(R.id.no_txt);
        //t在view里写的的字符串会被消掉，只有自己手动添加
        t.setText("还没有订单哟!");
        ((ViewGroup) dingdanListLv.getParent()).addView(view);
        dingdanListLv.setEmptyView(view);
        dingShowAdapter = new DingShowAdapter(new ArrayList<XuQiuList.ListBean>(), this, type);
        dingdanListLv.setAdapter(dingShowAdapter);

        dingdanListLv.setOnLoadListener(this);
        viewListener();
        isLoginFresh();//判断登录  加载数据
    }

    /**
     * 判断登录  加载数据
     */
    private void isLoginFresh() {
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map.put("num", 10);
            map.put("pages", 1);
            map.put("type", 0);
            dingdanListSwipe.setRefreshing(true);
            dingdanData(type);
        }

    }

    private void viewListener() {
        dingdanListSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dingdanData(type);

            }
        });
        dingdanListLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                dingdanListLv.setScrollState(scrollState);
                dingdanListLv.ifNeedLoad(view, scrollState);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                dingdanListLv.setFirstVisibleItem(firstVisibleItem);
                boolean enable = false;
                if (dingdanListLv != null && dingdanListLv.getChildCount() > 0) {
                    // 检查listView第一个item是否可见
                    boolean firstItemVisible = dingdanListLv.getFirstVisiblePosition() == 0;
                    // 检查第一个item的顶部是否可见
                    boolean topOfFirstItemVisible = dingdanListLv.getChildAt(0).getTop() == 0;
                    // 启用或者禁用SwipeRefreshLayout刷新标识
                    enable = firstItemVisible && topOfFirstItemVisible;
                } else if (dingdanListLv != null && dingdanListLv.getChildCount() == 0) {
                    // 没有数据的时候允许刷新
                    enable = true;
                }
                // 把标识传给swipeRefreshLayout
                dingdanListSwipe.setEnabled(enable);
            }
        });

    }

    /**
     * 加载数据
     */
    private void doLoad() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_FABU_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(dingHD, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    Log.e("eqw", s);
                    xuQiuList = new Gson().fromJson(s, XuQiuList.class);
                    if (xuQiuList != null && xuQiuList.getList() != null) {
                        dingHD.sendEmptyMessage(ONLOAD);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * 刷新数据
     *
     * @param type
     */
    private void dingdanData(String type) {
        map.put("userid", MyApplication.userInfo.getUserID());
        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        map.put("num", 10);
        map.put("pages", 1);
        map.put("type", type);
        OkhttpTool.Companion.getOkhttpTool().post(dingUrl, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(dingHD, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    Log.e("123", s);
                    xuQiuList = new Gson().fromJson(s, XuQiuList.class);
                    if (xuQiuList.getErr_code().equals("0")) {
                        dingHD.sendEmptyMessage(JIAZAISHUJU);
                    } else {
                        dingHD.sendEmptyMessage(ERRO);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler dingHD = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case JIAZAISHUJU:
                    dingdanListLv.setLoadEnable(true);
                    if (null != xuQiuList) {
                        if (xuQiuList.getPagerInfo().getNextPageIndex() == 0) {
                            //此处判断是否有下一页，用于将footview直接屏蔽掉
                            dingdanListLv.setLoadEnable(false);
                        }
                        dingShowAdapter.refresh();
                        dingShowAdapter.addList(xuQiuList.getList());
                    } else {
                        dingShowAdapter.refresh();
                    }
                    dingdanListSwipe.setRefreshing(false);
                    dingShowAdapter.notifyDataSetChanged();
                    dingdanListLv.setSelection(0);
                    break;
                case ERRO:
                    dingdanListSwipe.setRefreshing(false);
                    break;
                case ONLOAD:
                    dingShowAdapter.addList(xuQiuList.getList());
                    dingdanListLv.onLoadComplete();
                    dingShowAdapter.notifyDataSetChanged();
                    break;
                case NETWORK_EXCEPTION:
                    LhtTool.showNetworkException(PersonDingDanActivity.this, msg);
                    break;
                    default:
                        break;
            }
        }
    };

    @Override
    public void onLoad() {
        if (xuQiuList.getPagerInfo().getNextPageIndex() == 0) {
            CustomToast.showToast(this, "没有更多了！", Toast.LENGTH_SHORT);
            dingdanListLv.setLoadEnable(false);
            return;
        }
        map.put("pages", xuQiuList.getPagerInfo().getNextPageIndex());
        doLoad();
    }
}
