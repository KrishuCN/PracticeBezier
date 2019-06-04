package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/20.
 */

public class FuWuGson {


    /**
     * list : [{"fwid":"46538","typename":"活动策划","paixu_f":"100.00","unit":"篇","price":"￥500","ucheck":"5","title":"活动策划/创意策划/营销策划","price_old":"￥1000","areaname":"湖北省","imgurl":"http://sp2.680.com/myfile/2016-1/13/22016011312451383772_3034529.jpg?0102","cjnum":"0","pcheck":"2"},{"fwid":"45402","typename":"LOGO设计","paixu_f":"7.99","unit":"个","price":"￥800","ucheck":"8","title":"LOGO设计/品牌logo/企业标志/资深设计师/助跑企业腾飞","price_old":"￥3000","areaname":"辽宁省","imgurl":"http://sp2.680.com/myfile/2016-4/19/22016041912524353843_9331803.jpg?0102","cjnum":"1460","pcheck":"2"},{"fwid":"47742","typename":"宣传册页","paixu_f":"7.30","unit":"个","price":"￥1500","ucheck":"8","title":"精品服务企业画册/餐饮地产酒店精品画册/欢迎咨询","price_old":"0","areaname":"江苏省","imgurl":"http://sp2.680.com/myfile/2016-4/27/32016042722034216866_1312478.jpg?0102","cjnum":"5","pcheck":"2"},{"fwid":"47716","typename":"宣传册页","paixu_f":"7.12","unit":"套起","price":"￥79","ucheck":"6","title":"公司精品宣传册设计高端格调画册设计形象推广宣传册设计","price_old":"￥3000","areaname":"广东省","imgurl":"http://sp2.680.com/myfile/2016-4/27/32016042714252428987_3005669.jpg?0102","cjnum":"32","pcheck":"2"},{"fwid":"44725","typename":"封面设计","paixu_f":"7.06","unit":"套","price":"￥498","ucheck":"8","title":"资深设计师设计/画册设计/封面设计特价","price_old":"￥996","areaname":"江苏省","imgurl":"http://sp2.680.com/myfile/2015-8/24/2015082420583925508_1312478.jpg?0102","cjnum":"3","pcheck":"2"},{"fwid":"48956","typename":"软件开发","paixu_f":"7.02","unit":"起","price":"￥1800","ucheck":"7","title":"软件定制开发","price_old":"0","areaname":"广东省","imgurl":"http://fj.p.680.com/myfile/2016-7/19/32016071912445230808_1989620.jpg?0102","cjnum":"2","pcheck":"2"},{"fwid":"48958","typename":"整站建设","paixu_f":"7.00","unit":"起","price":"￥3800","ucheck":"7","title":"应用级整站系统开发","price_old":"0","areaname":"广东省","imgurl":"http://fj.p.680.com/myfile/2016-7/19/32016071913060393781_1989620.jpg?0102","cjnum":"9","pcheck":"2"},{"fwid":"47741","typename":"LOGO设计","paixu_f":"7.00","unit":"个","price":"￥500","ucheck":"8","title":"特惠专场企业商业网站Logo设计/标志设计/商标设计","price_old":"￥1000","areaname":"江苏省","imgurl":"http://sp2.680.com/myfile/2016-4/27/32016042720464030730_1312478.jpg?0102","cjnum":"2","pcheck":"2"},{"fwid":"52382","typename":"卡通吉祥物","paixu_f":"6.91","unit":"套","price":"￥600","ucheck":"7","title":"卡通形象设计/品牌人物卡通形象设计/整体卡通形象设计企业","price_old":"￥668","areaname":"广东省","imgurl":"http://fj.p.680.com/myfile/2017-2/16/32017021616272792533_10411039.jpg?0102","cjnum":"12","pcheck":"2"},{"fwid":"43510","typename":"包装设计","paixu_f":"6.90","unit":"起","price":"￥3800","ucheck":"7","title":"产品包装设计,茶业,食品包装设计","price_old":"￥5000","areaname":"四川省","imgurl":"http://fj.p.680.com/myfile/2016-10/21/32016102116473345344_3231694.png?0102","cjnum":"3","pcheck":"2"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":10,"pageTotal":1066,"currPageIndex":1,"recordCount":10654}
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
         * pageTotal : 1066
         * currPageIndex : 1
         * recordCount : 10654
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
         * fwid : 46538
         * typename : 活动策划
         * paixu_f : 100.00
         * unit : 篇
         * price : ￥500
         * ucheck : 5
         * title : 活动策划/创意策划/营销策划
         * price_old : ￥1000
         * areaname : 湖北省
         * imgurl : http://sp2.680.com/myfile/2016-1/13/22016011312451383772_3034529.jpg?0102
         * cjnum : 0
         * pcheck : 2
         */

        private String fwid;
        private String typename;
        private String paixu_f;
        private String unit;
        private String price;
        private String ucheck;
        private String title;
        private String price_old;
        private String areaname;
        private String imgurl;
        private String cjnum;
        private String pcheck;

        public String getFwid() {
            return fwid;
        }

        public void setFwid(String fwid) {
            this.fwid = fwid;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getPaixu_f() {
            return paixu_f;
        }

        public void setPaixu_f(String paixu_f) {
            this.paixu_f = paixu_f;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUcheck() {
            return ucheck;
        }

        public void setUcheck(String ucheck) {
            this.ucheck = ucheck;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice_old() {
            return price_old;
        }

        public void setPrice_old(String price_old) {
            this.price_old = price_old;
        }

        public String getAreaname() {
            return areaname;
        }

        public void setAreaname(String areaname) {
            this.areaname = areaname;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getCjnum() {
            return cjnum;
        }

        public void setCjnum(String cjnum) {
            this.cjnum = cjnum;
        }

        public String getPcheck() {
            return pcheck;
        }

        public void setPcheck(String pcheck) {
            this.pcheck = pcheck;
        }
    }
}
