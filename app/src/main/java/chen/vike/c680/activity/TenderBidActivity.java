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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import chen.vike.c680.bean.ItemInfoGson;
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
 * Created by lht on 2017/6/6.
 *
 * 招标中标
 *
 */

public class TenderBidActivity extends BaseStatusBarActivity{

    private TextView type;
    private TextView id;
    private TextView money;
    private TextView zhuangtai;
    private TextView zb_title;
    private TextView content;
    private ImageView image;
    private TextView name;
    private Button chatWith;
    private Button fenQi_Bt;
    private Button quanBu_Bt;
    private LinearLayout ll;
    private LinearLayout zb_fws;
    private ItemInfoGson itemInfoGson;
    private Map<String, Object> map = new HashMap<>();
    private final int GETZHAOBIAOINFO = 0x123;
    private final int NETWORKEXCEPTION = 0x122;
    private LoadingDialog ld;
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GETZHAOBIAOINFO) {

                if (null != itemInfoGson) {
                    id.setText("项目编号：" + itemInfoGson.getItem_id());
                    money.setText("￥ " + itemInfoGson.getZhaobiao_money());
                    String shState = null;
                    String check = itemInfoGson.getItem_check();
                    String djState = null;
                    String pay = itemInfoGson.getItem_ispay();
                    if (check.equals("0")) {
                        shState = "未审核";
                    } else if (check.equals("1")) {
                        shState = "审核未通过";
                    } else if (Integer.valueOf(check)>1) {
                        shState = "已审核";
                    }

                    if (pay.equals("0")) {
                        djState = "定金未支付";
                    } else if (Integer.valueOf(pay) > 0) {
                        djState = "定金已支付";
                    }

                    zhuangtai.setText("订单状态：" + shState + "/" + djState);

                    zb_title.setText(itemInfoGson.getItem_name());
                    content.setText(Html.fromHtml(itemInfoGson.getItem_con()));
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.mipmap.image_loading)
                            .error(R.mipmap.image_erroe)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);

                    Glide.with(TenderBidActivity.this).load(itemInfoGson.getZb_vkimageurl()).apply(options).into(image);
                    name.setText(itemInfoGson.getZb_vkname());

                    if (itemInfoGson.getItem_hasfenqi().equals("1")) {
                        fenQi_Bt.setText("分期托管");
                        fenQi_Bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(TenderBidActivity.this, InstallmentDetailsActivity.class);
                                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                                startActivityForResult(intent,1);
                            }
                        });
                    } else {
                        fenQi_Bt.setText("我要分期");
                        fenQi_Bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(TenderBidActivity.this, InstallmentSettingActivity.class);
                                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                                startActivityForResult(intent,1);
                            }
                        });
                    }

                    //由于scrollview与底部有间距，所以即使底部按钮隐藏下面还是会空一块，这个后面需要调整
                    if (itemInfoGson.getZhaobiao_pay().equals("1")) {
                        ll.setVisibility(View.GONE);
                    } else {
                        ll.setVisibility(View.VISIBLE);
                    }

                    chatWith.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TenderBidActivity.this, MessageChatActivity.class);
                            intent.putExtra("ID", itemInfoGson.getZb_vkuid());
                            intent.putExtra("name", itemInfoGson.getZb_vkname());
                            startActivity(intent);
                        }
                    });

                    zb_fws.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemInfoGson.getZb_has_shop().equals("1")) {
                                Intent intent = new Intent(TenderBidActivity.this, GeRenZhongXinShopDetailsActivity.class);
                                intent.putExtra("ID", itemInfoGson.getZb_vkuid());
                                startActivity(intent);
                            } else {
                                CustomToast.showToast(TenderBidActivity.this,"该服务商还未开店，查看不了店铺哦~", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }

            } else if (msg.what == NETWORKEXCEPTION) {
                LhtTool.showNetworkException(TenderBidActivity.this, msg);
            }

            if (ld != null) {
                ld.dismiss();
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tender_bid_activity);

        getTitle().setText("中标详情");
        type = (TextView) findViewById(R.id.zb_type);
        id = (TextView) findViewById(R.id.zb_id);
        money = (TextView) findViewById(R.id.zb_money);
        zhuangtai = (TextView) findViewById(R.id.zb_zhuangtai);
        zb_title = (TextView) findViewById(R.id.zb_title);
        content = (TextView) findViewById(R.id.zb_content);
        image = (ImageView) findViewById(R.id.zb_image);
        name = (TextView) findViewById(R.id.zb_fws);
        chatWith = (Button) findViewById(R.id.zb_bt_chat);
        fenQi_Bt = (Button) findViewById(R.id.zb_fneqi);
        quanBu_Bt = (Button) findViewById(R.id.zb_quanbu);
        zb_fws = (LinearLayout) findViewById(R.id.zb_fws_ll);
        ll = (LinearLayout) findViewById(R.id.zb_db_ll);

        map.put("itemId", getIntent().getStringExtra("ID"));
        map.put("loginUserId", MyApplication.userInfo.getUserID());
        getItemInfo();
        ld = new LoadingDialog(TenderBidActivity.this).setMessage("加载中...");
        ld.show();

        quanBu_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TenderBidActivity.this, FuKuanActivity.class);
                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                intent.putExtra("ZB", "11");
                startActivityForResult(intent,1);
            }
        });

    }


    public void getItemInfo() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.ITEM_INFO, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd,e,NETWORKEXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d(" =================response:" + s);
                    itemInfoGson = new Gson().fromJson(s, ItemInfoGson.class);
                    hd.sendEmptyMessage(GETZHAOBIAOINFO);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
