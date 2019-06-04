package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/12/11.
 */

public class IpropBean {

    /**
     * total : 8
     * err_code : 0
     * err_msg :
     * itemList : [{"bianhao":"color-blue","sjb":"0","name":"染色卡( 蓝色24小时 )","danjia":"5","cont":"任何已成功发布的项目，使用该道具可以立即将您发布项目的标题字体设置为蓝色并保持24小时，使其您的项目在悬赏大厅中更加亮眼醒目。","sid":"19","mydjnum":"1","mydjid":"23471","img":"http://js.680.com/daoju/images/png-ranse.png"},{"bianhao":"jiaji","sjb":"0","name":"项目加急卡","danjia":"50","cont":"悬赏项目，使用该道具可将项目展示在首页项目加急处显示，同时也在悬赏大厅显示，持续24小时(使用时间不可叠加)。","sid":"25","mydjnum":"11","mydjid":"3896","img":"http://www.680.com/Images/png-jiaji.png"},{"bianhao":"jingbiao","sjb":"0","name":"竞标卡","danjia":"30","cont":"使用该卡可直接查看招标项目雇主的联系方式，自行联系雇主洽谈项目事宜。","sid":"24","mydjnum":"20","mydjid":"3885","img":"http://js.680.com/daoju/Images/yixiang.png"},{"bianhao":"gjpmk","sjb":"0","name":"方案速递卡","danjia":"2","cont":"设计类项目，当方案较多时，使用该卡后，将自己提交的某一个方案靠前显示，同时将以邮件方式通知雇主，让雇主第一时间看到该方案，增加中标的机率。","sid":"23","mydjnum":"20","mydjid":"3884","img":"http://js.680.com/daoju/Images/sdk.png"},{"bianhao":"biaoqing-wangwang","sjb":"5","name":"旺旺卡（24小时）","danjia":"0","cont":"任何已成功发布后的项目，使用该道具可以立即在您已发项目详细要求页面增加表情动画并保持24小时。","sid":"22","mydjnum":"3","mydjid":"86","img":"http://js.680.com/img3.0/ye.gif"},{"bianhao":"jtop","sjb":"0","name":"项目置顶卡（24小时）","danjia":"20","cont":"使用该卡可以将您已经成功发布的项目在页面置顶保持24小时（使用时间不可叠加)。","sid":"2","mydjnum":"20","mydjid":"7","img":"http://js.680.com/daoju/Images/png-zhiding.png"},{"bianhao":"item","sjb":"0","name":"项目排名提升卡","danjia":"10","cont":"使用该卡可以更新项目操作时间，让你的项目排名一次性提升到最前面。","sid":"1","mydjnum":"9","mydjid":"2","img":"http://js.680.com/daoju/Images/png-tisheng.png"},{"bianhao":"color-red","sjb":"0","name":"染色卡( 红色24小时 )","danjia":"5","cont":"任何已成功发布的项目，使用该道具可以立即将您发布项目的标题字体设置为红色并保持24小时，使其您的项目在悬赏大厅中更加亮眼醒目。","sid":"17","mydjnum":"1","mydjid":"1","img":"http://js.680.com/daoju/images/png-ranse.png"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":0,"beginPageIndex":0,"endPageIndex":0,"pageTotal":1,"currPageIndex":1,"recordCount":8}
     */

    private int total;
    private String err_code;
    private String err_msg;
    private PagerInfoBean pagerInfo;
    private List<ItemListBean> itemList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

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

    public List<ItemListBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListBean> itemList) {
        this.itemList = itemList;
    }

    public static class PagerInfoBean {
        /**
         * prePageIndex : 0
         * nextPageIndex : 0
         * beginPageIndex : 0.0
         * endPageIndex : 0.0
         * pageTotal : 1
         * currPageIndex : 1
         * recordCount : 8
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

    public static class ItemListBean {
        /**
         * bianhao : color-blue
         * sjb : 0
         * name : 染色卡( 蓝色24小时 )
         * danjia : 5
         * cont : 任何已成功发布的项目，使用该道具可以立即将您发布项目的标题字体设置为蓝色并保持24小时，使其您的项目在悬赏大厅中更加亮眼醒目。
         * sid : 19
         * mydjnum : 1
         * mydjid : 23471
         * img : http://js.680.com/daoju/images/png-ranse.png
         */

        private String bianhao;
        private String sjb;
        private String name;
        private String danjia;
        private String cont;
        private String sid;
        private String mydjnum;
        private String mydjid;
        private String img;

        public String getBianhao() {
            return bianhao;
        }

        public void setBianhao(String bianhao) {
            this.bianhao = bianhao;
        }

        public String getSjb() {
            return sjb;
        }

        public void setSjb(String sjb) {
            this.sjb = sjb;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDanjia() {
            return danjia;
        }

        public void setDanjia(String danjia) {
            this.danjia = danjia;
        }

        public String getCont() {
            return cont;
        }

        public void setCont(String cont) {
            this.cont = cont;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getMydjnum() {
            return mydjnum;
        }

        public void setMydjnum(String mydjnum) {
            this.mydjnum = mydjnum;
        }

        public String getMydjid() {
            return mydjid;
        }

        public void setMydjid(String mydjid) {
            this.mydjid = mydjid;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
