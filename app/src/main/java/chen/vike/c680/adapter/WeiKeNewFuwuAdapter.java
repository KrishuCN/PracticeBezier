package chen.vike.c680.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lht.vike.a680_v1.R;

import java.util.List;

import chen.vike.c680.Interface.ViewItemClick;
import chen.vike.c680.bean.WeikeBean;

/**
 * Created by lht on 2017/10/26.
 * 最新入驻服务商列表
 */

public class WeiKeNewFuwuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<WeikeBean.ZuixinruzhuListBean> lists;
    private Context context;
    private ViewItemClick viewItemClick;

    public WeiKeNewFuwuAdapter(List<WeikeBean.ZuixinruzhuListBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }
    public void refresh() {
        lists.clear();
    }

    public void addlist(List<WeikeBean.ZuixinruzhuListBean> list) {

        lists.addAll(list);

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_fuwu_item,null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (myViewHolder.linearLayout != null){
            myViewHolder.textView.setText(lists.get(position).getVkname());
            Glide.with(context).load(lists.get(position).getImageurl()).into(myViewHolder.imageView);
            myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getViewItemClick().shortClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private ImageView imageView;
        private TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.weike_fuwushang);
            imageView = (ImageView) itemView.findViewById(R.id.new_fuwu_iv_item);
            textView = (TextView) itemView.findViewById(R.id.new_fuwu_tv_item);
        }
    }

    public ViewItemClick getViewItemClick() {
        return viewItemClick;
    }

    public void setViewItemClick(ViewItemClick viewItemClick) {
        this.viewItemClick = viewItemClick;
    }
}
