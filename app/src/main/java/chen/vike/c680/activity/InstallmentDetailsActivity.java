package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import chen.vike.c680.bean.FenQiDetailsGson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.LoadingDialog;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/6/5.
 * <p>
 * 分期详情
 */

public class InstallmentDetailsActivity extends BaseStatusBarActivity {

    private ListView lv;
//    private Button bt;
    private FenQiDetailsGson fenQiDetailsGson;
    private Map<String, Object> map = new HashMap<>();
    private List<FenQiDetailsGson.FenqiListBean> list = new ArrayList<>();
    private LoadingDialog ld;
    private MyAdapter myAdapter;
    private final int FENQI_INFO = 0x123;
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FENQI_INFO) {
                if (null != fenQiDetailsGson) {
                    list = fenQiDetailsGson.getFenqi_list();
                    myAdapter.notifyDataSetChanged();

//                    bt.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            Intent intent = new Intent(InstallmentDetailsActivity.this, FuKuanActivity.class);
//                            intent.putExtra("ID", list.get(0).getItemid());
//                            intent.putExtra("ZB", "11");
//                            startActivityForResult(intent,1);
//
//                        }
//                    });
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
        setContentView(R.layout.installment_details_activity);

        getTitle().setText("分期详情");
        lv =  findViewById(R.id.fenqi_details_lv);
//        bt = (Button) findViewById(R.id.fenqi_details_qbtj);

        ld = new LoadingDialog(InstallmentDetailsActivity.this).setMessage("加载中...");
        ld.show();
        getFenQiInfo();
        myAdapter = new MyAdapter();
        lv.setAdapter(myAdapter);


    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        ID id;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null || convertView.getTag() == null) {
                convertView = LayoutInflater.from(InstallmentDetailsActivity.this).inflate(R.layout.fenqi_details_item, null);
                id = new ID();
                id.num =  convertView.findViewById(R.id.fenqi_details_num);
                id.money =  convertView.findViewById(R.id.fenqi_details_money);
                id.content =  convertView.findViewById(R.id.fenqi_details_content);
                id.bt =  convertView.findViewById(R.id.fenqi_details_bt);
                id.bt1 =  convertView.findViewById(R.id.fenqi_details_bt1);
                convertView.setTag(id);
            } else {
                id = (ID) convertView.getTag();
            }

            String s = null;
            switch (list.get(position).getFenqishu()) {
                case "1":
                    s = "一";
                    break;
                case "2":
                    s = "二";
                    break;
                case "3":
                    s = "三";
                    break;
                case "4":
                    s = "四";
                    break;
                case "5":
                    s = "五";
                    break;
                default:
                    s = "一";
                    break;
            }

            id.num.setText("第" + s + "期：");
            id.money.setText("￥ " + list.get(position).getFenqi_topay_money());
            id.content.setText("进度要求：" + list.get(position).getBeizhu());
            if (list.get(position).getIstuoguan().equals("0")) {
                id.bt.setText("托管本期");
                id.bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(InstallmentDetailsActivity.this, FuKuanActivity.class);
                        in.putExtra("ID", list.get(position).getItemid());
                        in.putExtra("FENQI", list.get(position).getFenqi_id());
                        in.putExtra("ZB", "11");
                        startActivityForResult(in,1);
                    }
                });
            } else {
                id.bt.setText("已托管");
                id.bt1.setVisibility(View.VISIBLE);
            }

            if (list.get(position).getIstuoguan().equals("1")) {
                if (list.get(position).getIszhuankuan().equals("0")) {
                    id.bt1.setText("确认转款");
                    id.bt1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //跳转到确认转款页面
                            Intent intent = new Intent(InstallmentDetailsActivity.this, PayMessageActivity.class);
                            if ("".equals(list.get(position).getShouji())) {
                                CustomToast.showToast(InstallmentDetailsActivity.this, "未绑定手机，请联系客服：400-680-6300 解决", Toast.LENGTH_SHORT);
                                return;
                            } else {
                                intent.putExtra("phone", list.get(position).getShouji());
                                intent.putExtra("ID", list.get(position).getFenqi_id());
                                intent.putExtra("itemid", list.get(position).getItemid());
                                intent.putExtra("FENQI", "1");
                                startActivityForResult(intent, 1);
                            }
                        }
                    });
                } else {
                    id.bt1.setText("已转账");
                    id.bt1.setClickable(false);
                }
            } else {
                id.bt1.setVisibility(View.GONE);
            }


            return convertView;
        }

        class ID {
            private TextView num;
            private TextView money;
            private TextView content;
            private Button bt;
            private Button bt1;
        }

    }

    private void getFenQiInfo() {
        map.put("userid", MyApplication.userInfo.getUserID());
        map.put("itemid", getIntent().getStringExtra("ID"));
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.FENQI_DETAILS, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String s = response.body().string();
                    LogUtils.d("==============response:" + s);
                    fenQiDetailsGson = new Gson().fromJson(s, FenQiDetailsGson.class);
                    hd.sendEmptyMessage(FENQI_INFO);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getFenQiInfo();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
