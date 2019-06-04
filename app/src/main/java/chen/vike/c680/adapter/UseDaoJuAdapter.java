package chen.vike.c680.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lht.vike.a680_v1.R;

import java.util.ArrayList;
import java.util.List;

import chen.vike.c680.bean.UseDaoJuBean;

/**
 * Created by lht on 2017/12/22.
 *道具使用记录
 */

public class UseDaoJuAdapter extends BaseAdapter {
    private Context context;
    private List<UseDaoJuBean.ListBean> lists = new ArrayList<>();

    public UseDaoJuAdapter(Context context) {
        this.context = context;
    }

    public void addList(List<UseDaoJuBean.ListBean> list) {
        this.lists.addAll(list);
    }

    public void refresh() {
        lists.clear();
    }


    @Override
    public int getCount() {
        return lists.size();
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

        if (context != null) {
            if (convertView == null || convertView.getTag() == null) {

                convertView = LayoutInflater.from(context).inflate(R.layout.use_daoju_item, null);
                id = new ID();
                id.tv_title =  convertView.findViewById(R.id.use_daoju_title);
                id.tv_time =  convertView.findViewById(R.id.use_daoju_time);
                convertView.setTag(id);

            } else {
                id = (ID) convertView.getTag();
            }
            if (lists!=null && lists.size() > 0){
                id.tv_title.setText(lists.get(position).getRbeizhu());
                id.tv_time.setText(lists.get(position).getRtime());
            }
        }
        return convertView;
    }

    class  ID{
        private TextView tv_title;
        private TextView tv_time;

    }
}
