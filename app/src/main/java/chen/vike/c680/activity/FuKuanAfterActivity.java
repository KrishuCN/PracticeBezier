package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import chen.vike.c680.ALiPay.Alipay;
import chen.vike.c680.bean.ItemAddToPay;
import chen.vike.c680.bean.ItemPayGson;
import chen.vike.c680.bean.PayResultGson;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/16.
 * 付款成功后的页面
 */

public class FuKuanAfterActivity extends BaseStatusBarActivity {


    @BindView(R.id.xmje)
    TextView xmje;
    @BindView(R.id.xmbh)
    TextView xmbh;
    @BindView(R.id.zfdj)
    Button zfdj;
    @BindView(R.id.ckxq)
    Button ckxq;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.textView32)
    TextView textView32;
    @BindView(R.id.zhifubao_zhifu)
    RelativeLayout zhifubaoZhifu;
    @BindView(R.id.WeCha_logo)
    ImageView WeChaLogo;
    @BindView(R.id.WeCha_text)
    TextView WeChaText;
    @BindView(R.id.wxzf_zhifu)
    RelativeLayout wxzfZhifu;
    @BindView(R.id.sjcf_logo)
    ImageView sjcfLogo;
    @BindView(R.id.fk_ye_num_show)
    TextView fkYeNumShow;
    @BindView(R.id.yezf_zhifu)
    RelativeLayout yezfZhifu;
    @BindView(R.id.zhushi)
    LinearLayout zhushi;

    private String id;
    private ItemPayGson itemPayGson;
    private Map<String, Object> map = new HashMap<>();
    private final int GET_INFO = 0x123;
    private final int NETWORKEXCEPTION = 0X111;

    private LoadingDialog ld;
    private ItemAddToPay itemAddToPay;
    private final int ITEMPAY = 0x113;
    private final int ADDTOPAY = 0x124;
    private boolean isAddToPay;
    private PayResultGson payResultGson;
    private final int ADDTOPAYDO = 0x133;
    private final int PAYRESULT = 556;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fukuan_activity);
        ButterKnife.bind(this);

        getTitle().setText("托管赏金");

        id = getIntent().getStringExtra("ID");
        if (null != id) {
            map.put("ItemId", id);
            if (LhtTool.isLogin) {
                map.put("UserId", MyApplication.userInfo.getUserID());
                map.put("vkuserid", MyApplication.userInfo.getUserID());
                map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            }
            map.put("type", "an");
            OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_ITEM_PAY, map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        String s = response.body().string();
                        Log.e("zhifu", s);
                        itemPayGson = new Gson().fromJson(s, ItemPayGson.class);
                        hd.sendEmptyMessage(GET_INFO);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }

        zfdj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id.equals("0")) {
                    CustomToast.showToast(FuKuanAfterActivity.this, "预约不支持此操作",
                            Toast.LENGTH_SHORT);
                } else {
                    if (null != itemPayGson) {
                        if (itemPayGson.getItemtype().equals("1")) {
                            //此处为招标,付定金会跳转到过度界面
                            Intent intent = new Intent(FuKuanAfterActivity.this, PayDepositActivity.class);
                            intent.putExtra("ID", id);
                            startActivityForResult(intent, 1);
                        } else {
                            Intent intent = new Intent(FuKuanAfterActivity.this, FuKuanActivity.class);
                            intent.putExtra("ID", id);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        ckxq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.equals("0")) {
                    CustomToast.showToast(FuKuanAfterActivity.this, "预约不支持此操作",
                            Toast.LENGTH_SHORT);
                } else {
                    Intent intent = new Intent(FuKuanAfterActivity.this, OrderDetailsActivity.class);
                    intent.putExtra("ID", id);
                    startActivityForResult(intent, 1);
                }

            }
        });
        jineNumber();
        viewListener();
    }


    private void viewListener() {
        zhifubaoZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UrlConfig.alipay_flag.equals("1")) {
                    if (isAddToPay) {
                        ld = new LoadingDialog(FuKuanAfterActivity.this).setMessage("加载中.....");
                        ld.show();
                        Alipay.pay(FuKuanAfterActivity.this, LhtTool.getHander(FuKuanAfterActivity.this, itemAddToPay.getTotalfee(), ld), Alipay.getOrderInfo("支付" + id + "号项目金额", "支付" + id + "号项目订单" + itemAddToPay.getOrderno(), itemAddToPay.getTotalfee(), itemAddToPay.getOrderno()));
                    } else {
                        Alipay.pay(FuKuanAfterActivity.this, LhtTool.getHander(FuKuanAfterActivity.this, itemPayGson.getTotal(), ld), Alipay.getOrderInfo("支付" + itemPayGson.getItemId() + "号项目金额", "支付" + itemPayGson.getItemId() + "号项目订单" + itemPayGson.getOrderno(), itemPayGson.getTotal(), itemPayGson.getOrderno()));
                    }
                } else {
                    CustomToast.showToast(FuKuanAfterActivity.this, "该版本暂时不支持支付宝支付",
                            Toast.LENGTH_SHORT);
                }
            }
        });
        wxzfZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UrlConfig.weixinpay_flag.equals("1")) {
                    if (isAddToPay) {
                        GetPrepayIdTask get = new GetPrepayIdTask(FuKuanAfterActivity.this, itemAddToPay.getOrderno(), "订单编号：" + id + "付款", itemAddToPay.getTotalfee());
                        get.execute();
                    } else {
                        GetPrepayIdTask get = new GetPrepayIdTask(FuKuanAfterActivity.this, itemPayGson.getOrderno(), "订单编号：" + itemPayGson.getItemId() + "付款", itemPayGson.getTotal());
                        get.execute();
                    }
                } else {
                    CustomToast.showToast(FuKuanAfterActivity.this, "该版本暂时不支持微信支付",
                            Toast.LENGTH_SHORT);
                }
            }
        });
        yezfZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FuKuanAfterActivity.this);
                builder.setTitle("温馨提示").setMessage("您确定使用余额支付？").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isAddToPay) {
                            if (Float.valueOf(MyApplication.userInfo.getBalance()) >= Float.valueOf(itemAddToPay.getTotalfee())) {
                                map.put("orderno", itemAddToPay.getOrderno());
                                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_ITEM_ADD_DO, map, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        String s = response.body().string();
                                        LogUtils.d("===============response:" + s);
                                        Message ms = new Message();
                                        ms.what = ADDTOPAYDO;
                                        Bundle b = new Bundle();
                                        b.putString("s", s);
                                        ms.setData(b);
                                        hd.sendMessage(ms);

                                    }
                                });

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FuKuanAfterActivity.this).setTitle("提示信息").setMessage("余额不足，是否充值").setNegativeButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(FuKuanAfterActivity.this, RechargeActivity.class), 1);
                                    }
                                }).setNeutralButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog1 = builder.create();
//                                dialog1.setInverseBackgroundForced(false);
                                dialog1.show();
                            }
                        } else {
                            if (Float.valueOf(itemPayGson.getBalance()) >= Float.valueOf(itemPayGson.getTotal())) {
                                map.put("orderno", itemPayGson.getOrderno());
                                // map.put("debug","1");
                                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_TOPAY_ITEM, map, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        try{
                                            String s = response.body().string();
                                            LogUtils.d("===================response:" + s);
                                            payResultGson = new Gson().fromJson(s, PayResultGson.class);
                                            hd.sendEmptyMessage(PAYRESULT);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                ld = new LoadingDialog(FuKuanAfterActivity.this).setMessage("加载中.....");
                                ld.show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FuKuanAfterActivity.this).setTitle("提示信息").setMessage("余额不足，是否充值").setNegativeButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(FuKuanAfterActivity.this, RechargeActivity.class), 1);
                                    }
                                }).setNeutralButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog1 = builder.create();
//                                dialog1.setInverseBackgroundForced(false);
                                dialog1.show();
                            }
                        }


                    }
                }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
//                dialog.setInverseBackgroundForced(false);
                dialog.show();
            }

        });
    }


    private void jineNumber() {
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("ZJXS")) {
            getTitle().setText("增加悬赏");
            /**
             * * userid 用户ID
             * vkuserip 登录cookieIPT
             * vktoken 登录加密串
             * projectId 项目ID
             * addmoney 加价金额
             * addtime 增加天数
             * type 是安卓app还是IOS版app支付 ，=an 表示安卓app支付
             */
            List list = bundle.getStringArrayList("ZJXS");
            if (list != null) {
                id = (String) list.get(0);
                map.put("projectId", list.get(0));
                if (LhtTool.isLogin) {
                    map.put("userid", MyApplication.userInfo.getUserID());
                    map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                    map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                    map.put("type", "an");
                    map.put("addmoney", list.get(1));
                    if (list.size() == 3) {
                        map.put("addtime", list.get(2));
                    }
                    fkYeNumShow.setText(Html.fromHtml("余额  <font color='#cccccc'>（可用余额：￥" + MyApplication.userInfo.getBalance() + "）</font>"));
                    OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_ITEM_ADD_TO_PAY, map, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                            LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);

                        }

                        @Override
                        public void onResponse(Call call, Response response) {
                            try{
                                String s = response.body().string();
                                LogUtils.d("=============response:" + s);
                                itemAddToPay = new Gson().fromJson(s, ItemAddToPay.class);
                                hd.sendEmptyMessage(ADDTOPAY);

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    });
                    ld = new LoadingDialog(FuKuanAfterActivity.this).setMessage("加载中.....");
                    ld.show();


                }


            }
        } else if (bundle.containsKey("ZB")) {
            map.put("ItemId", bundle.getString("ID"));
            if (LhtTool.isLogin) {
                map.put("UserId", MyApplication.userInfo.getUserID());
                map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            }
            map.put("type", "an");
            map.put("fenqi_id", bundle.getString("FENQI"));
            LogUtils.d("=====================ItemId:" + bundle.getString("ID"));
            LogUtils.d("=====================UserId:" + MyApplication.userInfo.getUserID());
            LogUtils.d("=====================fenqi_id:" + bundle.getInt("FENQI"));
            OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_ITEMPAY, map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        String s = response.body().string();
                        Log.e("fukuan", s);
                        itemPayGson = new Gson().fromJson(s, ItemPayGson.class);
                        hd.sendEmptyMessage(ITEMPAY);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            ld = new LoadingDialog(FuKuanAfterActivity.this).setMessage("加载中.....");
            ld.show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        finish();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == GET_INFO) {

                if (itemPayGson.getItemtype().equals("1")) {
                    zfdj.setText("支付定金");
                    xmje.setText("项目金额：￥" + itemPayGson.getShow_item_money());
//                    zhushi.setVisibility(View.VISIBLE);
                } else {
                    zfdj.setText("托管赏金");
                    xmje.setText("项目金额：￥" + itemPayGson.getTotal());
//                    zhushi.setVisibility(View.GONE);
                }
                xmbh.setText("项目编号：" + itemPayGson.getItemId());
                fkYeNumShow.setText(Html.fromHtml("余额  <font color='#cccccc'>（可用余额：￥" + itemPayGson.getBalance() + "）</font>"));

            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(FuKuanAfterActivity.this, msg);

            } else if (msg.what == ADDTOPAY) {
                isAddToPay = true;

            } else if (msg.what == ITEMPAY) {

                fkYeNumShow.setText(Html.fromHtml("余额  <font color='#cccccc'>（可用余额：￥" + itemPayGson.getBalance() + "）</font>"));

            } else if (msg.what == ADDTOPAYDO) {
                if (ld != null) {
                    ld.dismiss();
                }
                isAddToPay = true;
                String s = msg.getData().getString("s");
                switch (s) {
                    case "nopay":
                        CustomToast.showToast(FuKuanAfterActivity.this, " 无效支付请求", Toast.LENGTH_LONG);
                        break;
                    case "moneyless":
                        CustomToast.showToast(FuKuanAfterActivity.this, "余额不足", Toast.LENGTH_LONG);
                        break;
                    case "fail":
                        CustomToast.showToast(FuKuanAfterActivity.this, "支付失败", Toast.LENGTH_LONG);
                        break;
                    case "orderno_err":
                        CustomToast.showToast(FuKuanAfterActivity.this, "无效订单编号", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        CustomToast.showToast(FuKuanAfterActivity.this, "余额支付增加悬赏成功", Toast.LENGTH_LONG);
                        finish();
                        break;
                    case "unlogin":
                        CustomToast.showToast(FuKuanAfterActivity.this, "未登录", Toast.LENGTH_LONG);
                        break;
                    default:
                        CustomToast.showToast(FuKuanAfterActivity.this, "支付失败", Toast.LENGTH_LONG);

                        break;
                }
            } else if (msg.what == PAYRESULT) {
                if (ld != null) {
                    ld.dismiss();
                }
                switch (payResultGson.getPayResult()) {
                    case "has":
                        CustomToast.showToast(FuKuanAfterActivity.this, "该项目已经支付过了", Toast.LENGTH_LONG);
                        break;
                    case "moneyless":
                        CustomToast.showToast(FuKuanAfterActivity.this, "余额不足", Toast.LENGTH_LONG);
                        break;
                    case "fail":
                        CustomToast.showToast(FuKuanAfterActivity.this, "支付失败", Toast.LENGTH_LONG);
                        break;
                    case "nomoney":
                        CustomToast.showToast(FuKuanAfterActivity.this, "项目金额为0，不能支付", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        AlertDialog.Builder builder = new AlertDialog.Builder(FuKuanAfterActivity.this).setTitle("付款成功").setMessage("已支付成功，待审核通过后将发布； 您随时可以登录您的用户名查看威客们提交的方案，并采纳满意的方案，如需与威客沟通，请直接给他们发站内消息或者向时间财富客服索取相关威客的联系方式，如有不明白的地方请致电：4006306800 \n" +
                                "QQ：4006306800、1416446001").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MyApplication.userInfo.setBalance(String.valueOf(Float.valueOf(itemPayGson.getBalance()) - Float.valueOf(itemPayGson.getTotal())));
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        break;
                    default:
                        CustomToast.showToast(FuKuanAfterActivity.this, payResultGson.getPayResult(), Toast.LENGTH_LONG);
                        break;
                }
            }

        }
    };

}
