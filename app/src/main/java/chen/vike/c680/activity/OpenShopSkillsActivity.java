package chen.vike.c680.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import chen.vike.c680.bean.XiangMuFenLeiGson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CustomToast;
import chen.vike.c680.views.LoadingDialog;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/13.
 * 技能选择界面
 */

public class OpenShopSkillsActivity extends BaseStatusBarActivity {

    private FLAdapter flAdapter0;
    private FLAdapter flAdapter1;
    private List<XiangMuFenLeiGson.ListBean> list = new ArrayList<>();
    private List<XiangMuFenLeiGson.ListBean> list3 = new ArrayList<>();
    private List<Boolean> booleanList1 = new ArrayList<>();
    public static String ti, na;
    public static List<String> nalist = new ArrayList<String>();
    private LinearLayout sure_linear;
    public static String Did;
    public static List<String> xid = new ArrayList<String>();
    private RecyclerView class1;
    private RecyclerView class2;
    private int GETINFO_ONE = 0x123;
    private int NETWORKEXCEPTION = 0x213;
    private XiangMuFenLeiGson xiangMuFenLeiGson;
    //RecyclerView分割线
    private RecyclerView.ItemDecoration itemDecoration;
    private LoadingDialog ld;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fenlei_skill);

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
        class1 = findViewById(R.id.class1);
        class2 = findViewById(R.id.class2);
        sure_linear = findViewById(R.id.skill_queding);
        class1.setLayoutManager(new LinearLayoutManager(this));
        class2.setLayoutManager(new GridLayoutManager(this, 2));
        flAdapter0 = new FLAdapter(0);
        flAdapter1 = new FLAdapter(1);
        class1.setAdapter(flAdapter0);
        class2.setAdapter(flAdapter1);
        itemDecoration = new LhtTool.MyItemDecoration(getResources());
        class1.addItemDecoration(itemDecoration);
        sure_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可以直接把数据返回回去，他用的静态，时间不够，我也懒得改了
                setResult(10);
                finish();
            }
        });
    }

    public void selectClass2(String id) {
        list3.clear();
        flAdapter1.refresh2();
        for (int i = 0; i < xiangMuFenLeiGson.getList().size(); i++) {
            if (xiangMuFenLeiGson.getList().get(i).getParID().equals(id)) {
                list3.add(xiangMuFenLeiGson.getList().get(i));
                LogUtils.d("===============list3.get(i).getClassName():" + xiangMuFenLeiGson.getList().get(i).getClassName());
            }
        }
        flAdapter1.addList2(list3);
        LogUtils.d("===================list3.size():" + list3.size());
        flAdapter1.notifyDataSetChanged();
        if (ld != null) {
            ld.dismiss();
        }
    }


    class FLAdapter extends RecyclerView.Adapter {
        private int type; //0 表示大类 1表示小类
        private List<Boolean> booleanList = new ArrayList<>();
        private int DL;
        private int XL;
        private List<XiangMuFenLeiGson.ListBean> list_one = new ArrayList<>();
        private List<XiangMuFenLeiGson.ListBean> list_two = new ArrayList<>();

        public void addList1(List<XiangMuFenLeiGson.ListBean> list) {
            this.list_one.addAll(list);
        }

        public void refresh1() {
            this.list_one.clear();
        }

        public void addList2(List<XiangMuFenLeiGson.ListBean> list) {
            this.list_two.addAll(list);
        }

        public void refresh2() {
            this.list_two.clear();
        }


        public FLAdapter(int type) {
            this.type = type;
        }

        class FlViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public View view;


            public FlViewHolder(View itemView) {
                super(itemView);
                if (type == 0) {
                    title = (TextView) itemView.findViewById(R.id.name);
                } else if (type == 1) {
                    title = (TextView) itemView.findViewById(R.id.name1);
                }
                view = itemView;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FlViewHolder flViewHolder = null;
            if (type == 0) {
                flViewHolder = new FlViewHolder(LayoutInflater.from(
                        OpenShopSkillsActivity.this).inflate(R.layout.class1_item, null));
                for (int i = 0; i < list_one.size(); i++) {
                    if (i == 0) {
                        booleanList.add(true);
                    } else {
                        booleanList.add(false);
                    }

                }
                return flViewHolder;

            } else if (type == 1) {
                flViewHolder = new FlViewHolder(LayoutInflater.from(

                        OpenShopSkillsActivity.this).inflate(R.layout.openskill_activity, null));

            }
//            return null;
            return flViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
            if (type == 0) {
                FlViewHolder holder = (FlViewHolder) holder1;
                holder.title.setText(list_one.get(position).getClassName());
                holder.title.setTextColor(getResources().getColor(R.color.colorInfo));
                holder.title.setTextSize(14);
                holder.view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectClass2(list_one.get(position).getClassID());
                        booleanList1.clear();
                        for (int i = 0; i < list_one.size(); i++) {
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
                    holder.title.setBackgroundColor(getResources().getColor(R.color.colorbg));
                    ti = list_one.get(position).getClassName();
                    Did = list_one.get(position).getClassID();
                    nalist.clear();
                    xid.clear();
                } else {
                    holder.title.setBackgroundColor(getResources().getColor(R.color.white));
                }
            } else if (type == 1) {
                for (int i = 0; i < list_two.size(); i++) {
                    booleanList1.add(false);
                }
                final FlViewHolder holder = (FlViewHolder) holder1;
                holder.title.setText(list_two.get(position).getClassName());
                holder.title.setTextColor(getResources().getColor(R.color.colorInfo));
                holder.title.setTextSize(12);
                if (position % 2 == 0) {
                    holder.view.setPadding(40, 20, 0, 20);
                } else {
                    holder.view.setPadding(0, 20, 20, 20);
                }
//                holder.view.setLayoutParams(new LinearLayout.LayoutParams(new DisplayMetrics().widthPixels / 7 * 2, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (nalist.size() == 0) {
                            booleanList1.add(position, true);
                            nalist.add(list_two.get(position).getClassName());
                            xid.add(list_two.get(position).getClassID());
                            holder.title.setBackgroundResource(R.drawable.fenlei_item_check);
                            //将字体颜色改为白色
                            holder.title.setTextColor(android.graphics.Color.parseColor("#FFFFFF"));
                        } else if (nalist.size() < 5) {
                            boolean flag = true;
                            for (int i = 0; i < nalist.size(); i++) {
                                if (list_two.get(position).getClassName().equals(nalist.get(i))) {
                                    booleanList1.add(position, false);
                                    nalist.remove(i);
                                    xid.remove(i);
                                    holder.title.setBackgroundResource(R.drawable.fenlei_item_white);
                                    //没有选中则改为黑色
                                    holder.title.setTextColor(android.graphics.Color.parseColor("#333333"));
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
                                booleanList1.add(position, true);
                                nalist.add(list_two.get(position).getClassName());
                                xid.add(list_two.get(position).getClassID());
                                holder.title.setBackgroundResource(R.drawable.fenlei_item_check);
                                //将字体颜色改为白色
                                holder.title.setTextColor(android.graphics.Color.parseColor("#FFFFFF"));
                            }
                        } else if (nalist.size() == 5) {
                            boolean flag = true;
                            for (int i = 0; i < nalist.size(); i++) {
                                if (list_two.get(position).getClassName().equals(nalist.get(i))) {
                                    booleanList1.add(position, false);
                                    nalist.remove(i);
                                    xid.remove(i);
                                    holder.title.setBackgroundResource(R.drawable.fenlei_item_white);
                                    //没有选中则改为黑色
                                    holder.title.setTextColor(android.graphics.Color.parseColor("#333333"));
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
                                CustomToast.showToast(OpenShopSkillsActivity.this, "最多选择5个", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                });
            }


        }

        @Override
        public int getItemCount() {

            if (type == 0) {
                return list_one.size();
            } else if (type == 1) {
                return list_two.size();

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
                flAdapter0.addList1(list);
                selectClass2("1");


            } else if (msg.what == NETWORKEXCEPTION) {

                LhtTool.showNetworkException(OpenShopSkillsActivity.this, msg);
            }
        }
    };

}

