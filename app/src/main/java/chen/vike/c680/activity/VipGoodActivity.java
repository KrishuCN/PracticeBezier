package chen.vike.c680.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import chen.vike.c680.bean.WeiKePageGson;
import chen.vike.c680.main.BaseActivity;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.MyListView2;
import chen.vike.c680.adapter.MyWKListViewAdapter;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chen.vike.c680.tools.ShardTools;
import chen.vike.c680.bean.VipGoodBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/10/20.
 * 定制项目
 */

public class VipGoodActivity extends BaseActivity implements MyListView2.OnLoadListener {
    private SwipeRefreshLayout srl;
    private MyListView2 lv;
    private MyWKListViewAdapter myadapter;
    private Context con;
    private List<WeiKePageGson.ItemListBean> dingzhiLoadList = new ArrayList<>();
    private int weiKePageNum;
    private static String URL = "http://app.680.com/api/v3/vipku_item_list.ashx";
    private ImageView backImg;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_dingzhi);
        con = this;
    }
    private void iniview(){
        backImg = (ImageView) findViewById(R.id.btn_back);
        srl = (SwipeRefreshLayout) findViewById(R.id.swipe_dingzhi);

        srl.setColorSchemeColors(Color.parseColor("#ff3399"), Color.parseColor("#aa2299"), Color.parseColor("#dd5599"));

        lv = (MyListView2)findViewById(R.id.list_dingzhi);
        myadapter = new MyWKListViewAdapter(con, weikeload);
        lv.setAdapter(myadapter);
       httpData();
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv.setOnLoadListener(this);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("TAG","slssssssss");
            }
        });
    }
   Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           switch (msg.what){
               case 1:
                   myadapter.addlist(weikeload);
                   myadapter.notifyDataSetChanged();
                   lv.onLoadComplete();
                   break;
               case 2:

                   break;

           }
       }
   };
    @Override
    protected void onResume() {
        super.onResume();
        iniview();
    }
   private VipGoodBean vipGoodBean = new VipGoodBean();
    private List<WeiKePageGson.ItemListBean> weikeload = new ArrayList<>();
    public void httpData(){
        Map<String,Object> map = new HashMap<>();
        String userid = ShardTools.getInstance(con).getTempSharedata("userid");
        String vkuserip = ShardTools.getInstance(con).getTempSharedata("loginip");
        String vktoken = ShardTools.getInstance(con).getTempSharedata("logintoken");
        map.put("userid",userid);
        map.put("vkuserip",vkuserip);
        map.put("vktoken",vktoken);
        OkhttpTool.Companion.getOkhttpTool().post(URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    vipGoodBean = new Gson().fromJson(s,VipGoodBean.class);
                    for (int i = 0;i < vipGoodBean.getList().size();i++){
                        WeiKePageGson.ItemListBean listBean = new WeiKePageGson.ItemListBean();
                        listBean.setItemid(vipGoodBean.getList().get(i).getItemid());
                        listBean.setItemname(vipGoodBean.getList().get(i).getItemname());
                        listBean.setPrice(vipGoodBean.getList().get(i).getMoney());
                        listBean.setContent(vipGoodBean.getList().get(i).getContent());
                        listBean.setEndtime(vipGoodBean.getList().get(i).getEndtime());
                        listBean.setItemtype(vipGoodBean.getList().get(i).getZab_do());
                        weikeload.add(listBean);
                    }
                    Log.e("listbeansize",weikeload.size()+"");
                    handler.sendEmptyMessage(1);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    public void onLoad() {
        Log.e("TAG","refload");
        weiKePageNum++;
        Map<String, Object> map = new HashMap<>();
        String userid = ShardTools.getInstance(con).getTempSharedata("userid");
        String vkuserip = ShardTools.getInstance(con).getTempSharedata("loginip");
        String vktoken = ShardTools.getInstance(con).getTempSharedata("logintoken");
        map.put("userid",userid);
        map.put("vkuserip",vkuserip);
        map.put("vktoken",vktoken);
        if (weiKePageNum == 2) {
            map.put("pages", weiKePageNum + "");
        } else if (weiKePageNum > 2 && vipGoodBean.getPagerInfo().getNextPageIndex() != 0) {
            map.put("pages", weiKePageNum + "");
        } else {
            CustomToast.showToast(con, "已经是最后一页了!", Toast.LENGTH_SHORT);
            lv.setLoadEnable(false);
            return;
        }
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.WEIKE_ITEM_MORE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response){
                try {
                    String s = response.body().string();
                    vipGoodBean = new Gson().fromJson(s, VipGoodBean.class);
                    weikeload = new ArrayList<>();
                    for (int i = 0; i < vipGoodBean.getList().size(); i++) {
                        WeiKePageGson.ItemListBean listBean = new WeiKePageGson.ItemListBean();
                        listBean.setItemid(vipGoodBean.getList().get(i).getItemid());
                        listBean.setItemname(vipGoodBean.getList().get(i).getItemname());
                        listBean.setPrice(vipGoodBean.getList().get(i).getMoney());
                        listBean.setContent(vipGoodBean.getList().get(i).getContent());
                        listBean.setEndtime(vipGoodBean.getList().get(i).getEndtime());
                        listBean.setItemtype(vipGoodBean.getList().get(i).getZab_do());
                        weikeload.add(listBean);
                    }
                    handler.sendEmptyMessage(1);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
