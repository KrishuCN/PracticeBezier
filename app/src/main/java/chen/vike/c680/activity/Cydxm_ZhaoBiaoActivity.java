package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chen.vike.c680.bean.CydxmItemGson;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.ImageLoadUtils;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.EmptySwipeRefreshLayout;
import chen.vike.c680.views.MyListView2;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/13.
 * 参与项目——招标项目
 */

public class Cydxm_ZhaoBiaoActivity extends BaseStatusBarActivity implements MyListView2.OnLoadListener{

    private TabLayout tab;
    private MyListView2 lv;
    private EmptySwipeRefreshLayout srl;
    private final int GETINFO = 0X123;
    private final int NETWORK_EXCEPTION = 0x111;
    private final int ONLOAD = 0X121;
    private Map<String, Object> map = new HashMap<>();
    private CydxmItemGson cydxmXuanShangGson;
    private MyBaseAdapter myBaseAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cydxm_zhaobiao_activity);

        getTitle().setText("招标项目");
        tab =  findViewById(R.id.tab_cyd);
        lv =  findViewById(R.id.cyd_list_lv);
        srl =  findViewById(R.id.cyd_list_swipe);
        srl.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));

       isLoginFresh();//加载数据
        View view = LayoutInflater.from(this).inflate(R.layout.view_no, null);
        TextView t = view.findViewById(R.id.no_txt);
        //t在view里写的的字符串会被消掉，只有自己手动添加
        t.setText("没有参与项目");
        ((ViewGroup) lv.getParent()).addView(view);
        myBaseAdapter = new MyBaseAdapter(new ArrayList<CydxmItemGson.ListBean>());
        lv.setEmptyView(view);
        lv.setAdapter(myBaseAdapter);

        lv.setOnLoadListener(this);
        viewListener();//控件监听事件

    }

    /**
     * view控件监听事件
     */
    private void viewListener(){
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case "全部":
                        map.put("pages", 1);
                        map.put("xbstate", 0);
                        srl.setRefreshing(true);
                        break;
                    case "中标":
                        map.put("pages", 1);
                        map.put("xbstate", 1);
                        srl.setRefreshing(true);
                        break;
                    case "未中标":
                        map.put("pages", 1);
                        map.put("xbstate", 4);
                        srl.setRefreshing(true);
                        break;
                }
                doRefresh();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                lv.setScrollState(scrollState);
                lv.ifNeedLoad(view, scrollState);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lv.setFirstVisibleItem(firstVisibleItem);
                boolean enable = false;
                if (lv != null && lv.getChildCount() > 0) {
                    // 检查listView第一个item是否可见
                    boolean firstItemVisible = lv.getFirstVisiblePosition() == 0;
                    // 检查第一个item的顶部是否可见
                    boolean topOfFirstItemVisible = lv.getChildAt(0).getTop() == 0;
                    // 启用或者禁用SwipeRefreshLayout刷新标识
                    enable = firstItemVisible && topOfFirstItemVisible;
                } else if (lv != null && lv.getChildCount() == 0) {
                    // 没有数据的时候允许刷新
                    enable = true;
                }
                // 把标识传给swipeRefreshLayout
                srl.setEnabled(enable);
            }
        });

    }

    /**
     * 判断登录 加载数据
     */
    private void isLoginFresh(){
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map.put("num", 10);
            map.put("pages", 1);
            map.put("type", 2);
            srl.setRefreshing(true);
            doRefresh();
        }
    }
    /**
     * 刷新数据
     */
    private void doRefresh() {

        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.CANYU_XUANGSHANG_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String s = response.body().string();
                    LogUtils.d("======================Response:" + s);
                    cydxmXuanShangGson = new Gson().fromJson(s, CydxmItemGson.class);
                    hd.sendEmptyMessage(GETINFO);

                }catch (Exception e){
                    e.printStackTrace();
                }

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
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String s = response.body().string();
                    LogUtils.d("======================Response:" + s);
                    cydxmXuanShangGson = new Gson().fromJson(s, CydxmItemGson.class);
                    if (cydxmXuanShangGson != null) {
                        hd.sendEmptyMessage(ONLOAD);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onLoad() {
        LogUtils.d("==============load进来了");
        if (cydxmXuanShangGson.getPagerInfo().getNextPageIndex() == 0) {
            CustomToast.showToast(this, "没有更多了！", Toast.LENGTH_SHORT);
            lv.setLoadEnable(false);
            return;
        }
        map.put("pages", cydxmXuanShangGson.getPagerInfo().getNextPageIndex());
        doLoad();
        LogUtils.d("==============load出去了");
    }

    /**
     * listview的适配器
     */
    class MyBaseAdapter extends BaseAdapter {

        private List<CydxmItemGson.ListBean> list = new ArrayList<>();

        public MyBaseAdapter(List<CydxmItemGson.ListBean> list) {
            this.list = list;
        }

        public void addList(List<CydxmItemGson.ListBean> list) {
            this.list.addAll(list);
        }

        public void refresh() {
            list.clear();
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        ID id;
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null || convertView.getTag() == null) {

                convertView = LayoutInflater.from(Cydxm_ZhaoBiaoActivity.this).inflate(R.layout.view_cayu_list_item, null);
                id = new ID();
                id.title = (TextView) convertView.findViewById(R.id.title);
                id.money = (TextView) convertView.findViewById(R.id.money);
                id.name = (TextView) convertView.findViewById(R.id.name);
                id.logo = (ImageView) convertView.findViewById(R.id.icon);
                id.istg = (TextView) convertView.findViewById(R.id.istg);
                convertView.setTag(id);

            } else {
                id = (ID) convertView.getTag();
            }

            id.title.setText(list.get(position).getItemname());

            ImageLoadUtils.Companion.display(Cydxm_ZhaoBiaoActivity.this,id.logo,list.get(position).getGz_imgurl());

            id.name.setText(list.get(position).getGz_username());
            if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                try {
                    if (list.get(position).getPayok().equals("1") && Integer.valueOf(list.get(position).getMoney()) > 0) {
                        id.istg.setVisibility(View.VISIBLE);
                        id.istg.setText("已托管：￥" + list.get(position).getMoney());

                    } else {
                        id.istg.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    id.istg.setVisibility(View.GONE);
                }

                if (list.get(position).getZab_yusuan1().equals(list.get(position).getZab_yusuan2())) {
                    id.money.setText(Html.fromHtml("<font color='#df231b'>￥" + list.get(position).getZab_yusuan1() + "</font>"));
                } else {
                    id.money.setText(Html.fromHtml("<font color='#df231b'>￥" + list.get(position).getZab_yusuan1() + "-￥" + list.get(position).getZab_yusuan2() + "</font>"));
                }
            } else {
                if (list.get(position).getPayok().equals("1")) {
                    id.istg.setVisibility(View.VISIBLE);
                    id.istg.setText("已托管");
                } else {
                    id.istg.setVisibility(View.GONE);
                }
                id.money.setText(Html.fromHtml("<font color='#df231b'>￥" + list.get(position).getMoney() + "</font>"));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(Cydxm_ZhaoBiaoActivity.this, TaskDetailsActivity.class);
                    in.putExtra("ID", list.get(position).getItemid());
                    startActivity(in);
                }
            });


            return convertView;
        }


        private class ID{
            private TextView title;
            private TextView money;
            private TextView name;
            private ImageView logo;
            private TextView istg;
        }

    }
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GETINFO) {

                lv.setLoadEnable(true);
                if (null != cydxmXuanShangGson) {
                    if (cydxmXuanShangGson.getPagerInfo().getNextPageIndex() == 0) {
                        //此处判断是否有下一页，用于将footview直接屏蔽掉
                        lv.setLoadEnable(false);
                    }
                    myBaseAdapter.refresh();
                    myBaseAdapter.addList(cydxmXuanShangGson.getList());
                } else {
                    myBaseAdapter.refresh();
                }
                srl.setRefreshing(false);
                myBaseAdapter.notifyDataSetChanged();
                lv.setSelection(0);

            } else if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(Cydxm_ZhaoBiaoActivity.this, msg);

            } else if (msg.what == ONLOAD) {
                if (cydxmXuanShangGson.getList() != null && myBaseAdapter != null) {
                    myBaseAdapter.addList(cydxmXuanShangGson.getList());
                    lv.onLoadComplete();
                    myBaseAdapter.notifyDataSetChanged();
                }

            }
        }
    };

}
