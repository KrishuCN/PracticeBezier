package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/12/4.
 */

public class TuiSongServiceBean {
    private String err_code;
    private String err_msg;
    private List<ServicesBean> services;

    public List<ServicesBean> getServices() {
        return services;
    }

    public void setServices(List<ServicesBean> services) {
        this.services = services;
    }

    public static class ServicesBean {
        /**
         * pid : 52767
         * userid : 10416580
         * title : logo设计/企业logo/公司logo/品牌logo/网站logo/餐饮logo设计专业设计，满意为止！
         * price : 686元/个
         * fileurl : http://p1.shopimg.680.com/2017-3/7/32017030711354992725_10416580.jpg
         */

        private String pid;
        private String userid;
        private String title;
        private String price;
        private String fileurl;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getFileurl() {
            return fileurl;
        }

        public void setFileurl(String fileurl) {
            this.fileurl = fileurl;
        }
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
}
