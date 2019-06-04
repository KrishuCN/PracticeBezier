package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/10/20.
 */

public class VipGoodBean {

    /**
     * list : [{"money":"1000","endtime":"9天22时59分","itemid":"445955","content":"以中国茶文化为主题，设计两款吉他的外观，包括包装，吊牌，内标（类似附件图）。设计","zab_do":"0","itemname":"设计两款ukulele（尤克里里）","rowId":"1"},{"money":"488","endtime":"6天21时26分","itemid":"445947","content":"公司（或产品）的概述：设计两个LOGO用于商标注册LOGO设计要求：","zab_do":"0","itemname":"北新艾格全房实木定制家具、挪亚木门 LOGO设计 LOGO设计","rowId":"2"},{"money":"5000-10000","endtime":"28天21时20分","itemid":"445942","content":"\t需要开发一款由用户自行拖放小程序的模版及公众号开发模版，此模版中要包含各行","zab_do":"1","itemname":"微信开发小程序公众号开发","rowId":"3"},{"money":"800","endtime":"4天15时54分","itemid":"445894","content":"LOGO名称：4个文字logo设计\t以下四个文字logo设计","zab_do":"0","itemname":"设计4个LOGO设计","rowId":"4"},{"money":"10000-30000","endtime":"23天18时50分","itemid":"445347","content":"详情电话联系18231289776","zab_do":"1","itemname":"插件","rowId":"5"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":0,"beginPageIndex":0,"endPageIndex":0,"pageTotal":1,"currPageIndex":1,"recordCount":5}
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
         * nextPageIndex : 0
         * beginPageIndex : 0.0
         * endPageIndex : 0.0
         * pageTotal : 1
         * currPageIndex : 1
         * recordCount : 5
         */

        private int prePageIndex;
        private int nextPageIndex;
        private double beginPageIndex;
        private double endPageIndex;
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

        public double getBeginPageIndex() {
            return beginPageIndex;
        }

        public void setBeginPageIndex(double beginPageIndex) {
            this.beginPageIndex = beginPageIndex;
        }

        public double getEndPageIndex() {
            return endPageIndex;
        }

        public void setEndPageIndex(double endPageIndex) {
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
         * money : 1000
         * endtime : 9天22时59分
         * itemid : 445955
         * content : 以中国茶文化为主题，设计两款吉他的外观，包括包装，吊牌，内标（类似附件图）。设计
         * zab_do : 0
         * itemname : 设计两款ukulele（尤克里里）
         * rowId : 1
         */

        private String money;
        private String endtime;
        private String itemid;
        private String content;
        private String zab_do;
        private String itemname;
        private String rowId;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
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

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getRowId() {
            return rowId;
        }

        public void setRowId(String rowId) {
            this.rowId = rowId;
        }
    }
}
