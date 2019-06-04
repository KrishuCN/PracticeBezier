package chen.vike.c680.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.lht.vike.a680_v1.R;

import java.util.List;

/**
 * Created by Administrator on 2016-09-26.
 */
public class VipViewAnimatorWordComponent extends RelativeLayout {
    private ViewAnimator viewAnimator;
    private final int MSG_CODE = 0x667;
    private final int TIMER_INTERVAL = 3000;
    private List<String> strings;

    public void setStrings(List<String> strings, int color) {
        this.strings = strings;
        if(strings != null){
            for (int i = 0; i < strings.size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setText(strings.get(i));
                textView.setTextColor(getResources().getColor(color));
                //任意设置你的文字样式，在这里
                viewAnimator.addView(textView,i);
            }
        }
    }

    public VipViewAnimatorWordComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewAnimator = new ViewAnimator(getContext());
        viewAnimator.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(viewAnimator);
        Message message = handler.obtainMessage(MSG_CODE);
        handler.sendMessageDelayed(message,TIMER_INTERVAL);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == MSG_CODE){
                viewAnimator.setOutAnimation(getContext(), R.anim.slide_out_up);
                viewAnimator.setInAnimation(getContext(),R.anim.slide_in_down);
                viewAnimator.showNext();
                Message message = handler.obtainMessage(MSG_CODE);
                handler.sendMessageDelayed(message,TIMER_INTERVAL);
            }
        }
    };

}
