package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.LoadingDialog;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2018/4/18.
 */

public class WeiKeFabuTimeServiceActivity extends BaseStatusBarActivity {

    @BindView(R.id.sell_text_category1)
    TextView sellTextCategory1;
    @BindView(R.id.sell_text_category2)
    TextView sellTextCategory2;
    @BindView(R.id.sell_text_category3)
    TextView sellTextCategory3;
    @BindView(R.id.selllinear_category)
    LinearLayout selllinearCategory;
    @BindView(R.id.shijian_i_can_text)
    EditText shijianICanText;
    @BindView(R.id.shijian_i_can_spinner)
    Spinner shijianICanSpinner;
    @BindView(R.id.sell_price)
    EditText sellPrice;
    @BindView(R.id.spinner_sprice)
    Spinner spinnerSprice;
    @BindView(R.id.shijian_address)
    EditText shijianAddress;
    @BindView(R.id.sell_numtext)
    TextView sellNumtext;
    @BindView(R.id.sell_edt)
    EditText sellEdt;
    @BindView(R.id.linear_edt)
    LinearLayout linearEdt;
    @BindView(R.id.sell_commit)
    Button sellCommit;
    @BindView(R.id.ll)
    LinearLayout ll;
    private String service_sell, ican_service, address, service_content, price;
    private Context context;
    private String[] IDJI = {"169", "174", "190", "201", "205", "206", "250", "194", "200", "203"};
    private int positionNum = 0;
    private static final int SUCCESS = 0x123;
    private LoadingDialog ld;
    private CharSequence temp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shijian_fuwu_page);
        ButterKnife.bind(this);
        context = this;
        getTitle().setText("发布时间服务");
        setSpinnerText();
        setNumberText();
    }

    /**
     * 设置选择框
     */
    private void setSpinnerText() {
        ArrayAdapter<CharSequence> province_adapter = ArrayAdapter.createFromResource(this, R.array.shijian_service_sell, android.R.layout.simple_spinner_item);
        province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSprice.setAdapter(province_adapter);

        ArrayAdapter<CharSequence> ican_adapter = ArrayAdapter.createFromResource(this, R.array.shijian_service_i_can, android.R.layout.simple_spinner_item);
        ican_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shijianICanSpinner.setAdapter(ican_adapter);
        viewListener();
    }

    private void setNumberText() {
        TextWatcher edt = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                sellNumtext.setText(String.valueOf(count + 1));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                sellNumtext.setText(String.valueOf(temp.length()));
            }
        };
        sellEdt.addTextChangedListener(edt);
    }

    /**
     * 控件监听事件
     */
    private void viewListener() {
        spinnerSprice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                service_sell = spinnerSprice.getSelectedItem().toString();
                //Toast.makeText(context,service_sell,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shijianICanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ican_service = shijianICanSpinner.getSelectedItem().toString();
                positionNum = position;
                //Toast.makeText(context,ican_service+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sellCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = shijianAddress.getText().toString() + "";
                price = sellPrice.getText().toString() + "";
                service_content = sellEdt.getText().toString() + "";
                if (address.equals("")) {
                    Toast.makeText(context, "请输入地址", Toast.LENGTH_SHORT).show();
                } else if (price.equals("")) {
                    Toast.makeText(context, "请输入价格", Toast.LENGTH_SHORT).show();
                } else if (service_content.equals("")) {
                    Toast.makeText(context, "请输入服务详情", Toast.LENGTH_SHORT).show();

                } else {
                    httpData();
                }
            }
        });
    }

    private void httpData() {
        String URL = "http://app.680.com/api/v4/shijian_fabu.ashx";
        Map<String, Object> map = new HashMap<>();
        map.put("userid", MyApplication.userInfo.getUserID());
        map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        map.put("cid", IDJI[positionNum]);
        map.put("price", price);

        String[] a = service_sell.split("/");
        map.put("unit", a[1]);
        // map.put("unit",service_sell);
        map.put("address", address);
        map.put("content", service_content);
        map.put("apptype", "an");
        ld = new LoadingDialog(WeiKeFabuTimeServiceActivity.this).setMessage("提交中....");
        ld.show();
        OkhttpTool.Companion.getOkhttpTool().post(URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Log.e("shijianfuwu", s);
                Message ms = new Message();
                ms.what = SUCCESS;
                Bundle b = new Bundle();
                b.putString("s", s);
                ms.setData(b);
                // MHandler.sendMessage(ms);
                handler.sendMessage(ms);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    String s = msg.getData().getString("s");
                    if (ld != null) {
                        ld.dismiss();
                    }
                    switch (s) {
                        case "unlogin":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "未登录", Toast.LENGTH_LONG);
                            break;
                        case "unclass1id":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "请选择分类", Toast.LENGTH_LONG);
                            break;
                        case "unclass2id":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "请选择分类", Toast.LENGTH_LONG);
                            break;
                        case "has":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "服务标题已存在，你已发不过此类服务", Toast.LENGTH_LONG);
                            break;
                        case "okedit":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "修改服务成功", Toast.LENGTH_LONG);
                            break;
                        case "failedit":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "修改服务失败", Toast.LENGTH_LONG);
                            break;
                        case "ok":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "发布成功", Toast.LENGTH_LONG);
                            // finish();
                            startActivity(new Intent(WeiKeFabuTimeServiceActivity.this, MyServiceActivity.class));
                            finish();
                            break;
                        case "fail":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "发布失败", Toast.LENGTH_LONG);
                            break;
                        case "noshopid":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "你还未开通店铺不能发布服务", Toast.LENGTH_LONG);
                            break;
                        case "nocon":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "请填写服务详情", Toast.LENGTH_LONG);
                            break;
                        case "nounit":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "请选择单位", Toast.LENGTH_LONG);
                            break;
                        case "nobzpic":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "请上传一张封面图片", Toast.LENGTH_LONG);
                            break;
                        case "notitle":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "请先输入标题", Toast.LENGTH_LONG);
                            break;
                        case "noprice":
                            CustomToast.showToast(WeiKeFabuTimeServiceActivity.this, "请先输入价格", Toast.LENGTH_LONG);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
