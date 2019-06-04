package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/9.
 * <p>
 * 确认提现界面，该界面初始条件已经确认，所以提交就是直接提现了
 */

public class ReffectActivity extends BaseStatusBarActivity {

    private TextView username, blance_moneny, userID;
    private ImageView icon;
    private EditText password, effect_moneny;
    private Button but;
    private Map<String, Object> map = new HashMap<>();
    private final int TIXIANQUEREN = 0x123;
    private final int NETWORKEXCEPTION = 0x122;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reffect_activity);

        getTitle().setText("提现");
        effect_moneny = findViewById(R.id.effect_moneny);
        password = findViewById(R.id.password);
        username =  findViewById(R.id.userName);
        but =  findViewById(R.id.buttona);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确认提现，判断用户输入金额，输入密码是否符合要求
                if (!(effect_moneny.getText().toString().isEmpty()) || !(password.getText().toString().isEmpty())) {
                    Blance_tx();
                } else {
                    CustomToast.showToast(ReffectActivity.this, "请输入提现金额或密码", Toast.LENGTH_SHORT);
                }
            }
        });
        userID = findViewById(R.id.userId);
        blance_moneny = findViewById(R.id.mony_tx);
        icon = findViewById(R.id.imag_head);//用户头像
        username.setText("余额");
        if (MyApplication.userInfo != null) {
            userID.setText(MyApplication.userInfo.getNickame());
            blance_moneny.setText(MyApplication.userInfo.getBalance() + "元");
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.image_erroe)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(this).load(MyApplication.userInfo.getIcon()).apply(options).into(icon);
        effect_moneny = (EditText) findViewById(R.id.effect_moneny);
        password = (EditText) findViewById(R.id.password);


    }


    public void Blance_tx() {

        map.put("userid", MyApplication.userInfo.getUserID());
        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        map.put("tocashamount", effect_moneny.getText().toString());
        map.put("loginpwd", password.getText().toString());
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.SURE_TX_MONEY, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String s = response.body().string();
                LogUtils.d("===================Response:" + s);
                Message ms = new Message();
                ms.what = TIXIANQUEREN;
                Bundle b = new Bundle();
                b.putString("s", s);
                ms.setData(b);
                hd.sendMessage(ms);

            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TIXIANQUEREN) {

                String s = msg.getData().getString("s");
                switch (s) {
                    case "not_email_verifty":
                        CustomToast.showToast(ReffectActivity.this, "邮箱未被验证", Toast.LENGTH_LONG);
                        break;
                    case "not_fullname_verifty":
                        CustomToast.showToast(ReffectActivity.this, "实名未被验证", Toast.LENGTH_LONG);
                        break;
                    case "not_bank_verifty":
                        CustomToast.showToast(ReffectActivity.this, "你还未绑定银行卡", Toast.LENGTH_LONG);
                        break;
                    case "user_lock":
                        CustomToast.showToast(ReffectActivity.this, "你的卡被锁定，无法提现", Toast.LENGTH_LONG);
                        break;
                    case "not50":
                        CustomToast.showToast(ReffectActivity.this, "提现金额小于50元", Toast.LENGTH_LONG);
                        break;
                    case "notmoneyfull":
                        CustomToast.showToast(ReffectActivity.this, "提现金额大于余额", Toast.LENGTH_LONG);
                        break;
                    case "fail":
                        CustomToast.showToast(ReffectActivity.this, "申请提现失败", Toast.LENGTH_LONG);
                        break;
                    case "loginpderr":
                        CustomToast.showToast(ReffectActivity.this, "密码错误", Toast.LENGTH_LONG);
                        break;
                    case "nouid":
                        CustomToast.showToast(ReffectActivity.this, "用户无效", Toast.LENGTH_LONG);
                        break;
                    default:
                        String[] ss = s.split("=");
//                        String balance = ss[1];
                        if (ss[0].equals("ok")) {
                            CustomToast.showToast(ReffectActivity.this, "提现成功", Toast.LENGTH_LONG);
                            MyApplication.userInfo.setBalance(ss[1]);
                            blance_moneny.setText(ss[1] + "元");
                            setResult(0x333);
                            finish();
                        }
                        break;
                }


            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(ReffectActivity.this, msg);

            }
        }
    };

}
