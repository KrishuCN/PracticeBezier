package chen.vike.c680.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import chen.vike.c680.bean.VipRecordGson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
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
 * Created by lht on 2017/3/18.
 *
 * vip记录
 */

public class WeiKeMyVipRecordActivity extends BaseStatusBarActivity{

    private XRecyclerView xRecyclerView;
    private Map<String, Object> map = new HashMap<>();
    private List<VipRecordGson.ListBean> list = new ArrayList<>();
    private VipRecordGson vipRecordGson;
    private LoadingDialog ld;
    private RecyclerView.ItemDecoration itemDecoration;
    private final int GET_RECORD = 0x123;
    private final int NETWORK_EXCEPTION = 0x121;
    private Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_RECORD) {

                if(list.size()>0){
                    list.clear();
                }
                list.addAll(vipRecordGson.getList());
                adapter.notifyDataSetChanged();

            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(WeiKeMyVipRecordActivity.this, msg);
            }
            if (ld != null) {
                ld.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_record_activity);

        getTitle().setText("VIP购买记录");
        xRecyclerView=(XRecyclerView) findViewById(R.id.sell_listview);
        View view = LayoutInflater.from(this).inflate(R.layout.view_no, null);
        Button button = (Button) view.findViewById(R.id.no_text);
        TextView textView = (TextView) view.findViewById(R.id.no_txt);
        button.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText("暂无购买记录！");
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setEmptyView(view);
        itemDecoration = new LhtTool.MyItemDecoration(getResources());
        xRecyclerView.addItemDecoration(itemDecoration);
        map.put("userid", MyApplication.userInfo.getUserID());
        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.VIP_RECORD, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("==============response:"+s);
                    vipRecordGson = new Gson().fromJson(s, VipRecordGson.class);
                    hd.sendEmptyMessage(GET_RECORD);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        ld = new LoadingDialog(this).setMessage("加载中...");
        ld.show();
        xRecyclerView.setAdapter(adapter);
    }

    XRecyclerView.Adapter adapter=new XRecyclerView.Adapter(){

         class MyViewHolder extends RecyclerView.ViewHolder{

             private TextView sell_text;
             private TextView sell_time;
             private TextView sell_staretime;
             private TextView sell_endtime;
             private ImageView sell_imags;

             public MyViewHolder(View itemView) {
                 super(itemView);
                 sell_text = (TextView) itemView.findViewById(R.id.sell_text);
                 sell_time = (TextView) itemView.findViewById(R.id.sell_time);
                 sell_staretime = (TextView) itemView.findViewById(R.id.sell_staretime);
                 sell_endtime = (TextView) itemView.findViewById(R.id.sell_endtime);
                 sell_imags = (ImageView) itemView.findViewById(R.id.sell_imags);
             }
         }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(WeiKeMyVipRecordActivity.this).inflate(R.layout.vipselladapter, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if(Integer.valueOf(list.get(position).getCheck())==4){
                ((MyViewHolder)holder).sell_text.setText("黄金VIP");
                LhtTool.isVip(4,((MyViewHolder)holder).sell_imags);
            }else if(Integer.valueOf(list.get(position).getCheck())==5){
                ((MyViewHolder)holder).sell_text.setText("铂金VIP");
                LhtTool.isVip(5,((MyViewHolder)holder).sell_imags);
            }else if(Integer.valueOf(list.get(position).getCheck())==6){
                ((MyViewHolder)holder).sell_text.setText("钻石VIP");
                LhtTool.isVip(6,((MyViewHolder)holder).sell_imags);
            }else if(Integer.valueOf(list.get(position).getCheck())==7){
                ((MyViewHolder)holder).sell_text.setText("皇冠VIP");
                LhtTool.isVip(7,((MyViewHolder)holder).sell_imags);
            }else if(Integer.valueOf(list.get(position).getCheck())==8){
                ((MyViewHolder)holder).sell_text.setText("金冠VIP");
                LhtTool.isVip(8,((MyViewHolder)holder).sell_imags);
            }
            ((MyViewHolder)holder).sell_time.setText(list.get(position).getAddtime());
            ((MyViewHolder)holder).sell_staretime.setText(list.get(position).getBtime().split(" ")[0]);
            ((MyViewHolder)holder).sell_endtime.setText(list.get(position).getEtime().split(" ")[0]);

        }

        @Override
        public int getItemCount() {
            if (null == list) {
                return 0;
            }
            return list.size();
        }
    };


}
