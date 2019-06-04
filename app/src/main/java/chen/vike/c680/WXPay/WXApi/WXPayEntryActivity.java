package chen.vike.c680.WXPay.WXApi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import chen.vike.c680.main.MainActivity;
import chen.vike.c680.WXPay.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.text.DecimalFormat;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                try {
                    float f = Float.valueOf(10) + 5;
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//                    MyApplication.getInstance().userBean.setBalance(decimalFormat.format(f));
                }catch (RuntimeException e){
                }

                Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(WXPayEntryActivity.this, MainActivity.class);
                intent.putExtra("ME","ME");
                startActivity(intent);
            } else if (resp.errCode == -1) {
                Toast.makeText(this, "微信支付失败，请确认安装了微信！", Toast.LENGTH_LONG).show();
            } else if (resp.errCode == -2) {
                Toast.makeText(this, "取消支付", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "取消支付", Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }
}