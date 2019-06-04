package chen.vike.c680.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import chen.vike.c680.tools.BaseStatusBarActivity;
import com.lht.vike.a680_v1.R;

/**
 * Created by lht on 2017/3/10.
 *
 * 参与的项目
 */

public class ParticipateProjectActivity extends BaseStatusBarActivity {

    private View scdfw_qb;
    private TextView cydxm_ybx;
    private TextView cydxm_yzb;
    private TextView cydxm_bhg;
    private TextView scdfw_dtg;
    private TextView scdfw_dqr;
    private Intent in;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glzx_cydxm);
        getTitle().setText("参与的项目");

        cydxm_bhg = findViewById(R.id.cydfw_bhg);
        cydxm_ybx = findViewById(R.id.cydfw_ybx);
        cydxm_yzb = findViewById(R.id.cydfw_yzb);
        scdfw_qb = findViewById(R.id.scdfw_qb);
        scdfw_dtg = findViewById(R.id.scdfw_dtg);
        scdfw_dqr = findViewById(R.id.scdfw_dqr);
        cydxm_ybx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到悬赏界面
                in = new Intent(ParticipateProjectActivity.this, Cydxm_XuanShangActivity.class);
                startActivity(in);
            }
        });
        cydxm_yzb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到招标界面
                in = new Intent(ParticipateProjectActivity.this, Cydxm_ZhaoBiaoActivity.class);
                startActivity(in);
            }
        });
        cydxm_bhg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到计件界面
                in = new Intent(ParticipateProjectActivity.this, Cydxm_JiJianActivity.class);
                startActivity(in);
            }
        });
        scdfw_qb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到雇佣全部界面
                in = new Intent(ParticipateProjectActivity.this, Cydxm_GuYongActivity.class);
                in.putExtra("item", "全部");
                startActivity(in);
            }
        });
        scdfw_dtg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到雇佣进行中界面
                in = new Intent(ParticipateProjectActivity.this, Cydxm_GuYongActivity.class);
                in.putExtra("item", "进行中");
                startActivity(in);
            }
        });
        scdfw_dqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到雇佣已完成界面
                in = new Intent(ParticipateProjectActivity.this, Cydxm_GuYongActivity.class);
                in.putExtra("item", "已完成");
                startActivity(in);
            }
        });
    }
}
