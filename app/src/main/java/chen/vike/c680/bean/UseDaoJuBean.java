package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/12/22.
 */

public class UseDaoJuBean {

    /**
     * list : [{"rbeizhu":"您在道具中心购买了1个屏蔽收录卡，花费了金钱50元，占用5空间。","rtime":"2017/12/12 09:29:46","rowId":"1","id":"123179"},{"rbeizhu":"您在道具中心购买了1个染色卡( 蓝色24小时 )，花费了金钱5元，占用5空间。","rtime":"2017/12/07 15:16:56","rowId":"2","id":"123061"},{"rbeizhu":"您在道具中心购买了1个项目置顶卡（24小时），花费了金钱20元，占用5空间。","rtime":"2017/12/07 15:09:16","rowId":"3","id":"123060"},{"rbeizhu":"您在道具中心购买了2个旺旺卡（24小时），花费了时间币10枚，占用10空间。","rtime":"2016/08/03 19:51:42","rowId":"4","id":"96988"},{"rbeizhu":"您在道具中心购买了1个项目加急卡，花费了金钱50元，占用5空间。","rtime":"2016/08/03 12:41:17","rowId":"5","id":"96950"},{"rbeizhu":"您在道具中心购买了1个方案速递卡，花费了金钱2元，占用5空间。","rtime":"2016/08/03 12:34:41","rowId":"6","id":"96948"},{"rbeizhu":"您在道具中心购买了1个项目置顶卡（24小时），花费了金钱20元，占用5空间。","rtime":"2016/08/03 12:16:02","rowId":"7","id":"96947"},{"rbeizhu":"您在道具中心购买了1个项目加急卡，花费了金钱50元，占用5空间。","rtime":"2016/08/03 12:14:59","rowId":"8","id":"96946"},{"rbeizhu":"您在道具中心购买了1个旺旺卡（24小时），花费了时间币5枚，占用5空间。","rtime":"2016/08/03 11:09:45","rowId":"9","id":"96938"},{"rbeizhu":"您在道具中心购买了1个项目置顶卡（24小时），花费了金钱20元，占用5空间。","rtime":"2016/08/02 17:54:14","rowId":"10","id":"96900"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":10,"pageTotal":17,"currPageIndex":1,"recordCount":163}
     */

    private PagerInfoBean pagerInfo;
    private List<ListBean> list;
    private String err_code;
    private String err_msg;

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
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

    public static class PagerInfoBean {
        /**
         * prePageIndex : 0
         * nextPageIndex : 2
         * beginPageIndex : 1.0
         * endPageIndex : 10.0
         * pageTotal : 17
         * currPageIndex : 1
         * recordCount : 163
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
         * rbeizhu : 您在道具中心购买了1个屏蔽收录卡，花费了金钱50元，占用5空间。
         * rtime : 2017/12/12 09:29:46
         * rowId : 1
         * id : 123179
         */

        private String rbeizhu;
        private String rtime;
        private String rowId;
        private String id;

        public String getRbeizhu() {
            return rbeizhu;
        }

        public void setRbeizhu(String rbeizhu) {
            this.rbeizhu = rbeizhu;
        }

        public String getRtime() {
            return rtime;
        }

        public void setRtime(String rtime) {
            this.rtime = rtime;
        }

        public String getRowId() {
            return rowId;
        }

        public void setRowId(String rowId) {
            this.rowId = rowId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
