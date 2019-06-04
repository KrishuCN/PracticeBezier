package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/2/25.
 */

public class FuwuMoreGson {


    /**
     * fuwu_list : [{"paixu_f":"7.99","field_id":"1","cityname":"大连","rank":"黄金服务商","unit":"个","fuwu_name":"LOGO设计/品牌logo/企业标志/资深设计师/助跑企业腾飞","price":"￥800","fuwu_id":"45402","deal_count":"1412","imgurl":"http://sp2.680.com/myfile/2016-4/19/22016041912524353843_9331803.jpg"},{"paixu_f":"7.30","field_id":"1","cityname":"无锡","rank":"黄金服务商","unit":"个","fuwu_name":"精品服务企业画册/餐饮地产酒店精品画册/欢迎咨询","price":"￥1500","fuwu_id":"47742","deal_count":"9","imgurl":"http://sp2.680.com/myfile/2016-4/27/32016042722034216866_1312478.jpg"},{"paixu_f":"7.12","field_id":"1","cityname":"深圳","rank":"黄金服务商","unit":"套起","fuwu_name":"公司精品宣传册设计高端格调画册设计形象推广宣传册设计","price":"￥1000","fuwu_id":"47716","deal_count":"25","imgurl":"http://sp2.680.com/myfile/2016-4/27/32016042714252428987_3005669.jpg"},{"paixu_f":"7.06","field_id":"1","cityname":"无锡","rank":"黄金服务商","unit":"套","fuwu_name":"资深设计师设计/画册设计/封面设计特价","price":"￥498","fuwu_id":"44725","deal_count":"7","imgurl":"http://sp2.680.com/myfile/2015-8/24/2015082420583925508_1312478.jpg"},{"paixu_f":"7.00","field_id":"1","cityname":"无锡","rank":"黄金服务商","unit":"个","fuwu_name":"特惠专场企业商业网站Logo设计/标志设计/商标设计","price":"￥500","fuwu_id":"47741","deal_count":"4","imgurl":"http://sp2.680.com/myfile/2016-4/27/32016042720464030730_1312478.jpg"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":10,"pageTotal":103,"currPageIndex":1,"recordCount":513}
     */

    private PagerInfoBean pagerInfo;
    private List<FuwuListBean> fuwu_list;

    public PagerInfoBean getPagerInfo() {
        return pagerInfo;
    }

    public void setPagerInfo(PagerInfoBean pagerInfo) {
        this.pagerInfo = pagerInfo;
    }

    public List<FuwuListBean> getFuwu_list() {
        return fuwu_list;
    }

    public void setFuwu_list(List<FuwuListBean> fuwu_list) {
        this.fuwu_list = fuwu_list;
    }

    public static class PagerInfoBean {
        /**
         * prePageIndex : 0
         * nextPageIndex : 2
         * beginPageIndex : 1
         * endPageIndex : 10
         * pageTotal : 103
         * currPageIndex : 1
         * recordCount : 513
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

    public static class FuwuListBean {
        /**
         * paixu_f : 7.99
         * field_id : 1
         * cityname : 大连
         * rank : 黄金服务商
         * unit : 个
         * fuwu_name : LOGO设计/品牌logo/企业标志/资深设计师/助跑企业腾飞
         * price : ￥800
         * fuwu_id : 45402
         * deal_count : 1412
         * imgurl : http://sp2.680.com/myfile/2016-4/19/22016041912524353843_9331803.jpg
         */

//        private String paixu_f;
        private String field_id;
        private String cityname;
        private String rank;
        private String unit;
        private String fuwu_name;
        private String price;
        private String fuwu_id;
        private String deal_count;
        private String imgurl;

//        public String getPaixu_f() {
//            return paixu_f;
//        }
//
//        public void setPaixu_f(String paixu_f) {
//            this.paixu_f = paixu_f;
//        }

        public String getField_id() {
            return field_id;
        }

        public void setField_id(String field_id) {
            this.field_id = field_id;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getFuwu_name() {
            return fuwu_name;
        }

        public void setFuwu_name(String fuwu_name) {
            this.fuwu_name = fuwu_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getFuwu_id() {
            return fuwu_id;
        }

        public void setFuwu_id(String fuwu_id) {
            this.fuwu_id = fuwu_id;
        }

        public String getDeal_count() {
            return deal_count;
        }

        public void setDeal_count(String deal_count) {
            this.deal_count = deal_count;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
