package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/16.
 */

public class ZhaoBiaoFangAnGson {


    /**
     * list : [{"cyid":7797965,"viker_userid":2553067,"viker_name":"gguu997","viker_grade_pic":"<img src='/images/grade/star0.gif' alt='暂无等级&#13积分：10' title='暂无等级&#13积分：10' class='tip' align='absmiddle'>","viker_check":2,"viewlink":"10000","tj_time":"2017-01-16","fujian":"黑龙江 - 松花江地区","fujian1":null,"fujian2":null,"fujian3":null,"fujian4":null,"fujian5":null,"zuopindes":"11111111111","ispic":0,"picurl":null,"role":"zb","zb":11,"isread":0,"addtime":null,"imageurl":"http://shop.p.680.com/ShenFen/2016-11/22/201611221117190775_2553067.png","fujian_list":null,"viker_gradename":"一星级","viker_grade":10}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":0,"beginPageIndex":0,"endPageIndex":0,"pageTotal":1,"currPageIndex":1,"recordCount":1}
     * gzData : {"gz_item_userid":"9376232","gz_is_login_owner":"0"}
     */

    private PagerInfoBean pagerInfo;
    private GzDataBean gzData;
    private List<ListBean> list;

    public PagerInfoBean getPagerInfo() {
        return pagerInfo;
    }

    public void setPagerInfo(PagerInfoBean pagerInfo) {
        this.pagerInfo = pagerInfo;
    }

    public GzDataBean getGzData() {
        return gzData;
    }

    public void setGzData(GzDataBean gzData) {
        this.gzData = gzData;
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
         * beginPageIndex : 0.0
         * endPageIndex : 0.0
         * pageTotal : 1
         * currPageIndex : 1
         * recordCount : 1
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

    public static class GzDataBean {
        /**
         * gz_item_userid : 9376232
         * gz_is_login_owner : 0
         */

        private String gz_item_userid;
        private String gz_is_login_owner;

        public String getGz_item_userid() {
            return gz_item_userid;
        }

        public void setGz_item_userid(String gz_item_userid) {
            this.gz_item_userid = gz_item_userid;
        }

        public String getGz_is_login_owner() {
            return gz_is_login_owner;
        }

        public void setGz_is_login_owner(String gz_is_login_owner) {
            this.gz_is_login_owner = gz_is_login_owner;
        }
    }

    public static class ListBean {
        /**
         * cyid : 7797965
         * viker_userid : 2553067
         * viker_name : gguu997
         * viker_grade_pic : <img src='/images/grade/star0.gif' alt='暂无等级&#13积分：10' title='暂无等级&#13积分：10' class='tip' align='absmiddle'>
         * viker_check : 2
         * viewlink : 10000
         * tj_time : 2017-01-16
         * fujian : 黑龙江 - 松花江地区
         * fujian1 : null
         * fujian2 : null
         * fujian3 : null
         * fujian4 : null
         * fujian5 : null
         * zuopindes : 11111111111
         * ispic : 0
         * picurl : null
         * role : zb
         * zb : 11
         * isread : 0
         * addtime : null
         * imageurl : http://shop.p.680.com/ShenFen/2016-11/22/201611221117190775_2553067.png
         * fujian_list : null
         * viker_gradename : 一星级
         * viker_grade : 10
         */

        private int cyid;
        private int viker_userid;
        private String viker_name;
        private String viker_grade_pic;
        private int viker_check;
        private String viewlink;
        private String tj_time;
        private String fujian;
        private Object fujian1;
        private Object fujian2;
        private Object fujian3;
        private Object fujian4;
        private Object fujian5;
        private String zuopindes;
        private int ispic;
        private Object picurl;
        private String role;
        private int zb;
        private int isread;
        private Object addtime;
        private String imageurl;
        private Object fujian_list;
        private String viker_gradename;
        private int viker_grade;

        public int getCyid() {
            return cyid;
        }

        public void setCyid(int cyid) {
            this.cyid = cyid;
        }

        public int getViker_userid() {
            return viker_userid;
        }

        public void setViker_userid(int viker_userid) {
            this.viker_userid = viker_userid;
        }

        public String getViker_name() {
            return viker_name;
        }

        public void setViker_name(String viker_name) {
            this.viker_name = viker_name;
        }

        public String getViker_grade_pic() {
            return viker_grade_pic;
        }

        public void setViker_grade_pic(String viker_grade_pic) {
            this.viker_grade_pic = viker_grade_pic;
        }

        public int getViker_check() {
            return viker_check;
        }

        public void setViker_check(int viker_check) {
            this.viker_check = viker_check;
        }

        public String getViewlink() {
            return viewlink;
        }

        public void setViewlink(String viewlink) {
            this.viewlink = viewlink;
        }

        public String getTj_time() {
            return tj_time;
        }

        public void setTj_time(String tj_time) {
            this.tj_time = tj_time;
        }

        public String getFujian() {
            return fujian;
        }

        public void setFujian(String fujian) {
            this.fujian = fujian;
        }

        public Object getFujian1() {
            return fujian1;
        }

        public void setFujian1(Object fujian1) {
            this.fujian1 = fujian1;
        }

        public Object getFujian2() {
            return fujian2;
        }

        public void setFujian2(Object fujian2) {
            this.fujian2 = fujian2;
        }

        public Object getFujian3() {
            return fujian3;
        }

        public void setFujian3(Object fujian3) {
            this.fujian3 = fujian3;
        }

        public Object getFujian4() {
            return fujian4;
        }

        public void setFujian4(Object fujian4) {
            this.fujian4 = fujian4;
        }

        public Object getFujian5() {
            return fujian5;
        }

        public void setFujian5(Object fujian5) {
            this.fujian5 = fujian5;
        }

        public String getZuopindes() {
            return zuopindes;
        }

        public void setZuopindes(String zuopindes) {
            this.zuopindes = zuopindes;
        }

        public int getIspic() {
            return ispic;
        }

        public void setIspic(int ispic) {
            this.ispic = ispic;
        }

        public Object getPicurl() {
            return picurl;
        }

        public void setPicurl(Object picurl) {
            this.picurl = picurl;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public int getZb() {
            return zb;
        }

        public void setZb(int zb) {
            this.zb = zb;
        }

        public int getIsread() {
            return isread;
        }

        public void setIsread(int isread) {
            this.isread = isread;
        }

        public Object getAddtime() {
            return addtime;
        }

        public void setAddtime(Object addtime) {
            this.addtime = addtime;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public Object getFujian_list() {
            return fujian_list;
        }

        public void setFujian_list(Object fujian_list) {
            this.fujian_list = fujian_list;
        }

        public String getViker_gradename() {
            return viker_gradename;
        }

        public void setViker_gradename(String viker_gradename) {
            this.viker_gradename = viker_gradename;
        }

        public int getViker_grade() {
            return viker_grade;
        }

        public void setViker_grade(int viker_grade) {
            this.viker_grade = viker_grade;
        }
    }
}
