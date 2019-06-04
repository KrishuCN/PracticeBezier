package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import chen.vike.c680.bean.ItemInfoGson;
import chen.vike.c680.bean.ShopInfoGson;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.LoadingDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/15.
 *
 * 立即雇佣
 *
 */

public class ImmediateGyActivity extends BaseStatusBarActivity{

    private Map<String, Object> map = new HashMap<>();
    private ShopInfoGson shopInfoGson;
    private View shop;
    private ImageView mShop_logo;
    private TextView mShopName;
    private TextView fws_cjjl;
    private TextView fws_zl;
    private TextView fwxq_sd;
    private TextView fwxq_td;
    private EditText mContent;
    private EditText fbxq_phone;
    private EditText fbxq_ys;
    private Button button;
    private LoadingDialog ld;
    private Bundle bundle;
    private ItemInfoGson itemInfoGson;
    private final int SHOP_INFO = 0x123;
    private final int NETWORKEXCEPTION = 0X111;
    private final int GUYONG_FUWU = 0X121;
    private final int GET_ITEM_INFO = 0X122;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.immediategy_activity);

        getTitle().setText("雇佣服务商");
        iniView();//初始化
        //下面这个需要判断用户是否登录，没有登录则不能进行着两行代码
        isLogin();
         viewListener();//控件监听事件
    }

    /**
     * 初始化
     */
    private void iniView(){
        shop = findViewById(R.id.shop);
        mShop_logo =  findViewById(R.id.fws_logo);
        mShopName =  findViewById(R.id.fws_name);
        fws_cjjl =  findViewById(R.id.fws_cjjl);
        fws_zl =  findViewById(R.id.fws_zl);
        fwxq_sd = findViewById(R.id.fwxq_sd);
        fwxq_td = findViewById(R.id.fwxq_td);
        mContent = findViewById(R.id.xq_content);
        fbxq_ys = findViewById(R.id.fbxq_ys);
        fbxq_phone =  findViewById(R.id.fbxq_phone);
        button =  findViewById(R.id.control);

        bundle = getIntent().getExtras();
    }
    /**
     * 用户是否登录   加载数据
     */
    private void isLogin(){
        if (LhtTool.isLogin) {
            if(MyApplication.userInfo.getIs_verify_phone().equals("1")){
                fbxq_phone.setText(MyApplication.userInfo.getCellPhone());
            }
        } else {
            CustomToast.showToast(this, "请先登录", Toast.LENGTH_LONG);
            Intent intent = new Intent(this, UserLoginActivity.class);
            startActivity(intent);
        }
        map.put("apptype", "an");
        if (bundle.containsKey("ID")) {
            map.put("userid", bundle.getString("ID"));
            OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GET_SHOP_INFO, map, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LhtTool.sendMessage(hd,e,NETWORKEXCEPTION);
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        String s = response.body().string();
                        LogUtils.d("===============response:"+s);
                        shopInfoGson = new Gson().fromJson(s, ShopInfoGson.class);
                        hd.sendEmptyMessage(SHOP_INFO);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            ld = new LoadingDialog(ImmediateGyActivity.this).setMessage("加载中....");
            ld.show();
        }
    }
    /**
     * view控件监听事件
     */
    private void viewListener(){
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImmediateGyActivity.this, ShopDetailsActivity.class);
                intent.putExtra("ID", shopInfoGson.getUserid());
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContent.getText().toString().isEmpty()) {
                    CustomToast.showToast(ImmediateGyActivity.this, "请简要描述您的需求~", Toast.LENGTH_LONG);
                } else {
                    if (fbxq_ys.getText().toString().isEmpty()) {
                        CustomToast.showToast(ImmediateGyActivity.this, "金额不能为空", Toast.LENGTH_LONG);
                    } else {
                        if (LhtTool.isLogin) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("userid", MyApplication.userInfo.getUserID());
                            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                            map.put("vikeid", shopInfoGson.getUserid());
                            map.put("crv_title", "雇佣" + shopInfoGson.getShopname());
                            map.put("crv_content", mContent.getText());
                            map.put("money", fbxq_ys.getText());
                            map.put("data", 15);
                            if(fbxq_phone.getText().toString().isEmpty()){
                                CustomToast.showToast(ImmediateGyActivity.this, "电话号码不能为空", Toast.LENGTH_LONG);
                            }else{
                                map.put("mobile", fbxq_phone.getText().toString());
                                ld = new LoadingDialog(ImmediateGyActivity.this).setMessage("加载中....");
                                ld.show();
                                OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.GUYONG_FUWU, map, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                        LhtTool.sendMessage(hd,e,NETWORKEXCEPTION);

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        String s = response.body().string();
                                        LogUtils.d("==================response:"+s);
                                        Message ms = new Message();
                                        ms.what = GUYONG_FUWU;
                                        Bundle b = new Bundle();
                                        b.putString("s", s);
                                        ms.setData(b);
                                        hd.sendMessage(ms);

                                    }
                                });
                            }
                        } else {
                            Intent intent = new Intent(ImmediateGyActivity.this, UserLoginActivity.class);
                            startActivity(intent);
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
            if (ld != null) {
                ld.dismiss();
            }
            if (msg.what == SHOP_INFO) {
                Glide.with(ImmediateGyActivity.this).load(shopInfoGson.getImgurl()).into(mShop_logo);
                mShopName.setText(shopInfoGson.getShopname());
                fws_cjjl.setText(shopInfoGson.getCj_month3_money() + "元  " + shopInfoGson.getCj_month3_num() + "笔");
                fws_zl.setText(shopInfoGson.getPj_zhiliang());
                fwxq_sd.setText(shopInfoGson.getPj_xiaolv());
                fwxq_td.setText(shopInfoGson.getPj_taidu());

                if (bundle.containsKey("itemId")) {
                    map.put("itemId", bundle.getString("itemId"));
                    map.put("loginUserId", MyApplication.userInfo.getUserID());
                    OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.ITEM_INFO, map, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LhtTool.sendMessage(hd,e,NETWORKEXCEPTION);
                        }

                        @Override
                        public void onResponse(Call call, Response response) {
                            try {
                                String s = response.body().string();
                                LogUtils.d("=================response:" + response);
                                itemInfoGson = new Gson().fromJson(s, ItemInfoGson.class);
                                hd.sendEmptyMessage(GET_ITEM_INFO);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    });
                    ld = new LoadingDialog(ImmediateGyActivity.this).setMessage("加载中....");
                    ld.show();


                }

            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(ImmediateGyActivity.this, msg);

            } else if (msg.what == GUYONG_FUWU) {

                String s = msg.getData().getString("s");
                String[] strings = s.split(",");
                switch (strings[0]) {
                    case "self":
                        CustomToast.showToast(ImmediateGyActivity.this, "不能雇佣自己", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        CustomToast.showToast(ImmediateGyActivity.this, "雇佣成功", Toast.LENGTH_LONG);
                        Intent intent = new Intent(ImmediateGyActivity.this, FuKuanAfterActivity.class);
                        intent.putExtra("ID", strings[1]);
                        startActivity(intent);
                        finish();
                        break;
                    case "fail":
                        CustomToast.showToast(ImmediateGyActivity.this, "雇佣失败", Toast.LENGTH_LONG);
                        break;
                    case "nocon":
                        CustomToast.showToast(ImmediateGyActivity.this, "项目内容不能为空", Toast.LENGTH_LONG);
                        break;
                    case "nomoney":
                        CustomToast.showToast(ImmediateGyActivity.this, "雇佣金额无效", Toast.LENGTH_LONG);
                        break;
                    case "notitle":
                        CustomToast.showToast(ImmediateGyActivity.this, "项目标题不能为空", Toast.LENGTH_LONG);
                        break;
                    case "unlogin":
                        CustomToast.showToast(ImmediateGyActivity.this, "您还未登录", Toast.LENGTH_LONG);
                        break;
                    default:
                        break;
                }

            } else if (msg.what == GET_ITEM_INFO) {
                mContent.setText(Html.fromHtml(itemInfoGson.getItem_con()));
                fbxq_ys.setText(itemInfoGson.getItem_money());
            }
        }
    };

}
