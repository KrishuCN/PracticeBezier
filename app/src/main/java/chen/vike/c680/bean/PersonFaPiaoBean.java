package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/12/15.
 */

public class PersonFaPiaoBean {

    /**
     * err_code : 0
     * err_msg :
     * items : [{"itemid":"435845","itemname":"","money":"10元","fpsjr":"sdfsdfsd","ctime":"未审核","state":"暂无效","explain":null}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":0,"beginPageIndex":0,"endPageIndex":0,"pageTotal":1,"currPageIndex":1,"recordCount":2}
     */

    private String err_code;
    private String err_msg;
    private PagerInfoBean pagerInfo;
    private List<ItemsBean> items;

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

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class PagerInfoBean {
        /**
         * prePageIndex : 0
         * nextPageIndex : 0
         * beginPageIndex : 0
         * endPageIndex : 0
         * pageTotal : 1
         * currPageIndex : 1
         * recordCount : 2
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

    public static class ItemsBean {
        /**
         * itemid : 435845
         * itemname :
         * money : 10元
         * fpsjr : sdfsdfsd
         * ctime : 未审核
         * state : 暂无效
         * explain : null
         */

        private String itemid;
        private String itemname;
        private String money;
        private String fpsjr;
        private String ctime;
        private String state;
        private Object explain;
        private String statecode;
        private String fpid;
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

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getFpsjr() {
            return fpsjr;
        }

        public void setFpsjr(String fpsjr) {
            this.fpsjr = fpsjr;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Object getExplain() {
            return explain;
        }

        public void setExplain(Object explain) {
            this.explain = explain;
        }

        public String getStatecode() {
            return statecode;
        }

        public void setStatecode(String statecode) {
            this.statecode = statecode;
        }

        public String getFpid() {
            return fpid;
        }

        public void setFpid(String fpid) {
            this.fpid = fpid;
        }
    }

}
