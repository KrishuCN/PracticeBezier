package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/14.
 */

public class FuwuListGson {


    /**
     * list : [{"money":"100","unit":"个","title":"天猫店铺使用表情设计维护","id":"39010","check":"1","typename":"名片设计","imgUrl":"http://sp2.680.com/myfile/2016-4/26/32016042611544267492_583067.jpg?8372","showmoney":"￥100"},{"money":"150","unit":"张","title":"测试","id":"47648","check":"1","typename":"VI设计","imgUrl":"http://sp2.680.com/myfile/2016-4/22/32016042214484393694_583067.jpg?8372","showmoney":"￥150"},{"money":"10","unit":"条","title":"测试网站建设","id":"47647","check":"1","typename":"LOGO设计","imgUrl":"http://sp2.680.com/myfile/2016-4/22/32016042215421565164_583067.jpg?8372","showmoney":"￥10"}]
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
         * money : 100
         * unit : 个
         * title : 天猫店铺使用表情设计维护
         * id : 39010
         * check : 1
         * typename : 名片设计
         * imgUrl : http://sp2.680.com/myfile/2016-4/26/32016042611544267492_583067.jpg?8372
         * showmoney : ￥100
         */

        private String money;
        private String unit;
        private String title;
        private String id;
        private String check;
        private String typename;
        private String imgUrl;
        private String showmoney;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
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

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getShowmoney() {
            return showmoney;
        }

        public void setShowmoney(String showmoney) {
            this.showmoney = showmoney;
        }
    }
}
