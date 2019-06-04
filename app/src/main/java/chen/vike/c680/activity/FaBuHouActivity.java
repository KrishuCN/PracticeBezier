package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2018/9/27.
 * 托管赏金
 */
public class FaBuHouActivity extends BaseStatusBarActivity {
    @BindView(R.id.xiangmu_jin_e)
    TextView xiangmuJinE;
    @BindView(R.id.xiangmu_bian_hao)
    TextView xiangmuBianHao;
    @BindView(R.id.zhifu_dingjin)
    Button zhifuDingjin;
    @BindView(R.id.chakan_xiangqing)
    Button chakanXiangqing;
    @BindView(R.id.jiaji_jian)
    ImageView jiajiJian;
    @BindView(R.id.jiaji_jine)
    Button jiajiJine;
    @BindView(R.id.jiaji_zengjia)
    ImageView jiajiZengjia;
    @BindView(R.id.zhiding_jian)
    ImageView zhidingJian;
    @BindView(R.id.zhiding_jine)
    Button zhidingJine;
    @BindView(R.id.zhiding_zengjia)
    ImageView zhidingZengjia;
    @BindView(R.id.tisheng_jian)
    ImageView tishengJian;
    @BindView(R.id.tisheng_jine)
    Button tishengJine;
    @BindView(R.id.tisheng_zengjia)
    ImageView tishengZengjia;
    @BindView(R.id.baidu_jian)
    ImageView baiduJian;
    @BindView(R.id.baidu_jine)
    Button baiduJine;
    @BindView(R.id.baidu_zengjia)
    ImageView baiduZengjia;
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
    @BindView(R.id.wait_zhifu_data)
    LinearLayout waitZhifuData;
    @BindView(R.id.fabu_xiangmu_jianjie_content)
    TextView fabuXiangmuJianjieContent;
    @BindView(R.id.zengzhi_fuwu_fei)
    TextView zengzhiFuwuFei;
    private int chushiNum = 0;
    private String jiaNumStr, zhiNumStr, tiNumStr, baiduNumStr;
    private int jiaNum, zhiNum, tiNum, baiduNum;
    private int zengZhi = 0;
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
    private boolean isZhiFuB, isWeiX, isYuE;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangmu_tijiao_hou_fu_kuan);
        context = this;
        ButterKnife.bind(this);
        getTitle().setText("提交成功");
        id = getIntent().getStringExtra("ID");
        setView();
        dataHttp();
    }

    /**
     * 修改view
     */
    private void setView() {
        jiajiJine.setText(chushiNum + "");
        zhidingJine.setText(chushiNum + "");
        tishengJine.setText(chushiNum + "");
        jiaJiListener();
        jineNumber();
    }

    /**
     * 获取待支付项目详情
     */
    private void dataHttp() {
        if (null != id) {
            map.put("ItemId", id);
            if (LhtTool.isLogin) {
                map.put("UserId", MyApplication.userInfo.getUserID());
                map.put("vkuserid", MyApplication.userInfo.getUserID());
                map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            }
            map.put("type", "an");
            map.put("jiajika_num", jiaNum);
            map.put("zhidingka_num", zhiNum);
            map.put("tishengka_num", tiNum);
            map.put("is_pingbika", baiduNum);
            OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_ITEM_PAY, map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
                }

                @Override
                public void onResponse(Call call, Response response){
                    try {

                        String s = response.body().string();
                        Log.e("zhifu", s);
                        itemPayGson = new Gson().fromJson(s, ItemPayGson.class);
                        hd.sendEmptyMessage(GET_INFO);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    /**
     *
     */
    private void jiaJiListener() {
        jiaNumStr = jiajiJine.getText().toString();
        jiaNum = Integer.valueOf(jiaNumStr);
        zhiNumStr = zhidingJine.getText().toString();
        zhiNum = Integer.valueOf(zhiNumStr);
        tiNumStr = tishengJine.getText().toString();
        tiNum = Integer.valueOf(tiNumStr);
        baiduNumStr = baiduJine.getText().toString();
        baiduNum = Integer.valueOf(baiduNumStr);

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
                    ld = new LoadingDialog(FaBuHouActivity.this).setMessage("加载中.....");
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
                        Log.e("fukuan", s);
                        itemPayGson = new Gson().fromJson(s, ItemPayGson.class);
                        hd.sendEmptyMessage(ITEMPAY);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            ld = new LoadingDialog(FaBuHouActivity.this).setMessage("加载中.....");
            ld.show();

        }

    }

    @OnClick({R.id.zhifu_dingjin, R.id.chakan_xiangqing, R.id.jiaji_jian, R.id.jiaji_zengjia, R.id.zhiding_jian, R.id.zhiding_zengjia, R.id.tisheng_jian, R.id.tisheng_zengjia, R.id.baidu_jian, R.id.baidu_zengjia, R.id.zhifubao_zhifu, R.id.wxzf_zhifu, R.id.yezf_zhifu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zhifu_dingjin:
                if (id.equals("0")) {
                    CustomToast.showToast(FaBuHouActivity.this, "预约不支持此操作",
                            Toast.LENGTH_SHORT);
                } else {
                    if (null != itemPayGson) {
                        if (itemPayGson.getItemtype().equals("1")) {
                            //此处为招标,付定金会跳转到过度界面
                            Intent intent = new Intent(FaBuHouActivity.this, PayDepositActivity.class);
                            intent.putExtra("ID", id);
                            startActivityForResult(intent, 1);
                        } else {
                            showWindows();
                        }
                    }
                }
                break;
            case R.id.chakan_xiangqing:
                if (id.equals("0")) {
                    CustomToast.showToast(FaBuHouActivity.this, "预约不支持此操作",
                            Toast.LENGTH_SHORT);
                } else {
                    Intent intent = new Intent(FaBuHouActivity.this, OrderDetailsActivity.class);
                    intent.putExtra("ID", id);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.jiaji_jian:
                if (jiaNum > 0) {
                    jiaNum--;
                }
                jiajiJine.setText(jiaNum + "");
                zengZhi = jiaNum * 50 + zhiNum * 20 + tiNum * 10 + baiduNum * 50;
                zengzhiFuwuFei.setText(xiangmujine + zengZhi + "元");
                number = xiangmujine + zengZhi;
                break;
            case R.id.jiaji_zengjia:
                jiaNum++;
                jiajiJine.setText(jiaNum + "");
                zengZhi = jiaNum * 50 + zhiNum * 20 + tiNum * 10 + baiduNum * 50;
                zengzhiFuwuFei.setText(xiangmujine + zengZhi + "元");
                number = xiangmujine + zengZhi;
                break;
            case R.id.zhiding_jian:
                if (zhiNum > 0) {
                    zhiNum--;
                }
                zhidingJine.setText(zhiNum + "");
                zengZhi = jiaNum * 50 + zhiNum * 20 + tiNum * 10 + baiduNum * 50;
                zengzhiFuwuFei.setText(xiangmujine + zengZhi + "元");
                number = xiangmujine + zengZhi;
                break;
            case R.id.zhiding_zengjia:

                zhiNum++;
                zhidingJine.setText(zhiNum + "");
                zengZhi = jiaNum * 50 + zhiNum * 20 + tiNum * 10 + baiduNum * 50;
                zengzhiFuwuFei.setText(xiangmujine + zengZhi + "元");
                number = xiangmujine + zengZhi;
                break;
            case R.id.tisheng_jian:
                if (tiNum > 0) {
                    tiNum--;
                }
                tishengJine.setText(tiNum + "");
                zengZhi = jiaNum * 50 + zhiNum * 20 + tiNum * 10 + baiduNum * 50;
                zengzhiFuwuFei.setText(xiangmujine + zengZhi + "元");
                number = xiangmujine + zengZhi;
                break;
            case R.id.tisheng_zengjia:
                tiNum++;

                tishengJine.setText(tiNum + "");
                zengZhi = jiaNum * 50 + zhiNum * 20 + tiNum * 10 + baiduNum * 50;
                zengzhiFuwuFei.setText(xiangmujine + zengZhi + "元");
                number = xiangmujine + zengZhi;
                break;
            case R.id.baidu_jian:
                if (baiduNum > 0) {
                    baiduNum--;
                }
                baiduJine.setText(baiduNum + "");
                zengZhi = jiaNum * 50 + zhiNum * 20 + tiNum * 10 + baiduNum * 50;
                zengzhiFuwuFei.setText(xiangmujine + zengZhi + "元");
                number = xiangmujine + zengZhi;
                break;
            case R.id.baidu_zengjia:
                if (baiduNum < 1) {
                    baiduNum++;
                    baiduJine.setText(baiduNum + "");
                    zengZhi = jiaNum * 50 + zhiNum * 20 + tiNum * 10 + baiduNum * 50;
                    zengzhiFuwuFei.setText(xiangmujine + zengZhi + "元");
                    number = xiangmujine + zengZhi;
                }else {
                    CustomToast.showToast(FaBuHouActivity.this, "该卡只能购买一张",
                            Toast.LENGTH_SHORT);
                }
                break;
            case R.id.zhifubao_zhifu:
                dataHttp();
                isZhiFuB = true;
                isWeiX = false;
                isYuE = false;
                waitZhifuData.setVisibility(View.VISIBLE);
                break;
            case R.id.wxzf_zhifu:
                dataHttp();
                isZhiFuB = false;
                isWeiX = true;
                isYuE = false;
                waitZhifuData.setVisibility(View.VISIBLE);
                break;
            case R.id.yezf_zhifu:
                dataHttp();
                isZhiFuB = false;
                isWeiX = false;
                isYuE = true;
                waitZhifuData.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 支付宝支付方法
     */
    private void zhifubao() {
        if (UrlConfig.alipay_flag.equals("1")) {
            if (isAddToPay) {
                ld = new LoadingDialog(FaBuHouActivity.this).setMessage("加载中.....");
                ld.show();
                Alipay.pay(FaBuHouActivity.this, LhtTool.getHander(FaBuHouActivity.this, itemAddToPay.getTotalfee(), ld), Alipay.getOrderInfo("支付" + id + "号项目金额", "支付" + id + "号项目订单" + itemAddToPay.getOrderno(), itemAddToPay.getTotalfee(), itemAddToPay.getOrderno()));
            } else {
                Alipay.pay(FaBuHouActivity.this, LhtTool.getHander(FaBuHouActivity.this, itemPayGson.getTotal(), ld), Alipay.getOrderInfo("支付" + itemPayGson.getItemId() + "号项目金额", "支付" + itemPayGson.getItemId() + "号项目订单" + itemPayGson.getOrderno(), itemPayGson.getTotal(), itemPayGson.getOrderno()));
            }
        } else {
            CustomToast.showToast(FaBuHouActivity.this, "该版本暂时不支持支付宝支付",
                    Toast.LENGTH_SHORT);
        }
    }

    /**
     * 微信支付方法
     */
    private void weixin() {
        if (UrlConfig.weixinpay_flag.equals("1")) {
            if (isAddToPay) {
                GetPrepayIdTask get = new GetPrepayIdTask(FaBuHouActivity.this, itemAddToPay.getOrderno(), "订单编号：" + id + "付款", itemAddToPay.getTotalfee());
                get.execute();
            } else {
                GetPrepayIdTask get = new GetPrepayIdTask(FaBuHouActivity.this, itemPayGson.getOrderno(), "订单编号：" + itemPayGson.getItemId() + "付款", itemPayGson.getTotal());
                get.execute();
            }
        } else {
            CustomToast.showToast(FaBuHouActivity.this, "该版本暂时不支持微信支付",
                    Toast.LENGTH_SHORT);
        }
    }

    /**
     * 余额支付
     */
    private void yuezhifu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FaBuHouActivity.this);
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
                            public void onResponse(Call call, Response response) {
                                try {
                                    String s = response.body().string();
                                    LogUtils.d("===============response:" + s);
                                    Message ms = new Message();
                                    ms.what = ADDTOPAYDO;
                                    Bundle b = new Bundle();
                                    b.putString("s", s);
                                    ms.setData(b);
                                    hd.sendMessage(ms);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        });

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FaBuHouActivity.this).setTitle("提示信息").setMessage("余额不足，是否充值").setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(FaBuHouActivity.this, RechargeActivity.class), 1);
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
                        ld = new LoadingDialog(FaBuHouActivity.this).setMessage("加载中.....");
                        ld.show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FaBuHouActivity.this).setTitle("提示信息").setMessage("余额不足，是否充值").setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(FaBuHouActivity.this, RechargeActivity.class), 1);
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


    private void diaoYong() {
        if (isZhiFuB) {
            waitZhifuData.setVisibility(View.GONE);
            zhifubao();
        } else if (isWeiX) {
            waitZhifuData.setVisibility(View.GONE);
            weixin();
        } else if (isYuE) {
            waitZhifuData.setVisibility(View.GONE);
            yuezhifu();
        }else {
            if (itemPayGson.getItemtype().equals("1")) {
                zhifuDingjin.setText("支付定金");
                xiangmuJinE.setText(itemPayGson.getShow_item_money());
//                    zhushi.setVisibility(View.VISIBLE);
            } else {
                zhifuDingjin.setText("托管赏金");
                xiangmuJinE.setText(itemPayGson.getTotal());
//                    zhushi.setVisibility(View.GONE);
            }
            xiangmujine = Double.parseDouble(xiangmuJinE.getText().toString().trim());
            zengzhiFuwuFei.setText(xiangmujine+"");
            xiangmuBianHao.setText("项目编号：" + itemPayGson.getItemId());
            fkYeNumShow.setText(Html.fromHtml("余额  <font color='#cccccc'>（可用余额：￥" + itemPayGson.getBalance() + "）</font>"));
            fabuXiangmuJianjieContent.setText(itemPayGson.getProjectName());
        }
    }

    /**
     * 支付弹窗
     */
    private View viewShow;
    private PopupWindow popupWindow;

    private void showWindows() {
        viewShow = LayoutInflater.from(context).inflate(R.layout.daoju_goumai_window, null);
        popupWindow = new PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));//也可以直接把Color.TRANSPARENT换成0
        popupWindow.setAnimationStyle(R.style.popWindow_animation);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f; //0.0-1.0
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(viewShow, Gravity.CENTER, 0, 1000);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f; //0.0-1.0
                getWindow().setAttributes(lp);
            }
        });
        showData();
    }

    /**
     * 购买弹窗数据
     */
    private Button btn_jian, btn_number, btn_jia, btn_goumai;
    private TextView daojuNumberMoney, yueshow;
    private RelativeLayout zhifubao, weixin, yue;
    private LinearLayout waitData, goumaixiangqing;
    private double number = 0;

    private void showData() {
        btn_jian = viewShow.findViewById(R.id.btn_number_jian);
        btn_number = viewShow.findViewById(R.id.btn_number);
        btn_jia = viewShow.findViewById(R.id.btn_number_jia);
        btn_goumai = viewShow.findViewById(R.id.btn_enter_goumai);
        daojuNumberMoney = viewShow.findViewById(R.id.daoju_money_number);
        goumaixiangqing = viewShow.findViewById(R.id.goumai_xiangqing);
        zhifubao = viewShow.findViewById(R.id.daoju_zhifu_zfb);
        weixin = viewShow.findViewById(R.id.daoju_zhifu_wx);
        yue = viewShow.findViewById(R.id.daoju_zhifu_yue);
        waitData = viewShow.findViewById(R.id.wait_zhifu_data);
        yueshow = viewShow.findViewById(R.id.yue_num_show);
        goumaixiangqing.setVisibility(View.GONE);
        daojuNumberMoney.setText(number + "");
        yueshow.setText("余额:" + MyApplication.userInfo.getBalance());
        zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataHttp();
                isZhiFuB = true;
                isWeiX = false;
                isYuE = false;
                number = 0;
                waitZhifuData.setVisibility(View.VISIBLE);
            }
        });
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataHttp();
                isZhiFuB = false;
                isWeiX = true;
                isYuE = false;
                waitZhifuData.setVisibility(View.VISIBLE);
            }
        });
        yueshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataHttp();
                isZhiFuB = false;
                isWeiX = false;
                isYuE = true;
                waitZhifuData.setVisibility(View.VISIBLE);
            }
        });

    }

    private double xiangmujine = 0;
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == GET_INFO) {
                diaoYong();


            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(FaBuHouActivity.this, msg);

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
                        CustomToast.showToast(FaBuHouActivity.this, " 无效支付请求", Toast.LENGTH_LONG);
                        break;
                    case "moneyless":
                        CustomToast.showToast(FaBuHouActivity.this, "余额不足", Toast.LENGTH_LONG);
                        break;
                    case "fail":
                        CustomToast.showToast(FaBuHouActivity.this, "支付失败", Toast.LENGTH_LONG);
                        break;
                    case "orderno_err":
                        CustomToast.showToast(FaBuHouActivity.this, "无效订单编号", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        CustomToast.showToast(FaBuHouActivity.this, "余额支付增加悬赏成功", Toast.LENGTH_LONG);
                        finish();
                        break;
                    case "unlogin":
                        CustomToast.showToast(FaBuHouActivity.this, "未登录", Toast.LENGTH_LONG);
                        break;
                    default:
                        CustomToast.showToast(FaBuHouActivity.this, "支付失败", Toast.LENGTH_LONG);

                        break;
                }
            } else if (msg.what == PAYRESULT) {
                if (ld != null) {
                    ld.dismiss();
                }
                switch (payResultGson.getPayResult()) {
                    case "has":
                        CustomToast.showToast(FaBuHouActivity.this, "该项目已经支付过了", Toast.LENGTH_LONG);
                        break;
                    case "moneyless":
                        CustomToast.showToast(FaBuHouActivity.this, "余额不足", Toast.LENGTH_LONG);
                        break;
                    case "fail":
                        CustomToast.showToast(FaBuHouActivity.this, "支付失败", Toast.LENGTH_LONG);
                        break;
                    case "nomoney":
                        CustomToast.showToast(FaBuHouActivity.this, "项目金额为0，不能支付", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        AlertDialog.Builder builder = new AlertDialog.Builder(FaBuHouActivity.this).setTitle("付款成功").setMessage("已支付成功，待审核通过后将发布； 您随时可以登录您的用户名查看威客们提交的方案，并采纳满意的方案，如需与威客沟通，请直接给他们发站内消息或者向时间财富客服索取相关威客的联系方式，如有不明白的地方请致电：4006306800 \n" +
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
                        CustomToast.showToast(FaBuHouActivity.this, payResultGson.getPayResult(), Toast.LENGTH_LONG);
                        break;
                }
            }

        }
    };

}
