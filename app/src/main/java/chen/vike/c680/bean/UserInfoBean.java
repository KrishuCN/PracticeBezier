package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/3.
 */

public class UserInfoBean {


    /**
     * UserID : 583067
     * Icon : http://head.shopimg.680.com/2017-2/24/s/2017022411080614342_583067.jpg
     * Nickame : gguu
     * RealName : 聂天东
     * Balance : 10000.00
     * Sex : 男
     * Birthday : 2009-4-8 0:00:00
     * HomeName :
     * SelfIntroduction : 很懒吧
     * Sign : 无
     * IsSecrecy : 0
     * Email : 842764298@loginQQ.com
     * CellPhone : 18780234112
     * Phone : 4234234
     * QQ : 234234
     * Area : 7
     * Address : 吉祥如意幸福路22号
     * PostalCode : 402363
     * IsNew : null
     * PassWord : null
     * favItems : ["26164","162651","376479","376478","376480","379200","402017","402217","402274","401717","402172","401763","401188","402098","401340","402083","402078","404016","403650","417889"]
     * isGuzhu : 0
     * isShop : 1
     * is_verify_fullname : 1
     * is_verify_email : 1
     * is_verify_phone : 0
     * is_bind_bankinfo : 1
     * bankname : 其它
     * bankcardno : 11111111
     * viptype : 2
     * new_info_name : 诚信保
     * new_info_url : 时间财富网诚信保正式上线了！
     * tuiguang_money : 0.00
     * huanxin_api_password:09086F48F22CE8555975AD99CE1DBAA0
     * CookieLoginName : gguu
     * CookieLoginIpt : 45.32.18.37-45-51-app
     * CookieLoginTime : 2017-03-03 10:45:51
     * CookieLoginToken : 5d255105be205e14
     * CookieLoginLogs : null
     */

    private String UserID = "";
    private String Icon = "";
    private String Nickame = "";
    private String RealName = "";
    private String Balance = "";
    private String Sex = "";
    private String Birthday = "";
    private String HomeName = "";
    private String SelfIntroduction = "";
    private String Sign = "";
    private int IsSecrecy;
    private String Email = "";
    private String CellPhone ="";
    private String Phone = "";
    private String QQ = "";
    private String Area = "";
    private String Address = "";
    private String PostalCode = "";
    private Object IsNew;
    private Object PassWord;
    private String isGuzhu = "";
    private String isShop = "";
    private String is_verify_fullname = "";
    private String is_verify_email = "";
    private String is_verify_phone = "";
    private String is_bind_bankinfo = "";
    private String bankname = "";
    private String bankcardno = "";
    private String viptype = "";
    private String new_info_name = "";
    private String new_info_url = "";
    private String tuiguang_money = "";
    private String huanxin_api_password = "";
    private String CookieLoginName = "";
    private String CookieLoginIpt = "";
    private String CookieLoginTime = "";
    private String CookieLoginToken = "";
    private Object CookieLoginLogs;
    private List<String> favItems;
    private String isgguu;
    private String isvip;

    public String getIsvip() {
        return isvip;
    }

    public void setIsvip(String isvip) {
        this.isvip = isvip;
    }

    public String getIsgguu() {
        return isgguu;
    }

    public void setIsgguu(String isgguu) {
        this.isgguu = isgguu;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public String getNickame() {
        return Nickame;
    }

    public void setNickame(String Nickame) {
        this.Nickame = Nickame;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String RealName) {
        this.RealName = RealName;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String Balance) {
        this.Balance = Balance;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public String getHomeName() {
        return HomeName;
    }

    public void setHomeName(String HomeName) {
        this.HomeName = HomeName;
    }

    public String getSelfIntroduction() {
        return SelfIntroduction;
    }

    public void setSelfIntroduction(String SelfIntroduction) {
        this.SelfIntroduction = SelfIntroduction;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String Sign) {
        this.Sign = Sign;
    }

    public int getIsSecrecy() {
        return IsSecrecy;
    }

    public void setIsSecrecy(int IsSecrecy) {
        this.IsSecrecy = IsSecrecy;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getCellPhone() {
        return CellPhone;
    }

    public void setCellPhone(String CellPhone) {
        this.CellPhone = CellPhone;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String Area) {
        this.Area = Area;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String PostalCode) {
        this.PostalCode = PostalCode;
    }

    public Object getIsNew() {
        return IsNew;
    }

    public void setIsNew(Object IsNew) {
        this.IsNew = IsNew;
    }

    public Object getPassWord() {
        return PassWord;
    }

    public void setPassWord(Object PassWord) {
        this.PassWord = PassWord;
    }

    public String getIsGuzhu() {
        return isGuzhu;
    }

    public void setIsGuzhu(String isGuzhu) {
        this.isGuzhu = isGuzhu;
    }

    public String getIsShop() {
        return isShop;
    }

    public void setIsShop(String isShop) {
        this.isShop = isShop;
    }

    public String getIs_verify_fullname() {
        return is_verify_fullname;
    }

    public void setIs_verify_fullname(String is_verify_fullname) {
        this.is_verify_fullname = is_verify_fullname;
    }

    public String getIs_verify_email() {
        return is_verify_email;
    }

    public void setIs_verify_email(String is_verify_email) {
        this.is_verify_email = is_verify_email;
    }

    public String getIs_verify_phone() {
        return is_verify_phone;
    }

    public void setIs_verify_phone(String is_verify_phone) {
        this.is_verify_phone = is_verify_phone;
    }

    public String getIs_bind_bankinfo() {
        return is_bind_bankinfo;
    }

    public void setIs_bind_bankinfo(String is_bind_bankinfo) {
        this.is_bind_bankinfo = is_bind_bankinfo;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankcardno() {
        return bankcardno;
    }

    public void setBankcardno(String bankcardno) {
        this.bankcardno = bankcardno;
    }

    public String getViptype() {
        return viptype;
    }

    public void setViptype(String viptype) {
        this.viptype = viptype;
    }

    public String getNew_info_name() {
        return new_info_name;
    }

    public void setNew_info_name(String new_info_name) {
        this.new_info_name = new_info_name;
    }

    public String getNew_info_url() {
        return new_info_url;
    }

    public void setNew_info_url(String new_info_url) {
        this.new_info_url = new_info_url;
    }

    public String getTuiguang_money() {
        return tuiguang_money;
    }

    public void setTuiguang_money(String tuiguang_money) {
        this.tuiguang_money = tuiguang_money;
    }

    public String getHuanxin_api_password() {
        return huanxin_api_password;
    }

    public void setHuanxin_api_password(String huanxin_api_password) {
        this.huanxin_api_password = huanxin_api_password;
    }

    public String getCookieLoginName() {
        return CookieLoginName;
    }

    public void setCookieLoginName(String CookieLoginName) {
        this.CookieLoginName = CookieLoginName;
    }

    public String getCookieLoginIpt() {
        return CookieLoginIpt;
    }

    public void setCookieLoginIpt(String CookieLoginIpt) {
        this.CookieLoginIpt = CookieLoginIpt;
    }

    public String getCookieLoginTime() {
        return CookieLoginTime;
    }

    public void setCookieLoginTime(String CookieLoginTime) {
        this.CookieLoginTime = CookieLoginTime;
    }

    public String getCookieLoginToken() {
        return CookieLoginToken;
    }

    public void setCookieLoginToken(String CookieLoginToken) {
        this.CookieLoginToken = CookieLoginToken;
    }

    public Object getCookieLoginLogs() {
        return CookieLoginLogs;
    }

    public void setCookieLoginLogs(Object CookieLoginLogs) {
        this.CookieLoginLogs = CookieLoginLogs;
    }

    public List<String> getFavItems() {
        return favItems;
    }

    public void setFavItems(List<String> favItems) {
        this.favItems = favItems;
    }
}
