package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import chen.vike.c680.bean.MyCollectionGson;
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
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/16.
 *
 * 我的收藏
 */

public class MyCollectionActivity extends BaseStatusBarActivity implements MyListView2.OnLoadListener{

    private MyListView2 lv;
    private EmptySwipeRefreshLayout srl;
    private final int GETINFO = 0X123;
    private final int NETWORK_EXCEPTION = 0x111;
    private final int ONLOAD = 0X121;
    private final int DELETE = 0X122;
    private Map<String, Object> map = new HashMap<>();
    private MyCollectionGson myCollectionGson;
    private MyBaseAdapter myBaseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycollection_activity);

        getTitle().setText("我的收藏");
        lv =  findViewById(R.id.cyd_list_lv);
        srl = findViewById(R.id.cyd_list_swipe);
        srl.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));
        isLogoin();//是否登录

        View view = LayoutInflater.from(this).inflate(R.layout.view_no, null);
        TextView t = (TextView) view.findViewById(R.id.no_txt);
        //t在view里写的的字符串会被消掉，只有自己手动添加
        t.setText("暂无收藏");
        ((ViewGroup) lv.getParent()).addView(view);
        myBaseAdapter = new MyCollectionActivity.MyBaseAdapter(new ArrayList<MyCollectionGson.ProjectListBean>());
        lv.setEmptyView(view);

        lv.setAdapter(myBaseAdapter);

        lv.setOnLoadListener(this);
        viewListener();//控件监听事件

    }

    /**
     * 判断是否登录得到数据
     */
    private void isLogoin(){
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map.put("num", 10);
            map.put("pages", 1);
            srl.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    srl.setRefreshing(false);
                }
            }, 2000);
            doRefresh();
        }
    }
    /**
     * 控件监听事件
     */
    private void viewListener(){
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
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
     * 刷新
     */
    private void doRefresh() {

        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.USER_COLLECTION, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String s = response.body().string();
                    LogUtils.d("======================Response:" + s);
                    myCollectionGson = new Gson().fromJson(s, MyCollectionGson.class);
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
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.USER_COLLECTION, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("======================Response:" + s);
                    myCollectionGson = new Gson().fromJson(s, MyCollectionGson.class);
                    hd.sendEmptyMessage(ONLOAD);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLoad() {

        LogUtils.d("==============load进来了");
        if (myCollectionGson.getPagerInfo().getNextPageIndex() == 0) {
            CustomToast.showToast(this, "没有更多了！", Toast.LENGTH_SHORT);
            lv.setLoadEnable(false);
            return;
        }
        map.put("pages", myCollectionGson.getPagerInfo().getNextPageIndex());
        doLoad();
        LogUtils.d("==============load出去了");
    }

    /**
     * 适配器
     */
    class MyBaseAdapter extends BaseAdapter {

        private List<MyCollectionGson.ProjectListBean> list = new ArrayList<>();

        public MyBaseAdapter(List<MyCollectionGson.ProjectListBean> list) {
            this.list = list;
        }

        public void addList(List<MyCollectionGson.ProjectListBean> list) {
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

                convertView = LayoutInflater.from(MyCollectionActivity.this).inflate(R.layout.mycollection_item, null);
                id = new ID();
                id.tbs = (TextView) convertView.findViewById(R.id.tbs);
                id.title = (TextView) convertView.findViewById(R.id.title);
                id.money = (TextView) convertView.findViewById(R.id.money);
                id.control = (Button) convertView.findViewById(R.id.control);
                convertView.setTag(id);

            } else {
                id = (ID) convertView.getTag();
            }


            convertView.setPadding(0, 0, 0, 10);
            id.title.setText(list.get(position).getName());
            id.control.setText("删除");
            if (list.get(position).getClass1id().equals("6")) {
                id.money.setText(Html.fromHtml("计件 <font color='#df231b'>￥" + list.get(position).getMoney() + "</font>"));
            } else {
                if (Integer.valueOf(list.get(position).getType()) == 1) {
                    if (list.get(position).getYusuan1().equals(list.get(position).getYusuan2())) {
                        id.money.setText(Html.fromHtml("招标 <font color='#df231b'>￥" + list.get(position).getYusuan1() + "</font>"));

                    } else {
                        id.money.setText(Html.fromHtml("招标 <b><font color='#df231b'>￥" + list.get(position).getYusuan1() + "-￥" + list.get(position).getYusuan2() + "</font></b>"));
                    }
                } else {
                    id.money.setText(Html.fromHtml("悬赏 <font color='#df231b'>￥" + list.get(position).getMoney() + "</font>"));

                }
            }
            id.control.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyCollectionActivity.this).setTitle("提示信息")
                            .setMessage("您是否确定删除该项目？").setCancelable(false)
                            .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    map.put("ProId", list.get(position).getId());
                                    OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.DELETE_COLLECTION, map, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {

                                            String s = response.body().string();
                                            LogUtils.d("==================response:"+s);
                                            Message ms = new Message();
                                            ms.what = DELETE;
                                            Bundle b = new Bundle();
                                            b.putString("s", s);
                                            ms.setData(b);
                                            hd.sendMessage(ms);

                                        }
                                    });
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                }
            });

            id.tbs.setText(list.get(position).getNum() + "人投标");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MyCollectionActivity.this, TaskDetailsActivity.class);
                    intent.putExtra("ID", list.get(position).getItemid());
                    startActivity(intent);

                }
            });




            return convertView;
        }


        private class ID{
            private TextView title;
            private TextView money;
            private TextView tbs;
            private Button control;
        }

    }



    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GETINFO) {

                lv.setLoadEnable(true);
                if (null != myCollectionGson) {
                    if (myCollectionGson.getPagerInfo().getNextPageIndex() == 0) {
                        //此处判断是否有下一页，用于将footview直接屏蔽掉
                        lv.setLoadEnable(false);
                    }
                    myBaseAdapter.refresh();
                    myBaseAdapter.addList(myCollectionGson.getProjectList());
                } else {
                    myBaseAdapter.refresh();
                }
                srl.setRefreshing(false);
                myBaseAdapter.notifyDataSetChanged();
                lv.setSelection(0);

            } else if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(MyCollectionActivity.this, msg);

            } else if (msg.what == ONLOAD) {

                myBaseAdapter.addList(myCollectionGson.getProjectList());
                lv.onLoadComplete();
                myBaseAdapter.notifyDataSetChanged();

            } else if (msg.what == DELETE) {

                String s = msg.getData().getString("s");
                switch (s) {
                    case "unlogin":
                        CustomToast.showToast(MyCollectionActivity.this, "请登录", Toast.LENGTH_LONG);
                        break;
                    case "noitem":
                        CustomToast.showToast(MyCollectionActivity.this, "删除失败", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        CustomToast.showToast(MyCollectionActivity.this, "刪除成功", Toast.LENGTH_LONG);
                        map.put("pages", 1);
                        doRefresh();
                        break;
                    case "":
                        myBaseAdapter.refresh();
                        myBaseAdapter.notifyDataSetChanged();
                        break;
                }

            }
        }
    };

}
