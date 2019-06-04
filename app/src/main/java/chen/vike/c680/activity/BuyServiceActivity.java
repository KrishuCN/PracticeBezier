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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.vike.c680.ALiPay.Alipay;
import chen.vike.c680.WXPay.GetPrepayIdTask;
import chen.vike.c680.bean.ItemAddToPay;
import chen.vike.c680.bean.ItemInfoGson;
import chen.vike.c680.bean.ItemPayGson;
import chen.vike.c680.bean.PayResultGson;
import chen.vike.c680.bean.ServiceDetailsGson;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.LoadingDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/16.
 * <p>
 * <p>
 * 购买服务
 */

public class BuyServiceActivity extends BaseStatusBarActivity {


    @BindView(R.id.fw_img)
    ImageView fwImg;
    @BindView(R.id.fw_title)
    TextView fwTitle;
    @BindView(R.id.fw_money)
    TextView fwMoney;
    @BindView(R.id.fw_shop)
    TextView fwShop;
    @BindView(R.id.fuwu)
    RelativeLayout fuwu;
    @BindView(R.id.xq_content)
    EditText xqContent;
    @BindView(R.id.textView29)
    TextView textView29;
    @BindView(R.id.textView30)
    TextView textView30;
    @BindView(R.id.fbxq_ys)
    EditText fbxqYs;
    @BindView(R.id.textView31)
    TextView textView31;
    @BindView(R.id.textView32)
    TextView textView32;
    @BindView(R.id.fbxq_phone)
    EditText fbxqPhone;
    @BindView(R.id.control)
    Button control;
    @BindView(R.id.imageView2)
    ImageView imageView2;
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
    private ImageView fw_img;
    private TextView fw_txt;
    private TextView fw_money;
    private TextView fw_shop;
    private EditText fbxq_ys;
    private EditText fbxq_phone;
    private Button mControl;
    private EditText mContent;
    private ServiceDetailsGson serviceDetailsGson;
    private ItemInfoGson itemInfoGson;
    private Bundle bundle;
    private Map<String, Object> map = new HashMap<>();
    private final int GET_INFO = 0x123;
    private final int USER_GUYONG = 0x133;
    private final int GET_ITEM_INFO = 0x122;
    private final int NETWORKEXCEPTION = 0X111;
    private LoadingDialog ld;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gy);
        ButterKnife.bind(this);

        getTitle().setText("购买服务");
        fw_img =  findViewById(R.id.fw_img);
        fw_txt =  findViewById(R.id.fw_title);
        fw_money =  findViewById(R.id.fw_money);
        fw_shop =  findViewById(R.id.fw_shop);
        fbxq_ys =  findViewById(R.id.fbxq_ys);
        mContent =  findViewById(R.id.xq_content);
        mControl =  findViewById(R.id.control);
        fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyServiceActivity.this, ServiceDetailsActivity.class);
                intent.putExtra("ID", String.valueOf(map.get("proid")));
                startActivityForResult(intent, 3);
            }
        });
        mControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mContent.getText().toString().isEmpty()) {
                    CustomToast.showToast(BuyServiceActivity.this, "请简要描述您的需求~", Toast.LENGTH_LONG);
                } else {
                    if (fbxq_ys.getText().toString().isEmpty()) {
                        CustomToast.showToast(BuyServiceActivity.this, "金额不能为空", Toast.LENGTH_LONG);
                    } else {
                        if (LhtTool.isLogin) {
                            map.put("userid", MyApplication.userInfo.getUserID());
                            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                            map.put("vikeid", serviceDetailsGson.getUserId());
                            map.put("fuwuid", serviceDetailsGson.getId());
                            map.put("crv_title", serviceDetailsGson.getTitle());
                            map.put("crv_content", mContent.getText());
                            map.put("money", fbxq_ys.getText());
                            map.put("data", 15);
                            if (fbxq_phone.getText().toString().isEmpty()) {
                                CustomToast.showToast(BuyServiceActivity.this, "电话号码不能为空", Toast.LENGTH_LONG);
                            } else {
                                map.put("mobile", fbxq_phone.getText().toString());
                                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.USER_GUYONG, map, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        String s = response.body().string();
                                        LogUtils.d("=================response:" + s);
                                        Message ms = new Message();
                                        ms.what = USER_GUYONG;
                                        Bundle b = new Bundle();
                                        b.putString("s", s);
                                        ms.setData(b);
                                        hd.sendMessage(ms);

                                    }
                                });
                                ld = new LoadingDialog(BuyServiceActivity.this).setMessage("加载中...");
                                ld.show();
                            }


                        } else {
                            Intent intent = new Intent(BuyServiceActivity.this, UserLoginActivity.class);
                            startActivity(intent);
                        }


                    }
                }

            }
        });
        bundle = getIntent().getExtras();
        fbxq_phone =  findViewById(R.id.fbxq_phone);
        //下面这个需要判断用户是否登录，没有登录则不能进行着两行代码
        if (LhtTool.isLogin) {
            if (MyApplication.userInfo.getIs_verify_phone().equals("1")) {
                fbxq_phone.setText(MyApplication.userInfo.getCellPhone());
            }
        } else {
            CustomToast.showToast(this, "请先登录", Toast.LENGTH_LONG);
            Intent intent = new Intent(this, UserLoginActivity.class);
            startActivityForResult(intent, 1);
        }

        map.put("apptype", "an");
        if (bundle != null) {
            if (bundle.containsKey("ID")) {
                String id = getIntent().getStringExtra("ID");
                map.put("proid", id);
                getFuwuDetails();
            }
            if (bundle.containsKey("WEB")) {
                String id = getIntent().getStringExtra("WEB");
                map.put("proid", id);
                getFuwuDetails();
            }

        }
        viewListener();
        jineNumber();

    }

    private ItemAddToPay itemAddToPay;
    private final int ITEMPAY = 0x113;
    private final int ADDTOPAY = 0x124;
    private boolean isAddToPay;
    private PayResultGson payResultGson;
    private final int ADDTOPAYDO = 0x133;
    private final int PAYRESULT = 0x114;
    private ItemPayGson itemPayGson;
    private String id;

    /**
     * 支付按钮监听
     */
    private void viewListener() {
        zhifubaoZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UrlConfig.alipay_flag.equals("1")) {
                    if (isAddToPay) {
                        ld = new LoadingDialog(BuyServiceActivity.this).setMessage("加载中.....");
                        ld.show();
                        Alipay.pay(BuyServiceActivity.this, LhtTool.getHander(BuyServiceActivity.this, itemAddToPay.getTotalfee(), ld), Alipay.getOrderInfo("支付" + id + "号项目金额", "支付" + id + "号项目订单" + itemAddToPay.getOrderno(), itemAddToPay.getTotalfee(), itemAddToPay.getOrderno()));
                    } else {
                        Alipay.pay(BuyServiceActivity.this, LhtTool.getHander(BuyServiceActivity.this, itemPayGson.getTotal(), ld), Alipay.getOrderInfo("支付" + itemPayGson.getItemId() + "号项目金额", "支付" + itemPayGson.getItemId() + "号项目订单" + itemPayGson.getOrderno(), itemPayGson.getTotal(), itemPayGson.getOrderno()));
                    }
                } else {
                    CustomToast.showToast(BuyServiceActivity.this, "该版本暂时不支持支付宝支付",
                            Toast.LENGTH_SHORT);
                }
            }
        });
        wxzfZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UrlConfig.weixinpay_flag.equals("1")) {
                    if (isAddToPay) {
                        GetPrepayIdTask get = new GetPrepayIdTask(BuyServiceActivity.this, itemAddToPay.getOrderno(), "订单编号：" + id + "付款", itemAddToPay.getTotalfee());
                        get.execute();
                    } else {
                        GetPrepayIdTask get = new GetPrepayIdTask(BuyServiceActivity.this, itemPayGson.getOrderno(), "订单编号：" + itemPayGson.getItemId() + "付款", itemPayGson.getTotal());
                        get.execute();
                    }
                } else {
                    CustomToast.showToast(BuyServiceActivity.this, "该版本暂时不支持微信支付",
                            Toast.LENGTH_SHORT);
                }
            }
        });
        yezfZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BuyServiceActivity.this);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(BuyServiceActivity.this).setTitle("提示信息").setMessage("余额不足，是否充值").setNegativeButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(BuyServiceActivity.this, RechargeActivity.class), 1);
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
                                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_TOPAY_ITEM, map, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        try {
                                            String s = response.body().string();
                                            LogUtils.d("===================response:" + s);
                                            payResultGson = new Gson().fromJson(s, PayResultGson.class);
                                            hd.sendEmptyMessage(PAYRESULT);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                ld = new LoadingDialog(BuyServiceActivity.this).setMessage("加载中.....");
                                ld.show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(BuyServiceActivity.this).setTitle("提示信息").setMessage("余额不足，是否充值").setNegativeButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(BuyServiceActivity.this, RechargeActivity.class), 1);
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

        if (null != id) {
            map.put("ItemId", id);
            if (LhtTool.isLogin) {
                map.put("UserId", MyApplication.userInfo.getUserID());
                map.put("vkuserid", MyApplication.userInfo.getUserID());
                map.put("vkuserip ", MyApplication.userInfo.getCookieLoginIpt());
                map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                map.put("type", "an");
                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_ITEM_PAY, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
                    }

                    @Override
                    public void onResponse(Call call, Response response) {
                        try {
                            String s = response.body().string();
                            //   Log.e("zhifu",s);
                            itemPayGson = new Gson().fromJson(s, ItemPayGson.class);
                            hd.sendEmptyMessage(GET_INFO);

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });
            }
        }
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
                            try {
                                String s = response.body().string();
                                LogUtils.d("=============response:" + s);
                                itemAddToPay = new Gson().fromJson(s, ItemAddToPay.class);
                                hd.sendEmptyMessage(ADDTOPAY);
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    });
                    ld = new LoadingDialog(BuyServiceActivity.this).setMessage("加载中.....");
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
                    try {
                        String s = response.body().string();
                        LogUtils.d("============response:" + s);
                        itemPayGson = new Gson().fromJson(s, ItemPayGson.class);
                        hd.sendEmptyMessage(ITEMPAY);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            ld = new LoadingDialog(BuyServiceActivity.this).setMessage("加载中.....");
            ld.show();

        }

    }

    /**
     * 获取服务详情
     */
    private void getFuwuDetails() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.FUWU_XIANGQING, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("=============response:" + s);
                    serviceDetailsGson = new Gson().fromJson(s, ServiceDetailsGson.class);
                    hd.sendEmptyMessage(GET_INFO);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (ld != null) {
                ld.dismiss();
            }
            if (msg.what == GET_INFO) {

                RequestOptions options = new RequestOptions();
                options.diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(BuyServiceActivity.this).load(serviceDetailsGson.getImgUrl()).apply(options).into(fw_img);

                fw_txt.setText(serviceDetailsGson.getTitle());

                if (Float.valueOf(serviceDetailsGson.getPrice()) > 0) {
                    fw_money.setText("￥" + serviceDetailsGson.getPrice());
                    fbxq_ys.setText(serviceDetailsGson.getPrice());
                } else {

                    fw_money.setText("议价");
                }

                fw_shop.setText("服务商：" + serviceDetailsGson.getShopName());
                mContent.setHint("如：我需要" + serviceDetailsGson.getTitle());
                if (bundle.containsKey("ITEMID")) {
                    map.put("itemId", bundle.getString("ITEMID"));
                    map.put("loginUserId", MyApplication.userInfo.getUserID());
                    OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.ITEM_INFO, map, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
                        }

                        @Override
                        public void onResponse(Call call, Response response) {
                            try {
                                String s = response.body().string();
                                LogUtils.d("================response:" + s);
                                itemInfoGson = new Gson().fromJson(s, ItemInfoGson.class);
                                hd.sendEmptyMessage(GET_ITEM_INFO);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(BuyServiceActivity.this, msg);

            } else if (msg.what == GET_ITEM_INFO) {

                mContent.setText(Html.fromHtml(itemInfoGson.getItem_con()));
                fbxq_ys.setText(itemInfoGson.getItem_money());

            } else if (msg.what == USER_GUYONG) {

                String s = msg.getData().getString("s");
                if (s.contains(",")) {
                    String[] strings = s.split(",");
                    strings[0] = "ok";
                    CustomToast.showToast(BuyServiceActivity.this, "雇佣成功", Toast.LENGTH_LONG);
                    Intent intent = new Intent(BuyServiceActivity.this, FuKuanAfterActivity.class);
                    intent.putExtra("ID", strings[1]);
                    startActivity(intent);
                    finish();
                } else {
                    switch (s) {
                        case "self":
                            CustomToast.showToast(BuyServiceActivity.this, "不能雇佣自己", Toast.LENGTH_LONG);
                            break;
                        case "fail":
                            CustomToast.showToast(BuyServiceActivity.this, "雇佣失败", Toast.LENGTH_LONG);
                            break;
                        case "nocon":
                            CustomToast.showToast(BuyServiceActivity.this, "项目内容不能为空", Toast.LENGTH_LONG);
                            break;
                        case "nomoney":
                            CustomToast.showToast(BuyServiceActivity.this, "雇佣金额无效", Toast.LENGTH_LONG);
                            break;
                        case "notitle":
                            CustomToast.showToast(BuyServiceActivity.this, "项目标题不能为空", Toast.LENGTH_LONG);
                            break;
                        case "unlogin":
                            CustomToast.showToast(BuyServiceActivity.this, "您还未登录", Toast.LENGTH_LONG);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    };

}
