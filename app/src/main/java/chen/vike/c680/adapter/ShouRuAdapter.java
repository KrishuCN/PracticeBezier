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

import chen.vike.c680.bean.CaiFuBean;

/**
 * Created by lht on 2017/12/13.
 */

public class ShouRuAdapter  extends BaseAdapter{
    private Context context;
    private List<CaiFuBean.ListBean> list_caifu = new ArrayList<>();
    private List<CaiFuBean.ListBean> list_shouru = new ArrayList<>();
    public ShouRuAdapter(Context context, List<CaiFuBean.ListBean> list_caifu) {
        this.context = context;
        this.list_caifu = list_caifu;
    }
    public void addlist(List<CaiFuBean.ListBean> list) {

        list_caifu.addAll(list);

    }

    public void refresh() {
        list_caifu.clear();
    }

    @Override
    public int getCount() {
        return list_caifu.size();
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

                convertView = LayoutInflater.from(context).inflate(R.layout.caifu_jilu_item, null);
                id = new ID();
                id.tv_title = (TextView) convertView.findViewById(R.id.caifu_title);
                id.tv_money = (TextView) convertView.findViewById(R.id.caifu_jine);
                id.tv_time = (TextView) convertView.findViewById(R.id.caifu_time);
                convertView.setTag(id);

            } else {

                id = (ID) convertView.getTag();

            }

            if (list_caifu != null && list_caifu.size() > 0) {

                switch (list_caifu.get(position).getSou_zhi()){
                    case "1":
                        id.tv_money.setText("收入"+"+"+list_caifu.get(position).getMoney());
                        id.tv_title.setText(list_caifu.get(position).getBeizhu());
                        id.tv_time.setText(list_caifu.get(position).getAddtime());
                        break;
                    default:
                        break;
                }


            }
        }
        return convertView;
    }
    class  ID{
        private TextView tv_title;
        private TextView tv_money;
        private TextView tv_time;

    }
}
