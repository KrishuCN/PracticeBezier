package chen.vike.c680.bean;

/**
 * Created by lht on 2017/11/8.
 */

public class FaPiaoBean {

    /**
     * faPiaoInfo : {"id":4348,"userid":0,"itemid":0,"fpkind":2,"fptt":"dgsdgsdg","fpnr":"网络服务费","fpsjr":"sdfsdfsd","fpsjdz":"fsdfsdfsdf","fpyzbm":"","fplxdh":"sdfsdfsdfsdfsd","addtime":"0001-01-01T00:00:00","flag":0,"ctime":"0001-01-01T00:00:00","bz":null,"fp_nsr_code":"fsdfsdfsdf"}
     */

    private FaPiaoInfoBean faPiaoInfo;

    public FaPiaoInfoBean getFaPiaoInfo() {
        return faPiaoInfo;
    }

    public void setFaPiaoInfo(FaPiaoInfoBean faPiaoInfo) {
        this.faPiaoInfo = faPiaoInfo;
    }

    public static class FaPiaoInfoBean {
        /**
         * id : 4348
         * userid : 0
         * itemid : 0
         * fpkind : 2
         * fptt : dgsdgsdg
         * fpnr : 网络服务费
         * fpsjr : sdfsdfsd
         * fpsjdz : fsdfsdfsdf
         * fpyzbm :
         * fplxdh : sdfsdfsdfsdfsd
         * addtime : 0001-01-01T00:00:00
         * flag : 0
         * ctime : 0001-01-01T00:00:00
         * bz : null
         * fp_nsr_code : fsdfsdfsdf
         */

        private int id;
        private int userid;
        private int itemid;
        private int fpkind;
        private String fptt;
        private String fpnr;
        private String fpsjr;
        private String fpsjdz;
        private String fpyzbm;
        private String fplxdh;
        private String addtime;
        private int flag;
        private String ctime;
        private Object bz;
        private String fp_nsr_code;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public int getItemid() {
            return itemid;
        }

        public void setItemid(int itemid) {
            this.itemid = itemid;
        }

        public int getFpkind() {
            return fpkind;
        }

        public void setFpkind(int fpkind) {
            this.fpkind = fpkind;
        }

        public String getFptt() {
            return fptt;
        }

        public void setFptt(String fptt) {
            this.fptt = fptt;
        }

        public String getFpnr() {
            return fpnr;
        }

        public void setFpnr(String fpnr) {
            this.fpnr = fpnr;
        }

        public String getFpsjr() {
            return fpsjr;
        }

        public void setFpsjr(String fpsjr) {
            this.fpsjr = fpsjr;
        }

        public String getFpsjdz() {
            return fpsjdz;
        }

        public void setFpsjdz(String fpsjdz) {
            this.fpsjdz = fpsjdz;
        }

        public String getFpyzbm() {
            return fpyzbm;
        }

        public void setFpyzbm(String fpyzbm) {
            this.fpyzbm = fpyzbm;
        }

        public String getFplxdh() {
            return fplxdh;
        }

        public void setFplxdh(String fplxdh) {
            this.fplxdh = fplxdh;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public Object getBz() {
            return bz;
        }

        public void setBz(Object bz) {
            this.bz = bz;
        }

        public String getFp_nsr_code() {
            return fp_nsr_code;
        }

        public void setFp_nsr_code(String fp_nsr_code) {
            this.fp_nsr_code = fp_nsr_code;
        }
    }
}
