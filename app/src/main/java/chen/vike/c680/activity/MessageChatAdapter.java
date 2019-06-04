package chen.vike.c680.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lht.vike.a680_v1.R;

import java.util.ArrayList;
import java.util.List;

import chen.vike.c680.bean.MsgBean;
import chen.vike.c680.views.CircleImageView;

/**
 * Created by lht on 2017/1/23.
 */

public class MessageChatAdapter extends BaseAdapter {

    private List<MsgBean> list_msg = new ArrayList<>();
    private Context context;

    public MessageChatAdapter(Context context, List list) {
        this.context = context;
        this.list_msg = list;
    }

    public void addList(List<MsgBean> list) {
        this.list_msg.addAll(0,list);
    }

    public void addsendList(List<MsgBean> list) {
        this.list_msg.addAll(list);
    }
   public void refreshList(){
        if (this.list_msg != null){
            this.list_msg.clear();
        }
   }
    @Override
    public int getItemViewType(int position) {
        return ((MsgBean)getItem(position)).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override

    public int getCount() {
        if (null == list_msg) {
            return 0;
        }
        return list_msg.size();
    }

    @Override
    public Object getItem(int position) {
        return list_msg.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    ID id;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.getTag() == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
            id = new ID();
            id.tv_left = (TextView) convertView.findViewById(R.id.me);
            id.tv_right = (TextView) convertView.findViewById(R.id.you);
            id.iv_left = (CircleImageView) convertView.findViewById(R.id.iv_me);
            id.iv_right = (CircleImageView) convertView.findViewById(R.id.iv_you);
            id.ll = (LinearLayout) convertView.findViewById(R.id.ll);
            id.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
            convertView.setTag(id);

        } else {
            id = (ID) convertView.getTag();
        }


        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);

        if (getItemViewType(position) == MsgBean.TYPE_SEND) {
            id.rl.setVisibility(View.VISIBLE);
            id.ll.setVisibility(View.GONE);
            Glide.with(context).load(list_msg.get(position).getImageUrl()).apply(options).into(id.iv_right);
            id.tv_right.setText(list_msg.get(position).getContent());
        } else {
            id.ll.setVisibility(View.VISIBLE);
            id.rl.setVisibility(View.GONE);
            Glide.with(context).load(list_msg.get(position).getImageUrl()).apply(options).into(id.iv_left);
            id.tv_left.setText(list_msg.get(position).getContent());
        }

        return convertView;
    }

    class ID{

        private CircleImageView iv_left;
        private CircleImageView iv_right;
        private LinearLayout ll;
        private RelativeLayout rl;
        private TextView tv_left;
        private TextView tv_right;

    }

}
