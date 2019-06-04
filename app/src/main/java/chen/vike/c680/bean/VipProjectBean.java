package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/10/20.
 */

public class VipProjectBean {

    /**
     * list : [{"tid":"70422","ischakan":"1","itemname":"餐饮LOGO设计","endtime":"已到期","itemid":"444064","content":"LOGO名称：湘旺跳跳蛙我是做餐饮的，以牛蛙为主。设计一个卡通的牛蛙搭配我的","zab_do":"0","money":"58"},{"tid":"69618","ischakan":"1","itemname":"网站模板，dedecms模板，详见附件","endtime":"已到期","itemid":"443541","content":"详见附件~~网站类型：企业官网功能模块：新闻系统，产品展示，分享功能，在","zab_do":"0","money":"1000"},{"tid":"69591","ischakan":"1","itemname":"公司LOGO设计","endtime":"已到期","itemid":"443538","content":"LOGO名称：丝路研学详细要求见附件附件：http://d4.file.","zab_do":"0","money":"888"},{"tid":"45466","ischakan":"1","itemname":"水性金属漆的包装设计","endtime":"已到期","itemid":"422458","content":"产品为水性金属漆，现需要做一个外包装贴纸，要求包括图案、商标区域、介绍产品区贮存","zab_do":"0","money":"400"},{"tid":"411","ischakan":"1","itemname":"金属制品公司LOGO设计","endtime":"已到期","itemid":"392160","content":"宁波丰华金属制品有限公司。 主营：生产阀门及阀门配件，铜材配件，管件类金属加工。","zab_do":"0","money":"700"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":2,"pageTotal":2,"currPageIndex":1,"recordCount":10}
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
         * beginPageIndex : 1.0
         * endPageIndex : 2.0
         * pageTotal : 2
         * currPageIndex : 1
         * recordCount : 10
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
         * tid : 70422
         * ischakan : 1
         * itemname : 餐饮LOGO设计
         * endtime : 已到期
         * itemid : 444064
         * content : LOGO名称：湘旺跳跳蛙我是做餐饮的，以牛蛙为主。设计一个卡通的牛蛙搭配我的
         * zab_do : 0
         * money : 58
         */

        private String tid;
        private String ischakan;
        private String itemname;
        private String endtime;
        private String itemid;
        private String content;
        private String zab_do;
        private String money;

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getIschakan() {
            return ischakan;
        }

        public void setIschakan(String ischakan) {
            this.ischakan = ischakan;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
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

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
