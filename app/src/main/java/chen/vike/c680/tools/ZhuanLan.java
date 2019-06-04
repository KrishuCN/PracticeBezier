package chen.vike.c680.tools;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import com.lht.vike.a680_v1.R;

/**
 * Created by lht on 2017/3/2.
 */

public class ZhuanLan extends BaseStatusBarActivity{

    private LinearLayout linear_bar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuanlan_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            linear_bar = (LinearLayout) findViewById(R.id.linear_bar);
            linear_bar.setVisibility(View.VISIBLE);
            int statusHeight = LhtTool.getStatusBarHeight(this);
            android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) linear_bar.getLayoutParams();
            params.height = statusHeight;
            linear_bar.setLayoutParams(params);
        }








    }
}
