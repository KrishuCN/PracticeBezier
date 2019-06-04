package chen.vike.c680.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwenfeng.library.pulltorefresh.view.HeadView;
import com.lht.vike.a680_v1.R;


public class WeiKeNormalHeadView extends FrameLayout implements HeadView {

    private TextView tv;
    private ImageView img = null;
    private Animation animation1 = null;

    public WeiKeNormalHeadView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_normal_head, null);
        tv = (TextView) view.findViewById(R.id.normal_head_text);
        img = (ImageView) view.findViewById(R.id.normal_head_img);
        animation1 = AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
        addView(view);
    }

    @Override
    public void begin() {

    }

    @Override
    public void progress(float progress, float all) {
        if (progress >= all - 10) {
            tv.setText("松开刷新");
            img.setAnimation(animation1);
        } else {
            tv.setText("下拉加载");
            img.setImageResource(R.mipmap.pullup_icon_big);
        }
    }

    @Override
    public void finishing(float progress, float all) {

    }

    @Override
    public void loading() {
        tv.setText("刷新中...");
        img.setVisibility(GONE);

    }

    @Override
    public void normal() {
        tv.setText("下拉");
    }

    @Override
    public View getView() {
        return this;
    }
}
