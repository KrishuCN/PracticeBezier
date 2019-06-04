package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
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
 * Created by lht on 2017/3/13.
 *
 * 开通店铺页面
 */

public class OpenShopActivity extends BaseStatusBarActivity {

    private Map<String, Object> map = new HashMap<>();
    private RadioButton checkBox1, checkBox2, checkBoxg;//单选按钮
    private EditText shop_name, shop_describle;//店铺名称,店铺描述
    private Button shop_nowBut;
    private EditText shop_phone;//电话号码
    private Spinner province_spinner;
    private Spinner city_spinner;
    private int[] city = {R.array.beijin_province_item, R.array.tianjin_province_item, R.array.heibei_province_item, R.array.shanxi1_province_item, R.array.neimenggu_province_item, R.array.liaoning_province_item, R.array.jilin_province_item, R.array.heilongjiang_province_item, R.array.shanghai_province_item, R.array.jiangsu_province_item, R.array.zhejiang_province_item, R.array.anhui_province_item, R.array.fujian_province_item, R.array.jiangxi_province_item, R.array.shandong_province_item, R.array.henan_province_item, R.array.hubei_province_item, R.array.hunan_province_item, R.array.guangdong_province_item, R.array.guangxi_province_item, R.array.hainan_province_item, R.array.chongqing_province_item, R.array.sichuan_province_item, R.array.guizhou_province_item, R.array.yunnan_province_item, R.array.xizang_province_item, R.array.shanxi2_province_item, R.array.gansu_province_item, R.array.qinghai_province_item, R.array.linxia_province_item, R.array.xinjiang_province_item, R.array.hongkong_province_item, R.array.aomen_province_item, R.array.taiwan_province_item};
    private TextView shop_na;
    private LinearLayout open_shop_skill, open_shop_sk, shop_linrar;
    private GridView gridView;
    private String stringcheck;
    private String opencity, openprivence;
    private String s1;
    private LinearLayout foucuse_linear;
    private final int GETINFO = 0x123;
    private final int NETWORKEXCEPTION = 0X111;
    private LoadingDialog ld;
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GETINFO) {

                if (ld != null) {
                    ld.dismiss();
                }
                String s = msg.getData().getString("s");
                switch (s){
                    case "nouser":
                        CustomToast.showToast(OpenShopActivity.this, "用户不存在", Toast.LENGTH_LONG);
                        break;
                    case "stop":
                        CustomToast.showToast(OpenShopActivity.this, "账户被禁用", Toast.LENGTH_LONG);
                        break;
                    case "noverify":
                        CustomToast.showToast(OpenShopActivity.this, "用户未进行实名认证", Toast.LENGTH_LONG);
                        break;
                    case "noshopnam":
                        CustomToast.showToast(OpenShopActivity.this, "未提供店铺名称", Toast.LENGTH_LONG);
                        break;
                    case "isshop":
                        CustomToast.showToast(OpenShopActivity.this, "店铺已开通", Toast.LENGTH_LONG);
                        break;
                    case "noprovname":
                        CustomToast.showToast(OpenShopActivity.this, "未提供省份名称", Toast.LENGTH_LONG);
                        break;
                    case "nocityname":
                        CustomToast.showToast(OpenShopActivity.this, "未提供城市名称", Toast.LENGTH_LONG);
                        break;
                    case "hasshopname":
                        CustomToast.showToast(OpenShopActivity.this, "店铺名称已存在", Toast.LENGTH_LONG);
                        break;
                    case "noshopdes":
                        CustomToast.showToast(OpenShopActivity.this, "未提店铺简介", Toast.LENGTH_LONG);
                        break;
                    case "ok":
                        MyApplication.userInfo.setIsShop("1");
                        CustomToast.showToast(OpenShopActivity.this, "店铺开通成功", Toast.LENGTH_LONG);
                        finish();
                        break;
                    case "fail":
                        CustomToast.showToast(OpenShopActivity.this, "提交失败", Toast.LENGTH_LONG);
                        break;
                }



            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(OpenShopActivity.this, msg);

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.openshop_activity);

        getTitle().setText("开通店铺");
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBoxg = findViewById(R.id.checkBoxg);
        shop_name = findViewById(R.id.shop_name);
        shop_describle = findViewById(R.id.shop_describle);
        shop_nowBut = findViewById(R.id.shop_nowBut);
        province_spinner = findViewById(R.id.province_spinner);
        city_spinner = findViewById(R.id.city_spinner);
        open_shop_skill = findViewById(R.id.open_shop_skill);
        foucuse_linear = findViewById(R.id.foucuse_linear);
        shop_linrar = findViewById(R.id.shop_linrar);

        shop_na = findViewById(R.id.shop_na);
        open_shop_sk = findViewById(R.id.open_shop_sk);
        gridView = findViewById(R.id.gridView);
        shop_phone =  findViewById(R.id.shop_phone);
        shop_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        shop_nowBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openshop();
            }
        });
        open_shop_skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenShopActivity.this, OpenShopSkillsActivity.class);
                startActivityForResult(intent, 10);
            }
        });
        foucuse_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop_describle.requestFocus();
                InputMethodManager imm = (InputMethodManager) shop_describle.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });

        if (LhtTool.isLogin) {
            if (MyApplication.userInfo.getCellPhone().length() >= 4) {
                s1 = MyApplication.userInfo.getCellPhone().substring(0, 4);
                shop_phone.setText(s1 + "******");
                shop_phone.setFocusable(false);
            }
        } else {
            shop_phone.setFocusable(true);
        }

        ArrayAdapter<CharSequence> province_adapter = ArrayAdapter.createFromResource(this, R.array.province_item, android.R.layout.simple_spinner_item);
        province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province_spinner.setAdapter(province_adapter);
        province_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                opencity = province_spinner.getSelectedItem().toString();
                select(city_spinner, city[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                openprivence = city_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void select(Spinner spin, int arry) {
        //这儿也可以直接new ArrayAdapter，效果一样的
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arry, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }

    public void checkboxlinsonear() {
        if (checkBox1.isChecked()) {
            stringcheck = checkBox1.getText().toString();
        } else if (checkBox2.isChecked()) {
            stringcheck = checkBox2.getText().toString();
        } else if (checkBoxg.isChecked()) {
            stringcheck = checkBoxg.getText().toString();
        }

    }

    public void openshop() {
        if (!shop_name.getText().toString().isEmpty()) {
            if (!shop_describle.getText().toString().isEmpty()) {
                if (OpenShopSkillsActivity.nalist.size() > 0) {
                    if (!shop_phone.getText().toString().isEmpty()) {
                        //立即开店
                        checkboxlinsonear();
                        //店铺类型

                        if (LhtTool.isLogin) {
                            map.put("userid", MyApplication.userInfo.getUserID());
                            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
                            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
                            if (stringcheck.equals("个人")) {
                                map.put("shop_type", "10");
                            } else if (stringcheck.equals("工作室")) {
                                map.put("shop_type", "12");
                            } else {
                                map.put("shop_type", "13");
                            }

                            //店铺名称
                            map.put("shop_name", shop_name.getText().toString());
                            //店铺描述
                            map.put("shop_des", shop_describle.getText().toString());
                            //店铺技能
                            String s = null;
                            for (int i = 0; i < OpenShopSkillsActivity.xid.size(); i++) {
                                if (i == 0) {
                                    s = OpenShopSkillsActivity.xid.get(0);
                                } else {
                                    s = s + "," + OpenShopSkillsActivity.xid.get(i);
                                }

                            }
                            map.put("jineng", s);
                            if (MyApplication.userInfo.getCellPhone().isEmpty()) {
                                map.put("mobile", shop_phone.getText().toString());
                            } else {
                                map.put("mobile", "");
                            }
                            //地址
                            map.put("province", opencity);
                            map.put("city", openprivence);
                            OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.OPEN_SHOP, map, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                    LhtTool.sendMessage(hd,e,NETWORKEXCEPTION);

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                    String s = response.body().string();
                                    LogUtils.d("================response:"+s);
                                    Message ms = new Message();
                                    ms.what = GETINFO;
                                    Bundle b = new Bundle();
                                    b.putString("s",s);
                                    ms.setData(b);
                                    hd.sendMessage(ms);

                                }
                            });
                            ld = new LoadingDialog(this).setMessage("数据加载中...");
                        }

                    } else {
                        CustomToast.showToast(this, "请填写电话号码", Toast.LENGTH_SHORT);
                    }
                } else {
                    CustomToast.showToast(this, "请选择技能", Toast.LENGTH_SHORT);
                }
            } else {
                CustomToast.showToast(this, "请填写店铺的描述", Toast.LENGTH_SHORT);
            }
        } else {
            CustomToast.showToast(this, "请填写店铺的名称", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 10){
//                显示两个linear
            if(OpenShopSkillsActivity.nalist.size()==0){
                shop_linrar.setVisibility(View.GONE);
                open_shop_sk.setVisibility(View.GONE);
            }else{
                shop_linrar.setVisibility(View.VISIBLE);
                open_shop_sk.setVisibility(View.VISIBLE);
                shop_na.setText(OpenShopSkillsActivity.ti);
            }
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    BaseAdapter adapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return OpenShopSkillsActivity.nalist.size();
        }

        @Override
        public Object getItem(int position) {
            return OpenShopSkillsActivity.nalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHodler hodler;
            if(convertView==null){
                hodler=new ViewHodler();
                convertView= LayoutInflater.from(OpenShopActivity.this).inflate(R.layout.openshop_skill,parent,false);
                hodler.open_shop=(TextView) convertView.findViewById(R.id.open_shop);
                hodler.open_delete=(TextView) convertView.findViewById(R.id.open_delete);
                convertView.setTag(hodler);
            }else{
                hodler=(ViewHodler)convertView.getTag();
            }
            hodler.open_shop.setText(OpenShopSkillsActivity.nalist.get(position));
            hodler.open_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenShopSkillsActivity.nalist.remove(position);
                    if(OpenShopSkillsActivity.nalist.size()==0){
                        shop_linrar.setVisibility(View.GONE);
                        open_shop_sk.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
        class ViewHodler{
            TextView open_shop,open_delete;
        }
    };

}

