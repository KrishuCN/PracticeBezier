package chen.vike.c680.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lht.vike.a680_v1.R;

import java.util.ArrayList;
import java.util.List;

import chen.vike.c680.Interface.ViewItemClick;
import chen.vike.c680.bean.TuiSongFuwuSBean;
import chen.vike.c680.tools.ImageLoadUtils;

/**
 * Created by lht on 2018/3/6.
 */

public class FuWuShangAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<TuiSongFuwuSBean.ListBean> list = new ArrayList<>();
    private Context context;
    private View view;
    private ViewItemClick viewItemClick;
    public FuWuShangAdapter( Context context) {
        this.context = context;
    }
    public void addfulist(List<TuiSongFuwuSBean.ListBean> listfu){
        list.addAll(listfu);
    }
    public void refresh(){
        if (list.size() >0) {
            list.clear();
        }
    }

    public ViewItemClick getViewItemClick() {
        return viewItemClick;
    }

    public void setViewItemClick(ViewItemClick viewItemClick) {
        this.viewItemClick = viewItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.person_fuwus_list_item,null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
         MyViewHolder myViewHolder = (MyViewHolder) holder;
        if (myViewHolder.fuwuImage != null){
            ImageLoadUtils.Companion.display(context,myViewHolder.fuwuImage,list.get(position).getImageurl());
            ImageLoadUtils.Companion.display(context,myViewHolder.fuwudengjiImg,list.get(position).getCheck_imgurl());
            myViewHolder.fuwuName.setText(list.get(position).getShopname()+"");
            myViewHolder.fuwuDi.setText(list.get(position).getCityname()+"");
            myViewHolder.fuwuHao.setText("好评:"+list.get(position).getGoodval()+"");
            myViewHolder.fuwucjl.setText("成交"+list.get(position).getCjnum()+"笔");
            myViewHolder.fuwusclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   getViewItemClick().shortClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView fuwuImage,fuwudengjiImg;
        private TextView fuwuName,fuwucjl,fuwuDi,fuwuHao;
        private LinearLayout fuwusclick;
        public MyViewHolder(View itemView) {
            super(itemView);
            fuwuImage = (ImageView) itemView.findViewById(R.id.fuwu_image);
            fuwudengjiImg = (ImageView) itemView.findViewById(R.id.fuwu_dengji);
            fuwuName = (TextView) itemView.findViewById(R.id.fuwus_name);
            fuwucjl = (TextView) itemView.findViewById(R.id.fuwu_chengjliang);
            fuwuDi = (TextView) itemView.findViewById(R.id.fuwu_fizhi);
            fuwuHao = (TextView) itemView.findViewById(R.id.fuwu_haopinglue);
            fuwusclick = (LinearLayout) itemView.findViewById(R.id.click_fuwus);
        }
    }
}
