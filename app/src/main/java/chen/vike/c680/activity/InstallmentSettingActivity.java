package chen.vike.c680.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import chen.vike.c680.tools.BaseStatusBarActivity;
import com.lht.vike.a680_v1.R;

/**
 * Created by lht on 2017/6/1.
 *
 * 分期设置页面
 */

public class InstallmentSettingActivity extends BaseStatusBarActivity {

    private CheckBox cb_2;
    private CheckBox cb_3;
    private CheckBox cb_4;
    private CheckBox cb_5;
    private Button bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.installment_setting_activity);

        getTitle().setText("分期付款");
        cb_2 =  findViewById(R.id.fenqi_cb_2);
        cb_3 =  findViewById(R.id.fenqi_cb_3);
        cb_4 =  findViewById(R.id.fenqi_cb_4);
        cb_5 =  findViewById(R.id.fenqi_cb_5);
        bt =  findViewById(R.id.fenqi_bt);

        cb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstallmentSettingActivity.this, InstallmentActivity.class);
                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                intent.putExtra("NUMBER", "2");
                startActivityForResult(intent,1);
            }
        });


        cb_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstallmentSettingActivity.this, InstallmentActivity.class);
                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                intent.putExtra("NUMBER", "3");
                startActivityForResult(intent,1);
            }
        });


        cb_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstallmentSettingActivity.this, InstallmentActivity.class);
                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                intent.putExtra("NUMBER", "4");
                startActivityForResult(intent,1);
            }
        });


        cb_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstallmentSettingActivity.this, InstallmentActivity.class);
                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                intent.putExtra("NUMBER", "5");
                startActivityForResult(intent,1);
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(InstallmentSettingActivity.this, FuKuanActivity.class);
                intent.putExtra("ID", getIntent().getStringExtra("ID"));
                intent.putExtra("ZB", "1");
                startActivityForResult(intent,1);

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
