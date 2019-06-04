package chen.vike.c680.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import chen.vike.c680.bean.SystemListGson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.webview.WebViewActivity;
import chen.vike.c680.views.CustomToast;
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
 * Created by lht on 2017/3/24.
 */

public class MessageSystemListActivity extends BaseStatusBarActivity {

    private XRecyclerView xRecyclerView;
    private Map<String, Object> map = new HashMap<>();
    private SystemListGson systemListGson;
    private List<SystemListGson.ListBean> list = new ArrayList<>();
    private LoadingDialog ld;
    private MyrecycAdapter adapter;
    private AlertDialog al;
    private RecyclerView.ItemDecoration itemDecoration;
    private DisplayMetrics metrics = new DisplayMetrics();
    private final int GET_MESSAGE = 0x123;
    private final int NETWORK_EXCEPTION = 0x111;
    private final int DELETE = 0x121;
    private final int ONLOAD_MORE = 0x122;
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_MESSAGE) {

                //这样放在一个里处理问题的，比如当前页不是1，但是我按得刷新，显示完成，但是却是加载更多
                if (null != systemListGson) {
                    list.clear();
                    list.addAll(systemListGson.getList());
                }
                xRecyclerView.refreshComplete();
                adapter.notifyDataSetChanged();
            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(MessageSystemListActivity.this, msg);
            } else if (msg.what == DELETE) {

                String s = msg.getData().getString("s");
                int position = msg.getData().getInt("position");

                switch (s) {
                    case "noid":
                        CustomToast.showToast(MessageSystemListActivity.this, "无效记录", Toast.LENGTH_SHORT);
                        break;
                    case "ok":
                        adapter.notifyItemRemoved(position);
                        break;
                }
            } else if (msg.what == ONLOAD_MORE) {

                if (null != systemListGson) {
                    list.addAll(systemListGson.getList());
                }
                xRecyclerView.loadMoreComplete();
                adapter.notifyDataSetChanged();

            }

            if (ld != null) {
                ld.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setContentView(R.layout.systemlist_activity);


        getTitle().setText("系统消息");
        xRecyclerView = (XRecyclerView) findViewById(R.id.message_lv);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(MessageSystemListActivity.this));
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        map.put("pages", "1");
        map.put("num", "10");
        map.put("userid", MyApplication.userInfo.getUserID());

        ld = new LoadingDialog(MessageSystemListActivity.this).setMessage("加载中...");
        ld.show();
        doRefresh();
        adapter = new MyrecycAdapter();

        itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildPosition(view) != 0) {
                    outRect.top = 40;
                }
            }

        };

        xRecyclerView.addItemDecoration(itemDecoration);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }

            @Override
            public void onLoadMore() {
                if (systemListGson.getPagerInfo().getNextPageIndex() == 0) {
                    CustomToast.showToast(MessageSystemListActivity.this, "没有更多了", Toast.LENGTH_SHORT);
                    xRecyclerView.setNoMore(true);
                } else {
                    OnLoadMore();
                }
            }
        });


    }


    class MyrecycAdapter extends RecyclerView.Adapter<MyrecycAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(MessageSystemListActivity.this).inflate(R.layout.tongzhiliebiao_activity, null));
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.itemView.setLayoutParams(lp);
            if (Integer.valueOf(list.get(position).getTui_con_id()) <= 0 || list.get(position).getTui_con_type().equals("no") || (list.get(position).getTui_con_type().equals("weburl") && list.get(position).getGourl().equals(""))) {
                holder.v.setVisibility(View.GONE);
                holder.rl.setVisibility(View.GONE);
            } else {
                holder.v.setVisibility(View.VISIBLE);
                holder.rl.setVisibility(View.VISIBLE);
            }
            holder.title.setText(list.get(position).getTitle());
            holder.time.setText(list.get(position).getAddtime());
            holder.content.setText(list.get(position).getContext());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getTui_con_type().equals("weburl") && !list.get(position).getGourl().equals("")) {
                        Intent in = new Intent(MessageSystemListActivity.this, WebViewActivity.class);
                        in.putExtra("weburl", list.get(position).getGourl());
                        in.putExtra("title", list.get(position).getTitle());
                        startActivityForResult(in, 2);
                    } else if (!list.get(position).getTui_con_type().equals("no") && Integer.valueOf(list.get(position).getTui_con_id()) > 0) {
                        if (list.get(position).getTui_con_type().equals("fuwu")) {
                            Intent in = new Intent(MessageSystemListActivity.this, ServiceDetailsActivity.class);
                            in.putExtra("ID", list.get(position).getTui_con_id());
                            startActivity(in);
                        } else if (list.get(position).getTui_con_type().equals("shop")) {
                            Intent in = new Intent(MessageSystemListActivity.this, GeRenZhongXinShopDetailsActivity.class);
                            in.putExtra("ID", list.get(position).getTui_con_id());
                            startActivity(in);
                        } else if (list.get(position).getTui_con_type().equals("item")) {
                            Intent in = new Intent(MessageSystemListActivity.this, TaskDetailsActivity.class);
                            in.putExtra("ID", list.get(position).getTui_con_id());
                            startActivity(in);
                        }
                    } else {
                        AlertDialog.Builder ad = new AlertDialog.Builder(MessageSystemListActivity.this)
                                .setMessage("这里不能跳转！！")
                                .setCancelable(true);
                        ad.create();
                    }
                }
            });

//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//
//                    AlertDialog.Builder ad = new AlertDialog.Builder(MessageSystemListActivity.this)
//                            .setMessage("是否删除该条信息？")
//                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    map.put("msgid", systemListGson.getList().get(position).getXid());
//                                    deleteSystemMessage(position);
//                                }
//                            })
//                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    al.dismiss();
//                                }
//                            });
//                    al = ad.create();
//
//
//                    return true;
//                }
//            });


        }

        @Override
        public int getItemCount() {
            if (null == list) {
                return 0;
            }
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView title;
            private TextView time;
            private TextView content;
            private View v;
            private RelativeLayout rl;

            public MyViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.tongzhi_title);
                time = (TextView) itemView.findViewById(R.id.tongzhi_time);
                content = (TextView) itemView.findViewById(R.id.tongzhi_content);
                v = itemView.findViewById(R.id.huixian);
                rl = (RelativeLayout) itemView.findViewById(R.id.unit);
            }
        }


    }

    private void doRefresh() {

        if (null != map.get("msgid")) {
            map.remove("msgid");
        }
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_SYSTEMLIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response)  {
                try{
                    String s = response.body().string();
                    LogUtils.d("==============response:" + s);
                    systemListGson = new Gson().fromJson(s, SystemListGson.class);
                    hd.sendEmptyMessage(GET_MESSAGE);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void OnLoadMore() {
        if (null != map.get("msgid")) {
            map.remove("msgid");
        }
        map.put("pages", systemListGson.getPagerInfo().getNextPageIndex());
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_SYSTEMLIST, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response){
                try{
                    String s = response.body().string();
                    LogUtils.d("==============response:" + s);
                    systemListGson = new Gson().fromJson(s, SystemListGson.class);
                    hd.sendEmptyMessage(ONLOAD_MORE);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


    private void deleteSystemMessage(final int position) {
        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());

        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.DELETE_SYSTEM_MESSAGE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String s = response.body().string();
                LogUtils.d("=================response:" + s);
                Message ms = new Message();
                ms.what = DELETE;
                Bundle b = new Bundle();
                b.putString("s", s);
                b.putInt("position", position);
                ms.setData(b);
                hd.sendMessage(ms);

            }
        });
    }


}
