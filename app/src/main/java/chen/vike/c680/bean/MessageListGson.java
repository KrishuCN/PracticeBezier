package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/24.
 */

public class MessageListGson {


    /**
     * list : [{"msgid":"4872203","readok":"0","linkmanid":"1","sendtime":"2017-03-22","faceimg":"http://app.680.com/images/app/logo_u.png","unreadnum":"6","content":"123131231231","username":"系统消息","touserid":"583067"},{"msgid":"4872944","readok":"0","linkmanid":"9376232","sendtime":"16:15","faceimg":"http://m3.p.680.com/UserPhotoFiles/2017-3/15/s/2017031509324827137_9376232.jpg","unreadnum":"0","content":"Hgfdfg","username":"zyjeatyou中国","touserid":"9376232"},{"msgid":"4872918","readok":"0","linkmanid":"2553076","sendtime":"15:48","faceimg":"http://app.680.com/images/v3/face.png","unreadnum":"0","content":"Bbnbnn","username":"时间财富网项目推荐","touserid":"2553076"},{"msgid":"4872862","readok":"0","linkmanid":"3273728","sendtime":"14:38","faceimg":"http://app.680.com/images/v3/face.png","unreadnum":"0","content":"Ooooooooo","username":"gguu4567","touserid":"3273728"},{"msgid":"4870215","readok":"1","linkmanid":"10451258","sendtime":"2017-03-17","faceimg":"http://app.680.com/images/v3/face.png","unreadnum":"0","content":"在吗","username":"shoujiu4413632","touserid":"583067"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":0,"beginPageIndex":0,"endPageIndex":0,"pageTotal":1,"currPageIndex":1,"recordCount":5}
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
         * recordCount : 5
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
         * msgid : 4872203
         * readok : 0
         * linkmanid : 1
         * sendtime : 2017-03-22
         * faceimg : http://app.680.com/images/app/logo_u.png
         * unreadnum : 6
         * content : 123131231231
         * username : 系统消息
         * touserid : 583067
         */

        private String msgid;
        private String readok;
        private String linkmanid;
        private String sendtime;
        private String faceimg;
        private String unreadnum;
        private String content;
        private String username;
        private String touserid;

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

        public String getSendtime() {
            return sendtime;
        }

        public void setSendtime(String sendtime) {
            this.sendtime = sendtime;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTouserid() {
            return touserid;
        }

        public void setTouserid(String touserid) {
            this.touserid = touserid;
        }
    }
}
