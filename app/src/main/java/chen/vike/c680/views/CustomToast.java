package chen.vike.c680.views;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lht on 2017/4/25.
 */

public class CustomToast {

    private static Toast mToast;

    public static void showToast(Context mContext, String text, int duration) {
        if (mToast != null){
            mToast.setText(text);
        }else{
            mToast = Toast.makeText(mContext.getApplicationContext(), text, duration);
        }
        mToast.show();
    }
}
