package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/12/4.
 */

public class TuiSongBean {

    private String err_code;
    private String err_msg;
    private List<ItemsBean> items;

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * itemid : 444064
         * itemname : 餐饮LOGO设计
         * money : 58
         * overtime : null
         * summary : LOGO名称：湘旺跳跳蛙
         我是做餐饮的，以牛蛙为主。设计一个卡通的牛蛙搭配我的文字。要形象卡通，大气美观。
         */

        private int itemid;
        private String itemname;
        private String money;
        private Object overtime;
        private String summary;

        public int getItemid() {
            return itemid;
        }

        public void setItemid(int itemid) {
            this.itemid = itemid;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public Object getOvertime() {
            return overtime;
        }

        public void setOvertime(Object overtime) {
            this.overtime = overtime;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
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
