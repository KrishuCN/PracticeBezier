package chen.vike.c680.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import chen.vike.c680.webview.WebViewActivity;
import com.lht.vike.a680_v1.R;

import java.util.List;

import chen.vike.c680.bean.WeiKeModel01Bean;
import chen.vike.c680.views.WeiKeVerticalBannerView;

/**
 * Created by lht on 2018/6/6.
 */

public class LunBoAdapter extends WeiKeBaseBannerAdapter<WeiKeModel01Bean> {


    private List<WeiKeModel01Bean> mDatas;
    private Context context;

    public LunBoAdapter(List<WeiKeModel01Bean> datas, Context context) {
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
                Intent in = new Intent(context, WebViewActivity.class);
                in.putExtra("weburl",data.url);
                in.putExtra("title",data.biaoti);
                context.startActivity(in);
            }
        });
    }
}
