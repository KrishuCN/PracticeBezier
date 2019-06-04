package chen.vike.c680.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import chen.vike.c680.activity.TaskDetailsActivity;
import chen.vike.c680.bean.WeiKePageGson;
import com.lht.vike.a680_v1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lht on 2017/2/28.
 */

public class VipMoreAdapter extends BaseAdapter {

    private Context context;
    private List<WeiKePageGson.ItemListBean> list_info=new ArrayList<>();
    private String type;

    public VipMoreAdapter(Context context, List<WeiKePageGson.ItemListBean> list) {
        this.context = context;
        this.list_info = list;
    }

    public void addlist(List<WeiKePageGson.ItemListBean> list) {
        list_info.addAll(list);
    }

    public void refresh() {
        list_info.clear();
    }

    @Override
    public int getCount() {
        return list_info.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    ID id;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_rmxm, null);
            id = new ID();
            id.tv = (TextView) convertView.findViewById(R.id.rmxm_title);
            id.tv1 = (TextView) convertView.findViewById(R.id.rmxm_content);
            id.tv2 = (TextView) convertView.findViewById(R.id.money_weike);
            id.tv3 = (TextView) convertView.findViewById(R.id.time_weike);
            id.tv4 = (TextView) convertView.findViewById(R.id.type_weike);
            convertView.setTag(id);
        } else {
            id = (ID) convertView.getTag();
        }

        id.tv.setText(list_info.get(position).getItemname());
        id.tv1.setText(list_info.get(position).getContent());
        id.tv2.setText("￥"+list_info.get(position).getPrice());
        id.tv3.setText(list_info.get(position).getEndtime());
        switch (list_info.get(position).getItemtype()) {
            case "xuanshang":
                type = "悬赏";
                break;
            case "zhaobiao":
                type = "招标";
                break;
            case "jijian":
                type = "计件";
                break;
            default:
                type = "悬赏";
                break;
        }
        id.tv4.setText(type);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, TaskDetailsActivity.class);
                in.putExtra("ID", list_info.get(position).getItemid());
                context.startActivity(in);
            }
        });

        return convertView;
    }

    class ID{
        private TextView tv;
        private TextView tv1;
        private TextView tv2;
        private TextView tv3;
        private TextView tv4;
    }
}
