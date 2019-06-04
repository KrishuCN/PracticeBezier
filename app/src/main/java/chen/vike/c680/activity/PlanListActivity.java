package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * 方案列表
 *
 */

public class PlanListActivity extends BaseStatusBarActivity{

    private XRecyclerView xRecyclerView;
    private Map<String, Object> map = new HashMap<>();
    private List<Object> list = new ArrayList<>();
    private ItemAdapter adapter;
    private int TYPE;
    private XuanShangFangAnGson xuanShangFangAnGson;
    private ZhaoBiaoFangAnGson zhaoBiaoFangAnGson;
    private JiJianFangAnGson jiJianFangAnGson;
    private RecyclerView.ItemDecoration itemDecoration;
    private final int XS_INFO = 0x123;
    private final int ZB_INFO = 0x113;
    private final int JJ_INFO = 0x133;
    private final int NETWORK_EXCEPTION = 0x212;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_list_activity);

        getTitle().setText("所有方案");
        View no = findViewById(R.id.view_no);
        Button button = findViewById(R.id.no_text);
        button.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.no_txt);
        textView.setVisibility(View.VISIBLE);
        textView.setText("暂无方案");
        xRecyclerView = findViewById(R.id.xRecyclerView);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton tab1 = findViewById(R.id.tab1);
        RadioButton tab2 = findViewById(R.id.tab2);
        RadioButton tab3 = findViewById(R.id.tab3);
        RadioButton tab4 = findViewById(R.id.tab4);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle bundle = getIntent().getExtras();
        TYPE = bundle.getInt("TYPE");
        String id = bundle.getString("ID");
        String gzid = bundle.getString("GZID");
        adapter = new ItemAdapter(TYPE, this, list, id, gzid);
        itemDecoration = new LhtTool.MyItemDecoration(getResources());
        xRecyclerView.addItemDecoration(itemDecoration);
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
            tab1.setText("全部");
            tab2.setText("未查看");
            tab3.setText("备选");
            tab4.setText("中标");
            getXsInfo();
        } else if (TYPE == 1) {
            radioGroup.setVisibility(View.GONE);
            getZbInfo();
        } else if (TYPE == 2) {
            tab1.setText("全部");
            tab2.setText("未评标");
            tab3.setText("合格");
            tab4.setText("不合格");
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
                    map.put("pages", xuanShangFangAnGson.getPagerInfo().getNextPageIndex());
                    getXsInfo();
                } else if (TYPE == 1) {
                    map.put("pages", zhaoBiaoFangAnGson.getPagerInfo().getNextPageIndex());
                    getZbInfo();
                } else if (TYPE == 2) {
                    map.put("pages", jiJianFangAnGson.getPagerInfo().getNextPageIndex());
                    getJjInfo();
                }

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.tab1) {
                    map.put("seltype", "all");
                    if (TYPE == 0) {
                        map.put("pages", 1);
                        getXsInfo();
                    } else if (TYPE == 1) {
                        map.put("pages", 1);
                       getZbInfo();
                    } else if (TYPE == 2) {
                        map.put("pages", 1);
                       getJjInfo();
                    }

                } else if (checkedId == R.id.tab2) {

                    if (TYPE == 0) {
                        map.put("seltype", "unread");
                        map.put("pages", 1);
                        getXsInfo();
                    } else if (TYPE == 1) {
                        map.put("seltype", "all");
                        map.put("pages", 1);
                        getZbInfo();
                    } else if (TYPE == 2) {
                        map.put("seltype", "un");
                        map.put("pages", 1);
                        getJjInfo();
                    }

                } else if (checkedId == R.id.tab3) {

                    if (TYPE == 0) {
                        map.put("seltype", "bak");
                        map.put("pages", 1);
                        getXsInfo();
                    } else if (TYPE == 1) {
                        map.put("seltype", "all");
                        map.put("pages", 1);
                        getZbInfo();
                    } else if (TYPE == 2) {
                        map.put("seltype", "yes");
                        map.put("pages", 1);
                        getJjInfo();
                    }

                } else if (checkedId == R.id.tab4) {

                    if (TYPE == 0) {
                        map.put("seltype", "yes");
                        map.put("pages", 1);
                        getXsInfo();
                    } else if (TYPE == 1) {
                        map.put("seltype", "all");
                        map.put("pages", 1);
                        getZbInfo();

                    } else if (TYPE == 2) {
                        map.put("seltype", "no");
                        map.put("pages", 1);
                        getJjInfo();
                    }

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            map.put("pages", 1);
            if (TYPE == 0) {
                getXsInfo();
            } else if (TYPE == 1) {
                getZbInfo();
            } else if (TYPE == 2) {
                getJjInfo();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void  getXsInfo() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GAOJIAN_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("=============response:" + s);
                    xuanShangFangAnGson = new Gson().fromJson(s, XuanShangFangAnGson.class);
                    hd.sendEmptyMessage(XS_INFO);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void  getZbInfo() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.BAOJIA_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("=============response:" + s);
                    zhaoBiaoFangAnGson = new Gson().fromJson(s, ZhaoBiaoFangAnGson.class);
                    hd.sendEmptyMessage(ZB_INFO);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void  getJjInfo() {

        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GAOJIAN_LIST_JJ, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("=============response:" + s);
                    jiJianFangAnGson = new Gson().fromJson(s, JiJianFangAnGson.class);
                    hd.sendEmptyMessage(JJ_INFO);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == XS_INFO) {
                if ( null!= xuanShangFangAnGson) {
                    if (xuanShangFangAnGson.getPagerInfo().getCurrPageIndex() == 1) {
                        list.clear();
                    }
                    list.addAll(xuanShangFangAnGson.getList());
                } else {
                    list.clear();
                }
            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(PlanListActivity.this, msg);
            } else if (msg.what == ZB_INFO) {

                if ( null!= zhaoBiaoFangAnGson) {
                    if (zhaoBiaoFangAnGson.getPagerInfo().getCurrPageIndex() == 1) {
                        list.clear();
                    }
                    list.addAll(zhaoBiaoFangAnGson.getList());
                } else {
                    list.clear();
                }

            } else if (msg.what == JJ_INFO) {

                if ( null!= jiJianFangAnGson) {
                    if (jiJianFangAnGson.getPagerInfo().getCurrPageIndex() == 1) {
                        list.clear();
                    }
                    list.addAll(jiJianFangAnGson.getList());
                } else {
                    list.clear();
                }

            }

            adapter.notifyDataSetChanged();
        }
    };

}
