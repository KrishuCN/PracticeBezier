package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/12/12.
 */

public class CaiFuBean {
    private String err_code;
    private String err_msg;

    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * money : 5.00
         * addtime : 2017-12-7 15:17:00
         * state : 0
         * beizhu : 购买1个道具染色卡( 蓝色24小时 ),余额支付
         * moneyid : 7745192
         * sou_zhi : 2
         */

        private String money;
        private String addtime;
        private String state;
        private String beizhu;
        private String moneyid;
        private String sou_zhi;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
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

        public String getBeizhu() {
            return beizhu;
        }

        public void setBeizhu(String beizhu) {
            this.beizhu = beizhu;
        }

        public String getMoneyid() {
            return moneyid;
        }

        public void setMoneyid(String moneyid) {
            this.moneyid = moneyid;
        }

        public String getSou_zhi() {
            return sou_zhi;
        }

        public void setSou_zhi(String sou_zhi) {
            this.sou_zhi = sou_zhi;
        }
    }
}
