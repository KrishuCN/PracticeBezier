package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/15.
 */

public class ShopZuopinGson {


    /**
     * list : [{"zuopinimg4":"http://p9.680.com/zuopin/2015-10/15/2015101517511094176_583067.jpg","zuopinimg5":"http://p9.680.com/zuopin/2015-10/15/2015101517511522701_583067.jpg","zuopinname":"dasdasdasdfdfsdf","zuopinid":"113829","zuopinimg3":"http://p9.680.com/zuopin/2015-10/15/2015101517510706454_583067.jpg","zuopinimg2":"http://p9.680.com/zuopin/2015-10/15/2015101517510287930_583067.jpg","rowId":"1","zuopinimg_s":"http://p9.680.com/zuopin/2015-10/15/2015101519044376016_583067.jpg","zuopinimg":"http://p9.680.com/zuopin/2015-10/15/2015101519044376016_583067.jpg?6597"},{"zuopinimg4":"","zuopinimg5":"","zuopinname":"vvvv","zuopinid":"108490","zuopinimg3":"","zuopinimg2":"","rowId":"2","zuopinimg_s":"","zuopinimg":"http://p3.680.com/task/2013-1/9/2013010915230492950_583067.JPG?6597"},{"zuopinimg4":"","zuopinimg5":"","zuopinname":"dsfsdfsdfsdfsdf","zuopinid":"103311","zuopinimg3":"","zuopinimg2":"","rowId":"3","zuopinimg_s":"","zuopinimg":"http://p3.680.com/Zuopin/2012-7/20/145637734_583067.jpg?6597"},{"zuopinimg4":"","zuopinimg5":"","zuopinname":"gfgdfgdfgdfgd","zuopinid":"103310","zuopinimg3":"","zuopinimg2":"","rowId":"4","zuopinimg_s":"","zuopinimg":"http://p1.680.com/Zuopin/2012-7/20/145438734_583067.jpg?6597"},{"zuopinimg4":"","zuopinimg5":"","zuopinname":"asdasd","zuopinid":"90026","zuopinimg3":"","zuopinimg2":"","rowId":"5","zuopinimg_s":"","zuopinimg":"http://p4.vikecn.com/Zuopin/2011-9/15/85623734_583067.png?6597"},{"zuopinimg4":"","zuopinimg5":"","zuopinname":"xczxc","zuopinid":"89474","zuopinimg3":"","zuopinimg2":"","rowId":"6","zuopinimg_s":"","zuopinimg":"http://p2.7000.com/Zuopin/2011-8/29/10150734_583067.gif?6597"},{"zuopinimg4":"","zuopinimg5":"","zuopinname":"114206263_583067","zuopinid":"71367","zuopinimg3":"","zuopinimg2":"","rowId":"7","zuopinimg_s":"","zuopinimg":"http://img20.vikecn.com/Zuopin/2010-12/11/114206263_583067.jpg?6597"},{"zuopinimg4":"","zuopinimg5":"","zuopinname":"114128699_583067","zuopinid":"71366","zuopinimg3":"","zuopinimg2":"","rowId":"8","zuopinimg_s":"","zuopinimg":"http://img20.vikecn.com/Zuopin/2010-12/11/114128699_583067.gif?6597"},{"zuopinimg4":"","zuopinimg5":"","zuopinname":"100703215_583067","zuopinid":"71361","zuopinimg3":"","zuopinimg2":"","rowId":"9","zuopinimg_s":"","zuopinimg":"http://img20.vikecn.com/Zuopin/2010-12/11/100703215_583067.gif?6597"},{"zuopinimg4":"","zuopinimg5":"","zuopinname":"bbbb1111","zuopinid":"46252","zuopinimg3":"","zuopinimg2":"","rowId":"10","zuopinimg_s":"","zuopinimg":"http://img5.vikecn.com/Myfile/2009-5/6/133473266_583067.jpg?6597"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":2,"pageTotal":2,"currPageIndex":1,"recordCount":13}
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
         * endPageIndex : 2
         * pageTotal : 2
         * currPageIndex : 1
         * recordCount : 13
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
         * zuopinimg4 : http://p9.680.com/zuopin/2015-10/15/2015101517511094176_583067.jpg
         * zuopinimg5 : http://p9.680.com/zuopin/2015-10/15/2015101517511522701_583067.jpg
         * zuopinname : dasdasdasdfdfsdf
         * zuopinid : 113829
         * zuopinimg3 : http://p9.680.com/zuopin/2015-10/15/2015101517510706454_583067.jpg
         * zuopinimg2 : http://p9.680.com/zuopin/2015-10/15/2015101517510287930_583067.jpg
         * rowId : 1
         * zuopinimg_s : http://p9.680.com/zuopin/2015-10/15/2015101519044376016_583067.jpg
         * zuopinimg : http://p9.680.com/zuopin/2015-10/15/2015101519044376016_583067.jpg?6597
         */

        private String zuopinimg4;
        private String zuopinimg5;
        private String zuopinname;
        private String zuopinid;
        private String zuopinimg3;
        private String zuopinimg2;
        private String rowId;
        private String zuopinimg_s;
        private String zuopinimg;

        public String getZuopinimg4() {
            return zuopinimg4;
        }

        public void setZuopinimg4(String zuopinimg4) {
            this.zuopinimg4 = zuopinimg4;
        }

        public String getZuopinimg5() {
            return zuopinimg5;
        }

        public void setZuopinimg5(String zuopinimg5) {
            this.zuopinimg5 = zuopinimg5;
        }

        public String getZuopinname() {
            return zuopinname;
        }

        public void setZuopinname(String zuopinname) {
            this.zuopinname = zuopinname;
        }

        public String getZuopinid() {
            return zuopinid;
        }

        public void setZuopinid(String zuopinid) {
            this.zuopinid = zuopinid;
        }

        public String getZuopinimg3() {
            return zuopinimg3;
        }

        public void setZuopinimg3(String zuopinimg3) {
            this.zuopinimg3 = zuopinimg3;
        }

        public String getZuopinimg2() {
            return zuopinimg2;
        }

        public void setZuopinimg2(String zuopinimg2) {
            this.zuopinimg2 = zuopinimg2;
        }

        public String getRowId() {
            return rowId;
        }

        public void setRowId(String rowId) {
            this.rowId = rowId;
        }

        public String getZuopinimg_s() {
            return zuopinimg_s;
        }

        public void setZuopinimg_s(String zuopinimg_s) {
            this.zuopinimg_s = zuopinimg_s;
        }

        public String getZuopinimg() {
            return zuopinimg;
        }

        public void setZuopinimg(String zuopinimg) {
            this.zuopinimg = zuopinimg;
        }
    }
}
