package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/11/10.
 */

public class DianPuBean {

    /**
     * list : [{"id":"39010","money":"100","cjnum":"1","typename":"名片设计","type3_t":"1","fileurl_f":"http://sp2.680.com/myfile/2016-4/26/32016042611544267492_583067.jpg","title":"天猫店铺使用表情设计维护","salenum_f":"1","imgUrl":"http://sp2.680.com/myfile/2016-4/26/32016042611544267492_583067.jpg?8850","rowId":"1","showmoney":"￥100"},{"id":"53130","money":"12","cjnum":"0","typename":"封面设计","type3_t":"24","fileurl_f":"http://m2.p.680.com/UserPhotoFiles/2017-3/22/2017032211350097052_0.jpg","title":"gjm","salenum_f":"0","imgUrl":"http://m2.p.680.com/UserPhotoFiles/2017-3/22/2017032211350097052_0.jpg?8850","rowId":"2","showmoney":"￥12"},{"id":"47647","money":"10","cjnum":"0","typename":"LOGO设计","type3_t":"2","fileurl_f":"http://sp2.680.com/myfile/2016-4/22/32016042215421565164_583067.jpg","title":"测试网站建设","salenum_f":"0","imgUrl":"http://sp2.680.com/myfile/2016-4/22/32016042215421565164_583067.jpg?8850","rowId":"3","showmoney":"￥10"},{"id":"39125","money":"2000","cjnum":"0","typename":"包装设计","type3_t":"5","fileurl_f":"http://p6.680.com/myfile/2014-1/13/2014011315571800412_583067.jpg","title":"包装设计","salenum_f":"0","imgUrl":"http://p6.680.com/myfile/2014-1/13/2014011315571800412_583067.jpg?8850","rowId":"4","showmoney":"￥2000"},{"id":"110","money":"11","cjnum":"8","typename":"","type3_t":"171","fileurl_f":"","title":"网络兼职","salenum_f":"8","imgUrl":"http://app.680.com/images/nopic.jpg","rowId":"5","showmoney":"￥11"},{"id":"62","money":"0","cjnum":"0","typename":"","type3_t":"336","fileurl_f":"0","title":"一封匿名求爱情书","salenum_f":"0","imgUrl":"0?8850","rowId":"6","showmoney":"议价"},{"id":"61","money":"2","cjnum":"0","typename":"论文","type3_t":"61","fileurl_f":"0","title":"毕业设计：校园网络购物网站设计","salenum_f":"0","imgUrl":"0?8850","rowId":"7","showmoney":"￥2"},{"id":"60","money":"0","cjnum":"0","typename":"","type3_t":"349","fileurl_f":"0","title":"金融写作的几点经验","salenum_f":"0","imgUrl":"0?8850","rowId":"8","showmoney":"议价"},{"id":"59","money":"0","cjnum":"0","typename":"","type3_t":"349","fileurl_f":"0","title":"大学生公司采购部社会实践报告","salenum_f":"0","imgUrl":"0?8850","rowId":"9","showmoney":"议价"},{"id":"58","money":"0","cjnum":"0","typename":"","type3_t":"349","fileurl_f":"0","title":"暑期实践个人总结","salenum_f":"0","imgUrl":"0?8850","rowId":"10","showmoney":"议价"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":2,"pageTotal":2,"currPageIndex":1,"recordCount":12}
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
         * recordCount : 12
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
         * id : 39010
         * money : 100
         * cjnum : 1
         * typename : 名片设计
         * type3_t : 1
         * fileurl_f : http://sp2.680.com/myfile/2016-4/26/32016042611544267492_583067.jpg
         * title : 天猫店铺使用表情设计维护
         * salenum_f : 1
         * imgUrl : http://sp2.680.com/myfile/2016-4/26/32016042611544267492_583067.jpg?8850
         * rowId : 1
         * showmoney : ￥100
         */

        private String id;
        private String money;
        private String cjnum;
        private String typename;
        private String type3_t;
        private String fileurl_f;
        private String title;
        private String salenum_f;
        private String imgUrl;
        private String rowId;
        private String showmoney;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCjnum() {
            return cjnum;
        }

        public void setCjnum(String cjnum) {
            this.cjnum = cjnum;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getType3_t() {
            return type3_t;
        }

        public void setType3_t(String type3_t) {
            this.type3_t = type3_t;
        }

        public String getFileurl_f() {
            return fileurl_f;
        }

        public void setFileurl_f(String fileurl_f) {
            this.fileurl_f = fileurl_f;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSalenum_f() {
            return salenum_f;
        }

        public void setSalenum_f(String salenum_f) {
            this.salenum_f = salenum_f;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getRowId() {
            return rowId;
        }

        public void setRowId(String rowId) {
            this.rowId = rowId;
        }

        public String getShowmoney() {
            return showmoney;
        }

        public void setShowmoney(String showmoney) {
            this.showmoney = showmoney;
        }
    }
}
