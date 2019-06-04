package chen.vike.c680.ALiPay;

import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    private ExecutorService service;

    private ThreadPoolManager() {
        int num = Runtime.getRuntime().availableProcessors();
        service = Executors.newFixedThreadPool(num * 2);
    }

    private static final ThreadPoolManager manager = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return manager;
    }

    public void addTask(Runnable runnable) {
        String TAG = "ThreadPoolManager";
        LogUtils.v(TAG, "加载任务");
        service.execute(runnable);
    }
}
