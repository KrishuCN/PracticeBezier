package chen.vike.c680.tools;

import chen.vike.c680.bean.WeiKePageGson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lht on 2017/10/16.
 */

public class ConfigTools {

    public static List<WeiKePageGson.ItemListBean> weiKeList = new ArrayList<WeiKePageGson.ItemListBean>();
    //服务商标志0 是普通，大于3是vip
    public static int VIPMARK = 0;

    public static List<String> userMessageList = new ArrayList<>();//个人信息

    public static List<String> faPiaoList = new ArrayList<>();//发票信息
    
}
