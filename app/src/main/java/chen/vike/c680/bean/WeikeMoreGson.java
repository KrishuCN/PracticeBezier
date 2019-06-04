package chen.vike.c680.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lht on 2017/2/28.
 */

public class WeikeMoreGson {


    /**
     * item_list : [{"itemid":"423079","price":"100","itemname":"酒窖平面布局图","endtime":"1天19时53分","field_id":"1","itemtype":"xuanshang","content":"附件里面有消防图，用天正打开可以选择出平面结构图，做一个简单的平面布局思路，后期深化另说。要求：1、整个4层的空间大概15000平米，做酒窖展示用，预设大小酒窖164"},{"itemid":"423018","price":"200","itemname":"新聚丰饲料logo设计","endtime":"6天2时43分","field_id":"1","itemtype":"xuanshang","content":"符合行业特征，形象、简洁，国际化。"},{"itemid":"423019","price":"200","itemname":"跃茂饲料logo设计","endtime":"6天2时44分","field_id":"1","itemtype":"xuanshang","content":"相符合行业特征，国际化、专业化、简洁、识别性强"},{"itemid":"423015","price":"600","itemname":"饲令倌饲料logo设计","endtime":"6天2时41分","field_id":"1","itemtype":"xuanshang","content":"猪卡通敬礼（像部队的司令一样）；形象的展示饲令倌是饲料中的司令官。"},{"itemid":"423053","price":"50","itemname":"急需：招商Word文档美化","endtime":"7天6时31分","field_id":"1","itemtype":"xuanshang","content":"1、具视觉冲击力；2、美化的效果与招商的主题相符3、可以根据美化的效果任意修改已做好的word4、能够中标后，根据本人需要，提供个别地方的修改5、美化后"}]
     * pagerInfo : {"prePageIndex":1,"nextPageIndex":3,"beginPageIndex":1,"endPageIndex":10,"pageTotal":8576,"currPageIndex":2,"recordCount":42876}
     */

    private PagerInfoBean pagerInfo;
    private List<ItemListBean> item_list = new ArrayList<>();

    public PagerInfoBean getPagerInfo() {
        return pagerInfo;
    }

    public void setPagerInfo(PagerInfoBean pagerInfo) {
        this.pagerInfo = pagerInfo;
    }

    public List<ItemListBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemListBean> item_list) {
        this.item_list = item_list;
    }

    public static class PagerInfoBean {
        /**
         * prePageIndex : 1
         * nextPageIndex : 3
         * beginPageIndex : 1
         * endPageIndex : 10
         * pageTotal : 8576
         * currPageIndex : 2
         * recordCount : 42876
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

    public static class ItemListBean {
        /**
         * itemid : 423079
         * price : 100
         * itemname : 酒窖平面布局图
         * endtime : 1天19时53分
         * field_id : 1
         * itemtype : xuanshang
         * content : 附件里面有消防图，用天正打开可以选择出平面结构图，做一个简单的平面布局思路，后期深化另说。要求：1、整个4层的空间大概15000平米，做酒窖展示用，预设大小酒窖164
         */

        private String itemid;
        private String price;
        private String itemname;
        private String endtime;
        private String field_id;
        private String itemtype;
        private String content;

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
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

        public String getField_id() {
            return field_id;
        }

        public void setField_id(String field_id) {
            this.field_id = field_id;
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
