package chen.vike.c680.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import chen.vike.c680.tools.BaseStatusBarActivity;
import com.lht.vike.a680_v1.R;

/**
 * Created by lht on 2017/3/18.
 *
 * 协议展示界面
 */

public class WeiKeServiceProvideractivity extends BaseStatusBarActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serviceprovideractivity);
        getTitle().setText("服务协议");
    }
}
