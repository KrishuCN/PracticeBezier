package chen.vike.c680.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lht.vike.a680_v1.R;

import java.util.List;

import chen.vike.c680.views.PickerView;

/**
 * Created by lht on 2017/11/6.
 */

public class WheelActivity extends AppCompatActivity {
    private PickerView pickerView;
    private List<String> mDatas;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

    }
}
