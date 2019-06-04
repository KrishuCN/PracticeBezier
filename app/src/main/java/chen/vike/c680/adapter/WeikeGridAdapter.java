package chen.vike.c680.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import chen.vike.c680.bean.WeiKeGridViewWKInfoBean;
import com.lht.vike.a680_v1.R;

import java.util.List;

/**
 * Created by lht on 2017/10/25.
 */

public class WeikeGridAdapter extends BaseAdapter {
    private List<WeiKeGridViewWKInfoBean> list;
    private Context con;

    public WeikeGridAdapter(List<WeiKeGridViewWKInfoBean> list, Context con) {
        this.list = list;
        this.con = con;
    }

    @Override
    public int getCount() {
        return list.size();
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

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(con).inflate(R.layout.gridview_item_weike, null);
            id = new ID();
            id.imageView = (ImageView) convertView.findViewById(R.id.gv_iv_item);
            id.textView = (TextView) convertView.findViewById(R.id.gv_tv_item);
            convertView.setTag(id);
        } else {
            id = (ID) convertView.getTag();
        }

        id.imageView.setImageResource(list.get(position).getImage());
        id.textView.setText(list.get(position).getName());

        return convertView;
    }

    class ID {

        private ImageView imageView;
        private TextView textView;


    }
}
