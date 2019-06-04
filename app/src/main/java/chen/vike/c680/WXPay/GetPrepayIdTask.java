package chen.vike.c680.WXPay;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import chen.vike.c680.views.LoadingDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by Mr.Z on 2015/11/23.
 */
public class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

    private LoadingDialog ld;
    private String TAG = GetPrepayIdTask.class.getName();

    private Activity context;
    private String out_trade_no; //商户订单号
    private String body; //商品描述
    private String total_fee; //总金额
    private IWXAPI msgApi;
    private PayReq req;


    public GetPrepayIdTask(Activity context, String out_trade_no, String body, String total_fee) {
        this.context = context;
        this.out_trade_no = out_trade_no;
        this.body = body;
        this.total_fee = String.valueOf((int) (Float.valueOf(total_fee) * 100f));
        Log.v(TAG, "总金额：" + this.total_fee);
        req = new PayReq();
        msgApi = WXAPIFactory.createWXAPI(context, null);
    }

    @Override
    protected void onPreExecute() {

        ld = new LoadingDialog(context).setMessage("订单获取中...");
        ld.show();
    }

    @Override
    protected void onPostExecute(Map<String, String> result) {
        if (ld != null) {
            ld.dismiss();
        }
        if (result != null && result.get("prepay_id") != null) {

            if (result.get("prepay_id").equals("") || result.get("prepay_id").isEmpty()) {

            } else {
                genPayReq(result);
            }
        }


    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Map<String, String> doInBackground(Void... params) {

        String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
        String entity = genProductArgs();


        byte[] buf = Util.httpPost(url, entity);

        String content = new String(buf);
        Map<String, String> xml = decodeXml(content);

        return xml;
    }

    private Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if (!"xml".equals(nodeName)) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
        }
        return null;

    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();

            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", body));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://app.680.com/api/weixin_server.aspx"));
            packageParams.add(new BasicNameValuePair("out_trade_no", out_trade_no));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", total_fee));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            return toXml(packageParams);
        } catch (Exception e) {
            Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");
        Log.e(TAG, sb.toString());
        try {
            return new String(sb.toString().getBytes(), "ISO8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e(TAG, packageSign);
        return packageSign;
    }

    private void genPayReq(Map<String, String> result) {

        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = result.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        req.transaction = String.valueOf(System.currentTimeMillis());
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        Log.v(TAG, signParams.toString());
        sendPayReq();
    }

    private void sendPayReq() {
        Log.v(TAG, "启动支付");
        boolean c = msgApi.registerApp(Constants.APP_ID);
        boolean b = msgApi.sendReq(req);
        Log.v(TAG, c + " " + b);
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        Log.v(TAG, "sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.v(TAG, appSign);
        return appSign;
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

}

