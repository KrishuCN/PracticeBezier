package chen.vike.c680.tools;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import chen.vike.c680.bean.WeiKePageGson;
import chen.vike.c680.bean.WeikeMoreGson;
import com.tencent.mm.opensdk.utils.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/10/16.
 * 访问网络数据工具类
 */

public class HttpTools {
    private String filed_id = "1";
    private WeikeMoreGson weikeMoreGson = new WeikeMoreGson();
    public void WeikeContentList(){
        Map<String, Object> map = new HashMap<>();
        Log.e("TAG","===========");
        map.put("user_classid", filed_id);
        map.put("pages", "1");
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.WEIKE_ITEM_MORE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) {

                try{
                    String s = response.body().string();
                    Log.e("TAG",""+s);
                    weikeMoreGson = new Gson().fromJson(s, WeikeMoreGson.class);
                    LogUtils.e("TAG", "=========================weikeMoreGson.getItem_list().size():" + weikeMoreGson.getItem_list().size());
                    for (int i = 0; i < weikeMoreGson.getItem_list().size(); i++) {
                        WeiKePageGson.ItemListBean listBean = new WeiKePageGson.ItemListBean();
                        listBean.setField_id(weikeMoreGson.getItem_list().get(i).getField_id());
                        listBean.setItemid(weikeMoreGson.getItem_list().get(i).getItemid());
                        listBean.setItemname(weikeMoreGson.getItem_list().get(i).getItemname());
                        listBean.setPrice(weikeMoreGson.getItem_list().get(i).getPrice());
                        listBean.setContent(weikeMoreGson.getItem_list().get(i).getContent());
                        listBean.setEndtime(weikeMoreGson.getItem_list().get(i).getEndtime());
                        listBean.setItemtype(weikeMoreGson.getItem_list().get(i).getItemtype());
                        ConfigTools.weiKeList.add(listBean);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}
