package chen.vike.c680.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwenfeng.library.pulltorefresh.view.FooterView;
import com.lht.vike.a680_v1.R;

public class WeiKeNormalFooterView extends FrameLayout implements FooterView {

    private TextView tv;
    private ImageView normal_footer_img= null;
           // , normal_footer_img_one, normal_footer_img_two
   // private Animation animation = null;
    private Animation animation1 = null;


    public WeiKeNormalFooterView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public WeiKeNormalFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WeiKeNormalFooterView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_normal_footer, null);
        addView(view);
        tv = (TextView) view.findViewById(R.id.normal_footer_text);
        normal_footer_img = (ImageView) view.findViewById(R.id.normal_footer_img);
        animation1 = AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
    }

    @Override
    public void begin() {

    }

    @Override
    public void progress(float progress, float all) {
        if (progress >= all - 10) {
            tv.setText("松开加载更多");
            normal_footer_img.setAnimation(animation1);
        } else {
            tv.setText("上拉加载");
            normal_footer_img.setBackgroundResource(R.mipmap.pullup_icon_big);
        }
    }

    @Override
    public void finishing(float progress, float all) {

    }

    @Override
    public void loading() {
        tv.setText("加载中...");
        normal_footer_img.setVisibility(GONE);
    }

    @Override
    public void normal() {
        tv.setText("上拉加载");


    }

    @Override
    public View getView() {
        return this;
    }
}
