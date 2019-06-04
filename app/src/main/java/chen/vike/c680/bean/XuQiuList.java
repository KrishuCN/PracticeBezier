package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/9.
 */

public class XuQiuList {
    /**
     * list : [{"addtime":"2017/06/13 15:00:50","tjnum":"0","payok":"1","tuoguan_all_btn":"0","zab_pay":"0","vikecn_class1ID":"15","is_can_delete":"0","check":"0","itemid":"433695","zab_do":"0","state":"-10","itemname":"新房装修","zab_yusuan2":"0","hasfenqi":"0","jindu":"0","zab_yusuan1":"0","cynum":"0","rowId":"1","money":"1","zab_yusuan":"","cus_check":"0","dingjin_btn":"0","endtime":"2017/06/28 15:01:03","fenqi_btn":"0"},{"addtime":"2017/06/08 23:55:29","tjnum":"0","payok":"0","tuoguan_all_btn":"1","zab_pay":"0","vikecn_class1ID":"1","is_can_delete":"1","check":"0","itemid":"433228","zab_do":"0","state":"-10","itemname":"公司logo设计公司设计公司设计设计公司设计设计公司设计设计公司设计设计公司设计","zab_yusuan2":"0","hasfenqi":"0","jindu":"0","zab_yusuan1":"0","cynum":"0","rowId":"2","money":"588","zab_yusuan":"","cus_check":"0","dingjin_btn":"0","endtime":"2017/06/19 23:55:37","fenqi_btn":"0"},{"addtime":"2017/06/06 05:23:59","tjnum":"0","payok":"0","tuoguan_all_btn":"1","zab_pay":"0","vikecn_class1ID":"1","is_can_delete":"1","check":"0","itemid":"432898","zab_do":"0","state":"-10","itemname":"XXX公司LOGO设计","zab_yusuan2":"0","hasfenqi":"0","jindu":"0","zab_yusuan1":"0","cynum":"0","rowId":"3","money":"588","zab_yusuan":"","cus_check":"0","dingjin_btn":"0","endtime":"2017/06/17 05:25:02","fenqi_btn":"0"},{"addtime":"2017/06/06 05:22:15","tjnum":"0","payok":"0","tuoguan_all_btn":"1","zab_pay":"0","vikecn_class1ID":"0","is_can_delete":"1","check":"0","itemid":"432897","zab_do":"0","state":"-10","itemname":"XXX公司LOGO设计","zab_yusuan2":"0","hasfenqi":"0","jindu":"0","zab_yusuan1":"0","cynum":"0","rowId":"4","money":"111","zab_yusuan":"","cus_check":"0","dingjin_btn":"0","endtime":"2017/06/17 05:23:18","fenqi_btn":"0"},{"addtime":"2017/06/06 04:30:42","tjnum":"0","payok":"0","tuoguan_all_btn":"1","zab_pay":"0","vikecn_class1ID":"1","is_can_delete":"1","check":"0","itemid":"432895","zab_do":"0","state":"-10","itemname":"XXX公司LOGO设计","zab_yusuan2":"0","hasfenqi":"0","jindu":"0","zab_yusuan1":"0","cynum":"0","rowId":"5","money":"588","zab_yusuan":"","cus_check":"0","dingjin_btn":"0","endtime":"2017/06/17 04:31:44","fenqi_btn":"0"},{"addtime":"2017/06/05 15:13:31","tjnum":"0","payok":"0","tuoguan_all_btn":"1","zab_pay":"0","vikecn_class1ID":"1","is_can_delete":"1","check":"0","itemid":"432830","zab_do":"5","state":"-10","itemname":"XXX公司LOGO设计","zab_yusuan2":"0","hasfenqi":"0","jindu":"0","zab_yusuan1":"0","cynum":"0","rowId":"6","money":"3000","zab_yusuan":"","cus_check":"0","dingjin_btn":"0","endtime":"2017/07/05 15:14:30","fenqi_btn":"0"},{"addtime":"2017/06/05 10:20:04","tjnum":"0","payok":"0","tuoguan_all_btn":"1","zab_pay":"0","vikecn_class1ID":"15","is_can_delete":"1","check":"0","itemid":"432802","zab_do":"0","state":"-10","itemname":"XXX公司LOGO设计","zab_yusuan2":"0","hasfenqi":"0","jindu":"0","zab_yusuan1":"0","cynum":"0","rowId":"7","money":"5000","zab_yusuan":"","cus_check":"0","dingjin_btn":"0","endtime":"2017/06/18 10:20:09","fenqi_btn":"0"},{"addtime":"2017/06/05 10:17:55","tjnum":"0","payok":"0","tuoguan_all_btn":"1","zab_pay":"0","vikecn_class1ID":"1","is_can_delete":"1","check":"0","itemid":"432801","zab_do":"0","state":"-10","itemname":"XXX公司LOGO设计","zab_yusuan2":"0","hasfenqi":"0","jindu":"0","zab_yusuan1":"0","cynum":"0","rowId":"8","money":"588","zab_yusuan":"","cus_check":"0","dingjin_btn":"0","endtime":"2017/06/25 10:18:00","fenqi_btn":"0"},{"addtime":"2017/05/29 13:42:04","tjnum":"0","payok":"1","tuoguan_all_btn":"0","zab_pay":"1","vikecn_class1ID":"1","is_can_delete":"0","check":"2","itemid":"432328","zab_do":"1","state":"5","itemname":"包装设计","zab_yusuan2":"1000","hasfenqi":"0","jindu":"4","zab_yusuan1":"1000","cynum":"0","rowId":"9","money":"100","zab_yusuan":"101","cus_check":"1","dingjin_btn":"0","endtime":"2017/05/30 13:42:04","fenqi_btn":"0"},{"addtime":"2017/05/27 14:34:56","tjnum":"0","payok":"0","tuoguan_all_btn":"1","zab_pay":"0","vikecn_class1ID":"34","is_can_delete":"1","check":"0","itemid":"432072","zab_do":"0","state":"-10","itemname":"程序修改","zab_yusuan2":"0","hasfenqi":"0","jindu":"0","zab_yusuan1":"0","cynum":"0","rowId":"10","money":"500","zab_yusuan":"","cus_check":"0","dingjin_btn":"0","endtime":"2017/06/11 14:35:44","fenqi_btn":"0"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":10,"pageTotal":46,"currPageIndex":1,"recordCount":451}
     */

    private PagerInfoBean pagerInfo;
    private List<ListBean> list;
    private String err_code;
    private String err_msg;
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

    public PagerInfoBean getPagerInfo() {
        return pagerInfo;
    }

    public void setPagerInfo(PagerInfoBean pagerInfo) {
        this.pagerInfo = pagerInfo;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class PagerInfoBean {
        /**
         * prePageIndex : 0
         * nextPageIndex : 2
         * beginPageIndex : 1
         * endPageIndex : 10
         * pageTotal : 46
         * currPageIndex : 1
         * recordCount : 451
         */

        private int prePageIndex;
        private int nextPageIndex;
        private int beginPageIndex;
        private int endPageIndex;
        private int pageTotal;
        private int currPageIndex;
        private int recordCount;

        public int getPrePageIndex() {
            return prePageIndex;
        }

        public void setPrePageIndex(int prePageIndex) {
            this.prePageIndex = prePageIndex;
        }

        public int getNextPageIndex() {
            return nextPageIndex;
        }

        public void setNextPageIndex(int nextPageIndex) {
            this.nextPageIndex = nextPageIndex;
        }

        public int getBeginPageIndex() {
            return beginPageIndex;
        }

        public void setBeginPageIndex(int beginPageIndex) {
            this.beginPageIndex = beginPageIndex;
        }

        public int getEndPageIndex() {
            return endPageIndex;
        }

        public void setEndPageIndex(int endPageIndex) {
            this.endPageIndex = endPageIndex;
        }

        public int getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(int pageTotal) {
            this.pageTotal = pageTotal;
        }

        public int getCurrPageIndex() {
            return currPageIndex;
        }

        public void setCurrPageIndex(int currPageIndex) {
            this.currPageIndex = currPageIndex;
        }

        public int getRecordCount() {
            return recordCount;
        }

        public void setRecordCount(int recordCount) {
            this.recordCount = recordCount;
        }
    }

    public static class ListBean {
        /**
         * addtime : 2017/06/13 15:00:50
         * tjnum : 0
         * payok : 1
         * tuoguan_all_btn : 0
         * zab_pay : 0
         * vikecn_class1ID : 15
         * is_can_delete : 0
         * check : 0
         * itemid : 433695
         * zab_do : 0
         * state : -10
         * itemname : 新房装修
         * zab_yusuan2 : 0
         * hasfenqi : 0
         * jindu : 0
         * zab_yusuan1 : 0
         * cynum : 0
         * rowId : 1
         * money : 1
         * zab_yusuan :
         * cus_check : 0
         * dingjin_btn : 0
         * endtime : 2017/06/28 15:01:03
         * fenqi_btn : 0
         */

        private String addtime;
        private String tjnum;
        private String payok;
        private String tuoguan_all_btn;
        private String zab_pay;
        private String vikecn_class1ID;
        private String is_can_delete;
        private String check;
        private String itemid;
        private String zab_do;
        private String state;
        private String itemname;
        private String zab_yusuan2;
        private String hasfenqi;
        private String jindu;
        private String zab_yusuan1;
        private String cynum;
        private String rowId;
        private String money;
        private String zab_yusuan;
        private String cus_check;
        private String dingjin_btn;
        private String endtime;
        private String fenqi_btn;
        private String guyong_userid;
        private String guyong_username;
         private String con;

        public String getCon() {
            return con;
        }

        public void setCon(String con) {
            this.con = con;
        }

        public String getGuyong_userid() {
            return guyong_userid;
        }

        public void setGuyong_userid(String guyong_userid) {
            this.guyong_userid = guyong_userid;
        }

        public String getGuyong_username() {
            return guyong_username;
        }

        public void setGuyong_username(String guyong_username) {
            this.guyong_username = guyong_username;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getTjnum() {
            return tjnum;
        }

        public void setTjnum(String tjnum) {
            this.tjnum = tjnum;
        }

        public String getPayok() {
            return payok;
        }

        public void setPayok(String payok) {
            this.payok = payok;
        }

        public String getTuoguan_all_btn() {
            return tuoguan_all_btn;
        }

        public void setTuoguan_all_btn(String tuoguan_all_btn) {
            this.tuoguan_all_btn = tuoguan_all_btn;
        }

        public String getZab_pay() {
            return zab_pay;
        }

        public void setZab_pay(String zab_pay) {
            this.zab_pay = zab_pay;
        }

        public String getVikecn_class1ID() {
            return vikecn_class1ID;
        }

        public void setVikecn_class1ID(String vikecn_class1ID) {
            this.vikecn_class1ID = vikecn_class1ID;
        }

        public String getIs_can_delete() {
            return is_can_delete;
        }

        public void setIs_can_delete(String is_can_delete) {
            this.is_can_delete = is_can_delete;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getZab_do() {
            return zab_do;
        }

        public void setZab_do(String zab_do) {
            this.zab_do = zab_do;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getZab_yusuan2() {
            return zab_yusuan2;
        }

        public void setZab_yusuan2(String zab_yusuan2) {
            this.zab_yusuan2 = zab_yusuan2;
        }

        public String getHasfenqi() {
            return hasfenqi;
        }

        public void setHasfenqi(String hasfenqi) {
            this.hasfenqi = hasfenqi;
        }

        public String getJindu() {
            return jindu;
        }

        public void setJindu(String jindu) {
            this.jindu = jindu;
        }

        public String getZab_yusuan1() {
            return zab_yusuan1;
        }

        public void setZab_yusuan1(String zab_yusuan1) {
            this.zab_yusuan1 = zab_yusuan1;
        }

        public String getCynum() {
            return cynum;
        }

        public void setCynum(String cynum) {
            this.cynum = cynum;
        }

        public String getRowId() {
            return rowId;
        }

        public void setRowId(String rowId) {
            this.rowId = rowId;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getZab_yusuan() {
            return zab_yusuan;
        }

        public void setZab_yusuan(String zab_yusuan) {
            this.zab_yusuan = zab_yusuan;
        }

        public String getCus_check() {
            return cus_check;
        }

        public void setCus_check(String cus_check) {
            this.cus_check = cus_check;
        }

        public String getDingjin_btn() {
            return dingjin_btn;
        }

        public void setDingjin_btn(String dingjin_btn) {
            this.dingjin_btn = dingjin_btn;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getFenqi_btn() {
            return fenqi_btn;
        }

        public void setFenqi_btn(String fenqi_btn) {
            this.fenqi_btn = fenqi_btn;
        }
    }
}
