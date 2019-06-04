/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: XuQiuZhaoBiaoFrag
 * Author: chen
 * Date: 2019/1/23 11:43
 * Description: 我发布的招标项目
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package chen.vike.c680.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import chen.vike.c680.bean.XuQiuList;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.EmptySwipeRefreshLayout;
import chen.vike.c680.views.LoadingDialog;
import chen.vike.c680.views.MyListView2;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chen.vike.c680.adapter.MyXuQiuAdapter;
import chen.vike.c680.Interface.ViewItemClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class XuQiuZhaoBiaoFrag extends Fragment implements MyListView2.OnLoadListener{
        @BindView(R.id.fabu_list_lv)
        MyListView2 fabuListLv;
        @BindView(R.id.fabu_list_swipe)
        EmptySwipeRefreshLayout fabuListSwipe;
        Unbinder unbinder;
        private Context mContext;
        private View quanBuView;
        private Map<String, Object> map = new HashMap<>();
        private XuQiuList xuQiuList;
        private final int GETINFO = 0X123;
        private final int NETWORK_EXCEPTION = 0X121;
        private final int ONLOAD = 0X111;
        private MyXuQiuAdapter myXuQiuAdapter;
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
            iniview();
            isLoginFresh();
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_no, null);
            TextView t =  view.findViewById(R.id.no_txt);
            //t在view里写的的字符串会被消掉，只有自己手动添加
            t.setText("没有发布项目");
            ((ViewGroup) fabuListLv.getParent()).addView(view);
            fabuListLv.setEmptyView(view);
            fabuListSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    doRefresh();

                }
            });
            return quanBuView;
        }

        private void iniview(){
            myXuQiuAdapter = new MyXuQiuAdapter(new ArrayList<XuQiuList.ListBean>(),mContext,map);
            myXuQiuAdapter.setViewItemClick(viewItemClick);
            fabuListLv.setAdapter(myXuQiuAdapter);
            fabuListLv.setOnLoadListener(this);
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
                map.put("type", 2);
                fabuListSwipe.setRefreshing(true);
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
                public void onResponse(Call call, Response response) {
                    try {
                        String s = response.body().string();
                        LogUtils.d("======================Response:" + s);
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
                        LogUtils.d("======================Response:" + s);
                        xuQiuList = new Gson().fromJson(s, XuQiuList.class);
                        hd.sendEmptyMessage(ONLOAD);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }

        /**
         * 删除监听
         */
        ViewItemClick viewItemClick = new ViewItemClick() {
            @Override
            public void shortClick(int position) {
                showDleWindow(xuQiuList.getList(),position);
            }
        };
        /**
         * 显示删除的弹窗
         */
        private View viewShow;
        private PopupWindow popupWindow;
        private void showDleWindow(List<XuQiuList.ListBean> list, int position){
            viewShow = LayoutInflater.from(mContext).inflate(R.layout.delete_my_fuwu_window_item, null);
            popupWindow = new PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));//也可以直接把Color.TRANSPARENT换成0
            popupWindow.setAnimationStyle(R.style.popWindow_animation);
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.6f; //0.0-1.0
            getActivity().getWindow().setAttributes(lp);
            popupWindow.showAtLocation(viewShow, Gravity.CENTER, 0, 0);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 1.0f; //0.0-1.0
                    getActivity().getWindow().setAttributes(lp);
                }
            });
            showData(list,position);
        }

        private TextView del_price,del_content;
        private Button del_queren,del_quexiao;
        private int deleteNum;
        private LoadingDialog ld;
        private final int DELETE = 0X122;
        private void showData(final List<XuQiuList.ListBean> list, final int position){
            del_price = viewShow.findViewById(R.id.fuwu_delete_price_text);
            del_content = viewShow.findViewById(R.id.fuwu_delete_content_text);
            del_queren = viewShow.findViewById(R.id.fuwu_delete_fuwu_queren);
            del_quexiao = viewShow.findViewById(R.id.fuwu_delete_fuwu_quxiao);
            if (list != null && list.size() > 0) {
                del_price.setText("￥" + list.get(position).getMoney() + "元");
                del_content.setText(list.get(position).getItemname());
            }else {
                del_price.setText("未知错误请重新进入尝试");
            }
            del_queren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteNum=position;
                    DeleSeverce(list.get(position).getItemid());
                    popupWindow.dismiss();
                }
            });
            del_quexiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
        public void DeleSeverce(String id){
            if(LhtTool.isLogin){
                map.put("itemId", id);
                map.put("loginUserId", MyApplication.userInfo.getUserID());
                map.put("userid", MyApplication.userInfo.getUserID());
                map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                //  map.put("fuwu_id",id);
                // map.put("type","del");
                map.put("num", "1");
                map.put("pages", "1");
                map.put("seltype", "all");
                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.DELETE_PROJECT, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String s = response.body().string();
                        LogUtils.d("==============response:"+s);
                        Message ms = new Message();
                        ms.what = DELETE;
                        Bundle b = new Bundle();
                        b.putString("s", s);
                        ms.setData(b);
                        hd.sendMessage(ms);

                    }
                });
                ld = new LoadingDialog(mContext).setMessage("删除中...");
                ld.show();
            }
        }

        @Override
        public void onLoad() {

            if (xuQiuList.getPagerInfo().getNextPageIndex() == 0) {
                CustomToast.showToast(mContext, "没有更多了！", Toast.LENGTH_SHORT);
                fabuListLv.setLoadEnable(false);
                return;
            }
            map.put("pages", xuQiuList.getPagerInfo().getNextPageIndex());
            doLoad();

        }

        @SuppressLint("HandlerLeak")
        private Handler hd = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == GETINFO) {

                    fabuListLv.setLoadEnable(true);
                    if (null != xuQiuList) {
                        if (xuQiuList.getPagerInfo().getNextPageIndex() == 0) {
                            //此处判断是否有下一页，用于将footview直接屏蔽掉
                            fabuListLv.setLoadEnable(false);
                        }
                        myXuQiuAdapter.refresh();
                        myXuQiuAdapter.addList(xuQiuList.getList());
                    } else {
                        myXuQiuAdapter.refresh();
                    }
                    fabuListSwipe.setRefreshing(false);
                    myXuQiuAdapter.notifyDataSetChanged();
                    fabuListLv.setSelection(0);

                } else if (msg.what == NETWORK_EXCEPTION) {

                    LhtTool.showNetworkException(mContext, msg);

                } else if (msg.what == ONLOAD) {

                    myXuQiuAdapter.addList(xuQiuList.getList());
                    fabuListLv.onLoadComplete();
                    myXuQiuAdapter.notifyDataSetChanged();

                }else if (msg.what == DELETE) {
                    String s = msg.getData().getString("s");
                    switch (s){
                        case "unlogin":
                            CustomToast.showToast(mContext,"未登录",Toast.LENGTH_SHORT);
                            break;
                        case "nofwid":
                            CustomToast.showToast(mContext,"id无效",Toast.LENGTH_SHORT);
                            break;
                        case "ok":
                            CustomToast.showToast(mContext,"删除成功",Toast.LENGTH_SHORT);
                            myXuQiuAdapter.remove(deleteNum);
                            myXuQiuAdapter.notifyDataSetChanged();
                            break;
                        case "fail":
                            CustomToast.showToast(mContext,"删除失败",Toast.LENGTH_SHORT);
                            break;
                    }
                    if (ld != null) {
                        ld.dismiss();
                    }
                }
            }
        };

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

}
