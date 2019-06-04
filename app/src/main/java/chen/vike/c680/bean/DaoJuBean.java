package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/12/6.
 */

public class DaoJuBean {

    private List<ItemsBean> items;

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * bianhao :
         * name : 金牌雇主服务
         * cont :
         * sid : 9999
         * danjia : 680
         * img : //app.680.com/images/v4/props/9999.png
         */

        private String bianhao;
        private String name;
        private String cont;
        private String sid;
        private String danjia;
        private String img;

        public String getBianhao() {
            return bianhao;
        }

        public void setBianhao(String bianhao) {
            this.bianhao = bianhao;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCont() {
            return cont;
        }

        public void setCont(String cont) {
            this.cont = cont;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getDanjia() {
            return danjia;
        }

        public void setDanjia(String danjia) {
            this.danjia = danjia;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
