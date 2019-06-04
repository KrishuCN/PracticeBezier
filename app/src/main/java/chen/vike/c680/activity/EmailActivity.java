package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.LoadingDialog;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/7.
 * 邮箱验证
 */

public class EmailActivity extends BaseStatusBarActivity{

    private EditText email;
    private EditText code;
    private Button bt;
    private Button bt1;
    private TextView tv;
    private long currentTime = 60 * 1000;
    private Timer timer;
    private LoadingDialog ld;
    private Map<String, Object> map = new HashMap<>();
    private final int TIJIAOMESSAGE = 0x111;
    private final int GETYANZHENGMA = 0x121;
    private final int NETWORK_EXCEPTION = 0x112;
    private final int TIME = 0x113;
    private long exitTime2;
    private long exitTime;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        getTitle().setText("邮箱认证");
        email =  findViewById(R.id.email);
        tv =  findViewById(R.id.userName);
        bt =  findViewById(R.id.button9_eml);
        bt1 =  findViewById(R.id.button4);
        code =  findViewById(R.id.code_eml);
        isLogin();//判断是否登录  以便得到数据
        viewLinstener();//控件监听事件

    }

    /**
     * view控件监听事件
     */
    private void viewLinstener(){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   bt.setEnabled(false);
                if (email.getText().toString().isEmpty() || !email.getText().toString().contains("@")) {
                    if ((System.currentTimeMillis() - exitTime2) > 4000) {
                        CustomToast.showToast(EmailActivity.this, "请正确填写邮箱", Toast.LENGTH_LONG);
                        exitTime2 = System.currentTimeMillis();
                        bt.setEnabled(true);
                    }

                } else {

                    map.put("crv_email", email.getText());
                    map.put("type", "verify");
                    ld=new LoadingDialog(EmailActivity.this).setMessage("加载中....");
                    ld.show();
                    OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.EMAIL_VERIFICATION, map, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            String s = response.body().string();
                            LogUtils.d("======================GETYANZHENGMA_Response:" + s);
                            Message ms = new Message();
                            ms.what = GETYANZHENGMA;
                            Bundle b = new Bundle();
                            b.putString("s", s);
                            ms.setData(b);
                            hd.sendMessage(ms);
                        }
                    });

                }
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.getText().toString().isEmpty()) {
                    if ((System.currentTimeMillis() - exitTime) > 4000) {
                        CustomToast.showToast(EmailActivity.this, "验证码不能为空", Toast.LENGTH_SHORT);
                        exitTime = System.currentTimeMillis();
                    }
                } else {
                    map.put("ecode", code.getText());
                    map.put("type", "verify");
                    ld=new LoadingDialog(EmailActivity.this).setMessage("加载中....");
                    ld.show();
                    OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.EMAIL_UPDATE, map, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String s = response.body().string();
                            LogUtils.d("======================GETYANZHENGMA_Response:" + s);
                            Message ms = new Message();
                            ms.what = TIJIAOMESSAGE;
                            Bundle b = new Bundle();
                            b.putString("t", s);
                            ms.setData(b);
                            hd.sendMessage(ms);
                        }
                    });

                }
            }
        });
    }

    /**
     * 判断是否登录
     */
    private void isLogin(){
        if (LhtTool.isLogin) {

            email.setText(MyApplication.userInfo.getEmail());
            tv.setText("用户名：" + MyApplication.userInfo.getNickame());
            if (MyApplication.userInfo.getEmail().isEmpty()) {
                email.setHint("请输入您的邮箱");
            } else {
                if (!MyApplication.userInfo.getEmail().contains("@680.com")) {
                   email.setFocusable(false);
                }
            }

            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());

        }
    }
    @SuppressLint("HandlerLeak")
    private Handler hd=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TIME) {
                currentTime -= 1000;
                bt.setText(currentTime / 1000 + "秒后重新获取");
                if (currentTime <= 0) {
                    currentTime = 60 * 1000;
                    timer.cancel();
                    bt.setText("获取验证码");
                    bt.setEnabled(true);
                }
            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(EmailActivity.this, msg);
                bt.setEnabled(true);
            } else if (msg.what == GETYANZHENGMA) {
                if (ld != null) {
                    ld.dismiss();
                }
                String s = msg.getData().getString("s");
                switch (s) {
                    case "senderr":
                        CustomToast.showToast(EmailActivity.this, "发送邮件失败,请稍后再试", Toast.LENGTH_LONG);
                        break;
                    case "nouser":
                        CustomToast.showToast(EmailActivity.this, "无效用户", Toast.LENGTH_LONG);
                        break;
                    case "noemail":
                        CustomToast.showToast(EmailActivity.this, "无效邮箱", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        CustomToast.showToast(EmailActivity.this, "验证码已经发送", Toast.LENGTH_LONG);
                        //按钮开始计时
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                hd.sendEmptyMessage(TIME);
                            }
                        }, 0, 1000);
                        break;
                    case "stop":
                        CustomToast.showToast(EmailActivity.this, "暂不能进行邮箱验证", Toast.LENGTH_LONG);
                        break;
                    case "hasapp":
                        CustomToast.showToast(EmailActivity.this, "邮箱已被认证，请更换邮箱", Toast.LENGTH_LONG);
                        break;
                    case "err":
                        CustomToast.showToast(EmailActivity.this, "申请邮箱验证码失败", Toast.LENGTH_LONG);
                        break;
                }

            } else if (msg.what == TIJIAOMESSAGE) {
                if (ld != null) {
                    ld.dismiss();
                }
                String s = msg.getData().getString("t");
                switch (s) {
                    case "fail":
                        CustomToast.showToast(EmailActivity.this, "邮箱验证码错误", Toast.LENGTH_LONG);
                        break;
                    case "notcode":
                        CustomToast.showToast(EmailActivity.this, "请填写邮箱验证码", Toast.LENGTH_LONG);
                        break;
                    case "unlogin":
                        CustomToast.showToast(EmailActivity.this, "请先登录", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        CustomToast.showToast(EmailActivity.this, "邮箱认证成功", Toast.LENGTH_LONG);
                        MyApplication.userInfo.setIs_verify_email("1");
                        MyApplication.userInfo.setEmail(email.getText().toString());
                        finish();
                        break;
                }
            }
        }
    };

}
