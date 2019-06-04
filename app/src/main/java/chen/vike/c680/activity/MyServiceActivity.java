package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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

import chen.vike.c680.bean.FuwuListGson;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.ImageLoadUtils;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.EmptySwipeRefreshLayout;
import chen.vike.c680.views.LoadingDialog;
import chen.vike.c680.views.MyListView2;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/14.
 * 我的服务
 */

public class MyServiceActivity extends BaseStatusBarActivity implements MyListView2.OnLoadListener{

    private EmptySwipeRefreshLayout srl;
    private MyListView2 lv;
    private int deleteNum;
    private LoadingDialog ld;
    private final int GETINFO = 0X123;
    private final int NETWORK_EXCEPTION = 0x111;
    private final int ONLOAD = 0X121;
    private final int DELETE = 0X122;
    private Map<String, Object> map = new HashMap<>();
    private FuwuListGson fuwuListGson;
    private MyBaseAdapter adapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myservice_activity);

        getTitle().setText("出售的服务");
        srl =  findViewById(R.id.fuwu_list_swipe);
        lv =  findViewById(R.id.fuwu_list_lv);
        srl.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));


        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("num", 10);
            map.put("pages", 1);
            srl.setRefreshing(true);
            doRefresh();
        }

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });


        View view = LayoutInflater.from(this).inflate(R.layout.view_no, null);
        TextView t = (TextView) view.findViewById(R.id.no_txt);
        //t在view里写的的字符串会被消掉，只有自己手动添加
        t.setText("暂无出售记录");
        Button b =  view.findViewById(R.id.no_text);
        b.setText("我要发布服务");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MyServiceActivity.this, SellServicesActivity.class);
                startActivity(in);
            }
        });
        b.setVisibility(View.VISIBLE);
        t.setVisibility(View.VISIBLE);
        ((ViewGroup) lv.getParent()).addView(view);
        adapter = new MyServiceActivity.MyBaseAdapter(new ArrayList<FuwuListGson.ListBean>());
        lv.setEmptyView(view);
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
        lv.setAdapter(adapter);

        lv.setOnLoadListener(this);

    }


    private void doRefresh() {

        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.USER_FUWU_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("======================Response:" + s);
                    fuwuListGson = new Gson().fromJson(s, FuwuListGson.class);
                    hd.sendEmptyMessage(GETINFO);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });


    }


    private void doLoad() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.USER_FUWU_LIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("======================Response:" + s);
                    fuwuListGson = new Gson().fromJson(s, FuwuListGson.class);
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
        if (fuwuListGson.getPagerInfo().getNextPageIndex() == 0) {
            CustomToast.showToast(this, "没有更多了！", Toast.LENGTH_SHORT);
            lv.setLoadEnable(false);
            return;
        }
        map.put("pages", fuwuListGson.getPagerInfo().getNextPageIndex());
        doLoad();
        LogUtils.d("==============load出去了");
    }


    class MyBaseAdapter extends BaseAdapter {

        private List<FuwuListGson.ListBean> list = new ArrayList<>();

        public MyBaseAdapter(List<FuwuListGson.ListBean> list) {
            this.list = list;
        }

        public void addList(List<FuwuListGson.ListBean> list) {
            this.list.addAll(list);
        }

        public void refresh() {
            list.clear();
        }

        public void remove(int position){
            list.remove(position);
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

                convertView = LayoutInflater.from(MyServiceActivity.this).inflate(R.layout.shop_item, null);
                id = new ID();
                id.logo = convertView.findViewById(R.id.fw_logo);
                id.title = convertView.findViewById(R.id.title);
                id.money = convertView.findViewById(R.id.fw_money);
                id.name = convertView.findViewById(R.id.over_sell);
                id.zhuangtai = convertView.findViewById(R.id.myshop_zt);
                id.yulan = convertView.findViewById(R.id.myshop_yls);
                id.xiugai = convertView.findViewById(R.id.myshop_yl);
                id.shanchu = convertView.findViewById(R.id.myshop_shanc);
                id.relativrlayout= convertView.findViewById(R.id.relativrlayout);
                id.xigai_img = convertView.findViewById(R.id.myshop_xiugai_img);
                id.fuwu_zhuangtai_img = convertView.findViewById(R.id.fuwu_zhuangtai_img);
                convertView.setTag(id);

            } else {
                id = (ID) convertView.getTag();
            }

            ImageLoadUtils.Companion.display(MyServiceActivity.this,id.logo,list.get(position).getImgUrl());

            id.title.setText(list.get(position).getTitle());
            if(!list.get(position).getUnit().isEmpty()){
                id.money.setText(list.get(position).getShowmoney()+"/"+list.get(position).getUnit());
            }else{
                id.money.setText(list.get(position).getShowmoney());
            }
            id.name.setText("项目编号:"+list.get(position).getId());

            //点击整个VIEW跳转到的时候跳转到修改服务列表页
            id.relativrlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyServiceActivity.this, AlertServiceActivity.class);
                    intent.putExtra("ID", (list.get(position).getId()));
                    startActivityForResult(intent, 4);
                }
            });


            id.xiugai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.valueOf(list.get(position).getCheck())>1){
                        Intent intent = new Intent(MyServiceActivity.this, ServiceDetailsActivity.class);
                        intent.putExtra("ID", (list.get(position).getId()));
                        startActivityForResult(intent, 3);
                    }else{
                        Intent intent = new Intent(MyServiceActivity.this, AlertServiceActivity.class);
                        intent.putExtra("ID", (list.get(position).getId()));
                        startActivityForResult(intent, 4);
                    }

                }
            });
            //这里改为当前服务的状态

            if(Integer.valueOf(list.get(position).getCheck())<0){
                id.zhuangtai.setText("被屏蔽");
                id.fuwu_zhuangtai_img.setBackgroundResource(R.mipmap.fuwu_blocked_img);
                id.yulan.setText("修改");
                id.xigai_img.setVisibility(View.VISIBLE);
            }else if(Integer.valueOf(list.get(position).getCheck())==0){
                id.zhuangtai.setText("未审核");
                id.fuwu_zhuangtai_img.setBackgroundResource(R.mipmap.fuwu_approved_img);
                id.yulan.setText("修改");
                id.xigai_img.setVisibility(View.VISIBLE);
            }else if(Integer.valueOf(list.get(position).getCheck())==1){
                id.zhuangtai.setText("审核未通过");
                id.fuwu_zhuangtai_img.setBackgroundResource(R.mipmap.fuwu_audit_failed_img);
                id.yulan.setText("修改");
                id.xigai_img.setVisibility(View.VISIBLE);
            }else if(Integer.valueOf(list.get(position).getCheck())>1){
                id.zhuangtai.setText("出售中");
                id.fuwu_zhuangtai_img.setBackgroundResource(R.mipmap.fuwu_ongoing_img);
                id.yulan.setText("预览");
                id.xigai_img.setVisibility(View.GONE);

            }

            //分享改为删除
            id.shanchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDleWindow(list,position);

                }
            });




            return convertView;
        }


        private class ID{
            private TextView title;
            private TextView money;
            private TextView name;
            private TextView zhuangtai;
            private TextView yulan;
            private ImageView logo,xigai_img,fuwu_zhuangtai_img;
            private LinearLayout xiugai;
            private LinearLayout shanchu;
            private RelativeLayout relativrlayout;
        }

    }

    /**
     * 显示删除的弹窗
     */
     private View viewShow;
     private PopupWindow popupWindow;
     private void showDleWindow(List<FuwuListGson.ListBean> list,int position){
         viewShow = LayoutInflater.from(MyServiceActivity.this).inflate(R.layout.delete_my_fuwu_window_item, null);
         popupWindow = new PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
         popupWindow.setFocusable(true);
         popupWindow.setOutsideTouchable(true);
         popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));//也可以直接把Color.TRANSPARENT换成0
         popupWindow.setAnimationStyle(R.style.popWindow_animation);
         WindowManager.LayoutParams lp = getWindow().getAttributes();
         lp.alpha = 0.6f; //0.0-1.0
         getWindow().setAttributes(lp);
         popupWindow.showAtLocation(viewShow, Gravity.CENTER, 0, 0);
         popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
             @Override
             public void onDismiss() {
                 WindowManager.LayoutParams lp = getWindow().getAttributes();
                 lp.alpha = 1.0f; //0.0-1.0
                 getWindow().setAttributes(lp);
             }
         });
         showData(list,position);
     }

     private TextView del_price,del_content;
     private Button del_queren,del_quexiao;
     private void showData(final List<FuwuListGson.ListBean> list, final int position){
         del_price = viewShow.findViewById(R.id.fuwu_delete_price_text);
         del_content = viewShow.findViewById(R.id.fuwu_delete_content_text);
         del_queren = viewShow.findViewById(R.id.fuwu_delete_fuwu_queren);
         del_quexiao = viewShow.findViewById(R.id.fuwu_delete_fuwu_quxiao);
         if (list != null && list.size() > 0) {
             del_price.setText("￥" + list.get(position).getMoney() + "/" + list.get(position).getUnit());
             del_content.setText(list.get(position).getTitle());
         }else {
             del_price.setText("未知错误请重新进入尝试");
         }
         del_queren.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 deleteNum=position;
                 DeleSeverce(list.get(position).getId());
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
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map.put("fuwu_id",id);
            map.put("type","del");
            OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.DELETE_FUWU, map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);

                }

                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        String s = response.body().string();
                        LogUtils.d("==============response:"+s);
                        Message ms = new Message();
                        ms.what = DELETE;
                        Bundle b = new Bundle();
                        b.putString("s", s);
                        ms.setData(b);
                        hd.sendMessage(ms);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            ld = new LoadingDialog(this).setMessage("删除中...");
            ld.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4){
            doRefresh();
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GETINFO) {

                lv.setLoadEnable(true);
                if (null != fuwuListGson) {
                    if (fuwuListGson.getPagerInfo().getNextPageIndex() == 0) {
                        //此处判断是否有下一页，用于将footview直接屏蔽掉
                        lv.setLoadEnable(false);
                    }
                    adapter.refresh();
                    adapter.addList(fuwuListGson.getList());
                } else {
                    adapter.refresh();
                }
                srl.setRefreshing(false);
                adapter.notifyDataSetChanged();
                //lv.setSelection(0);

            } else if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(MyServiceActivity.this, msg);

            } else if (msg.what == ONLOAD) {

                if (null != fuwuListGson) {
                    adapter.addList(fuwuListGson.getList());
                }
                lv.onLoadComplete();
                adapter.notifyDataSetChanged();

            } else if (msg.what == DELETE) {
                String s = msg.getData().getString("s");
                switch (s){
                    case "unlogin":
                        CustomToast.showToast(MyServiceActivity.this,"未登录",Toast.LENGTH_SHORT);
                        break;
                    case "nofwid":
                        CustomToast.showToast(MyServiceActivity.this,"id无效",Toast.LENGTH_SHORT);
                        break;
                    case "ok":
                        CustomToast.showToast(MyServiceActivity.this,"删除成功",Toast.LENGTH_SHORT);
                        adapter.remove(deleteNum);
                        adapter.notifyDataSetChanged();
                        break;
                    case "fail":
                        CustomToast.showToast(MyServiceActivity.this,"删除失败",Toast.LENGTH_SHORT);
                        break;
                }
                if (ld != null) {
                    ld.dismiss();
                }
            }
        }
    };


}
