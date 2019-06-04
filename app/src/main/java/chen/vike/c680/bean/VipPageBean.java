package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/10/20.
 */

public class VipPageBean {


    /**
     * result : vktoken=4309cc4c74f99972&vkuserip=112.193.169.84-58-55-app&userid=583067&ALL_HTTP=HTTP_CONNECTION%3aKeep-Alive%0d%0aHTTP_CONTENT_LENGTH%3a72%0d%0aHTTP_CONTENT_TYPE%3aapplication%2fx-www-form-urlencoded%0d%0aHTTP_ACCEPT_ENCODING%3agzip%0d%0aHTTP_HOST%3aapp.680.com%0d%0aHTTP_USER_AGENT%3aokhttp%2f3.6.0%0d%0a&ALL_RAW=Connection%3a+Keep-Alive%0d%0aContent-Length%3a+72%0d%0aContent-Type%3a+application%2fx-www-form-urlencoded%0d%0aAccept-Encoding%3a+gzip%0d%0aHost%3a+app.680.com%0d%0aUser-Agent%3a+okhttp%2f3.6.0%0d%0a&APPL_MD_PATH=%2fLM%2fW3SVC%2f13%2fROOT&APPL_PHYSICAL_PATH=D%3a%5ciis%5capp.680.com%5c&AUTH_TYPE=&AUTH_USER=&AUTH_PASSWORD=&LOGON_USER=&REMOTE_USER=&CERT_COOKIE=&CERT_FLAGS=&CERT_ISSUER=&CERT_KEYSIZE=&CERT_SECRETKEYSIZE=&CERT_SERIALNUMBER=&CERT_SERVER_ISSUER=&CERT_SERVER_SUBJECT=&CERT_SUBJECT=&CONTENT_LENGTH=72&CONTENT_TYPE=application%2fx-www-form-urlencoded&GATEWAY_INTERFACE=CGI%2f1.1&HTTPS=off&HTTPS_KEYSIZE=&HTTPS_SECRETKEYSIZE=&HTTPS_SERVER_ISSUER=&HTTPS_SERVER_SUBJECT=&INSTANCE_ID=13&INSTANCE_META_PATH=%2fLM%2fW3SVC%2f13&LOCAL_ADDR=106.14.255.131&PATH_INFO=%2fapi%2fv3%2findex_vk_vip_data.ashx&PATH_TRANSLATED=D%3a%5ciis%5capp.680.com%5capi%5cv3%5cindex_vk_vip_data.ashx&QUERY_STRING=&REMOTE_ADDR=112.193.169.84&REMOTE_HOST=112.193.169.84&REMOTE_PORT=51571&REQUEST_METHOD=POST&SCRIPT_NAME=%2fapi%2fv3%2findex_vk_vip_data.ashx&SERVER_NAME=app.680.com&SERVER_PORT=80&SERVER_PORT_SECURE=0&SERVER_PROTOCOL=HTTP%2f1.1&SERVER_SOFTWARE=Microsoft-IIS%2f7.5&URL=%2fapi%2fv3%2findex_vk_vip_data.ashx&HTTP_CONNECTION=Keep-Alive&HTTP_CONTENT_LENGTH=72&HTTP_CONTENT_TYPE=application%2fx-www-form-urlencoded&HTTP_ACCEPT_ENCODING=gzip&HTTP_HOST=app.680.com&HTTP_USER_AGENT=okhttp%2f3.6.0
     * username : gguu
     * viptype : 5
     * vk_mon_pay : 469.00
     * vip_out_date : 2018-09-09
     * tuisong_num : 15
     * genjin_num : 10
     * good_item_list : [{"itemid":"446330","money":"998","zab_do":"0","itemname":"公司LOGO设计"},{"itemid":"446325","money":"588","zab_do":"0","itemname":"闪电心算LOGO设计"},{"itemid":"446324","money":"688","zab_do":"0","itemname":"空气净化器企业  LOGO设计"},{"itemid":"446294","money":"998","zab_do":"0","itemname":"旅游公司LOGO设计"},{"itemid":"446291","money":"450","zab_do":"0","itemname":"飞龙浩德物流LOGO设计"}]
     * tuisong_item_list : [{"itemid":"444064","itemname":"餐饮LOGO设计","endtime":"已到期","content":"LOGO名称：湘旺跳跳蛙我是做餐饮的，以牛蛙为主。设计一个卡通的牛蛙搭配我的","zab_do":"0","money":"58"},{"itemid":"443541","itemname":"网站模板，dedecms模板，详见附件","endtime":"已到期","content":"详见附件~~网站类型：企业官网功能模块：新闻系统，产品展示，分享功能，在","zab_do":"0","money":"1000"},{"itemid":"443538","itemname":"公司LOGO设计","endtime":"已到期","content":"LOGO名称：丝路研学详细要求见附件附件：http://d4.file.","zab_do":"0","money":"888"},{"itemid":"422458","itemname":"水性金属漆的包装设计","endtime":"已到期","content":"产品为水性金属漆，现需要做一个外包装贴纸，要求包括图案、商标区域、介绍产品区贮存","zab_do":"0","money":"400"},{"itemid":"392160","itemname":"金属制品公司LOGO设计","endtime":"已到期","content":"宁波丰华金属制品有限公司。 主营：生产阀门及阀门配件，铜材配件，管件类金属加工。","zab_do":"0","money":"700"}]
     */

    private String result;
    private String username;
    private String viptype;
    private String vk_mon_pay;
    private String vip_out_date;
    private int tuisong_num;
    private int genjin_num;
    private List<GoodItemListBean> good_item_list;
    private List<TuisongItemListBean> tuisong_item_list;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getViptype() {
        return viptype;
    }

    public void setViptype(String viptype) {
        this.viptype = viptype;
    }

    public String getVk_mon_pay() {
        return vk_mon_pay;
    }

    public void setVk_mon_pay(String vk_mon_pay) {
        this.vk_mon_pay = vk_mon_pay;
    }

    public String getVip_out_date() {
        return vip_out_date;
    }

    public void setVip_out_date(String vip_out_date) {
        this.vip_out_date = vip_out_date;
    }

    public int getTuisong_num() {
        return tuisong_num;
    }

    public void setTuisong_num(int tuisong_num) {
        this.tuisong_num = tuisong_num;
    }

    public int getGenjin_num() {
        return genjin_num;
    }

    public void setGenjin_num(int genjin_num) {
        this.genjin_num = genjin_num;
    }

    public List<GoodItemListBean> getGood_item_list() {
        return good_item_list;
    }

    public void setGood_item_list(List<GoodItemListBean> good_item_list) {
        this.good_item_list = good_item_list;
    }

    public List<TuisongItemListBean> getTuisong_item_list() {
        return tuisong_item_list;
    }

    public void setTuisong_item_list(List<TuisongItemListBean> tuisong_item_list) {
        this.tuisong_item_list = tuisong_item_list;
    }

    public static class GoodItemListBean {
        /**
         * itemid : 446330
         * money : 998
         * zab_do : 0
         * itemname : 公司LOGO设计
         */

        private String itemid;
        private String money;
        private String zab_do;
        private String itemname;

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getZab_do() {
            return zab_do;
        }

        public void setZab_do(String zab_do) {
            this.zab_do = zab_do;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }
    }

    public static class TuisongItemListBean {
        /**
         * itemid : 444064
         * itemname : 餐饮LOGO设计
         * endtime : 已到期
         * content : LOGO名称：湘旺跳跳蛙我是做餐饮的，以牛蛙为主。设计一个卡通的牛蛙搭配我的
         * zab_do : 0
         * money : 58
         */

        private String itemid;
        private String itemname;
        private String endtime;
        private String content;
        private String zab_do;
        private String money;

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getZab_do() {
            return zab_do;
        }

        public void setZab_do(String zab_do) {
            this.zab_do = zab_do;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
