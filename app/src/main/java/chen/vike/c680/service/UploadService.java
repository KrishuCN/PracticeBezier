package chen.vike.c680.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chen.vike.c680.tools.DataStatisticsBean;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by chen on 2018/12/7.
 * 8 *
 * 8
 *   上传页面统计数据的服务
 */
public class UploadService extends Service {
   private String strJson = "";
   private List<DataStatisticsBean> jsonList = new ArrayList<>();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // uploadJson();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("upload","oncreate");
        jsonList = LitePal.findAll(DataStatisticsBean.class);
        Gson gson = new Gson();
        strJson = gson.toJson(jsonList);
        uploadJson();

    }

    /**
     * 上传json语句
     */
    private void uploadJson(){
        OkhttpTool.Companion.getOkhttpTool().uploadJson(UrlConfig.UPLOADJSON, strJson, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
               // LitePal.deleteAll(DataStatisticsBean.class);
                Log.e("upload", s + "");
            }
        });
    }
}
