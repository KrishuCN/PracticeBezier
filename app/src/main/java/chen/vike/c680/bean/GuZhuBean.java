package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2018/5/31.
 */

public class GuZhuBean {


    /**
     * err_code : 0
     * err_msg :
     * tophuandeng : [{"imgurl":"http://app.680.com/images/app/gz_index_9331803.png","infotype":"shop","itemtype":"","infoname":"龙博品牌设计","infoid":"9331803"},{"imgurl":"http://app.680.com/images/app/3005669_750_330.jpg","infotype":"shop","itemtype":"","infoname":"苗子设计工作室","infoid":"3005669"},{"imgurl":"http://wk.p.680.com/2016-xbsy-vk/vk_10416580_750_330.jpg","infotype":"shop","itemtype":"","infoname":"米旭品牌设计","infoid":"10416580"},{"imgurl":"http://shop.p.680.com/home/2017-12/5/201712051541315212_1312478.jpg","infotype":"shop","itemtype":"","infoname":"风彩创意设计","infoid":"1312478"}]
     * fabulink : [{"name":"LOGO 设计500元起","price":"500","id":"1"},{"name":"VI 设计300元起","price":"300","id":"2"},{"name":"海报设计500元起","price":"500","id":"3"}]
     * zscqlink : [{"name":"LOGO 设计500元起","price":"500","id":"1"},{"name":"VI 设计300元起","price":"300","id":"2"},{"name":"海报设计500元起","price":"500","id":"3"}]
     * kefulist : [{"imgurl":"http://app.680.com/images/2018/kefu_sheji.png","name":"设计服务","tel":"4006306800"},{"imgurl":"http://app.680.com/images/2018/kefu_kaifa.png","name":"开发服务","tel":"4006306800"},{"imgurl":"http://app.680.com/images/2018/kefu_shangbiao.png","name":"商标服务","tel":"4006306800"}]
     * guanggaoinfo : {"imgurl":"http://app.680.com/images/2018/idx_logo_huodong.jpg","infotype":"shop","itemtype":"","infoname":"龙博品牌设计","infoid":"9331803"}
     * cainixihuan : [{"imgurl":"http://p1.shopimg.680.com/2017-3/7/32017030711354992725_10416580.jpg","deal_count":"473","dianpu_name":"米旭品牌设计","fuwu_name":"logo设计/企业logo/公司logo/品牌logo/网站logo/餐饮logo设计专业设计，满意为止！","cityname":"上海","pingfen":"5.00","price":"￥686","unit":"个","fuwu_id":"52767"},{"imgurl":"http://shop.p.680.com/2017-10/26/32017102614452851467_10565632.jpg","deal_count":"0","dianpu_name":"元道设计","fuwu_name":"LOGO设计企业餐饮品牌地产金融汽车科技地产教育标志图形商标","cityname":"成都","pingfen":"5.00","price":"￥1999","unit":"个起","fuwu_id":"59457"},{"imgurl":"http://shop.p.680.com/2018-1/5/32018010519222716143_10587811.jpg","deal_count":"0","dianpu_name":"天下摄影","fuwu_name":"影像服务/社会活动/会议影像记录/后期制作 ","cityname":"北京","pingfen":"5.00","price":"￥5000","unit":"起","fuwu_id":"73032"},{"imgurl":"http://p1.shopimg.680.com/2017-3/7/32017030713294334210_10416580.jpg","deal_count":"247","dianpu_name":"米旭品牌设计","fuwu_name":"VI设计/原创VI设计/企业公司VI设计/品牌VI设计，总监设计满意为止！","cityname":"上海","pingfen":"5.00","price":"￥2996","unit":"套","fuwu_id":"52773"},{"imgurl":"http://shop.p.680.com/2017-11/16/32017111615402622828_10575912.png","deal_count":"0","dianpu_name":"三智慧通北京科技有限公司","fuwu_name":"APP开发/定制开发/原生开发/混合开发/详谈","cityname":"北京","pingfen":"5.00","price":"￥20000","unit":"起","fuwu_id":"63096"},{"imgurl":"http://p1.shopimg.680.com/2017-3/7/32017030718241655795_10416580.jpg","deal_count":"320","dianpu_name":"米旭品牌设计","fuwu_name":"宣传册设计/企业画册设计/产品样册设计/招商手册设计，总监设计满意为止！","cityname":"上海","pingfen":"5.00","price":"￥98","unit":"页","fuwu_id":"52774"},{"imgurl":"http://p1.shopimg.680.com/2017-8/3/32017080311462731179_10549427.png","deal_count":"0","dianpu_name":"泰德仁和科技","fuwu_name":"高端企业网站定制开发建站软件","cityname":"成都","pingfen":"5.00","price":"￥100000","unit":"起","fuwu_id":"56241"},{"imgurl":"http://shop.p.680.com/2017-10/26/32017102614295475943_10565632.jpg","deal_count":"0","dianpu_name":"元道设计","fuwu_name":"元道品牌设计瓶贴设计手提袋设计不干胶设计标签设计","cityname":"成都","pingfen":"5.00","price":"￥1500","unit":"个起","fuwu_id":"59433"}]
     * alipay_config : {"alipay_flag":"1","public_key":"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB","private_key":"MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAO237yuEWhkhaSYLThF3SeXN19HB5GqEJzUZVJyyh8ucogwpkseCULOu+AbQ/ZLhUgJx9VmMOCRdHFF5RDzE6Sqvvxsa4x6ze2tFhEhgn/pN6co1obmHH8HxxQcU5jALzWX3KDuQAgTAVeCsButhe3f65DfWp9YAVC5UOevQX9/pAgMBAAECgYAPUkuYAm/MLtsYGKJvfcDFjv7aBiUyuFHOmlNZxif6d536KrPLKBsLebuCtPWjAeLHRZEKtH7p2RZ6Kda8E/TzK457URtuVeBpfETxFhaWaIM/T3UoSdZPWFzJBL0+TbRQBwtnBAE/GW4WrEwAz0gsRVJA7nWQoYUbU+8fqnEoAQJBAP/Wdgpoft5leMac3R0fXUNNDlUZftCeCjwWpChQvrp81ylWTCzDt39t1iyvge5Zhu2rC6RwNxgwxFtOilgXMlUCQQDt3of/lc9ErUI2RaPOrHZO4CN4gPjVnzjChZNRDYArVoNy8g179Gqqxhhp2I6Vf01TIYVddbyJuuCyd8TE1xNFAkEAxlEGe5b5EYhzEPKz7ElN0EfBHnJ+/VQk9uNOKPo+fu0bFK4Sqnikm6EW7ti1zX+UulNm2PcrnhLfgy/kydhhaQJBAJ+LM6KtI7FHmHJ4cS9tI4kx8vifYMc/nT1zhGTH7mCjDVWqnnq2bFkQt00/MBt7oCyS/jW6g3PJwW5SwMraCxUCQQDJXQANPqkqAOlaU57qkiU4nbUwvm5mIO23fHLLo7SSiCecFvEXN/1rgMr3osZs4sEN6D6S/7eW1XbqbMU4urnQ","seller":"680@680.com","partner":"2088101950325505"}
     * weixinpay_config : {"appid":"wxfc34fb9efbdedeae","mchid":"1289720501","appsecret":"51c56b886b5be869567dd389b3e5d1d6","key":"d4624c36b6795d1d99dcf0547af5443d","weixinpay_flag":"1"}
     */

    private String err_code;
    private String err_msg;
    private GuanggaoinfoBean guanggaoinfo;
    private AlipayConfigBean alipay_config;
    private WeixinpayConfigBean weixinpay_config;
    private List<TophuandengBean> tophuandeng;
    private List<FabulinkBean> fabulink;
    private List<ZscqlinkBean> zscqlink;
    private List<KefulistBean> kefulist;
    private List<CainixihuanBean> cainixihuan;

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

    public GuanggaoinfoBean getGuanggaoinfo() {
        return guanggaoinfo;
    }

    public void setGuanggaoinfo(GuanggaoinfoBean guanggaoinfo) {
        this.guanggaoinfo = guanggaoinfo;
    }

    public AlipayConfigBean getAlipay_config() {
        return alipay_config;
    }

    public void setAlipay_config(AlipayConfigBean alipay_config) {
        this.alipay_config = alipay_config;
    }

    public WeixinpayConfigBean getWeixinpay_config() {
        return weixinpay_config;
    }

    public void setWeixinpay_config(WeixinpayConfigBean weixinpay_config) {
        this.weixinpay_config = weixinpay_config;
    }

    public List<TophuandengBean> getTophuandeng() {
        return tophuandeng;
    }

    public void setTophuandeng(List<TophuandengBean> tophuandeng) {
        this.tophuandeng = tophuandeng;
    }

    public List<FabulinkBean> getFabulink() {
        return fabulink;
    }

    public void setFabulink(List<FabulinkBean> fabulink) {
        this.fabulink = fabulink;
    }

    public List<ZscqlinkBean> getZscqlink() {
        return zscqlink;
    }

    public void setZscqlink(List<ZscqlinkBean> zscqlink) {
        this.zscqlink = zscqlink;
    }

    public List<KefulistBean> getKefulist() {
        return kefulist;
    }

    public void setKefulist(List<KefulistBean> kefulist) {
        this.kefulist = kefulist;
    }

    public List<CainixihuanBean> getCainixihuan() {
        return cainixihuan;
    }

    public void setCainixihuan(List<CainixihuanBean> cainixihuan) {
        this.cainixihuan = cainixihuan;
    }

    public static class GuanggaoinfoBean {
        /**
         * imgurl : http://app.680.com/images/2018/idx_logo_huodong.jpg
         * infotype : shop
         * itemtype :
         * infoname : 龙博品牌设计
         * infoid : 9331803
         */

        private String imgurl;
        private String infotype;
        private String itemtype;
        private String infoname;
        private String infoid;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getInfotype() {
            return infotype;
        }

        public void setInfotype(String infotype) {
            this.infotype = infotype;
        }

        public String getItemtype() {
            return itemtype;
        }

        public void setItemtype(String itemtype) {
            this.itemtype = itemtype;
        }

        public String getInfoname() {
            return infoname;
        }

        public void setInfoname(String infoname) {
            this.infoname = infoname;
        }

        public String getInfoid() {
            return infoid;
        }

        public void setInfoid(String infoid) {
            this.infoid = infoid;
        }
    }

    public static class AlipayConfigBean {
        /**
         * alipay_flag : 1
         * public_key : MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB
         * private_key : MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAO237yuEWhkhaSYLThF3SeXN19HB5GqEJzUZVJyyh8ucogwpkseCULOu+AbQ/ZLhUgJx9VmMOCRdHFF5RDzE6Sqvvxsa4x6ze2tFhEhgn/pN6co1obmHH8HxxQcU5jALzWX3KDuQAgTAVeCsButhe3f65DfWp9YAVC5UOevQX9/pAgMBAAECgYAPUkuYAm/MLtsYGKJvfcDFjv7aBiUyuFHOmlNZxif6d536KrPLKBsLebuCtPWjAeLHRZEKtH7p2RZ6Kda8E/TzK457URtuVeBpfETxFhaWaIM/T3UoSdZPWFzJBL0+TbRQBwtnBAE/GW4WrEwAz0gsRVJA7nWQoYUbU+8fqnEoAQJBAP/Wdgpoft5leMac3R0fXUNNDlUZftCeCjwWpChQvrp81ylWTCzDt39t1iyvge5Zhu2rC6RwNxgwxFtOilgXMlUCQQDt3of/lc9ErUI2RaPOrHZO4CN4gPjVnzjChZNRDYArVoNy8g179Gqqxhhp2I6Vf01TIYVddbyJuuCyd8TE1xNFAkEAxlEGe5b5EYhzEPKz7ElN0EfBHnJ+/VQk9uNOKPo+fu0bFK4Sqnikm6EW7ti1zX+UulNm2PcrnhLfgy/kydhhaQJBAJ+LM6KtI7FHmHJ4cS9tI4kx8vifYMc/nT1zhGTH7mCjDVWqnnq2bFkQt00/MBt7oCyS/jW6g3PJwW5SwMraCxUCQQDJXQANPqkqAOlaU57qkiU4nbUwvm5mIO23fHLLo7SSiCecFvEXN/1rgMr3osZs4sEN6D6S/7eW1XbqbMU4urnQ
         * seller : 680@680.com
         * partner : 2088101950325505
         */

        private String alipay_flag;
        private String public_key;
        private String private_key;
        private String seller;
        private String partner;

        public String getAlipay_flag() {
            return alipay_flag;
        }

        public void setAlipay_flag(String alipay_flag) {
            this.alipay_flag = alipay_flag;
        }

        public String getPublic_key() {
            return public_key;
        }

        public void setPublic_key(String public_key) {
            this.public_key = public_key;
        }

        public String getPrivate_key() {
            return private_key;
        }

        public void setPrivate_key(String private_key) {
            this.private_key = private_key;
        }

        public String getSeller() {
            return seller;
        }

        public void setSeller(String seller) {
            this.seller = seller;
        }

        public String getPartner() {
            return partner;
        }

        public void setPartner(String partner) {
            this.partner = partner;
        }
    }

    public static class WeixinpayConfigBean {
        /**
         * appid : wxfc34fb9efbdedeae
         * mchid : 1289720501
         * appsecret : 51c56b886b5be869567dd389b3e5d1d6
         * key : d4624c36b6795d1d99dcf0547af5443d
         * weixinpay_flag : 1
         */

        private String appid;
        private String mchid;
        private String appsecret;
        private String key;
        private String weixinpay_flag;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getMchid() {
            return mchid;
        }

        public void setMchid(String mchid) {
            this.mchid = mchid;
        }

        public String getAppsecret() {
            return appsecret;
        }

        public void setAppsecret(String appsecret) {
            this.appsecret = appsecret;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getWeixinpay_flag() {
            return weixinpay_flag;
        }

        public void setWeixinpay_flag(String weixinpay_flag) {
            this.weixinpay_flag = weixinpay_flag;
        }
    }

    public static class TophuandengBean {
        /**
         * imgurl : http://app.680.com/images/app/gz_index_9331803.png
         * infotype : shop
         * itemtype :
         * infoname : 龙博品牌设计
         * infoid : 9331803
         */

        private String imgurl;
        private String infotype;
        private String itemtype;
        private String infoname;
        private String infoid;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getInfotype() {
            return infotype;
        }

        public void setInfotype(String infotype) {
            this.infotype = infotype;
        }

        public String getItemtype() {
            return itemtype;
        }

        public void setItemtype(String itemtype) {
            this.itemtype = itemtype;
        }

        public String getInfoname() {
            return infoname;
        }

        public void setInfoname(String infoname) {
            this.infoname = infoname;
        }

        public String getInfoid() {
            return infoid;
        }

        public void setInfoid(String infoid) {
            this.infoid = infoid;
        }
    }

    public static class FabulinkBean {
        /**
         * name : LOGO 设计500元起
         * price : 500
         * id : 1
         */

        private String name;
        private String price;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class ZscqlinkBean {
        /**
         * name : LOGO 设计500元起
         * price : 500
         * id : 1
         */

        private String name;
        private String price;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class KefulistBean {
        /**
         * imgurl : http://app.680.com/images/2018/kefu_sheji.png
         * name : 设计服务
         * tel : 4006306800
         */

        private String imgurl;
        private String name;
        private String tel;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }

    public static class CainixihuanBean {
        /**
         * imgurl : http://p1.shopimg.680.com/2017-3/7/32017030711354992725_10416580.jpg
         * deal_count : 473
         * dianpu_name : 米旭品牌设计
         * fuwu_name : logo设计/企业logo/公司logo/品牌logo/网站logo/餐饮logo设计专业设计，满意为止！
         * cityname : 上海
         * pingfen : 5.00
         * price : ￥686
         * unit : 个
         * fuwu_id : 52767
         */

        private String imgurl;
        private String deal_count;
        private String dianpu_name;
        private String fuwu_name;
        private String cityname;
        private String pingfen;
        private String price;
        private String unit;
        private String fuwu_id;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getDeal_count() {
            return deal_count;
        }

        public void setDeal_count(String deal_count) {
            this.deal_count = deal_count;
        }

        public String getDianpu_name() {
            return dianpu_name;
        }

        public void setDianpu_name(String dianpu_name) {
            this.dianpu_name = dianpu_name;
        }

        public String getFuwu_name() {
            return fuwu_name;
        }

        public void setFuwu_name(String fuwu_name) {
            this.fuwu_name = fuwu_name;
        }

        public String getCityname() {
            return cityname;
        }

        public void setCityname(String cityname) {
            this.cityname = cityname;
        }

        public String getPingfen() {
            return pingfen;
        }

        public void setPingfen(String pingfen) {
            this.pingfen = pingfen;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getFuwu_id() {
            return fuwu_id;
        }

        public void setFuwu_id(String fuwu_id) {
            this.fuwu_id = fuwu_id;
        }
    }
}
