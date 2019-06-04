package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2018/6/1.
 */

public class GuZhuMoreBean {

    /**
     * cainixihuan : [{"pingfen":"5.00","deal_count":"473","field_id":"1","unit":"个","fuwu_name":"logo设计/企业logo/公司logo/品牌logo/网站logo/餐饮logo设计专业设计，满意为止！","imgurl":"http://p1.shopimg.680.com/2017-3/7/32017030711354992725_10416580.jpg","fuwu_id":"52767","dianpu_name":"米旭品牌设计","cityname":"上海","price":"￥686"},{"pingfen":"5.00","deal_count":"0","field_id":"1","unit":"个起","fuwu_name":"LOGO设计企业餐饮品牌地产金融汽车科技地产教育标志图形商标","imgurl":"http://shop.p.680.com/2017-10/26/32017102614452851467_10565632.jpg","fuwu_id":"59457","dianpu_name":"元道设计","cityname":"成都","price":"￥1999"},{"pingfen":"5.00","deal_count":"0","field_id":"12","unit":"起","fuwu_name":"影像服务/社会活动/会议影像记录/后期制作 ","imgurl":"http://shop.p.680.com/2018-1/5/32018010519222716143_10587811.jpg","fuwu_id":"73032","dianpu_name":"天下摄影","cityname":"北京","price":"￥5000"},{"pingfen":"5.00","deal_count":"247","field_id":"1","unit":"套","fuwu_name":"VI设计/原创VI设计/企业公司VI设计/品牌VI设计，总监设计满意为止！","imgurl":"http://p1.shopimg.680.com/2017-3/7/32017030713294334210_10416580.jpg","fuwu_id":"52773","dianpu_name":"米旭品牌设计","cityname":"上海","price":"￥2996"},{"pingfen":"5.00","deal_count":"0","field_id":"35","unit":"起","fuwu_name":"APP开发/定制开发/原生开发/混合开发/详谈","imgurl":"http://shop.p.680.com/2017-11/16/32017111615402622828_10575912.png","fuwu_id":"63096","dianpu_name":"三智慧通北京科技有限公司","cityname":"北京","price":"￥20000"},{"pingfen":"5.00","deal_count":"320","field_id":"1","unit":"页","fuwu_name":"宣传册设计/企业画册设计/产品样册设计/招商手册设计，总监设计满意为止！","imgurl":"http://p1.shopimg.680.com/2017-3/7/32017030718241655795_10416580.jpg","fuwu_id":"52774","dianpu_name":"米旭品牌设计","cityname":"上海","price":"￥98"},{"pingfen":"5.00","deal_count":"1","field_id":"1","unit":"起","fuwu_name":"logo设计","imgurl":"http://p1.shopimg.680.com/2017-5/31/32017053109585888914_10458327.jpg","fuwu_id":"54602","dianpu_name":"意空间设计团队","cityname":"成都","price":"￥599"},{"pingfen":"5.00","deal_count":"21","field_id":"1","unit":"套","fuwu_name":"卡通形象设计/品牌人物卡通形象设计/整体卡通形象设计企业","imgurl":"http://fj.p.680.com/myfile/2017-2/16/32017021616272792533_10411039.jpg","fuwu_id":"52382","dianpu_name":"雨夜品牌策划","cityname":"广州","price":"￥600"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":10,"pageTotal":5638,"currPageIndex":1,"recordCount":45103}
     */

    private PagerInfoBean pagerInfo;
    private List<CainixihuanBean> cainixihuan;

    public PagerInfoBean getPagerInfo() {
        return pagerInfo;
    }

    public void setPagerInfo(PagerInfoBean pagerInfo) {
        this.pagerInfo = pagerInfo;
    }

    public List<CainixihuanBean> getCainixihuan() {
        return cainixihuan;
    }

    public void setCainixihuan(List<CainixihuanBean> cainixihuan) {
        this.cainixihuan = cainixihuan;
    }

    public static class PagerInfoBean {
        /**
         * prePageIndex : 0
         * nextPageIndex : 2
         * beginPageIndex : 1.0
         * endPageIndex : 10.0
         * pageTotal : 5638
         * currPageIndex : 1
         * recordCount : 45103
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

    public static class CainixihuanBean {
        /**
         * pingfen : 5.00
         * deal_count : 473
         * field_id : 1
         * unit : 个
         * fuwu_name : logo设计/企业logo/公司logo/品牌logo/网站logo/餐饮logo设计专业设计，满意为止！
         * imgurl : http://p1.shopimg.680.com/2017-3/7/32017030711354992725_10416580.jpg
         * fuwu_id : 52767
         * dianpu_name : 米旭品牌设计
         * cityname : 上海
         * price : ￥686
         */

        private String pingfen;
        private String deal_count;
        private String field_id;
        private String unit;
        private String fuwu_name;
        private String imgurl;
        private String fuwu_id;
        private String dianpu_name;
        private String cityname;
        private String price;

        public String getPingfen() {
            return pingfen;
        }

        public void setPingfen(String pingfen) {
            this.pingfen = pingfen;
        }

        public String getDeal_count() {
            return deal_count;
        }

        public void setDeal_count(String deal_count) {
            this.deal_count = deal_count;
        }

        public String getField_id() {
            return field_id;
        }

        public void setField_id(String field_id) {
            this.field_id = field_id;
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

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getFuwu_id() {
            return fuwu_id;
        }

        public void setFuwu_id(String fuwu_id) {
            this.fuwu_id = fuwu_id;
        }

        public String getDianpu_name() {
            return dianpu_name;
        }

        public void setDianpu_name(String dianpu_name) {
            this.dianpu_name = dianpu_name;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
