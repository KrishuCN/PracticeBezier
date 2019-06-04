package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import chen.vike.c680.bean.WeiKePageGson;
import chen.vike.c680.main.BaseActivity;
import chen.vike.c680.tools.OkhttpTool;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chen.vike.c680.adapter.VipRecyListAdapter;
import chen.vike.c680.bean.VipProjectBean;
import chen.vike.c680.tools.ShardTools;
import chen.vike.c680.Interface.ViewItemClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/10/19.
 * vip更多
 */

public class VipMoreProjectActivity extends BaseActivity {
    private TextView loadText;
    private VipProjectBean vipProjectBean = new VipProjectBean();
    private List<WeiKePageGson.ItemListBean> loadList = new ArrayList<>() ;
    private RecyclerView recyclerView;
    private VipRecyListAdapter vipMoreAdapter;
    private LinearLayoutManager manager;
    private Context context;
    private ImageView btnBack;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_more_list);
        context = this;
        iniview();
    }

    @Override
    protected void onResume() {
      data();

        super.onResume();
    }

    private void iniview(){
        loadText = (TextView) findViewById(R.id.load_wait);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        recyclerView = (RecyclerView) findViewById(R.id.vip_more_recy);
        manager = new LinearLayoutManager(this);

         btnBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });
    }
    ViewItemClick viewItemClick = new ViewItemClick() {
        @Override
        public void shortClick(int position) {
            Intent in = new Intent(context, TaskDetailsActivity.class);
            in.putExtra("ID", loadList.get(position).getItemid());
            context.startActivity(in);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                recyclerView.setLayoutManager(manager);
                vipMoreAdapter = new VipRecyListAdapter(loadList,context);
                vipMoreAdapter.setViewItemClick(viewItemClick);
                recyclerView.setAdapter(vipMoreAdapter);
                loadText.setVisibility(View.GONE);
            }
        }
    };
  

    private void data() {
        String userid = ShardTools.getInstance(this).getTempSharedata("userid");
        String vkuserip = ShardTools.getInstance(this).getTempSharedata("loginip");
        String vktoken = ShardTools.getInstance(this).getTempSharedata("logintoken");
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("vkuserip", vkuserip);
        map.put("vktoken", vktoken);
        map.put("num", "10");
        map.put("page", "1");
        map.put("iskan", "0");
        OkhttpTool.Companion.getOkhttpTool().post("http://app.680.com/api/v3/tuisong_itemlist.ashx", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                vipProjectBean = new Gson().fromJson(s, VipProjectBean.class);
                loadList = new ArrayList<>();
                for (int i = 0; i < vipProjectBean.getList().size(); i++) {
                    WeiKePageGson.ItemListBean listBean = new WeiKePageGson.ItemListBean();
                    listBean.setField_id(vipProjectBean.getList().get(i).getTid());
                    listBean.setItemid(vipProjectBean.getList().get(i).getItemid());
                    listBean.setItemname(vipProjectBean.getList().get(i).getItemname());
                    listBean.setPrice(vipProjectBean.getList().get(i).getMoney());
                    listBean.setContent(vipProjectBean.getList().get(i).getContent());
                    listBean.setEndtime(vipProjectBean.getList().get(i).getEndtime());
                    listBean.setItemtype(vipProjectBean.getList().get(i).getItemname());
                    loadList.add(listBean);
                }
                handler.sendEmptyMessage(1);
            }
        });
    }
}
