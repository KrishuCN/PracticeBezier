package chen.vike.c680.bean;

/**
 * Created by lht on 2017/3/18.
 */

public class VipPayGson {


    /**
     * userid : 583067
     * error :
     * orderno : 868050318140516_3_583067
     * paymoney : 6800
     */

    private String userid;
    private String error;
    private String orderno;
    private String paymoney;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(String paymoney) {
        this.paymoney = paymoney;
    }
}
