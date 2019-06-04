package chen.vike.c680.tools;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import chen.vike.c680.ALiPay.Alipay;
import chen.vike.c680.activity.PersonChangZuoPinActivity;
import chen.vike.c680.activity.BankActivity;
import chen.vike.c680.activity.BuyServiceActivity;
import chen.vike.c680.activity.EmailActivity;
import chen.vike.c680.activity.ImmediateGyActivity;
import chen.vike.c680.activity.PlanDetailsActivity;
import chen.vike.c680.activity.ServiceDetailsActivity;
import chen.vike.c680.activity.ShiMingRenZhengActivity;
import chen.vike.c680.activity.TaskDetailsActivity;

import chen.vike.c680.main.MyApplication;
import chen.vike.c680.activity.MessageChatActivity;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.LoadingDialog;
import chen.vike.c680.views.LodaWindow;

import chen.vike.c680.activity.ShopDetailsActivity;
import chen.vike.c680.activity.FaXianXiangQActivity;
import chen.vike.c680.activity.FaXianZhiFuActivity;
import chen.vike.c680.activity.UserLoginActivity;


/**
 * Created by Administrator on 2015/10/16.
 */
public class JavaScriptInterface {

    private Activity context;
    private LoadingDialog ld;
    String TAG = "JavaScriptInterface";
    private LodaWindow lod;

    private  Intent intent;

    public JavaScriptInterface(Activity context) {

        LogUtils.v(TAG, "实例化成功");
        this.context = context;
    }

    @JavascriptInterface
    public void callOnJs(String type, String s) {
        LogUtils.d("====================s:" + s);
        LogUtils.d("====================type:" + type);
        String[] strings = s.split(",");
        switch (type) {
            case "changeworks":   //修改作品
                if (strings[1].equals("edit")) {
                    intent = new Intent(context, PersonChangZuoPinActivity.class);
                    intent.putExtra("itemid", strings[0]);
                    context.startActivity(intent);
                }else {
                    context.finish();
                }
                break;
            case "jumpchangeworks":    //跳转到作品详情
                intent = new Intent(context,FaXianXiangQActivity.class);
                intent.putExtra("weburl",strings[1]+"&vkuserip="+ MyApplication.userInfo.getCookieLoginIpt()+"&vktoken="+ MyApplication.userInfo.getCookieLoginToken());
                intent.putExtra("title",strings[2]);
                context.startActivityForResult(intent, 1);
                break;
            case "tiaozhuan":
                Intent intent = new Intent(context, FaXianXiangQActivity.class);
                if (LhtTool.isLogin){
                    if (MyApplication.userInfo != null && MyApplication.userInfo.getPhone() != null){
                        intent.putExtra("weburl",strings[0]+"&phone="+ MyApplication.userInfo.getPhone()+"&uid="+ MyApplication.userInfo.getUserID());
                    }
                }else {
                    intent.putExtra("weburl", strings[0]);
                }
                intent.putExtra("title", strings[1]);
                context.startActivityForResult(intent, 1);
                break;
            case "faxianzhifu":     //作品支付
                if (LhtTool.isLogin){
                    intent = new Intent(context, FaXianZhiFuActivity.class);
                    intent.putExtra("zp_id_v", strings[0]);
                    intent.putExtra("money_v", strings[1]);
                    intent.putExtra("crv_phone_v", strings[2]);
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context, "请登录后进行操作",Toast.LENGTH_SHORT).show();
                    intent = new Intent(context, UserLoginActivity.class);
                    context.startActivity(intent);
                }
                break;
            case "guyong":     //购买服务
               intent = new Intent(context, BuyServiceActivity.class);
                intent.putExtra("WEB", strings[1]);
                context.startActivityForResult(intent, 1);
                break;
            case "zhijieguyong":     //立即雇佣服务商
                intent = new Intent(context, ImmediateGyActivity.class);
                intent.putExtra("ID", strings[0]);
                context.startActivityForResult(intent, 1);
                break;
            case "case":        //方案详情
                intent = new Intent(context, PlanDetailsActivity.class);
                intent.putExtra("TYPE", strings[0]);
                intent.putExtra("ID", strings[1]);
                intent.putExtra("CYID", strings[2]);
                context.startActivityForResult(intent, 1);
                break;
            case "shop":       //店铺详情
                intent = new Intent(context, ShopDetailsActivity.class);
                intent.putExtra("ID", strings[0]);
                context.startActivityForResult(intent, 1);
                break;
            case "item":       //任务详情
                intent = new Intent(context, TaskDetailsActivity.class);
                intent.putExtra("ID", strings[0]);
                context.startActivityForResult(intent, 1);
                break;
            case "fuwu":        //服务详情
                intent = new Intent(context, ServiceDetailsActivity.class);
                intent.putExtra("ID", strings[0]);
                context.startActivityForResult(intent, 1);
                break;
            case "zscq":      //知识产权
                if (UrlConfig.alipay_flag.equals("1")) {
//                    lod = new LodaWindow(context,context);
//                    lod.setMessage("加载中....");
                    Alipay.pay(context, LhtTool.getHander(context, strings[1],ld), Alipay.getOrderInfo("知识产权", "时间财富商标注册订单:" + strings[0], strings[1], strings[0]));
                } else {
                    Toast.makeText(context, "该版本暂时不支持支付宝支付",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case "back":       //返回
                context.finish();
                break;
            case "renzheng":      //认证
                switch (strings[0]){
                    case "shiming":
                        intent = new Intent(context, ShiMingRenZhengActivity.class);
                        context.startActivity(intent);
                        break;
                    case "youxiang":
                        intent = new Intent(context, EmailActivity.class);
                        context.startActivity(intent);
                        break;
                    case "yinhangka":
                        intent = new Intent(context, BankActivity.class);
                        context.startActivity(intent);
                        break;

                }

                break;
            case "GeRenZhongXinShopDetailsActivity":
                switch (strings[0]){
                    case "consulting":
                        if (LhtTool.isLogin) {
                                //跳到聊天界面
                                intent = new Intent(context, MessageChatActivity.class);
                                intent.putExtra("ID", strings[1]);
                                intent.putExtra("name", strings[2]);
                                context.startActivity(intent);
                            }
                            else {
                                CustomToast.showToast(context, "亲，你还没有登录哦~", Toast.LENGTH_SHORT);
                                intent = new Intent(context, UserLoginActivity.class);
                                context.startActivity(intent);
                        }
                        break;
                    case "collection":

                        break;
                }
                break;
            case "login":     //登录
                intent = new Intent(context, UserLoginActivity.class);
                context.startActivityForResult(intent,2);
                break;
        }

    }


}
