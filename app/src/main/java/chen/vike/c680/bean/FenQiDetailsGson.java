package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/6/5.
 */

public class FenQiDetailsGson {


    private List<FenqiListBean> fenqi_list;

    public List<FenqiListBean> getFenqi_list() {
        return fenqi_list;
    }

    public void setFenqi_list(List<FenqiListBean> fenqi_list) {
        this.fenqi_list = fenqi_list;
    }

    public static class FenqiListBean {
        /**
         * shouji :
         * itemid : 368767
         * fenqi_total_money : 2500
         * userid : 9366401
         * fenqishu : 1
         * fenqi_topay_money : 2500
         * iszhuankuan : 0
         * beizhu : 参看合同
         * fenqi_id : 7392727
         * istuoguan : 1
         */

        private String shouji;
        private String itemid;
        private String fenqi_total_money;
        private String userid;
        private String fenqishu;
        private String fenqi_topay_money;
        private String iszhuankuan;
        private String beizhu;
        private String fenqi_id;
        private String istuoguan;

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

        public String getFenqi_total_money() {
            return fenqi_total_money;
        }

        public void setFenqi_total_money(String fenqi_total_money) {
            this.fenqi_total_money = fenqi_total_money;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getFenqishu() {
            return fenqishu;
        }

        public void setFenqishu(String fenqishu) {
            this.fenqishu = fenqishu;
        }

        public String getFenqi_topay_money() {
            return fenqi_topay_money;
        }

        public void setFenqi_topay_money(String fenqi_topay_money) {
            this.fenqi_topay_money = fenqi_topay_money;
        }

        public String getIszhuankuan() {
            return iszhuankuan;
        }

        public void setIszhuankuan(String iszhuankuan) {
            this.iszhuankuan = iszhuankuan;
        }

        public String getBeizhu() {
            return beizhu;
        }

        public void setBeizhu(String beizhu) {
            this.beizhu = beizhu;
        }

        public String getFenqi_id() {
            return fenqi_id;
        }

        public void setFenqi_id(String fenqi_id) {
            this.fenqi_id = fenqi_id;
        }

        public String getIstuoguan() {
            return istuoguan;
        }

        public void setIstuoguan(String istuoguan) {
            this.istuoguan = istuoguan;
        }
    }
}
