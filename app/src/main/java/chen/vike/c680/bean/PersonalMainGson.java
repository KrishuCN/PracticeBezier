package chen.vike.c680.bean;

/**
 * Created by lht on 2017/3/6.
 */

public class PersonalMainGson {


    /**
     * vkdata : {"isshop":"1","imageurl":"http://head.shopimg.680.com/2017-2/24/s/2017022411080614342_583067.jpg","vk_mon_pay":"10000","fav_item_count":"20","username":"gguu","vk_money":"1491.29","viptype":"2"}
     */

    private VkdataBean vkdata;

    public VkdataBean getVkdata() {
        return vkdata;
    }

    public void setVkdata(VkdataBean vkdata) {
        this.vkdata = vkdata;
    }

    public static class VkdataBean {
        /**
         * isshop : 1
         * imageurl : http://head.shopimg.680.com/2017-2/24/s/2017022411080614342_583067.jpg
         * vk_mon_pay : 10000
         * fav_item_count : 20
         * username : gguu
         * vk_money : 1491.29
         * viptype : 2
         */

        private String isshop;
        private String imageurl;
        private String vk_mon_pay;
        private String fav_item_count;
        private String username;
        private String vk_money;
        private String viptype;

        public String getIsshop() {
            return isshop;
        }

        public void setIsshop(String isshop) {
            this.isshop = isshop;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public String getVk_mon_pay() {
            return vk_mon_pay;
        }

        public void setVk_mon_pay(String vk_mon_pay) {
            this.vk_mon_pay = vk_mon_pay;
        }

        public String getFav_item_count() {
            return fav_item_count;
        }

        public void setFav_item_count(String fav_item_count) {
            this.fav_item_count = fav_item_count;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getVk_money() {
            return vk_money;
        }

        public void setVk_money(String vk_money) {
            this.vk_money = vk_money;
        }

        public String getViptype() {
            return viptype;
        }

        public void setViptype(String viptype) {
            this.viptype = viptype;
        }
    }
}
