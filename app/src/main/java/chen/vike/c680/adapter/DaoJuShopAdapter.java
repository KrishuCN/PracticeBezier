package chen.vike.c680.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lht.vike.a680_v1.R;

import java.util.List;

import chen.vike.c680.bean.DaoJuBean;
import chen.vike.c680.Interface.ViewItemClick;
import chen.vike.c680.tools.ImageLoadUtils;

/**
 * Created by lht on 2017/12/6.
 * 道具商城控制器
 */

public class DaoJuShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<DaoJuBean.ItemsBean> lists;
    private ViewItemClick viewItemClick;
    public DaoJuShopAdapter(Context context, List<DaoJuBean.ItemsBean> lists) {
        this.context = context;
        this.lists = lists;
    }
    public void addList(List<DaoJuBean.ItemsBean> dajus){

            Log.e("adpyer",lists.size()+"1213123");


    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
             View view = LayoutInflater.from(context).inflate(R.layout.daoju_shop_item,null);
             DaoJuViewHolder daoJuViewHolder = new DaoJuViewHolder(view);
             return daoJuViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
           DaoJuViewHolder daoJuViewHolder = (DaoJuViewHolder) holder;
          if (daoJuViewHolder.button != null){
              daoJuViewHolder.button.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      getViewItemClick().shortClick(position);
                  }

              });
              daoJuViewHolder.content.setText(lists.get(position).getCont());
              daoJuViewHolder.name.setText(lists.get(position).getName());
              daoJuViewHolder.jiaqian.setText(lists.get(position).getDanjia());
              ImageLoadUtils.Companion.display(context,daoJuViewHolder.imageView,lists.get(position).getImg());
          }
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }
    public class DaoJuViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,content,jiaqian,danwei;
        Button button;
        public DaoJuViewHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.daoju_img);
            name = itemView.findViewById(R.id.daoju_name);
            content =  itemView.findViewById(R.id.daoju_content);
            jiaqian =  itemView.findViewById(R.id.daoju_jiaqian);
            danwei =  itemView.findViewById(R.id.daoju_danwei);
            button = itemView.findViewById(R.id.daoju_goumai);
        }
    }
    public ViewItemClick getViewItemClick() {
        return viewItemClick;
    }

    public void setViewItemClick(ViewItemClick viewItemClick) {
        this.viewItemClick = viewItemClick;
    }
}
