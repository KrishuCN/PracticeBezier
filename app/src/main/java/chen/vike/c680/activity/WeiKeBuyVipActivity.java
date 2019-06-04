package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import chen.vike.c680.WXPay.GetPrepayIdTask;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/18.
 * <p>
 * 购买VIP
 */

public class WeiKeBuyVipActivity extends BaseStatusBarActivity {

    @BindView(R.id.jinguan_biaozhi)
    ImageView jinguanBiaozhi;
    @BindView(R.id.purchase_text1)
    TextView purchaseText1;
    @BindView(R.id.purchase_linear1)
    LinearLayout purchaseLinear1;
    @BindView(R.id.huanggaun_biaozhi)
    ImageView huanggaunBiaozhi;
    @BindView(R.id.purchase_text2)
    TextView purchaseText2;
    @BindView(R.id.purchase_linear2)
    LinearLayout purchaseLinear2;
    @BindView(R.id.zhuanshi_biaozhi)
    ImageView zhuanshiBiaozhi;
    @BindView(R.id.purchase_text3)
    TextView purchaseText3;
    @BindView(R.id.purchase_linear3)
    LinearLayout purchaseLinear3;
    @BindView(R.id.purchase_je)
    TextView purchaseJe;
    @BindView(R.id.purchase_xy)
    TextView purchaseXy;
    @BindView(R.id.purchase_zfb)
    LinearLayout purchaseZfb;
    @BindView(R.id.vip_yuee)
    TextView vipYuee;
    @BindView(R.id.purchase_ye)
    LinearLayout purchaseYe;
    @BindView(R.id.purchase_wx)
    LinearLayout purchaseWx;
    private LinearLayout purchase_zfb, purchase_ye;
    private TextView purchase_je, vip_yuee;
    private String moneny;
    private String order;//订单号
    private Map<String, Object> map = new HashMap<>();
    private String yuan, vipname;
    private int NUMBER;
    private VipPayGson vipPayGson;
    private TextView purchase_xy;//服务协议
    private LoadingDialog ld;
    private final int NETWORK_EXCEPTION = 0x111;
    private final int ZFB_PAY_INFO = 0x222;
    private final int YU_E_PAY_INFO = 0x212;
    private final int WX_PAY_INFO = 0x223;
    private boolean iszfb = false;
    private boolean iswx = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_vip_activity);
        ButterKnife.bind(this);

        getTitle().setText("购买VIP");
        yuan = "26800";
        vipname = "皇冠";
        NUMBER = 7;
        initid();
        initlisen();
    }

    private void initid() {
        purchase_zfb = findViewById(R.id.purchase_zfb);
        purchase_ye = findViewById(R.id.purchase_ye);
        purchase_je = findViewById(R.id.purchase_je);
        vip_yuee = findViewById(R.id.vip_yuee);
        purchase_xy = findViewById(R.id.purchase_xy);
        if (LhtTool.isLogin) {
            vip_yuee.setText("(" + MyApplication.userInfo.getBalance() + ")");
            if (MyApplication.userInfo.getBalance().indexOf(".") == -1) {
                moneny = MyApplication.userInfo.getBalance();
            } else {
                moneny = MyApplication.userInfo.getBalance().substring(0, MyApplication.userInfo.getBalance().indexOf("."));
            }
            if (Integer.valueOf(moneny) < 26800) {
                purchase_ye.setClickable(false);
                purchase_ye.setBackgroundColor(getResources().getColor(R.color.puichaser_l));
            } else {
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
                        ld = new LoadingDialog(WeiKeBuyVipActivity.this).setMessage("加载中...");
                        ld.show();
                        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.BUY_VIP_YU_E, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
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

    private void initlisen() {
        purchaseLinear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase_je.setText("56800元/年");
                yuan = "56800";
                vipname = "金冠";
                NUMBER = 8;
                if (Integer.valueOf(moneny) < 56800) {
                    purchase_ye.setClickable(false);
                    purchase_ye.setBackgroundColor(getResources().getColor(R.color.puichaser_l));
                } else {
                    purchase_ye.setClickable(true);
                    purchase_ye.setOnClickListener(this);
                    purchase_ye.setBackgroundColor(getResources().getColor(R.color.white));
                }
                purchaseLinear1.setBackgroundColor(getResources().getColor(R.color.puichaser_l));
                purchaseLinear2.setBackgroundColor(getResources().getColor(R.color.white));
                purchaseLinear3.setBackgroundColor(getResources().getColor(R.color.white));
                jinguanBiaozhi.setVisibility(View.VISIBLE);
                huanggaunBiaozhi.setVisibility(View.GONE);
                zhuanshiBiaozhi.setVisibility(View.GONE);
                purchaseText1.setTextColor(getResources().getColor(R.color.vip_red));
                purchaseText2.setTextColor(getResources().getColor(R.color.vip_back));
                purchaseText3.setTextColor(getResources().getColor(R.color.vip_back));
            }
        });
        purchaseLinear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase_je.setText("26800元/年");
                yuan = "26800";
                vipname = "皇冠";
                NUMBER = 7;
                if (Integer.valueOf(moneny) < 26800) {
                    purchase_ye.setClickable(false);
                    purchase_ye.setBackgroundColor(getResources().getColor(R.color.puichaser_l));
                } else {
                    purchase_ye.setClickable(true);
                    purchase_ye.setOnClickListener(this);
                    purchase_ye.setBackgroundColor(getResources().getColor(R.color.white));
                }
                purchaseLinear1.setBackgroundColor(getResources().getColor(R.color.white));
                purchaseLinear2.setBackgroundColor(getResources().getColor(R.color.puichaser_l));
                purchaseLinear3.setBackgroundColor(getResources().getColor(R.color.white));
                jinguanBiaozhi.setVisibility(View.GONE);
                huanggaunBiaozhi.setVisibility(View.VISIBLE);
                zhuanshiBiaozhi.setVisibility(View.GONE);
                purchaseText1.setTextColor(getResources().getColor(R.color.vip_back));
                purchaseText2.setTextColor(getResources().getColor(R.color.vip_red));
                purchaseText3.setTextColor(getResources().getColor(R.color.vip_back));
            }
        });
        purchaseLinear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchase_je.setText("13800元/年");
                yuan = "13800";
                vipname = "钻石";
                NUMBER = 6;
                if (Integer.valueOf(moneny) < 13800) {
                    purchase_ye.setClickable(false);
                    purchase_ye.setBackgroundColor(getResources().getColor(R.color.puichaser_l));
                } else {
                    purchase_ye.setClickable(true);
                    purchase_ye.setOnClickListener(this);
                    purchase_ye.setBackgroundColor(getResources().getColor(R.color.white));
                }
                purchaseLinear1.setBackgroundColor(getResources().getColor(R.color.white));
                purchaseLinear2.setBackgroundColor(getResources().getColor(R.color.white));
                purchaseLinear3.setBackgroundColor(getResources().getColor(R.color.puichaser_l));
                jinguanBiaozhi.setVisibility(View.GONE);
                huanggaunBiaozhi.setVisibility(View.GONE);
                zhuanshiBiaozhi.setVisibility(View.VISIBLE);
                purchaseText1.setTextColor(getResources().getColor(R.color.vip_back));
                purchaseText2.setTextColor(getResources().getColor(R.color.vip_back));
                purchaseText3.setTextColor(getResources().getColor(R.color.vip_red));
            }
        });

        purchase_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iszfb = true;
                iswx = false;
                map.put("userid", MyApplication.userInfo.getUserID());
                map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                map.put("buy_viptype", NUMBER);
                map.put("buy_vipyear", "1");
                map.put("type", "an");
                ld = new LoadingDialog(WeiKeBuyVipActivity.this).setMessage("加载中...");
                ld.show();
                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.BUY_VIP_ZFB, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
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


        purchaseWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iswx = true;
                iszfb = false;
                map.put("userid", MyApplication.userInfo.getUserID());
                map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                map.put("buy_viptype", NUMBER);
                map.put("buy_vipyear", "1");
                map.put("type", "an");
                ld = new LoadingDialog(WeiKeBuyVipActivity.this).setMessage("加载中...");
                ld.show();
                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.BUY_VIP_ZFB, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            String s = response.body().string();
                            LogUtils.d("==============response:" + s);
                            vipPayGson = new Gson().fromJson(s, VipPayGson.class);
                            hd.sendEmptyMessage(WX_PAY_INFO);
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
                //这后面增加协议展示的跳转
                Intent in = new Intent(WeiKeBuyVipActivity.this, WeiKeServiceProvideractivity.class);
                startActivity(in);
            }
        });
    }


    public void Dilaogdata() {
        AlertDialog.Builder Builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.vipzfbdialog, null);
        TextView zfb_mc = (TextView) view.findViewById(R.id.zfb_mc);
        TextView zfb_je = (TextView) view.findViewById(R.id.zfb_je);
        TextView zfb_danh = (TextView) view.findViewById(R.id.zfb_danh);
        TextView zfb_sj = (TextView) view.findViewById(R.id.zfb_sj);
        zfb_je.setText("￥" + yuan + "元");
        zfb_mc.setText("购买时间财富" + vipname + "VIP");
        zfb_danh.setText(order);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new Date());
        zfb_sj.setText(date);
        Builder.setTitle("确定购买信息")
                .setView(view)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (iszfb) {
                            if (UrlConfig.alipay_flag.equals("1")) {
                                ld = new LoadingDialog(WeiKeBuyVipActivity.this).setMessage("加载中....");
                                ld.show();
                                Alipay.pay(WeiKeBuyVipActivity.this, LhtTool.getHander(WeiKeBuyVipActivity.this, yuan, ld), Alipay.getOrderInfo("时间财富会员【" + MyApplication.userInfo.getNickame() + "】充值", "时间财富充值订单" + order, yuan, order));
                            } else {
                                CustomToast.showToast(WeiKeBuyVipActivity.this, "该版本暂时不支持支付宝支付",
                                        Toast.LENGTH_SHORT);
                            }
                        }else if (iswx){
                            if ("1".equals(UrlConfig.weixinpay_flag)) {
                                GetPrepayIdTask get = new GetPrepayIdTask(WeiKeBuyVipActivity.this, order,
                                        "时间财富会员【" + MyApplication.userInfo.getNickame() + "】充值", yuan);
                                get.execute();
                            } else {
                                CustomToast.showToast(WeiKeBuyVipActivity.this, "该版本暂时不支持微信支付",
                                        Toast.LENGTH_SHORT);
                            }
                        }
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        Builder.create().show();
    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(WeiKeBuyVipActivity.this, msg);

            } else if (msg.what == ZFB_PAY_INFO) {
                if (vipPayGson.getError().equals("moneyerr")) {
                    CustomToast.showToast(WeiKeBuyVipActivity.this, "订单金额无效", Toast.LENGTH_LONG);
                } else if (vipPayGson.getError().equals("unlogin")) {
                    CustomToast.showToast(WeiKeBuyVipActivity.this, "未登录", Toast.LENGTH_LONG);
                } else {
                    order = vipPayGson.getOrderno();
                    if (yuan.equals(vipPayGson.getPaymoney())) {
                        //余额支付是我们本地不用生成订单，所以一步就行，支付宝因为要生成订单，所以要两部才能完成
                        Dilaogdata();
                    } else {
                        CustomToast.showToast(WeiKeBuyVipActivity.this, "金额不匹配", Toast.LENGTH_LONG);
                    }
                }
            } else if (msg.what == WX_PAY_INFO){
                if (vipPayGson.getError().equals("moneyerr")) {
                    CustomToast.showToast(WeiKeBuyVipActivity.this, "订单金额无效", Toast.LENGTH_LONG);
                } else if (vipPayGson.getError().equals("unlogin")) {
                    CustomToast.showToast(WeiKeBuyVipActivity.this, "未登录", Toast.LENGTH_LONG);
                } else {
                    order = vipPayGson.getOrderno();
                    if (yuan.equals(vipPayGson.getPaymoney())) {
                        //余额支付是我们本地不用生成订单，所以一步就行，支付宝因为要生成订单，所以要两部才能完成
                        Dilaogdata();
                    } else {
                        CustomToast.showToast(WeiKeBuyVipActivity.this, "金额不匹配", Toast.LENGTH_LONG);
                    }
                }
            }
            else if (msg.what == YU_E_PAY_INFO) {

                String s = msg.getData().getString("s");
                switch (s) {
                    case "moneyerr":
                        CustomToast.showToast(WeiKeBuyVipActivity.this, "订单无效", Toast.LENGTH_LONG);
                        break;
                    case "unlogin":
                        CustomToast.showToast(WeiKeBuyVipActivity.this, "未登录", Toast.LENGTH_LONG);
                        break;
                    case "umoney_less":
                        CustomToast.showToast(WeiKeBuyVipActivity.this, "账号余额不足", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        CustomToast.showToast(WeiKeBuyVipActivity.this, "购买成功", Toast.LENGTH_LONG);
                        String a = MyApplication.userInfo.getBalance();
                        String b = "";
                        String c = null;
                        if (a.indexOf(".") == -1) {
                            b = a;
                        } else {
                            b = a.substring(0, a.indexOf("."));
                            c = a.substring(a.indexOf(".") + 1, a.length());
                        }
                        int k = Integer.valueOf(b) - Integer.valueOf("1");
                        if (c == null) {
                            MyApplication.userInfo.setBalance(k + "." + "00");
                        } else {
                            MyApplication.userInfo.setBalance(k + "." + c);
                        }
                        setResult(2);
                        finish();
                        break;
                    case "fail":
                        CustomToast.showToast(WeiKeBuyVipActivity.this, "购买失败", Toast.LENGTH_LONG);
                        break;

                }
            }
            if (ld != null) {
                ld.dismiss();
            }
        }
    };

}
