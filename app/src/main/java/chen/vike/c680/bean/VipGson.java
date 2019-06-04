package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/18.
 */

public class VipGson {


    /**
     * is_vip : 1
     * check : 6
     * vip_endtime : 2017/09/09 15:40:14
     * tj_item_list : [{"money":"400","itemname":"水性金属漆的包装设计","zab_yusuan1":"0","itemid":"422458","zab_yusuan2":"0","zab_do":"0"},{"money":"700","itemname":"金属制品公司LOGO设计","zab_yusuan1":"0","itemid":"392160","zab_yusuan2":"0","zab_do":"0"},{"money":"1200","itemname":"高级美容会所LOGO设计","zab_yusuan1":"0","itemid":"391931","zab_yusuan2":"0","zab_do":"6"},{"money":"1000","itemname":"LOGO设计/助跑企业腾飞","zab_yusuan1":"0","itemid":"391756","zab_yusuan2":"0","zab_do":"6"},{"money":"100","itemname":"我要设计公司LOGO","zab_yusuan1":"0","itemid":"391496","zab_yusuan2":"0","zab_do":"0"}]
     * good_item_list : [{"zab_yusuan1":"0","itemname":"苏木子家具LOGO设计","itemid":"425223","money":"488","zab_yusuan2":"0","zab_do":"0"},{"zab_yusuan1":"10000","itemname":"软件开发","itemid":"425188","money":"10000-20000","zab_yusuan2":"20000","zab_do":"1"},{"zab_yusuan1":"0","itemname":"建材公司LOGO设计","itemid":"425169","money":"400","zab_yusuan2":"0","zab_do":"0"},{"zab_yusuan1":"10000","itemname":"企业网站建设","itemid":"425146","money":"1500","zab_yusuan2":"20000","zab_do":"0"},{"zab_yusuan1":"600000","itemname":"旅行社网站建议项目报价","itemid":"425145","money":"600000-700000","zab_yusuan2":"700000","zab_do":"1"}]
     * city_item_list : [{"zab_yusuan1":"1000","itemname":"微信QQ返利机器人","itemid":"424449","money":"1000-10000","zab_yusuan2":"10000","zab_do":"1"},{"zab_yusuan1":"0","itemname":"需要华丽美美的文字介绍公司","itemid":"424245","money":"100","zab_yusuan2":"0","zab_do":"0"},{"zab_yusuan1":"0","itemname":"深圳市行者实践企业管理咨询有限公司LOGO设计","itemid":"424232","money":"200","zab_yusuan2":"0","zab_do":"0"},{"zab_yusuan1":"0","itemname":"QQ群简单推广群发拉人","itemid":"423253","money":"50","zab_yusuan2":"0","zab_do":"0"},{"zab_yusuan1":"0","itemname":"求合作伙伴--专业生产建筑防火门系列的合伙人","itemid":"421838","money":"100","zab_yusuan2":"0","zab_do":"0"}]
     */

    private int is_vip;
    private int check;
    private String vip_endtime;
    private List<TjItemListBean> tj_item_list;
    private List<GoodItemListBean> good_item_list;
    private List<CityItemListBean> city_item_list;

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getVip_endtime() {
        return vip_endtime;
    }

    public void setVip_endtime(String vip_endtime) {
        this.vip_endtime = vip_endtime;
    }

    public List<TjItemListBean> getTj_item_list() {
        return tj_item_list;
    }

    public void setTj_item_list(List<TjItemListBean> tj_item_list) {
        this.tj_item_list = tj_item_list;
    }

    public List<GoodItemListBean> getGood_item_list() {
        return good_item_list;
    }

    public void setGood_item_list(List<GoodItemListBean> good_item_list) {
        this.good_item_list = good_item_list;
    }

    public List<CityItemListBean> getCity_item_list() {
        return city_item_list;
    }

    public void setCity_item_list(List<CityItemListBean> city_item_list) {
        this.city_item_list = city_item_list;
    }

    public static class TjItemListBean {
        /**
         * money : 400
         * itemname : 水性金属漆的包装设计
         * zab_yusuan1 : 0
         * itemid : 422458
         * zab_yusuan2 : 0
         * zab_do : 0
         */

        private String money;
        private String itemname;
        private String zab_yusuan1;
        private String itemid;
        private String zab_yusuan2;
        private String zab_do;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getZab_yusuan1() {
            return zab_yusuan1;
        }

        public void setZab_yusuan1(String zab_yusuan1) {
            this.zab_yusuan1 = zab_yusuan1;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getZab_yusuan2() {
            return zab_yusuan2;
        }

        public void setZab_yusuan2(String zab_yusuan2) {
            this.zab_yusuan2 = zab_yusuan2;
        }

        public String getZab_do() {
            return zab_do;
        }

        public void setZab_do(String zab_do) {
            this.zab_do = zab_do;
        }
    }

    public static class GoodItemListBean {
        /**
         * zab_yusuan1 : 0
         * itemname : 苏木子家具LOGO设计
         * itemid : 425223
         * money : 488
         * zab_yusuan2 : 0
         * zab_do : 0
         */

        private String zab_yusuan1;
        private String itemname;
        private String itemid;
        private String money;
        private String zab_yusuan2;
        private String zab_do;

        public String getZab_yusuan1() {
            return zab_yusuan1;
        }

        public void setZab_yusuan1(String zab_yusuan1) {
            this.zab_yusuan1 = zab_yusuan1;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

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

        public String getZab_yusuan2() {
            return zab_yusuan2;
        }

        public void setZab_yusuan2(String zab_yusuan2) {
            this.zab_yusuan2 = zab_yusuan2;
        }

        public String getZab_do() {
            return zab_do;
        }

        public void setZab_do(String zab_do) {
            this.zab_do = zab_do;
        }
    }

    public static class CityItemListBean {
        /**
         * zab_yusuan1 : 1000
         * itemname : 微信QQ返利机器人
         * itemid : 424449
         * money : 1000-10000
         * zab_yusuan2 : 10000
         * zab_do : 1
         */

        private String zab_yusuan1;
        private String itemname;
        private String itemid;
        private String money;
        private String zab_yusuan2;
        private String zab_do;

        public String getZab_yusuan1() {
            return zab_yusuan1;
        }

        public void setZab_yusuan1(String zab_yusuan1) {
            this.zab_yusuan1 = zab_yusuan1;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

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

        public String getZab_yusuan2() {
            return zab_yusuan2;
        }

        public void setZab_yusuan2(String zab_yusuan2) {
            this.zab_yusuan2 = zab_yusuan2;
        }

        public String getZab_do() {
            return zab_do;
        }

        public void setZab_do(String zab_do) {
            this.zab_do = zab_do;
        }
    }
}
