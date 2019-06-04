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
 * Created by lht on 2017/5/13.
 *
 * 确认转款
 */

public class PayMessageActivity extends BaseStatusBarActivity {

    private TextView phone;
    private EditText yzm;
    private Button getYzm;
    private Button surePay;
    private Map<String, Object> map = new HashMap<>();
    private Map<String, Object> map1 = new HashMap<>();
    private LoadingDialog ld;
    private String key;
    private Timer timer = new Timer();
    private int i = 120;
    private final static int GET_YZM = 0x123;
    private final static int SURE_PAY = 0x111;
    private final static int TIME_FADE = 0x112;
    private final static int NETWORKEXCEPTION = 0x121;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrfk_message_confirm_activity);

        getTitle().setText("确认转款");

        phone = findViewById(R.id.qrfk_mes_phone);
        yzm = findViewById(R.id.qrfk_mes_et);
        getYzm = findViewById(R.id.qrfk_mes_bt);
        surePay = findViewById(R.id.qrfk_mes_bt1);
        phone.setText("电话号码：" + getIntent().getStringExtra("phone"));

        map.put("mobile", getIntent().getStringExtra("phone"));
        map.put("key", UrlConfig.MOBILE_VERIFICATION_KEY);
        map.put("type", "verify");
        map.put("android","1");
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
        }


        getYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYZM();
            }
        });

        surePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(yzm.getText().toString())) {
                    CustomToast.showToast(PayMessageActivity.this, "请输入验证码", Toast.LENGTH_SHORT);
                } else {
                    map1.put("yzm", yzm.getText().toString());
                    map1.put("yzmkey", key);
                    map1.put("userid", MyApplication.userInfo.getUserID());
                    map1.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                    map1.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                    map1.put("shouji", getIntent().getStringExtra("phone"));
                    map1.put("itemid", getIntent().getStringExtra("itemid"));
                    if (getIntent().getExtras().containsKey("FENQI")) {
                        map1.put("is_fenqi", "1");
                        map1.put("fenqi_id", getIntent().getStringExtra("ID"));
                    } else {
                        map1.put("cyids", getIntent().getStringExtra("cyids"));
                    }
                    LogUtils.d("====================getIntent().getStringExtra(\"cyids\"):"+getIntent().getStringExtra("cyids"));

                    surePay();

                    ld = new LoadingDialog(PayMessageActivity.this).setMessage("确认中....");
                    ld.show();

                }
            }
        });

    }

    private void getYZM() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.MOBILE_VERIFICATION, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String s = response.body().string();
                LogUtils.d("====================response:" + s);
                Message ms = new Message();
                ms.what = GET_YZM;
                Bundle b = new Bundle();
                b.putString("s", s);
                ms.setData(b);
                hd.sendMessage(ms);

            }
        });
    }

    private void surePay() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.SURE_ZHUANKUAN, map1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String s = response.body().string();
                LogUtils.d("=====================response:" + s);
                Message ms = new Message();
                ms.what = SURE_PAY;
                Bundle b = new Bundle();
                b.putString("s", s);
                ms.setData(b);
                hd.sendMessage(ms);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getYzm.setText("获取验证码");
        getYzm.setEnabled(true);
        timer.cancel();
        i = 120;
    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_YZM) {

                String s = msg.getData().getString("s");
                switch (s) {
                    case "false":
                        CustomToast.showToast(PayMessageActivity.this, "短信发送失败", Toast.LENGTH_SHORT);
                        break;
                    case "":
                        CustomToast.showToast(PayMessageActivity.this, "短信发送失败", Toast.LENGTH_SHORT);
                        break;
                    default:
                        CustomToast.showToast(PayMessageActivity.this, "短信已发送，请注意查收", Toast.LENGTH_SHORT);
                        getYzm.setEnabled(false);
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                hd.sendEmptyMessage(TIME_FADE);
                            }
                        }, 0, 1000);
                        key = s;
                        break;
                }

            } else if (msg.what == SURE_PAY) {

                String s = msg.getData().getString("s");
                switch (s) {
                    case "ok":
                        CustomToast.showToast(PayMessageActivity.this, "转款成功！", Toast.LENGTH_SHORT);
                        setResult(0x121);
                        finish();
                        break;
                    case "haserr":
                        CustomToast.showToast(PayMessageActivity.this, "操作失败", Toast.LENGTH_SHORT);
                        break;
                    case "noitem":
                        CustomToast.showToast(PayMessageActivity.this, "无效项目", Toast.LENGTH_SHORT);
                        break;
                    case "yzmerr":
                        CustomToast.showToast(PayMessageActivity.this, "验证码输入错误", Toast.LENGTH_SHORT);
                        break;
                    case "noyzm":
                        CustomToast.showToast(PayMessageActivity.this, "请提交验证码", Toast.LENGTH_SHORT);
                        break;
                    case "notel":
                        CustomToast.showToast(PayMessageActivity.this, "请提交手机电话号码", Toast.LENGTH_SHORT);
                        break;
                    case "unlogin":
                        CustomToast.showToast(PayMessageActivity.this, "请先登录", Toast.LENGTH_SHORT);
                        break;
                    case "nocyids":
                        CustomToast.showToast(PayMessageActivity.this, "没有参与稿件", Toast.LENGTH_SHORT);
                        break;
                }

            } else if (msg.what == NETWORKEXCEPTION) {
                LhtTool.showNetworkException(PayMessageActivity.this, msg);
            } else if (msg.what == TIME_FADE) {
                i--;
                if (i == 0) {
                    getYzm.setText("获取验证码");
                    getYzm.setEnabled(true);
                    timer.cancel();
                    i = 120;
                    return;
                }
                getYzm.setText("剩余" + i + "秒");
            }

            if (ld != null) {
                ld.dismiss();
            }
        }
    };

}
