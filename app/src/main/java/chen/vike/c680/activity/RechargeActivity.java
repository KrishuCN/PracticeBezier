package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import chen.vike.c680.ALiPay.Alipay;
import chen.vike.c680.bean.GridViewInfoBean;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.LoadingDialog;
import chen.vike.c680.views.MyGridView;
import chen.vike.c680.WXPay.GetPrepayIdTask;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chen.vike.c680.adapter.ChongZhiAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/9.
 * <p>
 * 充值
 */

public class RechargeActivity extends BaseStatusBarActivity {


    @BindView(R.id.textView37)
    TextView textView37;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.now_money)
    TextView nowMoney;
    @BindView(R.id.textView16)
    TextView textView16;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.jine)
    EditText jine;
    @BindView(R.id.textView35)
    TextView textView35;
    @BindView(R.id.chongzhi_jine)
    MyGridView chongzhiJine;
    @BindView(R.id.recharge_bt)
    Button rechargeBt;
    @BindView(R.id.qrcz)
    LinearLayout qrcz;
    @BindView(R.id.qr)
    LinearLayout qr;
//    @BindView(R.id.money)
//    TextView money;

    private LoadingDialog ld;
    private Map<String, Object> map = new HashMap<>();
    private String order_number;
    private final int GETINFO = 0x123;
    private final int NETWORKEXCEPTION = 0X111;

    private boolean isRun = false;
    private ChongZhiAdapter chongZhiAdapter;
    private String[] moneys = {"50","100","200", "300", "500", "1000","2000","5000"};
    private List<GridViewInfoBean> list_money = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chongzhi);
        ButterKnife.bind(this);
        mContext = this;
        getTitle().setText("充值");
        if (MyApplication.userInfo != null && MyApplication.userInfo.getNickame() != null) {
            userName.setText(MyApplication.userInfo.getNickame());
            nowMoney.setText("￥" + MyApplication.userInfo.getBalance());
        }
        for (int i = 0; i < moneys.length; i++) {
            GridViewInfoBean info = new GridViewInfoBean();
            info.setName(moneys[i]);
            list_money.add(info);
        }
        iniview();
        viewListener();//控件的监听事件

    }

    private void iniview() {
        chongZhiAdapter = new ChongZhiAdapter(mContext, list_money);
        chongzhiJine.setAdapter(chongZhiAdapter);
        chongzhiJine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jine.setText(moneys[position]);
                jine.setSelection(moneys[position].length());
                zhiFuFangS();
            }
        });
    }

    /**
     * view控件的监听事件
     */
    private void viewListener() {
//

        rechargeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zhiFuFangS();
            }
        });
        jine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chongzhiJine.setVisibility(View.VISIBLE);

            }
        });

    }

    /**
     * 充值方式弹窗
     */
    private View viewShow;
    private PopupWindow popupWindow;
    private View zhifuB,weiXin;
    private  boolean isZhiFb,isWeix;
    private void zhiFuFangS(){
        viewShow = LayoutInflater.from(mContext).inflate(R.layout.chongzhi_yue_fangshi_item, null);
        popupWindow = new PopupWindow(viewShow, LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));//也可以直接把Color.TRANSPARENT换成0
        popupWindow.setAnimationStyle(R.style.popWindow_animation);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f; //0.0-1.0
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(viewShow, Gravity.CENTER, 0, 900);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f; //0.0-1.0
                getWindow().setAttributes(lp);
            }
        });
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        zhifuB = viewShow.findViewById(R.id.zfb);
        weiXin = viewShow.findViewById(R.id.wxzf);
        isZhiFb = false;
        isWeix = false;
        windListener();
    }

    private void windListener(){
        zhifuB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isZhiFb = true;
                isWeix = false;
                chongbtn();
            }
        });

        weiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isZhiFb = false;
                isWeix = true;
                chongbtn();
            }
        });
    }
    /**
     * 确定充值按钮事件
     */
   private void chongbtn(){
       if (jine.getText().toString().isEmpty()) {
           CustomToast.showToast(RechargeActivity.this, "充值金额不能为空",
                   Toast.LENGTH_SHORT);
       } else {
           if (Integer.valueOf(jine.getText().toString()) >= 1) {
               //点击按钮隐藏软键盘
               InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(jine.getWindowToken(), 0);
               ld = new LoadingDialog(RechargeActivity.this).setMessage("加载中.....");
               map.put("UserId", MyApplication.userInfo.getUserID());
               map.put("ToMoney", jine.getText());
               map.put("type", "an");
               OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.RECHANGE_MONEY, map, new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {
                       LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {

                       String s = response.body().string();
                       LogUtils.d("===============Response:" + s);
                       Message ms = new Message();
                       ms.what = GETINFO;
                       Bundle b = new Bundle();
                       b.putString("s", s);
                       ms.setData(b);
                       hd.sendMessage(ms);

                   }
               });
           } else {
               CustomToast.showToast(RechargeActivity.this, "充值金额不能低于1元",
                       Toast.LENGTH_SHORT);

           }

       }
   }
    @Override
    protected void onResume() {
        Log.e("chongzhiyemian", "chongzhiyemian");
        if (isRun) {
            // finish();
        }
        super.onResume();
    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GETINFO) {

                String s = msg.getData().getString("s");
                if (!s.equals("")) {

                    order_number = s;
                    if (ld != null) {
                        ld.dismiss();
                    }
                    if (isZhiFb){
                        if ("1".equals(UrlConfig.alipay_flag)) {
                            if (LhtTool.firstClick) {
                                LhtTool.firstClick = false;
                                ld = new LoadingDialog(RechargeActivity.this).setMessage("跳转中.....");
                                ld.show();
                                Alipay.pay(RechargeActivity.this, LhtTool.getHander(
                                        RechargeActivity.this, jine.getText().toString(), ld),
                                        Alipay.getOrderInfo("时间财富会员【" + MyApplication.userInfo.getNickame() + "】充值",
                                                "时间财富充值订单" + order_number, jine.getText().toString(), order_number));
                                isRun = true;
                            }
                        } else {
                            CustomToast.showToast(RechargeActivity.this, "该版本暂时不支持支付宝支付",
                                    Toast.LENGTH_SHORT);
                        }
                    }
                    if (isWeix){
                        if ("1".equals(UrlConfig.weixinpay_flag)) {
                            GetPrepayIdTask get = new GetPrepayIdTask(RechargeActivity.this, order_number,
                                    "时间财富会员【" + MyApplication.userInfo.getNickame() + "】充值", jine.getText().toString());

                            get.execute();
                        } else {
                            CustomToast.showToast(RechargeActivity.this, "该版本暂时不支持微信支付",
                                    Toast.LENGTH_SHORT);
                        }
                    }

                } else {
                    CustomToast.showToast(RechargeActivity.this, "订单生成失败，请重试！",
                            Toast.LENGTH_SHORT);
                }


            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(RechargeActivity.this, msg);

            }
        }
    };

}
