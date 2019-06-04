package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/28.
 */

public class MesPersonSeaGson {


    /**
     * list : [{"sendtime":"2017-3-27 20:20:00","msgid":"4874178","readok":"1","linkmanid":"9376232","username":"zyjeatyou中国","faceimg":"http://m3.p.680.com/UserPhotoFiles/2017-3/15/s/2017031509324827137_9376232.jpg","unreadnum":"0","content":"你就能否","touserid":"583067"},{"sendtime":"2017-3-25 14:14:00","msgid":"4873193","readok":"0","linkmanid":"3247484","username":"zyjeatyou3","faceimg":"http://head.shopimg.680.com/2017-3/7/s/2017030715551224436_3247484.jpg","unreadnum":"0","content":"fharfgi","touserid":"3247484"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":0,"beginPageIndex":0,"endPageIndex":0,"pageTotal":1,"currPageIndex":1,"recordCount":2}
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
         * nextPageIndex : 0
         * beginPageIndex : 0
         * endPageIndex : 0
         * pageTotal : 1
         * currPageIndex : 1
         * recordCount : 2
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
         * sendtime : 2017-3-27 20:20:00
         * msgid : 4874178
         * readok : 1
         * linkmanid : 9376232
         * username : zyjeatyou中国
         * faceimg : http://m3.p.680.com/UserPhotoFiles/2017-3/15/s/2017031509324827137_9376232.jpg
         * unreadnum : 0
         * content : 你就能否
         * touserid : 583067
         */

        private String sendtime;
        private String msgid;
        private String readok;
        private String linkmanid;
        private String username;
        private String faceimg;
        private String unreadnum;
        private String content;
        private String touserid;

        public String getSendtime() {
            return sendtime;
        }

        public void setSendtime(String sendtime) {
            this.sendtime = sendtime;
        }

        public String getMsgid() {
            return msgid;
        }

        public void setMsgid(String msgid) {
            this.msgid = msgid;
        }

        public String getReadok() {
            return readok;
        }

        public void setReadok(String readok) {
            this.readok = readok;
        }

        public String getLinkmanid() {
            return linkmanid;
        }

        public void setLinkmanid(String linkmanid) {
            this.linkmanid = linkmanid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFaceimg() {
            return faceimg;
        }

        public void setFaceimg(String faceimg) {
            this.faceimg = faceimg;
        }

        public String getUnreadnum() {
            return unreadnum;
        }

        public void setUnreadnum(String unreadnum) {
            this.unreadnum = unreadnum;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTouserid() {
            return touserid;
        }

        public void setTouserid(String touserid) {
            this.touserid = touserid;
        }
    }
}
