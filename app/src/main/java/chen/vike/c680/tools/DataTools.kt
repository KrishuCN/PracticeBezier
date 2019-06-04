package chen.vike.c680.tools

/**
 * Created by chen on 2018/12/4.
 * 8 *
 * 8  储存页面数据的工具类
 */
object DataTools {

    fun saveData(statisticsBean: DataStatisticsBean, userid: String, pagename: String, pageindex: String, pageinittime: String) {
        statisticsBean.userid = userid
        statisticsBean.pagename = pagename
        statisticsBean.pageindex = pageindex
        statisticsBean.pageinittime = pageinittime
        // statisticsBean.setApptype("an");

    }
}
