package chen.vike.c680.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lht.vike.a680_v1.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chen.vike.c680.bean.WkHotItemGson;
import chen.vike.c680.tools.BaseStatusBarActivity;
import chen.vike.c680.tools.LhtTool;
import chen.vike.c680.tools.OkhttpTool;
import chen.vike.c680.tools.UrlConfig;
import chen.vike.c680.views.CircleImageView;
import chen.vike.c680.views.CustomDigitalClockEnd1;
import chen.vike.c680.views.CustomDigitalClockStar;
import chen.vike.c680.views.LoadingDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lht on 2017/3/21.
 */

public class WeiKeProjectClassActivity extends BaseStatusBarActivity {


    private Map<String, Object> map = new HashMap<>();
    private XRecyclerView xRecyclerView;
    private WkHotItemGson wkHotItemGson;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss");
    private LoadingDialog ld;
    private List<WkHotItemGson.ListBean> itemList = new ArrayList<>();
    private Myadapter myadapter = new Myadapter();
    private final int GET_INFO = 0x123;
    private final int NETWORK_EXCEPTION = 0x111;
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_INFO) {

                if (null != wkHotItemGson) {
                    if (wkHotItemGson.getPagerInfo().getCurrPageIndex() == 1) {
                        itemList.clear();
                        xRecyclerView.refreshComplete();
                    } else {
                        xRecyclerView.loadMoreComplete();
                    }
                    itemList.addAll(wkHotItemGson.getList());
                } else {
                    itemList.clear();
                }
                myadapter.notifyDataSetChanged();

            } else if (msg.what == NETWORK_EXCEPTION) {
                LhtTool.showNetworkException(WeiKeProjectClassActivity.this, msg);
            }

            if (ld != null) {
                ld.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_xiangmu_activity);

        getTitle().setText(getIntent().getStringExtra("name"));
        xRecyclerView = (XRecyclerView) findViewById(R.id.remen_lv);
        View view = findViewById(R.id.no);
        TextView t = (TextView) view.findViewById(R.id.no_txt);
        t.setText("暂时没有项目");
        xRecyclerView.setEmptyView(view);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulseRise);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                map.put("pages", 1);
                getXmInfo();
            }

            @Override
            public void onLoadMore() {
                if (wkHotItemGson.getPagerInfo().getNextPageIndex() == 0) {
                    xRecyclerView.setNoMore(true);
                } else {
                    map.put("pages", wkHotItemGson.getPagerInfo().getNextPageIndex());
                    getXmInfo();
                }

            }
        });
        xRecyclerView.setAdapter(myadapter);

        String a = getIntent().getStringExtra("class1id");
        map.put("class1id", a);
        map.put("num", 10);
        map.put("pages", 1);
        ld = new LoadingDialog(this).setMessage("加载中...");
        ld.show();
        getXmInfo();


    }

    private void getXmInfo() {
        OkhttpTool.Companion.getOkhttpTool().post(UrlConfig.SEARCH_ITEM, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LhtTool.sendMessage(hd, e, NETWORK_EXCEPTION);
            }

            @Override
            public void onResponse(Call call, Response response)  {
                try{
                    String s = response.body().string();
                    LogUtils.d("======================response:" + s);
                    wkHotItemGson = new Gson().fromJson(s, WkHotItemGson.class);
                    hd.sendEmptyMessage(GET_INFO);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHoler> {
        @Override
        public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHoler(LayoutInflater.from(WeiKeProjectClassActivity.this).inflate(R.layout.xiangmu_list_item, null));
        }

        @Override
        public void onBindViewHolder(MyViewHoler holder, final int position) {

            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.itemView.setLayoutParams(lp);
            holder.name.setText(itemList.get(position).getGz_username());
            holder.title.setText(itemList.get(position).getItemname());
            if (itemList.get(position).getVikecn_class1ID().equals("6")) {
                holder.type.setText(getResources().getString(R.string.rwxq_jj));
                holder.money.setText("￥" + itemList.get(position).getMoney());
                if (itemList.get(position).getPayok().equals("1")) {
                    holder.tuoguan.setVisibility(View.VISIBLE);
                    holder.tuoguan.setText("已托管");
                } else {
                    holder.tuoguan.setVisibility(View.GONE);
                }
            } else {
                if (Integer.valueOf(itemList.get(position).getZab_do()) == 1) {
                    holder.type.setText(getResources().getString(R.string.rwxq_zb));
                    holder.type.setTextColor(getResources().getColor(R.color.text_color_2));
                    if (itemList.get(position).getZab_yusuan1().equals(itemList.get(position).getZab_yusuan2())) {
                        holder.money.setText("￥" + itemList.get(position).getZab_yusuan1());
                    } else {
                        holder.money.setText("￥" + itemList.get(position).getZab_yusuan1() + "-￥" + itemList.get(position).getZab_yusuan2());
                    }

                    if (itemList.get(position).getPayok().equals("1")) {
                        holder.tuoguan.setVisibility(View.VISIBLE);
                        holder.tuoguan.setText("已托管：￥" + itemList.get(position).getMoney());
                    } else {
                        holder.tuoguan.setVisibility(View.GONE);
                    }
                } else {
                    holder.type.setText(getResources().getString(R.string.rwxq_xs));
                    holder.type.setTextColor(getResources().getColor(R.color.text_color_10));
                    holder.money.setText("￥" + itemList.get(position).getMoney());
                    if (itemList.get(position).getPayok().equals("1")) {
                        holder.tuoguan.setVisibility(View.VISIBLE);
                        holder.tuoguan.setText("已托管");
                    } else {
                        holder.tuoguan.setVisibility(View.GONE);
                    }
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", itemList.get(position).getItemid());
                    Intent intent = new Intent(WeiKeProjectClassActivity.this, TaskDetailsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            long endTime = new Date(itemList.get(position).getEndtime()).getTime()
                    - new Date(dateFormat.format(new Date())).getTime();
            long starTime =
                    new Date(dateFormat.format(new Date())).getTime() - new Date(itemList.get(position).getAddtime()).getTime();
            holder.end.setEndTime(System.currentTimeMillis() + endTime, Integer.valueOf(itemList.get(position).getState()));
            holder.star.setEndTime(starTime - System.currentTimeMillis());
            holder.bs.setText(itemList.get(position).getCynum() + "人投标");
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(WeiKeProjectClassActivity.this).load(itemList.get(position).getGz_imgurl()).apply(options).into(holder.civ);
        }

        @Override
        public int getItemCount() {
            if (null == itemList) {
                return 0;
            }
            return itemList.size();
        }

        class MyViewHoler extends RecyclerView.ViewHolder {

            private CircleImageView civ;
            private TextView title;
            private TextView name;
            private TextView money;
            private TextView tuoguan;
            private TextView type;
            private CustomDigitalClockStar star;
            private CustomDigitalClockEnd1 end;
            private TextView bs;
            private View view;


            public MyViewHoler(View itemView) {
                super(itemView);
                civ = (CircleImageView) itemView.findViewById(R.id.fb_logo);
                name = (TextView) itemView.findViewById(R.id.cy_name);
                title = (TextView) itemView.findViewById(R.id.title);
                money = (TextView) itemView.findViewById(R.id.now_money);
                tuoguan = (TextView) itemView.findViewById(R.id.tg);
                star = (CustomDigitalClockStar) itemView.findViewById(R.id.timeStar);
                end = (CustomDigitalClockEnd1) itemView.findViewById(R.id.timeEnd);
                bs = (TextView) itemView.findViewById(R.id.bs);
                type = (TextView) itemView.findViewById(R.id.xm_type);

            }
        }
    }
}
