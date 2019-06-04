package chen.vike.c680.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lht on 2017/10/18.
 */

public class ShardTools {
        private static ShardTools shardTools;
        private static Context mContext;
        private static final String SHARNAME = "tempData";

        public ShardTools(Context context){
            mContext = context.getApplicationContext();
        }

        public static synchronized ShardTools getInstance(Context context){
            if (shardTools==null){
                synchronized (ShardTools.class){
                    shardTools = new ShardTools(context);
                }
            }
            return shardTools;
        }
    public void tempSaveSharedata(String fileName,String dataContent){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dataContent",dataContent);
        editor.commit();
    }
    public String getTempSharedata(String fileName){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        String dataContent = sharedPreferences.getString("dataContent","0");
        return dataContent;
    }
}
