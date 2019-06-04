package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.views.MyListView2;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.vike.c680.adapter.CaiFuAdapter;
import chen.vike.c680.bean.CaiFuBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/12/12.
 */

public class PersonCaiFuActivity extends BaseStatusBarActivity implements MyListView2.OnLoadListener {

    private final static String URL = "http://app.680.com/api/user_finance_list.ashx";
    @BindView(R.id.shouru_text_zanwu)
    ImageView shouruTextZanwu;
    @BindView(R.id.caifu_list)
    MyListView2 caifuList;
    @BindView(R.id.caifu_refresh)
    SwipeRefreshLayout caifuRefresh;
    private CaiFuAdapter caiFuAdapter;
    private List<CaiFuBean.ListBean> lists = new ArrayList<>();
    private Context context;
    private final static int HUOQUDATA = 010;
    private final static int JIAZAI = 011;
    private final static int ERRO = 000;
    private int page = 1;
    private final int NETWORK_EXCEPTION = 0X111;
    //private final static int
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caifu_jilu);
        ButterKnife.bind(this);
        getTitle().setText("收入详情");
        context = this;
        setView();
    }

    private void setView() {
        caifuRefresh.setColorSchemeColors(Color.parseColor("#ff3399"),
                Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));

        View view = LayoutInflater.from(this).inflate(R.layout.view_no, null);
        TextView t = (TextView) view.findViewById(R.id.no_txt);
        //t在view里写的的字符串会被消掉，只有自己手动添加
        t.setText("暂无记录!");
        ((ViewGroup) caifuList.getParent()).addView(view);
        caifuList.setEmptyView(view);
        caiFuAdapter = new CaiFuAdapter(context, lists);
        caifuList.setAdapter(caiFuAdapter);
        caifuData();
        caifuRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        caifuData();
                    }
                }, 3000);
            }
        });
        caifuList.setOnLoadListener(this);
    }

    private CaiFuBean caiFuBean = new CaiFuBean();

    private void caifuData() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", MyApplication.userInfo.getUserID());
        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        map.put("pages", 1);
        map.put("num", 10);
        map.put("date", "");
        OkhttpTool.Companion.getOkhttpTool().post(URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(caifuHd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    if (lists.size() > 0) {
                        caiFuAdapter.refresh();
                    }
                    Log.e("ddddd", s);
                    caiFuBean = new Gson().fromJson(s, CaiFuBean.class);
                    if (caiFuBean.getErr_code().equals("0")) {
                        if (caiFuBean.getList() != null && caiFuBean.getList().size() > 0) {
                            for (int i = 0; i < caiFuBean.getList().size(); i++) {
                                CaiFuBean.ListBean listBean = new CaiFuBean.ListBean();
                                listBean.setBeizhu(caiFuBean.getList().get(i).getBeizhu());
                                listBean.setAddtime(caiFuBean.getList().get(i).getAddtime());
                                listBean.setMoney(caiFuBean.getList().get(i).getMoney());
                                listBean.setSou_zhi(caiFuBean.getList().get(i).getSou_zhi());
                                lists.add(listBean);
                            }
                            caifuHd.sendEmptyMessage(HUOQUDATA);
                        }

                    } else {
                        caifuHd.sendEmptyMessage(ERRO);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void jiazaiData(int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", MyApplication.userInfo.getUserID());
        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        map.put("pages", page);
        map.put("num", 10);
        map.put("date", "");
        OkhttpTool.Companion.getOkhttpTool().post(URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(caifuHd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();

                    caiFuBean = new Gson().fromJson(s, CaiFuBean.class);
                    if (caiFuBean.getList() != null && caiFuBean.getList().size() > 0) {
                        for (int i = 0; i < caiFuBean.getList().size(); i++) {
                            CaiFuBean.ListBean listBean = new CaiFuBean.ListBean();
                            listBean.setBeizhu(caiFuBean.getList().get(i).getBeizhu());
                            listBean.setAddtime(caiFuBean.getList().get(i).getAddtime());
                            listBean.setMoney(caiFuBean.getList().get(i).getMoney());
                            listBean.setSou_zhi(caiFuBean.getList().get(i).getSou_zhi());
                            lists.add(listBean);
                        }
                        caifuHd.sendEmptyMessage(JIAZAI);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler caifuHd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HUOQUDATA:
                    //  caiFuAdapter.addlist(lists);
                    caifuRefresh.setRefreshing(false);
                    caiFuAdapter.notifyDataSetChanged();
                    break;
                case JIAZAI:
                    caiFuAdapter.addlist(lists);
                    caiFuAdapter.notifyDataSetChanged();
                    break;
                case ERRO:
                    // CustomToast.showToast(PersonCaiFuActivity.this, "暂无数据!", Toast.LENGTH_SHORT);
                    caifuRefresh.setRefreshing(false);
                    caifuList.setLoadEnable(false);
                    break;
                case NETWORK_EXCEPTION:
                    LhtTool.showNetworkException(PersonCaiFuActivity.this, msg);
                    break;
                    default:
                        break;

            }
        }
    };

    @Override
    public void onLoad() {
        page++;

        jiazaiData(page);
    }
}
