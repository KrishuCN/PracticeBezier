package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import chen.vike.c680.bean.XiangMuFenLeiGson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.LoadingDialog;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/17.
 *
 *   发布服务全部分类
 */

public class CatoryActivity extends BaseStatusBarActivity{

    private FLAdapter flAdapter0;
    private FLAdapter flAdapter1;
    private List<XiangMuFenLeiGson.ListBean> list = new ArrayList<>();
    private List<XiangMuFenLeiGson.ListBean> list3 = new ArrayList<>();
    private List<Boolean> booleanList1 = new ArrayList<>();
    public static String ti,na;
    public static String Did,Xid;
    private RecyclerView.ItemDecoration itemDecoration;
    private XiangMuFenLeiGson xiangMuFenLeiGson;
    private DisplayMetrics metrics = new DisplayMetrics();
    private int GETINFO_ONE = 0x123;
    private int NETWORKEXCEPTION = 0x213;
    private LoadingDialog ld;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_fenlei_list);

        getTitle().setText("全部分类");
        ld = new LoadingDialog(this).setMessage("数据加载中...");
        ld.show();
        OkhttpTool.Companion.getOkhttpTool().get(UrlConfig.GET_FENLEI_INFO, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORKEXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try{
                    String s = response.body().string();
                    LogUtils.d("=============Response:" + s);
                    xiangMuFenLeiGson = new Gson().fromJson(s, XiangMuFenLeiGson.class);
                    hd.sendEmptyMessage(GETINFO_ONE);
                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });
        XRecyclerView class1 = (XRecyclerView) findViewById(R.id.class1);
        XRecyclerView class2 = (XRecyclerView) findViewById(R.id.class2);
        class1.setPullRefreshEnabled(false);
        class1.setLayoutManager(new LinearLayoutManager(this));
        class2.setPullRefreshEnabled(false);
        class2.setLayoutManager(new GridLayoutManager(this, 2));
        flAdapter0 = new FLAdapter(0);
        flAdapter1 = new FLAdapter(1);
        class1.setAdapter(flAdapter0);
        class2.setAdapter(flAdapter1);
        itemDecoration = new LhtTool.MyItemDecoration(getResources());
        class1.addItemDecoration(itemDecoration);
    }

    /**
     * 选择的类别
     * @param id  类别的id号
     */
    public void selectClass2(String id) {
        String class1id = id;
        list3.clear();
        for (int i = 0; i < xiangMuFenLeiGson.getList().size(); i++) {
            if (xiangMuFenLeiGson.getList().get(i).getParID().equals(id)) {
                list3.add(xiangMuFenLeiGson.getList().get(i));
                LogUtils.d("===============list3.get(i).getClassName():" + xiangMuFenLeiGson.getList().get(i).getClassName());
            }
        }
        LogUtils.d("===================list3.size():" + list3.size());
        flAdapter1.notifyDataSetChanged();
        if (ld != null) {
            ld.dismiss();
        }
    }

    /**
     * 列表的Adapter
     */
    class FLAdapter extends RecyclerView.Adapter {
        private int type; //0 表示大类 1表示小类
        private List<Boolean> booleanList = new ArrayList<>();
        private int DL;
        private int XL;

        public FLAdapter(int type) {
            this.type = type;
        }

        class FlViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public View view;


            public FlViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.name);
                view = itemView;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == DL) {
                FlViewHolder flViewHolder = new FlViewHolder(LayoutInflater.from(
                        CatoryActivity.this).inflate(R.layout.class1_item, parent,
                        false));
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        booleanList.add(true);
                    } else {
                        booleanList.add(false);
                    }

                }
                return flViewHolder;

            } else if (viewType == XL) {
                FlViewHolder flViewHolder = new FlViewHolder(LayoutInflater.from(

                        CatoryActivity.this).inflate(R.layout.class1_item, parent,
                        false));
                return flViewHolder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
            if (type == 0) {
                FlViewHolder holder = (FlViewHolder) holder1;
                holder.title.setText(list.get(position).getClassName());
                holder.title.setTextColor(getResources().getColor(R.color.colorInfo));
                holder.title.setTextSize(14);
                holder.view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectClass2(list.get(position).getClassID());
                        booleanList1.clear();
                        for (int i = 0; i < list.size(); i++) {
                            if (i == position) {
                                booleanList.add(i, true);
                            } else {
                                booleanList.add(i, false);
                            }
                        }
                        flAdapter0.notifyDataSetChanged();
                    }
                });
                if (booleanList.get(position)) {
                    ti=list.get(position).getClassName();
                    Did=list.get(position).getClassID();
                    holder.title.setBackgroundColor(getResources().getColor(R.color.colorbg));
                } else {
                    holder.title.setBackgroundColor(getResources().getColor(R.color.white));
                }
            } else if (type == 1) {
                for (int i = 0; i < list3.size(); i++) {
                    booleanList1.add(false);
                }
                FlViewHolder holder = (FlViewHolder) holder1;
                holder.title.setText(list3.get(position).getClassName());
                //这句可能有问题
                ti=list3.get(position).getClassName();
                holder.title.setTextColor(getResources().getColor(R.color.colorText));
                holder.title.setTextSize(12);
                if (position % 2 == 0) {
                    holder.view.setPadding(40, 20, 0, 20);
                } else {
                    holder.view.setPadding(0, 20, 20, 20);
                }
//                holder.view.setLayoutParams(new LinearLayout.LayoutParams(metrics.widthPixels / 7 * 2, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i = 0; i < list3.size(); i++) {
                            if (i == position) {
                                booleanList1.add(i, true);
                            } else {
                                booleanList1.add(i, false);
                            }
                        }
                        flAdapter1.notifyDataSetChanged();
                        setResult(400);
                        finish();

                    }
                });
                if (booleanList1.get(position)) {
                    na=list3.get(position).getClassName();
                    Xid=list3.get(position).getClassID();
                    holder.title.setBackgroundResource(R.drawable.fenlei_item_bg);
                } else {
                    holder.title.setBackgroundResource(R.drawable.fenlei_item_bg1);
                }

            }

        }

        @Override
        public int getItemCount() {

            if (type == 0) {
                return list.size();
            } else if (type == 1) {
                return list3.size();

            }

            return 0;
        }

    }
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GETINFO_ONE) {

                for (int i = 0; i < xiangMuFenLeiGson.getList().size(); i++) {
                    if (xiangMuFenLeiGson.getList().get(i).getParID().equals("0")) {
                        list.add(xiangMuFenLeiGson.getList().get(i));
                    }
                }
                selectClass2("1");


            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(CatoryActivity.this, msg);
            }
        }
    };

}
