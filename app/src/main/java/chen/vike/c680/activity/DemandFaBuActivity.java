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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/9.
 * <p>
 * 我的订单
 */

public class DemandFaBuActivity extends BaseStatusBarActivity implements MyListView2.OnLoadListener {


    private EmptySwipeRefreshLayout srl;
    private MyListView2 lv;
    private TabLayout tab;
    private final int GETINFO = 0X123;
    private final int NETWORK_EXCEPTION = 0X121;
    private final int ONLOAD = 0X111;
    private Map<String, Object> map = new HashMap<>();
    private XuQiuList xuQiuList;
    private MybaseAdaper mybaseAdapter;
    //暂时不用，看下效果
//    private LoadingDialog ld;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demand_fabu_activity);

        getTitle().setText("我的订单");
        srl =  findViewById(R.id.fabu_list_swipe);
        lv =  findViewById(R.id.fabu_list_lv);
        tab =  findViewById(R.id.tab);//该控件默认选中第一项，所以也不用设置了
        srl.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));
        isLoginFresh();//判断登录  加载数据
        View view = LayoutInflater.from(this).inflate(R.layout.view_no, null);
        TextView t =  view.findViewById(R.id.no_txt);
        //t在view里写的的字符串会被消掉，只有自己手动添加
        t.setText("没有发布项目");
        ((ViewGroup) lv.getParent()).addView(view);
        mybaseAdapter = new MybaseAdaper(new ArrayList<XuQiuList.ListBean>());
        lv.setEmptyView(view);

        lv.setAdapter(mybaseAdapter);

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
                        map.put("type", 0);
                        srl.setRefreshing(true);
                        break;
                    case "悬赏":
                        map.put("pages", 1);
                        map.put("type", 1);
                        srl.setRefreshing(true);
                        break;
                    case "招标":
                        map.put("pages", 1);
                        map.put("type", 2);
                        srl.setRefreshing(true);
                        break;
                    case "计件":
                        map.put("pages", 1);
                        map.put("type", 3);
                        srl.setRefreshing(true);
                        break;
                    case "雇佣":
                        map.put("pages", 1);
                        map.put("type", 4);
                        srl.setRefreshing(true);
                        break;
                    case "待转款":
                        map.put("pages", 1);
                        map.put("type", 5);
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
     * 判断登录  加载数据
     */
    private void isLoginFresh(){
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map.put("num", 10);
            map.put("pages", 1);
            map.put("type", 0);
            srl.setRefreshing(true);
            doRefresh();
        }
    }
    /**
     * 刷新数据
     */
    private void doRefresh() {

        map.put("pages", "1");
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_FABU_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response)  {
                try {
                    String s = response.body().string();
                    LogUtils.d(" =======================Response:" + s);
                    xuQiuList = new Gson().fromJson(s, XuQiuList.class);
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
                try {
                    String s = response.body().string();
                    LogUtils.d(" =======================Response:" + s);
                    xuQiuList = new Gson().fromJson(s, XuQiuList.class);
                    hd.sendEmptyMessage(ONLOAD);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public void onLoad() {

        if (xuQiuList.getPagerInfo().getNextPageIndex() == 0) {
            CustomToast.showToast(this, "没有更多了！", Toast.LENGTH_SHORT);
            lv.setLoadEnable(false);
            return;
        }
        map.put("pages", xuQiuList.getPagerInfo().getNextPageIndex());
        doLoad();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        doRefresh();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * listview的适配器
     */
    class MybaseAdaper extends BaseAdapter {


        private List<XuQiuList.ListBean> list = new ArrayList<>();

        public MybaseAdaper(List<XuQiuList.ListBean> list) {
            this.list = list;
        }

        public void addList(List<XuQiuList.ListBean> list) {
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

                convertView = LayoutInflater.from(DemandFaBuActivity.this).inflate(R.layout.view_fabu_list_item, null);
                id = new ID();
                id.tbs =  convertView.findViewById(R.id.tbs);
                id.title =  convertView.findViewById(R.id.title);
                id.money =  convertView.findViewById(R.id.money);
                id.item_zt =  convertView.findViewById(R.id.item_zt);
                id.control =  convertView.findViewById(R.id.control);
                id.contro =  convertView.findViewById(R.id.contro);
                convertView.setTag(id);

            } else {
                id = (ID) convertView.getTag();
            }

            id.title.setText(list.get(position).getItemname());
            if (list.get(position).getPayok().equals("0")) {
                id.control.setVisibility(View.VISIBLE);
                id.control.setText("托管赏金");
                id.control.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DemandFaBuActivity.this, FuKuanActivity.class);
                        intent.putExtra("ID", list.get(position).getItemid());
                        startActivity(intent);
                    }
                });
            } else if (map.get("type").toString().equals("5")) {
                id.control.setVisibility(View.VISIBLE);
                id.control.setText("确认付款");
                id.control.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DemandFaBuActivity.this, ConfirmPayActivity.class);
                        intent.putExtra("ID", list.get(position).getItemid());
                        startActivity(intent);
                    }
                });
            } else {
                long endtime = new Date(list.get(position).getEndtime()).getTime()
                        - new Date(new SimpleDateFormat(
                        "yyyy/MM/dd HH:mm:ss").format(new Date())).getTime();
                if (endtime < -((long) 30 * 24 * 60 * 60 * 1000)) {
                    id.control.setVisibility(View.GONE);
                } else {
                    id.control.setVisibility(View.VISIBLE);
                    id.control.setText("增加悬赏");
                    id.control.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DemandFaBuActivity.this, PriceMakUpActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            startActivity(intent);
                        }
                    });
                }
            }

            if (list.get(position).getVikecn_class1ID().equals("6")) {
                id.money.setText(Html.fromHtml("<font color='#20cbcb'>计件</font>：" + list.get(position).getMoney() + "元"));
            } else {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                    if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                        id.control.setVisibility(View.VISIBLE);
                        id.control.setText("托管定金");
                    } else {
                        id.control.setVisibility(View.GONE);
                    }
                    if (list.get(position).getZab_yusuan1().equals(list.get(position).getZab_yusuan2())) {
                        id.money.setText(Html.fromHtml("<font color='#20cbcb'>招标</font>：" + list.get(position).getZab_yusuan1() + "元"));
                    } else {
                        id.money.setText(Html.fromHtml("<font color='#20cbcb'>招标</font>：" + list.get(position).getZab_yusuan1() + "元-" + list.get(position).getZab_yusuan2() + "元"));
                    }
                } else if (Integer.valueOf(list.get(position).getZab_do()) == 6) {
                    id.money.setText(Html.fromHtml("<font color='#20cbcb'>雇佣</font>：" + list.get(position).getMoney() + "元"));
                } else {
                    id.money.setText(Html.fromHtml("<font color='#20cbcb'>悬赏</font>：" + list.get(position).getMoney() + "元"));
                }
            }

            if (Integer.valueOf(list.get(position).getCheck()) == 0) {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1 && Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) > 0) {
                    id.item_zt.setText("未审核/已付定金￥" + list.get(position).getMoney());
                } else {
                    if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                        if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("未审核/未付定金￥" + list.get(position).getMoney());
                        } else {
                            id.item_zt.setText("未审核");
                        }
                    } else {
                        if (Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("未审核/未付款");
                        } else {
                            id.item_zt.setText("未审核/已付款");
                        }
                    }

                }

            } else if (Integer.valueOf(list.get(position).getCheck()) == 1) {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1 && Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) > 0) {
                    id.item_zt.setText("审核未通过/已付定金￥" + list.get(position).getMoney());
                } else {

                    if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                        if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("审核未通过/未付定金￥" + list.get(position).getMoney());
                        } else {
                            id.item_zt.setText("审核未通过");
                        }
                    } else {
                        if (Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("审核未通过/未付款");
                        } else {
                            id.item_zt.setText("审核未通过/已付款");
                        }
                    }

                }
            } else {
                if (Integer.valueOf(list.get(position).getZab_do()) == 1 && Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) > 0) {
                    id.item_zt.setText("已审核/已付定金￥" + list.get(position).getMoney());
                } else {

                    if (Integer.valueOf(list.get(position).getZab_do()) == 1) {
                        if (Integer.valueOf(list.get(position).getMoney()) > 0 && Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("已审核/未付定金￥" + list.get(position).getMoney());
                        } else {
                            id.item_zt.setText("已审核");

                        }
                    } else {
                        if (Integer.valueOf(list.get(position).getPayok()) == 0) {
                            id.item_zt.setText("已审核/未付款");
                        } else {
                            id.item_zt.setText("已审核/已付款");

                        }
                    }

                }

            }

            if (map.get("type").toString().equals("2") && !(Integer.valueOf(list.get(position).getJindu()) == 10)) {
                if (Integer.valueOf(list.get(position).getJindu()) < 4 && list.get(position).getPayok().equals("0")) {
                    id.contro.setVisibility(View.GONE);
                    id.control.setVisibility(View.VISIBLE);
                    id.control.setText("托管定金");
                    id.control.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DemandFaBuActivity.this, PayDepositActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            if (Integer.valueOf(list.get(position).getMoney()) > 0) {
                                intent.putExtra("MONEY", list.get(position).getMoney());
                            }
                            startActivityForResult(intent,1);
                        }
                    });
                    if (list.get(position).getCheck().equals("1")) {
                        id.control.setVisibility(View.GONE);
                    }
                } else {
                    id.control.setVisibility(View.GONE);
                }

                if (Integer.valueOf(list.get(position).getJindu()) == 33 && list.get(position).getHasfenqi().equals("0")) {
                    if (Integer.valueOf(list.get(position).getZab_yusuan()) < 5000) {
                        id.contro.setVisibility(View.GONE);
                        id.control.setVisibility(View.VISIBLE);
                        id.control.setText("托管赏金");
                        id.control.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DemandFaBuActivity.this, FuKuanActivity.class);
                                intent.putExtra("ID", list.get(position).getItemid());
                                intent.putExtra("ZB", "11");
                                startActivityForResult(intent,1);
                            }
                        });
                    } else {
                        id.contro.setVisibility(View.VISIBLE);
                        id.control.setVisibility(View.VISIBLE);
                        id.contro.setText("托管赏金");
                        id.control.setText("我要分期");
                        id.contro.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DemandFaBuActivity.this, FuKuanActivity.class);
                                intent.putExtra("ID", list.get(position).getItemid());
                                intent.putExtra("ZB", "11");
                                startActivityForResult(intent,1);
                            }
                        });
                        id.control.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转到我要分期界面
                                Intent intent = new Intent(DemandFaBuActivity.this, InstallmentSettingActivity.class);
                                intent.putExtra("ID", list.get(position).getItemid());
                                startActivityForResult(intent,1);
                            }
                        });
                    }

                }

                if (list.get(position).getHasfenqi().equals("1")) {
                    id.contro.setVisibility(View.VISIBLE);
                    id.control.setVisibility(View.VISIBLE);
                    id.contro.setText("托管赏金");
                    id.control.setText("分期详情");
                    id.contro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DemandFaBuActivity.this, FuKuanActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            intent.putExtra("ZB", "11");
                            startActivityForResult(intent,1);
                        }
                    });
                    id.control.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //跳转到分期详情界面
                            Intent intent = new Intent(DemandFaBuActivity.this, InstallmentDetailsActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            startActivity(intent);
                        }
                    });
                    if (list.get(position).getTuoguan_all_btn().equals("0")) {
                        id.contro.setVisibility(View.GONE);
                    } else {
                        id.contro.setVisibility(View.VISIBLE);
                    }

                }

            }

            id.tbs.setText("(" + list.get(position).getCynum() + "稿件)");
//            id.view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (map.get("type").toString().equals("2")) {
                        if (Integer.valueOf(list.get(position).getJindu()) > 3 && Integer.valueOf(list.get(position).getJindu()) != 10) {
                            Intent intent = new Intent(DemandFaBuActivity.this, TenderBidActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(DemandFaBuActivity.this, OrderDetailsActivity.class);
                            intent.putExtra("ID", list.get(position).getItemid());
                            startActivity(intent);
                        }

                    } else {
                        Intent intent = new Intent(DemandFaBuActivity.this, OrderDetailsActivity.class);
                        intent.putExtra("ID", list.get(position).getItemid());
                        startActivity(intent);
                    }


                }
            });
            return convertView;
        }

        private class ID {
            private TextView title;
            private TextView money;
            private TextView tbs;
            private TextView item_zt;
            private Button control;
            private Button contro;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GETINFO) {

                lv.setLoadEnable(true);
                if (null != xuQiuList) {
                    if (xuQiuList.getPagerInfo().getNextPageIndex() == 0) {
                        //此处判断是否有下一页，用于将footview直接屏蔽掉
                        lv.setLoadEnable(false);
                    }
                    mybaseAdapter.refresh();
                    mybaseAdapter.addList(xuQiuList.getList());
                } else {
                    mybaseAdapter.refresh();
                }
                srl.setRefreshing(false);
                mybaseAdapter.notifyDataSetChanged();
                lv.setSelection(0);

            } else if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(DemandFaBuActivity.this, msg);

            } else if (msg.what == ONLOAD) {

                mybaseAdapter.addList(xuQiuList.getList());
                lv.onLoadComplete();
                mybaseAdapter.notifyDataSetChanged();

            }
        }
    };

}
