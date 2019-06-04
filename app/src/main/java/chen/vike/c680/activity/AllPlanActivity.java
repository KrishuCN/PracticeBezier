package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import chen.vike.c680.adapter.ItemAdapter;
import chen.vike.c680.bean.JiJianFangAnGson;
import chen.vike.c680.bean.XuanShangFangAnGson;
import chen.vike.c680.bean.ZhaoBiaoFangAnGson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.LoadingDialog;
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
 * 所有方案
 */

public class AllPlanActivity extends BaseStatusBarActivity {


    private XuanShangFangAnGson xuanShangFangAnGson;
    private JiJianFangAnGson jiJianFangAnGson;
    private ZhaoBiaoFangAnGson zhaoBiaoFangAnGson;
    private XRecyclerView xRecyclerView;
    private int TYPE;
    private List<Object> list = new ArrayList<>();
    private ItemAdapter adapter;
    private final int GETINFO_XS = 0X123;
    private final int NETWORK_EXCEPTION = 0x111;
    private final int GETINFO_ZB = 0X221;
    private final int GETINFO_JJ = 0X212;
    private Map<String, Object> map = new HashMap<>();
    private LoadingDialog ld;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allplan_activity);


        getTitle().setText("所有方案");
        ld = new LoadingDialog(this).setMessage("加载中...");
        ld.show();
        View no = findViewById(R.id.no);
        Button button =  no.findViewById(R.id.no_text);
        button.setVisibility(View.GONE);
        TextView textView =  no.findViewById(R.id.no_txt);
        textView.setVisibility(View.VISIBLE);
        textView.setText("暂无方案");
        xRecyclerView =  findViewById(R.id.xRecyclerView);
        xRecyclerView.setHasFixedSize(true);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle bundle = getIntent().getExtras();
        TYPE = bundle.getInt("TYPE");
        String id = bundle.getString("ID");
        String gzid = bundle.getString("GZID");
        adapter = new ItemAdapter(TYPE, this, list, id, gzid);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setEmptyView(no);
        /**
         * * userid 用户ID ( 用于判断当前登录账号与该项目发布人权限问题)
         * vkuserip 登录cookieIPT
         * vktoken 登录加密串
         * itemid 项目编号ID
         * num 每页显示多少条记录
         * pages 当前页码(请求第几页)
         * seltype 查询条件： =all 表示所有，unread 未查看的方案，=bak 备选的方案，=yes 中标的方案
         */
        map.put("itemid", id);
        map.put("num", 10);
        map.put("pages", 1);
        map.put("seltype", "all");
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        }
        if (TYPE == 0) {
            getXsInfo();
        } else if (TYPE == 1) {
            getZbInfo();
        } else if (TYPE == 2) {
            getJjInfo();
        }

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                map.put("pages", 1);
                if (TYPE == 0) {
                    getXsInfo();
                } else if (TYPE == 1) {
                    getZbInfo();
                } else if (TYPE == 2) {
                    getJjInfo();
                }
            }

            @Override
            public void onLoadMore() {

                if (TYPE == 0) {
                    if (xuanShangFangAnGson.getPagerInfo().getNextPageIndex() == 0) {
                        xRecyclerView.setNoMore(true);
                    } else {
                        map.put("pages", xuanShangFangAnGson.getPagerInfo().getNextPageIndex());
                        getXsInfo();
                    }

                } else if (TYPE == 1) {
                    if (zhaoBiaoFangAnGson.getPagerInfo().getNextPageIndex() == 0) {
                        xRecyclerView.setNoMore(true);

                    } else {
                        map.put("pages", zhaoBiaoFangAnGson.getPagerInfo().getNextPageIndex());
                        getZbInfo();
                    }


                } else if (TYPE == 2) {
                    if (jiJianFangAnGson.getPagerInfo().getNextPageIndex() == 0) {
                        xRecyclerView.setNoMore(true);
                    } else {
                        map.put("pages", jiJianFangAnGson.getPagerInfo().getNextPageIndex());
                        getJjInfo();
                    }

                }

            }
        });


    }

    private void getXsInfo() {

        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GAOJIAN_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String s = response.body().string();
                    LogUtils.d("==============response:" + s);
                    xuanShangFangAnGson = new Gson().fromJson(s, XuanShangFangAnGson.class);
                    hd.sendEmptyMessage(GETINFO_XS);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void getZbInfo() {

        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.BAOJIA_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("==============response:" + s);
                    zhaoBiaoFangAnGson = new Gson().fromJson(s, ZhaoBiaoFangAnGson.class);
                    hd.sendEmptyMessage(GETINFO_ZB);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void getJjInfo() {

        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GAOJIAN_LIST_JJ, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response){
                try {
                    String s = response.body().string();
                    LogUtils.d("==============response:" + s);
                    jiJianFangAnGson = new Gson().fromJson(s, JiJianFangAnGson.class);
                    hd.sendEmptyMessage(GETINFO_JJ);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GETINFO_XS) {

                if (null != xuanShangFangAnGson) {
                    if (xuanShangFangAnGson.getPagerInfo().getCurrPageIndex() == 1) {
                        list.clear();
                        xRecyclerView.refreshComplete();
                    } else {
                        xRecyclerView.loadMoreComplete();

                    }
                    list.addAll(xuanShangFangAnGson.getList());
                    adapter.notifyDataSetChanged();

                }

            } else if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(AllPlanActivity.this, msg);

            } else if (msg.what == GETINFO_ZB) {
                if (null != zhaoBiaoFangAnGson) {
                    if (zhaoBiaoFangAnGson.getPagerInfo().getCurrPageIndex() == 1) {
                        list.clear();
                        xRecyclerView.refreshComplete();
                    } else {
                        xRecyclerView.loadMoreComplete();
                    }
                    list.addAll(zhaoBiaoFangAnGson.getList());
                    adapter.notifyDataSetChanged();
                }
            } else if (msg.what == GETINFO_JJ) {

                if ( null!= jiJianFangAnGson) {
                    if (jiJianFangAnGson.getPagerInfo().getCurrPageIndex() == 1) {
                        list.clear();
                        xRecyclerView.refreshComplete();
                    } else {
                        xRecyclerView.loadMoreComplete();
                    }
                    list.addAll(jiJianFangAnGson.getList());
                    adapter.notifyDataSetChanged();

                } else {
                    adapter.notifyDataSetChanged();
                }


            }
            if (ld != null) {
                ld.dismiss();
            }
        }
    };

}
