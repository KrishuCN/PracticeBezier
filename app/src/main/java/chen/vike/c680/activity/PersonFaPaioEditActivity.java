package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import chen.vike.c680.main.BaseActivity;
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
import chen.vike.c680.bean.ErrorBean;
import chen.vike.c680.bean.FaPiaoEditBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/11/7.
 * 编辑发票页面
 */

public class PersonFaPaioEditActivity extends BaseActivity {

    @BindView(R.id.fapiao_back)
    ImageView fapiaoBack;
    @BindView(R.id.fapiao_fin)
    TextView fapiaoFin;
    @BindView(R.id.fapiao_taitou)
    EditText fapiaoTaitou;
    @BindView(R.id.naishui_number)
    EditText naishuiNumber;
    @BindView(R.id.fapaio_content)
    TextView fapaioContent;
    @BindView(R.id.shoujian_name)
    EditText shoujianName;
    @BindView(R.id.shoujian_address)
    EditText shoujianAddress;
    @BindView(R.id.email_number)
    EditText emailNumber;
    @BindView(R.id.lianxi_number)
    EditText lianxiNumber;
    @BindView(R.id.wuli_message)
    EditText wuliMessage;
    @BindView(R.id.shenqing_time)
    EditText shenqingTime;
    private List<String> faLists = new ArrayList<>();
    private static final String FPURL = "http://app.680.com/api/v4/get_invoice.ashx";
    private static final String EDITFP = "http://app.680.com/api/v4/edit_invoice.ashx";
    private FaPiaoEditBean faPiaoEditBean = new FaPiaoEditBean();
    private String fpid = "";
    private final static int JIAZAIDATA = 010;
    private final static int ERROR = 000;
    private final static int FANHUIDATA = 020;
    private final static int FANHUIERROR = 022;
    private final int NETWORK_EXCEPTION = 0X111;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fap_list_show);
        ButterKnife.bind(this);
        context = this;
        fpid = getIntent().getStringExtra("fpid");
        fHttpData();
    }

    /**
     * 页面数据
     */
    String taitou, naishuiNum, faContent, shouName, shouAddress, emailNum, lianxiNum, wuliu, time;

    private void dataFaP() {
        taitou = fapiaoTaitou.getText().toString();
        naishuiNum = naishuiNumber.getText().toString();
        faContent = fapaioContent.getText().toString();
        shouName = shoujianName.getText().toString();
        shouAddress = shoujianAddress.getText().toString();
        emailNum = emailNumber.getText().toString();
        lianxiNum = lianxiNumber.getText().toString();
        wuliu = wuliMessage.getText().toString();
        time = shenqingTime.getText().toString();
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
                        editFin();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 编辑内容提交
     */
    private ErrorBean errorBean = new ErrorBean();

    private void editFin() {
        Map<String, Object> tiMap = new HashMap<>();
        tiMap.put("act", "edit");
        tiMap.put("userid", MyApplication.userInfo.getUserID());
        tiMap.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
        tiMap.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
        tiMap.put("id", fpid);
        tiMap.put("crv_fptt_v", taitou);
        tiMap.put("fp_nsrcode", naishuiNum);
        tiMap.put("crv_fpsjr_v", shouName);
        tiMap.put("crv_fpsjdz_v", shouAddress);
        tiMap.put("crv_fpyzbm_v", emailNum);
        tiMap.put("crv_fplxdh_v", lianxiNum);
        Log.e("323", taitou + shouName + naishuiNum + shouAddress + emailNum + lianxiNum);
        OkhttpTool.Companion.getOkhttpTool().post(EDITFP, tiMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response)  {
                try {
                    String s = response.body().string();
                    Log.e("223", s);
                    errorBean = new Gson().fromJson(s, ErrorBean.class);

                    if (errorBean.getErr_code().equals("0")) {
                        handler.sendEmptyMessage(FANHUIDATA);
                    } else {
                        handler.sendEmptyMessage(FANHUIERROR);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 获取发票内容
     */
    Map<String, Object> map = new HashMap<>();

    public void fHttpData() {
        if (LhtTool.isLogin) {
            map.put("userid", MyApplication.userInfo.getUserID());
            map.put("vkuserip", MyApplication.userInfo.getCookieLoginIpt());
            map.put("vktoken", MyApplication.userInfo.getCookieLoginToken());
            map.put("fpid", fpid);
        } else {
            map.put("userid", 0);
        }
        OkhttpTool.Companion.getOkhttpTool().post(FPURL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(handler, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String s = response.body().string();
                    Log.e("faedit", s);
                    faPiaoEditBean = new Gson().fromJson(s, FaPiaoEditBean.class);
                    if (faPiaoEditBean.getErr_code().equals("0")) {
                        handler.sendEmptyMessage(JIAZAIDATA);
                    } else {
                        handler.sendEmptyMessage(ERROR);
                    }
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
                if (faLists.size() > 0) {
                    faLists.clear();
                }
                dataFaP();
                break;

        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case JIAZAIDATA:
                    fapiaoTaitou.setText(faPiaoEditBean.getFpInfo().getFptt());
                    naishuiNumber.setText(faPiaoEditBean.getFpInfo().getFp_nsr_code() + "");
                    fapaioContent.setText(faPiaoEditBean.getFpInfo().getFpnr() + "");
                    shoujianName.setText(faPiaoEditBean.getFpInfo().getFpsjr() + "");
                    shoujianAddress.setText(faPiaoEditBean.getFpInfo().getFpsjdz() + "");
                    emailNumber.setText(faPiaoEditBean.getFpInfo().getFpyzbm() + "");
                    lianxiNumber.setText(faPiaoEditBean.getFpInfo().getFplxdh() + "");
                    wuliMessage.setText(faPiaoEditBean.getFpInfo().getFpfype() + "");
                    shenqingTime.setText(faPiaoEditBean.getFpInfo().getAddtime());
                    break;
                case ERROR:
                    Toast.makeText(context, faPiaoEditBean.getErr_msg() + "", Toast.LENGTH_SHORT).show();
                    break;
                case FANHUIDATA:
                    Toast.makeText(context, errorBean.getErr_msg() + "", Toast.LENGTH_SHORT).show();
                    break;
                case FANHUIERROR:
                    Toast.makeText(context, errorBean.getErr_msg() + "", Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_EXCEPTION:
                    LhtTool.showNetworkException(PersonFaPaioEditActivity.this, msg);
                    break;
                default:
                    break;
            }
        }
    };
}
