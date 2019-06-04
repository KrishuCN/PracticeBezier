package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/25.
 */

public class ChatMessageGson {


    /**
     * list : [{"msgid":"4873193","linkmanid":"583067","sendtime":"14:14","faceimg":"http://head.shopimg.680.com/2017-2/24/s/2017022411080614342_583067.jpg","content":"fharfgi","username":"gguu","readok":"0"},{"msgid":"4872987","linkmanid":"583067","sendtime":"2017-03-24","faceimg":"http://head.shopimg.680.com/2017-2/24/s/2017022411080614342_583067.jpg","content":"Ytyghhh","username":"gguu","readok":"0"},{"msgid":"4872978","linkmanid":"3247484","sendtime":"2017-03-24","faceimg":"http://head.shopimg.680.com/2017-3/7/s/2017030715551224436_3247484.jpg","content":"你好，可以开发app吗","username":"zyjeatyou3","readok":"1"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":0,"beginPageIndex":0,"endPageIndex":0,"pageTotal":1,"currPageIndex":1,"recordCount":3}
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
         * recordCount : 3
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
         * msgid : 4873193
         * linkmanid : 583067
         * sendtime : 14:14
         * faceimg : http://head.shopimg.680.com/2017-2/24/s/2017022411080614342_583067.jpg
         * content : fharfgi
         * username : gguu
         * readok : 0
         */

        private String msgid;
        private String linkmanid;
        private String sendtime;
        private String faceimg;
        private String content;
        private String username;
        private String readok;

        public String getMsgid() {
            return msgid;
        }

        public void setMsgid(String msgid) {
            this.msgid = msgid;
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

        public String getReadok() {
            return readok;
        }

        public void setReadok(String readok) {
            this.readok = readok;
        }
    }
}
