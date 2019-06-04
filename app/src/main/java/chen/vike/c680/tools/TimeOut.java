package chen.vike.c680.tools;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName: TimeOut
 * @Description:   访问超时接口
 * @Author: chen
 * @Date: 2019/1/31
 */
public class TimeOut {

    /**
         * 网页访问判断超时问题
         */
        public static final int LOADTIMEOUT = 0x101;
        private static Timer timer;//计时器
        private static long timeout = 5000;//超时时间

        public static void webTimeOut(final Handler handler) {
            timer = new Timer();
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    /* * 超时后,首先判断页面加载是否小于100,就执行超时后的动作 */
                    handler.sendEmptyMessage(LOADTIMEOUT);
                }
            };
            timer.schedule(tt, timeout, timeout);
        }

        public  static void timeCancel(){
            timer.cancel();
            timer.purge();
        }

}
