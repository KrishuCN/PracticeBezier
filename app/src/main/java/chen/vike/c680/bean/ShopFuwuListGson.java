package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/15.
 */

public class ShopFuwuListGson {


    /**
     * list : [{"imgUrl":"http://sp2.680.com/myfile/2016-4/26/32016042611544267492_583067.jpg?8436","check":"1","unit":"个","money":"100","title":"天猫店铺使用表情设计维护","id":"39010","showmoney":"￥100","typename":"名片设计","cjnum":"1"},{"imgUrl":"http://sp2.680.com/myfile/2016-4/22/32016042214484393694_583067.jpg?8466","check":"1","unit":"张","money":"150","title":"测试","id":"47648","showmoney":"￥150","typename":"VI设计","cjnum":"0"},{"imgUrl":"http://sp2.680.com/myfile/2016-4/22/32016042215421565164_583067.jpg?8496","check":"1","unit":"条","money":"10","title":"测试网站建设","id":"47647","showmoney":"￥10","typename":"LOGO设计","cjnum":"0"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":0,"beginPageIndex":0,"endPageIndex":0,"pageTotal":1,"currPageIndex":1,"recordCount":3}
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
         * beginPageIndex : 0
         * endPageIndex : 0
         * pageTotal : 1
         * currPageIndex : 1
         * recordCount : 3
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
         * imgUrl : http://sp2.680.com/myfile/2016-4/26/32016042611544267492_583067.jpg?8436
         * check : 1
         * unit : 个
         * money : 100
         * title : 天猫店铺使用表情设计维护
         * id : 39010
         * showmoney : ￥100
         * typename : 名片设计
         * cjnum : 1
         */

        private String imgUrl;
        private String check;
        private String unit;
        private String money;
        private String title;
        private String id;
        private String showmoney;
        private String typename;
        private String cjnum;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShowmoney() {
            return showmoney;
        }

        public void setShowmoney(String showmoney) {
            this.showmoney = showmoney;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getCjnum() {
            return cjnum;
        }

        public void setCjnum(String cjnum) {
            this.cjnum = cjnum;
        }
    }
}
