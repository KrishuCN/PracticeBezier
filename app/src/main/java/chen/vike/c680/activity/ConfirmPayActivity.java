package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chen.vike.c680.bean.ZKInfoGson;
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
 * Created by lht on 2017/5/13.
 *
 * 确认付款
 */

public class ConfirmPayActivity extends BaseStatusBarActivity {

    private TextView biaoti;
    private TextView time;
    private Button sure_zk;
    private LoadingDialog ld;
    private RecyclerView recyclerView;
    private TextView tvNum;
    private Intent in;
    private List<Boolean> list1 = new ArrayList<>();
    private Map<String, Object> map = new HashMap<>();
    private Map<String, Object> map1 = new HashMap<>();
    private ZKInfoGson zkInfoGson;
    private List<ZKInfoGson.ZhongbiaoGaojianListBean> list = new ArrayList<>();
    private DisplayMetrics metrics = new DisplayMetrics();
    private Myadapter myadapter;
    private final static int GET_INFO = 0X112;
    private final static int NETWORK_EXCEPTION = 0x121;
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(ConfirmPayActivity.this, msg);
            } else if (msg.what == GET_INFO) {

                if (null != zkInfoGson) {
                    map1.put("shouji", zkInfoGson.getItemInfo().getShouji());
                    biaoti.setText(zkInfoGson.getItemInfo().getItemname());
                    SpannableString ss = new SpannableString("待确认方案（" + zkInfoGson.getZhongbiao_gaojian_list().size() + "）");
                    RelativeSizeSpan res = new RelativeSizeSpan(0.8f);
                    ss.setSpan(res, 5, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    tvNum.setText(ss);
                    time.setText("发布于：" + zkInfoGson.getItemInfo().getAddtime());
                    list.clear();
                    list.addAll(zkInfoGson.getZhongbiao_gaojian_list());
                    myadapter.notifyDataSetChanged();
                }
            }
            if (ld != null) {
                ld.dismiss();
            }
        }
    };


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setContentView(R.layout.confirm_pay_activity);

        getTitle().setText("确认转款");
        ld = new LoadingDialog(this).setMessage("加载中....");
        ld.show();

        biaoti =  findViewById(R.id.qrfk_title);
        time =  findViewById(R.id.qrfk_time);
        tvNum =  findViewById(R.id.qrfk_tv2);
        recyclerView = findViewById(R.id.qrfk_lv);
        sure_zk = findViewById(R.id.qrfk_bt);

        if (LhtTool.isLogin) {

            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map.put("itemid", getIntent().getStringExtra("ID"));

            map1.put("userid", MyApplication.userInfo.getUserID());
            map1.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map1.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map1.put("itemid", getIntent().getStringExtra("ID"));

        }

        surePay();
        myadapter = new Myadapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);
        sure_zk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                in = new Intent(ConfirmPayActivity.this, PayMessageActivity.class);
                in.putExtra("phone", zkInfoGson.getItemInfo().getShouji());
                in.putExtra("itemid", zkInfoGson.getItemInfo().getItemid());
                boolean cyFlag = false;
                for (int i = 0; i < list1.size(); i++) {
                    if (list1.get(i)) {
                        cyFlag = true;
                        if (i == 0) {
                            in.putExtra("cyids", zkInfoGson.getZhongbiao_gaojian_list().get(i).getCyid());
                        } else {
                            String s = in.getStringExtra("cyids");
                            s += "," + zkInfoGson.getZhongbiao_gaojian_list().get(i).getCyid();
                            in.putExtra("cyids", s);
                        }
                    }
                }
                if (!cyFlag) {
                    CustomToast.showToast(ConfirmPayActivity.this,"请选择转款的稿件",Toast.LENGTH_SHORT);
                    return;
                }
                startActivityForResult(in,0x111);
            }
        });
    }

    private void surePay() {

        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.USER_NO_ZHUANKUAN, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String s = response.body().string();
                    LogUtils.d("==================response:" + s);
                    zkInfoGson = new Gson().fromJson(s, ZKInfoGson.class);
                    hd.sendEmptyMessage(GET_INFO);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

    private void setRank(String name, ImageView iv) {
        switch (name) {
            case "一等奖":
                iv.setImageResource(R.mipmap.ydj);
                break;

            case "二等奖":
                iv.setImageResource(R.mipmap.edj);
                break;

            case "三等奖":
                iv.setImageResource(R.mipmap.sdj);
                break;
            case "中标":
                iv.setImageResource(R.mipmap.zb);
                break;

        }
    }


    private class Myadapter extends RecyclerView.Adapter<Myadapter.MyHolder> {

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(ConfirmPayActivity.this).inflate(R.layout.qrfk_recyc_item, null));
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {

            holder.itemView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(ConfirmPayActivity.this).load(list.get(position).getImageurl()).apply(options).into(holder.image);
            holder.name.setText(list.get(position).getUsername_vk());
            setRank(list.get(position).getZhongbiao(), holder.rank);
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (holder.cb.isChecked()) {
                        list1.set(position, true);
                    } else {
                        list1.set(position, false);
                    }
                }
            });

            if (list1.get(position)) {
                holder.cb.setChecked(true);
            } else {
                holder.cb.setChecked(false);
            }

        }

        @Override
        public int getItemCount() {

            if (list == null) {
                return 0;
            }

            for (int i = 0; i < list.size(); i++) {
                list1.add(false);
            }

            return list.size();
        }

        class MyHolder extends RecyclerView.ViewHolder {

            private CheckBox cb;
            private ImageView image;
            private TextView name;
            private ImageView rank;

            public MyHolder(View itemView) {
                super(itemView);
                cb = (CheckBox) itemView.findViewById(R.id.qrfk_gou);
                image = (ImageView) itemView.findViewById(R.id.qrfk_image);
                name = (TextView) itemView.findViewById(R.id.qrfk_name);
                rank = (ImageView) itemView.findViewById(R.id.qrfk_randk);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x111 && resultCode == 0x121) {
            surePay();
            for (int i = 0; i < list1.size(); i++) {
                list1.set(i, false);
            }
            myadapter.notifyDataSetChanged();
            ld = new LoadingDialog(this).setMessage("加载中....");
            ld.show();
        }
    }
}
