package chen.vike.c680.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lht.vike.a680_v1.R;

import java.util.List;

import chen.vike.c680.activity.ShopDetailsActivity;
import chen.vike.c680.bean.WeiKeModel01Bean;
import chen.vike.c680.views.WeiKeVerticalBannerView;

/**
 */
public class WeiKeSampleAdapter03 extends WeiKeBaseBannerAdapter<WeiKeModel01Bean> {
    private List<WeiKeModel01Bean> mDatas;
    private Context context;

    public WeiKeSampleAdapter03(List<WeiKeModel01Bean> datas, Context context) {
        super(datas);
        this.context = context;
        this.mDatas = datas;
    }
    @Override
    public View getView(WeiKeVerticalBannerView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_03,null);
    }

    @Override
    public void setItem(final View view, final WeiKeModel01Bean data) {
        TextView tv = (TextView) view.findViewById(R.id.title);

                    tv.setText(data.title);
         view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent in = new Intent(context, ShopDetailsActivity.class);
                 in.putExtra("ID",data.url);
                 context.startActivity(in);
             }
         });
    }
}
