package chen.vike.c680.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chen.vike.c680.bean.VipGson;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.LoadingDialog;
import chen.vike.c680.views.ViewDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/18.
 *
 * VIP服务
 */

public class WeiKeVipCenterActivity extends BaseStatusBarActivity{

    private String id;//用户id判断是否是vip
    private LinearLayout linear_tishivip;//这个是在未购买VIP时显示的提示信息
    private TextView vip_purchase,vip_timedate,vip_rennew,vip_join;//这个是点击时跳到购买界面的
    private ImageView imagview_vip,vip_imag;//用户头像 vip图标
    private TextView vip_name;//用户名字
    private LinearLayout vip_jilu;//vip购买记录
    private LinearLayout linrar_listview1,linrar_listview2,linrar_listview3;//这三个是在用户是VIP的时候显示项目用的
    private ListView vip_listview3,vip_listview2,vip_listview1;
    private int TYPE=1;
    private TextView vip_grad1,vip_grad;
    private ViewDialog pd;
    private Map<String, Object> map = new HashMap<>();
    private VipGson vipGson;
    private List<Object> list1 = new ArrayList<>();
    private List<Object> list2 = new ArrayList<>();
    private List<Object> list3 = new ArrayList<>();
    private int NUM=1;
    private LinearLayout linearcenter1,linearcenter2,linearcenter3;
    private boolean linearflag1,linearflag2,linearflag3;
    private LoadingDialog ld;
    private final int GET_VIP_INFO = 0x123;
    private final int NETWORK_EXCEPTION = 0x111;
    private Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_VIP_INFO) {

                if(Integer.valueOf(vipGson.getIs_vip())==1){
                    isVIP();
                    if(Integer.valueOf(vipGson.getCheck())==4){
                        NUM=4;
                        vip_grad.setText("黄金VIP");
                    }else if(Integer.valueOf(vipGson.getCheck())==5){
                        NUM=5;
                        vip_grad.setText("铂金VIP");
                    }else if(Integer.valueOf(vipGson.getCheck())==6){
                        NUM=6;
                        vip_grad.setText("钻石VIP");
                    }else if(Integer.valueOf(vipGson.getCheck())==7){
                        NUM=7;
                        vip_grad.setText("皇冠VIP");
                    }else if(Integer.valueOf(vipGson.getCheck())==8){
                        NUM=8;
                        vip_grad.setText("金冠VIP");
                    }
                    LhtTool.isVip(Integer.valueOf(vipGson.getCheck()), vip_imag);
                    vip_timedate.setText(vipGson.getVip_endtime().split(" ")[0]+"到期");
                    list1.addAll(vipGson.getTj_item_list());
                    vip_listview1.setAdapter(adapter);
                    vip_listview1.setOnItemClickListener(listener1);
                    adapter.notifyDataSetChanged();
                    list2.addAll(vipGson.getGood_item_list());
                    vip_listview2.setAdapter(adapter1);
                    vip_listview2.setOnItemClickListener(listener2);
                    adapter1.notifyDataSetChanged();
                    list3.addAll(vipGson.getCity_item_list());
                    vip_listview3.setAdapter(adapter2);
                    vip_listview3.setOnItemClickListener(listener3);
                    adapter2.notifyDataSetChanged();

                }else if(Integer.valueOf(vipGson.getIs_vip())==0){
                    noVIP();
                }

            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(WeiKeVipCenterActivity.this, msg);
            }

            if (ld != null) {
                ld.dismiss();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_center_activity);

        getTitle().setText("VIP中心");
        id=getIntent().getExtras().getString("ID");
        initId();
        initLensonear();
        initData();
        userMessage();

    }


    //初始化数据
    private void initData(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.image_erroe)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(this).load(MyApplication.userInfo.getIcon()).apply(options).into(imagview_vip);
        vip_name.setText(MyApplication.userInfo.getNickame());
    }

    /**
     * 初始化
     */
    private void initId(){
        linear_tishivip=(LinearLayout) findViewById(R.id.linear_tishivip);
        vip_jilu=(LinearLayout) findViewById(R.id.vip_jilu);
        linrar_listview1=(LinearLayout) findViewById(R.id.linrar_listview1);
        linrar_listview2=(LinearLayout) findViewById(R.id.linrar_listview2);
        linrar_listview3=(LinearLayout) findViewById(R.id.linrar_listview3);
        linearcenter3=(LinearLayout) findViewById(R.id.linearcenter3);
        linearcenter2=(LinearLayout) findViewById(R.id.linearcenter2);
        linearcenter1=(LinearLayout) findViewById(R.id.linearcenter1);
        vip_purchase=(TextView) findViewById(R.id.vip_purchase);
        vip_timedate=(TextView) findViewById(R.id.vip_timedate);
        vip_rennew=(TextView) findViewById(R.id.vip_rennew);
        vip_name=(TextView) findViewById(R.id.vip_name);
        vip_grad1=(TextView) findViewById(R.id.vip_grad1);
        vip_grad=(TextView) findViewById(R.id.vip_grad);
        vip_join=(TextView) findViewById(R.id.vip_join);
        imagview_vip=(ImageView) findViewById(R.id.imagview_vip);
        vip_imag=(ImageView) findViewById(R.id.vip_imag);
        vip_listview3=(ListView) findViewById(R.id.vip_listview3);
        vip_listview2=(ListView) findViewById(R.id.vip_listview2);
        vip_listview1=(ListView) findViewById(R.id.vip_listview1);
    }

    /**
     * 控件监听事件
     */
    private  void initLensonear(){
        vip_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击购买VIP
                Intent intent=new Intent(WeiKeVipCenterActivity.this, WeiKeBuyVipActivity.class);
                startActivityForResult(intent,2);
            }
        });
        vip_rennew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //续费VIP
                Intent intent=new Intent(WeiKeVipCenterActivity.this,WeiKeRenewVipActivity.class);
                intent.putExtra("num",String.valueOf(NUM));
                startActivityForResult(intent,2);
            }
        });
        vip_jilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //购买VIP的记录
                Intent intent=new Intent(WeiKeVipCenterActivity.this,WeiKeRenewVipActivity.class);
                intent.putExtra("ID",id);
                startActivityForResult(intent,1);
            }
        });
        vip_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击购买VIP
                Intent intent=new Intent(WeiKeVipCenterActivity.this, WeiKeBuyVipActivity.class);
                startActivityForResult(intent,2);
            }
        });
        linear_tishivip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_tishivip.setVisibility(View.GONE);
            }
        });
        linearcenter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(vipGson.getIs_vip())==1){
                    if(linearflag1){
                        if(list1.size()>0){
                            linearflag1=false;
                        }
                        linrar_listview1.setVisibility(View.GONE);
                    }else{
                        if(list1.size()>0){
                            linearflag1=true;
                        }
                        linrar_listview1.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        linearcenter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(vipGson.getIs_vip())==1){
                    if(linearflag2){
                        if(list2.size()>0){
                            linearflag2=false;
                        }
                        linrar_listview2.setVisibility(View.GONE);
                    }else{
                        if(list2.size()>0){
                            linearflag2=true;
                        }
                        linrar_listview2.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        linearcenter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(vipGson.getIs_vip())==1){
                    if(linearflag3){
                        if(list3.size()>0){
                            linearflag3=false;
                        }
                        linrar_listview3.setVisibility(View.GONE);
                    }else{
                        if(list3.size()>0){
                            linearflag3=false;
                        }
                        linrar_listview3.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
    //如果是VIP
    private void isVIP(){
        linear_tishivip.setVisibility(View.GONE);
        vip_grad1.setVisibility(View.GONE);
        vip_join.setVisibility(View.GONE);
        vip_rennew.setVisibility(View.VISIBLE);
        vip_grad.setVisibility(View.VISIBLE);
        vip_imag.setVisibility(View.VISIBLE);
        vip_rennew.setVisibility(View.VISIBLE);
        vip_timedate.setVisibility(View.VISIBLE);
        linrar_listview1.setVisibility(View.VISIBLE);
        linrar_listview2.setVisibility(View.VISIBLE);
        linrar_listview3.setVisibility(View.VISIBLE);
    }
    //不是VIP
    private  void noVIP(){
        linear_tishivip.setVisibility(View.VISIBLE);
        vip_grad1.setVisibility(View.VISIBLE);
        vip_join.setVisibility(View.VISIBLE);
        vip_grad.setVisibility(View.GONE);
        vip_imag.setVisibility(View.GONE);
        vip_timedate.setVisibility(View.GONE);
        linrar_listview1.setVisibility(View.GONE);
        linrar_listview2.setVisibility(View.GONE);
        linrar_listview3.setVisibility(View.GONE);
    }
    //尽力啊的时候根据id获取用户信息
    private void userMessage(){
        map.put("userid",id);
        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.USER_VIP_INFO, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("==============response:" + s);
                    vipGson = new Gson().fromJson(s, VipGson.class);
                    hd.sendEmptyMessage(GET_VIP_INFO);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        ld = new LoadingDialog(this).setMessage("加载中...");
        ld.show();
    }

    //推荐项目的监听。
    AdapterView.OnItemClickListener listener1=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            VipGson.TjItemListBean bean=(VipGson.TjItemListBean)list1.get(position);
            Intent intent=new Intent(WeiKeVipCenterActivity.this, TaskDetailsActivity.class);
            intent.putExtra("ID",bean.getItemid());
            startActivityForResult(intent,3);
        }
    };
    AdapterView.OnItemClickListener listener2=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            VipGson.GoodItemListBean bean=(VipGson.GoodItemListBean)list2.get(position);
            Intent intent=new Intent(WeiKeVipCenterActivity.this, TaskDetailsActivity.class);
            intent.putExtra("ID",bean.getItemid());
            startActivityForResult(intent,3);
        }
    };
    AdapterView.OnItemClickListener listener3=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            VipGson.CityItemListBean bean=(VipGson.CityItemListBean)list3.get(position);
            Intent intent=new Intent(WeiKeVipCenterActivity.this, TaskDetailsActivity.class);
            intent.putExtra("ID",bean.getItemid());
            startActivityForResult(intent,3);
        }
    };

    BaseAdapter adapter=new BaseAdapter() {
        @Override
        public int getCount() {
            if(list1.size()>=5){
                linearflag1=true;
                return 5;
            }else{
                if(list1.size()==0){
                    linearflag1=false;
                    linrar_listview1.setVisibility(View.GONE);
                }
                return list1.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return list1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ClassHodler hodler=null;
            if(convertView==null){
                convertView= LayoutInflater.from(WeiKeVipCenterActivity.this).inflate(R.layout.vipcenterlist,parent,false);
                hodler=new ClassHodler();
                hodler.moneny=(TextView) convertView.findViewById(R.id.moneny);
                hodler.name=(TextView) convertView.findViewById(R.id.name);
                convertView.setTag(hodler);
            }else{
                hodler=(ClassHodler) convertView.getTag();
            }

            if(Integer.valueOf(vipGson.getTj_item_list().get(position).getZab_do())==1){
                if(Integer.valueOf(vipGson.getTj_item_list().get(position).getZab_yusuan1())==Integer.valueOf(vipGson.getTj_item_list().get(position).getZab_yusuan2())){
                    hodler.moneny.setText(vipGson.getTj_item_list().get(position).getZab_yusuan1());
                }else if(Integer.valueOf(vipGson.getTj_item_list().get(position).getZab_yusuan1())>Integer.valueOf(vipGson.getTj_item_list().get(position).getZab_yusuan2())){
                    hodler.moneny.setText(vipGson.getTj_item_list().get(position).getZab_yusuan2()+"-"+vipGson.getTj_item_list().get(position).getZab_yusuan1());
                }else{
                    hodler.moneny.setText(vipGson.getTj_item_list().get(position).getZab_yusuan1()+"-"+vipGson.getTj_item_list().get(position).getZab_yusuan2());
                }
            }else{
                hodler.moneny.setText(vipGson.getTj_item_list().get(position).getMoney());
            }
            hodler.name.setText(vipGson.getTj_item_list().get(position).getItemname());
            return convertView;
        }

    };
    static class ClassHodler{
        private TextView moneny,name;
    }


    BaseAdapter adapter1=new BaseAdapter() {
        @Override
        public int getCount() {
            if(list2.size()>=5){
                linearflag2=true;
                return 5;
            }else{
                if(list2.size()==0){
                    linearflag2=false;
                    linrar_listview2.setVisibility(View.GONE);
                }
                return list2.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return list2.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ClassHodler hodler=null;
            if(convertView==null){
                convertView= LayoutInflater.from(WeiKeVipCenterActivity.this).inflate(R.layout.vipcenterlist,parent,false);
                hodler=new ClassHodler();
                hodler.moneny=(TextView) convertView.findViewById(R.id.moneny);
                hodler.name=(TextView) convertView.findViewById(R.id.name);
                convertView.setTag(hodler);
            }else{
                hodler=(ClassHodler) convertView.getTag();
            }

            if(Integer.valueOf(vipGson.getGood_item_list().get(position).getZab_do())==1){
                if(Integer.valueOf(vipGson.getGood_item_list().get(position).getZab_yusuan1())==Integer.valueOf(vipGson.getGood_item_list().get(position).getZab_yusuan2())){
                    hodler.moneny.setText(vipGson.getGood_item_list().get(position).getZab_yusuan1());
                }else if(Integer.valueOf(vipGson.getGood_item_list().get(position).getZab_yusuan1())>Integer.valueOf(vipGson.getGood_item_list().get(position).getZab_yusuan2())){
                    hodler.moneny.setText(vipGson.getGood_item_list().get(position).getZab_yusuan2()+"-"+vipGson.getGood_item_list().get(position).getZab_yusuan1());
                }else{
                    hodler.moneny.setText(vipGson.getGood_item_list().get(position).getZab_yusuan1()+"-"+vipGson.getGood_item_list().get(position).getZab_yusuan2());
                }
            }else{
                hodler.moneny.setText(vipGson.getGood_item_list().get(position).getMoney());
            }
            hodler.name.setText(vipGson.getGood_item_list().get(position).getItemname());
            return convertView;
        }

    };

    BaseAdapter adapter2=new BaseAdapter() {
        @Override
        public int getCount() {

            if(list3.size()>=5){
                linearflag3=true;
                return 5;
            }else{
                if(list3.size()==0){
                    linearflag3=false;
                    linrar_listview3.setVisibility(View.GONE);
                }
                return list3.size();
            }

        }

        @Override
        public Object getItem(int position) {
            return list3.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ClassHodler hodler=null;
            if(convertView==null){
                convertView= LayoutInflater.from(WeiKeVipCenterActivity.this).inflate(R.layout.vipcenterlist,parent,false);
                hodler=new ClassHodler();
                hodler.moneny=(TextView) convertView.findViewById(R.id.moneny);
                hodler.name=(TextView) convertView.findViewById(R.id.name);
                convertView.setTag(hodler);
            }else{
                hodler=(ClassHodler) convertView.getTag();
            }

            if(Integer.valueOf(vipGson.getCity_item_list().get(position).getZab_do())==1){
                if(Integer.valueOf(vipGson.getCity_item_list().get(position).getZab_yusuan1())==Integer.valueOf(vipGson.getCity_item_list().get(position).getZab_yusuan2())){
                    hodler.moneny.setText(vipGson.getCity_item_list().get(position).getZab_yusuan1());
                }else if(Integer.valueOf(vipGson.getCity_item_list().get(position).getZab_yusuan1())>Integer.valueOf(vipGson.getCity_item_list().get(position).getZab_yusuan2())){
                    hodler.moneny.setText(vipGson.getCity_item_list().get(position).getZab_yusuan2()+"-"+vipGson.getCity_item_list().get(position).getZab_yusuan1());
                }else{
                    hodler.moneny.setText(vipGson.getCity_item_list().get(position).getZab_yusuan1()+"-"+vipGson.getCity_item_list().get(position).getZab_yusuan2());
                }
            }else{
                hodler.moneny.setText(vipGson.getCity_item_list().get(position).getMoney());
            }
            hodler.name.setText(vipGson.getCity_item_list().get(position).getItemname());
            return convertView;
        }

    };

    //这里是返回该界面的时候刷新数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        userMessage();
    }
    
}
