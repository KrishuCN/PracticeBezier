package chen.vike.c680.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chen.vike.c680.bean.MesPersonSeaGson;
import chen.vike.c680.main.BaseActivity;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.ImageLoadUtils;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CircleImageView;
import chen.vike.c680.views.CustomToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by lht on 2017/3/28.
 */

public class MessageSearchActivity extends BaseActivity {

    private LinearLayout linear_bar;
    private EditText search;
    private TextView sousuo;
    private ImageView fanhui;
    private XRecyclerView xRecyclerView;
    private Map<String, Object> map = new HashMap<>();
    private MesPersonSeaGson mesPersonSeaGson;
    private List<MesPersonSeaGson.ListBean> list = new ArrayList<>();
    private MyAdapter adapter = new MyAdapter();
    private final int GET_MESSAGE = 0x123;
    private final int NETWORK_EXCEPTION = 0x121;
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_MESSAGE) {

                if (null != mesPersonSeaGson) {
                    if (mesPersonSeaGson.getPagerInfo().getCurrPageIndex() == 1) {
                        list.clear();
                        list = mesPersonSeaGson.getList();
                        xRecyclerView.refreshComplete();
                    } else {
                        list.addAll(mesPersonSeaGson.getList());
                        xRecyclerView.loadMoreComplete();
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    list = null;
                    adapter.notifyDataSetChanged();
                    CustomToast.showToast(MessageSearchActivity.this, "无此联系人", Toast.LENGTH_SHORT);
                }

            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(MessageSearchActivity.this, msg);
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_search_activity);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            linear_bar = (LinearLayout) findViewById(R.id.linear_bar);
            linear_bar.setVisibility(View.VISIBLE);
            int statusHeight = LhtTool.getStatusBarHeight(this);
            android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) linear_bar.getLayoutParams();
            params.height = statusHeight;
            linear_bar.setLayoutParams(params);
        }


        linear_bar.setBackgroundColor(Color.parseColor("#f53a33"));
        //在ActionBar隐藏的界面这样写ActionBar那一块会形成空白，原因不明
//        LhtTool.setColor(this, Color.parseColor("#f53a33"));


        search = (EditText) findViewById(R.id.ms_search);
        sousuo = (TextView) findViewById(R.id.ms_quxiao);
        fanhui = (ImageView) findViewById(R.id.mes_fh);
        xRecyclerView = (XRecyclerView) findViewById(R.id.search_person_lv);

        map.put("num", 10);
        map.put("pages", 1);
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
        }

        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search.getText().toString().equals("")) {
                    CustomToast.showToast(MessageSearchActivity.this, "搜索内容不能为空~", Toast.LENGTH_SHORT);
                } else {
                    map.put("search_key", search.getText().toString());
                    getMessage();
                }
            }
        });


        xRecyclerView.setLayoutManager(new LinearLayoutManager(MessageSearchActivity.this));
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);

        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                map.put("pages", 1);
                getMessage();
            }

            @Override
            public void onLoadMore() {

                if (null != mesPersonSeaGson) {
                    if (mesPersonSeaGson.getPagerInfo().getNextPageIndex() == 0) {
                        CustomToast.showToast(MessageSearchActivity.this, "已经到最后了~", Toast.LENGTH_SHORT);
                        xRecyclerView.setNoMore(true);
                    } else {
                        map.put("pages", mesPersonSeaGson.getPagerInfo().getNextPageIndex());
                        getMessage();
                    }
                }

            }
        });

    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(MessageSearchActivity.this).inflate(R.layout.listview_message_item, null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            holder.itemView.setLayoutParams(lp);
            ImageLoadUtils.Companion.display(MessageSearchActivity.this,holder.iv,list.get(position).getFaceimg());

            holder.tv.setText(list.get(position).getUsername());
            holder.tv1.setText(list.get(position).getContent());
            holder.tv2.setText(list.get(position).getSendtime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LhtTool.isLogin) {
                        Intent intent = new Intent(MessageSearchActivity.this, MessageChatActivity.class);
                        intent.putExtra("ID", list.get(position).getLinkmanid());
                        intent.putExtra("name", list.get(position).getUsername());
                        intent.putExtra("imageurl", list.get(position).getFaceimg());
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            if (null == list) {
                return 0;
            }
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private CircleImageView iv;
            private TextView tv;
            private TextView tv1;
            private TextView tv2;

            public MyViewHolder(View itemView) {
                super(itemView);
                iv = (CircleImageView) itemView.findViewById(R.id.avatar);
                tv = (TextView) itemView.findViewById(R.id.message_name);
                tv1 = (TextView) itemView.findViewById(R.id.message_content);
                tv2 = (TextView) itemView.findViewById(R.id.message_time);
            }
        }


    }


    private void getMessage() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.SEARCH_PERSON, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("=================response:" + s);
                    mesPersonSeaGson = new Gson().fromJson(s, MesPersonSeaGson.class);
                    hd.sendEmptyMessage(GET_MESSAGE);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


}
