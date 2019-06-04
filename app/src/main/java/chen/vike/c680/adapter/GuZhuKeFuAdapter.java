package chen.vike.c680.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lht.vike.a680_v1.R;

import java.util.List;

import chen.vike.c680.bean.GridViewInfoBean;
import chen.vike.c680.views.CircleImageView;

/**
 * Created by lht on 2018/5/24.
 * 雇主客服Adapter
 */

public class GuZhuKeFuAdapter extends BaseAdapter {
    private Context context;
    private List<GridViewInfoBean> list;

    public GuZhuKeFuAdapter(Context context, List<GridViewInfoBean> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.guzhu_kefu_item_item, null);
            }
            id = new ID();
            id.image =  convertView.findViewById(R.id.kefu_item_item_img);
            id.name = convertView.findViewById(R.id.kefu_item_item_name);
            convertView.setTag(id);
        } else {
            id = (ID) convertView.getTag();
        }
        if (list!=null && list.size() > 0) {

            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.image_loading)
                    .error(R.mipmap.image_erroe)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(context)
                    .load(list.get(position).getImgUrl())
                    .apply(options)
                    .into(id.image);

            id.name.setText(list.get(position).getName());
        }
        return convertView;
    }
    class ID{
        private CircleImageView image;
        private TextView name;
    }
}
