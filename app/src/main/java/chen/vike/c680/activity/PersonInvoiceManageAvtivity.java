package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.vike.c680.adapter.FaPiaoAdapter;
import chen.vike.c680.bean.ErrorBean;
import chen.vike.c680.bean.PersonFaPiaoBean;
import chen.vike.c680.Interface.EndLessOnScrollListener;
import chen.vike.c680.Interface.ViewItemClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/12/11.
 * 发票管理页面
 */

public class PersonInvoiceManageAvtivity extends BaseStatusBarActivity {

    @BindView(R.id.jiazai_shibai)
    LinearLayout jiazaiShibai;
    @BindView(R.id.dengdai_jiazai)
    ProgressBar dengdaiJiazai;
    @BindView(R.id.invoice_list_view)
    RecyclerView invoiceListView;
    @BindView(R.id.fa_manager_refresh)
    SwipeRefreshLayout faManagerRefresh;
    private LinearLayoutManager manager;
    private FaPiaoAdapter faPiaoAdapter;
    private Context context;
    private List<PersonFaPiaoBean.ItemsBean> lists = new ArrayList<>();
    private final static String URL = "http://app.680.com/api/v4/my_invoices.ashx";
    private final static String fQuURL = "http://app.680.com/api/v4/edit_invoice.ashx";
    private int page = 1;
    private final static int JIAZAIDATA = 010;
    private final static int ERROR = 000;
    private final static int GENGDUODATA = 011;
    private final static int fpERROR = 001;
    private final int NETWORK_EXCEPTION = 0X111;
    private PersonFaPiaoBean personFaPiaoBean = new PersonFaPiaoBean();
    Map<String, Object> map = new HashMap<>();
    private ErrorBean errorBean = new ErrorBean();
    private int tempNumber;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fapiao_guanli);
        ButterKnife.bind(this);
        getTitle().setText("发票管理");
        context = this;
        setView();
    }

    private void setView() {
        manager = new LinearLayoutManager(context);
        invoiceListView.setLayoutManager(manager);
        faPiaoAdapter = new FaPiaoAdapter(context, lists);
        faPiaoAdapter.setFaPiaoClick(viewItemClick);
        faPiaoAdapter.setEidtClick(editClick);
        invoiceListView.setAdapter(faPiaoAdapter);
        faManagerRefresh.setColorSchemeColors(Color.parseColor("#ff3399"),
                Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));
        listData(page);
        listener();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dengdaiJiazai.setVisibility(View.GONE);
                listData(page);
            }
        }, 4000);
    }

    /**
     * 监听事件
     */
    private void listener() {
        invoiceListView.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (currentPage <= personFaPiaoBean.getPagerInfo().getPageTotal()) {
                    gengDuoData(currentPage);
                } else {
                    Toast.makeText(context, "最后一页了！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        faManagerRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listData(page);
                    }
                }, 3000);

            }
        });
        jiazaiShibai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData(page);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dengdaiJiazai.setVisibility(View.GONE);

                    }
                }, 3000);
                jiazaiShibai.setVisibility(View.GONE);

            }
        });
    }

    private ViewItemClick viewItemClick = new ViewItemClick() {
        @Override
        public void shortClick(int position) {
            fpQuXiao(personFaPiaoBean.getItems().get(position).getFpid());
            tempNumber = position;
            //faPiaoAdapter.shanchu(position);
        }
    };
    private ViewItemClick editClick = new ViewItemClick() {
        @Override
        public void shortClick(int position) {
            Intent intent = new Intent(context, PersonFaPaioEditActivity.class);
            intent.putExtra("fpid", personFaPiaoBean.getItems().get(position).getFpid());
            startActivity(intent);
        }
    };

    /**
     * 取消发票
     */
    private void fpQuXiao(String fpid) {
        Map<String, Object> quMap = new HashMap<>();
        quMap.put("act", "cancel");
        quMap.put("userid", MyApplication.userInfo.getUserID());
        quMap.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        quMap.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        quMap.put("id", fpid);
        OkhttpTool.Companion.getOkhttpTool().post(fQuURL, quMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(managerHd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    Log.e("sss", s);
                    errorBean = new Gson().fromJson(s, ErrorBean.class);
                    if (errorBean.getErr_code() != null && errorBean != null) {
                        managerHd.sendEmptyMessage(fpERROR);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 加载数据
     */

    private void listData(int page) {
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        } else {
            map.put("userid", 0);
        }
        map.put("page", page + "");
        OkhttpTool.Companion.getOkhttpTool().post(URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(managerHd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
//                Log.e("fapiaoguanli", s);
                    personFaPiaoBean = new Gson().fromJson(s, PersonFaPiaoBean.class);
                    if (lists.size() > 0) {
                        lists.clear();
                    }

                    if (personFaPiaoBean.getErr_code().equals("0")) {
                        for (int i = 0; i < personFaPiaoBean.getItems().size(); i++) {
                            PersonFaPiaoBean.ItemsBean itemsBean = new PersonFaPiaoBean.ItemsBean();
                            itemsBean.setItemname(personFaPiaoBean.getItems().get(i).getItemname());
                            itemsBean.setItemid(personFaPiaoBean.getItems().get(i).getItemid());
                            itemsBean.setExplain(personFaPiaoBean.getItems().get(i).getExplain() + "");
                            itemsBean.setState(personFaPiaoBean.getItems().get(i).getState());
                            itemsBean.setMoney(personFaPiaoBean.getItems().get(i).getMoney());
                            lists.add(itemsBean);
                        }
                        managerHd.sendEmptyMessage(JIAZAIDATA);
                    } else {
                        managerHd.sendEmptyMessage(ERROR);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 加载更多
     *
     * @param page
     */
    private void gengDuoData(int page) {
        map.put("page", page);
        OkhttpTool.Companion.getOkhttpTool().post(URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(managerHd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    personFaPiaoBean = new Gson().fromJson(s, PersonFaPiaoBean.class);
                    if (personFaPiaoBean.getErr_code().equals("1")) {
                        for (int i = 0; i < personFaPiaoBean.getItems().size(); i++) {
                            PersonFaPiaoBean.ItemsBean itemsBean = new PersonFaPiaoBean.ItemsBean();
                            itemsBean.setItemname(personFaPiaoBean.getItems().get(i).getItemname());
                            itemsBean.setItemid(personFaPiaoBean.getItems().get(i).getItemid());
                            itemsBean.setExplain(personFaPiaoBean.getItems().get(i).getExplain());
                            itemsBean.setState(personFaPiaoBean.getItems().get(i).getState());
                            itemsBean.setMoney(personFaPiaoBean.getItems().get(i).getMoney());
                            itemsBean.setFpid(personFaPiaoBean.getItems().get(i).getFpid());
                            lists.add(itemsBean);
                        }
                        managerHd.sendEmptyMessage(GENGDUODATA);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler managerHd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case JIAZAIDATA:
                    faManagerRefresh.setRefreshing(false);
                    faPiaoAdapter.notifyDataSetChanged();
                    dengdaiJiazai.setVisibility(View.GONE);
                    jiazaiShibai.setVisibility(View.GONE);
                    faManagerRefresh.setRefreshing(false);
                    break;
                case ERROR:
                    jiazaiShibai.setVisibility(View.VISIBLE);
                    faManagerRefresh.setRefreshing(false);
                    faManagerRefresh.setVisibility(View.GONE);
                    dengdaiJiazai.setVisibility(View.GONE);
                    break;
                case GENGDUODATA:
                    faPiaoAdapter.addlist(lists);
                    faPiaoAdapter.notifyDataSetChanged();
                    break;
                case fpERROR:
                    if (errorBean.getErr_code().equals("0")) {
                        faPiaoAdapter.shanchu(tempNumber);
                        Toast.makeText(context, errorBean.getErr_msg() + "", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, errorBean.getErr_msg() + "", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case NETWORK_EXCEPTION:
                    LhtTool.showNetworkException(PersonInvoiceManageAvtivity.this, msg);
                    break;
                    default:
                        break;
            }
        }
    };
}
