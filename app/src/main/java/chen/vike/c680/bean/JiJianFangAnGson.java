package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/16.
 */

public class JiJianFangAnGson {


    /**
     * countInfo : {"yeshege_count":"8","all_count":"9","nohege_count":"1","unpingbiao_count":"0"}
     * list : [{"id":"11011126","con":"项目结束后公开","status":"2","addtime":"2017/03/09 14:46:23","role":"","userid":"1671823","username":"cleverboy121","imageurl":"http://head.p.680.com/home/2012-9/20/2012092014412002725_1671823.jpg","viker_check":"2","viker_grade":"352","viker_gradename":"四星级"},{"id":"11014205","con":"项目结束后公开","status":"2","addtime":"2017/03/13 11:59:53","role":"","userid":"2938854","username":"五月天555","imageurl":"http://p6.680.com/home/2014-3/2/2014030220585932212_2938854.jpeg","viker_check":"2","viker_grade":"131","viker_gradename":"二星级"},{"id":"11010990","con":"项目结束后公开","status":"2","addtime":"2017/03/09 12:41:08","role":"","userid":"10420239","username":"xx7758yj","imageurl":"http://fj.p.680.com/home/2016-12/23/2016122320381835107_10420239.jpg","viker_check":"2","viker_grade":"60","viker_gradename":"一星级"},{"id":"11010989","con":"项目结束后公开","status":"2","addtime":"2017/03/09 12:41:08","role":"","userid":"10420239","username":"xx7758yj","imageurl":"http://fj.p.680.com/home/2016-12/23/2016122320381835107_10420239.jpg","viker_check":"2","viker_grade":"60","viker_gradename":"一星级"},{"id":"11014321","con":"项目结束后公开","status":"2","addtime":"2017/03/13 15:58:41","role":"","userid":"1328116","username":"308391475","imageurl":"http://img10.vikecn.com/Myfile/2009-12/6/162421446_1328116.jpg","viker_check":"2","viker_grade":"52","viker_gradename":"一星级"},{"id":"11014320","con":"项目结束后公开","status":"1","addtime":"2017/03/13 15:58:41","role":"","userid":"1328116","username":"308391475","imageurl":"http://img10.vikecn.com/Myfile/2009-12/6/162421446_1328116.jpg","viker_check":"2","viker_grade":"52","viker_gradename":"一星级"},{"id":"11011030","con":"项目结束后公开","status":"2","addtime":"2017/03/09 13:30:40","role":"","userid":"1545864","username":"杨唐","imageurl":"","viker_check":"2","viker_grade":"18","viker_gradename":"一星级"},{"id":"11011029","con":"项目结束后公开","status":"2","addtime":"2017/03/09 13:30:39","role":"","userid":"1545864","username":"杨唐","imageurl":"","viker_check":"2","viker_grade":"18","viker_gradename":"一星级"},{"id":"11011401","con":"项目结束后公开","status":"2","addtime":"2017/03/09 21:22:07","role":"","userid":"9651073","username":"hua251414","imageurl":"","viker_check":"2","viker_grade":"10","viker_gradename":"一星级"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":0,"beginPageIndex":0,"endPageIndex":0,"pageTotal":1,"currPageIndex":1,"recordCount":9}
     */

    private CountInfoBean countInfo;
    private PagerInfoBean pagerInfo;
    private List<ListBean> list;

    public CountInfoBean getCountInfo() {
        return countInfo;
    }

    public void setCountInfo(CountInfoBean countInfo) {
        this.countInfo = countInfo;
    }

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

    public static class CountInfoBean {
        /**
         * yeshege_count : 8
         * all_count : 9
         * nohege_count : 1
         * unpingbiao_count : 0
         */

        private String yeshege_count;
        private String all_count;
        private String nohege_count;
        private String unpingbiao_count;

        public String getYeshege_count() {
            return yeshege_count;
        }

        public void setYeshege_count(String yeshege_count) {
            this.yeshege_count = yeshege_count;
        }

        public String getAll_count() {
            return all_count;
        }

        public void setAll_count(String all_count) {
            this.all_count = all_count;
        }

        public String getNohege_count() {
            return nohege_count;
        }

        public void setNohege_count(String nohege_count) {
            this.nohege_count = nohege_count;
        }

        public String getUnpingbiao_count() {
            return unpingbiao_count;
        }

        public void setUnpingbiao_count(String unpingbiao_count) {
            this.unpingbiao_count = unpingbiao_count;
        }
    }

    public static class PagerInfoBean {
        /**
         * prePageIndex : 0
         * nextPageIndex : 0
         * beginPageIndex : 0.0
         * endPageIndex : 0.0
         * pageTotal : 1
         * currPageIndex : 1
         * recordCount : 9
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

    public static class ListBean {
        /**
         * id : 11011126
         * con : 项目结束后公开
         * status : 2
         * addtime : 2017/03/09 14:46:23
         * role :
         * userid : 1671823
         * username : cleverboy121
         * imageurl : http://head.p.680.com/home/2012-9/20/2012092014412002725_1671823.jpg
         * viker_check : 2
         * viker_grade : 352
         * viker_gradename : 四星级
         */

        private String id;
        private String con;
        private String status;
        private String addtime;
        private String role;
        private String userid;
        private String username;
        private String imageurl;
        private String viker_check;
        private String viker_grade;
        private String viker_gradename;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCon() {
            return con;
        }

        public void setCon(String con) {
            this.con = con;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public String getViker_check() {
            return viker_check;
        }

        public void setViker_check(String viker_check) {
            this.viker_check = viker_check;
        }

        public String getViker_grade() {
            return viker_grade;
        }

        public void setViker_grade(String viker_grade) {
            this.viker_grade = viker_grade;
        }

        public String getViker_gradename() {
            return viker_gradename;
        }

        public void setViker_gradename(String viker_gradename) {
            this.viker_gradename = viker_gradename;
        }
    }
}
