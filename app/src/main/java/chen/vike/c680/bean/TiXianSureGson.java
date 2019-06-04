package chen.vike.c680.bean;

/**
 * Created by lht on 2017/3/9.
 */

public class TiXianSureGson {


    /**
     * user_data : {"account_money":"1194.00","fullname":"聂天东","bankname":"*********111","email":"842764298@loginQQ.com"}
     */

    private UserDataBean user_data;

    public UserDataBean getUser_data() {
        return user_data;
    }

    public void setUser_data(UserDataBean user_data) {
        this.user_data = user_data;
    }

    public static class UserDataBean {
        /**
         * account_money : 1194.00
         * fullname : 聂天东
         * bankname : *********111
         * email : 842764298@loginQQ.com
         */

        private String account_money;
        private String fullname;
        private String bankname;
        private String email;

        public String getAccount_money() {
            return account_money;
        }

        public void setAccount_money(String account_money) {
            this.account_money = account_money;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
