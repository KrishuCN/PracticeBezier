package chen.vike.c680.tools;

import org.litepal.crud.LitePalSupport;

/**
 * Created by chen on 2018/11/21.
 *
 * 统计表数据
 * 使用LitePal轻量级数据库存储
 */
public class DataStatisticsBean extends LitePalSupport {
    private String userid = "";  //用户id
    private String pagename = "";  //页面名字
    private String pageindex = "";  //
    private String pagenext = "";    //下一页面名字
    private String pageinittime = "";   //进入页面时间
    private String pagedowntime = "";   //退出页面时间
    //private String apptype = "";  //类型
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPagename() {
        return pagename;
    }

    public void setPagename(String pagename) {
        this.pagename = pagename;
    }

    public String getPageindex() {
        return pageindex;
    }

    public void setPageindex(String pageindex) {
        this.pageindex = pageindex;
    }

    public String getPagenext() {
        return pagenext;
    }

    public void setPagenext(String pagenext) {
        this.pagenext = pagenext;
    }

    public String getPageinittime() {
        return pageinittime;
    }

    public void setPageinittime(String pageinittime) {
        this.pageinittime = pageinittime;
    }

    public String getPagedowntime() {
        return pagedowntime;
    }

    public void setPagedowntime(String pagedowntime) {
        this.pagedowntime = pagedowntime;
    }

//    public String getApptype() {
//        return apptype;
//    }
//
//    public void setApptype(String apptype) {
//        this.apptype = apptype;
//    }
}
