package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.main.MyApplication;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chen.vike.c680.bean.FaPiaoBean;
import chen.vike.c680.tools.ConfigTools;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/11/7.
 */

public class FaPiaoActivity extends BaseStatusBarActivity {

    @BindView(R.id.fapiao_back)
    TextView fapiaoBack;
    @BindView(R.id.fapiao_taitou)
    EditText fapiaoTaitou;
    @BindView(R.id.naishui_number)
    EditText naishuiNumber;
    @BindView(R.id.fapaio_content)
    EditText fapaioContent;
    @BindView(R.id.shoujian_name)
    EditText shoujianName;
    @BindView(R.id.shoujian_address)
    EditText shoujianAddress;
    @BindView(R.id.email_number)
    EditText emailNumber;
    @BindView(R.id.lianxi_number)
    EditText lianxiNumber;
    @BindView(R.id.fapiao_fin)
    Button fapiaoFin;
    private List<String> faLists = new ArrayList<>();
    private static final String FPURL = "http://app.680.com/api/v3/fapiao_get.ashx";
    private FaPiaoBean infoBean = new FaPiaoBean();
    private final int NETWORK_EXCEPTION = 0X111;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fapiao_edit);
        ButterKnife.bind(this);
        fHttpData();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    fapiaoTaitou.setText(infoBean.getFaPiaoInfo().getFptt() + "");
                    naishuiNumber.setText(infoBean.getFaPiaoInfo().getFp_nsr_code() + "");
                    fapaioContent.setText(infoBean.getFaPiaoInfo().getFpnr() + "");
                    shoujianName.setText(infoBean.getFaPiaoInfo().getFpsjr() + "");
                    shoujianAddress.setText(infoBean.getFaPiaoInfo().getFpsjdz() + "");
                    emailNumber.setText(infoBean.getFaPiaoInfo().getFpyzbm() + "");
                    lianxiNumber.setText(infoBean.getFaPiaoInfo().getFplxdh() + "");
                    break;
                case NETWORK_EXCEPTION:
                    LhtTool.showNetworkException(FaPiaoActivity.this, msg);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 页面数据
     */
    private void dataFaP() {
        String taitou = fapiaoTaitou.getText().toString();
        String naishuiNum = naishuiNumber.getText().toString();
        String faContent = fapaioContent.getText().toString();
        String shouName = shoujianName.getText().toString();
        String shouAddress = shoujianAddress.getText().toString();
        String emailNum = emailNumber.getText().toString();
        String lianxiNum = lianxiNumber.getText().toString();
        faLists.add(taitou);
        faLists.add(naishuiNum);
        faLists.add(faContent);
        faLists.add(shouName);
        faLists.add(shouAddress);
        faLists.add(emailNum);
        faLists.add(lianxiNum);
        ConfigTools.faPiaoList.addAll(faLists);
        showDia();
    }

    /**
     * 是否确认弹窗
     */
    public void showDia() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否提交")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public void fHttpData() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", MyApplication.userInfo.getUserID());
        OkhttpTool.Companion.getOkhttpTool().post(FPURL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    Log.e("fapmessage", s);
                    infoBean = new Gson().fromJson(s, FaPiaoBean.class);
                    handler.sendEmptyMessage(1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.fapiao_back, R.id.fapiao_fin, R.id.fapiao_taitou, R.id.naishui_number, R.id.fapaio_content, R.id.shoujian_name, R.id.shoujian_address, R.id.email_number, R.id.lianxi_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fapiao_back:
                finish();
                break;
            case R.id.fapiao_fin:
                dataFaP();
                break;
            case R.id.fapiao_taitou:
                break;
            case R.id.naishui_number:
                break;
            case R.id.fapaio_content:
                break;
            case R.id.shoujian_name:
                break;
            case R.id.shoujian_address:
                break;
            case R.id.email_number:
                break;
            case R.id.lianxi_number:
                break;
        }
    }
}
