package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import chen.vike.c680.bean.UserYanZhengInfoBean;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2018/6/20.
 * 提现未认证
 */

public class GRZXWeiTiXianActivity extends BaseStatusBarActivity {

    @BindView(R.id.buke_tixian_jine)
    TextView bukeTixianJine;
    @BindView(R.id.wei_tixian_renzheng_name)
    TextView weiTixianRenzhengName;
    @BindView(R.id.wei_tixian_renzheng_btn)
    Button weiTixianRenzhengBtn;
    @BindView(R.id.wei_tixian_renzheng_email)
    TextView weiTixianRenzhengEmail;
    @BindView(R.id.wei_tixian_renzheng_email_btn)
    Button weiTixianRenzhengEmailBtn;
    @BindView(R.id.wei_tixian_renzheng_yihang)
    TextView weiTixianRenzhengYihang;
    @BindView(R.id.wei_tixian_renzheng_yihang_btn)
    Button weiTixianRenzhengYihangBtn;
    @BindView(R.id.wei_tixian_page)
    LinearLayout weiTixianPage;
    private Context mContext;
    private String email;
    private String bankname;
    private String fullname;
    private Map<String, Object> map = new HashMap<>();
    private final int TIXIANQUEREN = 0x123;
    private final int NETWORKEXCEPTION = 0x122;
    private UserYanZhengInfoBean userYanZhengInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian_buke_item);
        getTitle().setText("提现");
        mContext = this;
        email = getIntent().getStringExtra("email");
        bankname = getIntent().getStringExtra("bankname");
        fullname = getIntent().getStringExtra("fullname");
        ButterKnife.bind(this);
        setView();
    }

    /**
     * 操作视图
     */
    private void setView() {
        if (LhtTool.isLogin) {
            if (MyApplication.userInfo != null && MyApplication.userInfo.getBalance() != null) {
                bukeTixianJine.setText(MyApplication.userInfo.getBalance() + "元");
            }
        } else {
            CustomToast.showToast(mContext, "请登录后进行操作", Toast.LENGTH_SHORT);
        }
        viewListener();
        personalVerificationInfo();

    }

    /**
     * view监听事件
     */
    private void viewListener() {
        weiTixianRenzhengBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullname.isEmpty()) {

                    Intent in = new Intent(mContext, ShiMingRenZhengActivity.class);
                    startActivityForResult(in, 1);

                } else if (fullname.toString().equals("2")) {
                    CustomToast.showToast(mContext, "审核中...", Toast.LENGTH_SHORT);

                } else {
                    CustomToast.showToast(mContext, "实名已验证", Toast.LENGTH_SHORT);
                }
            }
        });
        weiTixianRenzhengEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //邮箱验证界面跳转
                if (email.isEmpty()) {
                    Intent in = new Intent(mContext, EmailActivity.class);
                    startActivityForResult(in, 2);
                } else {
                    CustomToast.showToast(mContext, "邮箱已验证", Toast.LENGTH_SHORT);
                }
            }
        });
        weiTixianRenzhengYihangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //银行卡绑定界面
                if (userYanZhengInfo.getV_bankinfo().equals("1")) {
                    CustomToast.showToast(mContext, "银行卡已绑定", Toast.LENGTH_SHORT);
                } else {
                    if (userYanZhengInfo.getV_fullname().equals("2")) {
                        CustomToast.showToast(mContext, "请等待审核", Toast.LENGTH_SHORT);
                    } else {
                        if (userYanZhengInfo.getV_fullname().equals("1")) {
                            if (userYanZhengInfo.getV_email().equals("1")) {
                                Intent in = new Intent(mContext, BankActivity.class);
                                startActivityForResult(in, 3);
                            } else {
                                CustomToast.showToast(mContext, "请先进行邮箱和实名验证", Toast.LENGTH_SHORT);
                            }
                        } else {
                            CustomToast.showToast(mContext, "请先进行邮箱和实名验证", Toast.LENGTH_SHORT);
                        }
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        personalVerificationInfo();
        if (MyApplication.userInfo != null) {
            if (MyApplication.userInfo.getIs_verify_fullname() != null) {
                if (requestCode == 1) {
                    if (MyApplication.userInfo.getIs_verify_fullname().equals("2")) {
                        weiTixianRenzhengName.setText("等待审核中...");
                        weiTixianRenzhengBtn.setText("等待审核中");
                        fullname = "等待审核中";
                    }
                } else if (requestCode == 2) {
                    if (MyApplication.userInfo.getIs_verify_email().equals("1")) {
                        weiTixianRenzhengEmail.setText(MyApplication.userInfo.getEmail());
                        weiTixianRenzhengEmailBtn.setTextColor(mContext.getResources().getColor(R.color.text_color_10));
                        email = MyApplication.userInfo.getEmail();
                    }
                } else if (requestCode == 3) {
                    if (MyApplication.userInfo.getIs_bind_bankinfo().equals("1")) {
                        weiTixianRenzhengYihang.setText(MyApplication.userInfo.getBankname());
                        weiTixianRenzhengYihangBtn.setTextColor(mContext.getResources().getColor(R.color.text_color_13));
                        bankname = MyApplication.userInfo.getBankname();
                    }
                }
            }
        }
    }

    /**
     * 基本数据
     */
    private void personalVerificationInfo() {

        Map<String, Object> map = new HashMap<>();
        if (MyApplication.userInfo != null && MyApplication.userInfo.getUserID() != null) {
            map.put("userid", MyApplication.userInfo.getUserID());
        }
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.USER_VERIFICATION_INFO, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("============Response:" + s);
                    userYanZhengInfo = new Gson().fromJson(s, UserYanZhengInfoBean.class);
                    hd.sendEmptyMessage(TIXIANQUEREN);
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
            if (msg.what == TIXIANQUEREN) {

                if (userYanZhengInfo.getV_fullname().equals("1")) {

                    weiTixianRenzhengName.setText(userYanZhengInfo.getTruename());
                    MyApplication.userInfo.setIs_verify_fullname("1");
                    weiTixianRenzhengBtn.setText("已认证");
                    weiTixianRenzhengBtn.setTextColor(mContext.getResources().getColor(R.color.text_color_10));

                } else if (userYanZhengInfo.getV_fullname().equals("2")) {
                    // weiTixianRenzhengBtn.setTextColor(mContext.getResources().getColor(R.color.text_color_10));
                    weiTixianRenzhengBtn.setText("等待审核中");
                    weiTixianRenzhengName.setText("等待审核中");
                    MyApplication.userInfo.setIs_verify_fullname("2");

                } else {
                    weiTixianRenzhengName.setText("请点击验证");
                }
                if (userYanZhengInfo.getV_email().equals("1")) {

                    weiTixianRenzhengEmail.setText(userYanZhengInfo.getEmail());
                    weiTixianRenzhengEmailBtn.setTextColor(mContext.getResources().getColor(R.color.text_color_10));
                } else {

                    weiTixianRenzhengEmail.setText("请点击验证");


                }
                if (userYanZhengInfo.getV_bankinfo().equals("1")) {

                    weiTixianRenzhengYihang.setText(userYanZhengInfo.getBankcardno());
                    weiTixianRenzhengYihangBtn.setTextColor(mContext.getResources().getColor(R.color.text_color_10));

                } else {

                    weiTixianRenzhengYihang.setText("请点击绑定银行卡");

                }


            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(mContext, msg);

            }
        }
    };

}
