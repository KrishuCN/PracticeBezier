package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import chen.vike.c680.ALiPay.Alipay;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.vike.c680.bean.FaXianYuEBean;
import chen.vike.c680.bean.FaXianZuoPinBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2018/4/2.
 * 购买发现中作品的支付
 */

public class FaXianZhiFuActivity extends BaseStatusBarActivity {

    @BindView(R.id.fa_zhifu_head_img)
    ImageView faZhifuHeadImg;
    @BindView(R.id.fa_zhifu_head_text)
    TextView faZhifuHeadText;
    @BindView(R.id.xmje)
    TextView xmje;
    @BindView(R.id.zpmc)
    TextView zpmc;
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
    private String faID, faMoney, faPhone;
    private static final String FXDINGD = "http://app.680.com/api/v4/faxian_zuopin_buy.ashx";
    private static final String YUEZHIFU = "http://app.680.com/api/v4/pay_zuopin_order.ashx";
    private static final int FANHUIDATA = 0x123;
    private static final int ERRODATA = 0x124;
    private static final int YUEDATA = 0X125;
    private static final int ERROYUE = 0X126;
    private LoadingDialog ld;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faxian_zhifu);
        ButterKnife.bind(this);
        this.getTitle().setText("订单获取中...");
        faID = getIntent().getStringExtra("zp_id_v");
        faMoney = getIntent().getStringExtra("money_v");
        faPhone = getIntent().getStringExtra("crv_phone_v");
        zhifuListener();//支付监听事件
        xiadanData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 支付监听事件
     */
    private void zhifuListener() {
        zhifubaoZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UrlConfig.alipay_flag.equals("1")) {
                    ld = new LoadingDialog(FaXianZhiFuActivity.this).setMessage("加载中.....");
                    ld.show();
                    Alipay.pay(FaXianZhiFuActivity.this, LhtTool.getHander(FaXianZhiFuActivity.this, zuopinbean.getTotal(), ld), Alipay.getOrderInfo("支付:" + zuopinbean.getOrdername(), "支付" + zuopinbean.getZuopinid() + "号项目订单" +
                            zuopinbean.getOrderno(), zuopinbean.getTotal(), zuopinbean.getOrderno()));

                } else {
                    CustomToast.showToast(FaXianZhiFuActivity.this, "该版本暂时不支持支付宝支付",
                            Toast.LENGTH_SHORT);
                }
            }
        });
        wxzfZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UrlConfig.weixinpay_flag.equals("1")) {
                    GetPrepayIdTask get = new GetPrepayIdTask(FaXianZhiFuActivity.this, zuopinbean.getOrderno(), zuopinbean.getOrdername() + "付款", zuopinbean.getTotal());
                    get.execute();
                } else {
                    CustomToast.showToast(FaXianZhiFuActivity.this, "该版本暂时不支持微信支付",
                            Toast.LENGTH_SHORT);
                }
            }
        });
        yezfZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ld = new LoadingDialog(FaXianZhiFuActivity.this).setMessage("加载中.....");
                ld.show();
                yueZhifu();
            }
        });
    }

    /**
     * 余额支付
     */
    private FaXianYuEBean yueBeanFaXian = new FaXianYuEBean();
    private final int NETWORK_EXCEPTION = 0X111;
    private void yueZhifu() {
        Map<String, Object> yMap = new HashMap<>();
        yMap.put("ItemId", zuopinbean.getItemid());
        yMap.put("vkuserid", zuopinbean.getUserid());
        yMap.put("orderno", zuopinbean.getOrderno());
        yMap.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        yMap.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        OkhttpTool.Companion.getOkhttpTool().post(YUEZHIFU, yMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    Log.e("yuezhifu", s);
                    yueBeanFaXian = new Gson().fromJson(s, FaXianYuEBean.class);
                    if (yueBeanFaXian != null) {
                        if (yueBeanFaXian.getPayResult().equals("ok")) {
                            handler.sendEmptyMessage(YUEDATA);
                        } else {
                            handler.sendEmptyMessage(ERROYUE);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 作品订单
     */
    private FaXianZuoPinBean zuopinbean = new FaXianZuoPinBean();
    private List<String> lists = new ArrayList<>();

    private void xiadanData() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", faPhone);
        map.put("item_money", faMoney);
        map.put("userid", MyApplication.userInfo.getUserID());
        map.put("zuopinid", faID);
        map.put("apptype", "an");
        OkhttpTool.Companion.getOkhttpTool().post(FXDINGD, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    Log.e("faxian", s);
                    zuopinbean = new Gson().fromJson(s, FaXianZuoPinBean.class);
                    if (zuopinbean != null) {
                        Log.e("faxian", zuopinbean.getErrmsg() + "+++");
                        if (zuopinbean.getErrmsg().equals("0")) {
                            handler.sendEmptyMessage(FANHUIDATA);
                            lists.add(zuopinbean.getZuopinid());
                            lists.add(zuopinbean.getUserid());
                            lists.add(zuopinbean.getTotal());
                            lists.add(zuopinbean.getOrderno());
                            lists.add(zuopinbean.getOrdername());
                            lists.add(zuopinbean.getItemid());
                            lists.add(zuopinbean.getBalance());
                        } else {
                            handler.sendEmptyMessage(ERRODATA);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FANHUIDATA:
                    getTitle().setText("支付中...");
                    faZhifuHeadImg.setBackgroundResource(R.mipmap.tjcg);
                    faZhifuHeadText.setText("提交成功");
                    xmje.setText("作品金额：￥ " + zuopinbean.getTotal());
                    zpmc.setText("作品名称：" + zuopinbean.getOrdername());
                    fkYeNumShow.setText("余额:" + zuopinbean.getBalance() + "元");
                    break;
                case ERRODATA:
                    getTitle().setText("提交失败");
                    switch (zuopinbean.getErrmsg()) {
                        case "unlogin":
                            CustomToast.showToast(FaXianZhiFuActivity.this, "请登录后进行操作", Toast.LENGTH_LONG);
                            faZhifuHeadText.setText("请登录后进行操作");
                            break;
                        case "nozuopin":
                            CustomToast.showToast(FaXianZhiFuActivity.this, "请提供作品ID", Toast.LENGTH_LONG);
                            faZhifuHeadText.setText("请提供作品ID");
                            break;
                        case "null_phone":
                            CustomToast.showToast(FaXianZhiFuActivity.this, "手机号码填写错误", Toast.LENGTH_LONG);
                            faZhifuHeadText.setText("手机号码填写错误");
                            break;
                        case "noitemmoney":
                            CustomToast.showToast(FaXianZhiFuActivity.this, "请填写金额", Toast.LENGTH_LONG);
                            faZhifuHeadText.setText("请填写金额");
                            break;
                        case "fabufail":
                            CustomToast.showToast(FaXianZhiFuActivity.this, "提交失败", Toast.LENGTH_LONG);
                            faZhifuHeadText.setText("提交失败");
                            break;
                        case "fabuerr":
                            CustomToast.showToast(FaXianZhiFuActivity.this, "提交失败，遇见错误", Toast.LENGTH_LONG);
                            faZhifuHeadText.setText("提交失败，遇见错误");
                            break;
                    }
                    break;
                case YUEDATA:
                    getTitle().setText("支付成功");
                    fkYeNumShow.setText("余额：" + yueBeanFaXian.getBalance() + "元");
                    if (ld != null) {
                        ld.dismiss();
                    }
                    // finish();
                    break;
                case ERROYUE:
                    getTitle().setText("支付失败");
                    switch (yueBeanFaXian.getPayResult()) {
                        case "has":
                            CustomToast.showToast(FaXianZhiFuActivity.this, "该作品已支付成功", Toast.LENGTH_LONG);
                            break;
                        case "moneyless":
                            CustomToast.showToast(FaXianZhiFuActivity.this, "余额不足", Toast.LENGTH_LONG);
                            break;
                        case "nomoney":
                            CustomToast.showToast(FaXianZhiFuActivity.this, "作品金额为0，不能支付", Toast.LENGTH_LONG);
                            break;
                    }
                    if (ld != null) {
                        ld.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
