package chen.vike.c680.bean;

/**
 * Created by lht on 2017/1/23.
 */

public class MsgBean {

    private int type;
    private String content;
    private String imageUrl;
    public static final int TYPE_RECIVE = 0;
    public static final int TYPE_SEND = 1;

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
