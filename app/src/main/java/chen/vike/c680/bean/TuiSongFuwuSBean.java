package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2018/3/7.
 */

public class TuiSongFuwuSBean {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * cjnum : 2599
         * imageurl : http://vip.head.p.680.com/2017-5/24/s/22017052418165063529_10416580.jpg
         * shopname : 米旭品牌设计
         * check : 8
         * goodval : 98.2%
         * goodpjper : 98.2
         * check_imgurl : http://app.680.com/images/vip/vip_8.png
         * userid : 10416580
         * cityname : 上海
         */

        private String cjnum;
        private String imageurl;
        private String shopname;
        private String check;
        private String goodval;
        private String goodpjper;
        private String check_imgurl;
        private String userid;
        private String cityname = "";

        public String getCjnum() {
            return cjnum;
        }

        public void setCjnum(String cjnum) {
            this.cjnum = cjnum;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getGoodval() {
            return goodval;
        }

        public void setGoodval(String goodval) {
            this.goodval = goodval;
        }

        public String getGoodpjper() {
            return goodpjper;
        }

        public void setGoodpjper(String goodpjper) {
            this.goodpjper = goodpjper;
        }

        public String getCheck_imgurl() {
            return check_imgurl;
        }

        public void setCheck_imgurl(String check_imgurl) {
            this.check_imgurl = check_imgurl;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }
    }
}
