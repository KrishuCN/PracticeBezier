package chen.vike.c680.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.webview.WebViewActivity;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chen.vike.c680.adapter.IpropAdapter;
import chen.vike.c680.bean.IpropBean;
import chen.vike.c680.Interface.EndLessOnScrollListener;
import chen.vike.c680.Interface.ViewItemClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2018/6/12.
 */

public class PersonMyIsDaoJuFrag extends Fragment {

    @BindView(R.id.jiazai_shibai)
    LinearLayout jiazaiShibai;
    @BindView(R.id.iprop_list_view)
    RecyclerView ipropListView;
    @BindView(R.id.iprop_refresh)
    SwipeRefreshLayout ipropRefresh;
    private View myDaojuView;
    private Context mContext;
    private LinearLayoutManager manager;
    private IpropAdapter ipropAdapter;
    private List<IpropBean.ItemListBean> lists = new ArrayList<>();
    private static final String URL = "http://app.680.com/api/v4/my_props.ashx";
    private static final int dataMyProp = 010;
    private static final int ERROR = 000;
    private static final int JIAZAIGENGDUO = 012;
    private final int NETWORK_EXCEPTION = 0X111;
    private Unbinder unbinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myDaojuView = LayoutInflater.from(mContext).inflate(R.layout.activity_i_prop, null);
        ButterKnife.bind(this, myDaojuView);
        unbinder = ButterKnife.bind(this,myDaojuView);
        setView();
        return myDaojuView;
    }

    private void setView() {
        manager = new LinearLayoutManager(mContext);
        ipropListView.setLayoutManager(manager);
        ipropAdapter = new IpropAdapter(lists, mContext);
        ipropAdapter.setIpropClick(useIpropClick);
        ipropListView.setAdapter(ipropAdapter);
        ipropRefresh.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));
        jiazaiShibai.setVisibility(View.GONE);
        ipropListener();
    }

    @Override
    public void onResume() {
        ipropData();
        super.onResume();
    }

    private ViewItemClick useIpropClick = new ViewItemClick() {
        @Override
        public void shortClick(int position) {
            String useDaoju = "http://apps.680.com/touch/props/use_props.aspx";
            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra("weburl", useDaoju + "?" + "userid=" + MyApplication.userInfo.getUserID() + "&" + "vkuserip=" + MyApplication.userInfo.getCookieLoginIpt()
                    + "&" + "vktoken=" + MyApplication.userInfo.getCookieLoginToken() + "&" + "id=" + lists.get(position).getMydjid());
            intent.putExtra("title", "道具使用");
            startActivity(intent);
        }
    };

    /**
     * 刷新及加载更多监听
     */
    private void ipropListener() {
        ipropRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ipropData();
                        ipropRefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        ipropListView.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (currentPage <= ipropBean.getPagerInfo().getPageTotal()) {
                    loadMore(currentPage);
                } else {
                    Toast.makeText(mContext, "没有数据了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMore(int page) {
        Toast.makeText(mContext, "正在加载", Toast.LENGTH_SHORT).show();
        Map<String, Object> map = new HashMap<>();
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map.put("page", page);
        } else {
            map.put("userid", 0);
        }
        OkhttpTool.Companion.getOkhttpTool().post(URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {

                if (lists.size() > 0) {
                    lists.clear();
                }
                try {
                    String s = response.body().string();
                    ipropBean = new Gson().fromJson(s, IpropBean.class);
                    Log.e("ipropbean", s);
                    if (ipropBean.getErr_code().equals("0")) {
                        if (ipropBean.getItemList() != null && ipropBean != null) {
                            for (int i = 0; i < ipropBean.getItemList().size(); i++) {
                                IpropBean.ItemListBean listBean = new IpropBean.ItemListBean();
                                listBean.setName(ipropBean.getItemList().get(i).getName());
                                listBean.setCont(ipropBean.getItemList().get(i).getCont());
                                listBean.setMydjnum(ipropBean.getItemList().get(i).getMydjnum());
                                listBean.setMydjid(ipropBean.getItemList().get(i).getMydjid());
                                listBean.setBianhao(ipropBean.getItemList().get(i).getBianhao());
                                listBean.setImg(ipropBean.getItemList().get(i).getImg());
                                lists.add(listBean);
                            }
                            handler.sendEmptyMessage(dataMyProp);
                        }
                    } else {
                        handler.sendEmptyMessage(ERROR);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 获取数据
     */
    private IpropBean ipropBean = new IpropBean();

    private void ipropData() {
        Map<String, Object> map = new HashMap<>();
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map.put("page", 1);
        } else {
            map.put("userid", 0);
        }
        OkhttpTool.Companion.getOkhttpTool().post(URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (lists.size() > 0) {
                    lists.clear();
                }
                try {
                    String s = response.body().string();
                    ipropBean = new Gson().fromJson(s, IpropBean.class);
                    Log.e("ipropbean", s);
                    if (ipropBean.getErr_code().equals("0")) {
                        if (ipropBean.getItemList() != null && ipropBean != null) {
                            for (int i = 0; i < ipropBean.getItemList().size(); i++) {
                                IpropBean.ItemListBean listBean = new IpropBean.ItemListBean();
                                listBean.setName(ipropBean.getItemList().get(i).getName());
                                listBean.setCont(ipropBean.getItemList().get(i).getCont());
                                listBean.setMydjnum(ipropBean.getItemList().get(i).getMydjnum());
                                listBean.setMydjid(ipropBean.getItemList().get(i).getMydjid());
                                listBean.setBianhao(ipropBean.getItemList().get(i).getBianhao());
                                listBean.setImg(ipropBean.getItemList().get(i).getImg());
                                lists.add(listBean);
                            }
                            handler.sendEmptyMessage(dataMyProp);
                        }
                    } else {
                        handler.sendEmptyMessage(ERROR);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case dataMyProp:
                    ipropAdapter.notifyDataSetChanged();
                    if (ipropRefresh != null) {
                        ipropRefresh.setRefreshing(false);
                    }
//                    title.setText("已拥有道具" + "(" + ipropBean.getTotal() + "" + ")");
                    break;
                case ERROR:
                    Toast.makeText(mContext, ipropBean.getErr_msg() + "", Toast.LENGTH_SHORT).show();
                    jiazaiShibai.setVisibility(View.VISIBLE);
                    break;
                case JIAZAIGENGDUO:
                    ipropAdapter.addList(lists);
                    ipropAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
