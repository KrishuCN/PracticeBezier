package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/16.
 */

public class MyCollectionGson {


    /**
     * ProjectList : [{"num":"76","name":"金融行业私募基金公司LOGO设计","state":"1","oname":"Sheji,logo","itemid":"404016","addtime":"2016/08/04 14:03:53","type":"0","itemurl":"http://m.680.com/logo/404016.html","jindu":"2","money":"400","class1id":"1","id":"404016","yusuan1":"0","time":"2016/08/14 10:26:19","payok":"1","yusuan2":"0"},{"num":"11","name":"制作母婴用品商城","state":"0","oname":"Web,jianzhan","itemid":"403650","addtime":"2016/07/24 14:38:44","type":"1","itemurl":"http://m.680.com/jianzhan/403650.html","jindu":"10","money":"50","class1id":"2","id":"403650","yusuan1":"1000","time":"2016/07/30 14:39:35","payok":"1","yusuan2":"5000"},{"num":"19","name":"无修改！餐饮店logo设计！ ","state":"1","oname":"Sheji,logo","itemid":"402274","addtime":"2016/07/12 21:44:56","type":"0","itemurl":"http://m.680.com/logo/402274.html","jindu":"2","money":"100","class1id":"1","id":"402274","yusuan1":"0","time":"2016/07/17 21:45:23","payok":"1","yusuan2":"0"},{"num":"9","name":"公司红酒宣传册，折页，酒箱，酒盒的设计。","state":"0","oname":"Sheji,xuanchuance","itemid":"402217","addtime":"2016/07/13 09:37:23","type":"0","itemurl":"http://m.680.com/xuanchuance/402217.html","jindu":"2","money":"200","class1id":"1","id":"402217","yusuan1":"0","time":"2016/07/15 12:18:29","payok":"1","yusuan2":"0"},{"num":"24","name":"无修改！影视道具租赁公司logo设计！","state":"1","oname":"Sheji,logo","itemid":"402172","addtime":"2016/07/11 21:40:04","type":"0","itemurl":"http://m.680.com/logo/402172.html","jindu":"2","money":"200","class1id":"1","id":"402172","yusuan1":"0","time":"2016/08/16 21:40:28","payok":"1","yusuan2":"0"},{"num":"20","name":"工作室LOGO设计","state":"1","oname":"Sheji,logo","itemid":"402098","addtime":"2016/07/11 11:00:21","type":"0","itemurl":"http://m.680.com/logo/402098.html","jindu":"2","money":"200","class1id":"1","id":"402098","yusuan1":"0","time":"2016/08/10 10:59:24","payok":"1","yusuan2":"0"},{"num":"18","name":"公司logo设计","state":"1","oname":"Sheji,logo","itemid":"402083","addtime":"2016/07/11 09:41:41","type":"0","itemurl":"http://m.680.com/logo/402083.html","jindu":"2","money":"100","class1id":"1","id":"402083","yusuan1":"0","time":"2016/07/18 09:41:18","payok":"1","yusuan2":"0"},{"num":"7","name":"取名","state":"0","oname":"Sheji,mingpian","itemid":"402078","addtime":"2016/07/11 09:02:11","type":"0","itemurl":"http://m.680.com/mingpian/402078.html","jindu":"2","money":"111","class1id":"1","id":"402078","yusuan1":"0","time":"2016/07/12 09:02:25","payok":"1","yusuan2":"0"},{"num":"53","name":"便蜜 LOGO设计","state":"1","oname":"Sheji,logo","itemid":"402017","addtime":"2016/07/13 09:58:25","type":"0","itemurl":"http://m.680.com/logo/402017.html","jindu":"2","money":"400","class1id":"1","id":"402017","yusuan1":"0","time":"2016/07/17 10:03:41","payok":"1","yusuan2":"0"},{"num":"92","name":"西店小海鲜 LOGO设计","state":"1","oname":"Sheji,logo","itemid":"401763","addtime":"2016/07/11 14:56:00","type":"0","itemurl":"http://m.680.com/logo/401763.html","jindu":"2","money":"1800","class1id":"1","id":"401763","yusuan1":"0","time":"2016/07/13 10:25:14","payok":"1","yusuan2":"0"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":2,"pageTotal":2,"currPageIndex":1,"recordCount":13}
     */

    private PagerInfoBean pagerInfo;
    private List<ProjectListBean> ProjectList;

    public PagerInfoBean getPagerInfo() {
        return pagerInfo;
    }

    public void setPagerInfo(PagerInfoBean pagerInfo) {
        this.pagerInfo = pagerInfo;
    }

    public List<ProjectListBean> getProjectList() {
        return ProjectList;
    }

    public void setProjectList(List<ProjectListBean> ProjectList) {
        this.ProjectList = ProjectList;
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

    public static class ProjectListBean {
        /**
         * num : 76
         * name : 金融行业私募基金公司LOGO设计
         * state : 1
         * oname : Sheji,logo
         * itemid : 404016
         * addtime : 2016/08/04 14:03:53
         * type : 0
         * itemurl : http://m.680.com/logo/404016.html
         * jindu : 2
         * money : 400
         * class1id : 1
         * id : 404016
         * yusuan1 : 0
         * time : 2016/08/14 10:26:19
         * payok : 1
         * yusuan2 : 0
         */

        private String num;
        private String name;
        private String state;
        private String oname;
        private String itemid;
        private String addtime;
        private String type;
        private String itemurl;
        private String jindu;
        private String money;
        private String class1id;
        private String id;
        private String yusuan1;
        private String time;
        private String payok;
        private String yusuan2;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getOname() {
            return oname;
        }

        public void setOname(String oname) {
            this.oname = oname;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getItemurl() {
            return itemurl;
        }

        public void setItemurl(String itemurl) {
            this.itemurl = itemurl;
        }

        public String getJindu() {
            return jindu;
        }

        public void setJindu(String jindu) {
            this.jindu = jindu;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getClass1id() {
            return class1id;
        }

        public void setClass1id(String class1id) {
            this.class1id = class1id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getYusuan1() {
            return yusuan1;
        }

        public void setYusuan1(String yusuan1) {
            this.yusuan1 = yusuan1;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPayok() {
            return payok;
        }

        public void setPayok(String payok) {
            this.payok = payok;
        }

        public String getYusuan2() {
            return yusuan2;
        }

        public void setYusuan2(String yusuan2) {
            this.yusuan2 = yusuan2;
        }
    }
}
