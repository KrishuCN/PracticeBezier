package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/2/28.
 */

public class WeiKePageGson {


    /**
     * result : 200
     * user_classid : 1
     * tabImage : [{"imgurl":"http://app.680.com/images/app/vk_index_ad1_1.jpg","newsid":"89436"},{"imgurl":"http://app.680.com/images/app/vk_index_ad1_2.jpg","newsid":"89430"}]
     * tabImage2 : [{"imgurl":"http://app.680.com/images/app/vk_index_ad2_1.jpg","itemid":"420653","itemtype":"xuanshang"},{"imgurl":"http://app.680.com/images/app/vk_index_ad2_2.jpg","itemid":"422905","itemtype":"xuanshang"}]
     * item_list : [{"field_id":"1","price":"1188","itemname":"昆明舟白化工设备有限公司 LOGO设计","endtime":"6天23时36分","itemid":"423125","itemtype":"xuanshang","content":"公司（或产品）的概述：化工设备LOGO设计要求：舟白从\u201c舶\u201d字而来，\u201c舶\u201d即大船，希望商标有大海航行的寓意。"},{"field_id":"1","price":"100","itemname":"汽车空调滤的宣传海报设计","endtime":"14天23时43分","itemid":"423128","itemtype":"xuanshang","content":"目前我们需要设计一张海报，主要是汽车空调滤的宣传，我提供一条标语和简单的描述，请根据这些文字设计。如下：主题及标语：爱由芯生---汉格斯特空调滤"},{"field_id":"1","price":"200","itemname":"景源装饰装修公司LOGO设计","endtime":"4天23时42分","itemid":"423127","itemtype":"xuanshang","content":"任务详情一、公司LOGO设计要求：1.公司名称为景源装饰有限公司，从事行业为室内设计，施工。2.LOGO设计应简洁明了、大气美观、构思新颖、具有强"},{"field_id":"1","price":"500","itemname":"食品油类味品道LOGO设计","endtime":"14天23时19分","itemid":"423122","itemtype":"xuanshang","content":"经营食品油类的注册公司，公司名称味品道，英文Taste Good ，广告标语味品道，地道好味道。欲设计一logo。"},{"field_id":"1","price":"100","itemname":"工业涂料公司LOGO","endtime":"29天23时5分","itemid":"423118","itemtype":"xuanshang","content":"公司LOGO制作，我公司是销售工业涂料的，要求LOGO简单清晰，需包含BW coatings"}]
     */

    private String result = "";
    private int user_classid;
    private List<TabImageBean> tabImage;
    private List<TabImage2Bean> tabImage2;
    private List<ItemListBean> item_list;
    private List<TophuandengBean> tophuandeng;
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getUser_classid() {
        return user_classid;
    }

    public void setUser_classid(int user_classid) {
        this.user_classid = user_classid;
    }

    public List<TabImageBean> getTabImage() {
        return tabImage;
    }

    public void setTabImage(List<TabImageBean> tabImage) {
        this.tabImage = tabImage;
    }

    public List<TabImage2Bean> getTabImage2() {
        return tabImage2;
    }

    public void setTabImage2(List<TabImage2Bean> tabImage2) {
        this.tabImage2 = tabImage2;
    }

    public List<ItemListBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemListBean> item_list) {
        this.item_list = item_list;
    }

    public List<TophuandengBean> getTophuandeng() {
        return tophuandeng;
    }

    public void setTophuandeng(List<TophuandengBean> tophuandeng) {
        this.tophuandeng = tophuandeng;
    }

    public static class TophuandengBean {
        /**
         * imgurl : http://shop.p.680.com/home/2017-12/5/201712051541315212_1312478.jpg
         * infotype : shop
         * infoname : 风彩创意设计
         * infoid : 1312478
         */

        private String imgurl = "";
        private String infotype = "";
        private String infoname = "";
        private String infoid = "";

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getInfotype() {
            return infotype;
        }

        public void setInfotype(String infotype) {
            this.infotype = infotype;
        }

        public String getInfoname() {
            return infoname;
        }

        public void setInfoname(String infoname) {
            this.infoname = infoname;
        }

        public String getInfoid() {
            return infoid;
        }

        public void setInfoid(String infoid) {
            this.infoid = infoid;
        }
    }

    public static class TabImageBean {
        /**
         * imgurl : http://app.680.com/images/app/vk_index_ad1_1.jpg
         * newsid : 89436
         */

        private String imgurl = "";
        private String newsid = "";

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getNewsid() {
            return newsid;
        }

        public void setNewsid(String newsid) {
            this.newsid = newsid;
        }
    }

    public static class TabImage2Bean {
        /**
         * imgurl : http://app.680.com/images/app/vk_index_ad2_1.jpg
         * itemid : 420653
         * itemtype : xuanshang
         */

        private String imgurl = "";
        private String itemid = "";
        private String itemtype = "";

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getItemtype() {
            return itemtype;
        }

        public void setItemtype(String itemtype) {
            this.itemtype = itemtype;
        }
    }

    public static class ItemListBean {
        /**
         * field_id : 1
         * price : 1188
         * itemname : 昆明舟白化工设备有限公司 LOGO设计
         * endtime : 6天23时36分
         * itemid : 423125
         * itemtype : xuanshang
         * content : 公司（或产品）的概述：化工设备LOGO设计要求：舟白从“舶”字而来，“舶”即大船，希望商标有大海航行的寓意。
         */

        private String field_id = "";
        private String price = "";
        private String itemname = "";
        private String endtime = "";
        private String itemid = "";
        private String itemtype = "";
        private String content = "";

        public String getField_id() {
            return field_id;
        }

        public void setField_id(String field_id) {
            this.field_id = field_id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getItemtype() {
            return itemtype;
        }

        public void setItemtype(String itemtype) {
            this.itemtype = itemtype;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
