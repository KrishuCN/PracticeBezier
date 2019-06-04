package chen.vike.c680.bean;

/**

 */
public class WeiKeModel01Bean {
    public String title;
    public String url;
    public String biaoti;
    public WeiKeModel01Bean(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public WeiKeModel01Bean(String title, String url, String biaoti) {
        this.title = title;
        this.url = url;
        this.biaoti = biaoti;
    }

    public WeiKeModel01Bean(String title) {
        this.title = title;
    }
}
