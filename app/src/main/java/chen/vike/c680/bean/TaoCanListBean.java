package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2018/5/11.
 */

public class TaoCanListBean {

    private List<TaocanListBean> taocan_list;

    public List<TaocanListBean> getTaocan_list() {
        return taocan_list;
    }

    public void setTaocan_list(List<TaocanListBean> taocan_list) {
        this.taocan_list = taocan_list;
    }

    public static class TaocanListBean {
        /**
         * typecode : logo
         * typename : LOGO
         * colname_list : [{"tc_oldprice":"1000","tc_note3":"至少10个logo设计方案","tc_price":"588","tc_note2":"至少3名钻石级以上设计师","tc_name":"经济套餐","tc_note1":"适合于个体户、工作室","tc_id":"11"},{"tc_oldprice":"2500","tc_note3":"至少15个logo设计方案","tc_price":"998","tc_note2":"至少5名钻石级以上设计师","tc_name":"标准型套餐","tc_note1":"适合于小公司、私营业主","tc_id":"12"},{"tc_oldprice":"3800","tc_note3":"至少25个logo设计方案","tc_price":"1998","tc_note2":"至少10名钻石级以上设计师","tc_name":"豪华型套餐","tc_note1":"适合于普通公司、中小型企业","tc_id":"13"},{"tc_oldprice":"8000","tc_note3":"至少30个logo设计方案","tc_price":"3000+","tc_note2":"至少10名皇冠级以上设计师","tc_name":"定制型套餐","tc_note1":"适合于大型企业、品牌理念强","tc_id":"14"}]
         */

        private String typecode;
        private String typename;
        private List<ColnameListBean> colname_list;

        public String getTypecode() {
            return typecode;
        }

        public void setTypecode(String typecode) {
            this.typecode = typecode;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public List<ColnameListBean> getColname_list() {
            return colname_list;
        }

        public void setColname_list(List<ColnameListBean> colname_list) {
            this.colname_list = colname_list;
        }

        public static class ColnameListBean {
            /**
             * tc_oldprice : 1000
             * tc_note3 : 至少10个logo设计方案
             * tc_price : 588
             * tc_note2 : 至少3名钻石级以上设计师
             * tc_name : 经济套餐
             * tc_note1 : 适合于个体户、工作室
             * tc_id : 11
             */

            private String tc_oldprice;
            private String tc_note3;
            private String tc_price;
            private String tc_note2;
            private String tc_name;
            private String tc_note1;
            private String tc_id;

            public String getTc_oldprice() {
                return tc_oldprice;
            }

            public void setTc_oldprice(String tc_oldprice) {
                this.tc_oldprice = tc_oldprice;
            }

            public String getTc_note3() {
                return tc_note3;
            }

            public void setTc_note3(String tc_note3) {
                this.tc_note3 = tc_note3;
            }

            public String getTc_price() {
                return tc_price;
            }

            public void setTc_price(String tc_price) {
                this.tc_price = tc_price;
            }

            public String getTc_note2() {
                return tc_note2;
            }

            public void setTc_note2(String tc_note2) {
                this.tc_note2 = tc_note2;
            }

            public String getTc_name() {
                return tc_name;
            }

            public void setTc_name(String tc_name) {
                this.tc_name = tc_name;
            }

            public String getTc_note1() {
                return tc_note1;
            }

            public void setTc_note1(String tc_note1) {
                this.tc_note1 = tc_note1;
            }

            public String getTc_id() {
                return tc_id;
            }

            public void setTc_id(String tc_id) {
                this.tc_id = tc_id;
            }
        }
    }
}
