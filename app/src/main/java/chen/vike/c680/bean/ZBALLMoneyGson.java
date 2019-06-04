package chen.vike.c680.bean;

/**
 * Created by lht on 2017/6/12.
 */

public class ZBALLMoneyGson {


    /**
     * data : {"total_money":"5000","need_pay_money":"4900","payok":"1","dingjin_money":"100"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total_money : 5000
         * need_pay_money : 4900
         * payok : 1
         * dingjin_money : 100
         */

        private String total_money;
        private String need_pay_money;
        private String payok;
        private String dingjin_money;

        public String getTotal_money() {
            return total_money;
        }

        public void setTotal_money(String total_money) {
            this.total_money = total_money;
        }

        public String getNeed_pay_money() {
            return need_pay_money;
        }

        public void setNeed_pay_money(String need_pay_money) {
            this.need_pay_money = need_pay_money;
        }

        public String getPayok() {
            return payok;
        }

        public void setPayok(String payok) {
            this.payok = payok;
        }

        public String getDingjin_money() {
            return dingjin_money;
        }

        public void setDingjin_money(String dingjin_money) {
            this.dingjin_money = dingjin_money;
        }
    }
}
