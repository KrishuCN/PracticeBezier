package chen.vike.c680.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import chen.vike.c680.bean.WeiKePageGson;
import com.lht.vike.a680_v1.R;

import java.util.ArrayList;
import java.util.List;

import chen.vike.c680.Interface.ViewItemClick;

/**
 * Created by lht on 2017/10/16.
 */

public class VipRecyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WeiKePageGson.ItemListBean> lists=new ArrayList<WeiKePageGson.ItemListBean>();
    private View view;
    private Context context;
    private ViewItemClick viewItemClick;


    public ViewItemClick getViewItemClick() {
        return viewItemClick;
    }

    public void setViewItemClick(ViewItemClick viewItemClick) {
        this.viewItemClick = viewItemClick;
    }

    public VipRecyListAdapter(List<WeiKePageGson.ItemListBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    public void addlist(List<WeiKePageGson.ItemListBean> list) {
        lists.addAll(list);
        notifyDataSetChanged();

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            view = LayoutInflater.from(context).inflate(R.layout.listview_rmxm, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
         MyViewHolder myViewHolder = (MyViewHolder) holder;
        Log.e("TAG","Adapter");
        if (myViewHolder.title != null){
            if (lists != null && lists.size() > 0) {
                myViewHolder.title.setText(lists.get(position).getItemname());
                myViewHolder.content.setText(lists.get(position).getContent());
                myViewHolder.money.setText("￥" + lists.get(position).getPrice());
                myViewHolder.time.setText(lists.get(position).getEndtime());
            }
            myViewHolder.tableItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getViewItemClick().shortClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.e("TAG","lists.size"+lists.size());
        return lists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title,content,money,time;
        private LinearLayout tableItem;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.rmxm_title);
            content = (TextView) itemView.findViewById(R.id.rmxm_content);
            money = (TextView) itemView.findViewById(R.id.money_weike);
            time = (TextView) itemView.findViewById(R.id.time_weike);
            tableItem = (LinearLayout) itemView.findViewById(R.id.vip_table_item);
        }
    }
}
