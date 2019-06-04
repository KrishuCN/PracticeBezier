package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/18.
 */

public class VipRecordGson {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * check : 6
         * addtime : 2017-3-9 15:39:49
         * state : 2
         * etime : 2017-9-9 15:40:14
         * btime : 2017-3-9 15:40:14
         */

        private String check;
        private String addtime;
        private String state;
        private String etime;
        private String btime;

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getEtime() {
            return etime;
        }

        public void setEtime(String etime) {
            this.etime = etime;
        }

        public String getBtime() {
            return btime;
        }

        public void setBtime(String btime) {
            this.btime = btime;
        }
    }
}
