package chen.vike.c680.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import chen.vike.c680.main.BaseActivity;
import com.lht.vike.a680_v1.R;

import java.util.HashMap;
import java.util.Map;

import chen.vike.c680.tools.ShardTools;

/**
 * Created by lht on 2017/10/20.
 */

public class VipExclusiveActivity extends BaseActivity {
    private static String URL = "http://app.680.com/api/v3/vip_user_kefu.aspx";
    private WebView webView;
    private ImageView back;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_exclusive);
        iniview();
    }
   private void iniview(){
       webView = (WebView) findViewById(R.id.vip_phone_webview);
       back = (ImageView) findViewById(R.id.btn_back);
       Map<String,String> map = new HashMap<>();
       String userid = ShardTools.getInstance(this).getTempSharedata("userid");
       map.put("userid",userid);
       webView.loadUrl(URL,map);
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
   }
    @Override
    protected void onResume() {
        super.onResume();
       // phoneHttpData();
    }

    /**
     * 专属客服网络数据
     */

}
