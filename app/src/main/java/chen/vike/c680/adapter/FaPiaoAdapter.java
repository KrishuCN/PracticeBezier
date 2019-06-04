package chen.vike.c680.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lht.vike.a680_v1.R;

import java.util.ArrayList;
import java.util.List;

import chen.vike.c680.bean.PersonFaPiaoBean;
import chen.vike.c680.Interface.ViewItemClick;

/**
 * Created by lht on 2017/12/8.
 */

public class FaPiaoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<PersonFaPiaoBean.ItemsBean> lists = new ArrayList<>();
    private ViewItemClick faPiaoClick,eidtClick;
    public FaPiaoAdapter(Context context, List<PersonFaPiaoBean.ItemsBean> lists) {
        this.context = context;
        this.lists = lists;
    }
    public void addlist(List<PersonFaPiaoBean.ItemsBean> list) {

        lists.addAll(list);

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fapiao_guanli_item,null);
        FaPiaoHolder faPiaoHolder = new FaPiaoHolder(view);
        return faPiaoHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
           FaPiaoHolder faPiaoHolder = (FaPiaoHolder) holder;
        faPiaoHolder.cance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFaPiaoClick().shortClick(position);
            }
        });
        if (faPiaoHolder.content != null) {
            faPiaoHolder.content.setText("发票内容："+lists.get(position).getExplain() + "");
            faPiaoHolder.title.setText(lists.get(position).getItemname());
            faPiaoHolder.number.setText("项目编号:"+lists.get(position).getItemid());
            faPiaoHolder.amount.setText(lists.get(position).getMoney());
            faPiaoHolder.address.setText(lists.get(position).getCtime());
            faPiaoHolder.state.setText(lists.get(position).getState());
        }
        if (lists.get(position).getState().equals("已办理")){
                 faPiaoHolder.cance.setVisibility(View.GONE);
                 faPiaoHolder.edit.setVisibility(View.GONE);
                 faPiaoHolder.state.setBackgroundResource(R.drawable.fapiao_zt_btn);
                 faPiaoHolder.state.setTextColor(Color.parseColor("#ffffff"));
        }
        faPiaoHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEidtClick().shortClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    private class FaPiaoHolder extends RecyclerView.ViewHolder {
        private TextView content,title,number,amount,address;
        private Button state;
        private ImageView edit,cance;
        public FaPiaoHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.fapiao_neirong);
            title = (TextView) itemView.findViewById(R.id.fapiao_title);
            number = (TextView) itemView.findViewById(R.id.fapiao_bianhao);
            amount = (TextView) itemView.findViewById(R.id.fapiao_jine);
            address = (TextView) itemView.findViewById(R.id.fapiao_address);
            state = (Button) itemView.findViewById(R.id.fapiao_zhuangtai);
            edit = (ImageView) itemView.findViewById(R.id.fapiao_bianji);
            cance = (ImageView) itemView.findViewById(R.id.fapiao_quxiao);
        }
    }

    public ViewItemClick getFaPiaoClick() {
        return faPiaoClick;
    }

    public void setFaPiaoClick(ViewItemClick faPiaoClick) {
        this.faPiaoClick = faPiaoClick;
    }
    public void shanchu(int position){
        lists.remove(position);
        notifyItemRemoved(position);
        if (position != lists.size()) {
            notifyItemRangeChanged(position, lists.size() - position);
        }
    }

    public ViewItemClick getEidtClick() {
        return eidtClick;
    }

    public void setEidtClick(ViewItemClick eidtClick) {
        this.eidtClick = eidtClick;
    }
}
