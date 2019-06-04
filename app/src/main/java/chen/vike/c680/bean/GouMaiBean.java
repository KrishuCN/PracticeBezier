package chen.vike.c680.bean;

/**
 * Created by lht on 2017/12/7.
 */

public class GouMaiBean {

    /**
     * err_code : 0
     * err_msg : 购买道具卡成功！
     */

    private String err_code;
    private String err_msg;
    private String user_yumoney;

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

    public String getUser_yumoney() {
        return user_yumoney;
    }

    public void setUser_yumoney(String user_yumoney) {
        this.user_yumoney = user_yumoney;
    }
}
