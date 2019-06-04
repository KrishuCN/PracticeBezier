package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/20.
 */

public class ShopGson {


    /**
     * list : [{"userid":"1312478","check":"8","cityname":"无锡","cj":"0","goodpjv":"5","dname_f":"风彩创意设计","fanwei":"LOGO设计/VI设计/包装设计","shopname":"风彩创意设计","imageurl":"http://fj.p.680.com/home/2016-12/23/s/2016122320251226111_1312478.jpg?8872","cjnum":"360","gradepicurl":"http://m.680.com/images/grade/cap5.gif","grade":"18960","paixu_ff":"80.00"},{"userid":"3231694","check":"7","cityname":"成都","cj":"28","goodpjv":"5","dname_f":"吴蜀品牌","fanwei":"LOGO设计/VI设计/包装设计","shopname":"吴蜀品牌","imageurl":"http://fj.p.680.com/home/2016-10/21/s/2016102117041238058_3231694.png?8882","cjnum":"369","gradepicurl":"http://m.680.com/images/grade/cap3.gif","grade":"9326","paixu_ff":"60.00"},{"userid":"9331803","check":"8","cityname":"大连","cj":"2845","goodpjv":"5","dname_f":"龙博品牌设计","fanwei":"LOGO设计/包装设计/VI设计","shopname":"龙博品牌设计","imageurl":"http://fj.p.680.com/home/2016-5/9/s/2016050917510223273_9331803.jpg?8892","cjnum":"4750","gradepicurl":"http://m.680.com/images/grade/crown2.gif","grade":"115643","paixu_ff":"50.00"},{"userid":"9637260","check":"7","cityname":"济南","cj":"34","goodpjv":"5","dname_f":"大熙网络","fanwei":"LOGO设计/包装设计/字体设计","shopname":"大熙网络","imageurl":"http://fj.p.680.com/home/2016-8/11/s/2016081122584973322_9637260.jpg?8902","cjnum":"114","gradepicurl":"http://m.680.com/images/grade/cap3.gif","grade":"14227","paixu_ff":"48.00"},{"userid":"9670860","check":"7","cityname":"成都","cj":"0","goodpjv":"5","dname_f":"早稻设计传媒","fanwei":"LOGO设计/VI设计/包装设计","shopname":"早稻设计传媒","imageurl":"http://fj.p.680.com/home/2016-8/16/s/2016081617270455717_9670860.jpg?8902","cjnum":"33","gradepicurl":"http://m.680.com/images/grade/zuan5.gif","grade":"2590","paixu_ff":"44.00"},{"userid":"9655478","check":"6","cityname":"石家庄","cj":"0","goodpjv":"5","dname_f":"仟木设计","fanwei":"效果图设计/办公室装修/商铺店面装修","shopname":"仟木设计","imageurl":"http://shop.p.680.com/ShenFen/2016-6/26/s/201606260509437375_0.jpg?8912","cjnum":"29","gradepicurl":"http://m.680.com/images/grade/zuan2.gif","grade":"1276","paixu_ff":"15.00"},{"userid":"353457","check":"6","cityname":"烟台","cj":"128","goodpjv":"5","dname_f":"万盟网络科技有限公司","fanwei":"UI设计/整站建设/网页设计","shopname":"万盟网络科技有限公司","imageurl":"http://p7.680.com/home/2014-9/27/s/2014092710020547210_353457.jpg?8922","cjnum":"182","gradepicurl":"http://m.680.com/images/grade/cap2.gif","grade":"7966","paixu_ff":"15.00"},{"userid":"3034529","check":"5","cityname":"武汉","cj":"40","goodpjv":"5","dname_f":"妙笔文案","fanwei":"论文/活动策划/软文","shopname":"妙笔文案","imageurl":"http://p9.680.com/home/2015-4/20/s/2015042016553019724_3034529.jpg?8932","cjnum":"154","gradepicurl":"http://m.680.com/images/grade/cap1.gif","grade":"4382","paixu_ff":"15.00"},{"userid":"10393411","check":"6","cityname":"广州","cj":"0","goodpjv":"5","dname_f":"广州咏赞数码科技有限公司","fanwei":"动画Flash/广告片制作/3D制作","shopname":"广州咏赞数码科技有限公司","imageurl":"http://fj.p.680.com/home/2016-9/16/s/2016091612242291260_10393411.jpg?8942","cjnum":"9","gradepicurl":"http://m.680.com/images/grade/zuan2.gif","grade":"1052","paixu_ff":"12.00"},{"userid":"9641197","check":"6","cityname":"广州","cj":"0","goodpjv":"5","dname_f":"一拳设计工作室","fanwei":"VI设计/LOGO设计/包装设计","shopname":"一拳设计工作室","imageurl":"http://fj.p.680.com/home/2016-8/9/s/2016080908392736584_9641197.jpg?8952","cjnum":"25","gradepicurl":"http://m.680.com/images/grade/zuan2.gif","grade":"1172","paixu_ff":"12.00"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":10,"pageTotal":10156,"currPageIndex":1,"recordCount":101554}
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
         * pageTotal : 10156
         * currPageIndex : 1
         * recordCount : 101554
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
         * userid : 1312478
         * check : 8
         * cityname : 无锡
         * cj : 0
         * goodpjv : 5
         * dname_f : 风彩创意设计
         * fanwei : LOGO设计/VI设计/包装设计
         * shopname : 风彩创意设计
         * imageurl : http://fj.p.680.com/home/2016-12/23/s/2016122320251226111_1312478.jpg?8872
         * cjnum : 360
         * gradepicurl : http://m.680.com/images/grade/cap5.gif
         * grade : 18960
         * paixu_ff : 80.00
         */

        private String userid;
        private String check;
        private String cityname;
        private String cj;
        private String goodpjv;
        private String dname_f;
        private String fanwei;
        private String shopname;
        private String imageurl;
        private String cjnum;
        private String gradepicurl;
        private String grade;
        private String paixu_ff;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public String getCj() {
            return cj;
        }

        public void setCj(String cj) {
            this.cj = cj;
        }

        public String getGoodpjv() {
            return goodpjv;
        }

        public void setGoodpjv(String goodpjv) {
            this.goodpjv = goodpjv;
        }

        public String getDname_f() {
            return dname_f;
        }

        public void setDname_f(String dname_f) {
            this.dname_f = dname_f;
        }

        public String getFanwei() {
            return fanwei;
        }

        public void setFanwei(String fanwei) {
            this.fanwei = fanwei;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public String getCjnum() {
            return cjnum;
        }

        public void setCjnum(String cjnum) {
            this.cjnum = cjnum;
        }

        public String getGradepicurl() {
            return gradepicurl;
        }

        public void setGradepicurl(String gradepicurl) {
            this.gradepicurl = gradepicurl;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getPaixu_ff() {
            return paixu_ff;
        }

        public void setPaixu_ff(String paixu_ff) {
            this.paixu_ff = paixu_ff;
        }
    }
}
