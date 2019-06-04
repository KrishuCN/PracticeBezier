package chen.vike.c680.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lht.vike.a680_v1.R;

import java.util.ArrayList;
import java.util.List;

import chen.vike.c680.bean.IpropBean;
import chen.vike.c680.Interface.ViewItemClick;

/**
 * Created by lht on 2017/12/11.
 */

public class IpropAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IpropBean.ItemListBean> lists = new ArrayList<>();
    private Context context;
    private ViewItemClick ipropClick;
    public IpropAdapter(List<IpropBean.ItemListBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }
   public void addList(List<IpropBean.ItemListBean> list){
         lists.addAll(list);
   }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.mine_prop_item, null);
            IpropHolder ipropHolder = new IpropHolder(view);
            return ipropHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        IpropHolder ipropHolder = (IpropHolder) holder;
        if (lists.size() > 0){
            if (ipropHolder.button != null){
                ipropHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       getIpropClick().shortClick(position);
                    }

                });

                    ipropHolder.content.setText(lists.get(position).getCont());
                    ipropHolder.name.setText(lists.get(position).getName());
                    ipropHolder.number.setText(lists.get(position).getMydjnum());
                    Glide.with(context).load(lists.get(position).getImg()).into(ipropHolder.imageView);

            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
   private class IpropHolder extends RecyclerView.ViewHolder {
       ImageView imageView;
       TextView name,content,number,danwei;
       Button button;
       public IpropHolder(View itemView) {
           super(itemView);
           imageView = (ImageView) itemView.findViewById(R.id.i_daoju_img);
           name = (TextView) itemView.findViewById(R.id.i_daoju_name);
           content = (TextView) itemView.findViewById(R.id.i_daoju_content);
           number = (TextView) itemView.findViewById(R.id.i_daoju_quantity);
           danwei = (TextView) itemView.findViewById(R.id.i_daoju_danwei);
           button = (Button) itemView.findViewById(R.id.i_daoju_shiyong);
       }
   }

    public ViewItemClick getIpropClick() {
        return ipropClick;
    }

    public void setIpropClick(ViewItemClick ipropClick) {
        this.ipropClick = ipropClick;
    }
}
