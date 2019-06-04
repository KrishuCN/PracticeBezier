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
 * Created by lht on 2017/3/6.
 * 更改密码
 */

public class ChangePasswordActivity extends BaseStatusBarActivity{

    private EditText jmm;
    private EditText xmm;
    private EditText qrmm;
    private Button bt;
    private Map<String, Object> map = new HashMap<>();
    private final int NETWORK_EXCEPTION = 0X123;
    private final int UPDATE_PASSWORD = 0X111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        getTitle().setText("修改密码");
        iniView();//初始化
        viewListener();//控件监听事件
    }

    /**
     * 初始化
     */
    private void iniView(){
        jmm=  findViewById(R.id.oldpassword);
        xmm= findViewById(R.id.NewPassword);
        qrmm=  findViewById(R.id.confirm);
        bt =  findViewById(R.id.contorl);
    }

    /**
     * view控件监听事件
     */
    private void viewListener(){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jmm.getText().toString().isEmpty()){
                    CustomToast.showToast(ChangePasswordActivity.this,"请输入您之前的密码",Toast.LENGTH_LONG);
                }else{
                    if (xmm.getText().toString().isEmpty()||xmm.getText().toString().length()<6){
                        CustomToast.showToast(ChangePasswordActivity.this,"请输入（6-20位的新密码）",Toast.LENGTH_LONG);
                    }else{
                        if (xmm.getText().toString().contentEquals(qrmm.getText().toString())){

                            if (LhtTool.isLogin)
                            {

                                Map<String,Object> map=new HashMap<>();
                                map.put("userid", MyApplication.userInfo.getUserID());
                                map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                                map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                                map.put("crv_oldpass",jmm.getText());
                                map.put("crv_newpass",xmm.getText());
                                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.UPDATE_PASSWORD, map, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        String s = response.body().string();
                                        LogUtils.d("================Response:" + s);
                                        Message ms = new Message();
                                        Bundle b = new Bundle();
                                        b.putString("s", s);
                                        ms.setData(b);
                                        ms.what = UPDATE_PASSWORD;
                                        hd.sendMessage(ms);


                                    }
                                });

                            }


                        }else{

                            CustomToast.showToast(ChangePasswordActivity.this,"上下密码不一致",Toast.LENGTH_LONG);

                        }
                    }
                }
            }
        });

    }
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NETWORK_EXCEPTION) {

                LhtTool.showNetworkException(ChangePasswordActivity.this, msg);

            } else if (msg.what == UPDATE_PASSWORD) {

                String s = msg.getData().getString("s");
                switch (s) {
                    case "unlogin":
                        CustomToast.showToast(ChangePasswordActivity.this, "未登录", Toast.LENGTH_LONG);
                        break;
                    case "noold":
                        CustomToast.showToast(ChangePasswordActivity.this, "请填写旧登录密码", Toast.LENGTH_LONG);
                        break;
                    case "nonewd":
                        CustomToast.showToast(ChangePasswordActivity.this, "请填写新登录密码", Toast.LENGTH_LONG);
                        break;
                    case "newpdlen":
                        CustomToast.showToast(ChangePasswordActivity.this, "新密码长度不对(6-16)", Toast.LENGTH_LONG);
                        break;
                    case "fail":
                        CustomToast.showToast(ChangePasswordActivity.this, "修改失败", Toast.LENGTH_LONG);
                        break;
                    case "olderr":
                        CustomToast.showToast(ChangePasswordActivity.this, "旧登录密码错误", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        CustomToast.showToast(ChangePasswordActivity.this, "修改登录密码成功", Toast.LENGTH_LONG);
                        finish();
                        //有必要有这句话？
                        Intent intent = new Intent(ChangePasswordActivity.this, UserLoginActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                }



            }
        }
    };

}
