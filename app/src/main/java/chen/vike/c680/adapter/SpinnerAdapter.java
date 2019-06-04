package chen.vike.c680.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lht.vike.a680_v1.R;

import java.util.List;

import chen.vike.c680.Interface.ViewItemClick;

/**
 * Created by lht on 2018/5/15.
 */

public class SpinnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mList;
    private Context mContext;
    private ViewItemClick viewItemClick;
    public SpinnerAdapter(Context pContext, List<String> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    public ViewItemClick getViewItemClick() {
        return viewItemClick;
    }

    public void setViewItemClick(ViewItemClick viewItemClick) {
        this.viewItemClick = viewItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spinner_item_xianshi,null);
        KuaiViewHolder kuaiViewHolder = new KuaiViewHolder(view);
        return kuaiViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
         KuaiViewHolder kuaiViewHolder = (KuaiViewHolder) holder;
        if (kuaiViewHolder.xuqiuContent != null){
            kuaiViewHolder.xuqiuContent.setText("· "+mList.get(position));
            kuaiViewHolder.xuqiuContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getViewItemClick().shortClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    private class KuaiViewHolder extends RecyclerView.ViewHolder {
        private TextView xuqiuContent;
        public KuaiViewHolder(View itemView) {
            super(itemView);
            xuqiuContent = itemView.findViewById(R.id.text_item);
        }
    }
}
