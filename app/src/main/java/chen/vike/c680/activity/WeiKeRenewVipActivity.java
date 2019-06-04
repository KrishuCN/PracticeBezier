package chen.vike.c680.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import chen.vike.c680.ALiPay.Alipay;
import chen.vike.c680.bean.VipPayGson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.LoadingDialog;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/18.
 *
 * 续费VIP
 * 没时间在圆截面上改了，直接COPY来用，和购买VIP界面没区别的
 */

public class WeiKeRenewVipActivity extends BaseStatusBarActivity{

    private LinearLayout purchase_linear1,purchase_linear2,purchase_linear3;
    private LinearLayout purchase_zfb,purchase_ye;
    private TextView purchase_je,vip_yuee;
    private String moneny;
    private String order;//订单号
    private Map<String, Object> map = new HashMap<>();
    private String yuan,vipname;
    private int NUMBER=7;
    private VipPayGson vipPayGson;
    private TextView purchase_xy;
    private LoadingDialog ld;
    private final int NETWORK_EXCEPTION = 0x111;
    private final int ZFB_PAY_INFO = 0x222;
    private final int YU_E_PAY_INFO = 0x212;
    private Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(WeiKeRenewVipActivity.this, msg);

            } else if (msg.what == ZFB_PAY_INFO) {
                if(vipPayGson.getError().equals("moneyerr")){
                    CustomToast.showToast(WeiKeRenewVipActivity.this, "订单金额无效", Toast.LENGTH_LONG);
                }else if(vipPayGson.getError().equals("unlogin")){
                    CustomToast.showToast(WeiKeRenewVipActivity.this, "未登录", Toast.LENGTH_LONG);
                }else{
                    order=vipPayGson.getOrderno();
                    if(yuan.equals(vipPayGson.getPaymoney())){
                        //余额支付是我们本地不用生成订单，所以一步就行，支付宝因为要生成订单，所以要两部才能完成
                        Dilaogdata();
                    }else{
                        CustomToast.showToast(WeiKeRenewVipActivity.this, "金额不匹配", Toast.LENGTH_LONG);
                    }
                }
            } else if (msg.what == YU_E_PAY_INFO) {

                String s = msg.getData().getString("s");
                switch (s){
                    case "moneyerr":
                        CustomToast.showToast(WeiKeRenewVipActivity.this, "订单无效", Toast.LENGTH_LONG);
                        break;
                    case "unlogin":
                        CustomToast.showToast(WeiKeRenewVipActivity.this, "未登录", Toast.LENGTH_LONG);
                        break;
                    case "umoney_less":
                        CustomToast.showToast(WeiKeRenewVipActivity.this, "账号余额不足", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        CustomToast.showToast(WeiKeRenewVipActivity.this, "购买成功", Toast.LENGTH_LONG);
                        String a= MyApplication.userInfo.getBalance();
                        String b="";
                        String c=null;
                        if(a.indexOf(".")==-1){
                            b=a;
                        }else{
                            b=a.substring(0,a.indexOf("."));
                            c=a.substring(a.indexOf(".")+1,a.length());
                        }
                        int k=Integer.valueOf(b)-Integer.valueOf("1");
                        if(c==null){
                            MyApplication.userInfo.setBalance(k+"."+"00");
                        }else{
                            MyApplication.userInfo.setBalance(k+"."+c);
                        }
                        finish();
                        break;
                    case "fail":
                        CustomToast.showToast(WeiKeRenewVipActivity.this, "购买失败", Toast.LENGTH_LONG);
                        break;

                }
            }
            if (ld != null) {
                ld.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_vip_activity);

        getTitle().setText("续费VIP");
        initid();
        initlisen();
        String num=getIntent().getExtras().getString("num");
        if(Integer.valueOf(num)==4||Integer.valueOf(num)==5){
            yuan="6800";
            vipname="铂金";
            NUMBER=5;
            purchase_je.setText("6800元/年");
            purchase_linear1.setVisibility(View.GONE);
            purchase_linear2.setVisibility(View.GONE);
            purchase_linear3.setVisibility(View.GONE);
        }else if(Integer.valueOf(num)==6){
            yuan="13800";
            vipname="钻石";
            NUMBER=6;
            purchase_je.setText("13800元/年");
            purchase_linear1.setVisibility(View.GONE);
            purchase_linear2.setVisibility(View.GONE);
            purchase_linear3.setVisibility(View.VISIBLE);

        }else if(Integer.valueOf(num)==7){
            yuan="26800";
            vipname="皇冠";
            NUMBER=7;
            purchase_je.setText("26800元/年");
            purchase_linear1.setVisibility(View.GONE);
            purchase_linear2.setVisibility(View.VISIBLE);
            purchase_linear3.setVisibility(View.GONE);
        }else if(Integer.valueOf(num)==8){
            yuan="56800";
            vipname="金冠";
            NUMBER=8;
            purchase_je.setText("56800元/年");
            purchase_linear1.setVisibility(View.VISIBLE);
            purchase_linear2.setVisibility(View.GONE);
            purchase_linear3.setVisibility(View.GONE);
        }
        if(LhtTool.isLogin){
            vip_yuee.setText("("+ MyApplication.userInfo.getBalance()+")");
            if(MyApplication.userInfo.getBalance().indexOf(".")==-1){
                moneny= MyApplication.userInfo.getBalance();
            }else{
                moneny= MyApplication.userInfo.getBalance().substring(0, MyApplication.userInfo.getBalance().indexOf("."));
            }

            if(Integer.valueOf(moneny)<Integer.valueOf(yuan)){
                purchase_ye.setClickable(false);
                purchase_ye.setBackgroundColor(getResources().getColor(R.color.puichaser_l));
            }else{
                purchase_ye.setClickable(true);
                purchase_ye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        map.put("userid", MyApplication.userInfo.getUserID());
                        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                        map.put("buy_viptype", NUMBER);
                        map.put("buy_vipyear", "1");
                        map.put("type", "an");
                        ld = new LoadingDialog(WeiKeRenewVipActivity.this).setMessage("加载中...");
                        ld.show();
                        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.BUY_VIP_YU_E, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                String s = response.body().string();
                                LogUtils.d("===============response:" + s);
                                Message ms = new Message();
                                ms.what = YU_E_PAY_INFO;
                                Bundle b = new Bundle();
                                b.putString("s", s);
                                ms.setData(b);
                                hd.sendMessage(ms);

                            }
                        });
                    }
                });
                purchase_ye.setBackgroundColor(getResources().getColor(R.color.white));
            }
        }

    }

    private void initid(){
        purchase_linear1=(LinearLayout) findViewById(R.id.purchase_linear1);
        purchase_linear2=(LinearLayout) findViewById(R.id.purchase_linear2);
        purchase_linear3=(LinearLayout) findViewById(R.id.purchase_linear3);
        purchase_zfb=(LinearLayout) findViewById(R.id.purchase_zfb);
        purchase_ye=(LinearLayout) findViewById(R.id.purchase_ye);
        purchase_je=(TextView) findViewById(R.id.purchase_je);
        vip_yuee=(TextView) findViewById(R.id.vip_yuee);
        purchase_xy=(TextView) findViewById(R.id.purchase_xy);
    }

    private void initlisen(){
        purchase_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.put("userid", MyApplication.userInfo.getUserID());
                map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                map.put("buy_viptype", NUMBER);
                map.put("buy_vipyear", "1");
                map.put("type", "an");
                ld = new LoadingDialog(WeiKeRenewVipActivity.this).setMessage("加载中....");
                ld.show();
                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.BUY_VIP_ZFB, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            String s = response.body().string();
                            LogUtils.d("==============response:" + s);
                            vipPayGson = new Gson().fromJson(s, VipPayGson.class);
                            hd.sendEmptyMessage(ZFB_PAY_INFO);
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }
                });
            }
        });
        purchase_xy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(WeiKeRenewVipActivity.this, WeiKeServiceProvideractivity.class);
                startActivity(in);
            }
        });
    }

    public void Dilaogdata(){
        AlertDialog.Builder Builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.vipzfbdialog,null);
        TextView zfb_mc=(TextView) view.findViewById(R.id.zfb_mc);
        TextView zfb_je=(TextView) view.findViewById(R.id.zfb_je);
        TextView zfb_danh=(TextView) view.findViewById(R.id.zfb_danh);
        TextView zfb_sj=(TextView) view.findViewById(R.id.zfb_sj);
        zfb_je.setText("￥"+yuan+"元");
        zfb_mc.setText("购买时间财富"+vipname+"VIP");
        zfb_danh.setText(order);
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date =sDateFormat.format(new java.util.Date());
        zfb_sj.setText(date);
        Builder.setTitle("确定购买信息")
                .setView(view)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(UrlConfig.alipay_flag.equals("1")){
                            ld = new LoadingDialog(WeiKeRenewVipActivity.this).setMessage("加载中....");
                            ld.show();
                            Alipay.pay(WeiKeRenewVipActivity.this, LhtTool.getHander(WeiKeRenewVipActivity.this, yuan,ld), Alipay.getOrderInfo("时间财富会员【" + MyApplication.userInfo.getNickame() + "】充值", "时间财富充值订单" + order, yuan, order));
                        }else{
                            CustomToast.showToast(WeiKeRenewVipActivity.this, "该版本暂时不支持支付宝支付",
                                    Toast.LENGTH_SHORT);
                        }
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        Builder.create();

    }
}
