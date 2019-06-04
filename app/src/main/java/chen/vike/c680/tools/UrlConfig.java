package chen.vike.c680.tools;

/**
 * Created by lht on 2017/2/24.
 */

public class UrlConfig {


    //支付宝相关配置字段
    public static String PARTNER;
    public static String SELLER;
    public static String RSA_PRIVATE;
    public static String RSA_PUBLIC;
    public static String alipay_flag;

    //微信支付相关配置字段
    public static String weixinpay_flag;

    //获取手机验证码key
    public final static String MOBILE_VERIFICATION_KEY = "DJKKJEREOIUIO";

    public static final String BASE_URL_API ="http://app.680.com/api/";
    public static final String BASE_URL = "http://app.680.com/";


    public static final String GET_VERSION = BASE_URL+"appinfo.ashx";
    public static final String WEIKEJINJIREN = BASE_URL+"union/index.aspx";
    public static final String CHENGGONGANLI = BASE_URL+"case/index.aspx";
    public static final String SHANGBIAOZHUCE = BASE_URL+"sb/index.aspx?dover=2";
    public static final String CAISHUI = BASE_URL+"finance/index.aspx";
    public static final String INTERVIEW_COLUMN = BASE_URL+"news/app_view.aspx";
    public static final String SHOUSUOYEMIAN = BASE_URL+"shop.html";//搜索  店铺页面
    public static final String SHOUSUOFUWU = BASE_URL+"fuwu.html";//搜索   服务页面
    public static final String SOUSUOXIANGMU = BASE_URL+"task.html";//搜索  项目页面
    public static final String TIXIANRENZHENG = BASE_URL+"user/accountbook/v2018/tocash.aspx";  //提现---认证页面
    public static final String DIANPUXIANGQING = BASE_URL+"shop/v2018/info.aspx?shopid=10416580";//  店铺详情页面
    public static final String SERVERS_DETAILS = BASE_URL+"touch/fuwu/view.aspx?id=";//服务详情
    public static final String USERINFO_ZHUANGTAI = BASE_URL+"api/user_verifyinfo.ashx";
    public static final String CHUANGYIZIXUN = BASE_URL+"news/index.aspx";
    public static final String SPLASH_NEWS = BASE_URL+"news/index.aspx";


    public static final String GUZHU_PAGE_INFO = BASE_URL_API +"v3/index_gz_data.ashx";
    public static final String GUZHU_FUWU_MORE = BASE_URL_API +"v3/index_gz_fuwu_more.ashx";
    public static final String WEIKE_PAGE_INFO = BASE_URL_API +"v3/index_vk_data.ashx";
    public static final String WEIKE_ITEM_MORE = BASE_URL_API +"v3/index_vk_item_more.ashx";
    public static final String GET_USER_INFO = BASE_URL_API +"v2/login.ashx";
    public static final String ZIDONG_LOGIN = BASE_URL_API +"v4/auto_login.ashx";
    public static final String QQ_LOGIN = BASE_URL_API +"v2/qq_login.ashx";
    public static final String MOBILE_VERIFICATION = BASE_URL_API +"mobilemsg.ashx";
    public static final String MOBILE_USER_FIND = BASE_URL_API +"user_find_loginpwd.ashx";
    public static final String REGISTER_MESSAGE = BASE_URL_API +"reg.ashx";
    public static final String PERSONAL_INFO = BASE_URL_API +"v3/index_user_center.ashx";
    public static final String PERSONAL_EXIT = BASE_URL_API +"v2/user_exit.ashx";
    public static final String UPDATE_PASSWORD = BASE_URL_API +"editloginpwd.ashx";
    public static final String UPLOAD_SFZ = BASE_URL_API +"upload.ashx?type=sfz";
    public static final String UPLOAD_TOUXIANG = BASE_URL_API +"upload.ashx?type=face";
    public static final String SHIMINGRENZHENG = BASE_URL_API +"verify_fullname.ashx";
    public static final String EMAIL_VERIFICATION = BASE_URL_API +"user_get_emailcode.ashx";
    public static final String EMAIL_UPDATE = BASE_URL_API +"user_email_verify.ashx";
    public static final String MOBILE_MESSAGE_VERIFICATION = BASE_URL_API +"mobilemsg.ashx";
    public static final String MOBILE_IS_SURE = BASE_URL_API +"user_phone_verify.ashx";
    public static final String BANK_BIND = BASE_URL_API +"user_bank_bind.ashx";
    public static final String HTTP_URL_ALIPAY_notify_url = BASE_URL_API +"alipay_server.aspx";
    public static final String GET_FABU_LIST = BASE_URL_API +"v2/user_item_gz_list.ashx";
    public static final String RECHANGE_MONEY = BASE_URL_API +"recharge.ashx";
    public static final String SURE_TX_MONEY = BASE_URL_API +"user_tocash_action.ashx";
    public static final String TX_VERIFICATION = BASE_URL_API +"user_tocash_get.ashx";
    public static final String USER_VERIFICATION_INFO = BASE_URL_API +"user_verifyinfo.ashx";
    public static final String CANYU_XUANGSHANG_LIST = BASE_URL_API +"v2/user_item_canyu_list.ashx";
    public static final String CANYU_JIJIAN_LIST = BASE_URL_API +"v2/user_item_canyu_list_jj.ashx";
    public static final String GET_FENLEI_INFO = BASE_URL_API +"itemclass.ashx";
    public static final String USER_FUWU_LIST = BASE_URL_API +"v2/user_fuwu_list.ashx";
    public static final String DELETE_FUWU = BASE_URL_API +"v2/user_fuwu_action.ashx";
    public static final String OPEN_SHOP = BASE_URL_API +"v2/openshop.ashx";
    public static final String GET_SHOP_INFO = BASE_URL_API +"v2/shop_info_index.ashx";
    public static final String SHOP_FUWU_LIST = BASE_URL_API +"shop_fuwu_list.ashx";
    public static final String SHOP_ZUOPIN_LIST = BASE_URL_API +"shop_zuopin_list.ashx";
    public static final String FUWU_PINGJIA = BASE_URL_API +"fuwu_pingjia_list.ashx";
    public static final String FUWU_XIANGQING = BASE_URL_API +"shop_fuwu_info.ashx";
    public static final String GUYONG_FUWU = BASE_URL_API +"user_guyong.ashx";
    public static final String SURE_FB = BASE_URL_API +"v3/fabu_item.ashx";
    public static final String ITEM_INFO = BASE_URL_API +"item_info.ashx";
    public static final String GET_ITEM_PAY = BASE_URL_API +"itempay.ashx";
    public static final String USER_JION = BASE_URL_API +"item_canyu_action.ashx";//提交稿件请求数据
    public static final String BAOJIA_JION = BASE_URL_API +"item_baojia_action.ashx";
    public static final String USER_COLLECTION = BASE_URL_API +"user_fav_list.ashx";
    public static final String COLLECTION = BASE_URL_API +"fav.ashx";
    public static final String DELETE_COLLECTION = BASE_URL_API +"user_fav_action.ashx";
    public static final String GET_ITEM_ADD_TO_PAY = BASE_URL_API +"item_addmoney_payinfo.ashx";
    public static final String GET_ITEM_ADD_TOPAY = BASE_URL_API +"item_add_topay.ashx";
    public static final String USER_GUYONG = BASE_URL_API +"user_guyong.ashx";
    public static final String SERVICE_ZL = BASE_URL_API +"v2/channel_fuwu_list.ashx";
    public static final String FABU_SERVICE = BASE_URL_API +"v2/user_fuwu_fabu.ashx";
    public static final String SHOP_ZUOPIN_INFO = BASE_URL_API +"shop_zuopin_info.ashx";
    public static final String MY_SHOP_INFO = BASE_URL_API +"v2/user_shop_info.ashx";//自己店铺信息
    public static final String GET_ITEM_ADD_DO = BASE_URL_API +"item_add_topay_do.aspx";
    public static final String GET_ITEMPAY = BASE_URL_API +"itempay.ashx";
    public static final String GAOJIAN_LIST_JJ = BASE_URL_API +"v2/item_gaojian_list_jj.ashx";
    public static final String GAOJIAN_LIST = BASE_URL_API +"v2/item_gaojian_list.ashx";
    public static final String BAOJIA_LIST = BASE_URL_API +"item_baojiao_list.ashx";
    public static final String GAOJIAN_XQ = BASE_URL_API +"item_canyu_info.ashx";//稿件详情
    public static final String DELETE_PROJECT = BASE_URL_API +"user_item_action.ashx";
    public static final String USER_VIP_INFO = BASE_URL_API +"v2/user_vip_index.ashx";
    public static final String XUANBIAO_ACTION = BASE_URL_API +"item_xuanbiao_action.ashx";//非计件选标
    public static final String XUANBIAO_ACTION_JJ = BASE_URL_API +"item_xuanbiao_jj_action.ashx";//非计件选表
    public static final String VIP_RECORD = BASE_URL_API +"v2/user_vip_tradelist.ashx";
    public static final String GET_TOPAY_ITEM = BASE_URL_API +"topayitem.ashx";//余额支付方式
    public static final String BUY_VIP_ZFB = BASE_URL_API +"v2/user_vip_pay.ashx";//VIP购买记录支付宝支付
    public static final String BUY_VIP_YU_E = BASE_URL_API +"v2/user_vip_pay_account.ashx";//VIP购买记录支付宝支付
    public static final String SEARCH_ITEM = BASE_URL_API +"v2/search_item_list.ashx";//项目搜索
    public static final String SEARCH_FUWU = BASE_URL_API +"v2/search_fuwu_list.ashx";//服务搜索
    public static final String SEARCH_SHOP = BASE_URL_API +"v2/search_shop_list.ashx";//店铺搜索
    public static final String WEIKE_HELP = BASE_URL_API +"help/vk_index.aspx";
    public static final String GUZHU_HELP = BASE_URL_API +"help/gz_index.aspx";
    public static final String FUWU_HELP = BASE_URL_API +"help/fabu_fuwu.aspx";
    public static final String WEIKE_HOT = BASE_URL_API +"v3/index_vk_rel_items.ashx";
    public static final String MESSAGE_LIST = BASE_URL_API +"v2/message_index.ashx";
    public static final String ALL_UNREAD = BASE_URL_API +"v2/message_all_unread.ashx";
    public static final String GET_SYSTEMLIST = BASE_URL_API +"v2/message_sys_datalist.ashx";
    public static final String DELETE_SYSTEM_MESSAGE = BASE_URL_API +"v2/message_sys_del.ashx";
    public static final String CHAT_MESSAGE = BASE_URL_API +"v2/message_user_from.ashx";
    public static final String SEND_MESSAGE = BASE_URL_API +"v2/message_user_send.ashx";
    public static final String SEARCH_PERSON = BASE_URL_API +"v3/message_index_search.ashx ";
    public static final String HELP_FANKUI = BASE_URL_API +"help/user_index.aspx";
    public static final String USER_NO_ZHUANKUAN = BASE_URL_API +"v3/user_item_zhuankuan_cylist.ashx";
    public static final String SURE_ZHUANKUAN = BASE_URL_API +"v3/zhuankuan_action.ashx";
    public static final String SET_DEPOSIT = BASE_URL_API +"v3/item_zhaobiao_set_dingjin.ashx";
    public static final String SET_FENQI = BASE_URL_API +"v3/item_zhaobiao_set_fenqi.ashx";
    public static final String FENQI_DETAILS = BASE_URL_API +"v3/item_zhaobiao_get_fenqi.ashx";
    public static final String ZHAOBIAO_TUOGUAN = BASE_URL_API +"v3/item_pay_zhaobiao.ashx";
    public static final String ZHAOBIAO_ALLMONEY = BASE_URL_API +"v3/get_zhaobiao_item_fenqi_pre.ashx";
    public static final String DIAN_URL = BASE_URL_API +"v3/favshop.ashx";//收藏店铺
    public static final String PERSON_TUISONG = BASE_URL_API +"v4/push_items.ashx" ;   //底部推送项目
    public static final String PERSON_MSG = BASE_URL_API +"v4/user_center.ashx";
    public static final String SHOP_FAVORITE = BASE_URL_API +"v3/favshop.ashx";//收藏店铺
    public static final String VIPTUISONG = BASE_URL_API +"v3/tuisong_itemlist.ashx";//vip推送更多
    public static final String ZUOPINURL = BASE_URL_API +"v4/faxian_fabu_zuopin.ashx";//作品我的发布删除
    public static final String TAOCANLIST = BASE_URL_API +"v4/taocan_list.ashx";//发布需求套餐发布列表数据
    public static final String TAOCANDING = BASE_URL_API +"v4/taocan_buy.ashx";//套餐发布接口
    public static final String KUAISUFABU = BASE_URL_API +"v4/fabu_fast.ashx";//快速发布需求接口
    public static final String GUZHUPAGE = BASE_URL_API +"v5/guzhu_index.ashx";//主页雇主页面数据接口
    public static final String GUZHUMORE = BASE_URL_API +"v5/guzhu_index_cainixihuan_more.ashx";//雇主列表数据更多接口
    public static final String LOGIN_PHONE = BASE_URL_API +"v4/login_mobile.ashx";//手机号验证码登陆
    public static final String UPDATEZUOPINGIMG = BASE_URL_API +"v4/upload_list.ashx";
    public static final String UPDATEZUOPING = BASE_URL_API +"v4/faxian_fabu_zuopin.ashx";
    public static final String UPLOADJSON = BASE_URL_API +"v5/app_tongji_data.ashx";  //页面统计接口
    public static final String WECHAT_LOGIN = BASE_URL_API +"v2/weixin_login.ashx";  //微信登录接口




}
