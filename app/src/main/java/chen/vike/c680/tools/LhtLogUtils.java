package chen.vike.c680.tools;

import android.util.Log;

/**
 * Created by lht on 2017/2/28.
 */

public class LhtLogUtils {

    private static LhtLogUtils lhtLogUtils;
    public static boolean showLog;

    public static LhtLogUtils getLhtLogUtils() {
        if (null == lhtLogUtils) {
            synchronized (LhtLogUtils.class) {
                if (null == lhtLogUtils) {
                    lhtLogUtils = new LhtLogUtils();
                }
            }
        }
        return lhtLogUtils;
    }


    public void v(String tag, String msg) {
        if (showLog) {
            Log.v(tag, msg);
        }
    }

    public void d(String tag, String msg) {
        if (showLog) {
            Log.d(tag, msg);
        }
    }

    public void i(String tag, String msg) {
        if (showLog) {
            Log.i(tag, msg);
        }
    }

    public void w(String tag, String msg) {
        if (showLog) {
            Log.w(tag, msg);
        }
    }

    public void e(String tag, String msg) {
        if (showLog) {
            Log.e(tag, msg);
        }
    }


}
