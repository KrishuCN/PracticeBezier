package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/24.
 */

public class SystemListGson {


    /**
     * list : [{"msgtype":"0","context":"123131231231","state":"1","addtime":"03月22日 20:17","type":"4","tui_con_class":"","xid":"4872203","title":"12313123","tui_con_id":"0","tui_con_type":"no","gourl":""},{"msgtype":"0","context":"1254564564","state":"1","addtime":"03月22日 20:02","type":"4","tui_con_class":"","xid":"4872201","title":"5564564","tui_con_id":"0","tui_con_type":"no","gourl":""},{"msgtype":"0","context":"54564","state":"1","addtime":"03月22日 19:59","type":"4","tui_con_class":"","xid":"4872200","title":" 95 5","tui_con_id":"0","tui_con_type":"no","gourl":""},{"msgtype":"0","context":"1231231321","state":"1","addtime":"03月22日 19:58","type":"4","tui_con_class":"","xid":"4872198","title":"见客户见客户","tui_con_id":"0","tui_con_type":"no","gourl":""},{"msgtype":"0","context":"121212121212","state":"1","addtime":"03月22日 19:57","type":"4","tui_con_class":"","xid":"4872197","title":"舒服舒服","tui_con_id":"0","tui_con_type":"no","gourl":""},{"msgtype":"0","context":"测试的第1集","state":"1","addtime":"03月22日 14:40","type":"4","tui_con_class":"xuanshang","xid":"4872015","title":"第1集","tui_con_id":"425643","tui_con_type":"item","gourl":""},{"msgtype":"21","context":"您好！您的项目[网站功能测试(400150)]已评标，请及时联系威客沟通交接源文件：\n威客[zyjeatyou中国]的联系方式：\n联系电话：\n手机：18780234112\n邮箱：602626463@loginQQ.com\n","state":"1","addtime":"02月23日 10:49","type":"2","tui_con_class":"xuanshang","xid":"4861005","title":"项目[400150]已评标通知","tui_con_id":"400150","tui_con_type":"item","gourl":""},{"msgtype":"21","context":"您好！您的项目[网站功能测试(400150)]已评标，请及时联系威客沟通交接源文件：\n威客[zyjeatyou中国]的联系方式：\n联系电话：\n手机：18780234112\n邮箱：602626463@loginQQ.com\n","state":"1","addtime":"02月23日 10:45","type":"2","tui_con_class":"xuanshang","xid":"4861002","title":"项目[400150]已评标通知","tui_con_id":"400150","tui_con_type":"item","gourl":""},{"msgtype":"0","context":"法国今年","state":"1","addtime":"02月11日 16:27","type":"4","tui_con_class":"xuanshang","xid":"4855529","title":"感觉","tui_con_id":"1211","tui_con_type":"item","gourl":""},{"msgtype":"0","context":"只需","state":"1","addtime":"02月11日 14:53","type":"4","tui_con_class":"xuanshang","xid":"4855450","title":"发过火","tui_con_id":"1211","tui_con_type":"item","gourl":""}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":10,"pageTotal":19,"currPageIndex":1,"recordCount":189}
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
         * pageTotal : 19
         * currPageIndex : 1
         * recordCount : 189
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
         * msgtype : 0
         * context : 123131231231
         * state : 1
         * addtime : 03月22日 20:17
         * type : 4
         * tui_con_class :
         * xid : 4872203
         * title : 12313123
         * tui_con_id : 0
         * tui_con_type : no
         * gourl :
         */

        private String msgtype;
        private String context;
        private String state;
        private String addtime;
        private String type;
        private String tui_con_class;
        private String xid;
        private String title;
        private String tui_con_id;
        private String tui_con_type;
        private String gourl;

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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

        public String getTui_con_class() {
            return tui_con_class;
        }

        public void setTui_con_class(String tui_con_class) {
            this.tui_con_class = tui_con_class;
        }

        public String getXid() {
            return xid;
        }

        public void setXid(String xid) {
            this.xid = xid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTui_con_id() {
            return tui_con_id;
        }

        public void setTui_con_id(String tui_con_id) {
            this.tui_con_id = tui_con_id;
        }

        public String getTui_con_type() {
            return tui_con_type;
        }

        public void setTui_con_type(String tui_con_type) {
            this.tui_con_type = tui_con_type;
        }

        public String getGourl() {
            return gourl;
        }

        public void setGourl(String gourl) {
            this.gourl = gourl;
        }
    }
}
