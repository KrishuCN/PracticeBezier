package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import chen.vike.c680.bean.ZBALLMoneyGson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.LoadingDialog;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/6/1.
 * 分期设置
 *
 */

public class InstallmentActivity extends BaseStatusBarActivity {

    private ListView lv;
    private Button bt;
    private Map<String, Object> map = new HashMap<>();
    private final int GET_INFO = 0x123;
    private final int GET_REWARD_INFO = 0x122;
    private final int NETWORK_EXCEPTION = 0x121;
    private LoadingDialog ld;
    private ZBALLMoneyGson zballMoneyGson;
    private TextView zb_dj_money;
    private TextView zb_dj_zhuangtai;
    private TextView zb_sj_money;
    private TextView zb_fenqi_payall_money;
    private final Map<Integer, String> map_flag = new HashMap<>();
    private final Map<Integer, String> map_flag1 = new HashMap<>();
    private BaseAdapter baseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.installment_activity);

        getTitle().setText("分期设置");

        iniView();//初始化
        getRewardInfo();
        listGetAdapter();//listview的适配器
        lv.setAdapter(baseAdapter);
        viewListener();//控件监听事件
    }

    /**
     * 初始化
     */
   private void iniView(){
       lv =  findViewById(R.id.fenqi_lv);
       bt = findViewById(R.id.fenqi_shezhi_bt);
       zb_dj_money = findViewById(R.id.zb_dj_money);
       zb_sj_money = findViewById(R.id.zb_sj_money);
       zb_dj_zhuangtai = findViewById(R.id.zb_dj_zhuangtai);
       zb_fenqi_payall_money = findViewById(R.id.zb_fenqi_payall_money);
   }
    /**
     * view控件监听事件
     */
   private void viewListener(){
       bt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               for (int i = 0; i < Integer.valueOf(getIntent().getStringExtra("NUMBER")); i++) {
                   LogUtils.d("==================map_flag.get("+i+"):" + map_flag.get(Integer.valueOf(i)));
                   LogUtils.d("==================map_flag1.get("+i+"):" + map_flag1.get(Integer.valueOf(i)));
                   if (null == map_flag.get(Integer.valueOf(i)) || "".equals(map_flag.get(Integer.valueOf(i)))) {
                       CustomToast.showToast(InstallmentActivity.this, "请输入第" + (i + 1) + "期的金额", Toast.LENGTH_SHORT);
                       break;
                   } else if (null == map_flag1.get(Integer.valueOf(i)) || "".equals(map_flag1.get(Integer.valueOf(i)))) {
                       CustomToast.showToast(InstallmentActivity.this, "请输入第" + (i + 1) + "期的要求", Toast.LENGTH_SHORT);
                       break;
                   }

                   if (i == Integer.valueOf(getIntent().getStringExtra("NUMBER")) - 1) {
                       getInfo();
                       ld = new LoadingDialog(InstallmentActivity.this).setMessage("数据提交中...");
                       ld.show();
                   }
               }
           }
       });

   }

    /**
     * listview的适配器
     */
   private void listGetAdapter(){
       baseAdapter = new BaseAdapter() {
           @Override
           public int getCount() {
               return Integer.valueOf(getIntent().getStringExtra("NUMBER"));
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
                   convertView = LayoutInflater.from(InstallmentActivity.this).inflate(R.layout.fenqi_item, null);
                   id = new ID();
                   id.tv = (TextView) convertView.findViewById(R.id.fenqi_num);
                   id.et = (EditText) convertView.findViewById(R.id.fenqi_money);
                   id.et1 = (EditText) convertView.findViewById(R.id.fenqi_content);
                   convertView.setTag(id);


               } else {
                   id = (ID) convertView.getTag();
               }

               id.tv.setText("第" + (position + 1) + "期");

               id.et.setTag(position);//给edittext设置一个标记
               id.et1.setTag(position);
               id.et.clearFocus();//清楚焦点，不清楚的话因为item复用的原因，多个edittext会同时改变
               id.et1.clearFocus();

               //用final关键字把id.et锁住，就不会造成数据混乱了
               final EditText et = id.et;
               final EditText et1 = id.et1;
               id.et.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                   }

                   @Override
                   public void onTextChanged(CharSequence s, int start, int before, int count) {

                   }

                   @Override
                   public void afterTextChanged(Editable s) {
                       LogUtils.d("====================posi:"+position);
                       map_flag.put((Integer) (et.getTag()), s.toString());
                   }
               });

               id.et1.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                   }

                   @Override
                   public void onTextChanged(CharSequence s, int start, int before, int count) {

                   }

                   @Override
                   public void afterTextChanged(Editable s) {
                       LogUtils.d("====================posi1:"+position);
                       map_flag1.put((Integer) (et1.getTag()), s.toString());
                   }
               });


               if (!TextUtils.isEmpty(map_flag.get(position))) {
                   id.et.setText(map_flag.get(position));
               } else {
                   id.et.setText("");
               }

               if (!TextUtils.isEmpty(map_flag1.get(position))) {
                   id.et1.setText(map_flag1.get(position));
               } else {
                   id.et1.setText("");
               }

               for (int i = 0; i < Integer.valueOf(getIntent().getStringExtra("NUMBER")); i++) {
                   LogUtils.d("===============map_flag.get(i):"+i+":"+ map_flag.get(i));
                   LogUtils.d("===============map_flag1.get(i):" +i+":"+ map_flag1.get(i));
               }
               return convertView;
           }

           class ID {
               private TextView tv;
               private EditText et;
               private EditText et1;
           }
       };

   }
    /**
     * 分期总金额用户等数据提交
     */
    private void getInfo() {
        map.put("userid", MyApplication.userInfo.getUserID());
        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());

        map.put("itemid", getIntent().getStringExtra("ID"));
        map.put("seltype", getIntent().getStringExtra("NUMBER"));
        for (int i = 0; i < Integer.valueOf(getIntent().getStringExtra("NUMBER")); i++) {
            map.put("fenqi" + (i + 1) + "_money", map_flag.get(i));
            map.put("crv_fenqi" + (i + 1) + "_note", map_flag1.get(i));
        }
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.SET_FENQI, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hd.sendEmptyMessage(NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String s = response.body().string();
                LogUtils.d("===================response:" + s);
                Message ms = new Message();
                ms.what = GET_INFO;
                Bundle b = new Bundle();
                b.putString("s", s);
                ms.setData(b);
                hd.sendMessage(ms);

            }
        });
    }

    /**
     * 分期列表信息
     */
    private void getRewardInfo() {
        map.put("itemid", getIntent().getStringExtra("ID"));
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.ZHAOBIAO_ALLMONEY, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hd.sendEmptyMessage(NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    LogUtils.d("====================response:" + s);
                    zballMoneyGson = new Gson().fromJson(s, ZBALLMoneyGson.class);
                    hd.sendEmptyMessage(GET_REWARD_INFO);
                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });
        ld = new LoadingDialog(InstallmentActivity.this).setMessage("加载中....");
        ld.show();
    }

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_INFO) {

                String s = msg.getData().getString("s");
                switch (s) {
                    case "ok":
                        CustomToast.showToast(InstallmentActivity.this, "设置分期付款成功", Toast.LENGTH_SHORT);
                        finish();
                        break;
                    case "notfulldata":
                        CustomToast.showToast(InstallmentActivity.this, "请填写完整金额和备注后提交", Toast.LENGTH_SHORT);
                        break;
                    case "has":
                        CustomToast.showToast(InstallmentActivity.this, "此项目已设置过分期付款", Toast.LENGTH_SHORT);
                        break;
                    case "fail":
                        CustomToast.showToast(InstallmentActivity.this, "系统繁忙，提交失败", Toast.LENGTH_SHORT);
                        break;
                    case "itemzhongbiao":
                        CustomToast.showToast(InstallmentActivity.this, "该项目未评标，请评标后在进行操作", Toast.LENGTH_SHORT);
                        break;
                    case "splitvmtterr":
                        CustomToast.showToast(InstallmentActivity.this, "分期付款金额与总赏金不一致", Toast.LENGTH_SHORT);
                        break;
                    case "nodatatopay":
                        CustomToast.showToast(InstallmentActivity.this, "该项目支付金额无效", Toast.LENGTH_SHORT);
                        break;
                    case "itemfail":
                        CustomToast.showToast(InstallmentActivity.this, "该项目不能设置分期付款", Toast.LENGTH_SHORT);
                        break;
                    case "noitemid":
                        CustomToast.showToast(InstallmentActivity.this, "无效项目id", Toast.LENGTH_SHORT);
                        break;
                    case "unlogin":
                        CustomToast.showToast(InstallmentActivity.this, "未登录，请登录后操作", Toast.LENGTH_SHORT);
                        startActivity(new Intent(InstallmentActivity.this, UserLoginActivity.class));
                        break;
                    case "itemunfag":
                        CustomToast.showToast(InstallmentActivity.this, "项目审核未通过,请等待审核通过后在提交", Toast.LENGTH_SHORT);
                        break;
                }

            } else if (msg.what == NETWORK_EXCEPTION) {
                CustomToast.showToast(InstallmentActivity.this, "数据请求失败，请退出重试！", Toast.LENGTH_SHORT);
            } else if (msg.what == GET_REWARD_INFO) {
                if (null != zballMoneyGson) {

                    zb_dj_money.setText(zballMoneyGson.getData().getDingjin_money()+"元");
                    zb_sj_money.setText(zballMoneyGson.getData().getTotal_money()+"元");
                    String s = zballMoneyGson.getData().getPayok();
                    if (s.equals("1")) {
                        zb_dj_zhuangtai.setText("（已支付）");
                    } else {
                        zb_dj_zhuangtai.setText("（未支付）");
                    }
                    zb_fenqi_payall_money.setText(zballMoneyGson.getData().getNeed_pay_money()+"元");

                }
            }

            if (ld != null) {
                ld.dismiss();
            }
        }
    };

}
