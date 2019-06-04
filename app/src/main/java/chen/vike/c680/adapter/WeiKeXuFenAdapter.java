package chen.vike.c680.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import chen.vike.c680.bean.GridViewInfoBean;
import com.lht.vike.a680_v1.R;

import java.util.List;

/**
 * Created by lht on 2018/4/8.
 */

public class WeiKeXuFenAdapter extends BaseAdapter {
    private Context context;
    private List<GridViewInfoBean> list;

    public WeiKeXuFenAdapter(Context context, List<GridViewInfoBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list.size() == 0) {
            return 0;
        }
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

    private ID id;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.getTag() == null) {
            if (context!=null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.weike_page_xuqiu_item_item, null);
            }
            id = new ID();
            id.name = (TextView) convertView.findViewById(R.id.xuqiu_fen_title);
            convertView.setTag(id);
        } else {
            id = (ID) convertView.getTag();
        }
        if (list!=null && list.size() > 0) {
            id.name.setText(list.get(position).getName());
        }
        return convertView;
    }

    class ID{
        private TextView name;
    }
}
