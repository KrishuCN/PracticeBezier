package chen.vike.c680.WXPay.WXApi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by Administrator on 2016/8/16.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册API
        api = WXAPIFactory.createWXAPI(this, "wxfc34fb9efbdedeae");
        api.handleIntent(getIntent(), this);
        Log.i("savedInstanceState"," sacvsa"+api.handleIntent(getIntent(), this));
    }
    @Override
    public void onReq(BaseReq baseReq) {

    }
    //  发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        if(resp instanceof SendAuth.Resp){
            SendAuth.Resp newResp = (SendAuth.Resp) resp;
            //获取微信传回的code
            String code = newResp.code;
            Log.i("newRespnewResp","   code    :"+code);

        }

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.i("savedInstanceState","发送成功ERR_OKERR_OK");
                //发送成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.i("savedInstanceState","发送取消ERR_USER_CANCEL");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.i("savedInstanceState","发送取消ERR_AUTH_DENIEDERR_AUTH_DENIEDERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                Log.i("savedInstanceState","发送返回breakbreakbreak");
                //发送返回
                break;
        }
        finish();

    }

}