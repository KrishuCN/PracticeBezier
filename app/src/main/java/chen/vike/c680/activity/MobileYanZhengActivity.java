package chen.vike.c680.activity;

import android.annotation.SuppressLint;
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
 * Created by lht on 2017/3/8.
 * 手机验证
 */

public class MobileYanZhengActivity extends BaseStatusBarActivity{


    private EditText mYzm;
    private Button mControl1;
    private Button mControl2;
    private Map<String, Object> map = new HashMap<>();
    private Bundle bundle;
    private String key;
    private EditText mPhone;
    private long exitTime=0;
    private long exitTime2=0;
    private final int GET_MESSAGE = 0x111;
    private final int NETWORK_EXCEPTION = 0x112;
    private final int TIJIAOYANZHENG = 0x113;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_mobilephone_two);
        getTitle().setText("手机验证");
        iniView();//初始化
        viewListener();//控件监听事件
    }

    /**
     * 初始化
     */
    private void iniView(){
        mYzm = findViewById(R.id.yzm);
        mPhone = findViewById(R.id.phoneNumber);
        mControl1 = findViewById(R.id.control1);
        mControl2 = findViewById(R.id.control2);
        if (MyApplication.userInfo != null && MyApplication.userInfo.getCellPhone() != null) {
            if (!MyApplication.userInfo.getCellPhone().equals("")) {
                mPhone.setText(MyApplication.userInfo.getCellPhone());
            }
        }
        bundle = getIntent().getExtras();
    }
    /**
     * view控件监听事件
     */
    private void viewListener(){
        mControl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mControl1) {
                    if (mPhone.getText().toString().isEmpty()) {
                        if ((System.currentTimeMillis() - exitTime) > 4000) {
                            CustomToast.showToast(MobileYanZhengActivity.this, "手机号码不能为空", Toast.LENGTH_LONG);
                            exitTime = System.currentTimeMillis();
                        }

                    } else if (mPhone.getText().toString().length() != 11) {
                        if ((System.currentTimeMillis() - exitTime) > 4000) {
                            CustomToast.showToast(MobileYanZhengActivity.this, "请输入11位手机号", Toast.LENGTH_LONG);
                            exitTime = System.currentTimeMillis();
                        }
                    } else {
                        map.put("mobile", mPhone.getText());
                        map.put("key", UrlConfig.MOBILE_VERIFICATION_KEY);
                        map.put("type", "verify");
                        map.put("check", "1");
                        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.MOBILE_MESSAGE_VERIFICATION, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                String s = response.body().string();
                                LogUtils.d("======================Response:" + s);
                                Message ms = new Message();
                                ms.what = GET_MESSAGE;
                                Bundle b = new Bundle();
                                b.putString("s", s);
                                ms.setData(b);
                                hd.sendMessage(ms);


                            }
                        });
                    }
                }
            }
        });

        mControl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhone.getText().toString().isEmpty()){
                    if ((System.currentTimeMillis() - exitTime) > 4000) {
                        CustomToast.showToast(MobileYanZhengActivity.this, "手机号码不能为空", Toast.LENGTH_LONG);
                        exitTime = System.currentTimeMillis();
                    }
                }else {

                    if (mYzm.getText().toString().isEmpty()) {
                        if ((System.currentTimeMillis() - exitTime2) > 4000) {
                            CustomToast.showToast(MobileYanZhengActivity.this, "验证码不能为空", Toast.LENGTH_LONG);
                            exitTime2 = System.currentTimeMillis();
                        }

                    } else {
                        /**
                         * userid 用户ID
                         * vkuserip 登录cookieIPT
                         * vktoken 登录加密串
                         * mobile 表示手机号码
                         * yzm 表示输入的验证码
                         * key 表示先前请求发送手机短信时服务器端反馈的加密串
                         */
                        if (LhtTool.isLogin) {
//                        if (bundle != null) {
                            map.put("userid", MyApplication.userInfo.getUserID());
                            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                            map.put("mobile", mPhone.getText().toString());
                            map.put("key", key);
//                        }
                        }
                        map.put("yzm", mYzm.getText());
                        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.MOBILE_IS_SURE, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                                LhtTool.sendMessage(hd,e,NETWORK_EXCEPTION);

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                String s = response.body().string();
                                LogUtils.d("======================Response:" + s);
                                Message ms = new Message();
                                ms.what = TIJIAOYANZHENG;
                                Bundle b = new Bundle();
                                b.putString("s", s);
                                ms.setData(b);
                                hd.sendMessage(ms);


                            }
                        });
                    }
                }
            }
        });

    }
    @SuppressLint("HandlerLeak")
    private Handler hd=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_MESSAGE) {

                String s = msg.getData().getString("s");
                switch (s) {
                    case "has_check":
                        CustomToast.showToast(MobileYanZhengActivity.this, "该手机已经被认证过了", Toast.LENGTH_LONG);
                        break;
                    case "":
                        CustomToast.showToast(MobileYanZhengActivity.this, "发送失败", Toast.LENGTH_LONG);
                        break;
                    case "false":
                        CustomToast.showToast(MobileYanZhengActivity.this, "发送失败", Toast.LENGTH_LONG);
                        break;
                    default:
                        CustomToast.showToast(MobileYanZhengActivity.this, "短信发送成功，请注意查收", Toast.LENGTH_LONG);
                        key= String.valueOf(s);
                        break;
                }

            } else if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(MobileYanZhengActivity.this,msg);

            } else if (msg.what == TIJIAOYANZHENG) {

                String s = msg.getData().getString("s");
                switch (s) {
                    case "unlogin":
                        CustomToast.showToast(MobileYanZhengActivity.this, "未登录", Toast.LENGTH_LONG);
                        break;
                    case "notdata":
                        CustomToast.showToast(MobileYanZhengActivity.this, "无效数据", Toast.LENGTH_LONG);
                        break;
                    case "erryzm":
                        CustomToast.showToast(MobileYanZhengActivity.this, "验证码错误", Toast.LENGTH_LONG);
                        break;
                    case "errphone":
                        CustomToast.showToast(MobileYanZhengActivity.this, "手机号无效", Toast.LENGTH_LONG);
                        break;
                    case "fail":
                        CustomToast.showToast(MobileYanZhengActivity.this, "手机认证失败", Toast.LENGTH_LONG);
                        break;
                    case "err_notsame_phone":
                        CustomToast.showToast(MobileYanZhengActivity.this, "提交的手机号和注册资料中的不一致", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        CustomToast.showToast(MobileYanZhengActivity.this, "手机认证成功", Toast.LENGTH_LONG);
                        MyApplication.userInfo.setIs_verify_phone("1");
                        MyApplication.userInfo.setCellPhone(mPhone.getText().toString());
                        finish();
                        break;
                    case "hasverify":
                        CustomToast.showToast(MobileYanZhengActivity.this, "该手机号已认证", Toast.LENGTH_LONG);
                        break;
                }
            }
        }
    };

}
