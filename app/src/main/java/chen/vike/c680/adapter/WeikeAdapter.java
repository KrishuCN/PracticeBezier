package chen.vike.c680.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import chen.vike.c680.bean.WeiKeGridViewWKInfoBean;
import com.lht.vike.a680_v1.R;

import java.util.List;

import chen.vike.c680.Interface.ViewItemClick;

/**
 * Created by lht on 2017/10/25.
 */

public class WeikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
     private List<WeiKeGridViewWKInfoBean> lists;
     private Context context;
     private ViewItemClick viewItemClick;
    public WeikeAdapter(List<WeiKeGridViewWKInfoBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gridview_item_weike,null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (myViewHolder.img != null){
            myViewHolder.tv.setText(lists.get(position).getName());
            myViewHolder.img.setBackgroundResource(lists.get(position).getImage());
            myViewHolder.img.setOnClickListener(new View.OnClickListener() {
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
  public class MyViewHolder extends RecyclerView.ViewHolder {
      private TextView tv;
      private ImageView img;

      public MyViewHolder(View itemView) {
          super(itemView);
            tv = (TextView) itemView.findViewById(R.id.gv_tv_item);
            img = (ImageView) itemView.findViewById(R.id.gv_iv_item);
      }
  }

    public List<WeiKeGridViewWKInfoBean> getLists() {
        return lists;
    }

    public void setLists(List<WeiKeGridViewWKInfoBean> lists) {
        this.lists = lists;
    }

    public ViewItemClick getViewItemClick() {
        return viewItemClick;
    }

    public void setViewItemClick(ViewItemClick viewItemClick) {
        this.viewItemClick = viewItemClick;
    }
}
