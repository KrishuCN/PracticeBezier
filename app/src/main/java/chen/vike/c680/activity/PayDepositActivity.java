package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/5/27.
 *
 * 支付定金
 *
 */

public class PayDepositActivity extends BaseStatusBarActivity{

    private EditText djje;
    private Button nextStep;
    private final int PUSH_MONEY = 0x111;
    private final int NETWORK_EXCEPTION = 0x121;
    private Map<String, Object> map = new HashMap<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paydeposit_activity);

        getTitle().setText("设置定金金额");
        djje = findViewById(R.id.djje);
        nextStep = findViewById(R.id.nextStep);

        //为保险写的判定，其实能发项目已经确定是登录的了，只是怕掉了
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map.put("itemid", getIntent().getStringExtra("ID"));
        }

        if (getIntent().getExtras().containsKey("MONEY")) {
            djje.setText(getIntent().getStringExtra("MONEY"));
        }

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(djje.getText().toString())) {
                    CustomToast.showToast(PayDepositActivity.this, "请设置支付定金的金额！", Toast.LENGTH_SHORT);
                } else if (Integer.valueOf(djje.getText().toString()) < 50) {
                    CustomToast.showToast(PayDepositActivity.this, "支付定金的金额不能小于50元！", Toast.LENGTH_SHORT);
                } else {
                    map.put("dingjin", djje.getText().toString());
                    getInfo();
                }
            }
        });

    }


    private void getInfo() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.SET_DEPOSIT, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                LogUtils.d("=================response:" + s);
                Message ms = new Message();
                ms.what = PUSH_MONEY;
                Bundle b = new Bundle();
                b.putString("s", s);
                ms.setData(b);
                hd.sendMessage(ms);
            }
        });
    }
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PUSH_MONEY) {
                String s = msg.getData().getString("s");
                switch (s) {
                    case "ok":
                        Intent in = new Intent(PayDepositActivity.this, FuKuanActivity.class);
                        in.putExtra("ID", getIntent().getStringExtra("ID"));
                        startActivity(in);
                        finish();
                        break;
                    case "itemfail":
                        CustomToast.showToast(PayDepositActivity.this,"此项目当前状态不能设置定金了",Toast.LENGTH_SHORT);
                        break;
                    case "noitemid":
                        CustomToast.showToast(PayDepositActivity.this,"无效项目id",Toast.LENGTH_SHORT);
                        break;
                    case "unlogin":
                        CustomToast.showToast(PayDepositActivity.this,"未登录，请登录后操作",Toast.LENGTH_SHORT);
                        break;
                    case "dingjinerr":
                        CustomToast.showToast(PayDepositActivity.this,"定金需大于50",Toast.LENGTH_SHORT);
                        break;
                }
            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(PayDepositActivity.this, msg);
            }
        }
    };

}
