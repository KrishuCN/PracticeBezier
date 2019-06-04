package chen.vike.c680.bean;

import java.util.List;

/**
 * Created by lht on 2017/3/13.
 */

public class XiangMuFenLeiGson {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * classID : 1
         * className : 平面设计
         * parID : 0
         */

        private String classID;
        private String className;
        private String parID;

        public String getClassID() {
            return classID;
        }

        public void setClassID(String classID) {
            this.classID = classID;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getParID() {
            return parID;
        }

        public void setParID(String parID) {
            this.parID = parID;
        }
    }
}
