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
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import chen.vike.c680.bean.ItemInfoGson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/16.
 *
 * 增加悬赏
 */

public class PriceMakUpActivity extends BaseStatusBarActivity{

    private TextView name;
    private TextView money;
    private TextView prompt;
    private EditText jine;
    private EditText data;
    private Button button;
    private int num;
    private String id;
    private Map<String, Object> map = new HashMap<>();
    private ItemInfoGson itemInfoGson;
    private final int GET_INFO = 0x123;
    private final int ADD_PRICE = 0x133;
    private final int NETWORKEXCEPTION = 0x111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_price_activity);

        getTitle().setText("增加悬赏");
        name =  findViewById(R.id.name);
        money =  findViewById(R.id.now_money);
        id = getIntent().getStringExtra("ID");
        if (id != null) {
            map.put("itemId", id);
            /**
             * * userid 用户ID
             * vkuserip 登录cookieIPT
             * vktoken 登录加密串
             * projectId 项目ID
             */
            if (LhtTool.isLogin) {
                map.put("loginUserId", MyApplication.userInfo.getUserID());
                map.put("userid", MyApplication.userInfo.getUserID());
                map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                map.put("projectId", id);
            }
            OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.ITEM_INFO, map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    LhtTool.sendMessage(hd,e,NETWORKEXCEPTION);

                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        String s = response.body().string();
                        LogUtils.d("=============response:"+s);
                        itemInfoGson = new Gson().fromJson(s, ItemInfoGson.class);
                        hd.sendEmptyMessage(GET_INFO);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }
        prompt = findViewById(R.id.prompt);
        jine = findViewById(R.id.jine);
        data = findViewById(R.id.data);
        button = findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jine.getText().toString().isEmpty()) {
                    CustomToast.showToast(PriceMakUpActivity.this, "金额不能为空", Toast.LENGTH_LONG);

                } else {
                    if (Integer.valueOf(jine.getText().toString()) >= num) {
                        ArrayList<String> list = new ArrayList<>();
                        list.add(0, id);
                        list.add(1, jine.getText().toString());
                        list.add(2, data.getText().toString());
                        Intent intent = new Intent(PriceMakUpActivity.this, FuKuanActivity.class);
                        intent.putExtra("ZJXS", list);
                        startActivity(intent);

                    } else {
                        CustomToast.showToast(PriceMakUpActivity.this, "金额不能低于" + num, Toast.LENGTH_LONG);
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
            if (msg.what == GET_INFO) {

                name.setText(itemInfoGson.getItem_name());
                if (itemInfoGson.getItem_class1id().equals("6")) {
                    money.setText("￥" + itemInfoGson.getItem_money());

                } else {
                    if (Integer.valueOf(itemInfoGson.getItem_type()) == 1) {

                        if (itemInfoGson.getItem_zab_yusuan1().equals(itemInfoGson.getItem_zab_yusuan2())) {
                            money.setText("￥" + itemInfoGson.getItem_zab_yusuan1());

                        } else {
                            money.setText("￥" + itemInfoGson.getItem_zab_yusuan1() + "-￥" + itemInfoGson.getItem_zab_yusuan2());
                        }
                    } else {
                        money.setText("￥" + itemInfoGson.getItem_money());
                    }
                }
                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_ITEM_ADD_TOPAY, map, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LhtTool.sendMessage(hd,e,NETWORKEXCEPTION);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String s = response.body().string();
                        LogUtils.d("===============response:"+s);
                        Message ms = new Message();
                        ms.what = ADD_PRICE;
                        Bundle b = new Bundle();
                        b.putString("s", s);
                        ms.setData(b);
                        hd.sendMessage(ms);

                    }
                });

            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(PriceMakUpActivity.this, msg);

            } else if (msg.what == ADD_PRICE) {
                String s = msg.getData().getString("s");
                if (s.equals("noitem")) {
                    CustomToast.showToast(PriceMakUpActivity.this, "不能增加赏金，原项目金额0无效", Toast.LENGTH_SHORT);
                } else {

                    num = Integer.valueOf(s.trim());
                    prompt.setText("提醒：加价金额不能少于项目金额的10%，本次不低于" + num + "元");
                }
            }
        }
    };

}
