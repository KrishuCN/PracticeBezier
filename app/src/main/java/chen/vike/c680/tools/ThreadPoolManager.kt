package chen.vike.c680.tools

import android.util.Log

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ThreadPoolManager private constructor() {
    private val service: ExecutorService

    init {
        val num = Runtime.getRuntime().availableProcessors()
        service = Executors.newFixedThreadPool(num * 2)
    }

    fun addTask(runnable: Runnable) {
        val tag = "ThreadPoolManager"
        Log.v(tag, "加载任务")
        service.execute(runnable)
    }

    fun shutDown(){
        if (!service.isShutdown || !service.isTerminated){
            service.shutdown()
        }
    }

    companion object {

        val instance = ThreadPoolManager()
    }
}
