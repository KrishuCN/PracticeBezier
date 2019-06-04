package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/21.
 */

public class WkHotItemGson {


    /**
     * list : [{"payok":"1","rowId":"1","vikecn_class1ID":"1","state":"0","itemname":"农业出口公司LOGO设计","itemid":"425503","gz_userid":"9348906","endtime":"2017/03/24 09:38:52","addtime":"2017/03/21 10:16:22","zab_yusuan1":"10000","gz_username":"妞妈妞妈妞妞妈","gz_imgurl":"http://app.680.com/images/v3/face.jpg?8549","userid":"9348906","money":"400","zab_yusuan2":"20000","yanqi":"0","cynum":"1","zab_do":"0","oname":"Sheji,logo"},{"payok":"1","rowId":"2","vikecn_class1ID":"1","state":"0","itemname":"品牌logo设计","itemid":"425508","gz_userid":"10452473","endtime":"2017/03/28 09:54:11","addtime":"2017/03/21 09:55:09","zab_yusuan1":"0","gz_username":"合金门窗","gz_imgurl":"http://app.680.com/images/v3/face.jpg?8559","userid":"10452473","money":"300","zab_yusuan2":"0","yanqi":"0","cynum":"1","zab_do":"0","oname":"Sheji,logo"},{"payok":"1","rowId":"3","vikecn_class1ID":"1","state":"0","itemname":"公司logo设计","itemid":"425505","gz_userid":"1476205","endtime":"2017/03/24 09:51:23","addtime":"2017/03/21 09:51:48","zab_yusuan1":"0","gz_username":"todayvi","gz_imgurl":"http://p9.680.com/home/2015-4/22/s/2015042208520436080_1476205.jpg?8569","userid":"1476205","money":"100","zab_yusuan2":"0","yanqi":"0","cynum":"0","zab_do":"0","oname":"Sheji,logo"},{"payok":"1","rowId":"4","vikecn_class1ID":"1","state":"0","itemname":"培训学校LOGO设计【发标自投标，全家死翘翘】","itemid":"425486","gz_userid":"10452356","endtime":"2017/03/25 21:48:38","addtime":"2017/03/21 09:28:58","zab_yusuan1":"0","gz_username":"s44060033","gz_imgurl":"http://app.680.com/images/v3/face.jpg?8579","userid":"10452356","money":"200","zab_yusuan2":"0","yanqi":"2","cynum":"4","zab_do":"0","oname":"Sheji,logo"},{"payok":"1","rowId":"5","vikecn_class1ID":"1","state":"0","itemname":"建筑LOGO设计","itemid":"425314","gz_userid":"10451179","endtime":"2017/04/13 14:30:51","addtime":"2017/03/21 09:21:32","zab_yusuan1":"0","gz_username":"Eric海","gz_imgurl":"http://app.680.com/images/v3/face.jpg?8579","userid":"10451179","money":"450","zab_yusuan2":"0","yanqi":"2","cynum":"34","zab_do":"0","oname":"Sheji,logo"},{"payok":"1","rowId":"6","vikecn_class1ID":"1","state":"0","itemname":"51 旅游 平台 LOGO设计","itemid":"425467","gz_userid":"1574055","endtime":"2017/04/09 18:19:00","addtime":"2017/03/21 09:00:00","zab_yusuan1":"0","gz_username":"junjei521","gz_imgurl":"http://app.680.com/images/v3/face.jpg?8589","userid":"1574055","money":"900","zab_yusuan2":"0","yanqi":"0","cynum":"3","zab_do":"0","oname":"Sheji,logo"},{"payok":"1","rowId":"7","vikecn_class1ID":"1","state":"0","itemname":"特警标志！！！急！急！急！","itemid":"425302","gz_userid":"2460851","endtime":"2017/03/22 11:27:27","addtime":"2017/03/21 07:15:04","zab_yusuan1":"0","gz_username":"seesea997","gz_imgurl":"http://app.680.com/images/v3/face.jpg?8599","userid":"2460851","money":"1000","zab_yusuan2":"0","yanqi":"0","cynum":"33","zab_do":"0","oname":"Sheji,logo"},{"payok":"1","rowId":"8","vikecn_class1ID":"1","state":"0","itemname":"logo设计05","itemid":"425491","gz_userid":"3134612","endtime":"2017/03/23 22:32:40","addtime":"2017/03/20 22:32:57","zab_yusuan1":"0","gz_username":"3033390793","gz_imgurl":"http://app.680.com/images/v3/face.jpg?8609","userid":"3134612","money":"100","zab_yusuan2":"0","yanqi":"0","cynum":"8","zab_do":"0","oname":"Sheji,logo"},{"payok":"1","rowId":"9","vikecn_class1ID":"1","state":"0","itemname":"logo设计04","itemid":"425490","gz_userid":"3134612","endtime":"2017/03/23 22:31:28","addtime":"2017/03/20 22:31:44","zab_yusuan1":"0","gz_username":"3033390793","gz_imgurl":"http://app.680.com/images/v3/face.jpg?8609","userid":"3134612","money":"100","zab_yusuan2":"0","yanqi":"0","cynum":"7","zab_do":"0","oname":"Sheji,logo"},{"payok":"1","rowId":"10","vikecn_class1ID":"1","state":"0","itemname":"logo设计03","itemid":"425489","gz_userid":"3134612","endtime":"2017/03/23 22:30:26","addtime":"2017/03/20 22:30:43","zab_yusuan1":"0","gz_username":"3033390793","gz_imgurl":"http://app.680.com/images/v3/face.jpg?8619","userid":"3134612","money":"100","zab_yusuan2":"0","yanqi":"0","cynum":"8","zab_do":"0","oname":"Sheji,logo"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":10,"pageTotal":11,"currPageIndex":1,"recordCount":103}
     */

    private PagerInfoBean pagerInfo;
    private List<ListBean> list;

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
         * pageTotal : 11
         * currPageIndex : 1
         * recordCount : 103
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
         * payok : 1
         * rowId : 1
         * vikecn_class1ID : 1
         * state : 0
         * itemname : 农业出口公司LOGO设计
         * itemid : 425503
         * gz_userid : 9348906
         * endtime : 2017/03/24 09:38:52
         * addtime : 2017/03/21 10:16:22
         * zab_yusuan1 : 10000
         * gz_username : 妞妈妞妈妞妞妈
         * gz_imgurl : http://app.680.com/images/v3/face.jpg?8549
         * userid : 9348906
         * money : 400
         * zab_yusuan2 : 20000
         * yanqi : 0
         * cynum : 1
         * zab_do : 0
         * oname : Sheji,logo
         */

        private String payok;
        private String rowId;
        private String vikecn_class1ID;
        private String state;
        private String itemname;
        private String itemid;
        private String gz_userid;
        private String endtime;
        private String addtime;
        private String zab_yusuan1;
        private String gz_username;
        private String gz_imgurl;
        private String userid;
        private String money;
        private String zab_yusuan2;
        private String yanqi;
        private String cynum;
        private String zab_do;
        private String oname;

        public String getPayok() {
            return payok;
        }

        public void setPayok(String payok) {
            this.payok = payok;
        }

        public String getRowId() {
            return rowId;
        }

        public void setRowId(String rowId) {
            this.rowId = rowId;
        }

        public String getVikecn_class1ID() {
            return vikecn_class1ID;
        }

        public void setVikecn_class1ID(String vikecn_class1ID) {
            this.vikecn_class1ID = vikecn_class1ID;
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

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getGz_userid() {
            return gz_userid;
        }

        public void setGz_userid(String gz_userid) {
            this.gz_userid = gz_userid;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getZab_yusuan1() {
            return zab_yusuan1;
        }

        public void setZab_yusuan1(String zab_yusuan1) {
            this.zab_yusuan1 = zab_yusuan1;
        }

        public String getGz_username() {
            return gz_username;
        }

        public void setGz_username(String gz_username) {
            this.gz_username = gz_username;
        }

        public String getGz_imgurl() {
            return gz_imgurl;
        }

        public void setGz_imgurl(String gz_imgurl) {
            this.gz_imgurl = gz_imgurl;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
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

        public String getYanqi() {
            return yanqi;
        }

        public void setYanqi(String yanqi) {
            this.yanqi = yanqi;
        }

        public String getCynum() {
            return cynum;
        }

        public void setCynum(String cynum) {
            this.cynum = cynum;
        }

        public String getZab_do() {
            return zab_do;
        }

        public void setZab_do(String zab_do) {
            this.zab_do = zab_do;
        }

        public String getOname() {
            return oname;
        }

        public void setOname(String oname) {
            this.oname = oname;
        }
    }
}
