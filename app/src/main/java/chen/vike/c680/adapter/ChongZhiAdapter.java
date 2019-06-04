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
 * Created by lht on 2018/9/12.
 */
public class ChongZhiAdapter extends BaseAdapter{


        private Context context;
        private List<GridViewInfoBean> list;

        public ChongZhiAdapter(Context context, List<GridViewInfoBean> list) {
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
                    convertView = LayoutInflater.from(context).inflate(R.layout.activity_chongzhi_item, null);
                }
                id = new ID();
                id.item =  convertView.findViewById(R.id.chongzhi_item_linear);
                id.name =  convertView.findViewById(R.id.chongzhi_money_text);
                convertView.setTag(id);
            } else {
                id = (ID) convertView.getTag();
            }
            if (list!=null && list.size() > 0) {
                id.name.setText(list.get(position).getName()+"元");
            }
            return convertView;
        }

        class ID{
            private LinearLayout item;
            private TextView name;
        }
    }


