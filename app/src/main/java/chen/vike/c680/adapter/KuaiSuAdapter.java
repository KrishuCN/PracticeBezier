package chen.vike.c680.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import chen.vike.c680.bean.GridViewInfoBean;
import com.lht.vike.a680_v1.R;

import java.util.List;

/**
 * Created by lht on 2018/6/5.
 */

public class KuaiSuAdapter extends BaseAdapter{
    private Context context;
    private List<GridViewInfoBean> list;


    private List<Boolean> booleanList;
    private int location;

    public KuaiSuAdapter(Context context, List<GridViewInfoBean> list, List<Boolean> booleanList) {
        this.context = context;
        this.list = list;
        this.booleanList = booleanList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.getTag() == null) {
            if (context!=null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.fubu_xueqiu_kuai_item,null);
            }
            id = new ID();
            id.item =  convertView.findViewById(R.id.kuaisu_item_linear);
            id.name =  convertView.findViewById(R.id.kuaisu_title_text);
            convertView.setTag(id);
        } else {
            id = (ID) convertView.getTag();
        }
        if (list!=null && list.size() > 0) {
            id.name.setText(list.get(position).getName()+"");
        }

            if (location == position) {
                id.name.setTextColor(context.getResources().getColor(R.color.white));
                id.item.setBackgroundResource(R.drawable.kuai_biaoqian_bg);
            }
        else {

                id.name.setTextColor(context.getResources().getColor(R.color.text_color_1));
                id.item.setBackgroundResource(R.drawable.edit_bg_color);

        }
        return convertView;
    }

    class ID{
        private LinearLayout item;
        private TextView name;
    }

    public void setSeclection(int position) {
        location = position;
    }
}
