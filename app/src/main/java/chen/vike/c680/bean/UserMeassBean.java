package chen.vike.c680.bean;

/**
 * Created by lht on 2017/12/4.
 */

public class UserMeassBean {


    /**
     * vkdata : {"username":"duanliangliang","isshop":"1","vk_money":"13","vk_sign":"0","vk_mon_pay":"0","sp3total":"1","viptype":"2","fav_item_count":"0","err_code":"","err_msg":""}
     */
    private String err_code;
    private String err_msg;
    private VkdataBean vkdata;

    public VkdataBean getVkdata() {
        return vkdata;
    }

    public void setVkdata(VkdataBean vkdata) {
        this.vkdata = vkdata;
    }

    public String getErr_code() {
            return err_code;
        }

        public void setErr_code(String err_code) {
            this.err_code = err_code;
        }

        public String getErr_msg() {
            return err_msg;
        }

        public void setErr_msg(String err_msg) {
            this.err_msg = err_msg;
        }

    public static class VkdataBean {
        /**
         * username : duanliangliang
         * isshop : 1
         * vk_money : 13
         * vk_sign : 0
         * vk_mon_pay : 0
         * sp3total : 1
         * viptype : 2
         * fav_item_count : 0
         * err_code :
         * err_msg :
         */

        private String username="";
        private String isshop="";
        private String vk_money="";
        private String vk_sign="";
        private String vk_mon_pay="";
        private String sp3total="";
        private String viptype="";
        private String fav_item_count="";

        private String dingzhi_num="";

        public String getDingzhi_num() {
            return dingzhi_num;
        }

        public void setDingzhi_num(String dingzhi_num) {
            this.dingzhi_num = dingzhi_num;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIsshop() {
            return isshop;
        }

        public void setIsshop(String isshop) {
            this.isshop = isshop;
        }

        public String getVk_money() {
            return vk_money;
        }

        public void setVk_money(String vk_money) {
            this.vk_money = vk_money;
        }

        public String getVk_sign() {
            return vk_sign;
        }

        public void setVk_sign(String vk_sign) {
            this.vk_sign = vk_sign;
        }

        public String getVk_mon_pay() {
            return vk_mon_pay;
        }

        public void setVk_mon_pay(String vk_mon_pay) {
            this.vk_mon_pay = vk_mon_pay;
        }

        public String getSp3total() {
            return sp3total;
        }

        public void setSp3total(String sp3total) {
            this.sp3total = sp3total;
        }

        public String getViptype() {
            return viptype;
        }

        public void setViptype(String viptype) {
            this.viptype = viptype;
        }

        public String getFav_item_count() {
            return fav_item_count;
        }

        public void setFav_item_count(String fav_item_count) {
            this.fav_item_count = fav_item_count;
        }

    }
}
