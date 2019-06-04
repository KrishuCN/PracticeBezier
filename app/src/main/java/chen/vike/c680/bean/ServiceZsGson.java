package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/20.
 */

public class ServiceZsGson {

    /**
     * top_scroll_ad : [{"imgurl":"http://app.680.com/images/v3/ex/app_sbzc_banner_640_200.jpg","title":"商标注册","type":"weburl","targetval":"http://m.680.com/sb/"}]
     * fuwu_rel_class_list : [{"type":"smallclass","classid":"2","classname":"LOGO设计"},{"type":"smallclass","classid":"4","classname":"VI设计"},{"type":"smallclass","classid":"5","classname":"包装设计"}]
     * list : [{"imgurl":"http://sp2.680.com/myfile/2016-4/19/22016041912524353843_9331803.jpg?2588","title":"LOGO设计/品牌logo/企业标志/资深设计师/助跑企业腾飞","fwid":"45402","cjnum":"1463","price":"￥800","paixu_f":"7.99","price_old":"￥3000"},{"imgurl":"http://sp2.680.com/myfile/2016-4/27/32016042722034216866_1312478.jpg?2588","title":"精品服务企业画册/餐饮地产酒店精品画册/欢迎咨询","fwid":"47742","cjnum":"5","price":"￥1500","paixu_f":"7.30","price_old":"0"},{"imgurl":"http://sp2.680.com/myfile/2016-4/27/32016042714252428987_3005669.jpg?2588","title":"公司精品宣传册设计高端格调画册设计形象推广宣传册设计","fwid":"47716","cjnum":"32","price":"￥79","paixu_f":"7.12","price_old":"￥3000"},{"imgurl":"http://sp2.680.com/myfile/2015-8/24/2015082420583925508_1312478.jpg?2588","title":"资深设计师设计/画册设计/封面设计特价","fwid":"44725","cjnum":"3","price":"￥498","paixu_f":"7.06","price_old":"￥996"},{"imgurl":"http://sp2.680.com/myfile/2016-4/27/32016042720464030730_1312478.jpg?2588","title":"特惠专场企业商业网站Logo设计/标志设计/商标设计","fwid":"47741","cjnum":"2","price":"￥500","paixu_f":"7.00","price_old":"￥1000"},{"imgurl":"http://fj.p.680.com/myfile/2017-2/16/32017021616272792533_10411039.jpg?2588","title":"卡通形象设计/品牌人物卡通形象设计/整体卡通形象设计企业","fwid":"52382","cjnum":"12","price":"￥600","paixu_f":"6.91","price_old":"￥668"},{"imgurl":"http://fj.p.680.com/myfile/2016-10/21/32016102116473345344_3231694.png?2588","title":"产品包装设计,茶业,食品包装设计","fwid":"43510","cjnum":"3","price":"￥3800","paixu_f":"6.90","price_old":"￥5000"},{"imgurl":"http://sp2.680.com/myfile/2015-8/24/2015082422341315904_1312478.jpg?2588","title":"定制名片设计/创意名片/个性名片","fwid":"44728","cjnum":"4","price":"￥99","paixu_f":"6.88","price_old":"￥298"},{"imgurl":"http://sp2.680.com/myfile/2016-5/3/32016050310384374575_2714781.jpg?2588","title":"总监操刀LOGO设计原创品牌公司企业标志设计LOGO满意为止","fwid":"47861","cjnum":"24","price":"￥500","paixu_f":"6.80","price_old":"￥999"},{"imgurl":"http://sp2.680.com/myfile/2015-8/24/2015082422140484393_1312478.jpg?2588","title":"品牌logo/产品logo/公司logo","fwid":"44726","cjnum":"12","price":"￥598","paixu_f":"6.77","price_old":"￥688"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":10,"pageTotal":396,"currPageIndex":1,"recordCount":3951}
     */

    private PagerInfoBean pagerInfo;
    private List<TopScrollAdBean> top_scroll_ad;
    private List<FuwuRelClassListBean> fuwu_rel_class_list;
    private List<ListBean> list;

    public PagerInfoBean getPagerInfo() {
        return pagerInfo;
    }

    public void setPagerInfo(PagerInfoBean pagerInfo) {
        this.pagerInfo = pagerInfo;
    }

    public List<TopScrollAdBean> getTop_scroll_ad() {
        return top_scroll_ad;
    }

    public void setTop_scroll_ad(List<TopScrollAdBean> top_scroll_ad) {
        this.top_scroll_ad = top_scroll_ad;
    }

    public List<FuwuRelClassListBean> getFuwu_rel_class_list() {
        return fuwu_rel_class_list;
    }

    public void setFuwu_rel_class_list(List<FuwuRelClassListBean> fuwu_rel_class_list) {
        this.fuwu_rel_class_list = fuwu_rel_class_list;
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
         * pageTotal : 396
         * currPageIndex : 1
         * recordCount : 3951
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

    public static class TopScrollAdBean {
        /**
         * imgurl : http://app.680.com/images/v3/ex/app_sbzc_banner_640_200.jpg
         * title : 商标注册
         * type : weburl
         * targetval : http://m.680.com/sb/
         */

        private String imgurl;
        private String title;
        private String type;
        private String targetval;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTargetval() {
            return targetval;
        }

        public void setTargetval(String targetval) {
            this.targetval = targetval;
        }
    }

    public static class FuwuRelClassListBean {
        /**
         * type : smallclass
         * classid : 2
         * classname : LOGO设计
         */

        private String type;
        private String classid;
        private String classname;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getClassid() {
            return classid;
        }

        public void setClassid(String classid) {
            this.classid = classid;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }
    }

    public static class ListBean {
        /**
         * imgurl : http://sp2.680.com/myfile/2016-4/19/22016041912524353843_9331803.jpg?2588
         * title : LOGO设计/品牌logo/企业标志/资深设计师/助跑企业腾飞
         * fwid : 45402
         * cjnum : 1463
         * price : ￥800
         * paixu_f : 7.99
         * price_old : ￥3000
         */

        private String imgurl;
        private String title;
        private String fwid;
        private String cjnum;
        private String price;
        private String paixu_f;
        private String price_old;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFwid() {
            return fwid;
        }

        public void setFwid(String fwid) {
            this.fwid = fwid;
        }

        public String getCjnum() {
            return cjnum;
        }

        public void setCjnum(String cjnum) {
            this.cjnum = cjnum;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPaixu_f() {
            return paixu_f;
        }

        public void setPaixu_f(String paixu_f) {
            this.paixu_f = paixu_f;
        }

        public String getPrice_old() {
            return price_old;
        }

        public void setPrice_old(String price_old) {
            this.price_old = price_old;
        }
    }
}
