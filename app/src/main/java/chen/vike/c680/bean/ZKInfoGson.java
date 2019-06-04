package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/5/13.
 */

public class ZKInfoGson {


    /**
     * itemInfo : {"shouji":"11780234112","itemid":"429204","err_msg":"","err_code":"0","addtime":"2017-04-25","itemname":"营销策划"}
     * zhongbiao_gaojian_list : [{"username_vk":"gguu2","itemid":"429204","zhongbiao":"一等奖","cyid":"7920830","imageurl":"http://img21.vikecn.com/Task/2011-2/24/607473_2011224174226.gif"},{"username_vk":"zyjeatyou中国","itemid":"429204","zhongbiao":"二等奖","cyid":"7926015","imageurl":"http://head.shopimg.680.com/2017-3/31/22017033110390755394_9376232.png"},{"username_vk":"gguu3","itemid":"429204","zhongbiao":"三等奖","cyid":"7926016","imageurl":"http://img21.vikecn.com/Task/2011-2/24/607473_2011224174226.gif"}]
     */

    private ItemInfoBean itemInfo;
    private List<ZhongbiaoGaojianListBean> zhongbiao_gaojian_list;

    public ItemInfoBean getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(ItemInfoBean itemInfo) {
        this.itemInfo = itemInfo;
    }

    public List<ZhongbiaoGaojianListBean> getZhongbiao_gaojian_list() {
        return zhongbiao_gaojian_list;
    }

    public void setZhongbiao_gaojian_list(List<ZhongbiaoGaojianListBean> zhongbiao_gaojian_list) {
        this.zhongbiao_gaojian_list = zhongbiao_gaojian_list;
    }

    public static class ItemInfoBean {
        /**
         * shouji : 11780234112
         * itemid : 429204
         * err_msg :
         * err_code : 0
         * addtime : 2017-04-25
         * itemname : 营销策划
         */

        private String shouji;
        private String itemid;
        private String err_msg;
        private String err_code;
        private String addtime;
        private String itemname;

        public String getShouji() {
            return shouji;
        }

        public void setShouji(String shouji) {
            this.shouji = shouji;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getErr_msg() {
            return err_msg;
        }

        public void setErr_msg(String err_msg) {
            this.err_msg = err_msg;
        }

        public String getErr_code() {
            return err_code;
        }

        public void setErr_code(String err_code) {
            this.err_code = err_code;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }
    }

    public static class ZhongbiaoGaojianListBean {
        /**
         * username_vk : gguu2
         * itemid : 429204
         * zhongbiao : 一等奖
         * cyid : 7920830
         * imageurl : http://img21.vikecn.com/Task/2011-2/24/607473_2011224174226.gif
         */

        private String username_vk;
        private String itemid;
        private String zhongbiao;
        private String cyid;
        private String imageurl;

        public String getUsername_vk() {
            return username_vk;
        }

        public void setUsername_vk(String username_vk) {
            this.username_vk = username_vk;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getZhongbiao() {
            return zhongbiao;
        }

        public void setZhongbiao(String zhongbiao) {
            this.zhongbiao = zhongbiao;
        }

        public String getCyid() {
            return cyid;
        }

        public void setCyid(String cyid) {
            this.cyid = cyid;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }
    }
}
