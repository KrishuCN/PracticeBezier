package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/15.
 */

public class ServicePingjiaGson {


    /**
     * list : [{"pj_userid":"9670607","userid":"9670607","pj_num":"5","pj_con":"方案做速度快修改及时","pj_time":"2017/03/15 09:35:27","username_vk":"大飞元","pj_fw":"5","id":"34195","imageurl":"http://fj.p.680.com/home/2016-8/7/s/2016080710560604451_9670607.jpg","pj_itemid":"424616","pj_xl":"5","pj_zl":"5"},{"pj_userid":"10430488","userid":"10430488","pj_num":"5","pj_con":"设计水平高人也很好","pj_time":"2017/03/14 10:58:31","username_vk":"zhx1988","pj_fw":"5","id":"34187","imageurl":"http://fj.p.680.com/home/2017-1/17/s/2017011715293490916_10430488.jpg","pj_itemid":"423972","pj_xl":"5","pj_zl":"5"},{"pj_userid":"10417764","userid":"10417764","pj_num":"5","pj_con":"合作愉快谢谢","pj_time":"2017/03/13 09:25:29","username_vk":"leedber","pj_fw":"5","id":"34175","imageurl":"http://fj.p.680.com/home/2017-1/11/s/2017011118500163865_10417764.jpg","pj_itemid":"422993","pj_xl":"5","pj_zl":"5"},{"pj_userid":"10385412","userid":"10385412","pj_num":"5","pj_con":"很专业的团队","pj_time":"2017/03/12 22:07:52","username_vk":"老胖羊肉汤","pj_fw":"5","id":"34174","imageurl":"http://app.680.com/images/v3/face.png","pj_itemid":"423226","pj_xl":"5","pj_zl":"5"},{"pj_userid":"10430491","userid":"10430491","pj_num":"5","pj_con":"小伙挺有耐心沟通也不错很容易理解我的意思","pj_time":"2017/03/12 12:02:42","username_vk":"ataerbe","pj_fw":"5","id":"34170","imageurl":"http://app.680.com/images/v3/face.png","pj_itemid":"424308","pj_xl":"5","pj_zl":"5"},{"pj_userid":"10400171","userid":"10400171","pj_num":"5","pj_con":"执行能力很强","pj_time":"2017/03/12 09:26:01","username_vk":"13695226550","pj_fw":"5","id":"34169","imageurl":"http://app.680.com/images/v3/face.png","pj_itemid":"418092","pj_xl":"5","pj_zl":"5"},{"pj_userid":"9663104","userid":"9663104","pj_num":"5","pj_con":"8号设计师小姑娘做东西就是仔细很喜欢他","pj_time":"2017/03/11 13:18:33","username_vk":"chenxujun","pj_fw":"5","id":"34163","imageurl":"http://fj.p.680.com/home/2016-8/7/s/2016080711161758181_9663104.jpg","pj_itemid":"422126","pj_xl":"5","pj_zl":"5"},{"pj_userid":"9674420","userid":"9674420","pj_num":"5","pj_con":"谢谢龙博7号态度诚恳服务很周到作品给力","pj_time":"2017/03/11 08:38:15","username_vk":"金庆宇","pj_fw":"5","id":"34162","imageurl":"http://app.680.com/images/v3/face.png","pj_itemid":"423890","pj_xl":"5","pj_zl":"5"},{"pj_userid":"10430483","userid":"10430483","pj_num":"5","pj_con":"你们设计师很优秀沟通很方便几乎一句话就了解我的意思了","pj_time":"2017/03/10 14:18:16","username_vk":"优达证券","pj_fw":"5","id":"34154","imageurl":"http://app.680.com/images/v3/face.png","pj_itemid":"423745","pj_xl":"5","pj_zl":"5"},{"pj_userid":"10417762","userid":"10417762","pj_num":"5","pj_con":"这个标志设计的非常完美我很喜欢厉害厉害","pj_time":"2017/03/10 12:55:10","username_vk":"13695200012","pj_fw":"5","id":"34149","imageurl":"http://app.680.com/images/v3/face.png","pj_itemid":"423848","pj_xl":"5","pj_zl":"5"}]
     * pagerInfo : {"prePageIndex":0,"nextPageIndex":2,"beginPageIndex":1,"endPageIndex":10,"pageTotal":134,"currPageIndex":1,"recordCount":1338}
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
         * pageTotal : 134
         * currPageIndex : 1
         * recordCount : 1338
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
         * pj_userid : 9670607
         * userid : 9670607
         * pj_num : 5
         * pj_con : 方案做速度快修改及时
         * pj_time : 2017/03/15 09:35:27
         * username_vk : 大飞元
         * pj_fw : 5
         * id : 34195
         * imageurl : http://fj.p.680.com/home/2016-8/7/s/2016080710560604451_9670607.jpg
         * pj_itemid : 424616
         * pj_xl : 5
         * pj_zl : 5
         */

        private String pj_userid;
        private String userid;
        private String pj_num;
        private String pj_con;
        private String pj_time;
        private String username_vk;
        private String pj_fw;
        private String id;
        private String imageurl;
        private String pj_itemid;
        private String pj_xl;
        private String pj_zl;

        public String getPj_userid() {
            return pj_userid;
        }

        public void setPj_userid(String pj_userid) {
            this.pj_userid = pj_userid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPj_num() {
            return pj_num;
        }

        public void setPj_num(String pj_num) {
            this.pj_num = pj_num;
        }

        public String getPj_con() {
            return pj_con;
        }

        public void setPj_con(String pj_con) {
            this.pj_con = pj_con;
        }

        public String getPj_time() {
            return pj_time;
        }

        public void setPj_time(String pj_time) {
            this.pj_time = pj_time;
        }

        public String getUsername_vk() {
            return username_vk;
        }

        public void setUsername_vk(String username_vk) {
            this.username_vk = username_vk;
        }

        public String getPj_fw() {
            return pj_fw;
        }

        public void setPj_fw(String pj_fw) {
            this.pj_fw = pj_fw;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public String getPj_itemid() {
            return pj_itemid;
        }

        public void setPj_itemid(String pj_itemid) {
            this.pj_itemid = pj_itemid;
        }

        public String getPj_xl() {
            return pj_xl;
        }

        public void setPj_xl(String pj_xl) {
            this.pj_xl = pj_xl;
        }

        public String getPj_zl() {
            return pj_zl;
        }

        public void setPj_zl(String pj_zl) {
            this.pj_zl = pj_zl;
        }
    }
}
