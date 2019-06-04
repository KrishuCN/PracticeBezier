package chen.vike.c680.WXPay;

import com.tencent.mm.opensdk.openapi.IWXAPI;

public class Constants {
    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    public static final String APP_ID = "wxfc34fb9efbdedeae";
    //商户号
    public static final String MCH_ID = "1289720501";
    //  API密钥，在商户平台设置
    public static final String API_KEY = "d4624c36b6795d1d99dcf0547af5443d";

    public static final String APP_SECRET = "51c56b886b5be869567dd389b3e5d1d6";
    public static IWXAPI wx_api;
}
